package io.github.nwtgck.event_loop

import java.util.Date

import scala.collection.mutable

/**
  * io.github.nwtgck.event_loop.EventLoop
  * Mutable class
  */
abstract class EventLoop {

  /**
    * Current date (this will be updated in event loop)
    */
  private[this] var currentDate: Date = new Date()

  /**
    * A queue of events
    */
  private[this] val nextTickEvents       : mutable.ListBuffer[Unit => Unit] = mutable.ListBuffer.empty

  /**
    * A queue of timered events
    */
  private[this] var timeredEvents: List[(Date, Unit => Unit)] = List.empty

  /**
    * Next tick
    * @param event
    */
  def nextTick(event: => Unit): Unit = {
    nextTickEvents += (_ => event)
  }

  def setTimeout(event: Unit => Unit, millis: Long): Unit = {
    timeredEvents =  timeredEvents :+ (new Date(currentDate.getTime+millis), event)
  }

  /**
    * Entry point of event loop
    * @param args
    */
  protected[this] def entryPoint(args: Array[String]): Unit


  /**
    * Run next-tick events
    */
  private[this] def runNextTickEvents(): Unit = {
    val clonedEventQueue = nextTickEvents.clone
    nextTickEvents.clear()
    for (event <- clonedEventQueue) {
      event()
    }
  }


  /**
    * Run a timer event
    */
  private[this] def runTimeredEvent(): Unit = {
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
    // Run entry point
    entryPoint(args)

    // Event loop
    while(nextTickEvents.nonEmpty || timeredEvents.nonEmpty){

      // Update current date
      currentDate = new Date()

      // Run next-tick events
      runNextTickEvents()

      // Run a timer event
      runTimeredEvent()
    }
  }

}
