package com.example.lokalapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lokalapp.analytics.AnalyticsLogger
import com.example.lokalapp.data.OtpManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.LoggedOut)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    // Exposed for session timer
    val sessionDuration = flow {
        while (true) {
            val state = _authState.value
            if (state is AuthState.LoggedIn) {
                val duration = System.currentTimeMillis() - state.sessionStartTime
                emit(duration)
            } else {
                emit(0L)
            }
            delay(1000)
        }
    }

    fun sendOtp(email: String) {
        if (email.isBlank()) return // Simple validation
        val otp = OtpManager.generateOtp(email)
        AnalyticsLogger.logOtpGenerated(email, otp)
        // In a real app, we would send the OTP via network. Here we just log it/simulate sending.
        // For testing convenience, you might want to log it to show in console.
        println("DEBUG: OTP for $email is $otp") 
        _authState.value = AuthState.OtpSent(email)
    }

    fun verifyOtp(email: String, otp: String) {
        val error = OtpManager.validateOtp(email, otp)
        if (error == null) {
            AnalyticsLogger.logOtpValidationSuccess(email)
            _authState.value = AuthState.LoggedIn(email, System.currentTimeMillis())
        } else {
            AnalyticsLogger.logOtpValidationFailure(email, error)
            _authState.value = AuthState.OtpSent(email, error = error)
        }
    }

    fun resendOtp(email: String) {
         sendOtp(email)
    }

    fun logout() {
        val state = _authState.value
        if (state is AuthState.LoggedIn) {
            AnalyticsLogger.logLogout(state.email)
        }
        _authState.value = AuthState.LoggedOut
    }
}
