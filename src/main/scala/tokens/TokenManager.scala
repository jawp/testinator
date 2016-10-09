package tokens

import java.util.concurrent.atomic.AtomicInteger
import scala.util.Random

trait TokenManager {
  def nextToken: String
}

class RandomTokenManager(size: Int = 10) extends TokenManager {
  def nextToken = Random.alphanumeric.take(size).mkString
}

class SimpleTokenManager extends TokenManager {
  private val tokenGenerator = new AtomicInteger()

  def nextToken = tokenGenerator.getAndIncrement().toString
}
