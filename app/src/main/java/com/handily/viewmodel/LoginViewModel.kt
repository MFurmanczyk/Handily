package com.handily.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginViewModel(application: Application): AndroidViewModel(application) {

    interface RegisterResultListener {
        fun onSuccess(uuid: String)
        fun onEmailMalformed()
        fun onPasswordTooShort()
        fun onPasswordNotMatched()
        fun onUserAlreadyExists()
    }

    interface LoginResultListener {
        fun onSuccess()
        fun onUserNotExists()
        fun onWrongPassword()
        fun onEmptyCredentials()
    }

    private val firebaseAuth: FirebaseAuth by lazy { Firebase.auth }

    private lateinit var loginListener: LoginResultListener

    private lateinit var registerListener: RegisterResultListener

    fun setRegisterResultListener(listener: RegisterResultListener) {
        registerListener = listener
    }

    fun setLoginResultListener(listener: LoginResultListener) {
        loginListener = listener
    }

    fun signInWithEmailAndPassword(email: String, password: String) {
        if(email.isNotEmpty() || password.isNotEmpty()) {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                        loginListener.onSuccess()
                } else {
                    when (it.exception) {
                        is FirebaseAuthInvalidUserException -> loginListener.onUserNotExists()
                        is FirebaseAuthInvalidCredentialsException -> loginListener.onWrongPassword()
                    }
                }
            }
        } else {
            loginListener.onEmptyCredentials()
        }
    }

    fun registerUserWithEmailAndPassword(email: String, password: String, repeatedPassword: String) {
        if(comparePassword(password, repeatedPassword) && passwordLength(password) && email.isNotEmpty()) {
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    registerListener.onSuccess(it.result?.user?.uid.toString())
                } else {
                    when(it.exception) {
                        is FirebaseAuthInvalidCredentialsException -> {
                            registerListener.onEmailMalformed()
                        }
                        is FirebaseAuthUserCollisionException -> {
                            registerListener.onUserAlreadyExists()
                        }
                    }
                }
            }
        } else {
            if(!comparePassword(password, repeatedPassword)) registerListener.onPasswordNotMatched()
            else if(!passwordLength(password)) registerListener.onPasswordTooShort()
            else if(email.isEmpty()) registerListener.onEmailMalformed()
        }
    }

    private fun comparePassword(password: String, repeatedPassword: String): Boolean {
        return password == repeatedPassword
    }

    private fun passwordLength(password: String): Boolean {
        return password.length >= 8
    }
}