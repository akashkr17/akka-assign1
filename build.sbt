import Dependencies.rootDependencies
name := "akka-assign1"
version := "0.1"

scalaVersion := "2.13.7"

libraryDependencies ++= rootDependencies
// https://mvnrepository.com/artifact/org.slf4j/slf4j-log4j12
libraryDependencies += "org.slf4j" % "slf4j-simple" % "1.7.25"
