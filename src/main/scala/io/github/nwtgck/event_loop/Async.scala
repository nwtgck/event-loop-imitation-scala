package io.github.nwtgck.event_loop


class Async(eventLoop: EventLoop) {
  /**
    * For-each for async
    * @param seq
    * @param f
    * @tparam A
    */
  def foreach[A](seq: Seq[A], f: A => Unit): Promise[Unit] = new Promise[Unit]{
    seq.headOption match {
      case Some(elem) =>
        // Execute process
        f(elem)
        // Go to next step
        eventLoop.nextTick{
          foreach(seq.drop(1), f)
            .andThen(resolve)
        }
      case _ =>
        resolve(()) // End of loop
    }
  }

  /**
    * Async map
    * @param seq
    * @param f
    * @tparam A
    * @tparam B
    */
  def map[A, B](seq: Seq[A], f: A => B): Promise[Seq[B]] = new Promise[Seq[B]]{
    seq match {
      case x +: xs =>
        eventLoop.nextTick {
          val mapedX: B = f(x)
          map(xs, f).andThen((mappedXs: Seq[B]) => {
            resolve(mapedX +: mappedXs)
          })
        }
      case Nil     =>
        resolve(Nil)
    }
  }


  /**
    * Async filter
    * @param seq
    * @param p
    * @tparam A
    */
  def filter[A](seq: Seq[A], p: A => Boolean): Promise[Seq[A]] = new Promise[Seq[A]]{
    seq match {
      case x +: xs =>
        eventLoop.nextTick {
          filter(xs, p).andThen((filteredXs: Seq[A]) => {
            resolve(if(p(x)) x +: filteredXs else filteredXs)
          })
        }
      case Nil     =>
        resolve(Nil)
    }
  }
}
