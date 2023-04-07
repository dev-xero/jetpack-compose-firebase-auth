package dev.xero.jetpackcomposefirebaseauth.presentation.signin

data class SignInResult(
	val data: UserData?,
	val errorMsg: String? = null
)

data class UserData (
	val userID: String,
	val username: String?,
	val profilePictureURL: String?
)
