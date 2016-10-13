package di

import akka.actor.ActorSystem
import akka.event.NoLogging
import akka.stream.ActorMaterializer
import service.Service
import tokens.SimpleTokenGenerator

object TestModule {

  implicit val system = ActorSystem("test")
  implicit val materializer = ActorMaterializer()

  val tokenGenerator = new SimpleTokenGenerator

  val service = new Service(tokenGenerator) {
    override val logger = NoLogging
  }

}