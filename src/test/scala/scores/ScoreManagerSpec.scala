package scores

import org.scalatest._

class ScoreManagerSpec extends FreeSpec with MustMatchers {

  "generates next questions for correct token" in {
    scoreManager.nextQuestion(correctTokens.head) mustBe "what is 1"
  }

  "doesn't generate question for bad token" in {
    intercept[RuntimeException] {
      scoreManager.nextQuestion(badToken)
    }.getMessage mustBe s"unknown token $badToken"
  }

  val correctTokens = "anyCorrectToken" :: Nil
  val badToken = "anyIncorrectToken"

  val scoreManager = new SimpleScoreManager(correctTokens, 3)
}
