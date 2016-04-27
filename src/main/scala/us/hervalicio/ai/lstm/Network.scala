package us.hervalicio.ai.lstm

import org.deeplearning4j.optimize.listeners.ScoreIterationListener

trait Network extends NetworkConfig with Loading with Training with Sampling {

  override lazy val model = {
    val model = loadOrSeed()
    model.setListeners(new ScoreIterationListener(1))

    model.getLayers.zipWithIndex.foreach { case (l, i) =>
      val nParams = l.numParams()
      println("Number of parameters in layer " + i + ": " + nParams)
    }

    model
  }

}