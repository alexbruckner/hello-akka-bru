package ax.bru.act

import org.specs2.mutable._
import ax.bru.defs.Action
import scala.collection.immutable.SortedMap

class ActionSpec extends Specification {

  //Action 1
  val action: Action = ExampleAction.action

  displayAction(action)

  Thread.sleep(5000)

  action.execute()

  println(action.data)

  // test annotation
  import scala.reflect.runtime.universe._
  val clazz = classOf[ExampleAction]
  val mirror = runtimeMirror(clazz.getClassLoader)
  val symbol = mirror.classSymbol(clazz)
  println(symbol.annotations)

  "\n\nAction" should {
    "have 8 map entries" in {
      action.getAll must have size 8
    }
    "return map entries with keys 1 to 8" in {
      SortedMap(action.getAll.toSeq:_*).keys.toString == "Set(1, 2, 3, 4, 5, 6, 7, 8)"
    }
    "return map entries with timestamp values in same order as keys" in {// single-threaded sequential step execution
      var previous: String = "0"
      var ok: Boolean = true
      for (i <- 1 to 8) {
        val next = action.get(s"$i").toString
        if (next < previous) {
          ok = false
        }
        previous = next
      }
      ok
    }
  }

  def displayAction(action: Action) {
    import javax.swing.{JFrame, JLabel}
    val frame = new JFrame()
    frame.setVisible(true)
    val l = new JLabel
    frame.getContentPane.add(l)
    val html = Action.toHtml(action)
    l.setText(s"<html>$html</html>")
    frame.pack()
  }

}

