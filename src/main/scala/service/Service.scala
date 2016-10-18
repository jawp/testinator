package service

import akka.actor.ActorSystem
import akka.event.LoggingAdapter
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.xml.ScalaXmlSupport._
import akka.http.scaladsl.server.Directives._
import akka.stream.Materializer
import model.HomePage
import scores.ScoreManager
import tokens.Token

abstract class Service(scoreManager: ScoreManager)(
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
        pathPrefix("startTest" / Segment) {
          name => complete(nextTokenFor(name))
        } ~
        pathPrefix(Segment / "nextQuestion") {
          token => complete(nextQuestionFor(token))
        } ~
        pathPrefix(Segment / "answer" / Segment) {
          (token, answer) => complete(answerFor(token, answer))
        }
    }
  }

  private def nextTokenFor(name: String) = s"Hi $name. Your token is: ${scoreManager.nextToken(name).value}"

  private def nextQuestionFor(token: String) = scoreManager.nextQuestion(Token(token))

  private def answerFor(token: String, answer: String) = scoreManager.answer(Token(token), answer)
}

