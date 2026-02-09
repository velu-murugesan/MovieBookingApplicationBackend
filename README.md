# Movie Booking Application - Backend

A robust and scalable RESTful API backend for a movie ticket booking system built with Spring Boot. This application provides comprehensive functionality for managing movies, theaters, shows, and bookings with secure user authentication and role-based access control.

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Installation & Setup](#installation--setup)
- [API Documentation](#api-documentation)
- [Database Schema](#database-schema)
- [Security](#security)
- [Contributing](#contributing)

## 🎯 Overview

This Spring Boot application serves as the backend for a movie booking platform, enabling users to browse movies, view show timings, book seats, and manage their reservations. The system supports role-based access with separate functionalities for regular users and administrators.

## ✨ Features

### User Management
- 🔐 **User Registration & Authentication** - Secure signup and login with JWT tokens
- 👥 **Role-Based Access Control** - Separate permissions for Users and Admins
- 🔑 **Password Encryption** - BCrypt password hashing for security

### Movie & Theater Management
- 🎬 **Movie Management** - CRUD operations for movies with details like genre, language, duration, and release date
- 🏢 **Theater Management** - Manage theaters with capacity, location, and screen type information
- 🎭 **Show Scheduling** - Create and manage movie shows with timing and pricing

### Booking System
- 🎫 **Seat Booking** - Book specific seats for shows
- ✅ **Seat Validation** - Real-time seat availability checking
- 🚫 **Duplicate Prevention** - Prevents booking already occupied seats
- 💰 **Dynamic Pricing** - Automatic price calculation based on number of seats
- 📊 **Booking Status** - Track booking status (PENDING, CONFIRMED, CANCELLED)
- 📄 **Pagination Support** - Efficient data retrieval with pagination

### Security Features
- 🛡️ **JWT Authentication** - Stateless authentication using JSON Web Tokens
- 🔒 **Spring Security Integration** - Comprehensive security configuration
- 🚷 **Access Control** - Custom access denied handling with meaningful error messages

## 🛠 Tech Stack

- **Framework**: Spring Boot 3.5.6
- **Language**: Java 21
- **Database**: MySQL
- **ORM**: Spring Data JPA (Hibernate)
- **Security**: Spring Security + JWT (jjwt 0.13.0)
- **Build Tool**: Maven
- **Development Tools**: 
  - Lombok (Reduce boilerplate code)
  - Spring Boot DevTools (Hot reload)

## 📁 Project Structure

```
src/main/java/com/velu/MovieBookingApplication/
│
├── config/              # Configuration classes (Security, JWT, etc.)
├── controller/          # REST API Controllers
├── dto/                 # Data Transfer Objects
│   ├── BookingDto.java
│   ├── LoginRequestDto.java
│   ├── LoginResponseDTO.java
│   ├── MovieDTO.java
│   ├── RegisterRequestDTO.java
│   ├── ShowDTO.java
│   └── TheaterDTO.java
│
├── entity/              # JPA Entities
│   ├── Booking.java
│   ├── Movie.java
│   ├── Show.java
│   ├── Theater.java
│   └── User.java
│
├── enums/               # Enumerations
│   └── BookingStatus.java
│
├── exception/           # Custom Exceptions & Handlers
│   ├── CustomAccessDeniedHandler.java
│   ├── DuplicateSeatException.java
│   ├── InvalidSeatSelectionException.java
│   ├── ResourceNotFoundException.java
│   ├── SeatsNotAvailableException.java
│   └── UserAlreadyExistsException.java
│
├── Repository/          # JPA Repositories
│   ├── BookingRepository.java
│   ├── MovieRepository.java
│   ├── ShowRepository.java
│   ├── TheaterRepository.java
│   └── UserRepository.java
│
├── service/             # Business Logic Layer
│   ├── AuthenticationService.java
│   ├── BookingService.java
│   ├── JwtService.java
│   ├── MovieService.java
│   ├── ShowService.java
│   └── TheaterService.java
│
├── util/                # Utility Classes
│   └── Utils.java
│
└── MovieBookingApplication.java  # Main Application Class
```

## 📋 Prerequisites

Before running this application, ensure you have the following installed:

- **Java Development Kit (JDK) 21** or higher
- **Maven 3.6+** (or use the included Maven wrapper)
- **MySQL 8.0+** database server
- **Git** (for cloning the repository)

## 🚀 Installation & Setup

### 1. Clone the Repository

```bash
git clone https://github.com/velu-murugesan/MovieBookingApplicationBackend.git
cd MovieBookingApplicationBackend
```

### 2. Configure Database

Create a MySQL database for the application:

```sql
CREATE DATABASE movie_booking_db;
```

Update the database configuration in `src/main/resources/application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/movie_booking_db
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# JWT Configuration (Add your secret key)
jwt.secret=your_secret_key_here
jwt.expiration=86400000
```

### 3. Build the Project

Using Maven wrapper (recommended):

```bash
# On Linux/Mac
./mvnw clean install

# On Windows
mvnw.cmd clean install
```

Or using Maven directly:

```bash
mvn clean install
```

### 4. Run the Application

```bash
# Using Maven wrapper
./mvnw spring-boot:run

# Or using Maven
mvn spring-boot:run

# Or run the JAR file
java -jar target/MovieBookingApplication-0.0.1-SNAPSHOT.jar
```

The application will start on `http://localhost:8080` by default.

## 📡 API Documentation

### Authentication Endpoints

#### Register User
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "password123"
}
```

#### Register Admin
```http
POST /api/auth/register/admin
Content-Type: application/json

{
  "username": "admin_user",
  "email": "admin@example.com",
  "password": "adminpass123"
}
```

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "john_doe",
  "password": "password123"
}
```

**Response:**
```json
{
  "jwtToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "john_doe",
  "roles": ["ROLE_USER"]
}
```

### Movie Endpoints

#### Get All Movies (Paginated)
```http
GET /api/movies?page=0&size=10&sortBy=name
Authorization: Bearer <jwt_token>
```

#### Get Movie by ID
```http
GET /api/movies/{id}
Authorization: Bearer <jwt_token>
```

#### Create Movie (Admin only)
```http
POST /api/movies
Authorization: Bearer <jwt_token>
Content-Type: application/json

{
  "name": "Inception",
  "genre": "Sci-Fi",
  "description": "A mind-bending thriller",
  "language": "English",
  "release_date": "2010-07-16",
  "duration": 148
}
```

### Theater Endpoints

#### Create Theater (Admin only)
```http
POST /api/theaters
Authorization: Bearer <jwt_token>
Content-Type: application/json

{
  "theaterName": "PVR Cinemas",
  "theaterLocation": "Mumbai",
  "theaterCapacity": 200,
  "theaterScreenType": "IMAX"
}
```

### Show Endpoints

#### Create Show (Admin only)
```http
POST /api/shows
Authorization: Bearer <jwt_token>
Content-Type: application/json

{
  "showTime": "2026-02-15T18:00:00",
  "price": 250.00,
  "movie_id": 1,
  "theater_id": 1
}
```

### Booking Endpoints

#### Create Booking
```http
POST /api/bookings
Authorization: Bearer <jwt_token>
Content-Type: application/json

{
  "numberOfSeats": 3,
  "bookingDate": "2026-02-15T18:00:00",
  "seatNumbers": ["A1", "A2", "A3"],
  "userId": 1,
  "showId": 1
}
```

#### Get User Bookings (Paginated)
```http
GET /api/bookings/user/{userId}?page=0&size=10
Authorization: Bearer <jwt_token>
```

#### Get Show Bookings (Admin only)
```http
GET /api/bookings/show/{showId}?page=0&size=10
Authorization: Bearer <jwt_token>
```

## 🗄 Database Schema

### Core Entities

**User**
- id (Primary Key)
- username (Unique)
- email
- password (Encrypted)
- roles (Collection)

**Movie**
- id (Primary Key)
- name
- genre
- description
- language
- release_date
- duration

**Theater**
- id (Primary Key)
- theaterName
- theaterLocation
- theaterCapacity
- theaterScreenType

**Show**
- id (Primary Key)
- showTime
- price
- movie_id (Foreign Key)
- theater_id (Foreign Key)

**Booking**
- id (Primary Key)
- numberOfSeats
- bookingDate
- price
- bookingStatus (PENDING, CONFIRMED, CANCELLED)
- createdAt
- seatNumbers (Collection)
- user_id (Foreign Key)
- show_id (Foreign Key)

## 🔒 Security

### JWT Authentication Flow

1. User registers or logs in
2. Server validates credentials and generates JWT token
3. Client includes token in Authorization header for subsequent requests
4. Server validates token and processes request

### Role-Based Access

- **ROLE_USER**: Can browse movies, view shows, create bookings
- **ROLE_ADMIN**: All user permissions + manage movies, theaters, shows, and view all bookings

### Password Security

- Passwords are encrypted using BCrypt algorithm
- Minimum password length: 6 characters
- Password validation on registration

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is created for demonstration purposes.

## 👤 Author

**Velu Murugesan**
- GitHub: [@velu-murugesan](https://github.com/velu-murugesan)

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- All contributors and supporters of this project

---

**Note**: This is a backend API application. For a complete movie booking system, you'll need to develop a frontend application that consumes these APIs.
