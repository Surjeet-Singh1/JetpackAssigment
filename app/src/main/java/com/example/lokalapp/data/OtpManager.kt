package com.example.lokalapp.data

import kotlin.random.Random

data class OtpData(
    val otp: String,
    val timestamp: Long,
    var attempts: Int = 0
)

object OtpManager {
    private val otpStorage = mutableMapOf<String, OtpData>()
    private const val OTP_EXPIRY_MS = 60 * 1000L // 60 seconds
    private const val MAX_ATTEMPTS = 3

    fun generateOtp(email: String): String {
        val otp = String.format("%06d", Random.nextInt(0, 999999))
        otpStorage[email] = OtpData(otp, System.currentTimeMillis())
        return otp
    }

    /**
     * @return null if valid, error message otherwise
     */
    fun validateOtp(email: String, inputOtp: String): String? {
        val data = otpStorage[email] ?: return "No OTP generated for this email"

        if (System.currentTimeMillis() - data.timestamp > OTP_EXPIRY_MS) {
            otpStorage.remove(email)
            return "OTP Expired"
        }

        if (data.attempts >= MAX_ATTEMPTS) {
            otpStorage.remove(email)
            return "Max attempts exceeded. Please request a new OTP."
        }

        if (data.otp != inputOtp) {
            data.attempts++
            return "Incorrect OTP. Attempts left: ${MAX_ATTEMPTS - data.attempts}"
        }

        // Success
        otpStorage.remove(email)
        return null
    }
    
    fun clearOtp(email: String) {
        otpStorage.remove(email)
    }
}
