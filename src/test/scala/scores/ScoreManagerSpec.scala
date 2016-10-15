package scores

import org.scalatest._
import tokens.{SimpleTokenGenerator, Token}

class ScoreManagerSpec extends FreeSpec with MustMatchers {

  "for token obtained" - {

    "when question is known" - {

      "finish after all correct answers" in new Fixture {
        val token = nextToken(smith)
        nextQuestion(token) mustBe "what is 0 ?"
        answer(token, "0") mustBe "pass"

        nextQuestion(token) mustBe "what is 1 ?"
        answer(token, "1") mustBe "You have finished"
      }

      "finish after all correct answers (regardless other tokens activities)" in new Fixture {
        val token = nextToken(smith)
        val otherToken = nextToken(johnson)

        nextQuestion(token) mustBe "what is 0 ?"
        answer(token, "0") mustBe "pass"

        nextQuestion(otherToken) mustBe "what is 0 ?"
        answer(otherToken, "anyWrongAnswer") mustBe "fail"

        nextQuestion(token) mustBe "what is 1 ?"
        answer(token, "1") mustBe "You have finished"
      }

      "fail at wrong answer" in new Fixture {
        val token = nextToken(smith)
        nextQuestion(token) mustBe "what is 0 ?"
        answer(token, "0") mustBe "pass"

        nextQuestion(token) mustBe "what is 1 ?"
        answer(token, "wrongAnswer") mustBe "fail"
      }

      "after pass" - {
        "invalid question" in new Fixture {
          val token = nextToken(smith)
          nextQuestion(token) mustBe "what is 0 ?"
          answer(token, "0") mustBe "pass"

          answer(token, "anyAnswer") mustBe "There's no pending question ..."
        }
      }
    }

    "when question is unknown" - {

      "don't accept any answer" in new Fixture {
        val token = nextToken(smith)
        answer(token, "anyAnswer") mustBe "There's no pending question ..."
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
