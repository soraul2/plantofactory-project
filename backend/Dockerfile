# --- 1. 빌드(Build) 스테이지 ---
# Java 21 JDK(빌드용) 이미지를 사용합니다.
FROM openjdk:21-jdk-slim AS build

WORKDIR /app

# Gradle 래퍼 파일을 먼저 복사합니다. (Maven이면 pom.xml)
COPY build.gradle settings.gradle /app/
COPY gradle /app/gradle

# 의존성을 먼저 다운로드합니다. (레이어 캐시 활용)
RUN ./gradlew dependencies

# 소스 코드를 복사합니다.
COPY src /app/src

# 앱을 빌드합니다. (실행 가능한 jar 파일 생성)
RUN ./gradlew bootJar

# --- 2. 실행(Runtime) 스테이지 ---
# 더 가벼운 Java 21 JRE(실행용) 이미지를 사용합니다.
FROM openjdk:21-jre-slim

WORKDIR /app

# 빌드 스테이지(build)에서 생성된 jar 파일만 복사해옵니다.
# build/libs/backend-0.0.1-SNAPSHOT.jar 같은 파일
COPY --from=build /app/build/libs/*.jar app.jar

# Spring Boot 앱은 기본적으로 8080 포트를 사용합니다.
EXPOSE 8080

# 컨테이너가 시작될 때 이 명령어를 실행합니다.
ENTRYPOINT ["java", "-jar", "app.jar"]