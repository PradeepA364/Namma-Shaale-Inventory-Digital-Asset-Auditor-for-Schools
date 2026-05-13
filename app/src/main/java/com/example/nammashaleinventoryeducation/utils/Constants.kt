package com.example.nammashaleinventoryeducation.utils

object Constants {
    // Admin credentials
    const val ADMIN_EMAIL = "admin@school.com"
    const val ADMIN_PASSWORD = "admin123"
    const val ADMIN_NAME = "Admin"

    // Asset conditions
    const val CONDITION_WORKING = "Working"
    const val CONDITION_NEEDS_REPAIR = "Needs Repair"
    const val CONDITION_BROKEN = "Broken"

    val CONDITIONS = listOf(CONDITION_WORKING, CONDITION_NEEDS_REPAIR, CONDITION_BROKEN)

    // Asset categories
    val CATEGORIES = listOf(
        "Computers", "Laptops", "Tablets", "Projectors", "Printers",
        "Lab Equipment", "Sports", "Furniture", "Classroom",
        "Digital Devices", "Electrical", "Books"
    )

    // Repair statuses
    const val REPAIR_PENDING = "Pending"
    const val REPAIR_IN_PROGRESS = "In Progress"
    const val REPAIR_COMPLETED = "Completed"
    const val REPAIR_CANCELLED = "Cancelled"

    val REPAIR_STATUSES = listOf(REPAIR_PENDING, REPAIR_IN_PROGRESS, REPAIR_COMPLETED, REPAIR_CANCELLED)

    // Roles
    const val ROLE_TEACHER = "Teacher"
    const val ROLE_ADMIN = "Admin"

    // School info
    const val SCHOOL_NAME = "Namma Shaale Government School"
    const val APP_VERSION = "1.0.0"
}
