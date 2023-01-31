package com.example.pkompoultrymanager.inventory.feed.addition

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.example.pkompoultrymanager.R
import java.text.SimpleDateFormat
import java.util.*

class FeedInventoryAdditionAdapter(currency: String, measuringUnit: String): RecyclerView.Adapter<FeedInventoryAdditionAdapter.MyViewHolder>() {
    private lateinit var mListener: OnItemClickListener
    private val myMeasuringUnit = measuringUnit
    private val myCurrency = currency


    interface OnItemClickListener{
        fun onDeleteFeedInventoryAdditionClickListener(feedInventoryAddition: FeedInventoryAddition, position: Int)
        fun onItemClickListener(feedInventoryAddition: FeedInventoryAddition)
    }
    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener=listener

    }


    private var feedInventoryAdditionList = emptyList<FeedInventoryAddition>()

    inner class MyViewHolder(itemView: View, listener: OnItemClickListener): RecyclerView.ViewHolder(itemView){
        val itemMenu: ImageView
        val itemFeedName: TextView
        val itemQuantity: TextView
        val itemDate: TextView
        val itemCost: TextView
        val itemSource: TextView

        init {
            itemMenu = itemView.findViewById(R.id.ivUpdate_Delete_Menu_FeedInventoryAdditionLayout)
            itemDate = itemView.findViewById(R.id.tvFeedAcquisitionDate_FeedInventoryAdditionLayout)
            itemCost = itemView.findViewById(R.id.tvFeedCost_FeedInventoryAdditionLayout)
            itemFeedName = itemView.findViewById(R.id.tvFeedName_FeedInventoryAdditionLayout)
            itemQuantity = itemView.findViewById(R.id.tvFeedQuantity_FeedInventoryAdditionLayout)
            itemSource = itemView.findViewById(R.id.tvFeedSource_FeedInventoryAdditionLayout)
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.feed_inventory_addition_layout,parent,false)
        return MyViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentFeedInventoryAddition= feedInventoryAdditionList[position]
        val myFormat =  "EEE, dd MMM, yyyy"
        val simpleDateFormat = SimpleDateFormat(myFormat, Locale.UK)
        val currency = myCurrency
        val measuringUnit = myMeasuringUnit
        val measuringUnitValue = if (measuringUnit.contains("k", true)){1.0}else 2.20462


        holder.itemView.apply {
            val quantity =
                "${(currentFeedInventoryAddition.FeedQuantity * measuringUnitValue)} $measuringUnit Added"
            val cost = "$currency ${currentFeedInventoryAddition.FeedCost}"
            val source = "Acquired From ${currentFeedInventoryAddition.FeedSource}"

            holder.itemFeedName.text = currentFeedInventoryAddition.FeedName
            holder.itemQuantity.text = quantity
            holder.itemDate.text = currentFeedInventoryAddition.DateAcquired.let { simpleDateFormat.format(it) }
            holder.itemCost.text = cost
            if (currentFeedInventoryAddition.FeedSource.isEmpty()) {
                holder.itemSource.isGone = true
            }else holder.itemSource.text = source
        }
        holder.itemMenu.setOnClickListener {
            mListener.onDeleteFeedInventoryAdditionClickListener(currentFeedInventoryAddition, position)
        }
        holder.itemView.setOnClickListener {
            mListener.onItemClickListener(currentFeedInventoryAddition)
        }
    }
    override fun getItemCount(): Int {
        return feedInventoryAdditionList.size
    }

    fun setData(feedInventoryAddition: List<FeedInventoryAddition>){
        this.feedInventoryAdditionList = feedInventoryAddition
        notifyDataSetChanged()
    }
}
