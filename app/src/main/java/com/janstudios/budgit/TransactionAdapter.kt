import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.janstudios.budgit.UserTransaction
import com.janstudios.budgit.databinding.ListItemBinding

class TransactionAdapter(private val transactionList: ArrayList<UserTransaction>) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(userTransaction: UserTransaction) {
            binding.itemAmount.text = userTransaction.amount
            binding.itemCategory.text = userTransaction.category
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val userTransaction = transactionList[position]
        holder.bind(userTransaction)
    }

    override fun getItemCount() = transactionList.size
}
