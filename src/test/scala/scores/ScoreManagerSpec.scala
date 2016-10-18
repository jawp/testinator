package scores

import java.util.concurrent.atomic.AtomicInteger

import org.scalatest._
import org.specs2.mock.Mockito
import questions.{QuestionAndAnswer, QuestionGenerator}
import tokens.{Token, TokenGenerator}

class ScoreManagerSpec extends FreeSpec with MustMatchers with Mockito {

  "for token generated" - {

    "when question is generated" - {

      "finish after all correct answers (regardless other tokens activities)" in new Fixture {
        val token = scoreManager.nextToken(smith)
        val otherToken = scoreManager.nextToken(johnson)

        scoreManager.nextQuestion(token) mustBe "what is 1 + 1 ?"
        scoreManager.answer(token, "2") mustBe "pass"

        scoreManager.nextQuestion(otherToken) mustBe "what is 1 + 1 ?"
        scoreManager.answer(otherToken, "anyWrongAnswer") mustBe "fail"

        scoreManager.nextQuestion(token) mustBe "what is 1 + 1 ?"
        scoreManager.answer(token, "2") mustBe "You have finished"
      }

      "fail at wrong answer and spoil the token" in new Fixture {
        val token = scoreManager.nextToken(smith)
        scoreManager.nextQuestion(token) mustBe "what is 1 + 1 ?"
        scoreManager.answer(token, "wrongAnswer") mustBe "fail"

        scoreManager.nextQuestion(token) mustBe "Token is spoilt. Please generate a new token to restart."
        scoreManager.answer(token, "any") mustBe "Token is spoilt. Please generate a new token to restart."
      }

      "don't generate other questions until first one is answered" in new Fixture {
        val token = scoreManager.nextToken(smith)
        scoreManager.nextQuestion(token) mustBe "what is 1 + 1 ?"
        scoreManager.nextQuestion(token) mustBe "You already got the question, but OK, once again: what is 1 + 1 ?"
        scoreManager.nextQuestion(token) mustBe "You already got the question, but OK, once again: what is 1 + 1 ?"
        scoreManager.answer(token, "2") mustBe "pass"

        scoreManager.nextQuestion(token) mustBe "what is 1 + 1 ?"
      }

      "after pass" - {
        "question should expire. You should be able to generate a new one" in new Fixture {
          val token = scoreManager.nextToken(smith)
          scoreManager.nextQuestion(token) mustBe "what is 1 + 1 ?"
          scoreManager.answer(token, "2") mustBe "pass"

          scoreManager.answer(token, "any") mustBe "There's no pending question ..."

          scoreManager.nextQuestion(token) mustBe "what is 1 + 1 ?"
          scoreManager.answer(token, "2") mustBe "You have finished"
        }
      }

      "after finish" - {
        "don't accept answers or generate questions. Only a new token makes restart possible" in new Fixture {
          val token = scoreManager.nextToken(smith)
          scoreManager.nextQuestion(token) mustBe "what is 1 + 1 ?"
          scoreManager.answer(token, "2") mustBe "pass"

          scoreManager.nextQuestion(token) mustBe "what is 1 + 1 ?"
          scoreManager.answer(token, "2") mustBe "You have finished"

          scoreManager.answer(token, "any") mustBe "Test is complete. Please generate a new token if you want to restart"
          scoreManager.nextQuestion(token) mustBe "Test is complete. Please generate a new token if you want to restart"

          val newToken = scoreManager.nextToken(smith)
          scoreManager.nextQuestion(newToken) mustBe "what is 1 + 1 ?"
          scoreManager.answer(newToken, "2") mustBe "pass"

          scoreManager.nextQuestion(newToken) mustBe "what is 1 + 1 ?"
          scoreManager.answer(newToken, "2") mustBe "You have finished"
        }
      }

      "after re-generating token" - {
        "generate and return the new one, but also keep the old one" in new Fixture {
          val token = scoreManager.nextToken(smith)
          scoreManager.nextQuestion(token) mustBe "what is 1 + 1 ?"
          scoreManager.answer(token, "2") mustBe "pass"

          val anotherToken = scoreManager.nextToken(smith)
          scoreManager.nextQuestion(anotherToken) mustBe "what is 1 + 1 ?"
          scoreManager.answer(anotherToken, "2") mustBe "pass"

          scoreManager.nextQuestion(token) mustBe "what is 1 + 1 ?"
          scoreManager.answer(token, "2") mustBe "You have finished"
        }
      }
    }

    "when question is not generated yet" - {

      "don't accept any answer" in new Fixture {
        val token = scoreManager.nextToken(smith)
        scoreManager.answer(token, "any") mustBe "There's no pending question ..."
      }
    }
  }

  "for no token generated" - {

    "don't generate question" in new Fixture {
      scoreManager.nextQuestion(badToken) mustBe s"Broken token: ${badToken.value}"
    }

    "don't accept any answer" in new Fixture {
      scoreManager.answer(badToken, "any") mustBe s"Broken token: ${badToken.value}"
    }

    "generate next token each time" in new Fixture {
      scoreManager.nextToken(smith) mustBe Token("0")
      scoreManager.nextToken(smith) mustBe Token("1")
      scoreManager.nextToken(smith) mustBe Token("2")
    }
  }


  class Fixture {
    val questionGenerator = mock[QuestionGenerator]
    questionGenerator.next returns QuestionAndAnswer("what is 1 + 1 ?", 2)

    val incrementingTokenGenerator = new TokenGenerator {
      private val generator = new AtomicInteger()
      override def nextTokenFor(name: String) = Token(generator.getAndIncrement().toString)
    }

    val scoreManager = new ScoreManager(incrementingTokenGenerator, questionGenerator, 2)
  }

  private val smith = "smith"
  private val johnson = "johnson"

  private val badToken = Token("anyIncorrectToken")
}
