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
      action.getAll should have size 8
    }

    "have map entries with keys 1 to 8" in {
      SortedMap(action.getAll.toSeq: _*).keys.toString must beEqualTo ("Set(1, 2, 3, 4, 5, 6, 7, 8)")
    }

    "have map entries with timestamp values in order (1, 2, 3, 4, 5, 6, 7, 8)" in {

      val map: Map[String, String] = for {
        entry <- action.getAll
      } yield (entry._1, entry._2.toString)

      SortedMap(map.map(_.swap).toSeq: _*).values.toString must beEqualTo ("MapLike(1, 2, 3, 4, 5, 6, 7, 8)")

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

