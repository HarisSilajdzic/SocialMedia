@file:Suppress("DEPRECATION")

package com.example.fragments

import android.content.Intent
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.FragmentActivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.socialmedia.R

import com.facebook.FacebookSdk.getApplicationContext
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth


@Suppress("DEPRECATION")
class LoginFragment : Fragment() {
    private val rcSignIN = 1
    private var googleApiClient: GoogleApiClient? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_feed, container, false)
    }
    private fun gotoProfile() {
//        startActivity(Intent(applicationContext, FeedActivity::class.java))
    }

    private fun handleSignInResult(result: GoogleSignInResult?) {
        if (result != null) {
            if(result.isSuccess){
                Toast.makeText(getApplicationContext(),"Sign in success", Toast.LENGTH_LONG).show()
                gotoProfile()
            }else{
                Toast.makeText(getApplicationContext(),"Sign in cancel", Toast.LENGTH_LONG).show()
            }
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnGoogle: SignInButton = view.findViewById(R.id.google_button)
        val btnLogin: Button = view.findViewById(R.id.login)
//        val btnRegister: Button = view.findViewById(R.id.register)
        val txtEmail: EditText = view.findViewById(R.id.email)
        val txtPassword: EditText = view.findViewById(R.id.password)
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        googleApiClient = context?.let {
            GoogleApiClient.Builder(it)
                .enableAutoManage(it as FragmentActivity) {  }
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()
        }

        btnGoogle.setOnClickListener {
            val intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
            startActivityForResult(intent, rcSignIN)
        }

        btnLogin.setOnClickListener {
            val email = txtEmail.text.toString()
            val password = txtPassword.text.toString()
            if (email.isNotEmpty() or password.isNotEmpty()) {
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        Log.d("TAG", "login successful")

                    }
                    .addOnFailureListener {
                        it.printStackTrace()
                    }
                    .addOnCompleteListener {

//                        startActivity(Intent(applicationContext, FeedActivity::class.java))
                    }
            }else
                Log.d("TAG", "empty email or pass")
        }

//        btnRegister.setOnClickListener {
//            val email = txtEmail.text.toString()
//            val password = txtPassword.text.toString()
//            if (email.isNotEmpty() or password.isNotEmpty()) {
//                FirebaseAuth.getInstance()
//                    .createUserWithEmailAndPassword(email, password)
//                    .addOnSuccessListener {
//                        Log.d("TAG", "login unsuccessful")
//                    }
//                    .addOnFailureListener {
//                        it.printStackTrace()
//                    }
//            } else
//                Log.d("TAG", "empty email or pass")
//
//
//        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == rcSignIN){
            val result =
                Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            handleSignInResult(result)
        }
    }
//
}