package com.job.news.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.job.news.Constants
import com.job.news.R
import com.job.news.model.CategoryResponse
import kotlinx.android.synthetic.main.item_category.view.*

/**
 * Created by Itz Shovon on 4/16/2020
 */
class CategoryAdapter(
        private val context: Context,
        private val notes: List<CategoryResponse>,
        private val onItemClicked: OnItemClicked
) : RecyclerView.Adapter<CategoryAdapter.NoteViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_category, parent, false)
        )
    }

    interface OnItemClicked {
        fun onItemClicked(id: String)
    }

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {

        val id = notes[position].id
        val name = notes[position].catName
        val img = notes[position].catImg
        val jobCount = notes[position].jobCount

        holder.view.itemCatName.text = name
        holder.view.itemCatTotalJobs.text = "Total jobs: $jobCount"

        Glide.with(context).load(Constants.BASE_URL_IMG+img).into(holder.view.itemCatImg)

        holder.view.setOnClickListener {
            onItemClicked.onItemClicked(id)
        }

        /*holder.view.basketNameTextView.text = "$title  \n Code: $code"
        holder.view.priceTextView.text = price
        holder.view.subTotalTextView.text = subTotal.toString()
        holder.view.qtyEditText.setText(quantity)

        Glide.with(context).load(img).into(holder.view.itemImageView)

        holder.view.deleteBgImageView.setOnClickListener {
            deleteCart(id)
        }

        holder.view.plusImageView.setOnClickListener {
            plusQuantity(id)
        }

        holder.view.minusImageView.setOnClickListener {
            if (quantity.toInt() > 1){
                minusQuantity(id)
            }
        }*/
    }

    class NoteViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}
