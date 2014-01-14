package ax.bru.act

import akka.actor.{Props, ActorSystem}
import org.eintr.loglady.Logging
import ax.bru.defs.{Action}

//import scala.concurrent.Await
//import akka.pattern.ask
import scala.concurrent.duration._
import ax.bru.act.cases._


/**
 * Created by alexbruckner on 14/01/2014.
 */
object ActionSystem extends Logging {

  val timeout = 2 seconds

  private val system = ActorSystem("Actions")

  val actionSupervisor = system.actorOf(Props[ActionSupervisor], "ActionSupervisor")

  def addAction(action: Action) {
    actionSupervisor ! Add(action)
  }

  def perform(action: String, data: Pair[String, Any]*) {
    actionSupervisor ! Perform(action, data.toMap)
  }

  //  def <-- (actor: ActorRef, message: Message): Option[Message] = {
  //    implicit val timeout = Timeout(ActionSystem.timeout)
  //    val future = actor ? message
  //    Await.result(future, timeout.duration).asInstanceOf[Option[Message]]
  //  }

}
