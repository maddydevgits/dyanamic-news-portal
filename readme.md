
# News Portal Project

This project is a web application that serves as a news portal where users, employees, and administrators can interact with various features like news management, job applications, and user administration. It is built with Java Spring Boot, Thymeleaf, and a relational database.

## Table of Contents

- [Project Structure](#project-structure)
- [Features](#features)
- [Endpoints](#endpoints)
- [Installation](#installation)
- [Usage](#usage)
- [Technologies](#technologies)

## Project Structure

- **Controllers**: Manage HTTP requests, process user data, and redirect to views.
  - `AuthController`: Handles authentication, registration, and user roles.
  - `EmployeeController`: Manages employee-specific actions like login, dashboard, and news uploads.
  - `JobApplicationController`: Manages job application actions, including viewing, approving, and rejecting applications.

- **Models**: Represent the application's data structure.
  - `User`: Represents a registered user in the application.
  - `Employee`: Represents an employee who can manage news and view applications.
  - `JobApplication`: Represents a user's job application, including details like qualifications, experience, and status.
  - `News`: Represents news articles uploaded by employees.

- **Repositories**: Handle database interactions for models.
  - `UserRepository`
  - `EmployeeRepository`
  - `JobApplicationRepository`
  - `NewsRepository`

- **Services**: Encapsulate business logic and handle complex tasks like sending emails.
  - `EmailService`
  - `JobApplicationService`
  - `UserService`

## Features

- **User Registration and Login**: Users can register, log in, and apply for jobs.
- **Employee Dashboard**: Employees can log in, post news articles, and manage job applications.
- **Admin Dashboard**: The admin can view all job applications, approve or reject them, and view employee details.
- **Email Notifications**: Users receive email notifications for registration, job application submissions, approvals, and rejections.

## Endpoints

### Authentication and Registration

- `GET /register`: Registration page.
- `POST /register`: Registers a new user.
- `GET /login`: User login page.
- `POST /login`: Logs in a user.
- `GET /logout`: Logs out the current user.

### Admin and Employee Management

- `GET /adminlogin`: Admin login page.
- `POST /adminlogin`: Logs in the admin.
- `GET /adminDashboard`: Admin dashboard for managing applications and employees.
- `GET /employee/login`: Employee login page.
- `POST /employee/login`: Logs in an employee.
- `GET /employee/employeeDashboard`: Employee dashboard to upload news and manage applications.

### News Management

- `POST /employee/uploadnews`: Uploads a new news article (employee only).
- `GET /news`: Shows a list of all news articles.
- `GET /news/{id}`: Displays details of a specific news article.
- `GET /adminnews/{id}`: Displays news for the admin with specific details.
- `GET /employeenews1/{id}`: Displays news for employees with specific details.

### Job Applications

- `POST /applyjob`: Submits a job application (user only).
- `GET /applicationDetails/{id}`: Shows detailed information about a specific job application.
- `POST /applications/approve/{id}`: Approves a job application, converting a user to an employee.
- `POST /applications/reject/{id}`: Rejects a job application.
- `POST /applications/update/{id}`: Updates the status of a job application.

## Installation

### Prerequisites

- Java 11+
- Maven
- A relational database (e.g., MySQL)

### Steps

1. **Clone the repository**:
   ```bash
   git clone https://github.com/yourusername/news-portal.git
   ```

2. **Navigate to the project directory**:
   ```bash
   cd news-portal
   ```

3. **Configure database settings**:
   Update `application.properties` in `src/main/resources` to set up your database connection.

4. **Build the project**:
   ```bash
   mvn clean install
   ```

5. **Run the project**:
   ```bash
   mvn spring-boot:run
   ```

6. **Access the application**:
   Open your browser and go to `http://localhost:8080`.

## Usage

- **Register as a User**: Go to `/register`, sign up, and log in.
- **Apply for a Job**: After logging in as a user, go to `/applyjob` to submit a job application.
- **Login as an Employee**: Employees can log in and view their dashboard at `/employee/employeeDashboard`.
- **Admin Login**: Use the static credentials in `AuthController` to log in as an admin and manage job applications.
- **Upload News**: Employees can upload news articles, which will be displayed on the main news page.

## Technologies

- **Backend**: Spring Boot
- **Database**: MySQL (or any relational database)
- **Frontend**: Thymeleaf, Bootstrap
- **Email Service**: JavaMail for email notifications

---
