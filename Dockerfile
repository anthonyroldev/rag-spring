FROM amazoncorretto:25-alpine3.23 AS build

WORKDIR /app

COPY build.gradle.kts settings.gradle.kts gradlew ./
COPY gradle gradle/

RUN ./gradlew dependencies --no-daemon

COPY src src/

RUN ./gradlew bootJar -x test --no-daemon

FROM amazoncorretto:25-alpine3.23 AS runtime

WORKDIR /app

COPY --from=build /app/build/libs/*-SNAPSHOT.jar app.jar

EXPOSE 8080

RUN apk add --no-cache wget

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring


ENTRYPOINT ["java", "-jar", "app.jar"]