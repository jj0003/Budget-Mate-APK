package com.janstudios.budgit.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.janstudios.budgit.database.UserBudget
import com.janstudios.budgit.databinding.BudgetListItemBinding

class BudgetAdapter(private val budgets: List<UserBudget>) : RecyclerView.Adapter<BudgetAdapter.ViewHolder>() {

    // ViewHolder class for holding view references
    class ViewHolder(private val binding: BudgetListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        // Bind budget data to the UI elements in each list item
        fun bind(userBudget: UserBudget) {
            "$ ${userBudget.amountBudget}".also { binding.textAmount.text = it }
            binding.textFrequency.text = userBudget.frequency
            binding.budgetDate.text = "Your Date Logic Here"
        }
    }

    // Create new ViewHolder for each list item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = BudgetListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    // Bind data to the ViewHolder at specified position
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(budgets[position])
    }

    // Return the total number of items in the list
    override fun getItemCount() = budgets.size
}
