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

  it should "be able to get a new greeting" in {

    ActionSystem.addAction(ExampleAction.action)

    ActionSystem.perform(ExampleAction.action.name, ("key", "value"))

//    expectMsg("ok.")

    Thread.sleep(5000)

  }





}
