package us.hervalicio.haiku

import us.hervalicio.ai.lstm.Network

/**
  * Created by herval on 4/19/16.
  */
class Writer(network: Network, dictionary: Set[String]) {

  private val sequenceSize = 800
  private val maxTries = 4

  // sample a Haiku from the network
  def sample(): Option[String] = {
    network
      .sample(sequenceSize, maxTries)
      .flatMap { raw =>
        splitInHaikus(raw)
          .filter(l => l.count(_ == '\n') == 3) // only 3-lines...
          .map { words =>
            (countWeirdWords(words), words)
          }
          .sortBy(_._1) // as few weird words as possible
          .map(_._2)
      }
      .headOption
  }

  private def splitInHaikus(raw: String): List[String] = {
    raw.split("\n").foldLeft(List[String]()) {
      case (lists, line) =>
        if (line.isEmpty) {
          lists ++ List("") // a new haiku appears!
        } else {
          lists.dropRight(1) ++ List(lists.lastOption.getOrElse("") + line +
              "\n") // append line to the last haiku
        }
    }
  }

  private def countWeirdWords(words: String): Int = {
    words.replace("\n", " ").split(" ").filterNot(_.isEmpty).count { w =>
      !dictionary.contains(w)
    }
  }
}
