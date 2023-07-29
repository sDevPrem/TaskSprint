package com.sdevprem.tasksprint.core.auth

sealed interface PhoneAuthState {
    object Idle : PhoneAuthState
    class InvalidNumber(val exception: Exception) : PhoneAuthState
    object SendingCode : PhoneAuthState
    object VerificationCodeSent : PhoneAuthState
    object VerificationInProgress : PhoneAuthState
    class InvalidCode(val exception: Exception) : PhoneAuthState
    class SignedInSuccess(val uid: String) : PhoneAuthState
}