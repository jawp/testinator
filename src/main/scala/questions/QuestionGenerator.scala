package questions

import java.util.concurrent.atomic.AtomicInteger

import scala.util.Random

trait QuestionGenerator {
  def next: QuestionAndAnswer
}

class SimpleQuestionGenerator extends QuestionGenerator {

  def next: QuestionAndAnswer = {
    val n = nextInt
    QuestionAndAnswer(s"what is $n ?", n)
  }

  private def nextInt = generator.getAndIncrement()
  private val generator = new AtomicInteger()
}

class RealQuestionGenerator extends QuestionGenerator {

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