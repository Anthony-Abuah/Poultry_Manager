package com.example.pkompoultrymanager.transactions.cash_in.loans

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.pkompoultrymanager.R
import java.text.SimpleDateFormat
import java.util.*

class LoansAdapter(value:String): RecyclerView.Adapter<LoansAdapter.MyViewHolder>() {
    private lateinit var mListener: OnItemClickListener
    private val myCurrency = value


    interface OnItemClickListener{
        fun onDeleteLoansClickListener(loans: Loans, position: Int)
        fun onItemClickListener(loans: Loans)
    }
    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener=listener
    }

    private var loansList = emptyList<Loans>()


    inner class MyViewHolder(itemView: View, listener: OnItemClickListener): RecyclerView.ViewHolder(itemView){
        val itemMenu: ImageView
        val itemAmountReceived: TextView
        val itemDate: TextView
        val itemLender: TextView
        val itemTransactionId: TextView

        init {
            itemMenu = itemView.findViewById(R.id.ivUpdate_Delete_Menu_LoansAndInvestmentLayout)
            itemDate = itemView.findViewById(R.id.tvLoansAndInvestmentDate_LoansAndInvestmentLayout)
            itemLender = itemView.findViewById(R.id.tvLenderOrInvestor_LoansAndInvestmentLayout)
            itemTransactionId = itemView.findViewById(R.id.tvTransactionID_LoansAndInvestmentLayout)
            itemAmountReceived = itemView.findViewById(R.id.tvAmountReceived_LoansAndInvestmentLayout)
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.loans_and_investment_layout,parent,false)
        return MyViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentLoan= loansList[position]
        val myFormat =  "EEE, dd MMM, yyyy"
        val simpleDateFormat = SimpleDateFormat(myFormat, Locale.UK)

        holder.itemView.apply {
            holder.itemAmountReceived.text= currentLoan.LoanAmountReceived.toString()
            holder.itemDate.text = simpleDateFormat.format(currentLoan.Date)
            holder.itemLender.text = currentLoan.Lender
            holder.itemTransactionId.text = currentLoan.TransactionID
        }
        holder.itemMenu.setOnClickListener {
            mListener.onDeleteLoansClickListener(currentLoan,position)
        }
        holder.itemView.setOnClickListener {
            mListener.onItemClickListener(currentLoan)
        }
    }

    override fun getItemCount(): Int {
        return loansList.size
    }

    fun setData(loans: List<Loans>){
        this.loansList = loans
        notifyDataSetChanged()
    }
}
