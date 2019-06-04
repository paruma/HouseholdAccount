package controllers

import java.util.Date

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


  val form: Form[(String, Long, Date)] = Form(
    tuple(
      "name" -> nonEmptyText,
      "price" -> longNumber,
      "date" -> date
    )
  )

  // GET /new
  def register: Action[AnyContent] = Action { implicit request: MessagesRequest[AnyContent] =>
    Ok(views.html.createForm(form))
  }

  // POST /
  def add(): Action[AnyContent] = Action { implicit request: MessagesRequest[AnyContent] =>
    val (name, price, date) = form.bindFromRequest().get
    service.insert(Entry(id = None, name, price, date))
    Redirect(routes.HomeController.list())
  }

  // GET     /edit/:todoId
  def edit(id: Long): Action[AnyContent] = Action { implicit request: MessagesRequest[AnyContent] =>
    service.findById(id).map { entry =>
      Ok(views.html.editForm(id, form.fill(entry.name, entry.price, new Date(entry.date.getTime))))
    }.getOrElse(NotFound)
  }

  // POST   /:todoId
  def update(id: Long): Action[AnyContent] = Action { implicit request: MessagesRequest[AnyContent] =>
    val (name, price, date) = form.bindFromRequest().get
    service.update(id, Entry(Some(id), name, price, date))
    Redirect(routes.HomeController.list())
  }

  // POST   /:todoId/delete
  def delete(id: Long): Action[AnyContent] = Action { implicit request: MessagesRequest[AnyContent] =>
    service.delete(id)
    Redirect(routes.HomeController.list())
  }

}
