## Использование

```cmd
> java -jar gen-files-by-template.jar --help
gen-files-by-template 0.1
Usage: gen-files-by-template [options] <TEMPLATE-FILE> <DATA-FILE> <OUTPUT-FILENAME-TMPL>

  --data-format <value>   формат файла с данными, один из: csv, csv-excel, csv-rfc4180; по умолчанию csv
  --charset <value>       кодировка файлов с данными и шаблоном, а также сгенерированных файлов, по умолчанию 'UTF-8'
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
```
