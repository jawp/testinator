package tokens

import java.util.concurrent.atomic.AtomicInteger
import scala.util.Random

trait TokenGenerator {
  def nextToken: String
}

class RandomTokenGenerator(size: Int = 10) extends TokenGenerator {
  def nextToken = Random.alphanumeric.take(size).mkString
}

class SimpleTokenGenerator extends TokenGenerator {
  private val tokenGenerator = new AtomicInteger()

  def nextToken = tokenGenerator.getAndIncrement().toString
}
