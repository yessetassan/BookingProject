# Базовый образ с OpenJDK 17
FROM eclipse-temurin:17-jdk

# Рабочая директория внутри контейнера
WORKDIR /app

# Копируем JAR-файл из папки target
COPY target/*.jar app.jar

# Открываем порт
EXPOSE 8080

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"]
