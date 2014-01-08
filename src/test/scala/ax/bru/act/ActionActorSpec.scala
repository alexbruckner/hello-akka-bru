import org.scalatest.{BeforeAndAfterAll, FlatSpec}
import org.scalatest.concurrent._
import org.scalatest.matchers.ShouldMatchers
import akka.actor.{Actor, Props, ActorSystem}
import akka.testkit.{ImplicitSender, TestKit, TestActorRef}
import scala.concurrent.duration._

class ActionActorSpec(_system: ActorSystem)
  extends TestKit(_system)
  with ImplicitSender
  with ShouldMatchers
  with FlatSpec
  with BeforeAndAfterAll {

  def this() = this(ActorSystem("action"))

  override def afterAll: Unit = {
    system.shutdown()
    system.awaitTermination(10.seconds)
  }

  it should "be able to get a new greeting" in {
    val action = system.actorOf(Props[ax.bru.act.ActionActor], "action")
    action ! "testing"

    expectMsg("testing. ok.")
  }


}
