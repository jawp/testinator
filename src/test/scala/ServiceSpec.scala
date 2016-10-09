import akka.event.NoLogging
import akka.http.scaladsl.model.ContentTypes._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import model.HomePage
import org.scalatest._

class ServiceSpec extends FreeSpec with MustMatchers with ScalatestRouteTest with Service {

  "Testinator should " - {

    "show home page" in {
      Get("/") ~> routes ~> check {
        status mustBe OK
        contentType mustBe `text/xml(UTF-8)`
        responseAs[String] mustBe HomePage.content.mkString
      }
    }

    "return new token" in {
      Get(s"/startTest/$name") ~> routes ~> check {
        status mustBe OK
        contentType mustBe `text/plain(UTF-8)`
        responseAs[String] mustBe "Hi " + name + s". Your token is: 0"
      }

      Get(s"/startTest/$name") ~> routes ~> check {
        status mustBe OK
        contentType mustBe `text/plain(UTF-8)`
        responseAs[String] mustBe "Hi " + name + s". Your token is: 1"
      }

    }
  }

  private val name = "John"

  override val logger = NoLogging
  override def testConfigSource = "akka.loglevel = WARNING"
}