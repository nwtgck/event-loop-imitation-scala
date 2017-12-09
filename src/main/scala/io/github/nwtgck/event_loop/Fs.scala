package io.github.nwtgck.event_loop

import java.io.{BufferedReader, FileReader}



class Fs(eventLoop: EventLoop) {

  class ReadableStream{

    protected[this] var onDataCallbacks: List[String => Unit] = List.empty
    protected[this] var onEndCallbacks : List[Unit => Unit]   = List.empty

    final def onData(f: String => Unit): Unit = {
      onDataCallbacks :+= f
    }
    final def onEnd(f : Unit => Unit): Unit = {
      onEndCallbacks  :+= f
    }
  }

  /**
    * Async read a file
    * @param path
    * @return
    */
  def readFile(path: String): Promise[String] = new Promise[String]{
    val builder: StringBuilder = new StringBuilder()
    val buff   : Array[Char]   = new Array(8196)
    val reader = new BufferedReader(new FileReader(path)) // TODO error handling

    def readLoop(): Unit = {
      val read: Int = reader.read(buff)
      if(read == -1){
        resolve(builder.toString())
      } else {
        val string = new String(buff, 0, read)
        builder.append(string)
        eventLoop.nextTick{
          readLoop()
        }
      }
    }
    readLoop()
  }

  def createReadStream(path: String): ReadableStream = new ReadableStream {
    
    val buff   : Array[Char]   = new Array(8196)
    val reader = new BufferedReader(new FileReader(path)) // TODO error handling

    def readLoop(): Unit = {
      val read: Int = reader.read(buff)
      if(read == -1){
        // Call all end-callbacks
        for(callback <- onEndCallbacks){
          callback(())
        }
      } else {
        val string = new String(buff, 0, read)
        for(callback <- onDataCallbacks){
          callback(string)
        }
        eventLoop.nextTick{
          readLoop()
        }
      }
    }

    // Delay to nextTick
    eventLoop.nextTick{
      readLoop()
    }
  }

}
