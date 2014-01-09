package ax.bru.act

import org.specs2.mutable._

class ActionSpec extends Specification {

  //Action 1
  val action: Action = Action("Action 1")
  action.addStep("1-1").setExecutable((action) => {println("exec 1-1"); action.set("test", "muadib")})
  action.addStep("1-2").setExecutable((action) => {println("exec 1-2")})
  val action2: Action = action.addStep("1-3").setAction("Action 2 (1-3)", parallel = true)
  action.addStep("1-4").setExecutable((action) => {println("exec 1-4")})

  //Action 2 (1-3)
  val action3 = action2.addStep("2-1").setAction("Action 3 (2-1)")
  action2.addStep("2-2").setExecutable((action) => {println("exec 2-2")})

  //Action 3 (2-1)
  action3.addStep("3-1").setExecutable((action) => {println("exec 3-1")})
  action3.addStep("3-2").setExecutable((action) => {println("exec 3-2")})
  val action4 = action3.addStep("3-3").setAction("Action 4 (3-3)", parallel = true)

  //Action 4 (3-3)
  action4.addStep("4-1").setExecutable((action) => {println("exec 4-1"); action.set("bla", 1234)})
  action4.addStep("4-2").setExecutable((action) => {println("exec 4-2")})

  displayAction(action)

  action.execute()

  println(action.data)


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

