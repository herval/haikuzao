package us.hervalicio.ai.lstm

import java.io.File
import java.nio.file.Path

import org.deeplearning4j.nn.api.OptimizationAlgorithm
import org.deeplearning4j.nn.conf.distribution.UniformDistribution
import org.deeplearning4j.nn.conf.{Updater, NeuralNetConfiguration}
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

  lazy val defaultTopology = {
    val topology = new NeuralNetConfiguration.Builder()
        .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT).iterations(1)
        .learningRate(0.01)
        .rmsDecay(0.97)
        .seed(12345)
        .regularization(true)
        .l1(0.001)
        .list(3)
        .layer(0, new GravesLSTM.Builder().nIn(characterMap.size).nOut(layerSize)
            .updater(Updater.RMSPROP)
            .activation("tanh").weightInit(WeightInit.DISTRIBUTION)
            .dist(new UniformDistribution(-0.08, 0.08)).build())
        .layer(1, new GravesLSTM.Builder().nIn(layerSize).nOut(layerSize)
            .updater(Updater.RMSPROP)
            .activation("tanh").weightInit(WeightInit.DISTRIBUTION)
            .dist(new UniformDistribution(-0.08, 0.08)).build())
        .layer(2, new RnnOutputLayer.Builder(LossFunctions.LossFunction.MCXENT).activation("softmax") //MCXENT + softmax for classification
            .updater(Updater.RMSPROP)
            .nIn(layerSize).nOut(characterMap.size).weightInit(WeightInit.DISTRIBUTION)
            .dist(new UniformDistribution(-0.08, 0.08)).build())
        .pretrain(false).backprop(true)
        .build()

    val model = new MultiLayerNetwork(topology)
    model.init()
    model
  }

}
