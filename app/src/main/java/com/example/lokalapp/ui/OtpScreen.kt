package com.example.lokalapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun OtpScreen(
    email: String,
    error: String?,
    onVerifyOtp: (String) -> Unit,
    onResendOtp: () -> Unit
) {
    var otp by remember { mutableStateOf("") }
    
    // Local timer state for visual countdown (60 seconds)
    var timeLeft by remember { mutableIntStateOf(60) }
    var canResend by remember { mutableStateOf(false) }

    // LaunchedEffect to handle the countdown
    LaunchedEffect(key1 = canResend) {
        if (!canResend) {
            timeLeft = 60
            while (timeLeft > 0) {
                delay(1000L)
                timeLeft--
            }
            canResend = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Enter OTP",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Sent to $email",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = otp,
            onValueChange = { 
                if (it.length <= 6 && it.all { char -> char.isDigit() }) {
                    otp = it
                }
            },
            label = { Text("6-Digit OTP") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            modifier = Modifier.fillMaxWidth()
        )

        if (error != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onVerifyOtp(otp) },
            enabled = otp.length == 6,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Verify OTP")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = {
                onResendOtp()
                canResend = false // Restart timer
            },
            enabled = canResend
        ) {
            if (canResend) {
                Text("Resend OTP")
            } else {
                Text("Resend OTP in ${timeLeft}s")
            }
        }
    }
}
