package com.example.juejin.lite.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.juejin.lite.data.local.UserPreferences
import com.example.juejin.lite.domain.model.LoginResult
import com.example.juejin.lite.domain.usecase.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val isLoading: Boolean = false,
    val loginResult: LoginResult? = null,
    val errorMessage: String? = null
)

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()
    
    fun login(userName: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            
            loginUseCase(userName, password)
                .onSuccess { result ->
                    // 保存用户信息
                    UserPreferences.saveUser(result)
                    
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        loginResult = result,
                        errorMessage = null
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        loginResult = null,
                        errorMessage = error.message ?: "登录失败"
                    )
                }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}
