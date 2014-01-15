package ax.bru.act

import akka.actor.{Props, ActorRef, Actor}
import org.eintr.loglady.Logging
import ax.bru.defs.Action
import ax.bru.act.cases._

/**
 * Created by alexbruckner on 14/01/2014
 */
trait Actions extends Actor with Logging {

  def addActor(action: Action) {
    val actorRef = createActor(action)
    storeActor(action.name, actorRef)
  }

  // create all actors for this action and return the first actors ref
  private def createActor(action: Action): ActorRef = {

    log.debug(s"creating actor for $action")
    val actionActor = context.actorOf(Props[ActionActor], action.id)

    if (action.parallel) {
      actionActor ! SetParallel(true)
    }

    if (action.hasSteps) {
      //TODO check whether already exists in context (testcase with reusing action as part of another)
      actionActor ! AddSteps(action.steps)
    } else {
      actionActor ! AddFunction(action.function)
    }

    actionActor

  }

  def storeActor(name: String, actor: ActorRef)

}
