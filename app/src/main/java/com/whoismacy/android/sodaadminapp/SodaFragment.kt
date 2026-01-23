package com.whoismacy.android.sodaadminapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.whoismacy.android.sodaadminapp.databinding.FragmentDuplicateBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SodaFragment : Fragment() {
    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: FragmentDuplicateBinding? = null
    private val binding get() = _binding!!
    private val apiService = SodaApiService.create()
    private var sodaName: String = "Unknown Soda"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDuplicateBinding.inflate(inflater, container, false)
        sodaName = arguments?.getString(ARG_SODA_NAME) ?: "Unknown Soda"
        binding.textView.text = sodaName

        // Initial state
        binding.soldTextView.text = "Sold: --"
        binding.moneyTextView.text = "Total Money: Kshs--"
        binding.quantityTextView.text = "Remaining: --"

        binding.innerBuyButton.setOnClickListener {
            val mainActivity = activity as? MainActivity
            val location = mainActivity?.selectedLocation

            if (location == null) {
                Toast.makeText(context, "Please select a location first!", Toast.LENGTH_SHORT).show()
            } else {
                showQuantityDialog(sodaName, location)
            }
        }

        return binding.root
    }

    fun refreshData(location: String) {
        apiService.getSodaDetails(location, sodaName).enqueue(
            object : Callback<SodaResponse> {
                @SuppressLint("SetTextI18n")
                override fun onResponse(
                    call: Call<SodaResponse>,
                    response: Response<SodaResponse>,
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val metrics = response.body()!!.metrics
                        binding.soldTextView.text = "Sold: ${metrics.sold}"
                        binding.moneyTextView.text = "Total Money: Kshs${metrics.revenue}"
                        binding.quantityTextView.text = "Remaining: ${metrics.remaining}"
                    } else {
                        Toast.makeText(context, "Error fetching data for $sodaName", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(
                    call: Call<SodaResponse>,
                    t: Throwable,
                ) {
                    Toast.makeText(context, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            },
        )
    }

    private fun showQuantityDialog(
        sodaName: String,
        location: String,
    ) {
        val numberPicker =
            NumberPicker(requireContext()).apply {
                minValue = 1
                maxValue = 100
                value = 1
            }

        AlertDialog
            .Builder(requireContext())
            .setTitle("Select Quantity for $sodaName")
            .setMessage("Location: $location")
            .setView(numberPicker)
            .setPositiveButton("Re-Stock") { _, _ ->
                val quantity = numberPicker.value
                performRestock(location, sodaName, quantity)
            }.setNegativeButton("Cancel", null)
            .show()
    }

    private fun performRestock(
        location: String,
        brand: String,
        quantity: Int,
    ) {
        apiService.restock(location, brand, quantity).enqueue(
            object : Callback<RestockResponse> {
                override fun onResponse(
                    call: Call<RestockResponse>,
                    response: Response<RestockResponse>,
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(context, "Successfully re-stocked $quantity $brand at $location", Toast.LENGTH_LONG).show()
                        refreshData(location)
                    } else {
                        Toast.makeText(context, "Failed to re-stock: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(
                    call: Call<RestockResponse>,
                    t: Throwable,
                ) {
                    Toast.makeText(context, "Network error during re-stock: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            },
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_SODA_NAME = "soda_name"

        fun newInstance(sodaName: String): SodaFragment {
            val fragment = SodaFragment()
            val args = Bundle()
            args.putString(ARG_SODA_NAME, sodaName)
            fragment.arguments = args
            return fragment
        }
    }
}
