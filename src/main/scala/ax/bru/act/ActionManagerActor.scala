package ax.bru.act

import akka.actor.Actor
import akka.event.LoggingReceive
import ax.bru.Action

class ActionManagerActor extends Actor {
  def receive() = LoggingReceive {
    case action: Action =>
      print("received action: " + action)
      sender ! action + ". ok."
      context.stop(self)
    case map: Map[String, String] =>
      println(map)
      context.stop(self)
    case _ =>
      context.stop(self)
  }
}

