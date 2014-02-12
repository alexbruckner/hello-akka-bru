package ax.bru.util

/**
 * Created by alexbruckner on 12/02/2014
 */
class ListMap[K, V] {
  var map: Map[K, List[V]] = Map()

  def put(key: K, value: V) {
    val listCheck: Option[List[V]] = map.get(key)
    val list: List[V] =
      if (listCheck.isDefined) {
        listCheck.get ::: List(value)
      } else {
        List(value)
      }
    map = map.updated(key, list)
  }

  def get(key: K): Option[List[V]] = map.get(key)

  def keys = map.keys
}

object ListMap {
  def apply[K, V](): ListMap[K, V] = new ListMap[K, V]()
}

