package com.example.nammashaleinventoryeducation.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.nammashaleinventoryeducation.data.dao.*
import com.example.nammashaleinventoryeducation.data.entity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Database(
    entities = [Teacher::class, Asset::class, HealthLog::class, IssueLog::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun teacherDao(): TeacherDao
    abstract fun assetDao(): AssetDao
    abstract fun healthLogDao(): HealthLogDao
    abstract fun issueLogDao(): IssueLogDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "namma_shaale_inventory_db"
                )
                    .addCallback(SeedDatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class SeedDatabaseCallback : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            // Using a separate scope and adding a small delay to avoid deadlocks
            // during the initial database creation and opening process.
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    // Wait a bit to ensure database is fully initialized
                    kotlinx.coroutines.delay(500)
                    INSTANCE?.let { database ->
                        seedTeachers(database.teacherDao())
                        seedAssets(database.assetDao())
                        seedHealthLogs(database.healthLogDao())
                        seedIssueLogs(database.issueLogDao())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        private suspend fun seedTeachers(dao: TeacherDao) {
            dao.insertAll(listOf(
                Teacher(name = "Priya Sharma", email = "priya@school.com", password = "priya123", role = "Teacher"),
                Teacher(name = "Rajesh Kumar", email = "rajesh@school.com", password = "rajesh123", role = "Teacher"),
                Teacher(name = "Anitha Rao", email = "anitha@school.com", password = "anitha123", role = "Teacher")
            ))
        }

        private suspend fun seedAssets(dao: AssetDao) {
            dao.insertAll(listOf(
                Asset(assetName = "Dell OptiPlex 7090", serialNumber = "DL-OPT-2024-001", category = "Computers", condition = "Working", notes = "Computer Lab - Station 1", purchaseDate = "2024-01-15"),
                Asset(assetName = "Dell OptiPlex 7090", serialNumber = "DL-OPT-2024-002", category = "Computers", condition = "Working", notes = "Computer Lab - Station 2", purchaseDate = "2024-01-15"),
                Asset(assetName = "HP ProBook 450 G9", serialNumber = "HP-PB-2024-001", category = "Laptops", condition = "Working", notes = "Staff Room Laptop", purchaseDate = "2024-02-10"),
                Asset(assetName = "HP ProBook 450 G9", serialNumber = "HP-PB-2024-002", category = "Laptops", condition = "Needs Repair", notes = "Battery needs replacement", purchaseDate = "2024-02-10"),
                Asset(assetName = "Samsung Galaxy Tab A8", serialNumber = "SM-TAB-2024-001", category = "Tablets", condition = "Working", notes = "Library digital reading", purchaseDate = "2024-03-05"),
                Asset(assetName = "Epson EB-X51 Projector", serialNumber = "EP-PRJ-2024-001", category = "Projectors", condition = "Working", notes = "Main Auditorium", purchaseDate = "2023-08-20"),
                Asset(assetName = "Epson EB-X51 Projector", serialNumber = "EP-PRJ-2024-002", category = "Projectors", condition = "Broken", notes = "Lamp burnt out - Class 10A", purchaseDate = "2023-08-20"),
                Asset(assetName = "HP LaserJet Pro M404", serialNumber = "HP-PRT-2024-001", category = "Printers", condition = "Working", notes = "Admin Office Printer", purchaseDate = "2024-01-20"),
                Asset(assetName = "Olympus CX23 Microscope", serialNumber = "OL-MIC-2024-001", category = "Lab Equipment", condition = "Working", notes = "Biology Lab", purchaseDate = "2023-06-15"),
                Asset(assetName = "Olympus CX23 Microscope", serialNumber = "OL-MIC-2024-002", category = "Lab Equipment", condition = "Needs Repair", notes = "Lens adjustment needed", purchaseDate = "2023-06-15"),
                Asset(assetName = "Chemistry Kit - Advanced", serialNumber = "CK-ADV-2024-001", category = "Lab Equipment", condition = "Working", notes = "Chemistry Lab - Set A", purchaseDate = "2024-04-01"),
                Asset(assetName = "Physics Lab Equipment Set", serialNumber = "PH-EQP-2024-001", category = "Lab Equipment", condition = "Working", notes = "Physics Lab - Mechanics Kit", purchaseDate = "2024-04-01"),
                Asset(assetName = "Football Kit (Set of 5)", serialNumber = "SP-FB-2024-001", category = "Sports", condition = "Working", notes = "Sports Room", purchaseDate = "2024-05-10"),
                Asset(assetName = "Cricket Bat - SG", serialNumber = "SP-CB-2024-001", category = "Sports", condition = "Needs Repair", notes = "Handle grip worn out", purchaseDate = "2023-09-15"),
                Asset(assetName = "Volleyball Net", serialNumber = "SP-VN-2024-001", category = "Sports", condition = "Working", notes = "Outdoor Court", purchaseDate = "2024-05-10"),
                Asset(assetName = "Student Bench (3-seater)", serialNumber = "FR-SB-2024-001", category = "Furniture", condition = "Working", notes = "Class 8B", purchaseDate = "2023-04-10"),
                Asset(assetName = "Teacher Chair - Ergonomic", serialNumber = "FR-TC-2024-001", category = "Furniture", condition = "Broken", notes = "Wheel broken - Staff Room", purchaseDate = "2023-04-10"),
                Asset(assetName = "Library Book Shelf (6ft)", serialNumber = "FR-LS-2024-001", category = "Furniture", condition = "Working", notes = "Library - Section A", purchaseDate = "2023-03-20"),
                Asset(assetName = "Whiteboard 6x4ft", serialNumber = "CL-WB-2024-001", category = "Classroom", condition = "Working", notes = "Class 9A", purchaseDate = "2024-01-05"),
                Asset(assetName = "Samsung Smart TV 55\"", serialNumber = "SM-TV-2024-001", category = "Digital Devices", condition = "Working", notes = "Smart Classroom 1", purchaseDate = "2024-06-01"),
                Asset(assetName = "CCTV Camera - Hikvision", serialNumber = "HK-CC-2024-001", category = "Digital Devices", condition = "Working", notes = "Main Gate", purchaseDate = "2024-02-15"),
                Asset(assetName = "Ceiling Fan - Crompton", serialNumber = "CF-CR-2024-001", category = "Electrical", condition = "Needs Repair", notes = "Class 7A - makes noise", purchaseDate = "2022-06-10"),
                Asset(assetName = "Water Purifier - Kent", serialNumber = "WP-KT-2024-001", category = "Electrical", condition = "Working", notes = "Ground Floor Corridor", purchaseDate = "2024-03-15"),
                Asset(assetName = "NCERT Textbooks (Class 10)", serialNumber = "BK-NC10-2024-001", category = "Books", condition = "Working", notes = "Library - 50 copies", purchaseDate = "2024-04-20")
            ))
        }

        private suspend fun seedHealthLogs(dao: HealthLogDao) {
            dao.insertAll(listOf(
                HealthLog(assetId = 1, status = "Working", updatedDate = "2025-01-15"),
                HealthLog(assetId = 1, status = "Working", updatedDate = "2025-02-15"),
                HealthLog(assetId = 1, status = "Working", updatedDate = "2025-03-15"),
                HealthLog(assetId = 4, status = "Working", updatedDate = "2025-01-15"),
                HealthLog(assetId = 4, status = "Working", updatedDate = "2025-02-15"),
                HealthLog(assetId = 4, status = "Needs Repair", updatedDate = "2025-03-15"),
                HealthLog(assetId = 7, status = "Working", updatedDate = "2025-01-15"),
                HealthLog(assetId = 7, status = "Needs Repair", updatedDate = "2025-02-15"),
                HealthLog(assetId = 7, status = "Broken", updatedDate = "2025-03-15"),
                HealthLog(assetId = 14, status = "Working", updatedDate = "2025-01-15"),
                HealthLog(assetId = 14, status = "Needs Repair", updatedDate = "2025-03-15"),
                HealthLog(assetId = 17, status = "Working", updatedDate = "2025-01-15"),
                HealthLog(assetId = 17, status = "Broken", updatedDate = "2025-03-15")
            ))
        }

        private suspend fun seedIssueLogs(dao: IssueLogDao) {
            dao.insertAll(listOf(
                IssueLog(assetId = 4, issueDescription = "Battery drains within 30 minutes, needs replacement", issueDate = "2025-03-10", repairStatus = "Pending"),
                IssueLog(assetId = 7, issueDescription = "Projector lamp burnt out, displays no image", issueDate = "2025-03-01", repairStatus = "In Progress"),
                IssueLog(assetId = 10, issueDescription = "Microscope lens alignment is off, images are blurry", issueDate = "2025-02-20", repairStatus = "Pending"),
                IssueLog(assetId = 14, issueDescription = "Cricket bat handle grip is worn out", issueDate = "2025-03-05", repairStatus = "Completed"),
                IssueLog(assetId = 17, issueDescription = "Chair wheel broken, unstable seating", issueDate = "2025-02-28", repairStatus = "Pending"),
                IssueLog(assetId = 22, issueDescription = "Ceiling fan making loud rattling noise", issueDate = "2025-03-12", repairStatus = "In Progress")
            ))
        }
    }
}
