import ax.bru.act.ActionSystem
import ax.bru.defs.{Data, Executable, Action}
import scala.collection.immutable.SortedMap

/**
 * Created by alexb on 05/06/2014.
 */
object Runner extends App {

  //Action 1
  val action: Action = Action("Action 1")
  action.addStep("1-1").setExecutable((message) => {
    println("exec 1-1"); message.set("1", System.nanoTime())
  })

  // or instead of function can set an executable
  def executable: Executable = new Executable {
    def execute(data: Data): Unit = {
      println("exec 1-2"); data.set("2", System.nanoTime())
    }
  }

  action.addStep("1-2").setExecutable(executable)
  private val action2: Action = action.addStep("1-3").setAction("Action 2 (1-3)", parallel = true)

  // last step
  action.addStep("1-4").setExecutable((message) => {
    message.set("8", System.nanoTime())
    println("exec 1-4")
    println(s"message id: ${message.dataId}")
    println(s"message history: ${message.history}")
    println(s"${SortedMap(message.getAll.toSeq: _*)}")
  })

  //Action 2 (1-3)
  private val action3 = action2.addStep("2-1").setAction("Action 3 (2-1)")
  action2.addStep("2-2").setExecutable((message) => {
    println("exec 2-2"); message.set("7", System.nanoTime())
  })

  //Action 3 (2-1)
  action3.addStep("3-1").setExecutable((message) => {
    println("exec 3-1"); message.set("3", System.nanoTime())
  })
  action3.addStep("3-2").setExecutable((message) => {
    println("exec 3-2"); message.set("4", System.nanoTime())
  })
  private val action4 = action3.addStep("3-3").setAction("Action 4 (3-3)", parallel = true)

  //Action 4 (3-3)
  action4.addStep("4-1").setExecutable((message) => {
    println("exec 4-1"); message.set("5", System.nanoTime())
  })
  action4.addStep("4-2").setExecutable((message) => {
    println("exec 4-2"); Thread.sleep(100); message.set("6", System.nanoTime())
  })

  ActionSystem.addAction(action)

  val sys = ActionSystem.start()



}

