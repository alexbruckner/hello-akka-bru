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
    val step: Step = new Step(name)
    log.debug(s"adding step $step to action $this")
    steps = steps ::: List(step)
    step
  }

  def toHtml(): String = {

    val stepBuilder: StringBuilder = new StringBuilder

    stepBuilder.append(s"<table border=1><tr><th colspan=2 style='background-color:lightgray; font-weight:normal;'>$name</th></tr>")

    if (parallel) stepBuilder.append("<tr>")

    for (step <- steps.iterator) {
      if (! parallel) stepBuilder.append("<tr>")
      stepBuilder.append("<th style='background-color:red;'>").append(step.toHtml()).append("</th>")
      if (! parallel) stepBuilder.append("</tr>")
    }

    if (parallel) stepBuilder.append("</tr>")

    stepBuilder.append("</table>")

    stepBuilder.toString()

  }

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

class Step(val name: String) extends Logging {

  var action: Action = null

  def setAction(name: String, parallel: Boolean = false): Action = {
    val action = new Action(name, parallel)()
    log.debug(s"setting further action $action for step $this")
    this.action = action
    action
  }

  def setExecutable(block: => Unit) {
    log.debug(s"setting executable block for step $this")
    action = new Action("block", false)(block)
  }

  def execute() {
    action.execute()
  }

  def toHtml(): String = {
    if (action != null && action.steps.size > 0) action.toHtml() else s"$name"
  }

}

