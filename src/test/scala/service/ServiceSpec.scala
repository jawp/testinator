package service

import akka.event.NoLogging
import akka.http.scaladsl.model.ContentTypes._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import model.HomePage
import org.scalatest._
import scores.ScoreManager
import tokens.SimpleTokenGenerator

class ServiceSpec extends FreeSpec with MustMatchers with ScalatestRouteTest {

  "Testinator should " - {

    "show home page" in new Fixture {
      Get("/") ~> routes ~> check {
        status mustBe OK
        contentType mustBe `text/xml(UTF-8)`
        responseAs[String] mustBe HomePage.content.mkString
      }
    }

    "return new token" in new Fixture {
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

  class Fixture {

    val tokenGenerator = new SimpleTokenGenerator
    val scoreManager = new ScoreManager(tokenGenerator, 10)
    val service = new Service(scoreManager) {
      override val logger = NoLogging
    }
    val routes = service.routes
  }

  val name = "John"
}