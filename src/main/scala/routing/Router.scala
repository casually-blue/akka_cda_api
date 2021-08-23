package routing

import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import endpoint.{ApiEndpoint, CUCMEndpoint, GraphEndpoint, TestEndpoint}

/**
 * The main router class for the application
 */
object Router  {
  /**
   * Endpoints and their api base url relative to the root of the server
   */
  val endPoints: List[(String, ApiEndpoint)] = List(
    ("cucm", new CUCMEndpoint("https://cdacucmpub.coramdeo.local:8443/axl/")),
    ("graph", new GraphEndpoint),
    ("test", TestEndpoint)
  )

  /**
   * Get the akka routing object for all the endpoints
   * @return The compiled route object
   */
  def routes: Route = endPoints
    .foldLeft(
      // Base Route
      pathEndOrSingleSlash {
        get {
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, views.Home.render))
        }
      }
    ) {
      (route,endpoint) => {
        // Register each route into the router
        val (name, endpointRoute) = endpoint
        route ~ pathPrefix(name)(endpointRoute.getRoutes)
      }
    }


}

