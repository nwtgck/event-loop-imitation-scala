package io.github.nwtgck.event_loop

import java.io.{BufferedReader, FileReader, FileWriter}



class Fs(eventLoop: EventLoop) {

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
  def createReadStream(path: String): ReadableStream = new ReadableStream(eventLoop) {

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
