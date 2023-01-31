package com.example.pkompoultrymanager.reports.flock_inventory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.pkompoultrymanager.R

class DateQuantityReportAdapter(number: Int): RecyclerView.Adapter<DateQuantityReportAdapter.MyViewHolder>() {
    private lateinit var mListener: OnItemClickListener
    private val numberOfEggsPerCrates = number

    interface OnItemClickListener{
        fun onItemDateClickListener(date: String)
        fun onItemQuantityClickListener(quantity: Int)
    }
    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener=listener
    }

    private var date = emptyList<String>()
    private var quantity = emptyList<Int>()

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
            val totalBirds = "$currentQuantity birds"
            holder.itemDate.text = currentDate
            holder.itemQuantity.text = totalBirds
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

    fun setData(dates: List<String>, quantities: List<Int>){
        this.date = dates
        this.quantity = quantities
        notifyDataSetChanged()
    }
}
