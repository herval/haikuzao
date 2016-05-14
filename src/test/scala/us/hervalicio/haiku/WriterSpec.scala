package us.hervalicio.haiku

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSuite, Matchers}
import us.hervalicio.ai.lstm.Network

/**
  * Created by herval on 4/27/16.
  */
class WriterSpec extends FunSuite with Matchers with MockFactory {

  val fakeNetwork = stub[Network]
  val writer = new Writer(fakeNetwork, Set("three", "line", "stuff"))

  test("Generate haikus") {
    val strings = List(
      """
        |foo
        |
        |this
        |will be ignored
        |because unknown words
        |
        |three
        |line
        |stuff
        |
        |ok?
      """.stripMargin
    )
    (fakeNetwork.sample _).when(800, 4).returns(strings)

    assert(
      writer.sample().get == "three\nline\nstuff\n"
    )
  }

}
