package scores

import questions.{QuestionAndAnswer, QuestionGenerator}
import tokens._

class ScoreManager(tokenGenerator: TokenGenerator, questionGenerator: QuestionGenerator, maxQuestions: Int) {

  private val scoreCards = collection.mutable.Map[Token, ScoreCard]()

  def nextToken(name: String): Token = {
    val nextToken = tokenGenerator.nextToken
    scoreCards.getOrElseUpdate(nextToken, ScoreCard())
    nextToken
  }

  def nextQuestion(token: Token): String = withScoreCard(token) {
    case ScoreCard(_, Some(q), _) => s"You already got the question, but OK, once again: ${q.question}"
    case ScoreCard(score, None, _) => generateNewQuestion(token, score)
  }

  def answer(token: Token, answer: String): String = withScoreCard(token) {
    case ScoreCard(_, None, _) => "There's no pending question ..."
    case ScoreCard(score, Some(question), _) =>
      if (question has answer) scoreUp(token, score) else spoil(token)
  }

  private def scoreUp(token: Token, score: Int) = {
    val newCard = ScoreCard(score + 1, None)
    scoreCards(token) = newCard
    if (newCard.isComplete) "You have finished" else "pass"
  }

  private def generateNewQuestion(token: Token, score: Int) = {
    val qna = questionGenerator.next
    scoreCards(token) = ScoreCard(score, Some(qna))
    qna.question
  }

  private def spoil(token: Token) = {
    scoreCards(token) = scoreCards(token).copy(isSpoilt = true)
    "fail"
  }

  private def withScoreCard(token: Token)(f: ScoreCard => String) =
    scoreCards.get(token) match {
      case Some(card) if card.isComplete => "Test is complete. Generate a new Token if you want to restart"
      case Some(card) if card.isSpoilt => "Token is spoilt. Please generate new token to restart."
      case Some(card) => f(card)
      case None => "Broken token: " + token.value
    }

  case class ScoreCard(score: Int = 0, question: Option[QuestionAndAnswer] = None, isSpoilt: Boolean = false) {
    def isComplete = score >= maxQuestions
  }

}

