package com.example.pkompoultrymanager.inventory.feed.reduction

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.pkompoultrymanager.R
import java.text.SimpleDateFormat
import java.util.*

class FeedInventoryReductionAdapter(measuringUnit: String) :
    RecyclerView.Adapter<FeedInventoryReductionAdapter.MyViewHolder>() {
    private lateinit var mListener: OnItemClickListener
    private var myMeasuringUnit = measuringUnit


    interface OnItemClickListener {
        fun onDeleteFeedInventoryReductionClickListener(feedInventoryReduction: FeedInventoryReduction, position: Int)
        fun onItemClickListener(feedInventoryReduction: FeedInventoryReduction)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener

    }


    private var feedInventoryReductionList = emptyList<FeedInventoryReduction>()

    inner class MyViewHolder(itemView: View, listener: OnItemClickListener) :
        RecyclerView.ViewHolder(itemView) {
        val itemMenu: ImageView
        val itemFlock: TextView
        val itemDate: TextView
        val itemFeedName: TextView
        val itemReductionQuantity: TextView

        init {
            itemMenu = itemView.findViewById(R.id.ivEdit_Delete_Menu_FeedInventoryReductionLayout)
            itemDate = itemView.findViewById(R.id.tvFeedReductionDate_FeedInventoryReductionLayout)
            itemFeedName = itemView.findViewById(R.id.tvFeedName_FeedInventoryReductionLayout)
            itemReductionQuantity = itemView.findViewById(R.id.tvFeedReductionQuantity_FeedInventoryReductionLayout)
            itemFlock = itemView.findViewById(R.id.tvFlockName_FeedInventoryReductionLayout)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.feed_inventory_reduction_layout, parent, false)
        return MyViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentFeedInventoryReduction = feedInventoryReductionList[position]
        val myFormat = "EEE, dd MMM, yyyy"
        val simpleDateFormat = SimpleDateFormat(myFormat, Locale.UK)
        val measuringUnit = myMeasuringUnit
        val measuringUnitValue = if (measuringUnit=="kg"){1.0}else 2.20462

        holder.itemView.apply {
            val date =currentFeedInventoryReduction.ReductionDate.let { simpleDateFormat.format(it)}
            val reductionReason =currentFeedInventoryReduction.ReductionReason
            val quantity ="${currentFeedInventoryReduction.ReductionQuantity} $measuringUnit reduced"
            val flockName =currentFeedInventoryReduction.FlockName
            val flockFed = if (reductionReason.contains("feed",true)){"Fed to $flockName"} else "Reason: $reductionReason"

            if (reductionReason.contains("feed",true)){
                holder.itemFeedName.setTextColor(resources.getColor(R.color.main_green))
            }else holder.itemFeedName.setTextColor(resources.getColor(R.color.brown))

            holder.itemFlock.text = flockFed
            holder.itemDate.text = date
            holder.itemFeedName.text = currentFeedInventoryReduction.FeedName
            holder.itemReductionQuantity.text = quantity
        }
        holder.itemMenu.setOnClickListener {
            mListener.onDeleteFeedInventoryReductionClickListener(
                currentFeedInventoryReduction, position
            )
        }
        holder.itemView.setOnClickListener {
            mListener.onItemClickListener(currentFeedInventoryReduction)
        }
    }

    override fun getItemCount(): Int {
        return feedInventoryReductionList.size
    }

    fun setData(feedInventoryReduction: List<FeedInventoryReduction>) {
        this.feedInventoryReductionList = feedInventoryReduction
        notifyDataSetChanged()
    }
}

