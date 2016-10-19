package questions

import org.scalatest.{FreeSpec, MustMatchers}

class QuestionGeneratorSpec extends FreeSpec with MustMatchers {

  "QuestionGenerator generates" - {
    "complex questions" in new Fixture {
      val qa = questionGenerator.next
      qa.question.contains(s"${qa.expectedAnswer}") mustBe false
    }

    "question with one of the three operators: plus, minus, multiplied by:" in new Fixture {
      val qa = questionGenerator.next
      List(Plus.name, Minus.name, Multiply.name).count(qa.question.contains) mustBe 1
    }

    "Operations make sense" in {
      Plus.compute(-2, 10) mustBe 8
      Minus.compute(-2, 10) mustBe -12
      Multiply.compute(-2, 10) mustBe -20
    }
  }

  class Fixture {
    val questionGenerator = new QuestionGenerator
  }

}
