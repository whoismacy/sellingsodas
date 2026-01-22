package com.whoismacy.android.sodaadminapp

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.whoismacy.android.sodaadminapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var selectedLocation: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get username from Intent and handle display name
        val username = intent.getStringExtra("EXTRA_USERNAME") ?: "User"
        val displayName = username.substringBefore("@")
        binding.userNameTextView.text = "Welcome, $displayName!"

        // Logout logic
        binding.logoutButton.setOnClickListener {
            Firebase.auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        val locations = arrayOf("Nairobi(HQ)", "Kisumu", "Eldoret", "Mombasa", "Nakuru")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, locations)
        binding.locationAutoComplete.setAdapter(adapter)

        binding.locationAutoComplete.setOnItemClickListener { parent, _, position, _ ->
            selectedLocation = parent.getItemAtPosition(position) as String
            Toast.makeText(this, "Location set to: $selectedLocation", Toast.LENGTH_SHORT).show()
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container1, SodaFragment.newInstance("Coke"))
                .replace(R.id.container2, SodaFragment.newInstance("Fanta"))
                .replace(R.id.container3, SodaFragment.newInstance("Sprite"))
                .commit()
        }
    }
}
