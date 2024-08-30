package com.example.smartlagoon.ui.screens.login

import android.content.SharedPreferences
import android.util.Log
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
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.smartlagoon.R
import com.example.smartlagoon.ui.SmartlagoonRoute
import com.example.smartlagoon.ui.viewmodel.UsersDbViewModel


@Composable
fun Login(
    navController: NavHostController,
    //viewModel: UsersViewModel,
    viewModel: UsersDbViewModel,
    sharedPreferences: SharedPreferences,
) {
    Log.d("LoginScreen", "dentro login screen")
    val loginResult by viewModel.loginResult.observeAsState()
    val loginLog by viewModel.loginLog.observeAsState()
    var mail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isEnabled by remember { mutableStateOf(false) }

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
                .border(1.dp, MaterialTheme.colorScheme.onTertiaryContainer, RectangleShape)
        ) {
            Image(
                painter = painterResource(id = R.drawable.lagoonguard_logo_nosfondo),//smartlagoon_logo_nosfondo),
                contentDescription = "Logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(2.dp)
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)  // Altezza del tuo spacer
                    .background(MaterialTheme.colorScheme.onTertiaryContainer)  // Colore del tuo spacer
            )
            val mailFocusRequester = remember { FocusRequester() }
            val passwordFocusRequester = remember { FocusRequester() }

            OutlinedTextField(
                value = mail,
                onValueChange = { mail = it },
                label = { Text("Mail") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .focusRequester(mailFocusRequester),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { passwordFocusRequester.requestFocus() }
                )
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .focusRequester(passwordFocusRequester),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                visualTransformation = PasswordVisualTransformation()
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                if (loginResult == false) {
                    Text(loginLog.toString(), color = Color.Red)
                } else if (loginResult == true) {
                    navController.navigate(SmartlagoonRoute.Home.route)//.buildRoute(state.username))
                } else if (loginResult == null) {
                    Spacer(Modifier.size(15.dp))
                }
            }

            if(mail.isNotEmpty() && password.isNotEmpty()) {
                isEnabled = true
            }
            Button(
                enabled = isEnabled,
                onClick = {
                    viewModel.login(mail, password, sharedPreferences)
                    navController.navigate(SmartlagoonRoute.Home.route)
                    Log.d("login", "vado in home")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer)
                ) {
                Text("Login")
            }

            /*// Controlla il risultato del login
            if (loginResult == false) {
                Text(loginLog.toString(), color = Color.Red)
            } else if (loginResult == true) {
                navController.navigate(SmartlagoonRoute.Home.route)//buildRoute(state.username))
            } else if (loginResult == null) {
                Spacer(Modifier.size(15.dp))
            }*/
            Spacer(Modifier.size(10.dp))
            Text(text = "Oppure")
            Spacer(Modifier.size(10.dp))

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
