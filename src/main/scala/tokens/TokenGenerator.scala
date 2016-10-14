package tokens

import java.util.concurrent.atomic.AtomicInteger
import scala.util.Random

trait TokenGenerator {
  def nextToken: Token
}

class RandomTokenGenerator(size: Int = 10) extends TokenGenerator {
  def nextToken = Token(Random.alphanumeric.take(size).mkString)
}

class SimpleTokenGenerator extends TokenGenerator {
  private val generator = new AtomicInteger()

  def nextToken = Token(generator.getAndIncrement().toString)
}

case class Token(value: String)