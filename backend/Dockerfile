# 1. 빌드 단계 (Build Stage)
# 자바 25 환경에서 소스 코드를 빌드함
FROM eclipse-temurin:25-jdk AS build
WORKDIR /app

# 프로젝트 파일 복사 (gradle 래퍼 포함)
COPY . .

# 실행 권한 부여 및 JAR 파일 빌드 (테스트는 제외하고 빌드 속도 향상)
RUN chmod +x gradlew
RUN ./gradlew clean build -x test

# 2. 실행 단계 (Run Stage)
# 실제 컨테이너 실행을 위한 가벼운 이미지 사용
FROM eclipse-temurin:25-jre-hotspot-minimal
WORKDIR /app

# 빌드 단계에서 생성된 JAR 파일만 가져옴
COPY --from=build /app/build/libs/*.jar app.jar

# 실행
ENTRYPOINT ["java", "-jar", "app.jar"]