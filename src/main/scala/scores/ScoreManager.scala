package scores


trait ScoreManager {
  def nextQuestion(token: Token): String
}

class SimpleScoreManager(correctTokens: List[Token], maxQuestions: Int) extends ScoreManager {
  var i = 0

  def nextQuestion(token: Token) =
    if (isKnown(token)) {
      i += 1
      s"what is $i"
    } else "Broken token: " + token

  def answer(token: Token, answer: String) =
    if (isKnown(token)) {
      if (isCorrect(answer)) {
        if (isFinished) "You have finished" else "pass"
      } else "fail"
    } else "Broken token: " + token

  private def isCorrect(answer: String) = answer == s"$i"
  private def isFinished = i >= maxQuestions
  private def isKnown(token: Token) = correctTokens.contains(token)

}

case class Token(value: String)