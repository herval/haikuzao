package us.hervalicio.ai.text

import java.io.File

import org.apache.commons.io.FileUtils

import scala.collection.JavaConversions._

class Loader(files: List[File], charMap: CharacterMap) {

  val lines: List[String] = files.flatMap { f => FileUtils.readLines(f) }

  val words: Set[String] = lines.flatMap(_.split(" ")).toSet

  def iterator(batchSize: Int, exampleLength: Int, examplesPerEpoch: Int): CharacterIterator = {
    new CharacterIterator(
      lines,
      batchSize,
      exampleLength,
      examplesPerEpoch,
      charMap,
      true
    )
  }
}