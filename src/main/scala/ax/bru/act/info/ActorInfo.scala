package ax.bru.act.info

import ax.bru.act.cases.Message
import akka.actor.ActorRef

/**
 * Created by alexbruckner on 10/02/2014
 */
trait ActorInfo {
  def checkInfo(message: Message, self: ActorRef, steps: List[ActorRef]) {
    if(message.isInfo){
      message.set(self.path.toString, steps.map((actorRef) => actorRef.path.toString))
    }
  }
}
