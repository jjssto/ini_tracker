name := "ini_tracker"

version := "1.7"

maintainer := "jjssto@posteo.de"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

resolvers += "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/"

scalaVersion := "2.13.5"

libraryDependencies ++= Seq(
  "junit" % "junit" % "4.13.2",
  jdbc,
  ehcache,
  ws,
  specs2 % Test,
  guice,
  javaJpa,
  "org.hibernate" % "hibernate-entitymanager" % "5.3.7.Final",
  "mysql" % "mysql-connector-java" % "8.0.27",
  "org.awaitility" % "awaitility" % "4.1.1" % "test",
  "org.assertj" % "assertj-core" % "3.14.0" % "test",
  "org.mockito" % "mockito-core" % "3.1.0" % "test",
  "org.apache.commons" % "commons-math3" % "3.6.1",
  "com.google.inject" % "guice" % "5.0.1",
  "com.typesafe.play" %% "play-json" % "2.9.2",
  "com.google.code.gson" % "gson" % "2.8.9",
  "be.objectify" %% "deadbolt-java" % "2.8.1",
  evolutions
)

PlayKeys.externalizeResourcesExcludes += baseDirectory.value / "conf" / "META-INF" / "persistence.xml"
