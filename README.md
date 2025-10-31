📝 Note Task Manager

This is a RESTful application for managing notes, providing functionality for creating, viewing, updating, and deleting notes, as well as obtaining text statistics for each note.

<<<<<<< HEAD
The project is developed using **Spring Boot** and utilizes **MongoDB** as its data store.

## 🛠️ Technologies and Stack

* **Backend:** Java 21, Spring Boot 3

* **Database:** MongoDB 6.0

* **Build Tool:** Gradle

* **Containerization:** Docker, Docker Compose

* **Testing:** JUnit 5, Mockito, Testcontainers

## ⚙️ Prerequisites

To run the project, the following are required:

1. **Java 21** (for local development)

2. **Docker** and **Docker Compose** (for running in containers)

## 🚀 Running the Project with Docker Compose

The project is configured to run in an isolated Docker environment, ensuring fast deployment and avoiding conflicts with local MongoDB installations.

**1. Build and Run:**

Use the following command to build the JAR file inside the container and simultaneously start all services (application + MongoDB). The `--build` flag is mandatory to ensure Docker compiles the JAR file.
=======
The project is developed using Spring Boot and utilizes MongoDB as its data store.

🛠️ Technologies and Stack

Backend: Java 21, Spring Boot 3

Database: MongoDB 6.0

Build Tool: Gradle

Containerization: Docker, Docker Compose

Testing: JUnit 5, Mockito, Testcontainers

⚙️ Prerequisites

To run the project, the following are required:

Java 21 (for local development)

Docker and Docker Compose (for running in containers)

🚀 Running the Project with Docker Compose

The project is configured to run in an isolated Docker environment, ensuring fast deployment and avoiding conflicts with local MongoDB installations.

1. Build and Run:

Use the following command to build the JAR file inside the container and simultaneously start all services (application + MongoDB). The --build flag is mandatory to ensure Docker compiles the JAR file.
>>>>>>> origin/main

docker compose up --build


<<<<<<< HEAD
**2. Accessing the Application:**
=======
2. Accessing the Application:
>>>>>>> origin/main

After a successful launch, the application will be available at:

http://localhost:8080/


<<<<<<< HEAD
**3. Stopping and Cleanup:**
=======
3. Stopping and Cleanup:
>>>>>>> origin/main

To stop and remove the containers, use:

docker compose down


<<<<<<< HEAD
If you wish to completely remove the MongoDB database (Volume), use the `--volumes` flag:
=======
If you wish to completely remove the MongoDB database (Volume), use the --volumes flag:
>>>>>>> origin/main

docker compose down --volumes


<<<<<<< HEAD
## 🗺️ Key API Endpoints

The application provides a RESTful API accessible via port `8080`.

| Method    | Endpoint                      | Description                                                                                         |
| :-------- | :---------------------------- | :-------------------------------------------------------------------------------------------------- |
| `POST`    | `/api/notes`                  | Create a new note.                                                                                  |
| `GET`     | `/api/notes`                  | Get a list of notes with support for pagination, sorting (by creation date DESC), and filtering by tags (`?tags=BUSINESS,PERSONAL`). |
| `GET`     | `/api/notes/{id}`             | Get a note by ID.                                                                                   |
| `PUT`     | `/api/notes/{id}`             | Update an existing note by ID.                                                                      |
| `DELETE`  | `/api/notes/{id}`             | Delete a note by ID.                                                                                |
| `GET`     | `/api/notes/{id}/stats`       | Get word statistics for a note by ID (word frequency, sorted in descending order).                  |
=======
🗺️ Key API Endpoints

The application provides a RESTful API accessible via port 8080.

Method

Endpoint

Description

POST

/api/notes

Create a new note.

GET

/api/notes

Get a list of notes with support for pagination, sorting (by creation date DESC), and filtering by tags (?tags=BUSINESS,PERSONAL).

GET

/api/notes/{id}

Get a note by ID.

PUT

/api/notes/{id}

Update an existing note by ID.

DELETE

/api/notes/{id}

Delete a note by ID.

GET

/api/notes/{id}/stats

Get word statistics for a note by ID (word frequency, sorted in descending order).
>>>>>>> origin/main
