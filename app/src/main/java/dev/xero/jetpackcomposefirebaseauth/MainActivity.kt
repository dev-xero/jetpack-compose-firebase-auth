package dev.xero.jetpackcomposefirebaseauth

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.identity.Identity
import dev.xero.jetpackcomposefirebaseauth.presentation.signin.GoogleAuthUIClient
import dev.xero.jetpackcomposefirebaseauth.presentation.signin.SignInScreen
import dev.xero.jetpackcomposefirebaseauth.presentation.signin.SignInViewModel
import dev.xero.jetpackcomposefirebaseauth.ui.theme.JetpackComposeFirebaseAuthTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
	private val googleAuthUIClient by lazy {
		GoogleAuthUIClient(applicationContext, Identity.getSignInClient(applicationContext))
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			JetpackComposeFirebaseAuthTheme {
				// A surface container using the 'background' color from the theme
				Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
					val navController = rememberNavController()
					NavHost(
						navController = navController,
						startDestination = "sign_in"
					) {
						composable("sign_in") {
							val viewModel = viewModel<SignInViewModel>()
							val state by viewModel.state.collectAsStateWithLifecycle()

							val launcher = rememberLauncherForActivityResult(
								contract = ActivityResultContracts.StartIntentSenderForResult(),
								onResult = { result ->
									if (result.resultCode == RESULT_OK) {
										lifecycleScope.launch {
											val signInResult = googleAuthUIClient.signInWithIntent(
												intent = result.data ?: return@launch
											)
											viewModel.onSignInResult(signInResult)
										}
									}
								}
							)
							
							LaunchedEffect(key1 = state.isSignInSuccessful) {
								if (state.isSignInSuccessful) {
									Toast.makeText(
										applicationContext,
										"SIGN IN SUCCESSFUL",
										Toast.LENGTH_LONG
									).show()
								}
							}

							SignInScreen(
								state = state,
								onSignInClick = {
									lifecycleScope.launch {
										val signInIntentSender = googleAuthUIClient.signIn()
										launcher.launch(
											IntentSenderRequest.Builder(signInIntentSender ?: return@launch).build()
										)
									}
								}
							)
						}
					}
				}
			}
		}
	}
}
