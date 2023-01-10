package bia.dd.looper


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList

class CustomAdapter(private val mList: ArrayList<ItemsViewModel>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    var onItemClick : ((ItemsViewModel) -> Unit)? =  null
    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_design, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        holder.textView.text = ItemsViewModel.name
        holder.dueText.text = ItemsViewModel.dueDate
        holder.checkBox.isChecked = ItemsViewModel.completed
        holder.checkBox.setOnClickListener {
            if(ItemsViewModel.completed) {
                ItemsViewModel.updateCompleted("Uncompleted", false)
            }
            else {
                ItemsViewModel.updateCompleted("$year/$month/$day", true)
            }
            mList.set(position, ItemsViewModel)
            (holder.itemView.context as MainActivity).sortBucketList()


        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(ItemsViewModel)
        }

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val textView: TextView = itemView.findViewById(R.id.textView)
        val dueText: TextView = itemView.findViewById(R.id.dueDate)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
    }

}
