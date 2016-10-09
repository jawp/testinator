import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import di._

class Main extends Service(new ProdConfigDI) {

  val config = ConfigFactory.load()

  override implicit val system = ActorSystem("testinator")
  override implicit val materializer = ActorMaterializer()
  override val logger = Logging(system, getClass)

  def start() = Http().bindAndHandle(routes, config.getString("http.interface"), config.getInt("http.port"))
}

object Main extends App  {
  (new Main).start()
}
