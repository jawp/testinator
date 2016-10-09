import akka.event.NoLogging
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.ContentTypes._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import model.HomePage
import model.Messages._
import org.scalatest._

class ServiceSpec extends FreeSpec with Matchers with ScalatestRouteTest with Service {

  "Testinator should " - {

    "respond to greeting request" in {
      Get(s"/name/$name") ~> routes ~> check {
        status shouldBe OK
        contentType shouldBe `application/json`
        responseAs[GreetingResult] shouldBe expectedResult
      }
    }

    "show home page" in {
      Get("/") ~> routes ~> check {
        status shouldBe OK
        contentType shouldBe `text/xml(UTF-8)`
        responseAs[String] shouldEqual HomePage.content.mkString
      }
    }

    "return new token" in {
      Get(s"/startTest/$name") ~> routes ~> check {
        status shouldBe OK
        contentType shouldBe `text/plain(UTF-8)`
        responseAs[String] shouldEqual "Hi " + name + s". Your token is: 0"
      }

      Get(s"/startTest/$name") ~> routes ~> check {
        status shouldBe OK
        contentType shouldBe `text/plain(UTF-8)`
        responseAs[String] shouldEqual "Hi " + name + s". Your token is: 1"
      }

    }
  }

  private val name = "John"
  private val expectedResult = GreetingResult(s"Hello, $name!")

  override val logger = NoLogging
  override def testConfigSource = "akka.loglevel = WARNING"
}