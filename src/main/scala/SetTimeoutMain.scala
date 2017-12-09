import io.github.nwtgck.event_loop.EventLoop

object SetTimeoutMain extends EventLoop {

  override protected[this] def entryPoint(args: Array[String]): Unit = {

    setTimeout(_ => {
      println("(1000msec) setTimeout1")
    }, 1000)

    setTimeout(_ => {
      println("(50msec)   setTimeout2")
    }, 50)

    setTimeout(_ => {
      println("(100msec)  setTimeout3")
    }, 100)
  }
}

