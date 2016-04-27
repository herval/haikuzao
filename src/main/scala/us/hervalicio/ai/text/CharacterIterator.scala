package us.hervalicio.ai.text

import java.util.Random

import org.deeplearning4j.datasets.iterator.DataSetIterator
import org.nd4j.linalg.dataset.DataSet
import org.nd4j.linalg.dataset.api.DataSetPreProcessor
import org.nd4j.linalg.factory.Nd4j

class CharacterIterator(lines: List[String],
                        batchSize: Int,
                        exampleLength: Int,
                        override val numExamples: Int,
                        charMap: CharacterMap,
                        alwaysStartAtNewLine: Boolean) extends DataSetIterator {

  if (numExamples % batchSize != 0) {
    throw new IllegalArgumentException("numExamples must be a multiple of batchSize")
  }
  if (batchSize <= 0) {
    throw new IllegalArgumentException("Invalid batchSize (must be >0)")
  }

  private val rng = new Random()

  private val allCharacters = {
    //Load lines and convert contents to a single char[]
    val newLineValid = charMap.contains('\n')
    val separator = if (newLineValid) {
      "\n"
    } else {
      " "
    }
    lines.mkString(separator).filter { c => charMap.contains(c) }
  }

  private var examplesSoFar = 0
  private val maxStartIdx = allCharacters.length - exampleLength

  def next(num: Int): DataSet = {
    if (examplesSoFar + num > numExamples) {
      null // forgive me, Scala gods.
    } else {
      val input = Nd4j.zeros(num, charMap.size, exampleLength)
      val labels = Nd4j.zeros(num, charMap.size, exampleLength)

      //Randomly select a subset of the file. No attempt is made to avoid overlapping subsets
      // of the file in the same minibatch
      (0 until num).foreach { i =>
        var startIdx = (rng.nextDouble() * maxStartIdx).toInt
        startIdx = if (alwaysStartAtNewLine && startIdx > 1) {
          val previousNewLine = allCharacters.substring(0, startIdx-1).lastIndexOf('\n')
          if(previousNewLine > -1) {
            previousNewLine
          } else {
            startIdx
          }
        } else {
          startIdx
        }
        val endIdx = startIdx + exampleLength

//        println(allCharacters.substring(startIdx, endIdx))

        (startIdx until endIdx).zipWithIndex.foreach { case(j, c) =>
          val currentChar = charMap.indexOf(allCharacters(j))
          val nextChar = charMap.indexOf(allCharacters(j + 1))
          input.putScalar(Array(i, currentChar, c), 1.0)
          labels.putScalar(Array(i, nextChar, c), 1.0)
        }
      }
      examplesSoFar += 1

      new DataSet(input, labels)
    }
  }

  override def hasNext = examplesSoFar + batchSize <= numExamples

  override def next() = next(batchSize)

  override def batch() = batchSize

  override def cursor() = examplesSoFar

  override def totalExamples() = numExamples

  override def inputColumns = charMap.size

  override def totalOutcomes = charMap.size

  override def setPreProcessor(preProcessor: DataSetPreProcessor) = {}

  override def reset() = examplesSoFar = 0

}