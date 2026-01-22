package com.whoismacy.android.sodaadminapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.whoismacy.android.sodaadminapp.databinding.ActivitySummaryBinding
import com.whoismacy.android.sodaadminapp.databinding.ItemSodaSummaryBinding

data class SodaSummary(val brand: String, val totalSold: Int)

class SummaryActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySummaryBinding
    private val sodaBrands = listOf("Coke", "Fanta", "Sprite")
    
    // Mock data: Map of Location -> List of SodaSummary
    private val salesData = mapOf(
        "All" to listOf(
            SodaSummary("Coke", 450),
            SodaSummary("Fanta", 320),
            SodaSummary("Sprite", 280)
        ),
        "Nairobi(HQ)" to listOf(
            SodaSummary("Coke", 200),
            SodaSummary("Fanta", 150),
            SodaSummary("Sprite", 100)
        ),
        "Kisumu" to listOf(
            SodaSummary("Coke", 80),
            SodaSummary("Fanta", 60),
            SodaSummary("Sprite", 50)
        ),
        "Eldoret" to listOf(
            SodaSummary("Coke", 70),
            SodaSummary("Fanta", 40),
            SodaSummary("Sprite", 60)
        ),
        "Mombasa" to listOf(
            SodaSummary("Coke", 60),
            SodaSummary("Fanta", 40),
            SodaSummary("Sprite", 40)
        ),
        "Nakuru" to listOf(
            SodaSummary("Coke", 40),
            SodaSummary("Fanta", 30),
            SodaSummary("Sprite", 30)
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra("EXTRA_USERNAME") ?: "Admin"
        val displayName = username.substringBefore("@")
        binding.welcomeTextView.text = "Welcome, $displayName"

        binding.logoutButton.setOnClickListener {
            Firebase.auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.reStockButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("EXTRA_USERNAME", username)
            startActivity(intent)
        }

        setupLocationSpinner()
        setupRecyclerView("All")
    }

    private fun setupLocationSpinner() {
        val locations = arrayOf("All", "Nairobi(HQ)", "Kisumu", "Eldoret", "Mombasa", "Nakuru")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, locations)
        binding.locationAutoComplete.setAdapter(adapter)
        
        // Set default text
        binding.locationAutoComplete.setText(locations[0], false)

        binding.locationAutoComplete.setOnItemClickListener { parent, _, position, _ ->
            val selected = parent.getItemAtPosition(position) as String
            updateSummary(selected)
        }
    }

    private fun setupRecyclerView(location: String) {
        binding.summaryRecyclerView.layoutManager = LinearLayoutManager(this)
        updateSummary(location)
    }

    private fun updateSummary(location: String) {
        val data = salesData[location] ?: salesData["All"]!!
        binding.summaryRecyclerView.adapter = SummaryAdapter(data)
    }

    class SummaryAdapter(private val items: List<SodaSummary>) : 
        RecyclerView.Adapter<SummaryAdapter.ViewHolder>() {

        class ViewHolder(val binding: ItemSodaSummaryBinding) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ItemSodaSummaryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = items[position]
            holder.binding.brandNameTextView.text = item.brand
            holder.binding.totalSoldTextView.text = "Total: ${item.totalSold}"
        }

        override fun getItemCount() = items.size
    }
}
