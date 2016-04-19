package us.hervalicio.ai.lstm

import java.io.File

/**
  * Created by herval on 4/19/16.
  */
trait TrainingConfig {
  val iterations: Int
  val batchSize: Int
  val exampleLength: Int
  val examplesPerIteration: Int
  val trainingFiles: List[File]
}
