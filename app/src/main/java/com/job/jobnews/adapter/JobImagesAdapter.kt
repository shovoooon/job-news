package com.job.jobnews.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.job.jobnews.Constants
import com.job.jobnews.R
import com.job.jobnews.model.JobImagesResponse
import kotlinx.android.synthetic.main.item_job_images.view.*

/**
 * Created by Itz Shovon on 4/16/2020
 */
class JobImagesAdapter(
    private val context: Context,
    private val notes: List<JobImagesResponse>,
    private val onItemClicked: OnItemClicked
) : RecyclerView.Adapter<JobImagesAdapter.NoteViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_job_images, parent, false)
        )
    }

    interface OnItemClicked {
        fun onClickBtnDownload(jobImg: String)
    }

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {

        val id = notes[position].id
        val jobId = notes[position].jobId
        val jobImg = notes[position].img

        Glide.with(context).load(Constants.BASE_URL_IMG+jobImg).into(holder.view.itemJobImage)


        holder.view.itemBtnDownload.setOnClickListener {
            onItemClicked.onClickBtnDownload(jobImg)
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
