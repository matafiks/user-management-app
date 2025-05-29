# User Management System

A full-stack web application for managing user accounts, built with **Spring Boot** for the backend and **React**  for the frontend.

The application allows users to register, log in, view the user list, and perform edit and delete operations. It includes essential security features such as password encryption, role-based access control, CSRF protection, and input validation.

---

## 🧩 Technologies Used

### Backend
- Java 17+
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate
- MySQL
- JWT (JSON Web Tokens)
- Maven

### Frontend
- React

---

## 🔐 Security Features

- Passwords are hashed using BCrypt
- JWT-based stateless authentication
- Role-based access control (USER, ADMIN)
- CSRF protection
- Input validation
- Protection against common attacks like XSS

---

## 📱 Features

- ✅ User registration
- ✅ Login/logout with JWT
- ✅ Display list of users (admin only)
- ✅ Edit user information
- ✅ Delete user account
- ✅ Responsive and accessible UI
- ✅ Protected routes (only authorized users can access certain pages)

---

## 📂 Project Structure

<pre>
<code>user-management-app/ 
├── backend/ # Spring Boot
│ └── ... 
├── frontend/ # React 
│ └── ... 
├── .gitignore 
├── LICENSE 
└── README.md
└── create_database.sql # SQL code to create database
└── start-all.bat # Script to automatically run both frontend and backend at once </code>
</pre>