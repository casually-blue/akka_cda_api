package endpoint

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives.{complete, get, pathEnd}
import akka.http.scaladsl.server.{Route, StandardRoute}
import com.azure.identity.{ClientSecretCredential, ClientSecretCredentialBuilder}
import com.fasterxml.jackson.databind.ObjectMapper
import com.microsoft.graph.authentication.TokenCredentialAuthProvider
import com.microsoft.graph.requests.GraphServiceClient
import okhttp3.Request

import java.util
import java.util.Properties
import scala.jdk.CollectionConverters._
import scala.language.postfixOps


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

  def getUser: StandardRoute = {

    var req = graphClient.users().buildRequest().get()
    val users = new util.ArrayList(req.getCurrentPage)
    var nextPage = req.getNextPage
    while(nextPage != null){
      req = nextPage.buildRequest().get()
      users.addAll(req.getCurrentPage)
      nextPage = req.getNextPage
    }

    val json = users.asScala.map( user => {
      s"""{
         | "UserPrincipalName": "${user.userPrincipalName}",
         | "FirstName": "${user.givenName}",
         | "LastName": "${user.surname}"
         |}""".stripMargin
    }).fold("")((x,y) => x+y+",")


    complete { HttpEntity(ContentTypes.`application/json`, "[" + json.dropRight(1) + "]") }
  }

  override def getRoutes: Route = pathEnd {
    get {
      getUser
    }
  }
}
