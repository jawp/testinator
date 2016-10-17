package scores

import org.scalatest._
import questions.SimpleQuestionGenerator
import tokens.{SimpleTokenGenerator, Token}

class ScoreManagerSpec extends FreeSpec with MustMatchers {

  "for token generated" - {

    "when question is generated" - {

      "finish after all correct answers (regardless other tokens activities)" in new Fixture {
        val token = nextToken(smith)
        val otherToken = nextToken(johnson)

        nextQuestion(token) mustBe "what is 0 ?"
        answer(token, "0") mustBe "pass"

        nextQuestion(otherToken) mustBe "what is 1 ?"
        answer(otherToken, "anyWrongAnswer") mustBe "fail"

        nextQuestion(token) mustBe "what is 2 ?"
        answer(token, "2") mustBe "You have finished"
      }

      "fail at wrong answer and spoil the token" in new Fixture {
        val token = nextToken(smith)
        nextQuestion(token) mustBe "what is 0 ?"
        answer(token, "wrongAnswer") mustBe "fail"

        nextQuestion(token) mustBe "Token is spoilt. Please generate a new token to restart."
        answer(token, "any") mustBe "Token is spoilt. Please generate a new token to restart."
      }

      "don't generate other questions until first one is answered" in new Fixture {
        val token = nextToken(smith)
        nextQuestion(token) mustBe "what is 0 ?"
        nextQuestion(token) mustBe "You already got the question, but OK, once again: what is 0 ?"
        nextQuestion(token) mustBe "You already got the question, but OK, once again: what is 0 ?"
        answer(token, "0") mustBe "pass"

        nextQuestion(token) mustBe "what is 1 ?"
      }

      "after pass" - {
        "question should expire. You should be able to generate a new one" in new Fixture {
          val token = nextToken(smith)
          nextQuestion(token) mustBe "what is 0 ?"
          answer(token, "0") mustBe "pass"

          answer(token, "any") mustBe "There's no pending question ..."

          nextQuestion(token) mustBe "what is 1 ?"
          answer(token, "1") mustBe "You have finished"
        }
      }

      "after finish" - {
        "don't accept answers or generate questions. Only a new token makes restart possible" in new Fixture {
          val token = nextToken(smith)
          nextQuestion(token) mustBe "what is 0 ?"
          answer(token, "0") mustBe "pass"

          nextQuestion(token) mustBe "what is 1 ?"
          answer(token, "1") mustBe "You have finished"

          answer(token, "any") mustBe "Test is complete. Please generate a new token if you want to restart"
          nextQuestion(token) mustBe "Test is complete. Please generate a new token if you want to restart"

          val newToken = nextToken(smith)
          nextQuestion(newToken) mustBe "what is 2 ?"
          answer(newToken, "2") mustBe "pass"

          nextQuestion(newToken) mustBe "what is 3 ?"
          answer(newToken, "3") mustBe "You have finished"
        }

      }

      "after re-generating token" - {
        "generate and return the new one, but also keep the old one" in new Fixture{
          val token = nextToken(smith)
          nextQuestion(token) mustBe "what is 0 ?"
          answer(token, "0") mustBe "pass"

          val anotherToken = nextToken(smith)
          nextQuestion(anotherToken) mustBe "what is 1 ?"
          answer(anotherToken, "1") mustBe "pass"

          nextQuestion(token) mustBe "what is 2 ?"
          answer(token, "2") mustBe "You have finished"
        }
      }
    }

    "when question is not generated yet" - {

      "don't accept any answer" in new Fixture {
        val token = nextToken(smith)
        answer(token, "any") mustBe "There's no pending question ..."
      }
    }
  }

  "for no token generated" - {

    "don't generate question" in new Fixture {
      nextQuestion(badToken) mustBe s"Broken token: ${badToken.value}"
    }

    "don't accept any answer" in new Fixture {
      answer(badToken, "any") mustBe s"Broken token: ${badToken.value}"
    }

    "generate next token each time" in new Fixture {
      nextToken(smith) mustBe Token("0", smith)
      nextToken(smith) mustBe Token("1", smith)
      nextToken(smith) mustBe Token("2", smith)
    }
  }

  class Fixture extends ScoreManager(new SimpleTokenGenerator, new SimpleQuestionGenerator, 2)

  private val smith = "smith"
  private val johnson = "johnson"

  private val badToken = Token("anyIncorrectToken", smith)
}
