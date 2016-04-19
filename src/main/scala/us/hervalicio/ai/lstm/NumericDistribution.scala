package us.hervalicio.ai.lstm

import java.util.Random

class NumericDistribution {
  private val random = new Random(12345)

  /**
    * Given a probability distribution over discrete classes, sample from the distribution
    * and return the generated class index.
    *
    * @param distribution Probability distribution over classes. Must sum to 1.0
    */
  def sample(distribution: Array[Double]): Int = {
    val d: Double = random.nextDouble
    val sum = (1 to distribution.length).foldLeft(0) { case (acc, i) =>
      val s = acc + i
      if (d <= s) {
        return i
      }
      s
    }
    throw new IllegalArgumentException("Distribution is invalid? d=" + d + ", sum=" + sum)
  }
}