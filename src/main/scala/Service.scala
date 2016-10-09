import akka.actor.ActorSystem
import akka.event.LoggingAdapter
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.marshallers.xml.ScalaXmlSupport._
import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.PathMatchers.Segment
import akka.stream.Materializer
import di.ProductionDependencies._
import model.HomePage
import model.Messages._

import scala.concurrent.{ExecutionContextExecutor, Future}

trait Service extends Protocols {
  implicit val system: ActorSystem
  implicit def executor: ExecutionContextExecutor
  implicit val materializer: Materializer

  val logger: LoggingAdapter

  def greet(nameToGreet: String): Future[Either[String, GreetingResult]] = Future {
    Right(GreetingResult(s"Hello, $nameToGreet!"))
  }

  val routes = {
    logRequestResult("akka-http-microservice") {
      path("") {
        get(complete(HomePage.content))
      } ~
        pathPrefix("startTest") {
          (get & path(Segment)) { name =>
            complete {nextTokenFor(name)}
          }
        } ~
        pathPrefix("name") {
          (get & path(Segment)) { name =>
            complete {
              greet(name).map[ToResponseMarshallable] {
                case Left(errorMsg) => BadRequest -> errorMsg
                case Right(result) => result
              }
            }
          }
        }
    }
  }

  private def nextTokenFor(name: String) = s"Hi $name. Your token is: ${tokenManager.nextToken}"
}

