package endpoint

import akka.http.scaladsl.model.StatusCodes.NotFound
import akka.http.scaladsl.server.Directives.{complete, get, pathEnd}
import akka.http.scaladsl.server.Route

object GraphEndpoint extends ApiEndpoint {
  override def getRoutes: Route = pathEnd {
    get {
      complete(NotFound, "404")
    }
  }
}
