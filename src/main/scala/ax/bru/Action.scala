package ax.bru

class Action(override val name: String, val steps: Seq[Step], val parallel: Boolean) extends Step(name)(null) {

  var data: Map[String, Any] = Map[String, Any]()

  val iterator: Iterator[Step] = steps.iterator

  def perform() = {
    execute(this)
    this
  }

  def set[T](key: String, value: T) {
    data = data.updated(key, value)
  }

  def get[T](key: String, c: Class[T] = classOf[String]): T = c.cast(data(key))

  override def execute(action: Action) = {
    println("Running " + (if (parallel) "parallel " else "") + "action: " + name)

    if (parallel) {
      while (iterator.hasNext) iterator.next() //exhaust the iterator
      steps.foreach(_.execute(this))
      // TODO a check that steps 3-5 have completed (as another step or part of framework)
    } else {
      iterator.next().execute(this)
    }

    println("Finished " + (if (parallel) "parallel " else "") + "action: " + name)

    if (!action.equals(this)) {
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

