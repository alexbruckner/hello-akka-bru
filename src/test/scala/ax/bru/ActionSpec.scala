package ax.bru

import org.specs2.mutable._

class ActionSpec extends Specification {

  val step1 = Step(name = "FIRST STEP")((action: Action) => {
    action.set("step1", System.nanoTime())
  }
  )

  val step2 = Step((action: Action) => {
    action.set("step2", System.nanoTime())
  }
  )

  val step3 = Step((action: Action) => {
    action.set("step3", System.nanoTime())
  }
  )

  val step4 = Step((action: Action) => {
    action.set("step4", System.nanoTime())
  }
  )

  val step5 = Step((action: Action) => {
    action.set("step5", System.nanoTime())
  }
  )

  val parallelAction3To5 = Action(name = "PARALLEL STEP ACTION", parallel = true)(step3, step4, step5)

  val step6 = Step((action: Action) => {
    action.set("step6", System.nanoTime())
  }
  )

  val step7 = Step((action: Action) => {
    action.set("step7", System.nanoTime())
  }
  )

  val step8_1 = Step((action: Action) => {
    action.set("step8_1", System.nanoTime())
  }
  )

  val step8_2 = Step((action: Action) => {
    action.set("step8_2", System.nanoTime())
  }
  )

  val step9 = Step((action: Action) => {
    action.set("step9", System.nanoTime())
  }
  )

  val step10 = Step((action: Action) => {
    action.set("step10", System.nanoTime())
  }
  )

  val step8SubAction = Action(name = "Step 8 sub action")(step8_1, step8_2)
  val anotherAction7To9 = Action(name = "Action for STEP 7 and STEP 8 and STEP 9")(step7, step8SubAction, step9)
  val action: Action = Action(name = "MAIN ACTION")(step1, step2, parallelAction3To5, step6, anotherAction7To9, step10).perform()

  "\n\nAction data" should {
    "contain 11 elements" in {
      action.data must have size(11)
    }
    "timestamps in order 1,2,3,4,5,6,7,8_1,8_2,9,10" in {
      var map: Map[String, Long] = Map[String, Long]()
      for {
        (key, value) <- action.data.toList
      } yield {
        map = map.updated(key, value.asInstanceOf[Long])
      }
      map.toList.sortBy(tuple => tuple._2).map(tuple => tuple._1).toString.intern() must be("List(step1, step2, step3, step4, step5, step6, step7, step8_1, step8_2, step9, step10)".intern())
    }

  }



}