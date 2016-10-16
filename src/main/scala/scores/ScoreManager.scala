package scores

import model.Messages._
import questions.{QuestionAndAnswer, QuestionGenerator}
import tokens._

class ScoreManager(tokenGenerator: TokenGenerator, questionGenerator: QuestionGenerator, maxQuestions: Int) {

  private val scoreCards = collection.mutable.Map[Token, ScoreCard]()

  def nextToken(name: String): Token = {
    //removing old tokens for name
    val tokensToInvalid = scoreCards.filterKeys( _.name == name)
    tokensToInvalid.foreach{ case(t, _) => scoreCards -= t}

    //adding new token for name
    val newToken = tokenGenerator.nextTokenFor(name)
    scoreCards += (newToken -> ScoreCard())
    newToken
  }

  def nextQuestion(token: Token): String = withValidTokenAndActiveScoreCard(token) {
    case ScoreCard(_, Some(q), _) => s"You already got the question, but OK, once again: ${q.question}"
    case ScoreCard(score, None, _) => generateNewQuestion(token, score)
  }

  def answer(token: Token, answer: String): String = withValidTokenAndActiveScoreCard(token) {
    case ScoreCard(_, None, _) => noPendingQuestion
    case ScoreCard(score, Some(question), _) =>
      if (question accepts answer) scoreUp(token, score) else spoil(token)
  }

  private def scoreUp(token: Token, score: Int) = {
    val newCard = ScoreCard(score + 1, None)
    scoreCards(token) = newCard
    if (newCard.isComplete) youHaveFinished else pass
  }

  private def generateNewQuestion(token: Token, score: Int) = {
    val qna = questionGenerator.next
    scoreCards(token) = ScoreCard(score, Some(qna))
    qna.question
  }

  private def spoil(token: Token) = {
    scoreCards(token) = scoreCards(token).copy(isSpoilt = true)
    fail
  }

  private def withValidTokenAndActiveScoreCard(token: Token)(f: ScoreCard => String) = scoreCards.get(token) match {
    case Some(card) if card.isComplete => testComplete
    case Some(card) if card.isSpoilt => tokenSpoilt
    case Some(card) => f(card)
    case None => "Broken token: " + token.value
  }

  case class ScoreCard(score: Int = 0, question: Option[QuestionAndAnswer] = None, isSpoilt: Boolean = false) {
    def isComplete = score >= maxQuestions
  }

}

