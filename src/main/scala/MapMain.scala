import io.github.nwtgck.event_loop.{Async, EventLoop, Promise}

object MapMain extends EventLoop {

  override protected[this] def entryPoint(args: Array[String]): Unit = {

    val async = new Async(this: EventLoop)

    async.foreach(1 to 10, (i: Int) => {
      println(s"async(1) i: ${i}")
    })

    async.map(
      Seq(1, 2, 3, 4, 5),
      (i: Int) => {
        i * i
      }
    ).andThen((mappedSeq: Seq[Int]) => Promise.resolved{
      println(s"mappedSeq: ${mappedSeq}")
    })
  }
}

