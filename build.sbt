lazy val genFilesByTemplate = (project in file("."))
  .enablePlugins(BuildInfoPlugin)
  .settings(commonSettings: _*)
  .settings(
    name := "gen-files-by-template",

    fork := true,

    mainClass in Compile := Some("Application"),

    assemblyJarName in assembly := "gen-files-by-template.jar",

    libraryDependencies ++= Seq(
      "org.apache.commons" % "commons-csv" % "1.5",
      "com.github.scopt" %% "scopt" % "3.7.0",
      "com.lihaoyi" %% "pprint" % "0.5.1",
      "org.scalactic" %% "scalactic" % "3.0.2",
      "org.scalatest" %% "scalatest" % "3.0.2" % "test"
    ),

    buildInfoKeys := Seq[BuildInfoKey](name, version)
  )

lazy val commonSettings = Seq(
  version               := "0.2-SNAPSHOT",
  startYear             := Some(2017),
  scalaVersion          := "2.12.4",
  scalacOptions         ++= Seq("-target:jvm-1.8", "-deprecation", "-unchecked", "-Xcheckinit", "-encoding", "utf8", "-feature"),
  scalacOptions         ++= Seq(
    "-language:implicitConversions",
    "-language:postfixOps",
    "-language:reflectiveCalls",
    "-language:higherKinds"
  ),

  // configure prompt to show current project
  shellPrompt           := { s => Project.extract(s).currentProject.id + " > " },

  initialCommands in console :=
    """
      |import java.nio.file._
      |import scala.concurrent._
      |import scala.concurrent.duration._
      |import ExecutionContext.Implicits.global
    """.stripMargin
)
