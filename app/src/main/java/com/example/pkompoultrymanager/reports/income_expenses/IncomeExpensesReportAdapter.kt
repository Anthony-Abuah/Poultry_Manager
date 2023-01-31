package com.example.pkompoultrymanager.reports.income_expenses

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.pkompoultrymanager.R
import com.example.pkompoultrymanager.inventory.eggs.addition.EggInventoryAddition
import java.util.*

class IncomeExpensesReportAdapter(number: String): RecyclerView.Adapter<IncomeExpensesReportAdapter.MyViewHolder>() {
    private lateinit var mListener: OnItemClickListener
    private val numberOfEggsPerCrates = number

    interface OnItemClickListener{
        fun onItemItemClickListener(item: String)
        fun onItemQuantityClickListener(quantity: Int)
        fun onItemCostClickListener(cost: Double)
    }
    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener=listener
    }

    private var item = emptyList<String>()
    private var quantity = emptyList<Int>()
    private var cost = emptyList<Double>()

    inner class MyViewHolder(itemView: View, listener: OnItemClickListener): RecyclerView.ViewHolder(itemView){
        val itemItem: TextView
        val itemQuantity: TextView
        val itemCost: TextView
        init {
            itemItem = itemView.findViewById(R.id.tvItem_ItemQuantityCostLayout)
            itemQuantity = itemView.findViewById(R.id.tvQuantity_ItemQuantityCostLayout)
            itemCost = itemView.findViewById(R.id.tvCost_ItemQuantityCostLayout)
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.item_quantity_cost_layout,parent,false)
        return MyViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem= item[position]
        val currentQuantity= quantity[position]
        val currentCost= cost[position]

        holder.itemView.apply {
            val totalBirds = "$currentQuantity birds"
            holder.itemItem.text = currentItem
            holder.itemQuantity.text = totalBirds
            holder.itemCost.text = currentCost.toString()
        }

        holder.itemItem.setOnClickListener {
            mListener.onItemItemClickListener(currentItem)
        }
        holder.itemQuantity.setOnClickListener {
            mListener.onItemQuantityClickListener(currentQuantity)
        }
        holder.itemCost.setOnClickListener {
            mListener.onItemCostClickListener(currentCost)
        }
    }

    override fun getItemCount(): Int {
        return item.size
    }

    fun setData(items: List<String>, quantities: List<Int>, costs: List<Double>){
        this.item = items
        this.quantity = quantities
        this.cost = costs
        notifyDataSetChanged()
    }
}
