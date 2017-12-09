object Main extends EventLoop {

  def nTimes(times: Int, process: Int => Unit): Unit = {
    if(times == -1){

    } else {
      // Execute process
      process(times)
      // Go to next step
      nextTick{
        nTimes(times-1, process)
      }

    }
  }

  def asyncForEach[A](seq: Seq[A], f: A => Unit): Unit = {
    seq.headOption match {
      case Some(elem) =>
        // Execute process
        f(elem)
        // Go to next step
        nextTick{
          asyncForEach(seq.drop(1), f)
        }
      case _ => () // End of looop
    }
  }

  /**
    * Main of event loop
    *
    * @param args
    */
  override protected[this] def entryPoint(args: Array[String]): Unit = {
    nTimes(10, i => {
      println(s"i: ${i}")
    })

    asyncForEach(1 to 10, (i: Int) => {
      println(s"async(1) i: ${i}")
    })

    asyncForEach(1 to 10, (i: Int) => {
      println(s"async(2) i: ${i}")
    })

  }
}
