package com.exertia.wikingo.data.auth

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.GoogleAuthProvider
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

    suspend fun signInWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        requireConfigured().signInWithCredential(credential).await()
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
