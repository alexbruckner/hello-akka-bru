package ax.bru.act

import org.eintr.loglady.Logging

object Action {
  def apply(name: String): Action = {
    new Action(name, false)()
  }
}

class Action(val name: String,
             val parallel: Boolean)(block: => Unit) extends Logging {

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

  //  def iterator: Iterator[Step] = allSteps(this).reverse.iterator
  //
  //  def allSteps(action: Action): List[Step] = {
  //
  //    var steps: List[Step] = List()
  //
  //    for (step <- action.steps) {
  //      if (step.action != null && step.action.steps.size > 0) {
  //        steps = allSteps(step.action) ::: List(step) ::: steps
  //      }
  //      else steps ::= step
  //    }
  //
  //    steps
  //  }

  def execute() {
    if (steps.size > 0) {
      for (step <- steps.iterator) {
        step.execute()
      }
    } else {
      block
    }
  }

}

class Step(val num: Int,
           val name: String) extends Logging {

  var action: Action = null

  def setAction(name: String, parallel: Boolean = false): Action = {
    val action = new Action(name, parallel)()
    log.debug(s"setting action $action for step $this")
    this.action = action
    action
  }

  def setExecutable(block: => Unit) {
    action = new Action("block", false)(block)
  }

  def execute() {
    action.execute()
  }

  override def toString(): String = {
    s"Step(stepNum: $num, name: $name)"
  }

}

