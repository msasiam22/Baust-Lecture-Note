package com.example.baustlecturenote.presentation.auth


import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.baustlecturenote.domain.model.User
import com.example.baustlecturenote.domain.repository.Repository
import com.example.baustlecturenote.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: Repository,
    private val pref: SharedPreferences
) : ViewModel() {
    private val _loginState = MutableStateFlow<Resource<User>?>(null)
    val loginState: StateFlow<Resource<User>?> = _loginState

    private val _signupState = MutableStateFlow<Resource<User>?>(null)
    val signupState: StateFlow<Resource<User>?> = _signupState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            repository.login(email, password).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        println("Login loading: ${resource.isLoading}")
                        _loginState.value = resource
                    }
                    is Resource.Success -> {
                        println("Login successful: User(id=${resource.data?.id}, role=${resource.data?.role}, token=${resource.data?.token})")
                        _loginState.value = resource
                        pref.edit().putString("token", resource.data?.token).putString("role", resource.data?.role).apply()
                    }
                    is Resource.Error -> {
                        println("Login failed: ${resource.message}")
                        _loginState.value = resource
                    }
                }
            }
        }
    }

    fun signup(email: String, password: String) {
        viewModelScope.launch {
            repository.signup(email, password).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        println("Signup loading: ${resource.isLoading}")
                        _signupState.value = resource
                    }
                    is Resource.Success -> {
                        println("Signup successful: User(id=${resource.data?.id}, role=${resource.data?.role}, token=${resource.data?.token})")
                        _signupState.value = resource
                    }
                    is Resource.Error -> {
                        println("Signup failed: ${resource.message}")
                        _signupState.value = resource
                    }
                }
            }
        }
    }
}