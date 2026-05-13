package com.example.nammashaleinventoryeducation.data.repository

import com.example.nammashaleinventoryeducation.data.dao.TeacherDao
import com.example.nammashaleinventoryeducation.data.entity.Teacher

class TeacherRepository(private val teacherDao: TeacherDao) {

    suspend fun login(email: String, password: String): Teacher? {
        return teacherDao.getTeacherByEmailAndPassword(email, password)
    }

    suspend fun insertTeacher(teacher: Teacher) {
        teacherDao.insertTeacher(teacher)
    }

    suspend fun getAllTeachers(): List<Teacher> {
        return teacherDao.getAllTeachers()
    }
}
