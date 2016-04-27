name := "haikuzao"

version := "1.0"

scalaVersion := "2.11.5"

libraryDependencies ++= Seq(
  "org.twitter4j" % "twitter4j-core" % "4.0.2",
  "org.deeplearning4j" % "deeplearning4j-core" % "0.4-rc3.4",
  "org.nd4j" % "nd4j-jblas" % "0.4-rc3.4",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test",
  "org.scalamock" %% "scalamock-scalatest-support" % "3.2.2" % "test"
)

mainClass in Compile := Some("us.hervalicio.TwitterBot")

packageArchetype.java_application