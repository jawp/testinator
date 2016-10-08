import Messages
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.ContentTypes._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import model.Messages
import org.scalatest._

class ServiceSpec extends FlatSpec with Matchers with ScalatestRouteTest with Service {
  override def testConfigSource = "akka.loglevel = WARNING"
  override def config = testConfig
  override val logger = NoLogging

  val name = "John"
  val expectedResult = GreetingResult(s"Hello, $name!")

  "Testinator" should "respond to greeting request" in {
    Get(s"/name/$name") ~> routes ~> check {
      status shouldBe OK
      contentType shouldBe `application/json`
      responseAs[GreetingResult] shouldBe expectedResult
    }
  }
}
