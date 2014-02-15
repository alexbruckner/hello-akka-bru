package ax.bru.act.config

import ax.bru.java.{SubAction, Step, Action}
import ax.bru.defs.Executable
import ax.bru.act.config.action1.steps.{Step4, Step3, Step2, Step1}

/**
 * Created by alexbruckner on 15/02/2014
 */
@Action(name = "Scala Config Action")
class ScalaConfigAction {
  @Step(order = 1, name = "1-1") def step1: Executable = new Step1

  @Step(order = 2, name = "1-2") def step2: Executable = new Step2

  @Step(order = 3, name = "1-3") def step3: SubAction = new Step3

  @Step(order = 4, name = "1-4") def step4: Executable = new Step4
}