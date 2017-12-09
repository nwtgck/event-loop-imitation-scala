package io.github.nwtgck.event_loop

abstract class ReadableStream(eventLoop: EventLoop){

  private[this] var onDataCallbacks: List[String => Unit] = List.empty
  private[this] var onEndCallbacks : List[Unit => Unit]   = List.empty

  private[this] def readLoop(): Unit = {
    val data: String = read(8196)
    if(data == ""){
      // Notify on-end
      notifyOnEnd()
    } else {
      // Notify on-data
      notifyOnData(data)
      eventLoop.nextTick{
        readLoop()
      }
    }
  }

  // Delay to nextTick
  eventLoop.nextTick{
    readLoop()
  }

  def read(size: Int): String

  final def onData(f: String => Unit): ReadableStream = {
    onDataCallbacks :+= f
    this
  }
  final def onEnd(f : Unit => Unit): ReadableStream = {
    onEndCallbacks  :+= f
    this
  }

  protected[this] def notifyOnData(data: String): Unit = {
    // Call onData callbacks
    for(callback <- onDataCallbacks){
      callback(data)
    }
  }

  protected[this] def notifyOnEnd(): Unit = {
    for(callback <- onEndCallbacks){
      callback(())
    }
  }

  final def pipe(writableStream: WritableStream): Promise[Unit] = new Promise[Unit]{
    // Enroll to onData-callback
    onData({(data: String) =>
      writableStream.write(data)
    })
    // Enroll to onEnd-callBack
    onEnd({_: Unit =>
      writableStream.end()
      resolve(())
    })
  }

}
