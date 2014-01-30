package controllers

import play.api.mvc.{Action, Controller}
import play.api.libs.json.Json
import ax.bru.act.{Result, ActionSystem}


object ActionController extends Controller {

  ActionSystem.start(getClass.getClassLoader)

  def perform = Action {

    val result: Result = ActionSystem.performAndWait(5, "Java Config Action 1", ("test", "test"), ("another", 1))
    val map: Map[String, String] = result.toMap.mapValues(any => String.valueOf(any)) // todo not a fix
    Ok(Json.toJson(map))
  }
}