package ax.bru

import org.specs2.mutable._

class ActionSpec extends Specification {

//  "Synchronous Action Example".title
//  args(xonly=true)

  var output: List[String] = List[String]()

  // TODO add and test a messaging data object (key value map to action that is accessible to steps)

  val step1 = new Step({output ::= "TEST 1"} )

  val step2 = new Step({output ::= "TEST 2"})

  val step3 = new Step({output ::= "TEST 3"})
  val step4 = new Step({output ::= "TEST 4"})
  val step5 = new Step({output ::= "TEST 5"})
  val steps3To5 = new ParallelSteps(step3, step4, step5)

  val step6 = new Step({output ::= "TEST 6"})

  val step7 = new Step({output ::= "TEST 7"})
  val step8 = new Step({output ::= "TEST 8"})
  val action2 = new Action(step7, step8)

  new Action(step1, step2, steps3To5, step6, action2).execute()

  "The output list " should {
    "contain 8 strings" in {
      output must have size(8)
    }
    "be in order" in {
      for (i <- 0 to 7){
        output(i).intern() must be(("TEST " + (8 - i)).intern())
      }
      true
    }
  }
}