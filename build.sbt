name := "ini_tracker"

version := "1.0"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

resolvers += "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/"

scalaVersion := "2.13.5"

libraryDependencies ++= Seq(
  jdbc,
  ehcache,
  ws,
  specs2 % Test,
  guice,
  //javaJdbc,
  javaJpa,
  //"com.h2database" % "h2" % "2.0.206",
  "org.hibernate" % "hibernate-entitymanager" % "5.3.7.Final",
  "mysql" % "mysql-connector-java" % "8.0.27",
  //javaWs % "test",
  "org.awaitility" % "awaitility" % "4.1.1" % "test",
  "org.assertj" % "assertj-core" % "3.14.0" % "test",
  "org.mockito" % "mockito-core" % "3.1.0" % "test",
  "org.apache.commons" % "commons-math3" % "3.6.1",
  "com.google.inject" % "guice" % "5.0.1",
  "com.typesafe.play" %% "play-json" % "2.9.2",
  "com.google.code.gson" % "gson" % "2.8.9"
)