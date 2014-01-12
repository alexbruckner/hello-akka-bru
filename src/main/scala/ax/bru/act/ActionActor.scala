package ax.bru.act

import akka.actor.{ActorRef, Actor}
import akka.event.LoggingReceive
import org.eintr.loglady.Logging

class ActionSupervisor extends Actor with Logging {

  // contains the system wide defined action names and the actor ref to the first actor for each action
  var actions: Map[String, ActorRef] = Map()

  def receive: Actor.Receive = LoggingReceive {

    case action: Action =>
      val actorRef = buildActors(action)
      storeFirstActor(action.name, actorRef)

    case message =>
      log.error(s"wrong message type: $message")

  }

  // create all actors for this action and return the first actors ref
  def buildActors(action: Action): ActorRef = {
    // TODO any subaction in an action that's already in 'actions' must be reused.
    // TODO and any subaction that's not in 'action' needs to be added separately.
    ???
  }

  def storeFirstActor(name: String, actor: ActorRef) {
    actions.updated(name, actor)
  }

}

//class StepActor extends Actor {
//  def receive: Actor.Receive = LoggingReceive {
//    case action: Action =>
//    case _ => throw new Error("invalid message received")
//  }
//}
//
//class ActionActor
// extends Actor {
//  var steps: List[Step] = null
//  def receive: Actor.Receive = LoggingReceive {
//    case steps: List[Step] => this.steps = steps
//    case action: Action => ???
//    case _ => throw new Error("invalid message received")
//  }
//}