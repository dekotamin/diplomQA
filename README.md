## Дипломная работа по курсу тестирования
Дипломный проект представляет собой автоматизацию тестирования комплексного сервиса покупки тура, взаимодействующего с СУБД и API Банка:
#### Запуск контейнеров приложения и базы:
```
docker-compose up
```
#### Запуск SUT
postgresql:
```
DB_URL=jdbc:postgresql://localhost:5432/app java -jar artifacts/aqa-shop.jar
```
mysql:
```
DB_URL=jdbc:mysql://localhost:3306/app java -jar artifacts/aqa-shop.jar
```
#### Запуск автоматизированных тестов
выполнить команду:
```
gradlew test
```
#### Формирование отчета Allure
выполнить команду:
```
gradle allureReport
```
далее выполнить команду:
```
gradle allureServe
```