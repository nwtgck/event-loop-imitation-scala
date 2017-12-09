package io.github.nwtgck.event_loop

import java.util.Date

import scala.collection.mutable

/**
  * io.github.nwtgck.event_loop.EventLoop
  * Mutable class
  */
abstract class EventLoop {

  /**
    * A queue of events
    */
  private[this] val eventQueue       : mutable.Queue[Unit => Unit] = mutable.Queue.empty

  /**
    * A queue of timered events
    */
  private[this] var timeredEvents: List[(Date, Unit => Unit)] = List.empty

  /**
    * Next tick
    * @param event
    */
  def nextTick(event: => Unit): Unit = {
    eventQueue.enqueue(_ => event)
  }

  def setTimeout(event: Unit => Unit, millis: Long): Unit = {
    val currentDate: Date = new Date()
    timeredEvents = (new Date(currentDate.getTime+millis), event) +: timeredEvents
  }

  /**
    * Entry point of event loop
    * @param args
    */
  protected[this] def entryPoint(args: Array[String]): Unit


  /**
    * Run a timer event
    */
  private[this] def runTimeredEvent(): Unit = {
    // Get current date
    val currentDate: Date = new Date()
    // Find index
    val idx = timeredEvents.indexWhere { a => a._1.compareTo(currentDate) < 0 }

    if (idx != -1) {
      // Execute event
      val (_, event) = timeredEvents(idx)
      event()
      // Delete the element
      timeredEvents = timeredEvents.take(idx) ++ timeredEvents.drop(idx + 1)

    }
  }

  def main(args: Array[String]): Unit ={
    // Get main event
    lazy val mainEvent: Unit = entryPoint(args)
    // Put main event to queue
    nextTick(mainEvent)
    // Event loop
    while(eventQueue.nonEmpty || timeredEvents.nonEmpty){

      // Run a timer event
      runTimeredEvent()

      if(eventQueue.nonEmpty) {
        // Execute event
        val event: Unit => Unit = eventQueue.dequeue()
        event()
      }

    }
  }

}
