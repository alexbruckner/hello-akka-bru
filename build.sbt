name := """hello-akka"""

version := "1.0"

scalaVersion := "2.10.2"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.2.1",
  "com.typesafe.akka" %% "akka-testkit" % "2.2.1",
  "org.scalatest" %% "scalatest" % "1.9.1" % "test",
  "junit" % "junit" % "4.11" % "test",
  "com.novocode" % "junit-interface" % "0.10" % "test",
  "org.specs2" %% "specs2" % "2.3.6" % "test",
  "org.eintr.loglady" %% "loglady" % "1.1.0",
  "ch.qos.logback" % "logback-classic" % "1.0.3"
)

testOptions += Tests.Argument(TestFrameworks.JUnit, "-v")