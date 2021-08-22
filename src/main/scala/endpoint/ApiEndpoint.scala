package endpoint

import akka.http.scaladsl.server.Route

trait ApiEndpoint {
  def getRoutes: Route
}
