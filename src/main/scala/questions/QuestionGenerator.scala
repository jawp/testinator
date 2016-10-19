package questions

import scala.util.Random

class QuestionGenerator {

  def next: QuestionAndAnswer = {
    val n1 = nextInt
    val n2 = nextInt
    val op = randomOp
    QuestionAndAnswer("What is " + n1 + " " + op.name + " " + n1 + "?", op.compute(n1, n2))
  }

  private def nextInt = Random.nextInt() % 9 + 1

  private def randomOp: Operation = (Random.nextInt() % 3).abs match {
    case 0 => Plus
    case 1 => Minus
    case 2 => Multiply
  }
}

case class QuestionAndAnswer(question: String, expectedAnswer: Int) {
  def accepts(answer: String) = answer == s"$expectedAnswer"
}

case class Operation(name: String, compute: (Int, Int) => Int)

object Plus extends Operation("plus", _ + _)

object Minus extends Operation("minus", _ - _)

object Multiply extends Operation("multiplied by", _ * _)
