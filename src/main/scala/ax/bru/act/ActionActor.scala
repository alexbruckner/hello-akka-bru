package ax.bru.act

import akka.actor.{Props, ActorSystem, ActorRef, Actor}
import akka.event.LoggingReceive
import org.eintr.loglady.Logging

import scala.concurrent.Await
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._

class ActionSupervisor extends Actor with Logging {

  // contains the system wide defined 'top level' action names and the actor ref to the first actor for each action
  var actions: Map[String, ActorRef] = Map()

  def receive: Actor.Receive = LoggingReceive {

    case Add(action) =>
      val actorRef = createActor(action)
      storeActor(action.name, actorRef)

    case Get(action) => {
      log.debug(s"Get($action)")
      val ref = actions.get(action)
      log.debug(s"found ref $ref")
      sender ! ref
    }

    case message =>
      log.error(s"wrong message type: $message")

  }

  // create all actors for this action and return the first actors ref
  def createActor(action: Action): ActorRef = {

    log.debug(s"creating actor for $action")
    val actionActor = context.actorOf(Props[ActionActor])

    actionActor

  }

  def storeActor(name: String, actor: ActorRef) {
    actions = actions.updated(name, actor)
  }

}

object ActionSystem extends Logging {

  private val system = ActorSystem("Actions")

  val actionSupervisor = system.actorOf(Props[ActionSupervisor], "ActionSupervisor")

  var actions: Map[String, Action] = Map()

  def addAction(action: Action) {
    implicit val timeout = Timeout(5 seconds)
    actionSupervisor ! Add(action)
    actions = actions.updated(action.name, action)
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

  def perform(action: String) {
    val actor = getActor(action)
    if (actor.isDefined) {
      println("action is defined!")
      actor.get ! actions.get(action).get
    } else {
      log.error(s"$action is not defined.")
    }
  }

}


case class Get(action: String)
case class Add(action: Action)

//class StepActor extends Actor {
//  def receive: Actor.Receive = LoggingReceive {
//    case action: Action =>
//    case _ => throw new Error("invalid message received")
//  }
//}
//

class ActionActor extends Actor {
  var steps: List[Step] = null
  def receive: Actor.Receive = LoggingReceive {
    case steps: List[Step] => this.steps = steps
    case action: Action => {
      println(s"received action: $action")
    }

    case msg => println(msg); throw new Error("invalid message received")
  }
}