package ax.bru.act

import akka.actor.{Props, ActorSystem, ActorRef, Actor}
import akka.event.LoggingReceive
import org.eintr.loglady.Logging

//import scala.concurrent.Await
//import akka.pattern.ask

import akka.util.Timeout
import scala.concurrent.duration._

class ActionSupervisor extends Actions {

  // contains the system wide defined 'top level' action names and the actor ref to the first actor for each action
  var actions: Map[String, ActorRef] = Map()

  def receive: Actor.Receive = LoggingReceive {

    case Add(action) =>
      addActor(action)

    case Perform(actionName, map) =>
      val actor = actions.get(actionName)
      if (actor.isDefined) {
        actor.get ! Message(map)
      }

    case message =>
      log.error(s"wrong message type: $message")

  }

  override def storeActor(name: String, actor: ActorRef) {
    actions = actions.updated(name, actor)
  }

}

trait Actions extends Actor with Logging {

  def addActor(action: Action) {
    val actorRef = createActor(action)
    storeActor(action.name, actorRef)
  }

  // create all actors for this action and return the first actors ref
  private def createActor(action: Action): ActorRef = {

    log.debug(s"creating actor for $action")
    val actionActor = context.actorOf(Props[ActionActor], action.id)

    if (action.hasSteps) {
      //TODO check whether already exists in context (testcase with reusing action as part of another)
      actionActor ! AddSteps(action.steps)
    } else {
      actionActor ! AddFunction(action.function)
    }

    actionActor

  }

  def storeActor(name: String, actor: ActorRef)
}

object ActionSystem extends Logging {

  val timeout = 2 seconds

  private val system = ActorSystem("Actions")

  val actionSupervisor = system.actorOf(Props[ActionSupervisor], "ActionSupervisor")

  def addAction(action: Action) {
    actionSupervisor ! Add(action)
  }

  def perform(action: String, data: Pair[String, Any]*) {
    actionSupervisor ! Perform(action, data.toMap)
  }

  //  def <-- (actor: ActorRef, message: Message): Option[Message] = {
  //    implicit val timeout = Timeout(ActionSystem.timeout)
  //    val future = actor ? message
  //    Await.result(future, timeout.duration).asInstanceOf[Option[Message]]
  //  }

}


case class Add(action: Action)

case class Perform(actionName: String, data: Map[String, Any])

case class AddSteps(steps: List[Step])

case class AddFunction(function: (Data) => Unit)

case class Link(actor: ActorRef)


class StepActor extends Actions {

  var action: ActorRef = null

  var nextStep: ActorRef = null

  def receive: Actor.Receive = LoggingReceive {
    case Add(action) => {
      log.debug(s"setting $action for step ${self.path}")
      if (action != null) { // todo necessary check?
        addActor(action)
      }
    }
    case Link(actor) =>
      log.debug(s"${self.path} has now next step ${actor.path}")
      nextStep = actor
    case message: Message =>
      log.debug(s"${self.path} received message.")

      if (action != null) {
        action ! message // todo action needs to know next step to go to once complete
      } else if (nextStep != null) {
        nextStep ! message
      }

    case _ => throw new Error("invalid message received")
  }

  override def storeActor(name: String, actor: ActorRef) {
    action = actor
  }

}


class ActionActor extends Actor with Logging {
  var steps: List[ActorRef] = List()
  var function: (Data) => Unit = null
  var nextStep: ActorRef = null

  def receive: Actor.Receive = LoggingReceive {
    case AddSteps(steps) => {
      for (step <- steps) {
        val actorRef = createActor(step)
        //link pervious one to this one
        linkLastToThis(actorRef)
        storeActor(actorRef)
      }
    }
    case Link(actor) =>
      log.debug(s"${self.path} has now next step ${actor.path}")
      nextStep = actor // todo get rid of this and link direct?
      linkLastToThis(nextStep)
    case AddFunction(function) => this.function = function
    case message: Message =>
      val data = message.getAll
      log.debug(s"received message with $data at ${self.path}")
      if (steps.size > 0) {
        // todo parallel case
        steps(0) ! message
      } else if (function != null) {
        log.debug("executing function...")
        function(message)
        if (nextStep != null) {
          nextStep ! message
        }
      }
    case msg => log.debug(s"$msg"); throw new Error(s"invalid message received: $msg")
  }

  def createActor(step: Step): ActorRef = {

    log.debug(s"creating actor for $step")
    val stepActor = context.actorOf(Props[StepActor], step.id)

    if (step.hasAction) {
      stepActor ! Add(step.action)
    }

    stepActor

  }

  def linkLastToThis(actor: ActorRef) {
    if (steps.size > 0) {
      steps.last ! Link(actor)
    }
  }

  def storeActor(actor: ActorRef) {
    steps = steps ::: List(actor)
  }


}

case class Message(var map: Map[String, Any]) extends Data {
  def set(key: String, value: Any): Unit = map = map.updated(key, value)

  def get(key: String): Any = map.get(key)

  def getAll: Map[String, Any] = map
}