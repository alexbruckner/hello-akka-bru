package ax.bru.act

import akka.actor.{Props, ActorRef, Actor}
import akka.event.LoggingReceive
import org.eintr.loglady.Logging
import ax.bru.defs.{Step, Data}
import ax.bru.act.cases._

/**
 * Created by alexbruckner on 14/01/2014
 */
class ActionActor extends Actor with Logging {
  var steps: List[ActorRef] = List()
  var function: (Data) => Unit = null
  var nextStep: ActorRef = null
  var parallel: Boolean = false
  var parallelResultActor: ActorRef = null

  def receive: Actor.Receive = LoggingReceive {
    case AddSteps(steps) =>
      for (step <- steps) {
        val actorRef = createActor(step)

        if (parallel) {
          actorRef ! Link(parallelResultActor)
        } else {
          //link previous one to this one
          linkLastTo(actorRef)
        }

        storeActor(actorRef)
      }

      if (parallel) parallelResultActor ! Multiple(this.steps)


    case SetParallel(parallel) =>
      log.debug(s"${self.path} now executes in parallel.")
      this.parallel = parallel
      parallelResultActor = context.actorOf(Props[StepActor], "parallelResultActor")

    case Link(actor) =>
      log.debug(s"${self.path} has now next step ${actor.path}")
      nextStep = actor
      if (parallel) {
        parallelResultActor ! Link(nextStep)
      } else {
        linkLastTo(nextStep)
      }

    case AddFunction(function) => this.function = function

    case message: Message =>
      if (steps.size > 0) {
        if (parallel) {
          for (step <- steps) {
            step ! message.withRecord(self)
          }
        } else {
          steps(0) ! message.withRecord(self)
        }
      } else if (function != null) {
        function(message)
        if (nextStep != null) {
          nextStep ! message.withRecord(self)
        } else {
          // return to sender
          if (message.sender != null) {
            message.sender ! message.withRecord(self)
          }
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

  def linkLastTo(actor: ActorRef) {
    if (steps.size > 0) {
      steps.last ! Link(actor)
    }
  }

  def storeActor(actor: ActorRef) {
    steps = steps ::: List(actor)
  }

}

