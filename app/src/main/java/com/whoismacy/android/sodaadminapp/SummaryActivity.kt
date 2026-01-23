package com.whoismacy.android.sodaadminapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.whoismacy.android.sodaadminapp.databinding.ActivitySummaryBinding
import com.whoismacy.android.sodaadminapp.databinding.ItemSodaSummaryBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SummaryActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySummaryBinding
    private val apiService = SodaApiService.create()
    private var allSalesData: Map<String, List<SodaSummary>> = emptyMap()

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

        binding.summaryRecyclerView.layoutManager = LinearLayoutManager(this)
        
        fetchSummaryData()
    }

    private fun fetchSummaryData() {
        apiService.getSalesSummary().enqueue(object : Callback<SummaryResponse> {
            override fun onResponse(call: Call<SummaryResponse>, response: Response<SummaryResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val summary = response.body()!!
                    allSalesData = summary.salesData
                    setupLocationSpinner(summary.locations)
                    updateSummary("All")
                } else {
                    Toast.makeText(this@SummaryActivity, "Failed to load summary", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SummaryResponse>, t: Throwable) {
                Toast.makeText(this@SummaryActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupLocationSpinner(apiLocations: List<String>) {
        val locations = mutableListOf("All")
        locations.addAll(apiLocations)
        
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, locations)
        binding.locationAutoComplete.setAdapter(adapter)
        
        binding.locationAutoComplete.setText(locations[0], false)

        binding.locationAutoComplete.setOnItemClickListener { parent, _, position, _ ->
            val selected = parent.getItemAtPosition(position) as String
            updateSummary(selected)
        }
    }

    private fun updateSummary(location: String) {
        val data = allSalesData[location] ?: emptyList()
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
