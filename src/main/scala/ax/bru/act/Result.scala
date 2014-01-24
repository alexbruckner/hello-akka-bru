package ax.bru.act

import ax.bru.defs.Data
import java.util
import java.util.Map.Entry

/**
 * Created by alexbruckner on 24/01/2014
 */
class Result(private val data: Data) extends java.lang.Iterable[java.util.Map.Entry[String, Any]] {
  def keys = data.getAll.keys
  def get(key: String) = data.get(key)

  import collection.JavaConversions._
  def iterator(): util.Iterator[Entry[String, Any]] = mapAsJavaMap(data.getAll).entrySet().iterator

}
