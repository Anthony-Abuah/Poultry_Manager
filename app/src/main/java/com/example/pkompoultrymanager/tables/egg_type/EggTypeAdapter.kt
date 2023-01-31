package com.example.pkompoultrymanager.tables.egg_type

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.pkompoultrymanager.R

class EggTypeAdapter: RecyclerView.Adapter<EggTypeAdapter.MyViewHolder>() {
    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener{
        fun onDeleteEggTypeClickListener(eggType: EggType)
        fun onItemClickListener(eggType: EggType)
    }
    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener=listener
    }


    private var eggTypeList = emptyList<EggType>()

    inner class MyViewHolder(itemView: View, listener: OnItemClickListener): RecyclerView.ViewHolder(itemView){
        val itemMenu: ImageView
        val itemEggTypeName: TextView
        init {
            itemMenu = itemView.findViewById(R.id.ivUpdateDeleteMenu_EggTypeLayout)
            itemEggTypeName = itemView.findViewById(R.id.tvEggTypeName_EggTypeLayout)
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.egg_type_layout,parent,false)
        return MyViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentEggType= eggTypeList[position]

        holder.itemView.apply {
            holder.itemEggTypeName.text = currentEggType.EggTypeName
        }
        holder.itemMenu.setOnClickListener {
            mListener.onDeleteEggTypeClickListener(currentEggType)
        }
        holder.itemView.setOnClickListener {
            mListener.onItemClickListener(currentEggType)
        }
    }

    override fun getItemCount(): Int {
        return eggTypeList.size
    }

    fun setData(eggType: List<EggType>){
        this.eggTypeList = eggType
        notifyDataSetChanged()
    }
}
