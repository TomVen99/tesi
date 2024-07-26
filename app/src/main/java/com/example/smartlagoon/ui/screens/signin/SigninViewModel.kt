package com.example.smartlagoon.ui.screens.signin

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class SigninState(
    val username: String = "",
    val password: String = "",
    val name: String = "",
    val surname: String = "",
    val mail: String = "",
    val salt: ByteArray = ByteArray(800)
) {
    val canSubmit get() =
        username.isNotBlank() &&
            password.isNotBlank() &&
                name.isNotBlank() &&
                surname.isNotBlank() &&
                mail.isNotBlank()


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SigninState

        if (username != other.username) return false
        if (password != other.password) return false
        if (name != other.name) return false
        if (surname != other.surname) return false
        if (mail != other.mail) return false
        return salt.contentEquals(other.salt)
    }

    override fun hashCode(): Int {
        var result = username.hashCode()
        result = 31 * result + password.hashCode()
        result = 31 * result + salt.contentHashCode()
        return result
    }
}

interface SigninActions {
    fun setUsername(username: String)
    fun setPassword(password: String)
    fun setFirstName(firstName: String)
    fun setSurname(surname: String)
    fun setMail(mail: String)
    fun setSalt(byteArray: ByteArray)
}

class SigninViewModel : ViewModel() {
    private val _state = MutableStateFlow(SigninState())
    val state = _state.asStateFlow()

    val actions = object : SigninActions {
        override fun setUsername(username: String) =
            _state.update { it.copy(username = username) }

        override fun setPassword(password: String) =
            _state.update { it.copy(password = password) }

        override fun setFirstName(firstName: String) =
            _state.update { it.copy(name = firstName) }


        override fun setSurname(surname: String) =
            _state.update { it.copy(surname = surname) }


        override fun setMail(mail: String) =
            _state.update { it.copy(mail = mail) }


        override fun setSalt(byteArray: ByteArray) {
            _state.update { it.copy(salt = byteArray) }
        }
    }
}