package us.hervalicio.ai.neural;

import java.util.Random;

/**
 * Created by herval on 10/30/15.
 */
public class NumericDistribution {
    private Random random = new Random(12345);

    /**
     * Given a probability distribution over discrete classes, sample from the distribution
     * and return the generated class index.
     *
     * @param distribution Probability distribution over classes. Must sum to 1.0
     */
    public int sample(double[] distribution) {
        double d = random.nextDouble();
        double sum = 0.0;
        for (int i = 0; i < distribution.length; i++) {
            sum += distribution[i];
            if (d <= sum) return i;
        }
        //Should never happen if distribution is a valid probability distribution
        throw new IllegalArgumentException("Distribution is invalid? d=" + d + ", sum=" + sum);
    }
}
