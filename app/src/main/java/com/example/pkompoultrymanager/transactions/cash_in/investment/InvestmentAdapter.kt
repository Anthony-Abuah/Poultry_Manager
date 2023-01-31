package com.example.pkompoultrymanager.transactions.cash_in.investment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.pkompoultrymanager.R
import java.text.SimpleDateFormat
import java.util.*

class InvestmentAdapter(value:String): RecyclerView.Adapter<InvestmentAdapter.MyViewHolder>() {
    private lateinit var mListener: OnItemClickListener
    private val myCurrency = value


    interface OnItemClickListener{
        fun onDeleteInvestmentClickListener(investment: Investment, position: Int)
        fun onItemClickListener(investment: Investment)
    }
    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener=listener
    }

    private var investmentList = emptyList<Investment>()


    inner class MyViewHolder(itemView: View, listener: OnItemClickListener): RecyclerView.ViewHolder(itemView){
        val itemMenu: ImageView
        val itemAmountReceived: TextView
        val itemDate: TextView
        val itemInvestor: TextView
        val itemTransactionId: TextView

        init {
            itemMenu = itemView.findViewById(R.id.ivUpdate_Delete_Menu_LoansAndInvestmentLayout)
            itemDate = itemView.findViewById(R.id.tvLoansAndInvestmentDate_LoansAndInvestmentLayout)
            itemInvestor = itemView.findViewById(R.id.tvLenderOrInvestor_LoansAndInvestmentLayout)
            itemTransactionId = itemView.findViewById(R.id.tvTransactionID_LoansAndInvestmentLayout)
            itemAmountReceived = itemView.findViewById(R.id.tvAmountReceived_LoansAndInvestmentLayout)
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.loans_and_investment_layout,parent,false)
        return MyViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentInvestment= investmentList[position]
        val myFormat =  "EEE, dd MMM, yyyy"
        val simpleDateFormat = SimpleDateFormat(myFormat, Locale.UK)

        holder.itemView.apply {
            holder.itemAmountReceived.text= currentInvestment.AmountInvested.toString()
            holder.itemDate.text = simpleDateFormat.format(currentInvestment.Date)
            holder.itemInvestor.text = currentInvestment.Investor
            holder.itemTransactionId.text = currentInvestment.TransactionID
        }
        holder.itemMenu.setOnClickListener {
            mListener.onDeleteInvestmentClickListener(currentInvestment,position)
        }
        holder.itemView.setOnClickListener {
            mListener.onItemClickListener(currentInvestment)
        }
    }

    override fun getItemCount(): Int {
        return investmentList.size
    }

    fun setData(investment: List<Investment>){
        this.investmentList = investment
        notifyDataSetChanged()
    }
}
