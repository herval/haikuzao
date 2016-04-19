package us.hervalicio.haiku

import us.hervalicio.ai.lstm.Network

/**
  * Created by herval on 4/19/16.
  */
class Writer(network: Network) {

  // sample a Haiku from the network
  def sample(): Option[String] = {
    val lines = network.sample(140, 1)
    lines.headOption
  }

}
