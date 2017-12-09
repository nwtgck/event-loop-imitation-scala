package io.github.nwtgck.event_loop

abstract class WritableStream{
  def write(data: String): Unit
  def end(): Unit
}
