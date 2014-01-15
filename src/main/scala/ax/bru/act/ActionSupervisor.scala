package ax.bru.act

import akka.actor.{ActorRef, Actor}
import akka.event.LoggingReceive
import ax.bru.act.cases._

/**
 * Created by alexbruckner on 14/01/2014
 */
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
