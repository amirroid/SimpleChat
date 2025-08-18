package ir.amirroid.simplechat.features.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.amirroid.simplechat.data.models.response.NetworkErrors
import ir.amirroid.simplechat.data.repository.user.UserRepository
import ir.amirroid.simplechat.data.response.Response
import ir.amirroid.simplechat.models.DefaultRequiredResponse
import ir.amirroid.simplechat.models.token.Token
import ir.amirroid.simplechat.models.user.RegisterUserBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _registerResponse =
        MutableStateFlow<Response<DefaultRequiredResponse<Token>, NetworkErrors>>(Response.Idle)
    val registerResponse = _registerResponse.asStateFlow()

    var userId by mutableStateOf("")
    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var isSignInMethod by mutableStateOf(true)

    fun clearResponse() {
        _registerResponse.update { Response.Idle }
    }

    fun login() = viewModelScope.launch(Dispatchers.IO) {
        _registerResponse.update { Response.Idle }
        val newResponse = userRepository.login(generateBody())
        _registerResponse.update { newResponse }
    }

    fun signup() = viewModelScope.launch(Dispatchers.IO) {
        _registerResponse.update { Response.Idle }
        val newResponse = userRepository.signup(generateBody())
        _registerResponse.update { newResponse }
    }

    fun saveToken(token: Token) = viewModelScope.launch(Dispatchers.IO) {
        userRepository.saveToken(token)
    }

    private fun generateBody() = RegisterUserBody(
        userId = userId,
        username = username,
        password = password
    )

}