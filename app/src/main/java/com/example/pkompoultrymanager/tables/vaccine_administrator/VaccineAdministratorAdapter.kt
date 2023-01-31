package com.example.pkompoultrymanager.tables.vaccine_administrator

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.pkompoultrymanager.R
import com.example.pkompoultrymanager.tables.customer.VaccineAdministrator

class VaccineAdministratorAdapter: RecyclerView.Adapter<VaccineAdministratorAdapter.MyViewHolder>() {
    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener{
        fun onDeleteVaccineAdministratorClickListener(vaccineAdministrator: VaccineAdministrator, position: Int)
        fun onItemClickListener(vaccineAdministrator: VaccineAdministrator)
    }
    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener=listener
    }


    private var vaccineAdministratorList = emptyList<VaccineAdministrator>()

    inner class MyViewHolder(itemView: View, listener: OnItemClickListener): RecyclerView.ViewHolder(itemView){
        val itemMenu: ImageView
        val itemVaccineAdministratorName: TextView
        val itemVaccineAdministratorContact: TextView
        val itemVaccineAdministratorLocation: TextView

        init {
            itemMenu = itemView.findViewById(R.id.ivUpdateDeleteMenu_PersonLayout)
            itemVaccineAdministratorName = itemView.findViewById(R.id.tvPersonName_PersonLayout)
            itemVaccineAdministratorContact = itemView.findViewById(R.id.tvPersonContact_PersonLayout)
            itemVaccineAdministratorLocation = itemView.findViewById(R.id.tvPersonLocation_PersonLayout)
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.person_layout,parent,false)
        return MyViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentVaccineAdministrator= vaccineAdministratorList[position]

        holder.itemView.apply {
            val name = currentVaccineAdministrator.AdministratorName
            val contact = "Contact: ${currentVaccineAdministrator.AdministratorContact}"
            val location = "Location: ${currentVaccineAdministrator.AdministratorLocation}"

            holder.itemVaccineAdministratorName.text = name
            holder.itemVaccineAdministratorContact.text = contact
            holder.itemVaccineAdministratorLocation.text = location
        }
        holder.itemMenu.setOnClickListener {
            mListener.onDeleteVaccineAdministratorClickListener(currentVaccineAdministrator, position)
        }
        holder.itemView.setOnClickListener {
            mListener.onItemClickListener(currentVaccineAdministrator)
        }
    }

    override fun getItemCount(): Int {
        return vaccineAdministratorList.size
    }

    fun setData(vaccineAdministrator: List<VaccineAdministrator>){
        this.vaccineAdministratorList = vaccineAdministrator
        notifyDataSetChanged()
    }
}
