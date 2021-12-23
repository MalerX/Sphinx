# Sphinx

ПО для проведения интеграционного тестирования разрабатываемых микросервисов.

## Принципиальная схема работы

1. Выполнить чтение набора тестовых данных (*цепочки*) из БД MongoDb.
2. Если в поле **direction** указано *in* -- отправить сообщение формата json, лежащее в поле под именем **data**, в
   топик брокера Kafka, имя топика указано в поле **topic_name**.
3. Если в поле **direction** указано *out* -- получить ответ в формате json из топика брокера Kafka, имя топика указано
   в поле **topic_name**.
4. Выполнить сравнение полученного и ожидаемого (в поле **data**) сообщения.
5. Записать результат сравнения в текстовый файл. Формат строки следующий:

|        Date       |          chain_id         |  result | detail |
|:-----------------:|:-------------------------:|:-------:|:------:|
| dd.MM.yyyy-kk:mm:S|  61b7195596ae3c4390119609 |    OK   |        |
| dd.MM.yyyy-kk:mm:S|  61b7195596ae3c4390119609 |   FAIL  | error: code: NON EQUALS expected '42' != received '400'. |
| dd.MM.yyyy-kk:mm:S|  61b7195596ae3c4390119609 |   FAIL  | The expected message format does not match the received one: |

### Для использования приложения, необходимо выполнить настройку под конкретный тестируемый микросервис.

1. Настроить подключение к брокеру сообщений Kafka в файле *application.yaml*;
2. Указать в конфигурационном файле *application.yaml* топики, на которые должен подписаться консюмер, из которых
   ожидаются ответы;

**P.S.: на данный момент (23.12.2021) работоспособность приложения в боевых условиях не проверена, в связи с отсутствием
работоспособного микросервиса. Корректоность работы основных модулей проверена на тестовых данных и эхосервисе.
Критический участок кода, функция глубокого сравнения сложного JSON (бизнес-логика), покрыта Unit-тестами.**
