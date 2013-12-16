package ax.bru


class Action(val steps: Step*) extends Step {
  override def execute() = {
    steps.foreach(_.execute())
  }
}

// define an Action (containing sequential/concurrent Steps)

// ie Action1: -- Step 1 -- Step 2 -- Step4
//                       \- Step 3 -/
// Action 1 executes Step 1, then concurrently Step2 and Step3 and once both finish, Step 4.  (2 & 3 need a tmp actor checking progress)


// for now try sequential execution only, ie Step 1 - Step 2

class Step(block: => Unit) {
  def execute() = {
    block
  }
}

class ParallelSteps(val steps: Step*) extends Step {
  override def execute() = {
    steps.foreach(_.execute())
  }
}

object ActionMain extends App {

  val step1 = new Step({println("TEST 1")})

  val step2 = new Step({println("TEST 2")})

  val step3 = new Step({println("TEST 3")})
  val step4 = new Step({println("TEST 4")})
  val step5 = new Step({println("TEST 5")})
  val steps3To5 = new ParallelSteps(step3, step4, step5)

  val step6 = new Step({println("TEST 6")})

  val step7 = new Step({println("TEST 7")})
  val step8 = new Step({println("TEST 8")})
  val action2 = new Action(step7, step8)

  new Action(step1, step2, steps3To5, step6, action2).execute()


}
