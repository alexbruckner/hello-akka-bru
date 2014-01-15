package ax.bru.act

import akka.actor.{ActorRef, Actor}
import akka.event.LoggingReceive
import ax.bru.act.cases._

/**
 * Created by alexbruckner on 14/01/2014
 */
class StepActor extends Actions {

  var action: ActorRef = null

  var nextStep: ActorRef = null

  var awaitRefs: List[ActorRef] = null

  // TODO timeout for messages in cache
  var receivedSoFar: Map[String, List[Message]] = Map()

  def receive: Actor.Receive = LoggingReceive {
    case Add(action) =>
      log.debug(s"setting '$action' for step ${self.path}")
      if (action != null) {
        addActor(action)
      }

    case Link(actor) =>
      nextStep = actor
      if (action != null) action ! Link(nextStep)

    case message: Message =>

      if (awaitRefs != null) {
        val messagesReceivedSoFar = receivedSoFar.get(message.dataId)
        if (messagesReceivedSoFar.isDefined) {
          receivedSoFar = receivedSoFar.updated(message.dataId, messagesReceivedSoFar.get ::: List(message))
        } else {
          receivedSoFar = receivedSoFar.updated(message.dataId, List(message))
        }
      }

      if (action != null) {
        action ! message.withRecord(self)
      } else if (nextStep != null) {
        if (awaitRefs != null) {
          val messagesReceivedSoFar = receivedSoFar.get(message.dataId)
          if (messagesReceivedSoFar.isDefined && messagesReceivedSoFar.get.size == awaitRefs.size) {
            //TODO if serialised over network need to collate results from multiple actors before sending
            for (storedMessage <- messagesReceivedSoFar.get) {
              for (entry <- storedMessage.getAll){
                message.set(entry._1, entry._2) // todo check this (also means incomplete history on message)
              }
            }
            nextStep ! message.withRecord(self)
            receivedSoFar = receivedSoFar - message.dataId
          }
        } else {
          nextStep ! message.withRecord(self)
        }
      }

    case Multiple(steps) =>
      awaitRefs = steps

    case _ => throw new Error("invalid message received")
  }

  override def storeActor(name: String, actor: ActorRef) {
    action = actor
  }

}
