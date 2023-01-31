package com.example.pkompoultrymanager.tables.customer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.pkompoultrymanager.R

class CustomerAdapter: RecyclerView.Adapter<CustomerAdapter.MyViewHolder>() {
    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener{
        fun onDeleteCustomerClickListener(customer: Customer)
        fun onItemClickListener(customer: Customer)
    }
    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener=listener
    }


    private var customerList = emptyList<Customer>()

    inner class MyViewHolder(itemView: View, listener: OnItemClickListener): RecyclerView.ViewHolder(itemView){
        val itemMenu: ImageView
        val itemCustomerName: TextView
        val itemCustomerContact: TextView
        val itemCustomerLocation: TextView

        init {
            itemMenu = itemView.findViewById(R.id.ivUpdateDeleteMenu_PersonLayout)
            itemCustomerName = itemView.findViewById(R.id.tvPersonName_PersonLayout)
            itemCustomerContact = itemView.findViewById(R.id.tvPersonContact_PersonLayout)
            itemCustomerLocation = itemView.findViewById(R.id.tvPersonLocation_PersonLayout)
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.person_layout,parent,false)
        return MyViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentCustomer= customerList[position]

        holder.itemView.apply {
            val name = currentCustomer.CustomerName
            val contact = "Contact: ${currentCustomer.CustomerContact}"
            val location = "Location: ${currentCustomer.CustomerLocation}"

            holder.itemCustomerName.text = name
            holder.itemCustomerContact.text = contact
            holder.itemCustomerLocation.text = location
        }
        holder.itemMenu.setOnClickListener {
            mListener.onDeleteCustomerClickListener(currentCustomer)
        }
        holder.itemView.setOnClickListener {
            mListener.onItemClickListener(currentCustomer)
        }
    }

    override fun getItemCount(): Int {
        return customerList.size
    }

    fun setData(customer: List<Customer>){
        this.customerList = customer
        notifyDataSetChanged()
    }
}
