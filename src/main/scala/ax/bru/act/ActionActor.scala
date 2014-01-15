package ax.bru.act

import akka.actor.{Props, ActorRef, Actor}
import akka.event.LoggingReceive
import org.eintr.loglady.Logging
import ax.bru.defs.{Step, Data}
import ax.bru.act.cases._

/**
 * Created by alexbruckner on 14/01/2014.
 */
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
      nextStep = actor
      linkLastToThis(nextStep)
    case AddFunction(function) => this.function = function
    case message: Message =>
      if (steps.size > 0) {
        // todo parallel case
        steps(0) ! message.withRecord(self)
      } else if (function != null) {
        function(message)
        if (nextStep != null) {
          nextStep ! message.withRecord(self)
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

