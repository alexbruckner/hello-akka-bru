package ax.bru.act

import akka.actor.Actor
import akka.event.LoggingReceive

class ActionActor extends Actor {
  def receive() = LoggingReceive {
    case action: String =>
      print(action)
      sender ! action + ". ok."
      context.stop(self)
    case map: Map[String, String] =>
      println(map)
      context.stop(self)
  }
}

