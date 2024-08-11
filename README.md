# Y_LAB_HW_TIMBEZH

Это репозиторий для хранения домашних заданий по курсу Y_LAB.

## Содержание

Запуск программы выполняется следующим образом: Сначала мы запускаем конфигурационный файл докера, чтобы запустить контейнер с PostgreSQL.
После чего мы запускаем класс с миграцией, чтобы Liquibase создал необходимы таблицы и заполнил необходимые данные. 
Выполнив эти действия, мы можем благополучно запустить нашу программу

## Требования для запуска

JDK 17+
Maven 3.5.8
Docker

## Запуск

1. git clone https://github.com/TimurOlegovichDev/Y_LAB_HW_TIMBEZH.git
2. cd Y_LAB_HW_TIMBEZH/src/main/resources
3. docker-compose up -d
4. cd ..
5. cd java/Model/DataBase/dbconfig
6. java DBMigration
7. cd ..
8. cd ..
9. cd ..
10. java DealerShipApplication

* [Задание 1](/https://github.com/TimurOlegovichDev/Y_LAB_HW_TIMBEZH.git)
## Автор

* [TimurOlegovichDev]([https://github.com/TimurOlegovichDev])
