package com.example.outdoorromagna.ui.composables

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.example.outdoorromagna.ui.screens.login.LoginActions
import com.example.outdoorromagna.ui.screens.signin.SigninActions

@Composable
fun PasswordTextField(
    password: String,
    onPasswordChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Password",
    keyboardOptions: KeyboardOptions,
    signinActions: SigninActions? = null,
    loginActions: LoginActions? = null,
) {
    signinActions?.setPassword(password)
    loginActions?.setPassword(password)
    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        modifier = modifier,
        label = { Text(label) },
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Password,
            imeAction = keyboardOptions.imeAction
        ),
    )
}