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
        val token = nextToken(smith)
        nextQuestion(token) mustBe "what is 1 + 1 ?"
        answer(token, "2") mustBe "pass"

        val otherToken = nextToken(johnson)
        nextQuestion(otherToken) mustBe "what is 1 + 1 ?"
        answer(otherToken, "anyWrongAnswer") mustBe "fail"

        nextQuestion(token) mustBe "what is 1 + 1 ?"
        answer(token, "2") mustBe "You have finished"
      }

      "don't generate other questions until first one is answered" in new Fixture {
        val token = nextToken(smith)
        nextQuestion(token) mustBe "what is 1 + 1 ?"
        nextQuestion(token) mustBe "You already got the question, but OK, once again: what is 1 + 1 ?"
        nextQuestion(token) mustBe "You already got the question, but OK, once again: what is 1 + 1 ?"
        answer(token, "2") mustBe "pass"

        nextQuestion(token) mustBe "what is 1 + 1 ?"
      }

      "after pass" - {
        "question should expire. You should be able to generate a new one" in new Fixture {
          val token = nextToken(smith)
          nextQuestion(token) mustBe "what is 1 + 1 ?"
          answer(token, "2") mustBe "pass"

          answer(token, "any") mustBe "There's no pending question ..."

          nextQuestion(token) mustBe "what is 1 + 1 ?"
          answer(token, "2") mustBe "You have finished"
        }
      }

      "after fail" - {
        "don't accept answers or generate questions. Only a new token makes restart possible" in new Fixture {
          val token = nextToken(smith)
          nextQuestion(token) mustBe "what is 1 + 1 ?"
          answer(token, "wrongAnswer") mustBe "fail"

          nextQuestion(token) mustBe "Token is spoilt. Please generate a new token to restart."
          answer(token, "any") mustBe "Token is spoilt. Please generate a new token to restart."

          assertRestartTestWithNewTokenFor(smith)
        }
      }

      "after finish" - {
        "don't accept answers or generate questions. Only a new token makes restart possible" in new Fixture {
          val token = nextToken(smith)
          nextQuestion(token) mustBe "what is 1 + 1 ?"
          answer(token, "2") mustBe "pass"

          nextQuestion(token) mustBe "what is 1 + 1 ?"
          answer(token, "2") mustBe "You have finished"

          answer(token, "any") mustBe "Test is complete. Please generate a new token if you want to restart"
          nextQuestion(token) mustBe "Test is complete. Please generate a new token if you want to restart"

          assertRestartTestWithNewTokenFor(smith)
        }
      }

      "after re-generating token" - {
        "generate and return the new one, but also keep the old one" in new Fixture {
          val token = nextToken(smith)
          nextQuestion(token) mustBe "what is 1 + 1 ?"
          answer(token, "2") mustBe "pass"

          val anotherToken = nextToken(smith)
          nextQuestion(anotherToken) mustBe "what is 1 + 1 ?"
          answer(anotherToken, "2") mustBe "pass"

          nextQuestion(token) mustBe "what is 1 + 1 ?"
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
      nextToken(smith) mustBe Token("0")
      nextToken(smith) mustBe Token("1")
      nextToken(smith) mustBe Token("2")
    }
  }


  class Fixture {
    val questionGenerator = mock[QuestionGenerator]
    questionGenerator.next returns QuestionAndAnswer("what is 1 + 1 ?", 2)

    val scoreManager = new ScoreManager(new IncrementingTokenGenerator, questionGenerator, 2)

    def nextToken = scoreManager.nextToken _

    def nextQuestion = scoreManager.nextQuestion _

    def answer = scoreManager.answer _

    def assertRestartTestWithNewTokenFor(name: String) = {
      val newToken = nextToken(name)
      nextQuestion(newToken) mustBe "what is 1 + 1 ?"
      answer(newToken, "2") mustBe "pass"
      nextQuestion(newToken) mustBe "what is 1 + 1 ?"
      answer(newToken, "2") mustBe "You have finished"
    }
  }

  class IncrementingTokenGenerator extends TokenGenerator {
    private val gen = new AtomicInteger()

    override def nextTokenFor(name: String) = Token(gen.getAndIncrement().toString)
  }

  private val smith = "smith"
  private val johnson = "johnson"

  private val badToken = Token("anyIncorrectToken")
}
