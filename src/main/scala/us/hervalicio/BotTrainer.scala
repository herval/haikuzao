package us.hervalicio

import us.hervalicio.ai.lstm.Network

/**
  * Created by herval on 4/19/16.
  */
object BotTrainer extends App {

  val network = new Network with Config

  network.runTraining()
}
