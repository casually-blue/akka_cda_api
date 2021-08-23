package endpoint

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Route, StandardRoute}
import io.github.casuallyblue.cucm._
import com.fasterxml.jackson.databind.ObjectMapper

import java.util.Properties
import javax.xml.ws.BindingProvider
import scala.jdk.CollectionConverters.MapHasAsScala

class CUCMEndpoint extends ApiEndpoint {
  val client: AXLPort = new AXLAPIService().getAXLPort()

  private var authenticated = false

  val properties = new Properties()
  properties.load(this.getClass.getClassLoader.getResourceAsStream("application.properties"))

  client.asInstanceOf[BindingProvider].getRequestContext.put(
    BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
    properties.getProperty("cucm.endpoint")
  )

  def login(username: String, password: String): StandardRoute = {
    val ctx = client.asInstanceOf[BindingProvider].getRequestContext.asScala
    ctx(BindingProvider.USERNAME_PROPERTY) = username
    ctx(BindingProvider.PASSWORD_PROPERTY) = password

    authenticated = true

    complete { StatusCodes.OK }
  }

  def getPhones(name_filter: String): StandardRoute = {
    val req = new ListPhoneReq {
        this.searchCriteria = new ListPhoneReq.SearchCriteria {
          this.setName(name_filter)
        }
    }

    val phones = client.listPhone(req).getReturn.getPhone
    val phonesString = new ObjectMapper().writeValueAsString(phones)

    complete { HttpEntity(ContentTypes.`application/json`, phonesString) }
  }

  override def getRoutes: Route = concat(
    pathEnd(get(complete(StatusCodes.OK, "CUCM Base Rest Endpoint"))),

    path("login")(
      get(parameters("username", "password")(login))
    ),

    path("phones") {
      if (authenticated)
        get(parameters(Symbol("name_filter").as[String])(getPhones))
      else
        complete(StatusCodes.Forbidden, "Must Login First")
    }
  )
}
