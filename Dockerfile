# 1. Java 17 버전 slim 이미지 사용 (필수: Java 17!)
FROM openjdk:17-jdk-slim

# 2. 컨테이너 안에서 작업할 디렉토리 설정
WORKDIR /app

# 3. 빌드된 JAR 파일 복사
COPY build/libs/*.jar app.jar

# 4. 앱 실행 명령어 (환경 변수로 프로필 설정)
ENTRYPOINT ["java", "-Dspring.profiles.active=local", "-jar", "app.jar"]

# 5. 컨테이너가 외부와 통신할 포트 지정
EXPOSE 8080

