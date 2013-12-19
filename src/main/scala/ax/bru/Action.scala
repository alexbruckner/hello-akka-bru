package ax.bru

class Action(override val name: String, val steps: Seq[Step], val parallel: Boolean) extends Step(name)(null) {

  var data: Map[String, Any] = Map[String, Any]()

  val iterator: Iterator[Step] = steps.iterator

  def perform() = {
    execute(this)
    this
  }

  override def execute(action: Action) = {
    println("Running " + (if (parallel) "parallel " else "") + "action: " + name)

    if (parallel) {
      while (iterator.hasNext) iterator.next()  //exhaust the iterator
      steps.reverse.foreach(_.execute(this)) // for now use reverse to indicate parallelism for later
    } else {
      iterator.next().execute(this)
    }

    println("Finished " + (if (parallel) "parallel " else "") + "action: " + name)

    if (!action.equals(this)){
      action.data = action.data ++ data
      executeNextStep(action)
    }
  }
}

object Action {
  def apply(name: String, parallel: Boolean = false)(steps: Step*): Action = {
    new Action(name, steps, parallel)
  }
}

class Step(val name: String)(function: Action => Unit) {

  def execute(action: Action): Unit = {
    println("Executing step: " + name)
    function(action)
    executeNextStep(action)
  }

  def executeNextStep(action: Action): Unit = if (action.iterator.hasNext) {
    action.iterator.next().execute(action)
  }
}

object Step {
  def apply(function: => Action => Unit): Step = new Step("SOME STEP")(function)

  def apply(name: String)(function: => Action => Unit): Step = new Step(name)(function)
}

object ActionMain extends App {

  val step1 = Step(name = "FIRST STEP")((action: Action) => {
      action.data = action.data.updated("step1", "TEST 1")
      println("TEST 1")
    }
  )

  val step2 = Step((action: Action) => {
      action.data = action.data.updated("step2", "TEST 2")
      println("TEST 2")
    }
  )

  val step3 = Step((action: Action) => {
    action.data = action.data.updated("step3", "TEST 3")
    println("TEST 3")
  }
  )

  val step4 = Step((action: Action) => {
    action.data = action.data.updated("step4", "TEST 4")
    println("TEST 4")
  }
  )

  val step5 = Step((action: Action) => {
    action.data = action.data.updated("step5", "TEST 5")
    println("TEST 5")
  }
  )

  val parallelSteps3To5 = Action(name = "PARALLEL STEP ACTION", parallel = true)(step3, step4, step5)

  val step6 = Step((action: Action) => {
    action.data = action.data.updated("step6", "TEST 6")
    println("TEST 6")
  }
  )

  val step7 = Step((action: Action) => {
    action.data = action.data.updated("step7", "TEST 7")
    println("TEST 7")
  }
  )

  val step8_1 = Step((action: Action) => {
    action.data = action.data.updated("step8_1", "TEST 8.1")
    println("TEST 8.1")
  }
  )

  val step8_2 = Step((action: Action) => {
    action.data = action.data.updated("step8_2", "TEST 8.2")
    println("TEST 8.2")
  }
  )

  val step9 = Step((action: Action) => {
    action.data = action.data.updated("step9", "TEST 9")
    println("TEST 9")
  }
  )

  val step10 = Step((action: Action) => {
    action.data = action.data.updated("step10", "TEST 10")
    println("TEST 10")
  }
  )

  val step8SubAction = Action(name = "Step 8 sub action")(step8_1, step8_2)
  val anotherAction = Action(name = "Action for STEP 7 and STEP 8")(step7, step8SubAction, step9)

  val action: Action = Action(name = "MAIN ACTION")(step1, step2,
    parallelSteps3To5,
    step6, anotherAction, step10).perform()

  println(action.data)

}
