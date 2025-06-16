# Geotechnical Testing Search Application

## Introduction
This project is an application designed to assist users, particularly inexperienced clients or engineers, in locating geotechnical tests, standard test methods, testing parameters, and testing specifications. The application integrates three different geotechnical testing databases, allowing the user to search by test names, parameters (e.g., Young's modulus), or standard test methods.

## Table of Contents
- [Stakeholders](#stakeholders)
- [User Stories](#user-stories)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
- [Project Structure](#project-structure)
- [Endpoints](#endpoints)
- [Available Scripts](#available-scripts)
- [Future Scope](#future-scope)
- [License](#license)

 ## Stakeholders
- The stakeholders of this project are the junior engineers working at Structured Soils Ltd, who are more inexperienced and need to reference the GeotechnicalDatabase to find tests
- Senior engineers who will occasionally use the database for referencing tests
- The director of Structured Soils Ltd
- As a client of Structured Soils Ltd, we belive that this application will improve the speed of the testing process allowing us to obtain the data we need faster.

## User Stories
### Junior Engineer
- As a junior engineer, who currently lacks the experience to have all tests memorised, this product will help me find the test I want to use after inputting a certain set of parameters. It will also help me find the parameters needed for a particular test that I am conducting. This will not only save me time, but will hopefully help me to remember the necessary parameters and vice versa.
### Senior Engineer
- As a senior engineer, whilst I have extensive knowledge of all tests and the parameters needed for to carry out these tests, there are times when I am in a rush and in need of a quick reference to speed up the development of my projects.
### Director
- As the director of the company, this product will help junior engineers find the test they need quicker and more efficiently.
  

## Features
- Search for geotechnical tests by simple or complex search terms.
- Search based on specific test parameters, standard test methods, or test names.
- Support complex searches by geotechnical problems (e.g., foundation design).

## Technologies Used
- **Backend**: Spring Boot
- **Frontend**: React.js
- **Database**: SQL (PostgreSQL or MySQL recommended)
- **Build Tools**: Maven (for Spring Boot)
- **Version Control**: GitHub

## Getting Started
### Prerequisites
- Java 11 or higher
- Node.js and npm
- IDE (e.g., IntelliJ, VSCode)
- Git
- Docker

### Installation
1. Clone the repository:
   ```sh
   git clone <repository-url>
   cd geotechnical-testing-app
   ```

   Start your downloaded docker application, log in and do nothing, then go to the next step
2. Backend (Spring Boot):
   - Navigate to the backend directory:
     ```sh
     cd backend
     ```
   - Build the backend project using Maven:
     ```sh
     mvn clean install
     ```
   - Run the Spring Boot application:
     ```sh
     mvn spring-boot:run
     ```
   - Run
    ```bash
    mvn cleen package
     ```
    - In the database directory
    ```sh
     cd database
     ```
4. Frontend (React):
   - Navigate to the frontend directory:
     ```sh
     cd ../frontend
     ```
   - Install dependencies:
     ```sh
     npm install
     ```
 4.Root directory(docker compose):
   - Navigate to the project's root directory (the one containing `docker-compose.yml`).
   - Execute the following command to build and start the containers:
     ```bash
     docker-compose up --build
     ```
   - This command will automatically build and start three containers:
     - **GeoTechnicalDatabase**: MySQL database
     - **GeoTechnicalApp**: Spring Boot backend
     - **GeoTechnicalFrontend**: React frontend
     - 
  5.Quick Start Method:

   -Finally, we realized the shortcut in the root directory, we for window linux mac system are configured with three "**runStartScript**" (linux and mac need to configure its security access), as long as the guarantee docker running in the background this, click to start.

   > **Note:** The first startup may take some time as Docker pulls images, initializes the database, and packages the backend.
 

**Then open the`http://localhost:3100`link in any browser on your computer and you will be able to view a visualization of the project's interface**

**Stopping Containers:**
   - To stop the containers, press `Ctrl + C` in the terminal where they are running.
  ## Logs and Debugging

### Terminal Logs

- If you did **not** use the `-d` (detached) flag when running the containers, all logs (from the backend, frontend, etc.) will appear in the terminal.

### Container Logs

- If you ran the containers in detached mode (`-d`), you can inspect each container's logs individually:

- **Backend (Spring Boot App):**
  ```bash
  docker-compose logs -f app
  ```

- **Frontend (React App):**
  ```bash
  docker-compose logs -f frontend
  ```

- **Database (MySQL):**
  ```bash
  docker-compose logs -f mysql
  ```

These logs provide details such as the frontend's requests to the backend and database initialization and queries.

### Inspecting the Database Manually

- To manually inspect or debug the database, connect to:
localhost:3307

> **Important:** The default MySQL port `3306` is mapped to `3307` in the Docker Compose file.  
> The username and password are specified in the `environment` section of `docker-compose.yml`.

### Configuration
- The backend server runs on `http://localhost:8080` by default.
- The frontend application runs on `http://localhost:3100` by default.

## Project Structure
### Backend (Spring Boot)
- **/src/main/java/com/geoapp**: The main directory containing Java source files.
  - **controller**: Defines REST API endpoints for searching tests.
  - **service**: Contains the business logic for handling search queries.
  - **repository**: Handles the interaction with the database.
  - **model**: Defines entities and data models for the application.
  - **GeoApplication.java**: Main entry point for the Spring Boot application.
- **/src/main/resources**: Contains application properties.
  - **application.properties**: Configuration for database connection, port, etc.

### Frontend (React)
- **/src**: The main directory for React components.
  - **components**: UI components like search bar, result list, etc.
  - **pages**: Main pages like SearchPage, ResultPage.
  - **services**: Handles communication with the backend API.
  - **App.js**: Main component that holds the routing logic.
- **/public**: Static assets like HTML template, images, etc.

## Endpoints
- **GET /api/tests**: Retrieve a list of all geotechnical tests.
- **GET /api/tests/search**: Search for tests by keyword, parameter, or test method.
  - **Query Parameters**:
    - `name`: Search by test name.
    - `parameter`: Search by test parameter (e.g., Young's modulus).
    - `method`: Search by standard test method.

## Available Scripts
### Backend
- **`mvn spring-boot:run`**: Run the backend server.
- **`mvn clean install`**: Build the backend.

### Frontend
- **`npm start`**: Runs the frontend in the development mode.
- **`npm run build`**: Builds the app for production.

## Future Scope
- Expand the database to include additional geotechnical tests and standards.
- Allow exporting of search results in PDF or Excel format.

## License
This project is licensed under the terms of the MIT license.
