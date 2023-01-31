package com.example.pkompoultrymanager.transactions.cash_in.alternative_income

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.pkompoultrymanager.R
import java.text.SimpleDateFormat
import java.util.*

class AlternativeIncomeAdapter: RecyclerView.Adapter<AlternativeIncomeAdapter.MyViewHolder>() {
    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener{
        fun onDeleteAlternativeIncomeClickListener(alternativeIncome: AlternativeIncome)
        fun onItemClickListener(alternativeIncome: AlternativeIncome)
    }
    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener=listener
    }


    private var alternativeIncomeList = emptyList<AlternativeIncome>()

    inner class MyViewHolder(itemView: View, listener: OnItemClickListener): RecyclerView.ViewHolder(itemView){
        val itemMenu: ImageView
        val itemAlternativeIncomeID: TextView
        val itemAlternativeIncomeDate: TextView
        val itemAlternativeIncomeItemPurchased: TextView
        val itemAlternativeIncomeCustomer: TextView
        val itemAlternativeIncomeProductCost: TextView
        val itemAlternativeIncomeAmountReceived: TextView
        val itemAlternativeIncomeAmountOwed: TextView

        init {
            itemMenu = itemView.findViewById(R.id.ivUpdate_Delete_Menu_AlternativeIncomeLayout)
            itemAlternativeIncomeDate = itemView.findViewById(R.id.tvAlternativeIncomeDate_AlternativeIncomeLayout)
            itemAlternativeIncomeItemPurchased = itemView.findViewById(R.id.tvProductName_AlternativeIncomeLayout)
            itemAlternativeIncomeCustomer = itemView.findViewById(R.id.tvCustomer_AlternativeIncomeLayout)
            itemAlternativeIncomeID = itemView.findViewById(R.id.tvAlternativeIncomeId_AlternativeIncomeLayout)
            itemAlternativeIncomeAmountReceived = itemView.findViewById(R.id.tvAmountTheCustomerPaid_AlternativeIncomeLayout)
            itemAlternativeIncomeAmountOwed = itemView.findViewById(R.id.tvAmountOwed_AlternativeIncomeLayout)
            itemAlternativeIncomeProductCost = itemView.findViewById(R.id.tvProductCost_AlternativeIncomeLayout)
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.alternative_income_layout,parent,false)
        return MyViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentAlternativeIncome= alternativeIncomeList[position]
        val myFormat =  "dd-MM-yyyy"
        val simpleDateFormat = SimpleDateFormat(myFormat, Locale.UK)

        holder.itemView.apply {
            holder.itemAlternativeIncomeID.text= currentAlternativeIncome.AlternativeIncomeId.toString()
            holder.itemAlternativeIncomeDate.text = simpleDateFormat.format(currentAlternativeIncome.Date)
            holder.itemAlternativeIncomeItemPurchased.text = currentAlternativeIncome.ItemPurchased
            holder.itemAlternativeIncomeCustomer.text = currentAlternativeIncome.Customer
            holder.itemAlternativeIncomeAmountReceived.text = currentAlternativeIncome.AmountReceived.toString()
            holder.itemAlternativeIncomeAmountOwed.text = currentAlternativeIncome.AmountOwed.toString()
            holder.itemAlternativeIncomeProductCost.text = currentAlternativeIncome.ItemPurchased_Cost.toString()
        }
        holder.itemMenu.setOnClickListener {
            mListener.onDeleteAlternativeIncomeClickListener(currentAlternativeIncome)
        }
        holder.itemView.setOnClickListener {
            mListener.onItemClickListener(currentAlternativeIncome)
        }
    }

    override fun getItemCount(): Int {
        return alternativeIncomeList.size
    }

    fun setData(alternativeIncome: List<AlternativeIncome>){
        this.alternativeIncomeList = alternativeIncome
        notifyDataSetChanged()
    }
}
