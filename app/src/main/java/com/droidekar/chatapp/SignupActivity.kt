package com.droidekar.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignupActivity : AppCompatActivity() {

    private lateinit var emailView: EditText
    private lateinit var nameView: EditText
    private lateinit var passwdView: EditText
    private lateinit var signupButton: Button

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDb: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()

        nameView = findViewById(R.id.user_id)
        emailView = findViewById(R.id.email)
        passwdView = findViewById(R.id.passwd)
        signupButton = findViewById(R.id.signup)

        signupButton.setOnClickListener() {
            val name = nameView.text.toString()
            val email = emailView.text.toString()
            val passwd = passwdView.text.toString()
            signup(name, email, passwd)
        }

    }

    private val TAG = "ChatApp"

    private fun signup(name: String, email: String, passwd: String) {
        mAuth.createUserWithEmailAndPassword(email, passwd)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = mAuth.currentUser

                    // add user to DB
                    addUserToDb(name, email, mAuth.currentUser?.uid!!)
                    finish()

                    // go to main activity
                    intent = Intent(this@SignupActivity, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(this@SignupActivity, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun addUserToDb(name: String, email: String, uid: String) {
        mDb = FirebaseDatabase.getInstance().getReference()

        mDb.child("user").child(uid).setValue(User(name, email, uid))
    }
}