package net.omd

import cats.effect._

object FirstExample extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = IO(ExitCode.Success)
}
