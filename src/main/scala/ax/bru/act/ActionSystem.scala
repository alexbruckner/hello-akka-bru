package ax.bru.act

import akka.actor.{ActorRef, Props, ActorSystem}
import org.eintr.loglady.Logging
import ax.bru.defs.Action
import ax.bru.act.cases._

import scala.concurrent.Await
import akka.pattern.ask
import akka.util.Timeout
import java.util.concurrent.TimeUnit
import ax.bru.java.CustomLoader

import scala.collection.JavaConverters._
import ax.bru.util.{LinkedTree, Node}
import scala.collection.immutable.SortedMap

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

  def performAndWait(waitFor: Long, action: String, data: Pair[String, Any]*): Result = performAndWait(waitFor, action, data.toMap)

  def performAndWait(waitFor: Long, action: String, data: Map[String, Any] = Map()): Result = {
    implicit val timeout = Timeout(waitFor, TimeUnit.SECONDS)
    val future = actionSupervisor ? Perform(action, data)
    val message = Await.result(future, timeout.duration).asInstanceOf[Message]
    new Result(message)
  }

  def performAndWait(waitFor: Long, action: String): Result = performAndWait(waitFor, action, Map[String, Any]())

  // java
  def performAndWait(waitFor: Long, action: String, data: java.util.Map[String, Any]): Result = performAndWait(waitFor, action, data.asScala.toMap) // java

  // TODO logging!!!
  def start() {
    start(null)
  }
  def start(additionalClassLoader: ClassLoader) {
    println("Loading config...........")
    val actions: List[Action] = CustomLoader.loadConfigWith(additionalClassLoader).asScala.toList
    for (action <- actions) {
      println("Adding action: " + action.name)
      addAction(action)
    }
  }

  // introspection of defined action actors

  // info only request, should return paths and connections between actors

  def printActorTree(actionName: String) {
    val received2: Map[String, Any] = performAndWait(5, actionName, (Reserved.INFO, true)).toMap

    def printNext(sorted: Map[String, List[String]], current: List[String], node: Node) {
      for (elem <- current) {
        val currentNode = node.add(elem.replace("akka://Actions/user/ActionSupervisor/", ""))
        val checkNext = sorted.get(elem)
        if (checkNext.isDefined) {
          val next = checkNext.get
          printNext(sorted, next, currentNode)
        }
      }
    }

    def prettyString(map: Map[String, Any]): String = {

      val filtered: Map[String, List[String]] = for {
        (key, value) <- map
        if key.startsWith("akka://")
      } yield (key, value.asInstanceOf[List[String]])

      val sorted: Map[String, List[String]] = SortedMap(filtered.toSeq: _*)

      println
      println("---------------------------------------")
      val tree = LinkedTree(s"'' $actionName ''")
      printNext(sorted, List("akka://Actions/user/ActionSupervisor"), tree.root)
      tree.removeDuplicates().print()
      println
      println("---------------------------------------")

      sorted.toString()

    }

    println(s"${prettyString(received2)}")

  }






}

