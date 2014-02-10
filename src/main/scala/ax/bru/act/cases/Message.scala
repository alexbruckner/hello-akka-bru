package ax.bru.act.cases

import ax.bru.defs.{Step, Action, Data}
import akka.actor.ActorRef
import ax.bru.act.Reserved


/**
 * Created by alexbruckner on 14/01/2014
 */
case class Message(var map: Map[String, Any]) extends Data {
  var sender:ActorRef = null
  def set(key: String, value: Any): Unit = map = map.updated(key, value)
  def get(key: String): Any = {
    val result = map.get(key)
    if (result.isDefined) result.get else "undefined"
  }
  def getAll: Map[String, Any] = map
  def withRecord(actor: ActorRef): Message = {
    addRecord(actor.path.toString)
    this
  }
  def withSender(sender: ActorRef): Message = {
    this.sender = sender
    this
  }
  def isInfo: Boolean = {
    val isInfo = map.get(Reserved.INFO)
    if (isInfo.isDefined) {
      return isInfo.get.asInstanceOf[Boolean]
    }
    false
  }
}

case class Add(action: Action)

case class Perform(actionName: String, data: Map[String, Any], sender: ActorRef = null)

case class AddSteps(steps: List[Step])

case class AddFunction(function: (Data) => Unit)

case class Link(actor: ActorRef)

case class SetParallel(flag: Boolean)

case class Multiple(steps: List[ActorRef])