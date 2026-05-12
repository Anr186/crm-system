# CRM System 

REST-сервис для управления продавцами, транзакциями и базовой аналитикой продаж.

Проект выполнен на `Java 17`, `Spring Boot 3`, `JDBI`, `PostgreSQL`, `Gradle`, `Docker`.

## Реализованный функционал

- Управление продавцами (`Seller`):
  - создание продавца;
  - получение списка активных продавцов;
  - получение продавца по `id`;
  - обновление данных продавца;
  - выборка продавцов с продажами ниже порога.
- Управление транзакциями (`Transaction`):
  - создание транзакции;
  - получение транзакции по `id`;
  - получение списка всех транзакций;
  - получение транзакций конкретного продавца.
- Аналитика:
  - лучший продавец за период;
  - наиболее продуктивный временной промежуток продавца.
- Централизованная обработка ошибок через `@ControllerAdvice`.
- SQL-инициализация схемы БД при старте (`schema.sql`).
- Unit/Web/Integration тесты (Spring Test, JUnit 5, Mockito, H2).

## Технологии

- `Java 17`
- `Spring Boot 3.5.14`
- `Spring Web`
- `Spring JDBC`
- `JDBI 3.45.0` (`jdbi3-spring5`, `jdbi3-sqlobject`, `jdbi3-postgres`)
- `PostgreSQL 15`
- `Gradle 8` 
- `Docker`, `docker-compose`
- `JUnit 5`, `Mockito`, `Spring Boot Test`
- `H2` (для тестов)

## Зависимости проекта

Ключевые зависимости определены в `build.gradle`:

- `org.springframework.boot:spring-boot-starter-web`
- `org.springframework.boot:spring-boot-starter-jdbc`
- `org.jdbi:jdbi3-spring5:3.45.0`
- `org.jdbi:jdbi3-sqlobject:3.45.0`
- `org.jdbi:jdbi3-postgres:3.45.0`
- `org.postgresql:postgresql` (runtime)
- `com.h2database:h2` (runtime/test)
- `org.springframework.boot:spring-boot-starter-test`

## Запуск (Docker)

### Требования

- Установлен и запущен Docker.

### Запуск

Из корня проекта выполните:

```bash
docker-compose up --build
```

После запуска доступны:

- API: `http://localhost:8080`
- PostgreSQL: `localhost:5432`

Параметры БД (из `docker-compose.yml`):

- DB: `crm_db`
- User: `user`
- Password: `password`

### Остановка

```bash
docker-compose down
```

## Локальный запуск (без Docker)

### Требования

- `JDK 17`
- локальный `PostgreSQL` (или другой доступный инстанс)

### Подготовка БД

1. Создайте БД `crm_db`.
2. Проверьте параметры подключения в `src/main/resources/application.properties`:
   - `spring.datasource.url=jdbc:postgresql://localhost:5432/crm_db`
   - `spring.datasource.username=user`
   - `spring.datasource.password=password`
3. Схема создастся автоматически при старте приложения (`spring.sql.init.mode=always`).

### Запуск приложения

Windows (PowerShell):

```powershell
.\gradlew.bat bootRun
```

Linux/macOS:

```bash
./gradlew bootRun
```

## Сборка

Сборка JAR:

Windows:

```powershell
.\gradlew.bat clean bootJar
```

Linux/macOS:

```bash
./gradlew clean bootJar
```

Артефакт будет в `build/libs/`.

## Тесты

Локально:

Windows:

```powershell
.\gradlew.bat test
```

Linux/macOS:

```bash
./gradlew test
```

Через Docker-сервис `tester`:

```bash
docker-compose run --rm tester
```

## API: примеры использования

Базовый URL: `http://localhost:8080`

### 1) Создать продавца

- Method: `POST`
- URL: `http://localhost:8080/api/sellers`
- Body (JSON):

```json
{
  "name": "Михаил Бобиков",
  "contactInfo": "misha@example.com"
}
```

### 2) Получить всех активных продавцов

- Method: `GET`
- URL: `http://localhost:8080/api/sellers`

### 3) Получить продавца по ID

- Method: `GET`
- URL: `http://localhost:8080/api/sellers/1`

### 4) Обновить продавца

- Method: `PUT`
- URL: `http://localhost:8080/api/sellers/1`
- Body (JSON):

```json
{
  "name": "Михаил Новый",
  "contactInfo": "new_misha@example.com"
}
```

### 5) Удалить продавца

- Method: `DELETE`
- URL: `http://localhost:8080/api/sellers/1`

### 6) Получить продавцов с продажами ниже указанной суммы

- Method: `GET`
- URL: `http://localhost:8080/api/sellers/low-sales?threshold=1000`

### 7) Создать транзакцию

- Method: `POST`
- URL: `http://localhost:8080/api/transactions`
- Body (JSON):

```json
{
  "sellerId": 1,
  "amount": 5000,
  "paymentType": "CARD"
}
```

### 8) Получить все транзакции

- Method: `GET`
- URL: `http://localhost:8080/api/transactions`

### 9) Получить транзакцию по ID

- Method: `GET`
- URL: `http://localhost:8080/api/transactions/1`

### 10) Получить транзакции продавца

- Method: `GET`
- URL: `http://localhost:8080/api/transactions/seller/1`

### 11) Лучший продавец за период

- Method: `GET`
- URL:

