package scores

import tokens._


class ScoreManager(tokenGenerator: TokenGenerator, maxQuestions: Int) {

  private val scores = collection.mutable.Map[Token, Int]()

  def nextToken(name: String): Token = {
    val nextToken = tokenGenerator.nextToken
    scores.getOrElseUpdate(nextToken, 0)
    nextToken
  }

  def nextQuestion(token: Token) = scores.get(token) match {
    case Some(score) => s"what is ${score} ?"
    case None => "Broken token: " + token
  }

  def answer(token: Token, answer: String) = scores.get(token) match {
    case Some(score) =>
      if (isCorrect(token, answer)) {
        scores(token) += 1
        if (isFinished(token)) "You have finished" else "pass"
      } else "fail"
    case None => "Broken token: " + token
  }

  private def isCorrect(token: Token, answer: String) = answer == s"${scores(token)}"
  private def isFinished(token: Token) = scores(token) >= maxQuestions
}
