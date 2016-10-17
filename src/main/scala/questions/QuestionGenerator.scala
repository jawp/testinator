package questions

import scala.util.Random

class QuestionGenerator {

  def next: QuestionAndAnswer = {
    val n1 = nextInt
    val n2 = nextInt
    QuestionAndAnswer(s"What is the value of $n1 + $n2 ?", n1 + n2)
  }

  private def nextInt = Random.nextInt(100)
}

case class QuestionAndAnswer(question: String, expectedAnswer: Int) {
  def accepts(answer: String) = answer == s"$expectedAnswer"
}