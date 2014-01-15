package ax.bru.act

import ax.bru.defs.Action
import ax.bru.java.TestAnnotation
import ax.bru.annot.Awesome
import scala.collection.immutable.SortedMap

/**
 * Created by alexbruckner on 13/01/2014.
 */

object ExampleAction {
  //Action 1
  val action: Action = Action("Action 1")
  action.addStep("1-1").setExecutable((message) => {println("exec 1-1"); message.set("1", System.nanoTime())})
  action.addStep("1-2").setExecutable((message) => {println("exec 1-2"); message.set("2", System.nanoTime())})
  private val action2: Action = action.addStep("1-3").setAction("Action 2 (1-3)", parallel = true)
  action.addStep("1-4").setExecutable((message) => {
    message.set("8", System.nanoTime());
    println(s"exec 1-4")
    println(s"message id: ${message.dataId}")
    println(s"message history: ${message.history}")
    println(s"${SortedMap(message.getAll.toSeq:_*)}")
  })

  //Action 2 (1-3)
  private val action3 = action2.addStep("2-1").setAction("Action 3 (2-1)")
  action2.addStep("2-2").setExecutable((message) => {println("exec 2-2"); message.set("7", System.nanoTime())})

  //Action 3 (2-1)
  action3.addStep("3-1").setExecutable((message) => {println("exec 3-1"); message.set("3", System.nanoTime())})
  action3.addStep("3-2").setExecutable((message) => {println("exec 3-2"); message.set("4", System.nanoTime())})
  private val action4 = action3.addStep("3-3").setAction("Action 4 (3-3)", parallel = true)

  //Action 4 (3-3)
  action4.addStep("4-1").setExecutable((message) => {println("exec 4-1"); message.set("5", System.nanoTime())})
  action4.addStep("4-2").setExecutable((message) => {println(s"exec 4-2"); message.set("6", System.nanoTime())})
}

@TestAnnotation("woohoo!") // java version
@Awesome("we are awesome!") // scala version
class ExampleAction
