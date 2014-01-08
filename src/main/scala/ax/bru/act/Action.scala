package ax.bru.act

import org.eintr.loglady.Logging

object Action {
  def apply(name: String): Action = {
    new Action(name, false)
  }
}

class Action(val name: String,
             val parallel: Boolean) extends Iterable[Step] with Logging {

  var steps: List[Step] = List[Step]()

  def addStep(name: String): Step = {
    val step: Step = new Step(steps.size, name)
    log.debug(s"adding step $step to action $this")
    steps = steps ::: List(step)
    step
  }

  override def toString(): String = {
    s"Action(name: $name, parallel: $parallel)"
  }

  def iterator: Iterator[Step] = allSteps(this).reverse.iterator

  def allSteps(action: Action): List[Step] = {

    var steps: List[Step] = List()

    for (step <- action.steps) {

      if (step.action != null) {
        steps = allSteps(step.action) ::: steps
      }

      else steps ::= step
    }

    steps
  }

}

class Step(val num: Int,
           val name: String) extends Logging {

  var action: Action = null

  def setFurtherAction(name: String, parallel: Boolean = false): Action = {
    val action = new Action(name, parallel)
    log.debug(s"setting action $action for step $this")
    this.action = action
    action
  }

//  def setExecutionBlock(block: => Unit) {
//    if (action != null) {
//      log.warn("Ignoring execution block as further action is set.")
//    }
//    log.info("to be implemented...")
//  }

  override def toString(): String = {
    s"Step(stepNum: $num, name: $name)"
  }

}

