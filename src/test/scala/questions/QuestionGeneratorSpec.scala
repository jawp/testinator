package questions

import org.scalatest.{FreeSpec, MustMatchers}

class QuestionGeneratorSpec extends FreeSpec with MustMatchers {

  "QuestionGenerator generates" - {

    "correct question" in new Fixture {
      questionAndAnswer(-2, 10, Plus).expectedAnswer mustBe 8
      questionAndAnswer(-2, 10, Minus).expectedAnswer mustBe -12
      questionAndAnswer(-2, 10, Multiply).expectedAnswer mustBe -20
    }

    "question with one of the three operators: plus, minus, multiplied by:" in new Fixture {
      val qa = next
      List(Plus.name, Minus.name, Multiply.name).count(qa.question.contains) mustBe 1
    }
  }

  class Fixture extends QuestionGenerator
}
