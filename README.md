# 🎓 Student Management System

A web-based **Student Management System** developed using **Java Spring Boot, Thymeleaf, MySQL, and Hibernate/JPA**.

The system provides student registration, login, student management, search, sorting, pagination, photo upload, email notifications, OTP-based password reset, attendance data management, Excel export, PDF/ID-card support, and QR code generation.

---

## ✨ Features

### 👤 User Management

* User Registration
* User Login
* User Authentication
* Forgot Password
* OTP Verification
* Password Reset
* Email notifications

### 🎓 Student Management

* Add Student
* View Students
* Search Student
* Search by Student ID
* Search by Name
* Search by Email
* Search by Course
* Update Student
* Delete Student
* Student Profile
* Student Photo Upload

### 📊 Dashboard

* Total Students
* Total Courses
* Total Users
* Course-wise Student Statistics
* Recent Students
* Student Pagination
* Student Sorting

### 📅 Attendance

* Attendance entity linked with students
* Attendance date
* Attendance status
* Student-attendance relationship

### 📧 Email & OTP

* Student welcome email
* Password reset OTP
* OTP verification
* Gmail SMTP integration

### 📄 Export & Documents

* Export student data to Excel
* Student ID Card
* PDF support

### 🔳 QR Code

* QR Code generation using ZXing
* QR Codes are generated as PNG files
* QR files are stored inside the uploads directory

---

## 🛠 Technologies Used

| Technology        | Purpose               |
| ----------------- | --------------------- |
| Java              | Programming Language  |
| Spring Boot 3.5.4 | Backend Framework     |
| Spring MVC        | Web Application       |
| Spring Data JPA   | Database Access       |
| Hibernate         | ORM                   |
| Thymeleaf         | Frontend Templates    |
| MySQL             | Database              |
| Maven             | Dependency Management |
| JavaMailSender    | Email Service         |
| Apache POI        | Excel Export          |
| iTextPDF          | PDF Generation        |
| ZXing             | QR Code Generation    |
| HTML/CSS          | User Interface        |

---

## 📂 Project Structure

```text
studentmanagementsystem/
│
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── kanchan/
│       │           └── studentmanagementsystem/
│       │               │
│       │               ├── config/
│       │               │   └── WebConfig.java
│       │               │
│       │               ├── entity/
│       │               │   ├── Attendance.java
│       │               │   ├── Student.java
│       │               │   └── User.java
│       │               │
│       │               ├── repository/
│       │               │   ├── StudentRepository.java
│       │               │   └── UserRepository.java
│       │               │
│       │               ├── service/
│       │               │   ├── EmailService.java
│       │               │   ├── ExcelService.java
│       │               │   ├── OtpService.java
│       │               │   ├── StudentService.java
│       │               │   └── UserService.java
│       │               │
│       │               ├── util/
│       │               │   └── QRGenerator.java
│       │               │
│       │               ├── HomeController.java
│       │               └── StudentmanagementsystemApplication.java
│       │
│       └── resources/
│           └── templates/
│               ├── addStudent.html
│               ├── dashboard.html
│               ├── deleteStudent.html
│               ├── forgot-password.html
│               ├── login.html
│               ├── register.html
│               ├── reset-password.html
│               ├── searchResult.html
│               ├── searchStudent.html
│               ├── student-id-card.html
│               ├── student-profile.html
│               ├── updateStudent.html
│               ├── verify-otp.html
│               └── viewStudents.html
│
├── pom.xml
├── .gitignore
└── README.md
```

---

## 🗄 Database

The application uses **MySQL** with Spring Data JPA and Hibernate.

Main database:

```text
studentdb
```

Main tables/entities include:

```text
users
student
attendance
```

The application automatically manages the database schema using Hibernate/JPA configuration.

---

## 📧 Email Configuration

The application uses Gmail SMTP to send:

* Student welcome emails
* Password reset OTP emails

For security, email credentials should **never be committed to GitHub**.

The following file is intentionally ignored by Git:

```text
src/main/resources/application.properties
```

Create this file locally and add your own database and Gmail SMTP configuration.

---

## 🔐 Security

Sensitive configuration is protected using `.gitignore`.

Ignored files/directories include:

```text
src/main/resources/application.properties
uploads/
```

Therefore:

* MySQL passwords are not committed
* Gmail App Passwords are not committed
* Uploaded student photos are not committed
* Generated QR files are not committed

**Never publish passwords, API keys, tokens, or other secrets in the repository.**

---

## ⚙️ Requirements

Before running the project, install:

* Java JDK 21 or compatible JDK
* Maven
* MySQL Server
* Git
* Internet connection for Maven dependencies

---

## ▶️ How to Run

### 1. Clone the repository

```bash
git clone https://github.com/KanchanBijode/studentmanagementsystem.git
```

### 2. Open the project

```bash
cd studentmanagementsystem
```

### 3. Create the MySQL database

Open MySQL and create:

```sql
CREATE DATABASE studentdb;
```

### 4. Configure the application

Create:

```text
src/main/resources/application.properties
```

Add your own:

* MySQL username
* MySQL password
* Gmail address
* Gmail App Password
* SMTP configuration

### 5. Run the application

```bash
mvn spring-boot:run
```

### 6. Open in browser

```text
http://localhost:8080
```

---

## 📊 Student Data Export

The application uses **Apache POI** to generate Excel files containing student information such as:

* Student ID
* Name
* Email
* Course
* Mobile

---

## 🔳 QR Code Generation

The project uses **ZXing** for QR code generation.

Generated QR codes are stored under:

```text
uploads/qr/
```

The QR code is generated as a PNG image.

---

## 📱 Student ID Card

The system includes a student ID-card page that can be used to display student information in an ID-card format.

---

## 📈 Dashboard Statistics

The dashboard provides useful information such as:

```text
Total Students
Total Courses
Total Users
Course-wise Student Count
Recent Students
```

Student records also support:

* Pagination
* Name sorting
* Latest/oldest sorting
* Course filtering
* Name searching

---

## 🧪 Testing

The complete application flow can be tested using:

```text
Register
   ↓
Login
   ↓
Dashboard
   ↓
Add Student
   ↓
Photo Upload
   ↓
View Students
   ↓
Search Student
   ↓
Update Student
   ↓
Delete Student
   ↓
Forgot Password
   ↓
OTP Verification
   ↓
Password Reset
   ↓
Email Notification
```

---

## 🚀 Future Improvements

Possible future enhancements:

* Role-based access control
* Admin and Student separate dashboards
* Attendance management UI
* Attendance reports
* Advanced student filtering
* REST API
* Spring Security authentication
* Cloud image storage
* Deployment on cloud platforms
* Responsive mobile UI

---

## 👨‍💻 Author

**Kanchan Bijode**

GitHub:

https://github.com/KanchanBijode

---

## 📌 Project

**Student Management System**

Built with:

```text
Java
Spring Boot
Thymeleaf
MySQL
Hibernate/JPA
Maven
```

---

⭐ If you find this project useful, consider giving the repository a star.
