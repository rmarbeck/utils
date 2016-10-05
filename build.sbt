name := """utils"""

version := "1.03-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.11.6"

resolvers ++= Seq(
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
)

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs
)

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator

//*******************************
// Maven settings
//*******************************

publishMavenStyle := true

organization := "fr.watchnext"

description := "This is a collection of helpers and tags."

homepage := Some(url("http://watchnext.fr"))

licenses := Seq("Apache License" -> url("https://github.com/rmarbeck/utils/LICENSE"))

startYear := Some(2016)

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

pomExtra := (
  <scm>
    <url>git@github.com:rmarbeck/utils.git</url>
    <connection>scm:git:git@github.com:rmarbeck/utils.git</connection>
  </scm>
  <developers>
    <developer>
      <id>rmarbeck</id>
      <name>Raphaël Marbeck</name>
      <url>https://github.com/rmarbeck</url>
    </developer>
  </developers>
)

credentials += Credentials(Path.userHome / ".sbt" / "sonatype.credentials")