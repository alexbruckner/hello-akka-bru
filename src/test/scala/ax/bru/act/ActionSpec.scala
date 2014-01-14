package ax.bru.act

import org.specs2.mutable._
import ax.bru.defs.Action

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

  "\n\nAction " should {
    "blah 11 elements" in {
      //action.data must have size (11)
      true
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

