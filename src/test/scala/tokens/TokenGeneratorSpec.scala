package tokens

import org.scalatest.{FreeSpec, MustMatchers}

class TokenGeneratorSpec extends FreeSpec with MustMatchers{

  "SimpleTokenGenerator" - {
    "generates integer tokens sequentially" in new Fixture {
      simple.nextTokenFor(smith) mustBe Token("0", smith)
      simple.nextTokenFor(smith) mustBe Token("1", smith)
      simple.nextTokenFor(smith) mustBe Token("2", smith)
    }
  }

  "RandomTokenGenerator" - {
    s"generates strings of one size" in new Fixture {
      random.nextTokenFor(smith).value.length mustBe tokenSize
      random.nextTokenFor(smith).value.length mustBe tokenSize
    }
    s"generates different tokens each time" in new Fixture {
      random.nextTokenFor(smith) == random.nextTokenFor(smith) mustBe false
    }
  }

  class Fixture{
    val tokenSize = 5
    val random = new RandomTokenGenerator(tokenSize)

    val simple = new SimpleTokenGenerator

    val smith = "smith"
  }

}
