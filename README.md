# Estream. Сервис тех.поддержки с электронной очередью

### Задача проекта 

___

Решить проблему организации пользовательских запросов на получение

той или иной информации, ответов на интересующие вопросы в порядке очереди

без их физического присутствия и звонков.

### Реализация

___

Это Spring Boot приложение с базовой аутентификацией операторов (см. документацию).

В проекте используется JSF. Информация о представлениях хранится в `@ManagedBean` 

Сборщик проекта - Maven.

### Doker 

___

В разработке

## Концепция

___

1. Клиенты

 - Операторы - клиенты, проходящие процесс аутентификации (`UsernamePasswordAuthenticationToken`)

Оператор имеет список специализаций, которые он может обслуживать.

 - Пользователи - клиенты, НЕ проходящие процесс аутентификации (`AnonymousAuthenticationToken`).
Это сделано с целью облегчить процесс получения информации для пользователя.

2. Специализации

Специализация - тема, на которую пользователь может получить информацию (пример: "Медицинское страхование", "Вопросы по ОСАГО" и т.п.)

При выборе пользователем специализации формируется уникальный код очереди.

Оператор в своей панели (`/panel`) может получить пользователя из очереди, если специализация из запроса находится в списке специализаций оператора

3. Очередь

Очередь - последовательность клиентских запросов (таблица `queue`). Единица очереди - один клиентский запрос (одна запись в БД).

Для каждой очереди формируется уникальный код очереди исходя из названия специализации. Пример (специализация: "Первая", коды: "`I12, I13, I14 ... In`", для специализации "Вторая": `K1, K2, K3 ... Kn` )

## Документация

___

1. Общее

Проект поделен на модули - `operator`, `user`, `common`. В них содержится реализация функционала оператора, пользователя соответственно. `common` - общий модуль (общие сущности).

2. Сессия

Существует класс для хранения объекта оператора в рамках сессии. Этот класс - `ClientSessionDataService`
хранит `Optional<Operator>`, который будет пуст если клиент - пользователь (гость) и объект оператора в противном случае. 

Значение он получает из `SecurityContext` при помощи проверки authentication token объекта.

В целях безопасности и соблюдения идеи хранения актуальных данных в рамках сессии, сессия уничтожается и автоматически подменяется на новую когда пользователь проходит процесс аутентификации.
Это реализовано в переопределенном провайдере `ApplicationAuthenticationProvider`, который после проверки кредов предоставит клиенту новую сессию, уничтожив старую.

`SessionEndedListener` содержит функционал, исполняемый при завершении сессии. Это логирование (оповещение о завершении сессии), а также завершение рабочей сессии (переключатель "активности" оператора становится false).

3. Работа с операторами

Операторы представлены интерфейсом `Operator`. Этот интерфейс реализован в `OperatorDetails` и `OperatorMapper`.

Таким образом, объект оператора полученный при помощи hibernate (mapper) и объект оператора из `Security Context` рассматриваются как единая по поведению сущность (сущность, реализующая один контракт).

Операции с операторами описаны в `OperatorService` классе. Общие поля (такие как список специализаций, флаг активности) при изменении своего значения
в `OperatorService` обновляются в БД и в `SecurityContext`. Это сделано с целью минимизации запросов к БД.

Например: при обновлении значения флага активности оператора значение будет изменено в БД, а также в `Security Context`. Представление, которому понадобится значение этого флага возьмет его из `Security Context` 
при помощи `ClientSessionDataService` уже свежим, вместо отправки запроса в БД, либо обновления данных в контексте путём аналогичного запроса.
