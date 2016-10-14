package scores


trait ScoreManager {
  def nextQuestion(token: Token): String
}

class SimpleScoreManager(correctTokens: List[Token], maxQuestions: Int) extends ScoreManager {
  var i = 0

  def nextQuestion(token: Token) =
    if (correctTokens.contains(token)) {
      i += 1
      s"what is $i"
    } else throw new RuntimeException(s"unknown token $token")

  def answer(answer: String) = if(isFinished) "You have finished" else "pass"

  private def isFinished = i >= maxQuestions
}

case class Token(value: String)