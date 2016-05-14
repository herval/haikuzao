package us.hervalicio.twitter

import us.hervalicio.ai.lstm.Network
import us.hervalicio.ai.text.Loader
import us.hervalicio.haiku.Writer

import scala.concurrent.duration._

class RandomHaikuMaker(api: Api, network: Network, trainingData: Loader) extends Runnable {

  val writer = new Writer(network, trainingData.words)

  override def run() = {
    while (true) {
      writer.sample.foreach { haiku =>
        println(
          api.post(haiku + "\n#haiku #micropoetry")
        )
        println("Entering deep slumber for some time...")
        Thread.sleep(2.hours.toMillis)
      }
    }
  }

}
