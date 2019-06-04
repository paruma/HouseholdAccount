package services

import java.util.{Calendar, Date}


private object Info {
  def getYear(date: Date): Int = {
    val cal = Calendar.getInstance
    cal.setTime(date)
    cal.get(Calendar.YEAR)
  }

  def getMonth(date: Date): Int = {
    val cal = Calendar.getInstance
    cal.setTime(date)
    cal.get(Calendar.MONTH) + 1
  }

  def getDay(date: Date): Int = {
    val cal = Calendar.getInstance
    cal.setTime(date)
    cal.get(Calendar.DATE)
  }

  private def sumPrice(item: Seq[Entry]): Long = item.map(entry => entry.price).sum

}

case class Info(private val items: Seq[Entry]) {
  // 愚直実装(高計算量)

  def nonEmpty: Boolean = items.nonEmpty

  def maxYear: Int = items.map(entry => Info.getYear(entry.date)).max

  def minYear: Int = items.map(entry => Info.getYear(entry.date)).min

  def getItemsByTotal: Seq[Entry] = items

  def getItemsByYear(year: Int): Seq[Entry] =
    items.filter(entry => Info.getYear(entry.date) == year)

  def getItemsByMonth(year: Int, month: Int): Seq[Entry] =
    getItemsByYear(year).filter(entry => Info.getMonth(entry.date) == month)

  def getItemsByDay(year: Int, month: Int, day: Int): Seq[Entry] =
    getItemsByMonth(year, month).filter(entry => Info.getDay(entry.date) == day)


  def sumByTotal(): Long = Info.sumPrice(items)

  def sumByYear(year: Int): Long = Info.sumPrice(getItemsByYear(year))

  def sumByMonth(year: Int, month: Int): Long = Info.sumPrice(getItemsByMonth(year, month))

  def sumByDay(year: Int, month: Int, day: Int): Long = Info.sumPrice(getItemsByDay(year, month, day))
}

