package us.hervalicio.haiku

import us.hervalicio.ai.lstm.Network

/**
  * Created by herval on 4/19/16.
  */
class Writer(network: Network) {

  // sample a Haiku from the network
  def sample(): Option[String] = {
    val lines = network.sample(140, 1)
    lines.headOption.flatMap { raw =>
      val haikus: List[String] = raw.split("\n").foldLeft(List[String]()) { case (lists, line) =>
        line match {
          case _ if line.isEmpty => lists ++ List("")
          case _ => {
            val last: String = lists.lastOption.getOrElse("") + line + "\n"
            lists.dropRight(1) ++ List(last)
          }
        }
      }

      haikus.dropWhile(l => l.count(_ == '\n') != 3).headOption
    }
  }

}
