package com.whoismacy.android.sodaadminapp

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.whoismacy.android.sodaadminapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var selectedLocation: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra("EXTRA_USERNAME") ?: "User"
        val displayName = username.substringBefore("@")
        binding.userNameTextView.text = "Welcome, $displayName!"
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
            refreshFragments(selectedLocation!!)
        }

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container1, SodaFragment.newInstance("Coke"), "soda_coke")
                .replace(R.id.container2, SodaFragment.newInstance("Fanta"), "soda_fanta")
                .replace(R.id.container3, SodaFragment.newInstance("Sprite"), "soda_sprite")
                .commit()
        }
    }

    private fun refreshFragments(location: String) {
        val fragments = listOf("soda_coke", "soda_fanta", "soda_sprite")
        for (tag in fragments) {
            val fragment = supportFragmentManager.findFragmentByTag(tag) as? SodaFragment
            fragment?.refreshData(location)
        }
    }
}
