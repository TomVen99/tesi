package com.example.smartlagoon.ui.screens.login

import android.content.SharedPreferences
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.smartlagoon.ui.composables.PasswordTextField
import com.example.smartlagoon.R
import com.example.smartlagoon.ui.SmartlagoonRoute
import com.example.smartlagoon.ui.UsersViewModel



@Composable
fun Login(
    state: LoginState,
    actions: LoginActions,
    onSubmit: () -> Unit,
    navController: NavHostController,
    viewModel: UsersViewModel,
    sharedPreferences: SharedPreferences,
) {
    val signinResult by viewModel.loginResult.observeAsState()
    val signinLog by viewModel.loginLog.observeAsState()

    Box(modifier = Modifier
        .fillMaxSize()
        .wrapContentHeight(Alignment.CenterVertically))
    {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(10.dp)
                .background(MaterialTheme.colorScheme.background)
                .border(1.dp, MaterialTheme.colorScheme.onBackground, RectangleShape)
        ) {
            val focusManager = LocalFocusManager.current
            val usernameFocusRequester = remember { FocusRequester() }
            val passwordFocusRequester = remember { FocusRequester() }
            Image(
                painter = painterResource(id = R.drawable.smartlagoon_logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            )
            OutlinedTextField(
                value = state.username,
                onValueChange = actions::setUsername,
                label = { Text("Username") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .focusRequester(usernameFocusRequester),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { passwordFocusRequester.requestFocus() }
                )
            )

            var pwd by remember { mutableStateOf(state.password) }

            PasswordTextField(
                password = pwd,
                onPasswordChange = { newPassword -> pwd = newPassword },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .focusRequester(passwordFocusRequester),
                label = "Password",
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                loginActions = actions
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                if (signinResult == false) {
                    Text(signinLog.toString(), color = Color.Red)
                } else if (signinResult == true) {
                    navController.navigate(SmartlagoonRoute.Home.buildRoute(state.username))
                } else if (signinResult == null) {
                    Spacer(Modifier.size(15.dp))
                }
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                onClick = {
                    if (!state.canSubmit) return@Button
                    onSubmit()
                    val edit = sharedPreferences.edit()
                    edit.putBoolean("isUserLogged", true)
                    edit.putString("username",state.username)
                    edit.putString("password",state.password)
                    edit.apply()
                    //sharedPreferences.getString("username", "")?.let { Log.d("TAG", "dentro Login " + it) }
                },
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("Accedi")
            }
            Text(text = "Oppure")
            TextButton(
                onClick = {
                    navController.navigate(SmartlagoonRoute.Signin.route)
                },
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .padding(15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            ) {
                Text("Registrati ora")
            }
        }
    }
}
