import io.github.nwtgck.event_loop.{Async, EventLoop}

object Main extends EventLoop {

  /**
    * Main of event loop
    *
    * @param args
    */
  override protected[this] def entryPoint(args: Array[String]): Unit = {

    if(false) {
      val async = new Async(this: EventLoop)

      async.foreach(1 to 10, (i: Int) => {
        println(s"async(1) i: ${i}")
      })

      async.foreach(1 to 10, (i: Int) => {
        println(s"async(2) i: ${i}")
      })

      async.map(
        Seq(1, 2, 3, 4, 5),
        (i: Int) => {
          i * i
        },
        (mapedSeq: Seq[Int]) => {
          println(s"mapedSeq: ${mapedSeq}")
        }
      )

      async.map(
        Vector(1, 2, 3, 4, 5),
        (i: Int) => {
          i * i
        },
        (mapedSeq: Seq[Int]) => {
          println(s"mapedSeq: ${mapedSeq}")
        }
      )

      async.map(
        1 to 100,
        (i: Int) => {
          i * i
        },
        (mapedSeq: Seq[Int]) => {
          println(s"mapedSeq: ${mapedSeq}")
        }
      )

      async.filter(
        1 to 20,
        (i: Int) => {
          i % 2 == 0
        },
        (filteredSeq: Seq[Int]) => {
          println(s"filteredSeq: ${filteredSeq}")
        }
      )

      setTimeout(_ => {
        println("(1000) hello, world1")
      }, 1000)

      setTimeout(_ => {
        println("(1000) hello, world2")
        nextTick {
          println("nextTick1")
        }
      }, 1000)

      setTimeout(_ => {
        println("(1000) hello, world3")
      }, 1000)
    }

    setTimeout(_ => {
      println("(0) setTimeout1")
    }, 0)

    setTimeout(_ => {
      println("(0) setTimeout2")
    }, 0)

    setTimeout(_ => {
      println("(0) setTimeout3")
    }, 0)

    nextTick{
      println("nextTick1")
      nextTick{
        println("nextTick4")
      }
    }

    nextTick{
      println("nextTick2")
    }

    nextTick{
      println("nextTick3")
    }

  }
}
