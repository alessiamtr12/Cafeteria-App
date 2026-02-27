# Cafeteria App

A full-stack Spring Boot application for managing cafeteria operations, featuring a **Distributed RMI Architecture** and a **Smart Content-Based Recommendation Engine**. 
This was a team project and my main contribution was the Content-Based Recommendation System.

---

## Content-Based Recommender
The core highlight of this application is the `RecommendationService`. Unlike simple random suggestions, it uses **Jaccard Similarity** on item metadata to understand user preferences.

### How it Works:
1.  **Profile Extraction:** The system builds a "Taste Profile" for the user by aggregating keywords from the names and descriptions of all items they have marked as favorites.
2.  **Keyword Tokenization:** Each menu component's content is processed into lower-case tokens, stripping punctuation and whitespace.
3.  **Similarity Calculation:** It calculates the overlap between the user's profile and new items using the Jaccard Index formula:
    $$J(A, B) = \frac{|A \cap B|}{|A \cup B|}$$
    *(Where A is the User Profile and B is the Item Keywords)*.
4.  **Filtering & Ranking:** The system automatically filters out items the user has already favorited, ranks the rest by their similarity score, and returns the top matches.

---

## Key Components

### 1. `RecommendationService`
The engine of the app. It uses Java Streams to map, filter, and score every item in the database against the user's favorite history in real-time.

### 2. `FavoriteController`
Manages user-specific state. It uses the `java.security.Principal` to ensure favorites are mapped to the correct authenticated user, allowing for a personalized "Toggle" experience.

### 3. `AdminController`
A secure portal (protected by `@PreAuthorize("hasRole('ADMIN')")`) that acts as an RMI client. It allows administrators to perform CRUD operations on dishes and complex Menu Bundles across the network.

---

## Tech Stack & Architecture

### MVC Architecture & Thymeleaf
The application follows the **Model-View-Controller (MVC)** architecture. It uses **Thymeleaf** as the server-side template engine to dynamically render the frontend, ensuring a clean separation between business logic and the UI.

### Spring Data JPA
Data persistence is handled via **Spring Data JPA** with **Hibernate**. This provides an abstraction layer for database operations, allowing for efficient management of User and Menu entities.

### Distributed RMI
Administrative functions are decoupled using **Java RMI**, allowing the Admin panel to interact with a remote server for menu management.

---

## Database Configuration
The project uses **SQLite** optimized with **Write-Ahead Logging (WAL)** to support concurrent access in a distributed environment.

---

### Run application
```bash
mvn spring-boot:run
