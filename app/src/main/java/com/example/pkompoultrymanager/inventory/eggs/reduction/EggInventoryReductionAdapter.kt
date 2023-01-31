package com.example.pkompoultrymanager.inventory.eggs.reduction

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.example.pkompoultrymanager.R
import java.text.SimpleDateFormat
import java.util.*

class EggInventoryReductionAdapter(currency: String, numberOfEggs: Int): RecyclerView.Adapter<EggInventoryReductionAdapter.MyViewHolder>() {
    private lateinit var mListener: OnItemClickListener
    private var myCurrency = currency
    private var myNumberOfEggs = numberOfEggs


    interface OnItemClickListener{
        fun onDeleteEggInventoryReductionClickListener(eggInventoryReduction: EggInventoryReduction, position: Int)
        fun onItemClickListener(eggInventoryReduction: EggInventoryReduction)
    }
    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener=listener

    }


    private var eggInventoryReductionList = emptyList<EggInventoryReduction>()

    inner class MyViewHolder(itemView: View, listener: OnItemClickListener): RecyclerView.ViewHolder(itemView){
        val itemMenu: ImageView
        val itemDate: TextView
        val itemEggType: TextView
        val itemNumberOfEggs: TextView
        val itemReductionReason: TextView
        val itemSalesInfo: TextView

        init {
            itemMenu = itemView.findViewById(R.id.ivUpdate_Delete_Menu_EggsReductionLayout)
            itemDate = itemView.findViewById(R.id.tvEggsReductionDate_EggsReductionLayout)
            itemEggType = itemView.findViewById(R.id.tvEggType_EggsReductionLayout)
            itemNumberOfEggs = itemView.findViewById(R.id.tvEggQuantity_EggsReductionLayout)
            itemReductionReason = itemView.findViewById(R.id.tvReductionReason_EggsReductionLayout)
            itemSalesInfo = itemView.findViewById(R.id.tvSalesInfo_EggsReductionLayout)

            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.eggs_reduction_layout,parent,false)
        return MyViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentEggInventoryReduction= eggInventoryReductionList[position]
        val myFormat =  "EEE, dd MMM, yyyy"
        val simpleDateFormat = SimpleDateFormat(myFormat, Locale.UK)
        val numberOfEggsPerCrate = myNumberOfEggs
        val currency = myCurrency

        holder.itemView.apply {
            val eggType = currentEggInventoryReduction.EggType
            val customer = currentEggInventoryReduction.Customer
            val reductionReason =if(currentEggInventoryReduction.ReductionReason.contains("sold", true)) "Sold to $customer" else currentEggInventoryReduction.ReductionReason
            val eggCost = currentEggInventoryReduction.EggCost
            val amountPaid = currentEggInventoryReduction.AmountPaid
            val amountOwed = currentEggInventoryReduction.AmountOwed

            if(amountOwed >0.0) {holder.itemSalesInfo.setTextColor(resources.getColor(R.color.red)) }
            else {holder.itemSalesInfo.setTextColor(resources.getColor(R.color.main_green))}

            val salesInfo = if (currentEggInventoryReduction.ReductionReason.contains("sold", true)){"EggCost: $currency $eggCost \nAmount Paid: $currency $amountPaid \nAmount Owed: $currency $amountOwed"} else ""
            val numberOfEggs = currentEggInventoryReduction.NumberOfEggs
            val numberOfEggCrates = (numberOfEggs.div(numberOfEggsPerCrate))
            val numberOfEggsNotInCrates = if(numberOfEggs%numberOfEggsPerCrate == 1){"1 egg"} else {"${numberOfEggs.rem(numberOfEggsPerCrate)} eggs"}
            val date = currentEggInventoryReduction.DateReduced.let{simpleDateFormat.format(it)}
            val theNumberOfEggs = if(numberOfEggCrates < 1) numberOfEggsNotInCrates else {"$numberOfEggCrates crates, $numberOfEggsNotInCrates"}


            holder.itemDate.text = date
            holder.itemEggType.text = eggType
            holder.itemNumberOfEggs.text = theNumberOfEggs
            holder.itemReductionReason.text = reductionReason

            if(currentEggInventoryReduction.ReductionReason.contains("sold", true)){
            holder.itemSalesInfo.text = salesInfo
           } else holder.itemSalesInfo.isGone = true

            holder.itemMenu.setOnClickListener {
                mListener.onDeleteEggInventoryReductionClickListener(currentEggInventoryReduction, position)
            }
            holder.itemView.setOnClickListener {
                mListener.onItemClickListener(currentEggInventoryReduction)
            }
    }
    }

    override fun getItemCount(): Int {
        return eggInventoryReductionList.size
    }

    fun setData(eggInventoryReduction: List<EggInventoryReduction>){
        this.eggInventoryReductionList = eggInventoryReduction
        notifyDataSetChanged()
    }
}
