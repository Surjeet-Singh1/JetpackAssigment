

package com.example.lokalapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect // Added
import androidx.compose.runtime.collectAsState // Added
import androidx.compose.runtime.getValue     // Added
import androidx.compose.ui.Modifier          // Added
import androidx.compose.ui.platform.LocalContext
import com.example.lokalapp.ui.LoginScreen
import com.example.lokalapp.ui.OtpScreen
import com.example.lokalapp.ui.SessionScreen
import com.example.lokalapp.viewmodel.AuthState
import com.example.lokalapp.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val authState by authViewModel.authState.collectAsState()
                    val sessionDuration by authViewModel.sessionDuration.collectAsState(initial = 0L)

                    when (val state = authState) {
                        is AuthState.LoggedOut -> {
                            LoginScreen(
                                onSendOtp = { email ->
                                    authViewModel.sendOtp(email)
                                }
                            )
                        }
                        is AuthState.OtpSent -> {
                             val context = LocalContext.current
                            // For Demo purposes: Show OTP in Toast since no email is sent
                            // We need to fetch it from a side effect or just trust the user looks at logs?
                            // To be helpful, let's look at the logs. But capturing the OTP here from state is hard unless state has it.
                            // Cleaner: Just show a toast saying "Check Logcat for OTP"
                             LaunchedEffect(Unit) {
                                 Toast.makeText(context, "OTP sent! Check Logcat (Search 'OTP_DEBUG')", Toast.LENGTH_LONG).show()
                             }

                            OtpScreen(
                                email = state.email,
                                error = state.error,
                                onVerifyOtp = { otp ->
                                    authViewModel.verifyOtp(state.email, otp)
                                },
                                onResendOtp = {
                                    authViewModel.resendOtp(state.email)
                                }
                            )
                        }
                        is AuthState.LoggedIn -> {
                            SessionScreen(
                                email = state.email,
                                sessionStartTime = state.sessionStartTime,
                                sessionDuration = sessionDuration,
                                onLogout = {
                                    authViewModel.logout()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
