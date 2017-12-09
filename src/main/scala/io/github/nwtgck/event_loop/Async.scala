package io.github.nwtgck.event_loop


class Async(eventLoop: EventLoop) {
  /**
    * For-each for async
    * @param seq
    * @param f
    * @tparam A
    */
  def foreach[A](seq: Seq[A], f: A => Unit): Unit = {
    seq.headOption match {
      case Some(elem) =>
        // Execute process
        f(elem)
        // Go to next step
        eventLoop.nextTick{
          foreach(seq.drop(1), f)
        }
      case _ => () // End of looop
    }
  }
}
