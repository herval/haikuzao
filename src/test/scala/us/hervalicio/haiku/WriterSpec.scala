package us.hervalicio.haiku

import org.scalamock.proxy.ProxyMockFactory
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSuite, Matchers}
import us.hervalicio.ai.lstm.Network

/**
  * Created by herval on 4/27/16.
  */
class WriterSpec extends FunSuite with Matchers with MockFactory {

  val fakeNetwork = stub[Network]
  val writer = new Writer(fakeNetwork)

  test("Generate haikus") {
    val strings = List(
      """
        |foo
        |
        |three
        |line
        |stuff
        |
        |ok?
      """.stripMargin
    )
    (fakeNetwork.sample _).when(140, 1).returns(strings)

    assert(
      writer.sample().get == "three\nline\nstuff\n"
    )
  }


}
