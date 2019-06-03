package controllers

import javax.inject._
import play.api.data.Form
import play.api.mvc._
import services.{Entry, EntryService}

import play.api.data.Forms._

class HomeController @Inject()(service: EntryService, mcc: MessagesControllerComponents)
  extends MessagesAbstractController(mcc) {

  def helloworld(): Action[AnyContent] = Action { implicit request: MessagesRequest[AnyContent] =>
    Ok("Hello World")
  }

  def list(): Action[AnyContent] = Action { implicit request: MessagesRequest[AnyContent] =>
    val items = service.list()
    Ok(views.html.list(items))
  }

  val todoForm: Form[String] = Form("name" -> nonEmptyText)

  // GET /new
  def register: Action[AnyContent] = Action { implicit request: MessagesRequest[AnyContent] =>
    Ok(views.html.createForm(todoForm))
  }

  // POST /
  def add(): Action[AnyContent] = Action { implicit request: MessagesRequest[AnyContent] =>
    val name: String = todoForm.bindFromRequest().get
    service.insert(Entry(id = None, name))
    Redirect(routes.HomeController.list())
  }

  // GET     /edit/:todoId
  def edit(todoId: Long): Action[AnyContent] = Action { implicit request: MessagesRequest[AnyContent] =>
    service.findById(todoId).map { todo =>
      Ok(views.html.editForm(todoId, todoForm.fill(todo.name)))
    }.getOrElse(NotFound)
  }

  // POST   /:todoId
  def update(todoId: Long): Action[AnyContent] = Action { implicit request: MessagesRequest[AnyContent] =>
    val name: String = todoForm.bindFromRequest().get
    service.update(todoId, Entry(Some(todoId), name))
    Redirect(routes.HomeController.list())
  }

  // POST   /:todoId/delete
  def delete(todoId: Long): Action[AnyContent] = Action { implicit request: MessagesRequest[AnyContent] =>
    service.delete(todoId)
    Redirect(routes.HomeController.list())
  }

}
