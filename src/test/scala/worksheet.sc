/**
 * Created by alexbruckner on 24/01/2014
 */

import ax.bru.annot.Awesome
import ax.bru.java.ActionConfig
import shapeless._
import syntax.singleton._
import record._
val map = ("double" ->> 4.0) :: ("string" ->> "foo") :: HNil




val d = map("double")
val s = map("string")

@Awesome("We are sooooo awesome!!!!")
@ActionConfig
class TestClass
// test annotation
import scala.reflect.runtime.universe._
val clazz = classOf[TestClass]
val mirror = runtimeMirror(clazz.getClassLoader)





val symbol = mirror.classSymbol(clazz)
println(symbol.annotations)



