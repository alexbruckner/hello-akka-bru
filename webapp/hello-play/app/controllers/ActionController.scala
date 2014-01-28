package controllers

import play.api.mvc.{Action, Controller}
import play.api.libs.json.Json
import ax.bru.act.{Result, ActionSystem}
import conf.scala.ExampleAction


object ActionController extends Controller {

  //System.setProperty("ax.bru.config","conf") //todo custom loader doesn't seem to work in play
  // for now test example scala action instead.
  ActionSystem.addAction(ExampleAction.action)

  def perform = Action {
    val result: Result = ActionSystem.performAndWait(5, "Action 1", ("test", "test"), ("another", 1))
    val map: Map[String, String] = result.toMap.mapValues(any => String.valueOf(any)) // todo not a fix
    Ok(Json.toJson(map))
  }

}