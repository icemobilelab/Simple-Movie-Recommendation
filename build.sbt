name := "Simple Movie Recommendation"

version := "1.0"

scalaVersion := "2.11.9"


libraryDependencies ++= Seq(
  "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
  "ch.qos.logback"                %  "logback-classic"          % "1.1.7",
  "joda-time" % "joda-time" % "2.9.9"
)

assemblyJarName in assembly := "recommender.jar"
