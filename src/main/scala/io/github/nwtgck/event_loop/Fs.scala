package io.github.nwtgck.event_loop

import java.io.{BufferedReader, FileReader, FileWriter}



class Fs(eventLoop: EventLoop) {

  abstract class ReadableStream{

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

  abstract class WritableStream{
    def write(data: String): Unit
    def end(): Unit
  }

  /**
    * Async read a file
    * @param path
    * @return
    */
  def readFile(path: String): Promise[String] = new Promise[String]{
    val builder: StringBuilder = new StringBuilder()
    createReadStream(path)
      .onData(data => {
        builder.append(data)
      })
      .onEnd(_ => {
        resolve(builder.toString())
      })
  }

  /**
    * Create ReadableStream
    * @param path
    * @return
    */
  def createReadStream(path: String): ReadableStream = new ReadableStream {

    var buff: Array[Char] = new Array(8196)
    val reader = new BufferedReader(new FileReader(path)) // TODO error handling

    override def read(size: Int): String = {
      if(size > buff.length){
        // Resize buff
        buff = new Array(size)
      }
      val read: Int = reader.read(buff, 0, size)
      if(read == -1) {
        ""
      } else {
        new String(buff, 0, read)
      }
    }
  }

  /**
    * Create WritableStream
    * @param path
    * @return
    */
  def createWriteStream(path: String): WritableStream = new WritableStream {

    val writer = new FileWriter(path)

    override def write(data: String): Unit = {
      writer.write(data)
    }

    override def end(): Unit = {
      writer.close()
    }
  }

}
