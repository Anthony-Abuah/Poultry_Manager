package com.example.pkompoultrymanager.inventory.flock.reduction

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.example.pkompoultrymanager.R
import java.text.SimpleDateFormat
import java.util.*

class FlockInventoryReductionAdapter(currency: String): RecyclerView.Adapter<FlockInventoryReductionAdapter.MyViewHolder>() {
    private lateinit var mListener: OnItemClickListener
    private var myCurrency = currency


    interface OnItemClickListener{
        fun onDeleteFlockInventoryReductionClickListener(flockInventoryReduction: FlockInventoryReduction, position: Int)
        fun onItemClickListener(flockInventoryReduction: FlockInventoryReduction)
    }
    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener=listener
    }

    private var flockInventoryReductionList = emptyList<FlockInventoryReduction>()

    inner class MyViewHolder(itemView: View, listener: OnItemClickListener): RecyclerView.ViewHolder(itemView){
        val itemMenu: ImageView
        val itemSalesInfo: TextView
        val itemDate: TextView
        val itemReductionReason: TextView
        val itemFlockName: TextView
        val itemFlockQuantity: TextView

        init {
            itemMenu = itemView.findViewById(R.id.ivUpdate_Delete_Menu_FlockReductionLayout)
            itemDate = itemView.findViewById(R.id.tvFlockReductionDate_FlockReductionLayout)
            itemReductionReason = itemView.findViewById(R.id.tvReductionReason_FlockReductionLayout)
            itemFlockName = itemView.findViewById(R.id.tvFlockName_FlockReductionLayout)
            itemFlockQuantity = itemView.findViewById(R.id.tvFlockQuantity_FlockReductionLayout)
            itemSalesInfo = itemView.findViewById(R.id.tvSalesInfo_FlockReductionLayout)
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.flock_reduction_layout,parent,false)
        return MyViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentFlockInventoryReduction= flockInventoryReductionList[position]
        val myFormat =  "EEE, dd MMM, yyyy"
        val simpleDateFormat = SimpleDateFormat(myFormat, Locale.UK)
        val currency = myCurrency
        holder.itemView.apply {

            val flockName = currentFlockInventoryReduction.FlockName
            val reductionReason = currentFlockInventoryReduction.ReductionReason
            val customer = currentFlockInventoryReduction.Customer
            val reason = if (reductionReason.contains("sold",true)){"Sold to $customer"}else "ReductionReason: $reductionReason"
            val quantity = "${currentFlockInventoryReduction.ReductionQuantity} birds"
            val flockCost = currentFlockInventoryReduction.FlockCost
            val amountPaid = currentFlockInventoryReduction.AmountReceived
            val amountOwed = currentFlockInventoryReduction.AmountOwed
            val salesInfo = if (currentFlockInventoryReduction.ReductionReason.contains("sold", true)){"EggCost: $currency $flockCost \nAmount Paid: $currency $amountPaid \nAmount Owed: $currency $amountOwed"} else ""
            val date = "Reduced on ${currentFlockInventoryReduction.DateReduced.let{simpleDateFormat.format(it)}}"

            holder.itemDate.text = date
            holder.itemReductionReason.text = reason
            holder.itemFlockName.text = flockName
            holder.itemFlockQuantity.text = quantity

            if(currentFlockInventoryReduction.ReductionReason.contains("sold", true)){
                holder.itemSalesInfo.text = salesInfo
            } else holder.itemSalesInfo.isGone = true

            if(amountOwed > 0.0) {holder.itemSalesInfo.setTextColor(resources.getColor(R.color.red)) }
            else {holder.itemSalesInfo.setTextColor(resources.getColor(R.color.main_green))}

        }

        holder.itemMenu.setOnClickListener {
            mListener.onDeleteFlockInventoryReductionClickListener(currentFlockInventoryReduction, position)
        }
        holder.itemView.setOnClickListener {
            mListener.onItemClickListener(currentFlockInventoryReduction)
        }
    }

    override fun getItemCount(): Int {
        return flockInventoryReductionList.size
    }

    fun setData(flockInventoryReduction: List<FlockInventoryReduction>){
        this.flockInventoryReductionList = flockInventoryReduction
        notifyDataSetChanged()
    }
}
