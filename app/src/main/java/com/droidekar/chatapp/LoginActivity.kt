package com.droidekar.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var email: EditText
    private lateinit var passwd: EditText
    private lateinit var login: Button
    private lateinit var signup: Button

    private  lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()

        email = findViewById(R.id.email)
        passwd = findViewById(R.id.passwd)
        signup = findViewById(R.id.signup)
        login = findViewById(R.id.login)

        signup.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            finish()
            startActivity(intent)
        }

        login.setOnClickListener {
            val email = email.text.toString()
            val passwd = passwd.text.toString()

            login(email, passwd)
        }
    }

    private fun login(email: String, passwd: String) {
        mAuth.signInWithEmailAndPassword(email, passwd)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    //Log.d(TAG, "signInWithEmail:success")
                    val user = mAuth.currentUser

                    // go to main activity
                    intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    //Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(this@LoginActivity, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

}