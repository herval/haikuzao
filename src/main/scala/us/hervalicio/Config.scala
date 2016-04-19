package us.hervalicio

import java.io.File
import java.nio.file.Paths

import us.hervalicio.ai.lstm.{NetworkConfig, TrainingConfig}
import us.hervalicio.ai.text.CharacterMap
import us.hervalicio.twitter.ApiConfig

/**
  * Created by herval on 10/31/15.
  */
trait Config extends ApiConfig with NetworkConfig with TrainingConfig {
  override lazy val storagePath = Paths.get("networks/200_neurons")
  override lazy val coefficientsFile = new File(storagePath.toFile, "coefficients_network.bin")
  override lazy val networkConfigFile = new File(storagePath.toFile, "conf_network.json")
  override lazy val layerSize = 200

  override lazy val consumerKey = System.getenv("TWITTER_CONSUMER_KEY")
  override lazy val consumerSecret = System.getenv("TWITTER_CONSUMER_SECRET")
  override lazy val accessToken = System.getenv("TWITTER_ACCESS_TOKEN")
  override lazy val accessTokenSecret = System.getenv("TWITTER_ACCESS_TOKEN_SECRET")

  override lazy val characterMap = CharacterMap.minimal

  override lazy val iterations = 100
  override lazy val batchSize = 10
  override lazy val exampleLength = 200
  override lazy val examplesPerIteration = 1600
  override lazy val trainingFiles = List(new File("inputs/haiku.txt"))
}
