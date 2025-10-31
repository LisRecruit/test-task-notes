# 📝 Note Task Manager

A RESTful **Spring Boot** application for managing notes — allowing you to **create, view, update, and delete** notes,  
as well as retrieve **word statistics** for each note.  
The app uses **MongoDB** for data storage and is fully containerized with **Docker Compose**.

---

## ⚙️ Tech Stack

- **Language:** Java 21
- **Framework:** Spring Boot 3
- **Database:** MongoDB 6.0
- **Build Tool:** Gradle
- **Containerization:** Docker, Docker Compose
- **Testing:** JUnit 5, Mockito, Testcontainers

---

## 🧩 Prerequisites

Before running the project, make sure you have:

- Java 21+ (only needed for local builds)
- Docker and Docker Compose installed

---

## 🚀 Running with Docker Compose

The project is preconfigured for isolated Docker execution —  
no local MongoDB setup is required.

### 1. Build and start all services

From the project root (where `docker-compose.yml` is located):

```bash
docker compose up --build
This will:

Build the Spring Boot JAR (testtask-0.0.1-SNAPSHOT.jar) inside the container

Start both the application and MongoDB containers

2. Access the API
Once started, the backend will be available at:

👉 http://localhost:8080

🗺️ API Endpoints
Method	Endpoint	Description
POST	/api/notes	Create a new note
GET	/api/notes	Get all notes (supports pagination, sorting by creation date DESC, and filtering by tags — e.g. ?tags=BUSINESS,PERSONAL)
GET	/api/notes/{id}	Retrieve a note by ID
PUT	/api/notes/{id}	Update an existing note
DELETE	/api/notes/{id}	Delete a note
GET	/api/notes/{id}/stats	Get word frequency statistics for a note (sorted descending)

🧠 Example Requests
Create a Note
POST http://localhost:8080/api/notes

json

{
  "title": "Docker test note",
  "text": "Testing note creation",
  "tags": ["BUSINESS"]
}
Response:

json

{
  "id": "654f1c2e8bda5b1234f90d12",
  "title": "Docker test note",
  "text": "Testing note creation",
  "tags": ["BUSINESS"],
  "createdDate": "2025-10-31T14:32:45Z"
}
Get Word Statistics
GET http://localhost:8080/api/notes/{id}/stats

Response:

json

{
  "testing": 1,
  "note": 1,
  "creation": 1
}
🧹 Stopping and Cleanup
Stop and remove containers:

bash
Копировать код
docker compose down
Remove containers and MongoDB data volume:

bash

docker compose down --volumes
🧾 Notes
MongoDB data is stored in the mongo-data Docker volume

You can rebuild the app at any time — data will persist unless you remove the volume
