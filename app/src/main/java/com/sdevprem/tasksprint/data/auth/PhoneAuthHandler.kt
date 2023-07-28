package com.sdevprem.tasksprint.data.auth

import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhoneAuthHandler @Inject constructor(
    private val auth: FirebaseAuth
) {

    private var verificationId: String? = null
    private val _verificationState = MutableStateFlow<PhoneAuthState>(PhoneAuthState.Idle)
    val verificationState = _verificationState.asStateFlow()

    val isUserSignedIn
        get() = auth.currentUser != null

    private val callback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            _verificationState.value = PhoneAuthState.InvalidNumber(e)
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            this@PhoneAuthHandler.verificationId = verificationId
            _verificationState.value = PhoneAuthState.VerificationCodeSent
        }

    }

    val basePhoneAuthOptionsBuilder: PhoneAuthOptions.Builder
        get() = PhoneAuthOptions.Builder(auth)
            .setCallbacks(callback)


    fun sendVerificationCode(options: PhoneAuthOptions) {
        _verificationState.value = PhoneAuthState.SendingCode
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun verifyCode(code: String) {
        verificationId?.let {
            signInWithPhoneAuthCredential(
                PhoneAuthProvider.getCredential(it, code)
            )
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        _verificationState.value = PhoneAuthState.VerificationInProgress
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Timber.d("signInWithCredential:success")
                    _verificationState.value = PhoneAuthState.SignedInSuccess(
                        task.result.user!!.uid
                    )
                    verificationId = null
                } else {
                    Timber.w("signInWithCredential:failure", task.exception)
                    _verificationState.value = PhoneAuthState.InvalidCode(
                        task.exception ?: FirebaseException("Verification Failed")
                    )
                }
            }
    }
}