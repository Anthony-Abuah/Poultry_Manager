package com.example.pkompoultrymanager.tables.flock_source

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.pkompoultrymanager.R

class FlockSourceAdapter: RecyclerView.Adapter<FlockSourceAdapter.MyViewHolder>() {
    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener{
        fun onDeleteFlockSourceClickListener(flockSource: FlockSource, position: Int)
        fun onItemClickListener(flockSource: FlockSource)
    }
    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener=listener
    }


    private var flockSourceList = emptyList<FlockSource>()

    inner class MyViewHolder(itemView: View, listener: OnItemClickListener): RecyclerView.ViewHolder(itemView){
        val itemMenu: ImageView
        val itemFlockSourceName: TextView
        val itemFlockSourceContact: TextView
        val itemFlockSourceLocation: TextView

        init {
            itemMenu = itemView.findViewById(R.id.ivUpdateDeleteMenu_PersonLayout)
            itemFlockSourceName = itemView.findViewById(R.id.tvPersonName_PersonLayout)
            itemFlockSourceContact = itemView.findViewById(R.id.tvPersonContact_PersonLayout)
            itemFlockSourceLocation = itemView.findViewById(R.id.tvPersonLocation_PersonLayout)
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.person_layout,parent,false)
        return MyViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentFlockSource= flockSourceList[position]

        holder.itemView.apply {
            val name = currentFlockSource.FlockSourceName
            val contact = "Contact: ${currentFlockSource.FlockSourceContact}"
            val location = "Location: ${currentFlockSource.FlockSourceLocation}"

            holder.itemFlockSourceName.text = name
            holder.itemFlockSourceContact.text = contact
            holder.itemFlockSourceLocation.text = location
        }
        holder.itemMenu.setOnClickListener {
            mListener.onDeleteFlockSourceClickListener(currentFlockSource, position)
        }
        holder.itemView.setOnClickListener {
            mListener.onItemClickListener(currentFlockSource)
        }
    }

    override fun getItemCount(): Int {
        return flockSourceList.size
    }

    fun setData(flockSource: List<FlockSource>){
        this.flockSourceList = flockSource
        notifyDataSetChanged()
    }
}
