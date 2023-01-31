package com.example.pkompoultrymanager.tables.lender_investor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.pkompoultrymanager.R

class LenderInvestorAdapter: RecyclerView.Adapter<LenderInvestorAdapter.MyViewHolder>() {
    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener{
        fun onDeleteLenderInvestorClickListener(lenderInvestor: LenderInvestor,position: Int)
        fun onItemClickListener(lenderInvestor: LenderInvestor)
    }
    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener=listener
    }


    private var lenderInvestorList = emptyList<LenderInvestor>()

    inner class MyViewHolder(itemView: View, listener: OnItemClickListener): RecyclerView.ViewHolder(itemView){
        val itemMenu: ImageView
        val itemLenderInvestorName: TextView
        val itemLenderInvestorContact: TextView
        val itemLenderInvestorLocation: TextView

        init {
            itemMenu = itemView.findViewById(R.id.ivUpdateDeleteMenu_PersonLayout)
            itemLenderInvestorName = itemView.findViewById(R.id.tvPersonName_PersonLayout)
            itemLenderInvestorContact = itemView.findViewById(R.id.tvPersonContact_PersonLayout)
            itemLenderInvestorLocation = itemView.findViewById(R.id.tvPersonLocation_PersonLayout)
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.person_layout,parent,false)
        return MyViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentLenderInvestor= lenderInvestorList[position]

        holder.itemView.apply {
            val name = currentLenderInvestor.LenderInvestorName
            val contact = "Contact: ${currentLenderInvestor.LenderInvestorContact}"
            val location = "Location: ${currentLenderInvestor.LenderInvestorLocation}"

            holder.itemLenderInvestorName.text = name
            holder.itemLenderInvestorContact.text = contact
            holder.itemLenderInvestorLocation.text = location
        }
        holder.itemMenu.setOnClickListener {
            mListener.onDeleteLenderInvestorClickListener(currentLenderInvestor, position)
        }
        holder.itemView.setOnClickListener {
            mListener.onItemClickListener(currentLenderInvestor)
        }
    }

    override fun getItemCount(): Int {
        return lenderInvestorList.size
    }

    fun setData(lenderInvestor: List<LenderInvestor>){
        this.lenderInvestorList = lenderInvestor
        notifyDataSetChanged()
    }
}
