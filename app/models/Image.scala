package models

import java.util.UUID

import at.droelf.backend.DateTimeUtil
import play.api.db.slick.joda.PlayJodaSupport._
import play.api.db.slick.Config.driver.simple._
import org.joda.time._

case class Image(id: UUID, name: String, dateTime: LocalDateTime, dateTimeZone: DateTimeZone, path: String)

class ImageTable(tag: Tag) extends Table[Image](tag, "IMAGES"){

  def id = column[UUID]("ID", O.PrimaryKey)
  def name = column[String]("NAME", O.NotNull)
  def dateTime = column[LocalDateTime]("DATETIME", O.NotNull)
  def dateTimeZone = column[DateTimeZone]("DATETIME_ZONE", O.NotNull)
  def path = column[String]("PATH")

  def * = (id, name, dateTime, dateTimeZone, path) <> (Image.tupled, Image.unapply)

}

object Images extends DateTimeUtil{

  val imageTable = TableQuery[ImageTable]

  def insertImage(image: Image)(implicit session: Session) = {
    imageTable.insert(image)
  }

  def getImagesByLocalDate(date: LocalDate)(implicit session: Session): Seq[Image] = {
    imageTable.list.filter{ img =>
      utcWithTimeZoneToDateTime(img.dateTime, img.dateTimeZone).toLocalDate.isEqual(date)
    }
  }

  def getNewestImagesForTimeRange(startDate: LocalDate, endDate: LocalDate, numberOfImages: Int)(implicit session: Session): Seq[Image] = {
    val start = startDate.toLocalDateTime(LocalTime.MIDNIGHT)
    val end = endDate.plusDays(1).toLocalDateTime(LocalTime.MIDNIGHT)

    imageTable.filter(img => (img.dateTime >= start && img.dateTime < end)).sortBy(_.dateTime.desc).take(numberOfImages).list
  }

  def getAllImages(implicit session: Session): Seq[Image] = {
    imageTable.list
  }

  def getImageById(uuid: UUID)(implicit session: Session): Image = {
    imageTable.filter(_.id === uuid).list.head
  }

  def delete(uuid: UUID)(implicit session: Session ) {
    imageTable.filter(_.id === uuid).delete
  }

  def update(image: Image)(implicit session: Session){
    imageTable.filter(_.id === image.id).update(image)
  }

}
