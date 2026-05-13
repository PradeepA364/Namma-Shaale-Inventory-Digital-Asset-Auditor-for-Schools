package com.example.nammashaleinventoryeducation.data.dao

import androidx.room.*
import com.example.nammashaleinventoryeducation.data.entity.Teacher

@Dao
interface TeacherDao {
    @Query("SELECT * FROM teachers WHERE email = :email AND password = :password LIMIT 1")
    suspend fun getTeacherByEmailAndPassword(email: String, password: String): Teacher?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeacher(teacher: Teacher)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(teachers: List<Teacher>)

    @Query("SELECT * FROM teachers")
    suspend fun getAllTeachers(): List<Teacher>
}
