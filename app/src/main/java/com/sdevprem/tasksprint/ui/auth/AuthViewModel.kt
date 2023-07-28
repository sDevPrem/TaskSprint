package com.sdevprem.tasksprint.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.PhoneAuthOptions
import com.sdevprem.tasksprint.data.auth.PhoneAuthHandler
import com.sdevprem.tasksprint.data.auth.PhoneAuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val phoneAuthHandler: PhoneAuthHandler
) : ViewModel() {
    val authUiState = phoneAuthHandler.verificationState.map {
        AuthUiState(
            numberVerificationMode = (it is PhoneAuthState.Idle || it is PhoneAuthState.InvalidNumber || it is PhoneAuthState.SendingCode),
            codeVerificationMode = (it is PhoneAuthState.VerificationCodeSent || it is PhoneAuthState.InvalidCode || it is PhoneAuthState.VerificationCodeSent),
            errorMsg = when (it) {
                is PhoneAuthState.InvalidCode -> it.exception.localizedMessage
                is PhoneAuthState.InvalidNumber -> it.exception.localizedMessage
                else -> null
            },
            isLoading = it is PhoneAuthState.SendingCode || it is PhoneAuthState.VerificationInProgress,
            isSignedInSuccess = it is PhoneAuthState.SignedInSuccess
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        AuthUiState()
    )

    val basePhoneAuthOptionsBuilder: PhoneAuthOptions.Builder
        get() = phoneAuthHandler.basePhoneAuthOptionsBuilder

    fun sendVerificationCode(options: PhoneAuthOptions) =
        phoneAuthHandler.sendVerificationCode(options)

    fun verifyCode(code: String) = phoneAuthHandler.verifyCode(code)
}