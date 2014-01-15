import ax.bru.act.cases.Message
import ax.bru.act.{ExampleAction, ActionSystem}
import org.scalatest.{BeforeAndAfterAll, FlatSpec}
import org.scalatest.matchers.ShouldMatchers
import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import scala.collection.immutable.SortedMap
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
  ActionSystem.perform(self, ExampleAction.action.name, ("0", 0))
  val received: Message = receiveOne(5 seconds).asInstanceOf[Message]

  it should "return message to sender with 9 map entries" in {
    received.getAll.size == 9
  }

  it should "return map entries with keys 0 to 8" in {
    SortedMap(received.getAll.toSeq:_*).keys.toString == "Set(0, 1, 2, 3, 4, 5, 6, 7, 8)"
  }

  it should "return map entries with timestamp values in same order as keys" in { // todo this will have to revisited once the 'parallel' case is implemented!
    var previous: String = "0"
    var ok: Boolean = true
    for (i <- 0 to 8) {
      val next = received.get(s"$i").toString
      if (next < previous) {
        ok = false
      }
      previous = next
    }
    ok
  }

}
