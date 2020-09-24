package com.job.jobnews.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.job.jobnews.R
import com.job.jobnews.model.JobResponse
import kotlinx.android.synthetic.main.item_job.view.*

/**
 * Created by Itz Shovon on 4/16/2020
 */
class JobAdapter(
    private val context: Context,
    private val notes: List<JobResponse>,
    private val onItemClicked: OnItemClicked
) : RecyclerView.Adapter<JobAdapter.NoteViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_job, parent, false)
        )
    }

    interface OnItemClicked {
        fun onClickView(id: String)
    }

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {

        holder.view.itemJobViewBtn.animation = AnimationUtils.loadAnimation(context, R.anim.fade_transition_anim)
        holder.view.itemJobCard.animation = AnimationUtils.loadAnimation(context, R.anim.fade_scale_anim)

        val id = notes[position].id
        val title = notes[position].title
        val desc = notes[position].desc
        val catName = notes[position].catName
        val pos = notes[position].position
        val lastDate = notes[position].lastDate
        val views = notes[position].views

        holder.view.itemJobTitle.text = title
        holder.view.itemJobLastDate.text = "Last date: $lastDate"
        holder.view.itemJobPosition.text = pos


        holder.view.itemJobViewBtn.setOnClickListener {
            onItemClicked.onClickView(id)
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
