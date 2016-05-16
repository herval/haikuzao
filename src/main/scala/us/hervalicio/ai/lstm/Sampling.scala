package us.hervalicio.ai.lstm

import org.nd4j.linalg.factory.Nd4j
import us.hervalicio.ai.text.CharacterMap

/**
  * Created by herval on 4/19/16.
  */
trait Sampling extends NetworkHolder {

  val characterMap: CharacterMap

  def sample(chars: Int, numSamples: Int): List[String] = {
    sampleFromNetwork(None, chars, numSamples)
  }

  private def sampleFromNetwork(
      start: Option[String], chars: Int, samples: Int): List[String] = {
    val initialization = start.getOrElse(characterMap.sampleChar().toString)

    val initializationInput =
      Nd4j.zeros(samples, characterMap.size, initialization.length)
    initialization.zipWithIndex.foreach {
      case (c, i) =>
        val idx = characterMap.indexOf(c)
        (0 until samples).foreach { j =>
          initializationInput.putScalar(Array(j, idx, i), 1.0f)
        }
    }

    val buffers: List[StringBuffer] =
      List.fill(samples)(new StringBuffer(initialization))

    val distribution = new NumericDistribution

    //Sample from network (and feed samples back into input) one character at a time (for all samples)
    //Sampling is done in parallel here
    model.rnnClearPreviousState()

    var output = model.rnnTimeStep(initializationInput)
    output = output.tensorAlongDimension(output.size(2) - 1, 1, 0) //Gets the last time step output

    (0 until chars).foreach { i =>
      val nextInput = Nd4j.zeros(samples, characterMap.size)

      (0 until samples).map { s =>
        val outputProbDistribution = (0 until characterMap.size).map { j =>
          output.getDouble(s, j)
        }.toArray

        val sampledCharacterIdx = distribution.sample(outputProbDistribution)

        nextInput.putScalar(Array(s, sampledCharacterIdx), 1.0f) //Prepare next time step input
        buffers(s).append(characterMap.charAt(sampledCharacterIdx)) //Add sampled character to StringBuilder (human readable output)
      }

      output = model.rnnTimeStep(nextInput)
    }

    buffers.map(_.toString)
  }
}
