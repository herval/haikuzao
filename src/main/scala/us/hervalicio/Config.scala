package us.hervalicio

import java.io.File
import java.nio.file.Paths

import org.deeplearning4j.nn.api.OptimizationAlgorithm
import org.deeplearning4j.nn.conf.distribution.UniformDistribution
import org.deeplearning4j.nn.conf.{Updater, NeuralNetConfiguration}
import org.deeplearning4j.nn.conf.layers.{RnnOutputLayer, GravesLSTM}
import org.deeplearning4j.nn.weights.WeightInit
import org.nd4j.linalg.lossfunctions.LossFunctions
import us.hervalicio.ai.lstm.{NetworkConfig, TrainingConfig}
import us.hervalicio.ai.text.CharacterMap
import us.hervalicio.twitter.ApiConfig

/**
  * Created by herval on 10/31/15.
  */
trait Config extends ApiConfig with NetworkConfig with TrainingConfig {
  override lazy val storagePath = Paths.get("networks/200_neurons")
  override lazy val coefficientsFile = new File(storagePath.toFile, "coefficients_network.bin")
  override lazy val topologyFile = new File(storagePath.toFile, "conf_network.json")
  override lazy val layerSize = 200

  override lazy val consumerKey = System.getenv("TWITTER_CONSUMER_KEY")
  override lazy val consumerSecret = System.getenv("TWITTER_CONSUMER_SECRET")
  override lazy val accessToken = System.getenv("TWITTER_ACCESS_TOKEN")
  override lazy val accessTokenSecret = System.getenv("TWITTER_ACCESS_TOKEN_SECRET")

  override lazy val characterMap = CharacterMap.minimal

  override lazy val iterations = 100
  override lazy val batchSize = 10
  override lazy val exampleLength = 100
  override lazy val examplesPerIteration = 500
  override lazy val trainingFiles = List(new File("inputs/haiku.txt"))

  override lazy val topology = {
    new NeuralNetConfiguration.Builder()
        .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT).iterations(1)
        .learningRate(0.1)
        .rmsDecay(0.95)
        .seed(12345)
        .regularization(true)
        .l2(0.001)
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
  }
}
