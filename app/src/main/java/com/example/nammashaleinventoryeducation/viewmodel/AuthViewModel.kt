package com.example.nammashaleinventoryeducation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.nammashaleinventoryeducation.data.database.AppDatabase
import com.example.nammashaleinventoryeducation.data.repository.TeacherRepository
import com.example.nammashaleinventoryeducation.utils.Constants
import com.example.nammashaleinventoryeducation.utils.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
    val selectedRole: String = Constants.ROLE_TEACHER
)

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TeacherRepository
    val sessionManager: SessionManager

    init {
        val db = AppDatabase.getDatabase(application)
        repository = TeacherRepository(db.teacherDao())
        sessionManager = SessionManager(application)
    }

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun setRole(role: String) {
        _uiState.value = _uiState.value.copy(selectedRole = role, error = null)
    }

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Please fill in all fields")
            return
        }

        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                if (_uiState.value.selectedRole == Constants.ROLE_ADMIN) {
                    if (email == Constants.ADMIN_EMAIL && password == Constants.ADMIN_PASSWORD) {
                        sessionManager.saveSession(0, Constants.ADMIN_NAME, email, Constants.ROLE_ADMIN)
                        _uiState.value = _uiState.value.copy(isLoading = false, isSuccess = true)
                    } else {
                        _uiState.value = _uiState.value.copy(isLoading = false, error = "Invalid admin credentials")
                    }
                } else {
                    // For demo purposes: any non-empty teacher login succeeds
                    // First try to find in DB to get real name if possible, otherwise use input
                    val teacher = repository.login(email, password)
                    val name = teacher?.name ?: email.substringBefore("@")
                    val id = teacher?.id ?: -1
                    
                    sessionManager.saveSession(id, name, email, Constants.ROLE_TEACHER)
                    _uiState.value = _uiState.value.copy(isLoading = false, isSuccess = true)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = "Login failed: ${e.message}")
            }
        }
    }

    fun logout() {
        sessionManager.clearSession()
        _uiState.value = LoginUiState()
    }

    fun resetState() {
        _uiState.value = LoginUiState()
    }
}
