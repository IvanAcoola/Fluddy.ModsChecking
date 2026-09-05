# Fluddy.com - Проверка & Анализ модов

Данный инструмент использовался как один из утилит проверки модов в проекте fluddy.com.

![Java](https://img.shields.io/badge/Java-25-ED8B00?logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-Build%20Tool-C71A36?logo=apachemaven&logoColor=white)
![OW2](https://img.shields.io/badge/OW2-Open%20Source-0066CC)

### Рекомендация

Инструмент рекомендуется использовать вместе с [Fluddy.Hashes](https://github.com/IvanAcoola/Fluddy.Hashes) для первичной проверки и лишь далее отправлять мод на анализ.  
## Загрузка и использование

Для билда и последующего использование проекта необходимо использовать maven & Java 25 (рекомендуется).

```bash
  git clone https://github.com/IvanAcoola/Fluddy.ModsChecking.git
```
```bash
  cd Fluddy.ModsChecking
```
```bash
  mvn clean package
```
```bash
  java -jar target/<имя-файла>.jar
```
## Настрока

В проекте предусмотрена работа в двух возможных версия. 

#### 1 - DEBUG. Проверка файла по указанному пути, находящемся на локальном файлу.

```java
  private static boolean _DEBUG = true;
```

В основной функции укажите путь к файлу для проверки:

```java
  Path path = Paths.get("/path/");
```

Вывод будет следующий:

```
[~] Started analyze for -> files\custom-crosshair-mod-v1.3.1-fabric-mc1.16.5.jar
[~] Fields: 34
[~] Variables: 182
[~] Instructions: 88
[~] Output result -> {"illegalFields":false,"stealler":false,"hitbox":false,"overweight":false,"hash":"0c4b149df0c0ed0aaca2d2a43417025a","illegalClasses":false,"illegalStrings":false}
```
Тут можно найти логи исполнения и демострационный json ответ на основе анализа файла. 

#### 2 - Release. Проверка файла, получаемого по сокету в байтах. 

```java
  private static boolean _DEBUG = false;
```

```java
  InetAddress localHost = InetAddress.getLoopbackAddress();
  InetSocketAddress sockAddr = new InetSocketAddress(localHost, 1337);
  HttpServer server = HttpServer.create(sockAddr, 0);
  server.createContext("/checkout", new RequestHandler());
  server.setExecutor(null);
  server.start();
```

Тут необходимо настроить порт (1337 для примера) и адрес. Данный режим создан для работы через сторонних сайт, откуда и производится отправка мода для проверки.
## Функции

- Проверка модов на содержание хитбоксов
- Проверка на наличие стиллера (Не гарантированная работа)
- Обнаружение нелегальных классов (Обфускация)
- Обнаружение нелегальных строк (Обфускация)
- Обнаружение нелегальных полей (Обфускация)
## Поддержка

Для поддержки / любым вопросам обращаться по следующему контакту - [Telegram](https://t.me/cherepoveciv).


## Лицензия

[MIT](https://choosealicense.com/licenses/mit/)


## Автор

- [IvanAcoola](https://github.com/IvanAcoola)

