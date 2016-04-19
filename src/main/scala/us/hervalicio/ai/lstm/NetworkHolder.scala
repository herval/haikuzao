package us.hervalicio.ai.lstm

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork

/**
  * Created by herval on 4/19/16.
  */
trait NetworkHolder {
  val model: MultiLayerNetwork
}
