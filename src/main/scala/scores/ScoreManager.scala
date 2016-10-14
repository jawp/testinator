package scores


trait ScoreManager {
  def nextQuestion(token: String): String
}

class SimpleScoreManager(correctTokens: List[String], questions: Int) extends ScoreManager {
  var i = 0

  def nextQuestion(token: String) =
    if (correctTokens.contains(token)) {
      i += 1
      s"what is $i"
    } else throw new RuntimeException(s"unknown token $token")
}
