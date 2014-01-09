package ax.bru.act

import org.specs2.mutable._
import javax.swing.JTable

class ActionSpec extends Specification {

  //Action 1
  val action: Action = Action("Action 1")
  action.addStep("1-1").setExecutable({println("exec 1-1")})
  action.addStep("1-2").setExecutable({println("exec 1-2")})
  val action2: Action = action.addStep("1-3").setAction("Action 2 (1-3)", parallel = true) // inner action with further steps , TODO code block for direct execution as direct action
  action.addStep("1-4").setExecutable({println("exec 1-4")})

  //Action 2 (1-3)
  val action3 = action2.addStep("2-1").setAction("Action 3 (2-1)")
  action2.addStep("2-2").setExecutable({println("exec 2-2")})

  //Action 3 (2-1)
  action3.addStep("3-1").setExecutable({println("exec 3-1")})
  action3.addStep("3-2").setExecutable({println("exec 3-2")})
  val action4 = action3.addStep("3-3").setAction("Action 4 (3-3)", parallel = true)

  //Action 4 (3-3)
  action4.addStep("4-1").setExecutable({println("exec 4-1")})
  action4.addStep("4-2").setExecutable({println("exec 4-2")})

  displayAction(action)

  action.execute()


  "\n\nAction " should {
    "blah 11 elements" in {
      //action.data must have size (11)
      true
    }
  }

  def displayAction(action: Action) {
    import javax.swing._
    val frame = new JFrame()
    frame.setVisible(true)
    val l = new JLabel
    frame.getContentPane.add(l)
    val html = action.toHtml()
    l.setText(s"<html>$html</html>")
    frame.pack
  }

}

