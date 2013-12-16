//import akka.actor.{ ActorRef, ActorSystem, Props, Actor, Inbox }
//import scala.concurrent.duration._
//
//case object Greet
//case class WhoToGreet(who: String)
//case class Greeting(message: String)
//
//class Greeter extends Actor {
//  var greeting = ""
//
//  def receive = {
//    case WhoToGreet(who) => greeting = s"hello, $who"
//    case Greet           => sender ! Greeting(greeting) // Send the current greeting back to the sender
//  }
//}
//
//object HelloAkkaScala extends App {
//
//  // Create the 'helloakka' actor system
//  val system = ActorSystem("helloakka")
//
//  // Create the 'greeter' actor
//  val greeter = system.actorOf(Props[Greeter], "greeter")
//
//  // Create an "actor-in-a-box"
//  val inbox = Inbox.create(system)
//
//  // Tell the 'greeter' to change its 'greeting' message
//  greeter.tell(WhoToGreet("akka"), ActorRef.noSender)
//
//  // Ask the 'greeter for the latest 'greeting'
//  // Reply should go to the "actor-in-a-box"
//  inbox.send(greeter, Greet)
//
//  // Wait 5 seconds for the reply with the 'greeting' message
//  val Greeting(message1) = inbox.receive(5.seconds)
//  println(s"Greeting: $message1")
//
//  // Change the greeting and ask for it again
//  greeter.tell(WhoToGreet("typesafe"), ActorRef.noSender)
//  inbox.send(greeter, Greet)
//  val Greeting(message2) = inbox.receive(5.seconds)
//  println(s"Greeting: $message2")
//
//  val greetPrinter = system.actorOf(Props[GreetPrinter])
//  // after zero seconds, send a Greet message every second to the greeter with a sender of the greetPrinter
//  system.scheduler.schedule(0.seconds, 1.second, greeter, Greet)(system.dispatcher, greetPrinter)
//
//}
//
//// prints a greeting
//class GreetPrinter extends Actor {
//  def receive = {
//    case Greeting(message) => println(message)
//  }
//}

import akka.actor.{ActorSystem, Actor, Props}

class Counter extends Actor{
  var count = 0
  def receive = {
    case "incr" => count += 1
    case "get" => sender ! count
  }
}

class Counter2 extends Actor{

  def receive(n: Int): Actor.Receive = {
    case "incr" =>  context.become(receive(n + 1))
    case "get" => sender ! n
  }

  def receive() = {
    receive(0)
  }
}


class CounterMain extends Actor {

  val counter = context.actorOf(Props[Counter], "counter")

  counter ! "incr"
  counter ! "incr"
  counter ! "incr"
  counter ! "get"

  def receive = {
    case count: Int =>
      println(s"counter 1: count was $count")
      context.stop(self)
  }

}

class CounterMain2 extends Actor {

  val counter = context.actorOf(Props[Counter2], "counter2")

  counter ! "incr"
  counter ! "incr"
  counter ! "incr"
  counter ! "incr"
  counter ! "get"

  def receive = {
    case count: Int =>
      println(s"counter 2: count was $count")
      context.stop(self)
  }

}

object Main extends App {
  val system = ActorSystem("helloakka")
  system.actorOf(Props[CounterMain], "main1")
  system.actorOf(Props[CounterMain2], "main2")
}





