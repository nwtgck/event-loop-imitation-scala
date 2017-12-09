package io.github.nwtgck.event_loop
import java.util.Date

object DateOrderingImplicits {
  implicit class RichDate(val date: Date) extends Ordered[RichDate]{
    override def compare(that: RichDate): Int = this.date.compareTo(that.date)
  }

}
