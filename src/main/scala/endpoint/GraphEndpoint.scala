package endpoint

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives.{complete, get, pathEnd}
import akka.http.scaladsl.server.{Route, StandardRoute}
import com.azure.identity.{ClientSecretCredential, ClientSecretCredentialBuilder}
import com.fasterxml.jackson.databind.ObjectMapper
import com.microsoft.graph.authentication.TokenCredentialAuthProvider
import com.microsoft.graph.requests.GraphServiceClient
import okhttp3.Request

import java.util.Properties
import scala.jdk.CollectionConverters._


class GraphEndpoint extends ApiEndpoint {
  val properties = new Properties()
  properties.load(this.getClass.getClassLoader.getResourceAsStream("auth.properties"))

  val clientId: String = properties.getProperty("client")
  val secretKey: String = properties.getProperty("secret")
  val tenantId: String = properties.getProperty("tenant")
  val scopes: java.util.List[String] = List("https://graph.microsoft.com/.default").asJava

  val credential: ClientSecretCredential = new ClientSecretCredentialBuilder()
      .clientId(clientId)
      .clientSecret(secretKey)
      .tenantId(tenantId)
      .build()

  val authProvider = new TokenCredentialAuthProvider(scopes, credential)

  val graphClient: GraphServiceClient[Request] = GraphServiceClient
      .builder()
      .authenticationProvider(authProvider)
      .buildClient()

  def getUser: StandardRoute = {
    val mapper = new ObjectMapper()

    val req = graphClient.users().buildRequest().get()
    complete { HttpEntity(ContentTypes.`application/json`, mapper.writeValueAsString(req)) }
  }

  override def getRoutes: Route = pathEnd {
    get {
      getUser
    }
  }
}
