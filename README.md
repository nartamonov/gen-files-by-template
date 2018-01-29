## Использование

```cmd
> java -jar gen-files-by-template.jar --help
gen-files-by-template 0.2
Usage: gen-files-by-template [options] <TEMPLATE-FILE> <DATA-FILE> <OUTPUT-FILENAME-TMPL>

  --data-format <value>   формат файла с данными, один из: csv, csv-excel, csv-rfc4180; по умолчанию csv
  --charset <value>       кодировка файлов с данными и шаблоном, а также сгенерированных файлов, по умолчанию 'UTF-8'
  --append                дополнять текст сгенерированных файлов
  --debug                 включить отладочный вывод
  --help                  показать данный текст
  <TEMPLATE-FILE>         файл шаблона, на основе которого будут сгенерированы выходные файлы
  <DATA-FILE>             файл с данными, подставляемыми в шаблон
  <OUTPUT-FILENAME-TMPL>  шаблон имени выходных файлов

Генерирует текстовые файлы по заданному шаблону с подстановкой произвольных данных.
К примеру, имея следующие файлы:

template.json:
  { "name": "{{name}}", "age": {{age}} }
data.csv:
  name,age
  Peter,21
  Alice,25

В результате выполнения следующей команды:

gen-files-by-template template.json data.csv {{name}}.json

Будут сгенерированы следующие файлы:

Peter.json:
  { "name": "Peter", "age": 21 }
Alice.json
  { "name": "Alice", "age": 25 }

С помощью опции --append сгенерированный текст будет добавлен к содержимому (при его наличии)
сгенерированных файлов. Эту возможность можно использовать, к примеру, для того, чтобы объединить
весь сгенерированный текст в один файл, вместо нескольких:

gen-files-by-template --append template.json data.csv out.json

При этом все сгенерированные данные будут объединены в единственном файле out.json:

out.json:
  { "name": "Peter", "age": 21 }
  { "name": "Alice", "age": 25 }
```
