package scores

import java.util.concurrent.atomic.AtomicInteger

import org.scalatest._
import org.specs2.mock.Mockito
import questions.{QuestionAndAnswer, QuestionGenerator}
import tokens.{Token, TokenGenerator}

class ScoreManagerSpec extends FreeSpec with MustMatchers with Mockito {

  "for token generated" - {

    "when question is generated" - {

      "pass with correct answer" in new Fixture {
        val token = nextToken(smith)
        assertQuestionPass(token)
      }
      "finish with last correct answer" in new Fixture {
        val token = nextToken(smith)
        assertQuestionPass(token)
        assertQuestionFinish(token)
      }
      "fail with bad answer" in new Fixture {
        val token = nextToken(smith)
        assertQuestionFail(token)
      }

      "don't generate other questions until first one is answered" in new Fixture {
        val token = nextToken(smith)
        nextQuestion(token) mustBe "what year is it ?"
        nextQuestion(token) mustBe "You already got the question, but OK, once again: what year is it ?"
        nextQuestion(token) mustBe "You already got the question, but OK, once again: what year is it ?"
        answer(token, "2016") mustBe "pass"
      }

      "after pass" - {
        "question should expire. You should be able to generate a new one" in new Fixture {
          val token = nextToken(smith)
          assertQuestionPass(token)
          answer(token, "any") mustBe "There's no pending question ..."
          assertQuestionFinish(token)
        }
      }

      "after fail" - {
        "don't accept answers or generate questions. Only a new token makes restart possible" in new Fixture {
          val token = nextToken(smith)
          assertQuestionFail(token)
          nextQuestion(token) mustBe "Token is spoilt. Please generate a new token to restart."
          answer(token, "any") mustBe "Token is spoilt. Please generate a new token to restart."
          assertCompleteTestWithNewTokenFor(smith)
        }
      }

      "after finish" - {
        "don't accept answers or generate questions. Only a new token makes restart possible" in new Fixture {
          val token = assertCompleteTestWithNewTokenFor(smith)
          answer(token, "any") mustBe "Test is complete. Please generate a new token if you want to restart"
          nextQuestion(token) mustBe "Test is complete. Please generate a new token if you want to restart"
          assertCompleteTestWithNewTokenFor(smith)
        }
      }

      "after generating token for a new person" - {
        "let the first person continue the test" in new Fixture {
          val token = nextToken(smith)
          assertQuestionPass(token)

          val otherToken = nextToken(johnson)
          assertQuestionFail(otherToken)

          assertQuestionFinish(token)
        }
      }

      "after re-generating token for the same person" - {
        "generate and return the new one, but also keep the old token" in new Fixture {
          val token = nextToken(smith)
          assertQuestionPass(token)

          val anotherToken = nextToken(smith)
          assertQuestionPass(anotherToken)

          assertQuestionFinish(token)
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

    "generate next token each time, even for the same person" in new Fixture {
      nextToken(smith) mustBe Token("0")
      nextToken(smith) mustBe Token("1")
      nextToken(johnson) mustBe Token("2")
      nextToken(johnson) mustBe Token("3")
    }
  }


  class Fixture {
    val questionGenerator = mock[QuestionGenerator]
    questionGenerator.next returns QuestionAndAnswer("what year is it ?", 2016)

    val scoreManager = new ScoreManager(new IncrementingTokenGenerator, questionGenerator, 2)

    def assertCompleteTestWithNewTokenFor(name: String) = {
      val token = nextToken(name)
      assertQuestionPass(token)
      assertQuestionFinish(token)
      token
    }

    def assertQuestionPass(token: Token) = {
      nextQuestion(token) mustBe "what year is it ?"
      answer(token, "2016") mustBe "pass"
    }

    def assertQuestionFinish(token: Token) = {
      nextQuestion(token) mustBe "what year is it ?"
      answer(token, "2016") mustBe "You have finished"
    }

    def assertQuestionFail(token: Token) = {
      nextQuestion(token) mustBe "what year is it ?"
      answer(token, "wrongAnswer") mustBe "fail"
    }

    def nextToken = scoreManager.nextToken _

    def nextQuestion = scoreManager.nextQuestion _

    def answer = scoreManager.answer _
  }

  class IncrementingTokenGenerator extends TokenGenerator {
    private val gen = new AtomicInteger()

    override def nextTokenFor(name: String) = Token(gen.getAndIncrement().toString)
  }

  private val smith = "smith"
  private val johnson = "johnson"

  private val badToken = Token("anyIncorrectToken")
}
