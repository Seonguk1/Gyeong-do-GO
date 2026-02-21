# 1. 베이스 이미지 설정 (자바 버전 17 기준)
FROM openjdk:25-jdk-slim

# 2. 컨테이너 내부 작업 디렉토리 설정
WORKDIR /app

# 3. 빌드된 JAR 파일을 컨테이너 안으로 복사
# (미리 ./gradlew build를 통해 JAR가 생성되어 있어야 함)
COPY build/libs/*.jar app.jar

# 4. 앱 실행 명령어
ENTRYPOINT ["java", "-jar", "app.jar"]