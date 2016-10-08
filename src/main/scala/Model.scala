import spray.json.DefaultJsonProtocol

object Model {

  case class GreetingResult(name: String)

  trait Protocols extends DefaultJsonProtocol {
    implicit val ipInfoFormat = jsonFormat1(GreetingResult.apply)
  }

}
