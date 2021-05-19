package team.unravel.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk.getApplicationContext
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.unravel.socialmedia.R


class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var auth: FirebaseAuth
    private val callBackManager: CallbackManager = CallbackManager.Factory.create()
    private val rcSignIN = 1
    private var googleApiClient: GoogleApiClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    private fun gotoProfile() {
        findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
//        activity?.let { Navigation.findNavController(it, R.id.nav_host_fragment).navigate(R.id.action_loginFragment_to_mainFragment) }
    }

    private fun handleSignInResult(result: GoogleSignInResult?) {
        if (result != null) {
            if (result.isSuccess) {
                Toast.makeText(getApplicationContext(), "Sign in success", Toast.LENGTH_LONG).show()
                gotoProfile()
            } else {
                Toast.makeText(getApplicationContext(), "Sign in cancel", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun updateUI() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnFb: ImageButton = view.findViewById(R.id.fb)
        val btnFacebook: LoginButton = view.findViewById(R.id.facebook_button)
        val btnG: ImageButton = view.findViewById(R.id.google)
        val btnGoogle: SignInButton = view.findViewById(R.id.google_button)
        val btnLogin: Button = view.findViewById(R.id.login)
        val btnRegister: TextView = view.findViewById(R.id.register)
        val txtEmail: EditText = view.findViewById(R.id.email)
        val txtPassword: EditText = view.findViewById(R.id.password)
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build()


        btnFb.setOnClickListener {

//            try {
//                val info = context!!.packageManager.getPackageInfo(
//                    context!!.packageName,
//                    PackageManager.GET_SIGNATURES
//                )
//                for (signature in info.signatures) {
//                    val md = MessageDigest.getInstance("SHA")
//                    md.update(signature.toByteArray())
//                    val hashKey = String(Base64.getEncoder().encode(md.digest()))
//                    Log.i("AppLog", "key:$hashKey=")
//                }
//            } catch (e: Exception) {
//                Log.e("AppLog", "error:", e)
//            }

            btnFacebook.fragment = this
            btnFacebook.performClick()
        }
        btnFacebook.setReadPermissions("email", "public_profile")
        btnFacebook.registerCallback(callBackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                this@LoginFragment.handleFacebookAccessToken(result!!.accessToken)
            }

            override fun onCancel() {
                Log.d("TAG", "onCancel: ")
            }

            override fun onError(error: FacebookException?) {
                Log.d("TAG", "onError: ")
            }
        })

        googleApiClient = context?.let {
            GoogleApiClient.Builder(it)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()
        }


        btnG.setOnClickListener {
            val intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
            btnGoogle.performClick()
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
                        Toast.makeText(
                            getApplicationContext(),
                            "Login successful!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    .addOnFailureListener {
                        it.printStackTrace()
                    }
                    .addOnCompleteListener {

//                        startActivity(Intent(applicationContext, FeedActivity::class.java))
                    }
            } else
                Log.d("TAG", "empty email or pass")
        }

        btnRegister.setOnClickListener {
            val email = txtEmail.text.toString()
            val password = txtPassword.text.toString()
            if (email.isNotEmpty() or password.isNotEmpty()) {
                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        Log.d("TAG", "login unsuccessful")
                        Toast.makeText(getApplicationContext(), "Registered!", Toast.LENGTH_LONG)
                            .show()
                        gotoProfile()
                    }
                    .addOnFailureListener {
                        it.printStackTrace()
                    }
            } else
                Log.d("TAG", "empty email or pass")


        }
    }

//    override fun onPause() {
//        super.onPause()
//        activity?.let { googleApiClient?.stopAutoManage(it) }
//        googleApiClient?.disconnect()
//    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        if (requestCode == rcSignIN) {
            val result =  Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            account?.idToken?.let { authUser(it) }
            handleSignInResult(result)
        }

        callBackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun authUser(token: String) {
        val credential = GoogleAuthProvider.getCredential(token, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    updateUI()
                } else {
                    updateUI()
                }
            }

    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d("TAG", "signInWithCredential:success")
                val user = auth.currentUser
                updateUI()
                gotoProfile()
            } else {
                // If sign in fails, display a message to the user.
                Log.w("TAG", "signInWithCredential:failure", task.exception)
                Toast.makeText(
                    context, "Authentication failed.",
                    Toast.LENGTH_SHORT
                ).show()
                updateUI()
            }
        }
    }


}