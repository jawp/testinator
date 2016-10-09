package tokens

import java.util.concurrent.atomic.AtomicInteger

trait TokenManager {
  def nextToken: String
}

class IncrementingTokenManager extends TokenManager {
  private val tokenGenerator = new AtomicInteger()

  override def nextToken = tokenGenerator.getAndIncrement().toString
}
