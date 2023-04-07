package dev.xero.jetpackcomposefirebaseauth.presentation.signin

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SignInViewModel : ViewModel() {
	private val _state = MutableStateFlow(SignInState())
	val state = _state.asStateFlow()

	fun onSignInResult(result: SignInResult) {
		_state.update { it.copy(
			isSignInSuccessful = result.data != null,
			signInErrorMsg = result.errorMsg
		) }
	}

	fun resetState() {
		_state.update { SignInState() }
	}
}