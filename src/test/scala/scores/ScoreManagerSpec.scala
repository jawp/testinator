package scores

import org.scalatest._

class ScoreManagerSpec extends FreeSpec with MustMatchers {

  "generates next questions for correct token, and finishes" in new Fixture {
    nextQuestion(correctTokens.head) mustBe "what is 1"
    answer("1") mustBe "pass"

    nextQuestion(correctTokens.head) mustBe "what is 2"
    answer("2") mustBe "pass"

    nextQuestion(correctTokens.head) mustBe "what is 3"
    answer("3") mustBe "You have finished"
  }

  "doesn't generate question for bad token" in new Fixture {
    intercept[RuntimeException] {
      nextQuestion(badToken)
    }.getMessage mustBe s"unknown token $badToken"
  }


  class Fixture(nbOfQuestions: Int = 3) extends SimpleScoreManager(correctTokens, nbOfQuestions) {

  }

  lazy val correctTokens = "anyCorrectToken" :: Nil
  lazy val badToken = "anyIncorrectToken"

}
