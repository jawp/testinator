package scores

import org.scalatest._

class ScoreManagerSpec extends FreeSpec with MustMatchers {

  "for correct token" - {
    "generate next questions, then finish" in new Fixture {
      nextQuestion(correctToken) mustBe "what is 1"
      answer("1") mustBe "pass"

      nextQuestion(correctToken) mustBe "what is 2"
      answer("2") mustBe "pass"

      nextQuestion(correctToken) mustBe "what is 3"
      answer("3") mustBe "You have finished"
    }
  }

  "for incorrect token" - {
    "don't generate question" in new Fixture {
      intercept[RuntimeException] {
        nextQuestion(badToken)
      }.getMessage mustBe s"unknown token $badToken"
    }
  }

  class Fixture extends SimpleScoreManager(correctToken :: Nil, 3)

  lazy val correctToken = Token("anyCorrectToken")
  lazy val badToken = Token("anyIncorrectToken")

}
