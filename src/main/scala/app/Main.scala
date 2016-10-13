package app

import com.typesafe.config.Config
import di.ProductionModule
import service.Service

class Main(service: Service, config: Config) {

  def start() = service.start(config.getString("http.interface"), config.getInt("http.port"))
}

object Main extends App {
  val app: Main = ProductionModule.app
  app.start()
}
