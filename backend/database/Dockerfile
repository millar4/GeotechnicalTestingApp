FROM openjdk:17-jdk-slim

WORKDIR /app

# Update package list and upgrade if necessary
RUN apt-get update && apt-get upgrade -y

# Copy the Spring Boot application JAR
COPY ./backend/database/target/database-0.0.1-*.jar /app/app.jar

# Copy the CSV files from the correct directory
COPY ./backend/database/src/csv/parameters2.csv /parameters2.csv
COPY ./backend/database/src/csv/aggregate.csv /aggregate.csv
COPY ./backend/database/src/csv/rockParameters.csv /rockParameters.csv
COPY ./backend/database/src/csv/concreteParameters.csv /concreteParameters.csv
COPY ./backend/database/src/csv/inSituParameters.csv /inSituParameters.csv
COPY ./backend/database/src/csv/earthworksParameters.csv /earthworksParameters.csv

# Expose the application port
EXPOSE 8080

# Run the Spring Boot application
CMD ["java", "-jar", "/app/app.jar"]
