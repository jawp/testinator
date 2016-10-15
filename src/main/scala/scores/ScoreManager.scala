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

  def nextQuestion(token: Token): String = withUnfinishedScoreCard(token) {
    case ScoreCard(_, Some(q)) => q.question
    case ScoreCard(score, None) =>
      val question = questionGenerator.next
      scoreCards(token) = ScoreCard(score, Some(question))
      question.question
  }

  def answer(token: Token, answer: String): String = withUnfinishedScoreCard(token) {
    case ScoreCard(_, None) => "There's no pending question ..."
    case ScoreCard(score, Some(question)) =>
      if (isCorrect(question, answer)) {
        val newCard = ScoreCard(score + 1, None)
        scoreCards(token) = newCard
        if (newCard.isComplete) "You have finished" else "pass"
      } else "fail"
  }

  private def withUnfinishedScoreCard(token: Token)(f: ScoreCard => String) =
    scoreCards.get(token) match {
      case Some(card) if card.isComplete => "Test is complete. Generate a new Token if you want to restart"
      case Some(card) => f(card)
      case None => "Broken token: " + token.value
    }

  private def isCorrect(question: QuestionAndAnswer, answer: String) = answer == s"${question.expectedAnswer}"

  case class ScoreCard(score: Int = 0, question: Option[QuestionAndAnswer] = None) {
    def isComplete = score >= maxQuestions
  }

}

