package com.example.supfitness.adapaters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.supfitness.R
import com.example.supfitness.data.WeightsData

class WeightsAdapter(
    private val mList: List<WeightsData>,
    private val onDeleteButtonClick: (id: Int) -> Unit
) : RecyclerView.Adapter<WeightsAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_weight, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]

        // sets the image to the imageview from our itemHolder class
        holder.textView.text = ItemsViewModel.weight
        holder.dateView.text = ItemsViewModel.date

        holder.deleteButton.setOnClickListener {
            // delete weight
            onDeleteButtonClick(ItemsViewModel.id)
        }

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val dateView: TextView = itemView.findViewById(R.id.dateView)
        val textView: TextView = itemView.findViewById(R.id.textView)
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
    }
}