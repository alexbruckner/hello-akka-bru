package ax.bru.act

import akka.actor.{ActorRef, Actor}
import akka.event.LoggingReceive
import ax.bru.act.cases._
import ax.bru.act.info.ActorInfo

/**
 * Created by alexbruckner on 14/01/2014
 */
class ActionSupervisor extends Actions with ActorInfo {

  // contains the system wide defined 'top level' action names and the actor ref to the first actor for each action
  var actions: Map[String, ActorRef] = Map()

  def receive: Actor.Receive = LoggingReceive {

    case Add(action) =>
      addActor(action)

    case Perform(actionName, map, overwriteSender) =>
      val actor = actions.get(actionName)
      if (actor.isDefined) {
        var senderToUse = overwriteSender
        if (senderToUse == null) {
          senderToUse = sender
        }
        val message = Message(map).withRecord(self).withSender(senderToUse)
        checkInfo(message, self, List(actor.get))
        actor.get ! message
      }

    case message =>
      log.error(s"wrong message type: $message")

  }

  override def storeActor(name: String, actor: ActorRef) {
    actions = actions.updated(name, actor)
  }

}
