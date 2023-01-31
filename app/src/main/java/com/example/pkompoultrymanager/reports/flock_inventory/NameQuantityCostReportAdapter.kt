package com.example.pkompoultrymanager.reports.flock_inventory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.pkompoultrymanager.R

class NameQuantityCostReportAdapter: RecyclerView.Adapter<NameQuantityCostReportAdapter.MyViewHolder>() {

    private var name = emptyList<String>()
    private var quantity = emptyList<Int>()
    private var cost = emptyList<Double>()

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val itemName: TextView
        val itemQuantity: TextView
        val itemCost: TextView
        init {
            itemName = itemView.findViewById(R.id.tvName_NameQuantityCostLayout)
            itemQuantity = itemView.findViewById(R.id.tvQuantity_NameQuantityCostLayout)
            itemCost = itemView.findViewById(R.id.tvCost_NameQuantityCostLayout)
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.name_quantity_cost_layout,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentName= name[position]
        val currentQuantity= quantity[position]
        val currentCost= cost[position]

        holder.itemView.apply {
            val totalBirds = "$currentQuantity birds"
            val totalCost = "GHS $currentCost"
            holder.itemName.text = currentName
            holder.itemQuantity.text = totalBirds
            holder.itemCost.text = totalCost
        }

    }

    override fun getItemCount(): Int {
        return name.size
    }



    fun setData(names: List<String>, quantities: List<Int>, costs: List<Double>){
        this.name = names
        this.quantity = quantities
        this.cost = costs
        notifyDataSetChanged()
    }
}
