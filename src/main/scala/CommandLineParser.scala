import java.nio.charset.Charset
import java.nio.file.{Files, Path, Paths}

import Application.Args
import org.apache.commons.csv.CSVFormat
import scopt.Read

object CommandLineParser extends scopt.OptionParser[Args]("gen-files-by-template") with ArgReaders {
  head("gen-files-by-template", "0.1")

  opt[CSVFormat]("data-format").optional().text("формат файла с данными, один из: csv, csv-excel, csv-rfc4180; по умолчанию csv")
    .action((format, args) => args.copy(dataFormat = format))

  opt[Charset]("charset").optional().text("кодировка файлов с данными и шаблоном, а также сгенерированных файлов, по умолчанию 'UTF-8'")
    .action((charset,args) => args.copy(charset = charset))

  opt[Unit]("debug").text("включить отладочный вывод")
    .action((_, args) => args.copy(debug = true))

  help("help").text("показать данный текст")

  arg[Path]("<TEMPLATE-FILE>").required().text("файл шаблона, на основе которого будут сгенерированы выходные файлы")
    .action((p, args) => args.copy(templateFile = p))
    .validate(fileExistsAndReadable)

  arg[Path]("<DATA-FILE>").required().text("файл с данными, подставляемыми в шаблон")
    .action((p, args) => args.copy(dataFile = p))
    .validate(fileExistsAndReadable)

  arg[String]("<OUTPUT-FILENAME-TMPL>").required().text("шаблон имени выходных файлов")
    .action((s, args) => args.copy(outputFileNameTemplateBody = s))

  note("")
  note("Генерирует текстовые файлы по заданному шаблону с подстановкой произвольных данных.")
  note("К примеру, имея следующие файлы:")
  note("")
  note("template.json:")
  note("  { \"name\": \"{{name}}\", \"age\": {{age}} }")
  note("data.csv:")
  note("  name,age")
  note("  Peter,21")
  note("  Alice,25")
  note("")
  note("В результате выполнения следующей команды:")
  note("")
  note("gen-files-by-template template.json data.csv {{name}}.json")
  note("")
  note("Будут сгенерированы следующие файлы:")
  note("")
  note("Peter.json:")
  note("  { \"name\": \"Peter\", \"age\": 21 }")
  note("Alice.json")
  note("  { \"name\": \"Alice\", \"age\": 25 }")

  private def fileExistsAndReadable(file: Path): Either[String,Unit] =
    if (!Files.exists(file) || !Files.isReadable(file))
      Left(s"Нет доступа к файлу $file")
    else
      Right({})

}

trait ArgReaders {
  protected implicit val pathRead: Read[Path] = Read.reads { Paths.get(_) }
  protected implicit val charsetRead: Read[Charset] = Read.reads(Charset.forName)
  protected implicit val dataFormatRead: Read[CSVFormat] = Read.reads {
    _.toLowerCase match {
      case "csv" => CSVFormat.DEFAULT
      case "csv-excel" => CSVFormat.EXCEL
      case "csv-rfc4180" => CSVFormat.RFC4180
      case f => throw new Exception(s"Неподдерживаемый формат: $f")
    }
  }
}
