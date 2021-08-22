package endpoint

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives.{complete, pathEnd}
import akka.http.scaladsl.server.Route

/**
 * Basic test endpoint
 */
object TestEndpoint extends ApiEndpoint {
  /**
   * Get the routes for the endpoint
   *
   * @return the endpoint's routes
   */
  override def getRoutes: Route =
    pathEnd {
      complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "test"))
    }
}
