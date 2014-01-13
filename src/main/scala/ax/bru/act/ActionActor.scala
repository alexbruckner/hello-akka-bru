package ax.bru.act

import akka.actor.{Props, ActorSystem, ActorRef, Actor}
import akka.event.LoggingReceive
import org.eintr.loglady.Logging

import scala.concurrent.Await
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._

class ActionSupervisor extends Actor with Logging  {

  // contains the system wide defined 'top level' action names and the actor ref to the first actor for each action
  var actions: Map[String, ActorRef] = Map()

  def receive: Actor.Receive = LoggingReceive {

    case Add(action) =>
      val actorRef = createActor(action)
      storeActor(action.name, actorRef)

    case Get(actionName) => {
      log.debug(s"Get($actionName)")
      val ref = actions.get(actionName)
      log.debug(s"found ref $ref")
      sender ! ref
    }

    case message =>
      log.error(s"wrong message type: $message")

  }

  // create all actors for this action and return the first actors ref
  private def createActor(action: Action): ActorRef = {

    log.debug(s"creating actor for $action")
    val actionActor = context.actorOf(Props[ActionActor])

    if (action.hasSteps) {
      actionActor ! AddSteps(action.steps)
    } else {
      actionActor ! AddFunction(action.function)
    }

    actionActor

  }

  private def storeActor(name: String, actor: ActorRef) {
    actions = actions.updated(name, actor)
  }

}

object ActionSystem extends Logging {

  private val system = ActorSystem("Actions")

  val actionSupervisor = system.actorOf(Props[ActionSupervisor], "ActionSupervisor")

  def addAction(action: Action) {
    actionSupervisor ! Add(action)
  }

  def getActor(action: String): Option[ActorRef] = {
    log.debug(s"getting actor ref for $action")
    implicit val timeout = Timeout(5 seconds)
    val future = actionSupervisor ? Get(action)
    log.debug("awaiting result")
    val result = Await.result(future, timeout.duration).asInstanceOf[Option[ActorRef]]
    log.debug(s"got result $result")
    result
  }

  def perform(action: String, data: Pair[String, Any] *) {
    val actor = getActor(action)
    if (actor.isDefined) {
      println("action is defined!")
      actor.get ! Message(data.toMap)
    } else {
      log.error(s"$action is not defined.")
    }
  }

}


case class Get(action: String)
case class Add(action: Action)
case class AddSteps(steps: List[Step])
case class AddFunction(function: (Data) => Unit)

class StepActor extends Actor {
  def receive: Actor.Receive = LoggingReceive {
    case Add(action) => {
      println(s"adding ACTION $action to $this ... TODO!!!!!")
    }
    case _ => throw new Error("invalid message received")
    null
  }
}


class ActionActor extends Actor with Logging {
  var steps: List[ActorRef] = List()
  var function: (Data) => Unit = null

  def receive: Actor.Receive = LoggingReceive {
    case AddSteps(steps) => {
      for (step <- steps){
        val actorRef = createActor(step)
        storeActor(actorRef)
      }
    }
    case AddFunction(function) => this.function = function
    case message: Message =>
      val data = message.getAll
      println(s"received data $data for this action with steps: $steps")
      if (function != null) {
        log.debug("executing function...")
        function(message)
      }
    case msg => println(msg); throw new Error(s"invalid message received: $msg")
  }

  def createActor(step: Step): ActorRef = {

    log.debug(s"creating actor for $step")
    val stepActor = context.actorOf(Props[StepActor])

    if (step.hasAction) {
      stepActor ! Add(step.action)
    }

    stepActor

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