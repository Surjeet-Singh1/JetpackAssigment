package com.example.lokalapp.analytics

import timber.log.Timber

object AnalyticsLogger {
    fun logOtpGenerated(email: String, otp: String) {
        Timber.d("Event: OTP Generated for email: $email. OTP: $otp")
        Timber.tag("OTP_DEBUG").d("YOUR OTP IS: $otp") // Easier to find
    }

    fun logOtpValidationSuccess(email: String) {
        Timber.d("Event: OTP Validation Success for email: $email")
    }

    fun logOtpValidationFailure(email: String, reason: String) {
        Timber.e("Event: OTP Validation Failure for email: $email. Reason: $reason")
    }

    fun logLogout(email: String) {
        Timber.d("Event: Logout for email: $email")
    }
}
