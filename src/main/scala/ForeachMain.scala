import io.github.nwtgck.event_loop.{Async, EventLoop}

object ForeachMain extends EventLoop {

  override protected[this] def entryPoint(args: Array[String]): Unit = {

    val async = new Async(this: EventLoop)

    async.foreach(1 to 10, (i: Int) => {
      println(s"async(1) i: ${i}")
    })

    async.foreach(100 to 105, (i: Int) => {
      println(s"async(2) i: ${i}")
    })
  }
}

