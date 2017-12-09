import io.github.nwtgck.event_loop.{Async, EventLoop, Fs, Promise}

object FsPipeMain extends EventLoop {

  override protected[this] def entryPoint(args: Array[String]): Unit = {
    val async = new Async(this: EventLoop)
    val fs    = new Fs(this: EventLoop)

    async
      .foreach(1 to 10, (i: Int) => Promise.resolved{
        println(s"async(1) i: ${i}")
      })

    fs.createReadStream("./build.sbt").pipe(fs.createWriteStream("copied_build.sbt")).andThen(_ => Promise.resolved{
      println("Copied!")
    })
  }
}

