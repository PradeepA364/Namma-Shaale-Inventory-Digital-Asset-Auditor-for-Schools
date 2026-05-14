# Namma-Shaale Inventory

## Smart School Asset Management & Audit App

Namma-Shaale Inventory is an Android application developed to digitally manage and monitor school assets such as laboratory equipment, sports kits, computers, projectors, tablets, and classroom resources. The application helps schools improve asset tracking, maintenance monitoring, accountability, and inventory management through a modern mobile platform.

---

# Project Overview

Schools often receive government-funded resources, but many institutions do not have a proper digital system to monitor asset condition, maintenance status, or repair history. This project provides a lightweight and user-friendly solution for tracking school inventory efficiently.

The application supports:

* Asset registration
* Asset condition monitoring
* Monthly health checks
* Repair and issue tracking
* Role-based access control
* Analytics dashboard
* PDF report generation

---

# Features

## Teacher Module

* Teacher Login
* View Asset List
* Search & Filter Assets
* Update Asset Condition
* Monthly Health Check
* Capture Asset Images using CameraX
* Log Issues for Damaged/Lost Assets
* View Repair Status

## Admin Module

* Admin Dashboard
* Add/Edit/Delete Assets
* Analytics Dashboard
* Repair Management
* PDF Report Generation
* Download & Share Reports
* Inventory Monitoring

---

# Technologies Used

* Kotlin
* Jetpack Compose
* MVVM Architecture
* Room Database
* Navigation Compose
* Material 3
* CameraX
* StateFlow
* Coroutines
* PDF Generation
* Android Studio

---

# Application Architecture

The project follows MVVM (Model View ViewModel) architecture.

## Folder Structure

```text
app/
 ├── data/
 │    ├── dao/
 │    ├── database/
 │    ├── entity/
 │    └── repository/
 │
 ├── ui/
 │    ├── admin/
 │    ├── teacher/
 │    ├── common/
 │    ├── components/
 │    └── navigation/
 │
 ├── viewmodel/
 └── utils/
```

---

# Screens Included

1. Splash Screen
2. Login Screen
3. Teacher Dashboard
4. Admin Dashboard
5. Asset List Screen
6. Add Asset Screen
7. Asset Details Screen
8. Health Check Screen
9. Issue/Repair Screen
10. Reports Screen
11. Analytics Screen
12. Profile Screen

---

# Key Functionalities

## Asset Management

Teachers and Admins can monitor asset conditions such as:

* Working
* Needs Repair
* Broken

## CameraX Integration

High-value asset images can be captured and stored locally for documentation.

## Role-Based Access

Separate workflows and dashboards are implemented for:

* Teacher
* Admin / SDMC

## Analytics Dashboard

Admin users can monitor:

* Asset distribution
* Repair trends
* Condition statistics
* Monthly summaries

## PDF Report Generation

Admins can generate downloadable inventory audit reports in PDF format.

---

# Problem Statement

Schools frequently lack a proper digital inventory management system for tracking educational resources and monitoring their condition. This leads to delayed repairs, poor accountability, and inefficient resource utilization. Namma-Shaale Inventory addresses this issue by providing a smart and organized inventory management solution.

---

# Future Enhancements

* Cloud Backup Integration
* Barcode/QR Scanning
* Real-time Notifications
* Multi-school Support
* Firebase Authentication
* Online Sync Support

---

# Installation

## Clone Repository

```bash
git clone <repository-link>
```

## Open Project

1. Open Android Studio
2. Select "Open Project"
3. Choose the project folder

## Run Application

1. Connect Android device or emulator
2. Click Run ▶

---

# Developed By

Pradeep A

askokk499@gmail.com

Android Application Development using Generative AI Internship Project

MindMatrix.io | CL Infotech Pvt. Ltd.

---

# License

This project is developed for educational and internship purposes.
