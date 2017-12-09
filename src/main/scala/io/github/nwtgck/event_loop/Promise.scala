package io.github.nwtgck.event_loop

object Promise{
  def resolved[A](a: A): Promise[A] = new Promise[A]{
    resolve(a)
  }

  def rejected[A](e: Throwable): Promise[A] = new Promise[A]{
    reject(e)
  }
}

class Promise[Res] {
  sealed trait State
  case object Pending    extends State
  case object Fulfilled  extends State
  case object Rejected   extends State

  private[this] var state             : State            = Pending
  private[this] var value             : Res              = _
  private       var resolveSubscribers: Seq[Res => Unit] = Seq.empty
  private       var rejectSubscribers : Seq[Throwable => Unit] = Seq.empty

  def resolve(res: Res): Unit = {
    state = Fulfilled
    value = res
    for(s <- resolveSubscribers){
      s(value)
    }
  }

  def reject(throwable: Throwable): Unit = {
    state = Rejected
    for(s <- rejectSubscribers){
      s(throwable)
    }
  }

  def andThen[Next](f: Res => Promise[Next]): Promise[Next] = {
    val nextPromise = new Promise[Next]()

    state match {
      case Fulfilled =>
        val promise = f(value)
        promise.resolveSubscribers :+= {nextValue: Next =>
          nextPromise.resolve(nextValue)
        }
      case Pending    =>
        resolveSubscribers :+= {(res: Res) =>
          val promise: Promise[Next] = f(res)
          promise.resolveSubscribers :+= {nextValue: Next =>
            nextPromise.resolve(nextValue)
          }
        }
      case Rejected   => ???
    }
    nextPromise
  }

  def map[B](f: Res => B): Promise[B] =
    andThen((res: Res) => Promise.resolved(f(res)))

  def flatMap[B](f: Res => Promise[B]): Promise[B] =
    andThen(f)

}
