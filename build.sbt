enablePlugins(JavaAppPackaging)

name := "testinator rewritten by me"
version := "1.0"
scalaVersion := "2.11.8"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= {
  val akkaV       = "2.4.10"
  val scalaTestV  = "2.2.6"
  val specsMockV  = "2.4.6"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "com.typesafe.akka" %% "akka-stream" % akkaV,
    "com.typesafe.akka" %% "akka-http-experimental" % akkaV,
    "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaV,
    "com.typesafe.akka" %% "akka-http-testkit" % akkaV,
    "com.typesafe.akka" %% "akka-http-xml-experimental" % akkaV,
    "org.scalatest"     %% "scalatest" % scalaTestV % "test",
    "org.specs2"        %% "specs2-mock" % specsMockV % "test"
  )
}

Revolver.settings
