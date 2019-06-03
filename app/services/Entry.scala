package services

import javax.inject.Inject

import anorm.SqlParser._
import anorm._
import play.api.db.DBApi

import scala.language.postfixOps

case class Entry(id: Option[Long], name: String)

@javax.inject.Singleton
class EntryService @Inject()(dbapi: DBApi) {

  private val db = dbapi.database("default")

  val simple: RowParser[Entry] = {
    get[Option[Long]]("household_account.id") ~
      get[String]("household_account.name") map {
      case id ~ name => Entry(id, name)
    }
  }

  def list(): Seq[Entry] = {

    db.withConnection { implicit connection =>

      SQL(
        """
          select * from household_account
        """
      ).as(simple *)

    }

  }

  def insert(entry: Entry): Int = {
    db.withConnection { implicit connection =>
      SQL(
        """
        insert into household_account values ((select next value for id_seq), {name})
        """
      ).on(
        'name -> entry.name
      ).executeUpdate()
    }
  }

  def findById(id: Long): Option[Entry] = {
    db.withConnection { implicit connection =>
      SQL("select * from household_account where id = {id}").on('id -> id).as(simple.singleOpt)
    }
  }

  def update(id: Long, entry: Entry): Int = {
    db.withConnection { implicit connection =>
      SQL(
        """
          update household_account
          set name = {name}
          where id = {id}
        """
      ).on(
        'id -> id,
        'name -> entry.name
      ).executeUpdate()
    }
  }

  def delete(id: Long) = {
    db.withConnection { implicit connection =>
      SQL("delete from household_account where id = {id}").on('id -> id).executeUpdate()
    }
  }

}