package questions

import org.scalatest.{FreeSpec, MustMatchers}

class QuestionGeneratorSpec extends FreeSpec with MustMatchers {

  "QuestionGenerator generates" - {
    "complex questions" in new Fixture {
      val qa = questionGenerator.next
      qa.question.contains(" + ") mustBe true
      qa.question.contains(s"${qa.expectedAnswer}") mustBe false
    }
  }

  class Fixture {
    val questionGenerator = new QuestionGenerator
  }
}
