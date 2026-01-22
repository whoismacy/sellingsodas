package com.whoismacy.android.sodaadminapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.whoismacy.android.sodaadminapp.databinding.FragmentDuplicateBinding
import kotlin.random.Random

class SodaFragment : Fragment() {
    private var _binding: FragmentDuplicateBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDuplicateBinding.inflate(inflater, container, false)
        val sodaName = arguments?.getString(ARG_SODA_NAME) ?: "Unknown Soda"
        binding.textView.text = sodaName

        // Mock data for display (to be replaced with API later)
        val mockSold = Random.nextInt(50, 200)
        val mockRemaining = Random.nextInt(10, 100)
        val mockMoney = Random.nextInt(500, 5000)

        binding.soldTextView.text = "Sold: $mockSold"
        binding.remainingTextView.text = "Remaining: $mockRemaining"
        binding.moneyTextView.text = "Money Generated: $$mockMoney"

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
            .setPositiveButton("Buy") { _, _ ->
                val quantity = numberPicker.value
                Toast.makeText(context, "Re-stocked $quantity $sodaName for $location", Toast.LENGTH_LONG).show()
            }.setNegativeButton("Cancel", null)
            .show()
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
