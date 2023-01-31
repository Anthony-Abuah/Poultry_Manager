package com.example.pkompoultrymanager.inventory.eggs.addition

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import com.example.pkompoultrymanager.R
import java.text.SimpleDateFormat
import java.util.*

class EggInventoryAdditionAdapter(eggsPerCrate: Int): RecyclerView.Adapter<EggInventoryAdditionAdapter.MyViewHolder>() {
    private lateinit var mListener: OnItemClickListener
    private var numberOfEggs = eggsPerCrate

    interface OnItemClickListener{
        fun onDeleteEggInventoryAdditionClickListener(eggInventoryAddition: EggInventoryAddition, position: Int)
        fun onItemClickListener(eggInventoryAddition: EggInventoryAddition)
    }
    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener=listener

    }


    private var eggInventoryAdditionList = emptyList<EggInventoryAddition>()

    inner class MyViewHolder(itemView: View, listener: OnItemClickListener): RecyclerView.ViewHolder(itemView){
        val itemMenu: ImageView
        val itemEggType: TextView
        val itemNumberOfEggs: TextView
        val itemDate: TextView
        val itemFlock: TextView

        init {
            itemMenu = itemView.findViewById(R.id.ivUpdate_Delete_Menu_EggsAdditionLayout)
            itemDate = itemView.findViewById(R.id.tvEggsCollectionDate_EggsAdditionLayout)
            itemFlock = itemView.findViewById(R.id.tvFlock_EggsAdditionLayout)
            itemEggType = itemView.findViewById(R.id.tvEggType_EggsAdditionLayout)
            itemNumberOfEggs = itemView.findViewById(R.id.tvNumberOfEggs_EggsAdditionLayout)
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.eggs_addition_layout,parent,false)
        return MyViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentEggInventoryAddition= eggInventoryAdditionList[position]
        val myFormat =  "EEE, dd MMM, yyyy"
        val simpleDateFormat = SimpleDateFormat(myFormat, Locale.UK)
        val numberOfEggsPerCrate = numberOfEggs
        holder.itemView.apply {

            if(currentEggInventoryAddition.EggType.contains("intact", true))
            {
                holder.itemEggType.setTextColor(resources.getColor(R.color.main_green))
            }
            else if(currentEggInventoryAddition.EggType.contains("cracked", true))
            {
                holder.itemEggType.setTextColor(resources.getColor(R.color.brown))
            }
            else if(currentEggInventoryAddition.EggType.contains("damaged", true))
            {
                holder.itemEggType.setTextColor(resources.getColor(R.color.red))
            }
            else {
                holder.itemEggType.setTextColor(resources.getColor(R.color.light_black))

            }
            val numberOfEggs = currentEggInventoryAddition.EggQuantity
            val numberOfEggCrates = (numberOfEggs.div(numberOfEggsPerCrate))
            val numberOfEggsNotInCrates = if(numberOfEggs%numberOfEggsPerCrate == 1){"1 egg"} else {"${numberOfEggs.rem(numberOfEggsPerCrate)} eggs"}
            val date = currentEggInventoryAddition.DateCollected.let { simpleDateFormat.format(it) }
            val flock = "From ${currentEggInventoryAddition.Flock}"

            val theNumberOfEggs = if(numberOfEggCrates < 1) numberOfEggsNotInCrates else {"$numberOfEggCrates crates, $numberOfEggsNotInCrates"}

            holder.itemEggType.text= currentEggInventoryAddition.EggType
            holder.itemNumberOfEggs.text = theNumberOfEggs
            holder.itemDate.text = date
            holder.itemFlock.text = flock
        }
        holder.itemMenu.setOnClickListener {
            mListener.onDeleteEggInventoryAdditionClickListener(currentEggInventoryAddition, position)
        }
        holder.itemView.setOnClickListener {
            mListener.onItemClickListener(currentEggInventoryAddition)
        }
    }

    override fun getItemCount(): Int {
        return eggInventoryAdditionList.size
    }

    fun setData(eggInventoryAddition: List<EggInventoryAddition>){
        this.eggInventoryAdditionList = eggInventoryAddition
        notifyDataSetChanged()
    }
}
