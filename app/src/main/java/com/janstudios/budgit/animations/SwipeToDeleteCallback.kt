package com.janstudios.budgit.animations

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.janstudios.budgit.adapters.TransactionAdapter
import com.janstudios.budgit.database.BudgetDatabase
import com.janstudios.budgit.database.UserTransaction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SwipeToDeleteCallback(
    private val adapter: TransactionAdapter,
    private val transactionList: MutableList<UserTransaction>,
    private val db: BudgetDatabase,
    private val scope: CoroutineScope
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    // Disable move functionality as it's not needed
    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    // Handle swipe action: delete the item and update the database
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        val transactionToDelete = transactionList.removeAt(position)
        adapter.notifyItemRemoved(position)

        // Delete the transaction from the database asynchronously
        scope.launch(Dispatchers.IO) {
            db.transactionDao().delete(transactionToDelete)
        }
    }
}
