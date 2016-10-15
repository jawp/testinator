package scores

import tokens._

object Messages {
  val testComplete = "Test is complete. Generate a new Token if you want to restart"
  val finished = "You have finished"
  val pass = "pass"
  val noQuestion = "There's no pending question ..."
  def broken(token: Token) = "Broken token: " + token.value
}

class ScoreManager(tokenGenerator: TokenGenerator, maxQuestions: Int) {
  import Messages._

  private val scoreCards = collection.mutable.Map[Token, ScoreCard]()

  def nextToken(name: String): Token = {
    val nextToken = tokenGenerator.nextToken
    scoreCards.getOrElseUpdate(nextToken, ScoreCard())
    nextToken
  }

  def nextQuestion(token: Token): String = scoreCards.get(token) match {
    case Some(card) if card.isComplete => testComplete
    case Some(card) =>
      val question = Question(s"what is ${card.score} ?")
      scoreCards(token) = ScoreCard(card.score, Some(question))
      question.value
    case None => broken(token)
  }

  def answer(token: Token, answer: String): String = scoreCards.get(token) match {
    case Some(card) if card.isComplete => testComplete
    case Some(ScoreCard(_, None)) => noQuestion
    case Some(card) =>
      if (isCorrect(token, answer)) {
        val newCard = ScoreCard(card.score + 1, None)
        scoreCards(token) = newCard
        if (newCard.isComplete) finished else pass
      } else "fail"
    case None => broken(token)
  }

  private def isCorrect(token: Token, answer: String) = answer == s"${scoreCards(token).score}"

  case class ScoreCard(score: Int = 0, question: Option[Question] = None) {
    def isComplete = score >= maxQuestions
  }
  case class Question(value: String)
}

