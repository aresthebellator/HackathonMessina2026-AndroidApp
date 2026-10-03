package com.exertia.wikingo.data.auth

import android.app.Activity
import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.FirebaseOptions
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository(context: Context) {
    private val firebaseApp = FirebaseApp.getApps(context)
        .firstOrNull { it.name == FirebaseApp.DEFAULT_APP_NAME }
        ?: FirebaseApp.initializeApp(context)
        ?: FirebaseApp.initializeApp(context, fallbackOptions())

    private val auth: FirebaseAuth?
    private val firestore: FirebaseFirestore?

    init {
        auth = FirebaseAuth.getInstance(firebaseApp)
        firestore = FirebaseFirestore.getInstance(firebaseApp)
    }

    val currentUser get() = auth?.currentUser
    val isConfigured get() = auth != null

    suspend fun signIn(email: String, password: String) {
        requireConfigured().signInWithEmailAndPassword(email, password).await()
    }

    suspend fun signInWithGoogle(activity: Activity) {
        val provider = OAuthProvider.newBuilder("google.com", requireConfigured()).build()
        requireConfigured().startActivityForSignInWithProvider(activity, provider).await()
    }

    fun authErrorMessage(exception: Exception): String =
        when ((exception as? FirebaseAuthException)?.errorCode) {
            "ERROR_OPERATION_NOT_ALLOWED" ->
                "Abilita Google come provider in Firebase Authentication e riprova."
            "ERROR_INVALID_CREDENTIAL" ->
                "La configurazione OAuth Android non è valida. Verifica google-services.json, package name e SHA-1 in Firebase."
            "ERROR_NETWORK_REQUEST_FAILED" ->
                "Connessione non disponibile. Controlla Internet e riprova."
            "ERROR_TOO_MANY_REQUESTS" ->
                "Troppi tentativi. Attendi qualche minuto e riprova."
            "ERROR_WEB_STORAGE_UNSUPPORTED" ->
                "Il dispositivo non supporta il salvataggio richiesto per l'accesso Google."
            else -> when ((exception as? FirebaseException)?.message) {
                null, "" -> "Impossibile completare l'accesso Google. Verifica la configurazione Firebase."
                else -> exception.message ?: "Impossibile completare l'accesso Google. Riprova."
            }
        }

    suspend fun register(email: String, password: String) {
        val result = requireConfigured()
            .createUserWithEmailAndPassword(email, password)
            .await()
        val user = result.user ?: return
        firestore?.collection("users")?.document(user.uid)?.set(
            mapOf(
                "email" to (user.email ?: email),
                "createdAt" to com.google.firebase.firestore.FieldValue.serverTimestamp()
            )
        )?.await()
    }

    fun signOut() {
        auth?.signOut()
    }

    fun addAuthStateListener(listener: AuthStateListener): AuthStateListener? {
        auth?.addAuthStateListener(listener)
        return if (auth == null) null else listener
    }

    fun removeAuthStateListener(listener: AuthStateListener) {
        auth?.removeAuthStateListener(listener)
    }

    private fun requireConfigured(): FirebaseAuth =
        auth ?: error("Firebase non configurato: aggiungi google-services.json e configura Firebase.")

    private fun fallbackOptions(): FirebaseOptions =
        FirebaseOptions.Builder()
            .setApiKey("AIzaSyBmpQK-J9ybGcRg5_zBol6jYo89AZz-bnE")
            .setApplicationId("1:226310225271:web:531801f183acd93d8bb572")
            .setProjectId("wikingo-auth")
            .setDatabaseUrl("https://wikingo-auth-default-rtdb.europe-west1.firebasedatabase.app")
            .setStorageBucket("wikingo-auth.firebasestorage.app")
            .setGcmSenderId("226310225271")
            .build()
}
