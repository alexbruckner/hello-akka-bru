package ax.bru.defs

/**
 * Created by alexbruckner on 14/01/2014.
 */
trait Data {
   def set(key: String, value: Any): Unit
   def get(key: String): Any
   def getAll: Map[String, Any]
 }
