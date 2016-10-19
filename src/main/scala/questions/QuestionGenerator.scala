package questions

import scala.util.Random

class QuestionGenerator {

  def next: QuestionAndAnswer = {
    val n1 = nextInt
    val n2 = nextInt
    val plus = "+"
    QuestionAndAnswer("What is " + n1 + " " + plus + " " + n1 + "?", n1 + n2)
  }

  private def nextInt = Random.nextInt(100)
}

case class QuestionAndAnswer(question: String, expectedAnswer: Int) {
  def accepts(answer: String) = answer == s"$expectedAnswer"
}