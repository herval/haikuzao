package us.hervalicio.ai.lstm

import java.util.Random

class NumericDistribution {
  private val random = new Random()

  /**
    * Given a probability distribution over discrete classes, sample from the distribution
    * and return the generated class index.
    *
    * @param distribution Probability distribution over classes. Must sum to 1.0
    */
  def sample(distribution: Array[Double]): Int = {
    val d: Double = random.nextDouble
    var sum = 0.0
    distribution.zipWithIndex.foreach { case(n, i) =>
        sum += n
        if(d <= sum) {
          return i
        }
    }
    throw new IllegalArgumentException("Distribution is invalid? d=" + d + ", sum=" + sum)
  }
}