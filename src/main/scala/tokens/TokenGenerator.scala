package tokens

import scala.util.Random

class TokenGenerator(size: Int = 10) {
  def nextTokenFor(name: String): Token = Token(Random.alphanumeric.take(size).mkString)
}

case class Token(value: String)