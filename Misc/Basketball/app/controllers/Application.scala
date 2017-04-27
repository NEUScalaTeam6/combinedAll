package controllers

import play.api._
import play.api.mvc._

class Application extends Controller {

  def index = Action {

    Ok(views.html.index())
  }

  def shots = Action {
    Ok(views.html.shots())
  }

}