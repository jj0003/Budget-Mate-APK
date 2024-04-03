package com.janstudios.budgit.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.janstudios.budgit.R
import com.janstudios.budgit.database.BudgetDatabase
import com.janstudios.budgit.database.User
import com.janstudios.budgit.database.UserTransaction
import com.janstudios.budgit.databinding.FragmentHomeBinding
import com.janstudios.budgit.databinding.FragmentSettingsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Settings : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: BudgetDatabase

    // Inflate the layout and set up the fragment
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        // Initialize the database
        db = BudgetDatabase.getInstance(requireContext())

        displayNameAndCurrency()

        binding.editNameAndCurrency.setOnClickListener { addUserToDatabase() }

        return binding.root
    }

    private fun displayNameAndCurrency() {
        lifecycleScope.launch {
            val user = withContext(Dispatchers.IO) {
                // Fetch the latest user; adjust as necessary for your use case
                db.userDao().getLatestUser()
            }

            // Update UI elements on the main thread
            user?.let {
                binding.inputNameUser.setText(it.userName.toString())
                binding.inputFieldCurrency.setText(it.currencySet.toString())
            }
        }
    }

    // Adds a new user to the database
    private fun addUserToDatabase() {
        val name = binding.inputNameUser.text.toString()
        val currency = binding.inputFieldCurrency.text.toString()

        // Validate input fields
        if (name.isBlank() || currency.isBlank()) {
            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Create and insert user
        val user = User(userName = name, currencySet = currency)
        lifecycleScope.launch {
            db.userDao().insertUser(user)
        }
    }

    // Clean up binding when the view is destroyed
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}