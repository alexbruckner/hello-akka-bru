package ax.bru.act

import org.specs2.mutable._

class ActionSpec extends Specification {

  //Action 1
  val action: Action = Action("Action 1")
  action.addStep("1-1")//.setExecutionBlock({println('test)})
  action.addStep("1-2")
  val action2: Action = action.addStep("1-3").setFurtherAction("Action 2 (1-3)") // inner action with further steps , TODO code block for direct execution as direct action
  action.addStep("1-4")

  //Action 2 (1-3)
  val action3 = action2.addStep("2-1").setFurtherAction("Action 3 (2-1)", parallel = true)
  action2.addStep("2-2")

  //Action 3 (2-1)
  action3.addStep("3-1")
  action3.addStep("3-2")
  val action4 = action3.addStep("3-3").setFurtherAction("Action 4 (3-3)", parallel = true)

  //Action 4 (3-3)
  action4.addStep("4-1")
  action4.addStep("4-2")

  for (step <- action){
    println(step)
  }


  "\n\nAction " should {
    "blah 11 elements" in {
      //action.data must have size (11)
      true
    }


  }




}

