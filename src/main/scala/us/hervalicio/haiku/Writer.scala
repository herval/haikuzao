package us.hervalicio.haiku

import us.hervalicio.ai.lstm.Network

/**
  * Created by herval on 4/19/16.
  */
class Writer(network: Network, dictionary: Set[String]) {

  // sample a Haiku from the network
  def sample(): Option[String] = {
    val lines = network.sample(800, 1)
    lines.headOption.flatMap { raw =>
      splitInHaikus(raw)
          .filter(l => l.count(_ == '\n') == 3) // only 3-lines...
          .map { words => (countWeirdWords(words), words) }
          .sortBy(_._1) // sorted by as few weird words as possible
          .headOption.map(_._2)
    }
  }

  private def splitInHaikus(raw: String): List[String] = {
    raw.split("\n").foldLeft(List[String]()) { case (lists, line) =>
      if (line.isEmpty) {
        lists ++ List("") // a new haiku appears!
      } else {
        lists.dropRight(1) ++ List(lists.lastOption.getOrElse("") + line + "\n") // append line to the last haiku
      }
    }
  }

  private def countWeirdWords(words: String): Int = words.split(" ").count { w => !dictionary.contains(w) }

}
