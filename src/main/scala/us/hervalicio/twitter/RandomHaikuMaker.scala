package us.hervalicio.twitter

import us.hervalicio.ai.lstm.Network
import us.hervalicio.haiku.Writer

import scala.concurrent.duration._

class RandomHaikuMaker(api: Api, network: Network) extends Runnable {

  val writer = new Writer(network)

  override def run() = {
    while (true) {
      println(
        writer.sample.map(q => api.post(q))
      )
      println("Entering deep slumber for some time...")
      Thread.sleep(60.minutes.toMillis)
    }
  }

}
