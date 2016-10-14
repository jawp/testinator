package di

import akka.actor.ActorSystem
import akka.event.Logging
import akka.stream.ActorMaterializer
import app.Main
import com.typesafe.config.ConfigFactory
import scores.ScoreManager
import service.Service
import tokens._

object ProductionModule {

  implicit val system = ActorSystem("testinator")
  implicit val materializer = ActorMaterializer()

  val config = ConfigFactory.load()

  val tokenGenerator = new RandomTokenGenerator

  val scoreManager = new ScoreManager(tokenGenerator, 10)

  val service = new Service(scoreManager) {
    override val logger = Logging(system, getClass)
  }

  val app: Main = new Main(service, config)

}


