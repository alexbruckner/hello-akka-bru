import ax.bru.act.cases.Message
import ax.bru.act.{ExampleAction, ActionSystem}
import org.scalatest.{BeforeAndAfterAll, FlatSpec}
import org.scalatest.matchers.ShouldMatchers
import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import scala.concurrent.duration._

class ActionActorSpec(_system: ActorSystem)
  extends TestKit(_system)
  with ImplicitSender
  with ShouldMatchers
  with FlatSpec
  with BeforeAndAfterAll {

  def this() = this(ActorSystem("actions"))

  override def afterAll: Unit = {
    system.shutdown()
    system.awaitTermination(10.seconds)
  }

  ActionSystem.addAction(ExampleAction.action)

  it should "return message to sender with 8 map entries" in {
    ActionSystem.perform(self, ExampleAction.action.name, ("key", "value"))
    val received: Message = receiveOne(5 seconds).asInstanceOf[Message]
    received.getAll.size == 8
  }

}
