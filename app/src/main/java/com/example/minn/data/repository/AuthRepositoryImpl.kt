package com.example.minn.data.repository

import com.example.minn.Util.Response
import com.example.minn.domain.model.User
import com.example.minn.domain.repository.AuthRepository
import com.example.minn.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val userRepository: UserRepository
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
        password: String,
        name: String
    ): Flow<Response<Boolean>> = flow {
        emit(Response.Loading)

        try{
            auth.createUserWithEmailAndPassword(email,password).await()

            val firebaseUser = auth.currentUser!!

            val user = User(
                uid = firebaseUser.uid,
                email = email,
                name = name
            )

            userRepository.createUser(user)

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

    override suspend fun signOut(){
        auth.signOut()
    }
}