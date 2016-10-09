package di

import tokens._

trait ConfigDI {
  val tokenGenerator: TokenGenerator
}


class ProdConfigDI extends ConfigDI {
  val tokenGenerator: TokenGenerator = new RandomTokenGenerator
}


class TestConfigDI extends ConfigDI {
  val tokenGenerator: TokenGenerator = new SimpleTokenGenerator
}
