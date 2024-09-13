package com.example.smartlagoon.ui.screens.signin

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.getValue
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.AlertDialog
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.example.smartlagoon.R
import com.example.smartlagoon.ui.SmartlagoonRoute
import com.example.smartlagoon.ui.composables.AnimatedImage
import com.example.smartlagoon.ui.theme.myButtonColors
import com.example.smartlagoon.ui.viewmodel.UsersDbViewModel

@Composable
fun SigninScreen(
    state: SigninState,
    actions: SigninActions,
    navController: NavHostController,
    viewModel: UsersDbViewModel
    ) {

    val signinLog by viewModel.signinLog.observeAsState()
    val signinResult by viewModel.signinResult.observeAsState()

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var password1 by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var isEnabled by remember { mutableStateOf(false) }
    var showMessage by remember { mutableStateOf(false) }
    var showAlertDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier
        .fillMaxSize()
        )
    {
        AnimatedImage(R.raw.sea_background)
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(10.dp)
                //.border(1.dp, MaterialTheme.colorScheme.onTertiaryContainer, RectangleShape)
        ) {

            val usernameFocusRequester = remember { FocusRequester() }
            val passwordFocusRequester = remember { FocusRequester() }
            val passwordFocusRequester1 = remember { FocusRequester() }
            val nameFocusRequester = remember { FocusRequester() }
            val surnameFocusRequester = remember { FocusRequester() }
            val mailFocusRequester = remember { FocusRequester() }

            Image(
                painter = painterResource(id = R.drawable.lagoonguard_logo_nosfondo),
                contentDescription = "Logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(2.dp)
            )
            /*Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MaterialTheme.colorScheme.onTertiaryContainer)
            )*/
            OutlinedTextField(
                value = firstName,
                onValueChange = {
                    firstName = it
                    actions.setFirstName(it)
                                },
                label = { Text("Nome") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 4.dp)
                    .focusRequester(nameFocusRequester),
                keyboardOptions = KeyboardOptions.Default.copy(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { surnameFocusRequester.requestFocus() }
                )
            )

            OutlinedTextField(
                value = lastName,
                onValueChange = {
                    lastName = it
                    actions.setSurname(it)
                                },
                label = { Text("Cognome") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 4.dp)
                    .focusRequester(surnameFocusRequester),
                keyboardOptions = KeyboardOptions.Default.copy(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { mailFocusRequester.requestFocus() }
                )
            )
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    actions.setMail(it)
                                },
                label = { Text("Mail") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 4.dp)
                    .focusRequester(mailFocusRequester),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { usernameFocusRequester.requestFocus() }
                )
            )
            OutlinedTextField(
                value = username,
                onValueChange = {
                    username = it
                    actions.setUsername(it)
                                },
                label = { Text("Username") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 4.dp)
                    .focusRequester(usernameFocusRequester),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { passwordFocusRequester.requestFocus() }
                )
            )
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    actions.setPassword(it)
                                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 4.dp)
                    .focusRequester(passwordFocusRequester),
                label = { Text("Password") },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { passwordFocusRequester1.requestFocus() }
                ),
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(Modifier.size(10.dp))
            OutlinedTextField(
                value = password1,
                onValueChange = { password1 = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 4.dp)
                    .focusRequester(passwordFocusRequester1),
                label = { Text("Ripeti password") },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(Modifier.size(10.dp))

            if(password == password1) {
                showMessage = false
                if (password.isNotEmpty() &&
                    firstName.isNotEmpty() &&
                    lastName.isNotEmpty() &&
                    email.isNotEmpty() &&
                    username.isNotEmpty()
                ) {
                    isEnabled = true
                }
            } else {
                showMessage = true
                message = "Password non coincidono"
            }

            if(showMessage) {
                Text(text = message,
                    color = Color.Red,
                    modifier = Modifier.padding(8.dp)
                )
                Log.d("pippo", "Message: $message")
            }

            Button(
                enabled = isEnabled,
                onClick = {
                    viewModel.register(email, password, firstName, lastName, username)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)) {
                Text("Registrati")
            }
            if (signinResult == false) {
                Text(signinLog.toString(), color = Color.Red)
            } else if (signinResult == true) {
                navController.navigate(SmartlagoonRoute.Login.route)
            }

            if(showAlertDialog) {
                AlertDialog(
                    onDismissRequest = { showAlertDialog = false },
                    title = {
                        Text(text = "Utente creato correttamente!")
                    },
                    text = {
                        Text(text = "Complimenti! Hai creato il tuo utente!")
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                showAlertDialog = false
                                navController.navigate(SmartlagoonRoute.Login.route)
                            },
                            colors = myButtonColors(),
                        ) {
                            Text("Vai al login")
                        }
                    }
                )
            }
        }
    }
}

