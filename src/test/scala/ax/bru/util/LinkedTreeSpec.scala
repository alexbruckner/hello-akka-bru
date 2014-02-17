package ax.bru.util

import org.specs2.mutable._

class LinkedTreeSpec extends Specification {

  val tree = LinkedTree("root", 10)

  val child1 = tree.root.add("1")
  val child2 = tree.root.add("2")
  val child3 = tree.root.add("3")

  val child4 = child2.add("4")
  child2.add("5")
  child3.add("6").add("7")


  child4.add("x")
  child4.add("y")
  child4.add("z")

  tree.print()

  "\nTree" + tree.toString() should {
    "have a correct toString method" in {
      tree.toString must beEqualTo("\n   root   \n    |---------|---------------------------------------|     \n    1         2                                       3     \n              |-----------------------------|         |     \n              4                             5         6     \n              |---------|---------|                   |     \n              x         y         z                   7     \n")
    }


  }

  val action = LinkedTree("''\u001B[4m\u001B[33mScala Config Action\u001B[0m''")
  var node = action.root
  node = node.add("user/ActionSupervisor")
    .add("ActionSupervisor/Scala_Config_Action")
    .add("Scala_Config_Action/11")
    .add("executable (ax.bru.act.config.action1.steps.Step1)")
    .add("Scala_Config_Action/12")
    .add("executable (ax.bru.act.config.action1.steps.Step2)")
    .add("Scala_Config_Action/13")
    .add("13/Action_2_13")

  var step21 = node.add("Action_2_13/21")
  val step22 = node.add("Action_2_13/22")

  step21 = step21.add("21/Action_3_21")
  .add("Action_3_21/31")
  .add("executable (ax.bru.act.config.action1.steps.Step31)")
  .add("Action_3_21/32")
  .add("executable (ax.bru.act.config.action1.steps.Step32)")
  .add("Action_3_21/33")
  .add("33/Action_4_33")

  val step41 = step21.add("Action_4_33/41")
  step41.add("executable (ax.bru.act.config.action1.steps.Step41)").add("Action_4_33/parallelResultActor").add("Action_2_13/parallelResultActor").add("bla")
  val step42 = step21.add("Action_4_33/42")
  step42.add("executable (ax.bru.act.config.action1.steps.Step42)").add("Action_4_33/parallelResultActor").add("Action_2_13/parallelResultActor").add("bla")

  step22.add("executable (ax.bru.act.config.action1.steps.Step22)").add("Action_2_13/parallelResultActor").add("bla")

  action.removeDuplicates()

  "\nLinked Tree" + action.toString() should {
    "have a correct toString method" in {
      action.toString must beEqualTo("\n        ''\u001B[4m\u001B[33mScala Config Action\u001B[0m''         \n                          |                          \n                user/ActionSupervisor                \n                          |                          \n        ActionSupervisor/Scala_Config_Action         \n                          |                          \n               Scala_Config_Action/11                \n                          |                          \n executable (ax.bru.act.config.action1.steps.Step1)  \n                          |                          \n               Scala_Config_Action/12                \n                          |                          \n executable (ax.bru.act.config.action1.steps.Step2)  \n                          |                          \n               Scala_Config_Action/13                \n                          |                          \n                   13/Action_2_13                    \n                          |---------------------------------------------------------------------------------------------------------|                          \n                   Action_2_13/21                                                                                            Action_2_13/22                    \n                          |                                                                                                         |                          \n                   21/Action_3_21                                                                          executable (ax.bru.act.config.action1.steps.Step22) \n                          |                                                                                                         |                          \n                   Action_3_21/31                                                                                                   |                          \n                          |                                                                                                         |                          \n executable (ax.bru.act.config.action1.steps.Step31)                                                                                |                          \n                          |                                                                                                         |                          \n                   Action_3_21/32                                                                                                   |                          \n                          |                                                                                                         |                          \n executable (ax.bru.act.config.action1.steps.Step32)                                                                                |                          \n                          |                                                                                                         |                          \n                   Action_3_21/33                                                                                                   |                          \n                          |                                                                                                         |                          \n                   33/Action_4_33                                                                                                   |                          \n                          |----------------------------------------------------|                                                    |                          \n                   Action_4_33/41                                       Action_4_33/42                                              |                          \n                          |                                                    |                                                    |                          \n executable (ax.bru.act.config.action1.steps.Step41)  executable (ax.bru.act.config.action1.steps.Step42)                           |                          \n                          |                                                    |                                                    |                          \n           Action_4_33/parallelResultActor            <<<<<<<<<<<<<<<<<<<<<<<<|                                                     |                          \n                          |                                                                                                         |                          \n           Action_2_13/parallelResultActor                                                                 <<<<<<<<<<<<<<<<<<<<<<<<|                           \n                          |                          \n                         bla                         \n")
    }
  }


}

