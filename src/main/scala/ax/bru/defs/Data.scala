package ax.bru.defs

/**
 * Created by alexbruckner on 14/01/2014.
 */
trait Data extends MessageHistory {
  def set(key: String, value: Any): Unit
  def get(key: String): Any
  def getAll: Map[String, Any]
}

trait MessageHistory {
  val dataId = s"${System.nanoTime()}#${hashCode()}" // todo host, port, etc...
  var history: List[String] = List()
  def addRecord(path: String){
    history = history ::: List(path)
  }
}