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

    async.map(
      Seq(1, 2, 3, 4, 5),
      (i :Int) => {
        i * i
      },
      (mapedSeq: Seq[Int]) => {
        println(s"mapedSeq: ${mapedSeq}")
      }
    )

    async.map(
      Vector(1, 2, 3, 4, 5),
      (i :Int) => {
        i * i
      },
      (mapedSeq: Seq[Int]) => {
        println(s"mapedSeq: ${mapedSeq}")
      }
    )

    async.map(
      1 to 100,
      (i :Int) => {
        i * i
      },
      (mapedSeq: Seq[Int]) => {
        println(s"mapedSeq: ${mapedSeq}")
      }
    )
  }
}
