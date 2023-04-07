package dev.xero.jetpackcomposefirebaseauth.presentation.signin

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dev.xero.jetpackcomposefirebaseauth.R
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException

class GoogleAuthUIClient (
	private val context: Context,
	private val oneTapClient: SignInClient
) {
	private val auth = Firebase.auth

	suspend fun signIn(): IntentSender? {
		val result = try {
			oneTapClient.beginSignIn(
				buildSignInRequest()
			).await()
		} catch (e: Exception) {
			e.printStackTrace()
			if (e is CancellationException) throw e
			null
		}
		return result?.pendingIntent?.intentSender
	}

	private fun buildSignInRequest(): BeginSignInRequest {
		return BeginSignInRequest.Builder()
			.setGoogleIdTokenRequestOptions(
				GoogleIdTokenRequestOptions.builder()
					.setSupported(true)
					.setFilterByAuthorizedAccounts(false)
					.setServerClientId(context.getString(R.string.default_web_client_id))
					.build()
			)
			.setAutoSelectEnabled(true)
			.build()
	}
}