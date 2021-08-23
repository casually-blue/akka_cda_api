package endpoint

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives.{complete, get, pathEnd}
import akka.http.scaladsl.server.{Route, StandardRoute}
import com.azure.identity.{ClientSecretCredential, ClientSecretCredentialBuilder}
import com.microsoft.graph.authentication.TokenCredentialAuthProvider
import com.microsoft.graph.requests.GraphServiceClient
import okhttp3.Request

import java.util.Properties
import scala.jdk.CollectionConverters._

class GraphEndpoint extends ApiEndpoint {
  val properties = new Properties()
  properties.load(this.getClass.getClassLoader.getResourceAsStream("application.properties"))
  val clientId: String = properties.getProperty("graph.client")
  val secretKey: String = properties.getProperty("graph.secret")
  val tenantId: String = properties.getProperty("graph.tenant")
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

  def getUsers: StandardRoute = {

    var req = graphClient.users.buildRequest().get

    val users = req.getCurrentPage.asScala.toList
    var nextPage = req.getNextPage
    while(nextPage != null){
      req = nextPage.buildRequest().get()
      users.appendedAll(req.getCurrentPage.asScala.toList)
      nextPage = req.getNextPage
    }

    val json = "[" + ((users map (user => {
      s"""
         |{
         | "UserPrincipalName": "${user.userPrincipalName}",
         | "FirstName": "${user.givenName}",
         | "LastName": "${user.surname}"
         |}
         |""".stripMargin
    }) fold "") ((x, y) => x + y + ",") dropRight 1) + "]"


    complete { HttpEntity(ContentTypes.`application/json`, json) }
  }

  override def getRoutes: Route = pathEnd {
    get {
      getUsers
    }
  }
}


