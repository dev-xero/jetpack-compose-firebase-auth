package dev.xero.jetpackcomposefirebaseauth.presentation.signin

data class SignInState(
	val isSignInSuccessful: Boolean = false,
	val signInErrorMsg: String? = null
)
