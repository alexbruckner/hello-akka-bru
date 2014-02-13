import ax.bru.act.cases.Message
import ax.bru.act.{Reserved, ExampleAction, ActionSystem}
import ax.bru.util.{LinkedTree, Node}
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

  Thread.sleep(2000)

  ActionSystem.printActorTree(ExampleAction.action.name)

  ActionSystem.perform(self, ExampleAction.action.name, ("0", 0))
  val received: Message = receiveOne(5 seconds).asInstanceOf[Message]

  it should "return message to sender with 9 map entries" in {
    received.getAll should have size 9
  }

  it should "return map entries with keys 0 to 8" in {
    SortedMap(received.getAll.toSeq: _*).keys.toString should be("Set(0, 1, 2, 3, 4, 5, 6, 7, 8)")
  }

  it should "return map entries with timestamp values in order (0, 1, 2, 7, 3, 4, 5, 6, 8)" in {

    val map: Map[String, String] = for {
      entry <- received.getAll
    } yield (entry._1, entry._2.toString)

    SortedMap(map.map(_.swap).toSeq: _*).values.toString should be("MapLike(0, 1, 2, 7, 3, 4, 5, 6, 8)")

  }

}
