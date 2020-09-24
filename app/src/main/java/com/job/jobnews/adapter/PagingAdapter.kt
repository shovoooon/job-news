package com.job.jobnews.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.job.jobnews.App
import com.job.jobnews.R
import com.job.jobnews.ui.PagingActivity
import kotlinx.android.synthetic.main.item_job.view.*


class PagingAdapter(
    private val activity: PagingActivity,
    private val onItemClicked: OnItemClicked
) : RecyclerView.Adapter<PagingAdapter.NoteViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_job, parent, false)
        )
    }

    interface OnItemClicked {
        fun onClickView(id: String)
    }

    override fun getItemCount() = activity.jobList.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {

        holder.view.itemJobViewBtn.animation = AnimationUtils.loadAnimation(App.applicationContext(), R.anim.fade_transition_anim)
        holder.view.itemJobCard.animation = AnimationUtils.loadAnimation(App.applicationContext(), R.anim.fade_scale_anim)

        val id = activity.jobList[position].id
        val title = activity.jobList[position].title
        val desc = activity.jobList[position].desc
        val catName = activity.jobList[position].catName
        val pos = activity.jobList[position].position
        val lastDate = activity.jobList[position].lastDate
        val views = activity.jobList[position].views

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



/*
class PagingAdapter(val activity: PagingActivity) : RecyclerView.Adapter<PagingAdapter.NumberViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): NumberViewHolder {
        return NumberViewHolder(LayoutInflater.from(activity).inflate(R.layout.rv_child_number, p0, false))
    }

    override fun getItemCount(): Int {
        return activity.numberList.size
    }

    override fun onBindViewHolder(p0: NumberViewHolder, p1: Int) {
        p0.tvNumber.text = activity.numberList[p1]
    }

    class NumberViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvNumber = v.findViewById<TextView>(R.id.tv_number)
    }
}*/
