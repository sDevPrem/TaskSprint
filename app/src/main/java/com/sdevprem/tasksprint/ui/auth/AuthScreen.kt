package com.sdevprem.tasksprint.ui.auth

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.sdevprem.tasksprint.R
import com.sdevprem.tasksprint.ui.navigation.Destination
import java.util.concurrent.TimeUnit

@Composable
fun AuthScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    navController: NavController
) {

    val context = LocalContext.current
    val authUiState by viewModel.authUiState.collectAsStateWithLifecycle()

    AuthScreenContent(
        authUiState = authUiState,
        sendCode = {
            viewModel
                .sendVerificationCode(
                    viewModel.basePhoneAuthOptionsBuilder
                        .setActivity(context as Activity)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setPhoneNumber(it)
                        .build()
                )
        },
        verifyCode = viewModel::verifyCode,
        onSignedInSuccess = {
            navController.navigate(Destination.HomeScreen.route) {
                popUpTo(Destination.AuthScreen.route) {
                    inclusive = true
                }
            }
        }
    )
}

@Composable
private fun AuthScreenContent(
    authUiState: AuthUiState,
    sendCode: (String) -> Unit,
    verifyCode: (String) -> Unit,
    onSignedInSuccess: () -> Unit
) {
    if (authUiState.isSignedInSuccess) {
        Toast.makeText(
            LocalContext.current,
            "Signed In Success",
            Toast.LENGTH_SHORT
        )
            .show()
        onSignedInSuccess()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        HeroSection()
        Spacer(modifier = Modifier.size(40.dp))
        AuthSection(
            modifier = Modifier.weight(1f),
            authUiState = authUiState,
            sendCode = sendCode,
            verifyCode = verifyCode
        )
    }

}

@Composable
private fun AuthSection(
    modifier: Modifier = Modifier,
    authUiState: AuthUiState,
    sendCode: (String) -> Unit,
    verifyCode: (String) -> Unit,
) {
    var number by rememberSaveable { mutableStateOf("") }
    var otp by rememberSaveable { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OutlinedTextField(
            value = if (authUiState.numberVerificationMode) number else otp,
            onValueChange = {
                if (authUiState.numberVerificationMode)
                    number = it
                else
                    otp = it
            },
            readOnly = authUiState.isLoading,
            placeholder = {
                Text(
                    text = if (authUiState.numberVerificationMode)
                        "Enter your number"
                    else "Enter the otp"
                )
            },
            singleLine = true,
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done,
            ),
            prefix = {
                if (authUiState.numberVerificationMode)
                    Text("+91")
            }
        )

        Spacer(modifier = Modifier.size(24.dp))

        if (authUiState.isLoading) {
            LoadingDialog(msg = if (authUiState.numberVerificationMode) "Sending code" else "Verifying code")
        }

        authUiState.errorMsg?.let {
            Text(
                text = it,
                modifier = Modifier.padding(bottom = 16.dp),
                color = MaterialTheme.colorScheme.error
            )
        }

        Button(
            onClick = {
                focusManager.clearFocus()
                if (authUiState.numberVerificationMode && number.isNotBlank())
                    sendCode("+91$number")
                else if (authUiState.codeVerificationMode && otp.isNotBlank())
                    verifyCode(otp)
            },
            enabled = !authUiState.isLoading
        ) {
            Text(
                text = if (authUiState.numberVerificationMode)
                    "Send OTP"
                else "Verify"
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun HeroSection(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.team_up),
            contentDescription = null,
            modifier = Modifier
                .size(150.dp)
        )

        Spacer(modifier = Modifier.size(16.dp))
        Text(
            text = "Let's start organizing task together",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
        )

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoadingDialog(
    msg: String
) {
    AlertDialog(
        onDismissRequest = {},

        ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surface
                )
                .padding(24.dp)
        ) {
            Text(text = msg)
            Spacer(modifier = Modifier.size(16.dp))
            CircularProgressIndicator()
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun AuthScreenPreview() {
    AuthScreenContent(
        authUiState = AuthUiState(
            numberVerificationMode = true,
            codeVerificationMode = false,
            errorMsg = null,
            isLoading = false,
            isSignedInSuccess = false
        ),
        {},
        {},
        {}
    )
}