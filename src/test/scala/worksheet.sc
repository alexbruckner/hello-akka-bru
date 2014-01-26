/**
 * Created by alexbruckner on 24/01/2014
 */

import ax.bru.annot.Awesome
import shapeless._
import syntax.singleton._
import record._
val map = ("double" ->> 4.0) :: ("string" ->> "foo") :: HNil






val d = map("double")
val s = map("string")
@Awesome("We are sooooo awesome!!!!")
class TestClass
// test annotation
import scala.reflect.runtime.universe
val clazz = classOf[TestClass]
val mirror = universe.runtimeMirror(clazz.getClassLoader)




















mirror.classSymbol(clazz).annotations


//java way
//getClass.getClassLoader.loadClass("ax.bru.act.config.Config").getAnnotations




