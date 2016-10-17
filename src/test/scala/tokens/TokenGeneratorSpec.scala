package tokens

import org.scalatest.{FreeSpec, MustMatchers}

class TokenGeneratorSpec extends FreeSpec with MustMatchers {

  "RandomTokenGenerator" - {
    s"generates strings of one size" in new Fixture {
      tokenGenerator.nextTokenFor(smith).value.length mustBe tokenSize
      tokenGenerator.nextTokenFor(smith).value.length mustBe tokenSize
    }
    s"generates different tokens each time" in new Fixture {
      tokenGenerator.nextTokenFor(smith) == tokenGenerator.nextTokenFor(smith) mustBe false
    }
  }

  class Fixture {
    val tokenSize = 5
    val tokenGenerator = new TokenGenerator(tokenSize)

    val smith = "smith"
  }

}
