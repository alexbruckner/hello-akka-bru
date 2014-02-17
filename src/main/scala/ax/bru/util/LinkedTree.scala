package ax.bru.util

/**
 * LINKED TREE
 *
 * where child nodes can arbitrarily connect back to existing ones on a lower depth level
 *
 * Created by alexbruckner on 12/02/2014
 */
class LinkedTree(val name: String, val wantedCellSize: Int) extends Iterable[Node] {
  /*
   * ROOT NODE
   */
  val root = Node(name, 0)

  var downArrow = ""

  def print() {
    println(toColorString())
  }

  def toColorString(): String = {
    toString().replace("-", Console.GREEN + "-" + Console.RESET).replace("|", Console.GREEN + "|" + Console.RESET).replace("<", Console.GREEN + "<" + Console.RESET)
  }

  var cellSize = wantedCellSize

  def updateCellSize():Unit = synchronized {
    if (wantedCellSize == 0) {
      cellSize = root.flatten.map(node => node.name.length()).max + 1
    }
  }

  /* expect:
   *
   * root
   * |   |       |
   * 1   2       3
   *     |   |   |
   *     4   5   6
   *             |
   *             7
   */
  override def toString(): String = {
    updateCellSize()
    var result: String = ""
    val map: ListMap[Int, Node] = ListMap()
    for (node <- this) {
      map.put(node.depth, node)
    }
    for (key <- map.keys.toSeq.sorted) {
      var line = ""
      var arrowLine = ""
      var lastColumn = 0
      var first = true
      var arrowLineParents = List[String]()
      for (node <- map.get(key).get) {
        arrowLineParents = arrowLineParents ::: List(node.parent.name)
        if (first) {
          line += pad("", cellSize, " ") * node.column
          arrowLine += pad("", cellSize, " ") * node.column
        } else {
          val padCount = node.column - lastColumn - 1
          line += pad("", cellSize, " ") * padCount
          arrowLine += pad("", cellSize, "-") * padCount
        }
        val name = s"${node.name}"
        line += s"${pad(name.substring(0, math.min(cellSize, name.size)), cellSize, " ")}"
        arrowLine += s"${pad("|", cellSize, "-")}"
        lastColumn = node.column
        first = false
      }
      if (key == 0) arrowLine = ""

      var modArrowLine = ""
      var charpos = 0
      var hits = -1
      var write: Boolean = true
      for (c <- arrowLine) {

        if (c == '|') {
          hits += 1
          if (hits < arrowLineParents.size - 1 && arrowLineParents(hits) != arrowLineParents(hits + 1)) {
            write = false
          }
          modArrowLine += c
        }
        else if (write && hits > -1 && hits < arrowLineParents.size - 1) {
          modArrowLine += c
        } else {
          modArrowLine += " "
        }
        charpos += 1
      }

      result += modArrowLine + "\n" + line + "\n"
    }
    result
  }

  def pad(s: String, width: Int, ch: String) = {
    val l = s.length
    val left = (width - l) / 2
    val right = width - left - l
    ch * left + s + ch * right
  }


  override def iterator: Iterator[Node] = new Iterator[Node]() {

    val iterator = root.flatten.iterator

    var column: Int = 0
    var lastDepth: Int = -1

    /*
     * expect elem (depth) (column) [ie: whenever depth does not increase +1 to column]
     * r (0) (0)
     * 1 (1) (0)
     * 2 (1) (1)
     * 4 (2) (1)
     * 5 (2) (2)
     * 3 (1) (3)
     * 6 (2) (3)
     * 7 (3) (3)
     */
    override def next(): Node = {
      val node = iterator.next()
      if (node.depth != lastDepth + 1) {
        column = column + 1
      }
      lastDepth = node.depth
      node.column = column
      node
    }

    override def hasNext: Boolean = iterator.hasNext
  }

  def removeDuplicates(): Unit = synchronized {
    updateCellSize()
    var nodesNames: List[String] = List()
    for (node <- this) {
      if (nodesNames.contains(node.name)) {
        node.children = List()
        var nextNode = node
        val check: Option[Node] = find(node.name)
        node.name = "|"
        if (check.isDefined) {
          for (i <- 1 to check.get.depth - node.depth) {
            nextNode = nextNode.add("|")
          }
          nextNode.name = " " + ("<" * (cellSize / 2)).substring(2) + "|" + " " * (cellSize / 2)
        }
      }
      else nodesNames = nodesNames ::: List(node.name)
    }
  }

  def find(name: String): Option[Node] = {
    for (node <- this) {
      if (node.name == name) return Option(node)
    }
    Option.empty
  }

}

object LinkedTree {
  def apply(name: String, cellSize: Int = 0): LinkedTree = new LinkedTree(name, cellSize)
}

/*
 * NODE CLASS
 */
class Node(var name: String, var children: List[Node], val parent: Node)(val depth: Int, var column: Int = 0) {

  def add(child: String): Node = {
    val node = Node(child, depth + 1, this)
    children = children ::: List(node)
    node
  }

  override def toString: String = {
    s"$name ($depth) ($column)"
  }

  def flatten: List[Node] = {
    var list = List(this)
    for (child <- children) {
      list = list ::: child.flatten
    }
    list
  }

}

object Node {
  def apply(name: String, depth: Int, parent: Node = new Node("undef", null, null)(0)): Node = new Node(name, List(), parent)(depth)
}