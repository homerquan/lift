import Dependencies._

Build.Settings.project

name := "lift-notification-protocol"

libraryDependencies ++= Seq(
  akka.actor,
  akka.cluster,
  akka.contrib,
  akka.testkit % "test",
  spray.testkit % "test"
)
