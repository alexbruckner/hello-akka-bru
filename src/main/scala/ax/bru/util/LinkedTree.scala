package ax.bru.util

/**
 * LINKED TREE
 *
 * where child nodes can arbitrarily connect back to existing ones on a lower depth level
 *
 * Created by alexbruckner on 12/02/2014
 */
class LinkedTree(val name: String) extends Iterable[Node] {
  /*
   * ROOT NODE
   */
  val root = Node(name, 0)

  //max node name length in toString method
  val cellSize = 10

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
      for (node <- map.get(key).get) {
        if (first) {
          line += pad("", cellSize, " ") * node.column
          arrowLine += pad("", cellSize, " ") * node.column
        } else {
          val padCount = node.column - lastColumn - 1
          line += pad("", cellSize, " ") * padCount
          arrowLine += pad("", cellSize, " ") * padCount
        }
        val name = s"${node.name}"
        line += s"${pad(name.substring(0, math.min(cellSize, name.size)), cellSize, " ")}"
        arrowLine += s"${pad(""+0x25BC.toChar, cellSize, " ")}"
        lastColumn = node.column
        first = false
      }
      if (key == 0) arrowLine = ""
      result += arrowLine + "\n" + line + "\n"
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

}

object LinkedTree {
  def apply(name: String): LinkedTree = new LinkedTree(name)
}

/*
 * NODE CLASS
 */
class Node(val name: String, var children: List[Node])(val depth: Int, var column: Int = 0) {

  def add(child: String): Node = {
    val node = Node(child, depth + 1)
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
  def apply(name: String, depth: Int): Node = new Node(name, List())(depth)
}
