import Messages
import akka.event.LoggingAdapter
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.PathMatchers.Segment
import akka.stream.Materializer
import com.typesafe.config.Config
import model.Messages

import scala.concurrent.{ExecutionContextExecutor, Future}

trait Service extends Protocols {
  implicit val system: ActorSystem
  implicit def executor: ExecutionContextExecutor
  implicit val materializer: Materializer
  def config: Config

  val logger: LoggingAdapter

  def greet(nameToGreet: String): Future[Either[String, GreetingResult]] = Future {
    Right(GreetingResult(s"Hello, $nameToGreet!"))
  }

  val routes = {
    logRequestResult("akka-http-microservice") {
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
}