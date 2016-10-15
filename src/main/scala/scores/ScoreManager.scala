package scores

import questions.{QuestionAndAnswer, QuestionGenerator}
import tokens._

object Messages {
  val testComplete = "Test is complete. Generate a new Token if you want to restart"
  val finished = "You have finished"
  val pass = "pass"
  val noQuestion = "There's no pending question ..."
  def broken(token: Token) = "Broken token: " + token.value
}

class ScoreManager(tokenGenerator: TokenGenerator, questionGenerator: QuestionGenerator, maxQuestions: Int) {
  import Messages._

  private val scoreCards = collection.mutable.Map[Token, ScoreCard]()

  def nextToken(name: String): Token = {
    val nextToken = tokenGenerator.nextToken
    scoreCards.getOrElseUpdate(nextToken, ScoreCard())
    nextToken
  }

  def nextQuestion(token: Token): String = scoreCards.get(token) match {
    case Some(card) if card.isComplete => testComplete
    case Some(ScoreCard(_, Some(q))) => q.question
    case Some(card) =>
      val question = questionGenerator.next
      scoreCards(token) = ScoreCard(card.score, Some(question))
      question.question
    case None => broken(token)
  }

  def answer(token: Token, answer: String): String = scoreCards.get(token) match {
    case Some(card) if card.isComplete => testComplete
    case Some(ScoreCard(_, None)) => noQuestion
    case Some(ScoreCard(score, Some(question))) =>
      if (isCorrect(question, answer)) {
        val newCard = ScoreCard(score + 1, None)
        scoreCards(token) = newCard
        if (newCard.isComplete) finished else pass
      } else "fail"
    case None => broken(token)
  }

  private def isCorrect(question: QuestionAndAnswer, answer: String) = answer == s"${question.expectedAnswer}"

  case class ScoreCard(score: Int = 0, question: Option[QuestionAndAnswer] = None) {
    def isComplete = score >= maxQuestions
  }
}

