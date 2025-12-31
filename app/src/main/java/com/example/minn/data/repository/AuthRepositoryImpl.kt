package com.example.minn.data.repository

import com.example.minn.Util.Response
import com.example.minn.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val fs: FirebaseFirestore
): AuthRepository {
    override fun isUserAuth(): Boolean {
        return auth.currentUser!=null
    }

    override fun getAuthState(): Flow<Boolean> = callbackFlow {
        val authListener = FirebaseAuth.AuthStateListener{firebaseAuth ->
            trySend(firebaseAuth.currentUser!=null)
        }
        auth.addAuthStateListener(authListener)

        awaitClose {
            auth.removeAuthStateListener(authListener)
        }
    }

    override fun signUp(
        email: String,
        password: String
    ): Flow<Response<Boolean>> = flow {
        emit(Response.Loading)

        try{
            auth.createUserWithEmailAndPassword(email,password).await()
            emit(Response.Success(true))
        } catch (e: Exception){
            emit(Response.Error(e.message ?: "Sign Up error"))
        }
    }

    override fun signIn(
        email: String,
        password: String
    ): Flow<Response<Boolean>> = flow {
        emit(Response.Loading)

        try {
            auth.signInWithEmailAndPassword(email,password).await()
            emit(Response.Success(true))
        } catch (e: Exception){
            emit(Response.Error(e.message ?: "Sign In error"))
        }
    }

    override fun signOut(): Flow<Response<Boolean>> = flow {
        emit(Response.Loading)

        try{
            auth.signOut()
            emit(Response.Success(true))
        } catch (e: Exception){
            emit(Response.Error(e.message ?: "Sign out error"))
        }
    }
}