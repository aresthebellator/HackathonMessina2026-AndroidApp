package com.exertia.wikingo.presentation.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.exertia.wikingo.R
import com.exertia.wikingo.data.auth.FirebaseAuthRepository
import com.exertia.wikingo.core.i18n.i18n
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(
    repository: FirebaseAuthRepository,
    onAuthenticated: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isRegistering by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var isGoogleLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val googleClientId = remember(context) {
        val resourceId = context.resources.getIdentifier(
            "default_web_client_id",
            "string",
            context.packageName
        )
        if (resourceId == 0) null else context.getString(resourceId)
    }
    val invalidCredentialsMessage = i18n("auth.invalid_credentials")
    val genericErrorMessage = i18n("auth.generic_error")
    val googleNotConfiguredMessage = i18n("auth.google_not_configured")

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.ic_launcher_viking),
                contentDescription = i18n("auth.mascot_description"),
                modifier = Modifier.size(112.dp)
            )
            Spacer(Modifier.height(18.dp))
            Text(
                text = i18n(if (isRegistering) "auth.register_title" else "auth.login_title"),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = i18n("auth.subtitle"),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(22.dp))

            OutlinedButton(
                onClick = {
                    val clientId = googleClientId
                    if (clientId.isNullOrBlank()) {
                        errorMessage = googleNotConfiguredMessage
                        return@OutlinedButton
                    }
                    scope.launch {
                        isLoading = true
                        isGoogleLoading = true
                        errorMessage = null
                        try {
                            val googleIdOption = GetGoogleIdOption.Builder()
                                .setFilterByAuthorizedAccounts(false)
                                .setServerClientId(clientId)
                                .setAutoSelectEnabled(false)
                                .build()
                            val request = GetCredentialRequest.Builder()
                                .addCredentialOption(googleIdOption)
                                .build()
                            val result = CredentialManager.create(context)
                                .getCredential(context, request)
                            val credential = result.credential
                            if (
                                credential !is CustomCredential ||
                                credential.type != GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                            ) {
                                errorMessage = genericErrorMessage
                            } else {
                                val googleCredential =
                                    GoogleIdTokenCredential.createFrom(credential.data)
                                repository.signInWithGoogle(googleCredential.idToken)
                                onAuthenticated()
                            }
                        } catch (_: GetCredentialCancellationException) {
                            errorMessage = null
                        } catch (exception: CancellationException) {
                            throw exception
                        } catch (exception: Exception) {
                            errorMessage = exception.message ?: genericErrorMessage
                        } finally {
                            isLoading = false
                            isGoogleLoading = false
                        }
                    }
                },
                enabled = !isLoading && repository.isConfigured,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                if (isGoogleLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(22.dp))
                } else {
                    Text("G", color = Color(0xFF4285F4), fontWeight = FontWeight.Black)
                    Spacer(Modifier.size(10.dp))
                    Text(i18n("auth.google_action"), fontWeight = FontWeight.Bold)
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f))
                Text(
                    text = i18n("auth.or_divider"),
                    modifier = Modifier.padding(horizontal = 12.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
                HorizontalDivider(modifier = Modifier.weight(1f))
            }

            AnimatedContent(
                targetState = isRegistering,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "authMode"
            ) {
                Text(
                    text = i18n(if (it) "auth.register_hint" else "auth.login_hint"),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp
                )
            }
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it; errorMessage = null },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(i18n("auth.email")) },
                leadingIcon = { Icon(Icons.Default.Mail, contentDescription = null) },
                singleLine = true
            )
            Spacer(Modifier.height(10.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it; errorMessage = null },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(i18n("auth.password")) },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true
            )

            errorMessage?.let {
                Spacer(Modifier.height(10.dp))
                Text(it, color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
            }

            Spacer(Modifier.height(18.dp))
            Button(
                onClick = {
                    if (email.isBlank() || password.length < 6) {
                        errorMessage = invalidCredentialsMessage
                        return@Button
                    }
                    scope.launch {
                        isLoading = true
                        errorMessage = null
                        runCatching {
                            if (isRegistering) repository.register(email.trim(), password)
                            else repository.signIn(email.trim(), password)
                        }.onSuccess {
                            onAuthenticated()
                        }.onFailure {
                            errorMessage = it.message ?: genericErrorMessage
                        }
                        isLoading = false
                    }
                },
                enabled = !isLoading && repository.isConfigured,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                if (isLoading && !isGoogleLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(22.dp))
                } else {
                    Text(i18n(if (isRegistering) "auth.register_action" else "auth.login_action"))
                }
            }

            if (!repository.isConfigured) {
                Spacer(Modifier.height(12.dp))
                Text(
                    text = i18n("auth.firebase_not_configured"),
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = i18n(if (isRegistering) "auth.have_account" else "auth.no_account"),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp
                )
                TextButton(onClick = { isRegistering = !isRegistering; errorMessage = null }) {
                    Text(i18n(if (isRegistering) "auth.login_action" else "auth.register_action"))
                }
            }
        }
    }
}
