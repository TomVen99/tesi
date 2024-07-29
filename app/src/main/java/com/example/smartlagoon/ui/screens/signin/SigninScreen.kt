package com.example.smartlagoon.ui.screens.signin

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DoneOutline
import androidx.compose.runtime.getValue
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import com.example.smartlagoon.ui.composables.PasswordTextField
import com.example.smartlagoon.R
import com.example.smartlagoon.data.database.User
import com.example.smartlagoon.ui.SmartlagoonRoute
import com.example.smartlagoon.ui.viewmodel.UsersViewModel

@Composable
fun SigninScreen(
    state: SigninState,
    actions: SigninActions,
    onSubmit: (User) -> Unit,
    navController: NavHostController,
    viewModel: UsersViewModel
    ) {
        val signinResult by viewModel.signinResult.observeAsState()
        val signinLog by viewModel.signinLog.observeAsState()

        Box(modifier = Modifier
            .fillMaxSize()
            .wrapContentHeight(Alignment.CenterVertically))
        {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(10.dp)
                    .border(1.dp, MaterialTheme.colorScheme.onBackground, RectangleShape)
            ) {

                val focusManager = LocalFocusManager.current
                val usernameFocusRequester = remember { FocusRequester() }
                val passwordFocusRequester = remember { FocusRequester() }
                val nameFocusRequester = remember { FocusRequester() }
                val surnameFocusRequester = remember { FocusRequester() }
                val mailFocusRequester = remember { FocusRequester() }
                val buttonFocusRequester = remember { FocusRequester() }


                Image(
                    painter = painterResource(id = R.drawable.smartlagoon_logo),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                )
                OutlinedTextField(
                    value = state.name,
                    onValueChange = actions::setFirstName,
                    label = { Text("Nome") },
                    modifier = Modifier.fillMaxWidth()
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
                    value = state.surname,
                    onValueChange = actions::setSurname,
                    label = { Text("Cognome") },
                    modifier = Modifier.fillMaxWidth()
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
                    value = state.mail,
                    onValueChange = actions::setMail,
                    label = { Text("Mail") },
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                        .focusRequester(mailFocusRequester),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = { usernameFocusRequester.requestFocus() }
                    )
                )
                OutlinedTextField(
                    value = state.username,
                    onValueChange = actions::setUsername,
                    label = { Text("Username") },
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                        .focusRequester(usernameFocusRequester),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = { passwordFocusRequester.requestFocus() }
                    )
                )
                var pwd by remember { mutableStateOf(state.password) }
                PasswordTextField(
                    password = pwd,
                    onPasswordChange = { newPassword -> pwd = newPassword},
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                        .focusRequester(passwordFocusRequester),
                    label = "Password",
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    signinActions = actions)
                Spacer(Modifier.size(10.dp))
                Button(
                    onClick = {
                        if (!state.canSubmit) return@Button
                        val salt = viewModel.generateSalt()
                        val password = viewModel.hashPassword(state.password, salt)
                        onSubmit(
                            User(
                            username = state.username,
                            password = password,
                                salt = salt,
                                urlProfilePicture = "",
                                name = state.name,
                                surname = state.surname,
                                mail = state.mail
                            ))
                    },
                    contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    modifier = Modifier.align(Alignment.End)
                        .padding(end = 15.dp, bottom = 15.dp)
                        .focusRequester(buttonFocusRequester)
                ) {
                    Icon(
                        Icons.Outlined.DoneOutline,
                        contentDescription = "done icon",
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Registrati")
                }
                if (signinResult == false) {
                    Text(signinLog.toString(), color = Color.Red)
                } else if (signinResult == true) {
                    navController.navigate(SmartlagoonRoute.Login.route)
                }
            }
        }
}

