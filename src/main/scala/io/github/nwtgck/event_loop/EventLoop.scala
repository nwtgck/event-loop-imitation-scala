package io.github.nwtgck.event_loop

import scala.collection.mutable

/**
  * io.github.nwtgck.event_loop.EventLoop
  * Mutable class
  */
abstract class EventLoop {

  /**
    * A queue of events
    */
  private[this] val eventQueue: mutable.Queue[Unit => Unit] = mutable.Queue.empty

  def nextTick(event: => Unit): Unit = {
    eventQueue.enqueue(_ => event)
  }

  /**
    * Entry point of event loop
    * @param args
    */
  protected[this] def entryPoint(args: Array[String]): Unit


  def main(args: Array[String]): Unit ={
    // Get main event
    lazy val mainEvent: Unit = entryPoint(args)
    // Put main event to queue
    nextTick(mainEvent)
    // Event loop
    while(eventQueue.nonEmpty){
      val event: Unit => Unit = eventQueue.dequeue()
      event()
    }
  }

}
