package io.github.nwtgck.event_loop

import java.io.{BufferedReader, FileReader}



class Fs(eventLoop: EventLoop) {

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
}
