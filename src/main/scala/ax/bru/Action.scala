package ax.bru

class Action(override val name: String, val parallel: Boolean, val steps: Seq[Step]) extends Step(name)({}) {

  override def execute() = {
    println("Running " + (if (parallel) "parallel " else "") + "action: " + name)
    if (parallel) {
      steps.reverse.foreach(_.execute())  // for now use reverse to indicate parallelism for later
    } else {
      steps.foreach(_.execute())
    }
  }
}

object Action {
  def apply(name: String, parallel: Boolean = false)(steps: Step*):Action = new Action(name, parallel, steps)
}

class Step(val name: String)(block: => Unit){
  def execute() = {
    println("Executing step: " + name)
    block
  }
}

object Step {
  def apply(block: => Unit):Step = new Step("SOME STEP")(block)
  def apply(name: String)(block: => Unit): Step = new Step(name)(block)
}

object ActionMain extends App {

  val step1 = Step(name = "FIRST STEP") {println("TEST 1")}

  val step2 = Step {println("TEST 2")}

  val step3 = Step {println("TEST 3")}
  val step4 = Step {println("TEST 4")}
  val step5 = Step {println("TEST 5")}

  val parallelSteps3To5 = Action(name = "GRGVFVFVF", parallel = true)(step3, step4, step5)

  val step6 = Step {println("TEST 6")}

  val step7 = Step {println("TEST 7")}
  val step8 = Step {println("TEST 8")}

  val anotherAction = Action(name = "Action for STEP 7 and STEP 8")(step7, step8)

  Action(name = "MAIN ACTION")(step1, step2, parallelSteps3To5, step6, anotherAction).execute()

}
