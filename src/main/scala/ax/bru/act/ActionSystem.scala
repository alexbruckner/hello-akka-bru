package ax.bru.act

import akka.actor.{ActorRef, Props, ActorSystem}
import org.eintr.loglady.Logging
import ax.bru.defs.Action
import ax.bru.act.cases._

import scala.concurrent.Await
import akka.pattern.ask
import akka.util.Timeout
import java.util.concurrent.TimeUnit



/**
 * Created by alexbruckner on 14/01/2014
 */
object ActionSystem extends Logging {

  private val system = ActorSystem("Actions")

  val actionSupervisor = system.actorOf(Props[ActionSupervisor], "ActionSupervisor")

  def addAction(action: Action) {
    actionSupervisor ! Add(action)
  }

  def perform(action: String, data: Pair[String, Any]*) {
    actionSupervisor ! Perform(action, data.toMap)
  }

  // specify the last actor the message should be sent back to.
  def perform(source: ActorRef, action: String, data: Pair[String, Any]*) {
    actionSupervisor ! Perform(action, data.toMap, source)
  }

  def performAndWait(waitFor: Long, action: String,  data: Pair[String, Any]*): Result = performAndWait(waitFor, action, data.toMap)

  def performAndWait(waitFor: Long, action: String, data: Map[String, Any] = Map()): Result = {
    implicit val timeout = Timeout(waitFor, TimeUnit.SECONDS)
    val future = actionSupervisor ? Perform(action, data)
    val message = Await.result(future, timeout.duration).asInstanceOf[Message]
    new Result(message)
  }

  def performAndWait(waitFor: Long, action: String): Result = performAndWait(waitFor, action, Map[String, Any]()) // java

  def load(pkg: String) = {





  }

}

