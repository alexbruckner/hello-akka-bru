package ax.bru

import akka.actor.{Props, ActorSystem, Actor}
import akka.event.LoggingReceive

case class Message(msg: String)

class StepActor extends Actor {
  def receive = LoggingReceive {
    case Message(load) => println(load)
      context.stop(self)
  }
}

object Main extends App {
  val system = ActorSystem("actions")
  val actor = system.actorOf(Props[StepActor], "test")
  actor ! Message("ahoi!")
}










