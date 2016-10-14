package scores

import org.scalatest._

class ScoreManagerSpec extends FreeSpec with MustMatchers {

  "for correct token" - {
    "generate next questions, then finish" in new Fixture {
      nextQuestion(token1) mustBe "what is 1"
      answer(token1,"1") mustBe "pass"

      nextQuestion(token1) mustBe "what is 2"
      answer(token1, "2") mustBe "pass"

      nextQuestion(token1) mustBe "what is 3"
      answer(token1, "3") mustBe "You have finished"
    }

    "generate next questions, then fail" in new Fixture {
      nextQuestion(token1) mustBe "what is 1"
      answer(token1, "1") mustBe "pass"

      nextQuestion(token1) mustBe "what is 2"
      answer(token1, "wrongAnswer") mustBe "fail"
    }
  }

  "for incorrect token" - {
    "don't generate question" in new Fixture {
      intercept[RuntimeException] {
        nextQuestion(badToken)
      }.getMessage mustBe s"unknown token $badToken"
    }

    "don't accept answer" in new Fixture {
      intercept[RuntimeException] {
        answer(badToken, "anyAnswer")
      }.getMessage mustBe s"unknown token $badToken"
    }
  }

  class Fixture extends SimpleScoreManager(token1 :: token2 :: Nil, 3)

  lazy val token1 = Token("token_1")
  lazy val token2 = Token("token_2")

  lazy val badToken = Token("anyIncorrectToken")

}
