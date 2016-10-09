import akka.actor.ActorSystem
import akka.event.LoggingAdapter
import akka.http.scaladsl.marshallers.xml.ScalaXmlSupport._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.PathMatchers.Segment
import akka.stream.Materializer
import di.ConfigDI
import model.HomePage
import model.Messages._

abstract class Service(config: ConfigDI) extends Protocols {

  implicit val system: ActorSystem
  implicit val materializer: Materializer

  val logger: LoggingAdapter

  val routes = {
    logRequestResult("akka-http-microservice") {
      path("") {
        get(complete(HomePage.content))
      } ~
        pathPrefix("startTest") {
          (get & path(Segment)) { name =>
            complete {nextTokenFor(name)}
          }
        }
    }
  }

  private def nextTokenFor(name: String) = s"Hi $name. Your token is: ${config.tokenManager.nextToken}"
}

