name := "Simple Movie Recommendation"

version := "1.0"

scalaVersion := "2.11.9"


libraryDependencies ++= Seq(
  "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
  "ch.qos.logback"                %  "logback-classic"          % "1.1.7"
)

assemblyJarName in assembly := "recommender.jar"
