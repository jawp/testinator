package scores

import org.scalatest._
import tokens.{SimpleTokenGenerator, Token}

class ScoreManagerSpec extends FreeSpec with MustMatchers {

  "for token obtained" - {

    "when question is known" - {

      "finish successfully" in new Fixture {
        val token = nextToken(smith)
        nextQuestion(token) mustBe "what is 1"
        answer(token, "1") mustBe "pass"

        nextQuestion(token) mustBe "what is 2"
        answer(token, "2") mustBe "You have finished"
      }

      "finish successfully (regardless other tokens activities)" in new Fixture {
        val token = nextToken(smith)
        val token2 = nextToken(johnson)

        nextQuestion(token) mustBe "what is 1"
        answer(token, "1") mustBe "pass"

        nextQuestion(token2) mustBe "what is 1"
        answer(token2, "anyWrongAnswer") mustBe "fail"

        nextQuestion(token) mustBe "what is 2"
        answer(token, "2") mustBe "You have finished"
      }

      "fail before finishing" in new Fixture {
        val token = nextToken(smith)
        nextQuestion(token) mustBe "what is 1"
        answer(token, "1") mustBe "pass"

        nextQuestion(token) mustBe "what is 2"
        answer(token, "wrongAnswer") mustBe "fail"
      }
    }

    "when question is unknown" - {

      "don't accept any answer" in new Fixture {
        pending
        val token = nextToken(smith)
        answer(token, "anyAnswer") mustBe "how can you answer when you don't know question yet?"
      }
    }
  }

  "for no token obtained" - {

    "don't generate question" in new Fixture {
      nextQuestion(badToken) mustBe s"Broken token: $badToken"
    }

    "don't accept any answer" in new Fixture {
      answer(badToken, "anyAnswer") mustBe s"Broken token: $badToken"
    }

    "generate next token each time" in new Fixture {
      nextToken(smith) mustBe Token("0")
      nextToken(smith) mustBe Token("1")
      nextToken(smith) mustBe Token("2")
    }
  }

  class Fixture extends ScoreManager(new SimpleTokenGenerator, 2)

  private val badToken = Token("anyIncorrectToken")

  private val smith = "smith"
  private val johnson = "johnson"

}
