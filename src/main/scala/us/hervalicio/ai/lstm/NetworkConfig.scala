package us.hervalicio.ai.lstm

import java.io.File
import java.nio.file.Path

import org.deeplearning4j.nn.api.OptimizationAlgorithm
import org.deeplearning4j.nn.conf.distribution.UniformDistribution
import org.deeplearning4j.nn.conf.{MultiLayerConfiguration, Updater, NeuralNetConfiguration}
import org.deeplearning4j.nn.conf.layers.{RnnOutputLayer, GravesLSTM}
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.deeplearning4j.nn.weights.WeightInit
import org.nd4j.linalg.lossfunctions.LossFunctions
import us.hervalicio.ai.text.CharacterMap

/**
  * Created by herval on 4/19/16.
  */
trait NetworkConfig {
  val layerSize: Int //Number of units in each GravesLSTM layer

  val storagePath: Path
  val coefficientsFile: File
  val topologyFile: File
  val characterMap: CharacterMap
  val topology: MultiLayerConfiguration

  lazy val defaultTopology = {
    val model = new MultiLayerNetwork(topology)
    model.init()
    model
  }
}
