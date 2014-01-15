package ax.bru.act.cases

import ax.bru.defs.{Step, Action, Data}
import akka.actor.ActorRef


/**
 * Created by alexbruckner on 14/01/2014
 */
case class Message(var map: Map[String, Any]) extends Data {
  val id = s"${System.nanoTime()}#${hashCode()}" // todo host, port, etc...
  map = map.updated("id", id)
  def set(key: String, value: Any): Unit = map = map.updated(key, value)
  def get(key: String): Any = {
    val result = map.get(key)
    if (result.isDefined) result.get else "undefined"
  }
  def getAll: Map[String, Any] = map
}

case class Add(action: Action)

case class Perform(actionName: String, data: Map[String, Any])

case class AddSteps(steps: List[Step])

case class AddFunction(function: (Data) => Unit)

case class Link(actor: ActorRef)