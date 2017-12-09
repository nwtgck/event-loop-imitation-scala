# Imitation of Event Loop

This is just a imitation of Event Loop in Scala. Interface is inspired by Node.js.

## Features

* Single-thread asynchronous
* No `java.lang.Thread`, No `scala.concurrent.Future`
* No third-party libraries

## Usages

### Foreach

```scala
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
```


#### Output

```
async(1) i: 1
async(2) i: 100
async(1) i: 2
async(2) i: 101
async(1) i: 3
async(2) i: 102
async(1) i: 4
async(2) i: 103
async(1) i: 5
async(2) i: 104
async(1) i: 6
async(2) i: 105
async(1) i: 7
async(1) i: 8
async(1) i: 9
async(1) i: 10
```

### Map

```scala
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
```

#### output

```
async(1) i: 1
async(1) i: 2
async(1) i: 3
async(1) i: 4
async(1) i: 5
async(1) i: 6
mappedSeq: List(1, 4, 9, 16, 25)
async(1) i: 7
async(1) i: 8
async(1) i: 9
async(1) i: 10
```

### SetTimeout

```scala
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
```

#### output

```
(50msec)   setTimeout2
(100msec)  setTimeout3
(1000msec) setTimeout1
```



### Monadic - async-like in JavaScript

```scala
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
```

#### output

```
async(1) i: 1
async(1) i: 2
async(1) i: 3
async(1) i: 4
async(1) i: 5
async(1) i: 6
async(1) i: 7
async(1) i: 8
async(1) i: 9
async(1) i: 10
async(1) i: 11
async(1) i: 12
async(1) i: 13
async(1) i: 14
async(1) i: 15
async(1) i: 16
async(1) i: 17
async(1) i: 18
async(1) i: 19
async(1) i: 20
async(1) i: 21
seq2: List(10, 20, 30, 40, 50)
async(1) i: 22
async(1) i: 23
async(1) i: 24
async(1) i: 25
```

### Pipe - copy a file

```scala
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
```

#### output


```
async(1) i: 1
async(1) i: 2
async(1) i: 3
Copied!
async(1) i: 4
async(1) i: 5
async(1) i: 6
async(1) i: 7
async(1) i: 8
async(1) i: 9
async(1) i: 10
```