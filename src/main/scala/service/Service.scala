package service

import akka.actor.ActorSystem
import akka.event.LoggingAdapter
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.xml.ScalaXmlSupport._
import akka.http.scaladsl.server.Directives._
import akka.stream.Materializer
import model.HomePage
import tokens.TokenGenerator

abstract class Service(tokenGenerator: TokenGenerator)(
  implicit val system: ActorSystem,
  implicit val materializer: Materializer
) {

  def start(host: String, port: Int) = Http().bindAndHandle(routes, host, port)

  val logger: LoggingAdapter

  val routes = {
    logRequestResult("akka-http-microservice") {
      path("") {
        get(complete(HomePage.content))
      } ~
        pathPrefix("startTest") {
          (get & path(Segment)) { name =>
            complete {
              nextTokenFor(name)
            }
          }
        }
    }
  }

  private def nextTokenFor(name: String) = s"Hi $name. Your token is: ${tokenGenerator.nextToken}"
}

