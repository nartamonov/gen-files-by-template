import java.io.FileReader
import java.nio.charset.Charset
import java.nio.file._

import org.apache.commons.csv.CSVFormat

import scala.util.control.NonFatal

object Application extends App {
  case class Args(templateFile: Path = Paths.get("."),
                  dataFile: Path = Paths.get("."),
                  dataFormat: CSVFormat = CSVFormat.DEFAULT,
                  outputFileNameTemplateBody: String = "",
                  debug: Boolean = false,
                  charset: Charset = Charset.forName("UTF-8"),
                  append: Boolean = false)

  val appArgs = CommandLineParser.parse(args, Args()) match {
    case Some(args) =>
      if (args.debug)
        pprint.log(args)
      args
    case None =>
      sys.exit(-1)
  }

  generateFiles(
    SimpleTemplateEngine,
    appArgs.templateFile,
    appArgs.dataFile,
    appArgs.outputFileNameTemplateBody,
    appArgs.charset,
    appArgs.dataFormat)

  def generateFiles(templateEngine: TemplateEngine,
                    templateFile: Path,
                    dataFile: Path,
                    outputFileNameTemplateBody: String,
                    charset: Charset,
                    dataFormat: CSVFormat): Unit = {
    val templateBody =
      try { new String(Files.readAllBytes(templateFile), charset) }
      catch exitAbnormally(e => s"Не получилось прочитать файл шаблона $templateFile: ${e.description}", -2)

    val dataRecords =
      try { readDataFile(dataFile, dataFormat) }
      catch exitAbnormally(e => s"Не получилось прочитать или распарсить файл с данными $dataFile: ${e.description}", -3)

    val template = templateEngine.createTemplateFromString(templateBody)
    val outputFileNameTemplate = templateEngine.createTemplateFromString(outputFileNameTemplateBody)
    val openOpts: Array[OpenOption] =
      if (appArgs.append)
        Array(StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND)
      else
        Array.empty

    for (dataRecord <- dataRecords) {
      val outputFileName = outputFileNameTemplate.render(dataRecord)
      val outputFileContent = template.render(dataRecord)

      try {
        Files.write(Paths.get(outputFileName), outputFileContent.getBytes(charset), openOpts : _*)
        Console.out.println(
          if (appArgs.append)
            s"Осуществлена запись в файл $outputFileName"
          else
            s"Сгенерирован файл $outputFileName")
      } catch {
        case NonFatal(e) =>
          Console.err.println(s"Не получилось записать в файл $outputFileName: " + e.description)
      }
    }
  }

  type DataRecord = Map[String,String]

  def readDataFile(path: Path, format: CSVFormat): Vector[DataRecord] = {
    import scala.collection.JavaConverters._
    val parser = format.withFirstRecordAsHeader().parse(new FileReader(path.toFile))
    val headers = parser.getHeaderMap.keySet().asScala
    val records = for (record <- parser.asScala) yield headers.map(h => h -> record.get(h)).toMap
    records.toVector
  }

  def exitAbnormally(messageBuilder: Throwable => String, exitCode: Int): PartialFunction[Throwable, Nothing] = {
    case NonFatal(e) =>
      Console.err.println(messageBuilder(e))
      sys.exit(exitCode)
  }

  implicit class RichThrowable(t: Throwable) {
    def description: String = Option(t.getMessage).getOrElse(t.getClass.getCanonicalName)
  }
}
