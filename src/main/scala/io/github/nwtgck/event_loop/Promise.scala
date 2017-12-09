package io.github.nwtgck.event_loop

class Promise[Res] {
  sealed trait State
  case object Pending extends State
  case object Fullfilled extends State

  private[this] var state             : State            = Pending
  private[this] var value             : Res              = _
  private[this] var resolveSubscribers: Seq[Res => Unit] = Seq.empty
  private[this] var rejectSubscribers : Seq[Throwable => Unit] = Seq.empty

  def resolve(res: Res): Unit = {
    state = Fullfilled
    value = res
    for(s <- resolveSubscribers){
      s(value)
    }
  }

  def reject(throwable: Throwable): Unit = {
    state = Fullfilled
    for(s <- rejectSubscribers){
      s(throwable)
    }
  }

  def andThen(f: Res => Unit): Promise[Res] = {
    if(state == Fullfilled){
      f(value)
    } else {
      resolveSubscribers :+= f
    }
    this
  }

}
