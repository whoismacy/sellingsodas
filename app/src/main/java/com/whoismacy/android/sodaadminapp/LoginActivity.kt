package com.whoismacy.android.sodaadminapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.whoismacy.android.sodaadminapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        val currentUser = auth.currentUser
        if (currentUser != null) {
            navigateToSummary(currentUser.email ?: "User")
        }

        binding.loginButton.setOnClickListener {
            val email =
                binding.usernameEditText.text
                    .toString()
                    .trim()
            val password =
                binding.passwordEditText.text
                    .toString()
                    .trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth
                    .signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            navigateToSummary(user?.email ?: "User")
                        } else {
                            Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToSummary(username: String) {
        val intent = Intent(this, SummaryActivity::class.java)
        intent.putExtra("EXTRA_USERNAME", username)
        startActivity(intent)
        finish()
    }
}
