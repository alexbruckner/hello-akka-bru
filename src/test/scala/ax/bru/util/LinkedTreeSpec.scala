package ax.bru.util

import org.specs2.mutable._

class LinkedTreeSpec extends Specification {

  val tree = LinkedTree("root")

  val child1 = tree.root.add("1")
  val child2 = tree.root.add("2")
  val child3 = tree.root.add("3")

  child2.add("4")
  child2.add("5")
  child3.add("6").add("7")


  println(tree)


  "\n\nAction" should {
    "have 8 map entries" in {
      true
    }


  }


}

