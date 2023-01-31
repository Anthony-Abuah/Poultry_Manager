package com.example.pkompoultrymanager.reports.feed_inventory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.pkompoultrymanager.R
import com.example.pkompoultrymanager.inventory.eggs.addition.EggInventoryAddition
import java.text.SimpleDateFormat
import java.util.*

class FeedQuantityReportAdapter(number: Int): RecyclerView.Adapter<FeedQuantityReportAdapter.MyViewHolder>() {
    private lateinit var mListener: OnItemClickListener
    private val numberOfEggsPerCrates = number

    interface OnItemClickListener{
        fun onItemDateClickListener(date: String)
        fun onItemQuantityClickListener(quantity: Double)
    }
    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener=listener
    }

    private var date = emptyList<String>()
    private var quantity = emptyList<Double>()

    inner class MyViewHolder(itemView: View, listener: OnItemClickListener): RecyclerView.ViewHolder(itemView){
        val itemDate: TextView
        val itemQuantity: TextView
        init {
            itemDate = itemView.findViewById(R.id.tvDate_DateQuantityLayout)
            itemQuantity = itemView.findViewById(R.id.tvQuantity_DateQuantityLayout)
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.date_quantity_layout,parent,false)
        return MyViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentDate= date[position]
        val currentQuantity= quantity[position]

        holder.itemView.apply {
            val numberOfEggCrates = currentQuantity.div(numberOfEggsPerCrates)
            val numberOfEggsRemaining = currentQuantity.rem(numberOfEggsPerCrates)
            val stringNumberOfEggsRemaining = if (numberOfEggsRemaining==1.0)"$numberOfEggsRemaining egg" else "$numberOfEggsRemaining eggs"
            val totalEggsAddedString = if (numberOfEggCrates < 1) {stringNumberOfEggsRemaining} else if(numberOfEggCrates == 1.0) {"$numberOfEggCrates crate and $stringNumberOfEggsRemaining"} else {"$numberOfEggCrates crates and $stringNumberOfEggsRemaining"}

            holder.itemDate.text = currentDate
            holder.itemQuantity.text = currentQuantity.toString()
        }

        holder.itemDate.setOnClickListener {
            mListener.onItemDateClickListener(currentDate)
        }
        holder.itemQuantity.setOnClickListener {
            mListener.onItemQuantityClickListener(currentQuantity)
        }
    }

    override fun getItemCount(): Int {
        return date.size
    }

    fun setData(dates: List<String>, quantities: List<Double>){
        this.date = dates
        this.quantity = quantities
        notifyDataSetChanged()
    }
}
