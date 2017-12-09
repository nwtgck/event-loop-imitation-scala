package io.github.nwtgck.event_loop

import java.util.Date

import scala.collection.mutable

/**
  * Timered Events (mutable)
  */
class TimeredEvents {
  private[this] val inner = mutable.ListBuffer.empty[(Date, Unit => Unit)]

  /**
    * Append preserving sorted order
    * @param dateAndEvent
    */
  def +=(dateAndEvent: (Date, Unit => Unit)): Unit = {
    import DateOrderingImplicits._

    val (deadline, event) = dateAndEvent
    val idx = inner.indexWhere(de => de._1 > deadline)
    if(idx == -1){
      inner.append(dateAndEvent)
    } else {
      inner.insert(idx, dateAndEvent)
    }
  }

  /**
    * Remove hot date&event and returns hot event
    * @param currentDate
    * @return
    */
  def deleteAndGetHotEvents(currentDate: Date): Seq[Unit => Unit] = {
    import DateOrderingImplicits._
    val res = inner.takeWhile(de => de._1 <= currentDate).map(_._2)
    inner.remove(0, res.length)
    res
  }

  /**
    * Non empty or empty
    * @return
    */
  def nonEmpty(): Boolean = inner.nonEmpty


}
