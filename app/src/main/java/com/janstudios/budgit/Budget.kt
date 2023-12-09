package com.janstudios.budgit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.janstudios.budgit.databinding.FragmentBudgetBinding

class Budget : Fragment() {
    private var _binding: FragmentBudgetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBudgetBinding.inflate(inflater,container, false)

        val items = arrayOf("Daily", "Weekly", "Fortnightly", "Monthly", "Yearly")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_frequency, items)

        binding.autocompleteFrequency.setAdapter(adapter)
        binding.addBudgetButton.setOnClickListener(){}

        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Nullify the binding to avoid memory leaks
    }

}