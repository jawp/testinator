package tokens

import org.scalatest.{FreeSpec, MustMatchers}

class TokenGeneratorSpec extends FreeSpec with MustMatchers{

  "SimpleTokenGenerator" - {
    "generates integer tokens sequentially" in new Fixture {
      simple.nextToken mustBe Token("0")
      simple.nextToken mustBe Token("1")
      simple.nextToken mustBe Token("2")
    }
  }

  "RandomTokenGenerator" - {
    s"generates strings of one size" in new Fixture {
      random.nextToken.value.length mustBe tokenSize
      random.nextToken.value.length mustBe tokenSize
    }
    s"generates different tokens each time" in new Fixture {
      random.nextToken == random.nextToken mustBe false
    }
  }

  class Fixture{
    val tokenSize = 5
    val random = new RandomTokenGenerator(tokenSize)

    val simple = new SimpleTokenGenerator
  }

}
