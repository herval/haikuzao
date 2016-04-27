package us.hervalicio.ai.text

import java.util.Random

// Store valid characters is a map for later use in vectorization
class CharacterMap(charSet: Seq[Char]) {

  private val rng = new Random()
  private val charToIdxMap = charSet.zipWithIndex.toMap

  def charAt(idx: Int) = charSet(idx)

  def indexOf(char: Char) = charToIdxMap(char)

  def size = charSet.size

  def contains(char: Char) = charToIdxMap.contains(char)

  def sampleChar() = charSet((rng.nextDouble() * charSet.length).toInt)

}

object CharacterMap {

  // A minimal character set, with a-z, A-Z, 0-9 and common punctuation etc
  private val minimalCharSet = {
    ('a' to 'z') ++ ('A' to 'Z') ++ ('0' to '9') ++
        Seq('!', '&', '(', ')', '?', '-', '\'', '"',
          ',', '.', ':', ';', ' ', '\n', '\t')
  }

  // As per getMinimalCharacterSet(), but with a few extra characters
  private val defaultCharacterMap = {
    minimalCharSet ++ Seq('@', '#', '$', '%', '^',
      '*', '{', '}', '[', ']', '/', '+', '_',
      '\\', '|', '<', '>')
  }

  val minimal = new CharacterMap(minimalCharSet)
}