package services

import java.util.{Calendar, Date}

import javax.inject.Inject
import anorm.SqlParser._
import anorm._
import play.api.db.DBApi

import scala.language.postfixOps

case class Entry(id: Option[Long], name: String, price: Long, date: Date)

@javax.inject.Singleton
class EntryService @Inject()(dbapi: DBApi) {

  private val db = dbapi.database("default")

  val simple: RowParser[Entry] = {
    get[Option[Long]]("household_account.id") ~
      get[String]("household_account.name") ~
      get[Long]("household_account.price") ~
      get[Date]("household_account.date") map {
      case id ~ name ~ price ~ date => Entry(id, name, price, date)
    }
  }

  def list(): Seq[Entry] = {

    val items = db.withConnection { implicit connection =>

      SQL(
        """
          select * from household_account
        """
      ).as(simple *)

    }
    items.sortBy(entry => (entry.date, entry.id))
  }

  def insert(entry: Entry): Int = {
    db.withConnection { implicit connection =>
      SQL(
        """
        insert into household_account values ((select next value for id_seq), {name}, {price}, {date})
        """
      ).on(
        'name -> entry.name,
        'price -> entry.price,
        'date -> entry.date
      ).executeUpdate()
    }
  }

  def findById(id: Long): Option[Entry] = {
    db.withConnection { implicit connection =>
      SQL("select * from household_account where id = {id}")
        .on('id -> id)
        .as(simple.singleOpt)
    }
  }

  def update(id: Long, entry: Entry): Int = {
    db.withConnection { implicit connection =>
      SQL(
        """
          update household_account
          set name = {name},
            price = {price},
            date = {date}
          where id = {id}
        """
      ).on(
        'id -> id,
        'name -> entry.name,
        'price -> entry.price,
        'date -> entry.date
      ).executeUpdate()
    }
  }

  def delete(id: Long): Int = {
    db.withConnection { implicit connection =>
      SQL("delete from household_account where id = {id}")
        .on('id -> id)
        .executeUpdate()
    }
  }

}