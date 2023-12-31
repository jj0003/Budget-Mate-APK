package com.janstudios.budgit.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.datepicker.MaterialDatePicker
import com.janstudios.budgit.database.SleepDatabase
import com.janstudios.budgit.database.UserTransaction
import com.janstudios.budgit.databinding.FragmentAddTransactionBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class AddTransaction : Fragment() {

    private var _binding: FragmentAddTransactionBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: SleepDatabase

    // Inflate the layout and set up the fragment
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAddTransactionBinding.inflate(inflater, container, false)

        // Initialize the database
        db = SleepDatabase.getInstance(requireContext())

        // Setup click listeners
        binding.addTransactionButton.setOnClickListener { addTransactionToDatabase() }
        initializeDatePicker()

        return binding.root
    }

    // Adds a new transaction to the database
    private fun addTransactionToDatabase() {
        val label = binding.inputLabelEdit.text.toString()
        val amount = binding.inputAmountEdit.text.toString()
        val date = binding.inputDateEdit.text.toString()

        // Validate input fields
        if (label.isBlank() || amount.isBlank() || date.isBlank()) {
            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Create and insert transaction
        val transaction = UserTransaction(amount = amount, label = label, date = date)
        insertTransactionIntoDatabase(transaction)
    }

    // Inserts the given transaction into the database and clears the input fields
    private fun insertTransactionIntoDatabase(transaction: UserTransaction) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                db.transactionDao().insert(transaction)
            }
            // Switch to the Main thread for UI updates
            launch(Dispatchers.Main) {
                clearInputFields()
            }
        }
    }

    // Clears the input fields in the UI
    private fun clearInputFields() {
        binding.inputLabelEdit.setText("")
        binding.inputAmountEdit.setText("")
        binding.inputDateEdit.setText("")
    }

    // Initialize the date picker for the date input field
    private fun initializeDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker().build()

        binding.inputDateEdit.setOnClickListener {
            datePicker.show(parentFragmentManager, datePicker.toString())
        }

        datePicker.addOnPositiveButtonClickListener { selection ->
            // Use the selected date
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            calendar.time = Date(selection)
            val dateFormat = "yyyy-MM-dd"
            val sdf = SimpleDateFormat(dateFormat, Locale.getDefault())
            binding.inputDateEdit.setText(sdf.format(calendar.time))
        }
    }

    // Updates the date in the input field after selecting from date picker
    private fun updateDateInView(calendar: Calendar, year: Int, month: Int, dayOfMonth: Int) {
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        val dateFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(dateFormat, Locale.getDefault())
        binding.inputDateEdit.setText(sdf.format(calendar.time))
    }

    // Clean up binding when the view is destroyed
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
