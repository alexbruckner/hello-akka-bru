package ax.bru.act

import akka.actor.{ActorRef, Props, ActorSystem}
import org.eintr.loglady.Logging
import ax.bru.defs.Action
import ax.bru.act.cases._


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

}
