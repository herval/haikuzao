package us.hervalicio

import us.hervalicio.ai.lstm.Network
import us.hervalicio.twitter.{Api, RandomHaikuMaker}

object TwitterBot extends App {

  val twitterApi = new Api with Config
  val poet = new Network with Config

  println("Starting bot...")

  new Thread(
    new RandomHaikuMaker(twitterApi, poet, poet.trainingDataLoader)
  ).start()

}