import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

import routing.Router._

/**
 * The main application class.
 *
 * Runs a Akka http server on port 9000
 */
object HTTPMain extends App {
  implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "cda-system")
  implicit val executionContext: ExecutionContextExecutor = system.executionContext


  /**
   * Stores the server binding object
   */
  val bindingFuture = Http().newServerAt(interface="0.0.0.0", port=9000).bind(routes)

  var command: Option[String] = None
  while(!command.contains("exit")){
    command = Some(StdIn.readLine(">> "))
  }

  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())
}
