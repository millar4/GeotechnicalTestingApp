FROM openjdk:17-jdk-slim

WORKDIR /app

RUN apt-get update && apt-get upgrade -y

COPY ./backend/database/target/database-0.0.1-*.jar /app/app.jar
COPY ./backend/database/parameters2.csv /parameters2.csv
COPY ./backend/database/rockParameters2.csv /rockParameters2.csv

EXPOSE 8080

CMD ["java", "-jar", "/app/app.jar"]

