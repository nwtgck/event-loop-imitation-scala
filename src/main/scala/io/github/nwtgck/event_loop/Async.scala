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

  /**
    * Async map
    * @param seq
    * @param f
    * @param resultCallback
    * @tparam A
    * @tparam B
    */
  def map[A, B](seq: Seq[A], f: A => B, resultCallback: Seq[B] => Unit): Unit = {
    seq match {
      case x +: xs =>
        eventLoop.nextTick {
          val mapedX: B = f(x)
          map(xs, f, (mapedXs: Seq[B]) => {
            resultCallback(mapedX +: mapedXs)
          })
        }
      case Nil     =>
        resultCallback(Nil)
    }
  }
}
