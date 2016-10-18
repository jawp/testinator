package service

import akka.event.NoLogging
import akka.http.scaladsl.model.ContentTypes._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import model.HomePage
import org.scalatest._
import org.specs2.mock.Mockito
import questions.{QuestionAndAnswer, QuestionGenerator}
import scores.ScoreManager
import tokens.{Token, TokenGenerator}

class ServiceSpec extends FreeSpec with MustMatchers with ScalatestRouteTest with Mockito {

  "Testinator should " - {

    "show home page" in new Fixture {
      Get("/") ~> routes ~> check {
        status mustBe OK
        contentType mustBe `text/xml(UTF-8)`
        responseAs[String] mustBe HomePage.content.mkString
      }
    }

    "start test by generating token, generate question and answer" in new Fixture {
      Get(s"/startTest/$name") ~> routes ~> check {
        status mustBe OK
        contentType mustBe `text/plain(UTF-8)`
        responseAs[String] mustBe "Hi " + name + s". Your token is: 12345"
      }

      Get(s"/12345/nextQuestion") ~> routes ~> check {
        status mustBe OK
        contentType mustBe `text/plain(UTF-8)`
        responseAs[String] mustBe "what year is it?"
      }

      Get(s"/12345/answer/2016") ~> routes ~> check {
        status mustBe OK
        contentType mustBe `text/plain(UTF-8)`
        responseAs[String] mustBe "pass"
      }
    }
  }

  val name = "John"

  class Fixture {

    val tokenGenerator = mock[TokenGenerator]
    tokenGenerator.nextTokenFor(name) returns Token("12345")

    val questionGenerator = mock[QuestionGenerator]
    questionGenerator.next returns QuestionAndAnswer("what year is it?", 2016)

    val scoreManager = new ScoreManager(tokenGenerator, questionGenerator, 10)
    val service = new Service(scoreManager) {
      override val logger = NoLogging
    }
    val routes = service.routes
  }

}