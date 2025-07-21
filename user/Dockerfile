FROM eclipse-temurin:17-jdk as builder
WORKDIR /app

COPY pom.xml ./
COPY .mvn .mvn
COPY mvnw ./

RUN ./mvnw dependency:go-offline

COPY src ./src

RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]