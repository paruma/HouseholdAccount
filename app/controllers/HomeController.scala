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


  val form: Form[(String, Long)] = Form(
    tuple(
      "name" -> nonEmptyText,
      "price" -> longNumber)
  )

  // GET /new
  def register: Action[AnyContent] = Action { implicit request: MessagesRequest[AnyContent] =>
    Ok(views.html.createForm(form))
  }

  // POST /
  def add(): Action[AnyContent] = Action { implicit request: MessagesRequest[AnyContent] =>
    val (name, price) = form.bindFromRequest().get
    service.insert(Entry(id = None, name, price))
    Redirect(routes.HomeController.list())
  }

  // GET     /edit/:todoId
  def edit(id: Long): Action[AnyContent] = Action { implicit request: MessagesRequest[AnyContent] =>
    service.findById(id).map { entry =>
      Ok(views.html.editForm(id, form.fill(entry.name, entry.price)))
    }.getOrElse(NotFound)
  }

  // POST   /:todoId
  def update(id: Long): Action[AnyContent] = Action { implicit request: MessagesRequest[AnyContent] =>
    val (name, price) = form.bindFromRequest().get
    service.update(id, Entry(Some(id), name, price))
    Redirect(routes.HomeController.list())
  }

  // POST   /:todoId/delete
  def delete(todoId: Long): Action[AnyContent] = Action { implicit request: MessagesRequest[AnyContent] =>
    service.delete(todoId)
    Redirect(routes.HomeController.list())
  }

}
