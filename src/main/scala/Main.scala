import io.github.nwtgck.event_loop.{Async, EventLoop}

object Main extends EventLoop {

  /**
    * Main of event loop
    *
    * @param args
    */
  override protected[this] def entryPoint(args: Array[String]): Unit = {
    
    val async = new Async(this)

    async.foreach(1 to 10, (i: Int) => {
      println(s"async(1) i: ${i}")
    })

    async.foreach(1 to 10, (i: Int) => {
      println(s"async(2) i: ${i}")
    })

  }
}
