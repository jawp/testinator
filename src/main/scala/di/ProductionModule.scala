package di

import akka.actor.ActorSystem
import akka.event.Logging
import akka.stream.ActorMaterializer
import app.Main
import com.typesafe.config.ConfigFactory
import questions.QuestionGenerator
import scores.ScoreManager
import service.Service
import tokens._

object ProductionModule {

  val numberOfQuestions = 10

  implicit val system = ActorSystem("testinator")
  implicit val materializer = ActorMaterializer()

  val config = ConfigFactory.load()

  val tokenGenerator = new RandomTokenGenerator

  val questionGenerator = new QuestionGenerator

  val scoreManager = new ScoreManager(tokenGenerator, questionGenerator, numberOfQuestions)

  val service = new Service(scoreManager) {
    override val logger = Logging(system, getClass)
  }

  val app: Main = new Main(service, config)

}


