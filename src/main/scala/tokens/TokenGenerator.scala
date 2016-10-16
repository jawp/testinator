package tokens

import java.util.concurrent.atomic.AtomicInteger
import scala.util.Random

trait TokenGenerator {
  def nextTokenFor(name: String) : Token
}

class RandomTokenGenerator(size: Int = 10) extends TokenGenerator {
  def nextTokenFor(name: String) = Token(Random.alphanumeric.take(size).mkString, name)
}

class SimpleTokenGenerator extends TokenGenerator {
  private val generator = new AtomicInteger()

  def nextTokenFor(name: String)  = Token(generator.getAndIncrement().toString, name)
}

case class Token(value: String, name: String)