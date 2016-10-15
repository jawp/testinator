package scores

import tokens._


class ScoreManager(tokenGenerator: TokenGenerator, maxQuestions: Int) {

  private val scoreCards = collection.mutable.Map[Token, ScoreCard]()

  def nextToken(name: String): Token = {
    val nextToken = tokenGenerator.nextToken
    scoreCards.getOrElseUpdate(nextToken, ScoreCard())
    nextToken
  }

  def nextQuestion(token: Token) = scoreCards.get(token) match {
    case Some(card) => s"what is ${card.score} ?"
    case None => "Broken token: " + token
  }

  def answer(token: Token, answer: String) = scoreCards.get(token) match {
    case Some(card) =>
      if (isCorrect(token, answer)) {
        scoreCards(token) = ScoreCard(card.score + 1, card.question)
        if (isFinished(card)) "You have finished" else "pass"
      } else "fail"
    case None => "Broken token: " + token
  }

  private def isCorrect(token: Token, answer: String) = answer == s"${scoreCards(token).score}"

  private def isFinished(card: ScoreCard) = card.score + 1 >= maxQuestions
}

case class ScoreCard(score: Int = 0, question: Option[Question] = None)
case class Question(value: String)