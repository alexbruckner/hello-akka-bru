/**
 * Created by alexbruckner on 24/01/2014
 */
import shapeless._
import syntax.singleton._
import record._
val map = ("double" ->> 4.0) :: ("string" ->> "foo") :: HNil


val d = map("double")
val s = map("string")

