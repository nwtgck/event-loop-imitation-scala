import io.github.nwtgck.event_loop.{Async, EventLoop}

object MonadicMain extends EventLoop {

  override protected[this] def entryPoint(args: Array[String]): Unit = {

    val async = new Async(this: EventLoop)

    async.foreach(1 to 25, (i: Int) => {
      println(s"async(1) i: ${i}")
    })

    for{
      seq1 <- async.map(1 to 10, (i: Int) => i * 5)
      seq2 <- async.filter(seq1, (i: Int) => i % 2 == 0)
    } yield {
      println(s"seq2: ${seq2}")
    }
  }
}

