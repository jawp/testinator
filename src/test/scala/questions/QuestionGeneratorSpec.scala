package questions

import org.scalatest.{FreeSpec, MustMatchers}

class QuestionGeneratorSpec extends FreeSpec with MustMatchers {

  "SimpleQuestionGenerator generates" - {

    "simple questions" in new Fixture {
      val qa: QuestionAndAnswer = simple.next
      qa.question.contains(s"${qa.expectedAnswer}") mustBe true
    }

    "questions sequentially" in new Fixture {
      simple.next mustBe QuestionAndAnswer("what is 0 ?", 0)
      simple.next mustBe QuestionAndAnswer("what is 1 ?", 1)
      simple.next mustBe QuestionAndAnswer("what is 2 ?", 2)
    }
  }

  "RealQuestionGenerator generates" - {
    "complex questions" in new Fixture {
      val qa = real.next
      qa.question.contains(" + ") mustBe true
      qa.question.contains(s"${qa.expectedAnswer}") mustBe false
    }
  }


  class Fixture {
    val simple = new SimpleQuestionGenerator
    val real = new RealQuestionGenerator
  }

}
