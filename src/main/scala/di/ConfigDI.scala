package di

import tokens._

trait ConfigDI {
  val tokenManager: TokenManager
}


class ProdConfigDI extends ConfigDI {
  val tokenManager: TokenManager = new RandomTokenManager
}


class TestConfigDI extends ConfigDI {
  val tokenManager: TokenManager = new SimpleTokenManager
}
