package ax.bru.act

import org.eintr.loglady.Logging

import java.util.concurrent.ConcurrentHashMap
import java.util.Map

object Action {
  def apply(name: String): Action = {
    new Action(name, false, null)(new ConcurrentHashMap())
  }
  def toHtml(action: Action): String = {

    val name = action.name

    val stepBuilder: StringBuilder = new StringBuilder

    stepBuilder.append(s"<table border=1><tr><th colspan=2 style='background-color:lightgray; font-weight:normal;'>$name</th></tr>")

    if (action.parallel) stepBuilder.append("<tr>")

    for (step <- action.steps.iterator) {
      if (! action.parallel) stepBuilder.append("<tr>")
      stepBuilder.append("<th style='background-color:red;'>").append(Action.toHtml(step)).append("</th>")
      if (! action.parallel) stepBuilder.append("</tr>")
    }

    if (action.parallel) stepBuilder.append("</tr>")

    stepBuilder.append("</table>")

    stepBuilder.toString()

  }

  private def toHtml(step: Step): String = {
    val name = step.name
    if (step.action != null && step.action.hasFurtherActions) Action.toHtml(step.action) else s"$name"
  }


}

class Action(val name: String,
             val parallel: Boolean, function: => Action => Unit)(val data: Map[String, Any]) extends Logging {

  var steps: List[Step] = List()

  def addStep(name: String): Step = {
    val step: Step = new Step(name)(data)
    log.debug(s"adding step $step to action $this")
    steps = steps ::: List(step)
    step
  }
  
  def hasFurtherActions = steps.size > 0

  def execute() {
    if (hasFurtherActions) {
      for (step <- steps.iterator) {
        step.execute()
      }
    } else {
      function(this)
    }
  }

  def set(key: String, value: Any) {
    data.put(key, value)
  }

  def get(key: String): Any = data.get(key)

}

class Step(val name: String)(var data: Map[String, Any]) extends Logging {

  var action: Action = null

  def setAction(name: String, parallel: Boolean = false): Action = {
    val action = new Action(name, parallel, null)(data)
    log.debug(s"setting further action $action for step $this")
    this.action = action
    action
  }

  def setExecutable(function: => (Action) => Unit) {
    log.debug(s"setting executable function for step $this")
    action = new Action("executable", false, function)(data)
  }

  def execute() {
    action.execute()
  }

}

