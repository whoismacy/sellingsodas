package com.whoismacy.android.sodaadminapp

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.whoismacy.android.sodaadminapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var selectedLocation: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.userNameTextView.text = "Welcome, Macy!"

        val locations = arrayOf("New York", "Los Angeles", "Chicago", "Houston", "Phoenix")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, locations)
        binding.locationAutoComplete.setAdapter(adapter)

        binding.locationAutoComplete.setOnItemClickListener { parent, _, position, _ ->
            selectedLocation = parent.getItemAtPosition(position) as String
            Toast.makeText(this, "Location set to: $selectedLocation", Toast.LENGTH_SHORT).show()
        }

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container1, SodaFragment.newInstance("Coca Cola"))
                .replace(R.id.container2, SodaFragment.newInstance("Pepsi"))
                .replace(R.id.container3, SodaFragment.newInstance("Sprite"))
                .commit()
        }
    }
}
