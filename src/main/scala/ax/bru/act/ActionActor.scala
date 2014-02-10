package ax.bru.act

import akka.actor.{Props, ActorRef, Actor}
import akka.event.LoggingReceive
import org.eintr.loglady.Logging
import ax.bru.defs.{Data, Step}
import ax.bru.act.cases._
import ax.bru.act.info.ActorInfo

/**
 * Created by alexbruckner on 14/01/2014
 */
class ActionActor extends Actor with Logging with ActorInfo {
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
        executeSteps(message)
      } else if (function != null) {
        executeFunction(message)
        sendOn(message)
      }

    case msg => log.debug(s"$msg"); throw new Error(s"invalid message received: $msg")
  }

  def executeSteps(message: Message): Unit = {
    if (parallel) {
      checkInfo(message, self, steps)
      for (step <- steps) {
        step ! message.withRecord(self)
      }
    } else {
      checkInfo(message, self, List(steps(0)))
      steps(0) ! message.withRecord(self)
    }
  }

  def executeFunction(message: Message): Unit = {
    if (!message.isInfo) {
      function(message)
    }
  }

  def sendOn(message: Message): Unit = {
    if (nextStep != null) {
      checkInfo(message, self, List(nextStep))
      nextStep ! message.withRecord(self)
    } else if (message.sender != null) {
      // return to sender
      message.sender ! message.withRecord(self)
    }
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

