package net.omd

import doobie._
import doobie.implicits._
import doobie.util.ExecutionContexts
// import cats._
// import cats.data._
import cats.effect._
// import cats.implicits._
// import fs2.Stream

object SelectData extends IOApp {

// We need a ContextShift[IO] before we can construct a Transactor[IO]. The passed ExecutionContext
// is where nonblocking operations will be executed. For testing here we're using a synchronous EC.
  private implicit val cs = IO.contextShift(ExecutionContexts.synchronous)

// A transactor that gets connections from java.sql.DriverManager and executes blocking operations
// on an our synchronous EC. See the chapter on connection handling for more info.
  private val xa = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver", // driver classname
    "jdbc:postgresql://172.22.0.2:5432/postgres", // connect URL (driver-specific)
    "postgres", // user
    "example", // password
    Blocker.liftExecutionContext(ExecutionContexts.synchronous) // just for testing
  )

  override def run(args: List[String]): IO[ExitCode] =
    for {
      ns <- sql"select name from users".query[String].stream.compile.toList.transact(xa)
      _  <- IO(println(s"Output $ns"))
    } yield ExitCode.Success
}
