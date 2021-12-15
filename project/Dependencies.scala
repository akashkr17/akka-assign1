import sbt._
object Dependencies {

  object  Version {
    val AkkaVersion = "2.6.15"
    val ScalaTestVersion = "3.2.9"
    val logback                = "1.2.3"
    val slf4j                  = "1.7.25"
    val logging                = "3.9.0"
  }

  object Libraries {

//    val embeddedKafka = "net.manub" %% "scalatest-embedded-kafka" % "0.14.0" % "test"
    val akka = "com.typesafe.akka" %% "akka-actor-typed" % Version.AkkaVersion
//    val akkaStream = "com.typesafe.akka" %% "akka-stream" % Version.AkkaVersion
//    val akkaPersist =  "com.typesafe.akka" %% "akka-persistence-typed" % Version.AkkaVersion
//    val scalaTest  = "org.scalatest" %% "scalatest" % Version.ScalaTestVersion % Test

  }


  val rootDependencies: Seq[sbt.ModuleID] =
    Seq(Libraries.akka)
}