```text
http://localhost:8080/api/transactions/best-seller?start=2026-01-01T00:00:00&end=2026-12-31T23:59:59
```

### 12) Самый продуктивный час продавца

- Method: `GET`
- URL: `http://localhost:8080/api/transactions/seller/1/productive-hour`

## Проверка через Postman

В проекте есть готовая коллекция:

- Файл: `CRM_Full_API_Test.json`
- Название коллекции: `CRM System Final Testing`

Как импортировать:

1. Откройте Postman.
2. Нажмите `Import`.
3. Выберите файл `CRM_Full_API_Test.json`.
4. Запустите приложение.
5. Выполняйте запросы по группам: `Sellers` -> `Transactions` -> `Analytics`.

## Документация по тестовым файлам

### `CrmSystemApplicationTests.java`

- `contextLoads()` — тест запуска Spring-контекста приложения.

### `SellerControllerTest.java`

- `getAllSellers_SHOULD_ReturnListAndOkStatus()` — проверяет `GET /api/sellers`: код `200`, JSON-ответ и список продавцов.
- `createSeller_SHOULD_ReturnCreatedStatus()` — проверяет `POST /api/sellers`: код `201` и вызов `sellerService.createSeller(...)`.
- `deleteSeller_SHOULD_ReturnNoContent()` — проверяет `DELETE /api/sellers/{id}`: код `204` и вызов `sellerService.deleteSeller(id)`.

### `TransactionControllerTest.java`

- `createTransaction_SHOULD_ReturnCreated()` — проверяет `POST /api/transactions`: код `201` и вызов `transactionService.createTransaction(...)`.
- `getBestSeller_SHOULD_ReturnSellerInfo()` — проверяет `GET /api/transactions/best-seller`: код `200` и корректные данные лучшего продавца.
- `getProductiveHour_SHOULD_ReturnInteger()` — проверяет `GET /api/transactions/seller/{id}/productive-hour`: код `200` и числовой час в ответе.

### `SellerServiceTest.java`

- `createSeller_EXPECT_AutoSetRegistrationDate()` — проверяет автозаполнение `registrationDate`, если дата не задана.
- `createSeller_EXPECT_InvokeRepositoryInsert()` — проверяет, что при создании продавца вызывается `sellerRepository.insert(...)`.
- `deleteSeller_EXPECT_InvokeRepositoryDelete()` — проверяет, что удаление продавца делегируется в `sellerRepository.delete(...)`.

### `TransactionServiceTest.java`

- `createTransaction_EXPECT_AutoSetTransactionDate()` — проверяет автозаполнение `transactionDate` при создании транзакции.
- `getBestSeller_EXPECT_ReturnSellerFromRepository()` — проверяет, что сервис возвращает лучшего продавца из репозитория.
- `getMostProductiveHour_EXPECT_ReturnCorrectHourValue()` — проверяет корректность значения продуктивного часа.
- `getSellersWithLowSales_EXPECT_ReturnFilteredList()` — проверяет фильтрацию продавцов с низкими продажами.
- `getBestSeller_EXPECT_PassCorrectDatesToRepository()` — проверяет передачу корректного диапазона дат в репозиторий.

### `RepositoryIntegrationTest.java`

- `insertSeller_EXPECT_GeneratedIdInH2()` — проверяет, что при вставке продавца в H2 генерируется ID.
- `findSellersWithSalesLessThan_EXPECT_CorrectFilteringInH2()` — проверяет SQL-фильтрацию продавцов по сумме продаж.
- `findBestSeller_EXPECT_CorrectChampionInH2()` — проверяет SQL-логику выбора лучшего продавца за период.
- `findMostProductiveHour_EXPECT_CorrectHourValueInH2()` — проверяет вычисление самого продуктивного часа продавца.
- `findAll_EXPECT_ReturnAllNonDeletedSellers()` — проверяет, что `findAll()` возвращает активных (не удаленных) продавцов.
- `deleteById_EXPECT_SellerIsMarkedAsDeleted()` — проверяет soft delete: продавец скрывается из активных и получает `is_deleted=true`.

## Обработка ошибок

Глобальный обработчик `GlobalExceptionHandler` возвращает единый JSON-ответ с полями:

- `status`
- `error`
- `message`
- `details` (для части ошибок)

Обрабатываются, в том числе:

- ошибки БД (`UnableToExecuteStatementException`);
- неверные типы query/path параметров (`MethodArgumentTypeMismatchException`);
- некорректный JSON (`HttpMessageNotReadableException`);
- не найденные сущности (`NullPointerException` -> `404`).

## Модель данных

### Seller

- `id`
- `name`
- `contactInfo`
- `registrationDate`
- `isDeleted`

### Transaction

- `id`
- `sellerId`
- `amount`
- `paymentType`
- `transactionDate`

## Структура проекта

- `src/main/java/.../controller` — REST-контроллеры
- `src/main/java/.../service` — бизнес-логика
- `src/main/java/.../repository` — SQL-репозитории на JDBI
- `src/main/java/.../entity` — модели
- `src/main/java/.../exception` — обработка ошибок
- `src/main/resources/schema.sql` — SQL-схема
- `src/test/java` — тесты

## Примечания

- Удаление продавца реализовано как soft delete (`is_deleted=true`).
- В Docker-образе приложения тесты при сборке пропускаются (`bootJar -x test`).
