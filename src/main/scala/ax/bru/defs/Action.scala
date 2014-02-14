package ax.bru.defs

import org.eintr.loglady.Logging

import java.util.concurrent.ConcurrentHashMap
import java.util.{Map => JMap}

import scala.collection.JavaConverters._
import ax.bru.util.{Node, LinkedTree}

/**
 * Created by alexbruckner on 14/01/2014
 */
object Action {
  def apply(name: String): Action = {
    new Action(name, false, null)(new ConcurrentHashMap())
  }

  def create(name: String) = apply(name: String)

  def toHtml(action: Action): String = {

    val name = action.name

    val stepBuilder: StringBuilder = new StringBuilder

    stepBuilder.append(s"<table border=1><tr><th colspan=2 style='background-color:lightgray; font-weight:normal;'>$name</th></tr>")

    if (action.parallel) stepBuilder.append("<tr>")

    for (step <- action.steps) {
      if (!action.parallel) stepBuilder.append("<tr>")
      stepBuilder.append("<th style='background-color:red;'>").append(Action.toHtml(step)).append("</th>")
      if (!action.parallel) stepBuilder.append("</tr>")
    }

    if (action.parallel) stepBuilder.append("</tr>")

    stepBuilder.append("</table>")

    stepBuilder.toString()

  }

  def toTree(action: Action): LinkedTree = {

    val tree = LinkedTree(action.id)

    toTree(tree.root, action)

    def toTree(node: Node, action: Action) {

      var current = node.add(action.name)

      for (step <- action.steps) {

        val fun = if (step.action.function != null) " (executable)" else ""

        val stepNode = current.add(step.name + fun)

        if (!action.parallel) current = stepNode

        if (step.hasFurtherActionSteps) {
          toTree(stepNode, step.action)
        }

      }



    }

    tree

  }

  private def toHtml(step: Step): String = {
    val name = step.name
    if (step.hasFurtherActionSteps) Action.toHtml(step.action) else s"$name"
  }

  def getId(name: String): String = {
    name.filter((c) =>
      (('a' to 'z') contains c.toLower) || ('0' to '9' contains c) || c == ' ').replace(' ', '_')
  }

}

class Action(val name: String,
             val parallel: Boolean, val function: (Data) => Unit)(val data: JMap[String, Any]) extends Logging with Data {

  var steps: List[Step] = List()
  val id = Action.getId(this.name)

  def addStep(name: String): Step = {
    val step: Step = new Step(name)(data)
    log.debug(s"adding step $step to action $this")
    steps = steps ::: List(step)
    step
  }

  def hasSteps = steps.size > 0

  // single-threaded sequential step execution
  def execute() {
    if (hasSteps) {
      for (step <- steps) {
        step.execute()
      }
    } else {
      // execute stepless action
      function(this)
    }
  }

  override def set(key: String, value: Any) {
    data.put(key, value)
  }

  override def get(key: String): Any = data.get(key)

  override def getAll: Map[String, Any] = data.asScala.toMap

  override def toString() = s"Action $name"

  def print() {
    println(Action.toTree(this).toColorString().replace("executable", Console.RED + "executable" + Console.RESET))
  }

}









