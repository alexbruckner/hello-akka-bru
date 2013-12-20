package ax.bru

import org.eintr.loglady.Logging
import akka.actor.{ActorRef, Props, ActorSystem, Actor}
import akka.event.LoggingReceive

class Action(override val name: String, val steps: Seq[Step], val parallel: Boolean, val akka: Boolean) extends Step(name)(null) {

  var data: Map[String, Any] = Map[String, Any]()

  val iterator: Iterator[Step] = steps.iterator

  var currentStep: Step = null

  def perform() = {
    execute(this)
    this
  }

  def set[T](key: String, value: T) {
    data = data.updated(key, value)
  }

  def get[T](key: String, c: Class[T] = classOf[String]): T = c.cast(data(key))

  override def execute(action: Action) = {
    log.debug("Running " + (if (parallel) "parallel " else "") + "action: " + name)

//    if (parallel) {
//      while (iterator.hasNext) iterator.next() //exhaust the iterator
//      if(akka){
//        steps.foreach(_.execute(this))
//      } else {
//        steps.foreach(_.act(this))
//      }
//      // TODO a check that steps 3-5 have completed (as another step or part of framework)
//    } else
    {
      if (akka) {
        iterator.next().act(this)
      } else {
        iterator.next().execute(this)
      }
    }

    log.debug("Finished " + (if (parallel) "parallel " else "") + "action: " + name)

    if (!action.equals(this)) {
      action.data = action.data ++ data
      executeNextStep(action)
    }
  }
}

object Action {
  def apply(name: String, parallel: Boolean = false, akka: Boolean = false)(steps: Step*): Action = {
    new Action(name, steps, parallel, akka)
  }
}

class Step(val name: String)(function: Action => Unit) extends Logging {

  def execute(action: Action): Unit = {
    log.debug("Executing step: " + name)
    function(action)
    executeNextStep(action)
  }

  def executeNextStep(action: Action): Unit = if (action.iterator.hasNext) {
    if(action.akka){
      action.iterator.next().act(action)
    } else {
      action.iterator.next().execute(action)
    }
  }

  // actor version
  def act(action: Action):Unit = {
    action.currentStep = this
    convertToActor(this) ! action
  }

  def convertToActor(step: Step): ActorRef = {
    val system = ActorSystem("Actions")
    system.actorOf(Props[StepActor])
  }

}

object Step {
  def apply(function: => Action => Unit): Step = new Step("SOME STEP")(function)

  def apply(name: String)(function: => Action => Unit): Step = new Step(name)(function)
}

// ACTORS FOR ACTIONS AND STEPS ABOVE BELOW

class StepActor extends Actor {
  def receive() = LoggingReceive {
    case action: Action => action.currentStep.execute(action); context.stop(self)
  }
}