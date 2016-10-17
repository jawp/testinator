package tokens

import scala.util.Random

class TokenGenerator(size: Int = 10) {
  def nextTokenFor(name: String) = Token(Random.alphanumeric.take(size).mkString, name)
}


case class Token(value: String, name: String)