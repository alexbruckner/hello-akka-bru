package ax.bru.act

import akka.actor.{ActorRef, Actor}
import akka.event.LoggingReceive
import ax.bru.act.cases._

/**
 * Created by alexbruckner on 14/01/2014.
 */
class StepActor extends Actions {

  var action: ActorRef = null

  var nextStep: ActorRef = null

  def receive: Actor.Receive = LoggingReceive {
    case Add(action) => {
      log.debug(s"setting '$action' for step ${self.path}")
      if (action != null) {
        addActor(action)
      }
    }
    case Link(actor) =>
      nextStep = actor
      if (action != null) action ! Link(nextStep)
    case message: Message =>
      if (action != null) {
        action ! message
      } else if (nextStep != null) {
        nextStep ! message
      }

    case _ => throw new Error("invalid message received")
  }

  override def storeActor(name: String, actor: ActorRef) {
    action = actor
  }

}
