# Consultant Management System

A full-stack Consultant Management System developed using Java, Spring Boot,
Spring Data JPA, Thymeleaf, Spring Security, MySQL, Bootstrap, and Chart.js.

The application provides a centralized interface for managing consultant
records, monitoring consultant status, viewing reports, and exporting data.

## Features

- Secure administrator login and logout
- Add, view, edit, and delete consultants
- Dynamic dashboard data from MySQL
- Total consultant count
- Active consultant count
- Available consultant count
- On-project consultant count
- New consultants this month
- Consultant status doughnut chart
- Technology distribution bar chart
- Search by name, email, and technology
- Filter consultants by status
- Sort consultant records
- Server-side pagination
- Excel export
- PDF export
- Reports section
- Form validation and error messages
- Responsive user interface
- Support for more than 100 consultant records

## Technology Stack

### Backend

- Java 21
- Spring Boot 4.1.0
- Spring MVC
- Spring Data JPA
- Hibernate
- Spring Security
- Jakarta Validation
- Maven

### Frontend

- Thymeleaf
- HTML5
- CSS3
- Bootstrap 5
- JavaScript
- Chart.js

### Database

- MySQL

### Export Libraries

- Apache POI for Excel exports
- OpenPDF for PDF exports

## Application Architecture

The application follows a layered architecture:

- **Controller layer:** Handles HTTP requests and prepares model data
- **Service layer:** Contains business logic and transaction management
- **Repository layer:** Communicates with MySQL through Spring Data JPA
- **Entity layer:** Maps Java objects to database tables
- **View layer:** Uses Thymeleaf to render dynamic HTML pages

## Prerequisites

Install the following software before running the application:

- Java 21 or later
- Maven
- MySQL 8
- IntelliJ IDEA or another Java IDE
- Git

## Database Setup

Create the MySQL database:

```sql
CREATE DATABASE Consultant_Management;