package com.example.pkompoultrymanager.transactions.cash_out.loan_repayment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.example.pkompoultrymanager.R
import java.text.SimpleDateFormat
import java.util.*

class LoanRepaymentAdapter(value:String): RecyclerView.Adapter<LoanRepaymentAdapter.MyViewHolder>() {
    private lateinit var mListener: OnItemClickListener
    private val myCurrency = value


    interface OnItemClickListener{
        fun onDeleteLoanRepaymentClickListener(loanRepayment: LoanRepayment, position: Int)
        fun onItemClickListener(loanRepayment: LoanRepayment)
    }
    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener=listener
    }

    private var loanRepaymentList = emptyList<LoanRepayment>()


    inner class MyViewHolder(itemView: View, listener: OnItemClickListener): RecyclerView.ViewHolder(itemView){
        val itemMenu: ImageView
        val itemLender: TextView
        val itemLoanRepaymentDate: TextView
        val itemLoanRepaymentAmount: TextView
        val itemPaymentMethod: TextView
        val itemTransactionId: TextView

        init {
            itemMenu = itemView.findViewById(R.id.ivExpensesImage_ExpensesLayout)
            itemLoanRepaymentDate = itemView.findViewById(R.id.tvExpensesDate_ExpensesLayout)
            itemLoanRepaymentAmount = itemView.findViewById(R.id.tvExpenseAmount_ExpensesLayout)
            itemTransactionId = itemView.findViewById(R.id.tvTransactionID_ExpenseLayout)
            itemLender = itemView.findViewById(R.id.tvExpenseName_ExpensesLayout)
            itemPaymentMethod = itemView.findViewById(R.id.tvPaymentMethod_ExpensesLayout)
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.expenses_layout,parent,false)
        return MyViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentLoanRepayment= loanRepaymentList[position]
        val myFormat =  "EEE, dd MMM, yyyy"
        val simpleDateFormat = SimpleDateFormat(myFormat, Locale.UK)

        holder.itemView.apply {
            val amount = "$myCurrency ${currentLoanRepayment.AmountRepaid}"
            val transactionId = "Transaction ID: ${currentLoanRepayment.TransactionID}"
            val paymentMethod = "Payed via ${currentLoanRepayment.PaymentMethod}"

            holder.itemLender.text= currentLoanRepayment.Lender
            holder.itemLoanRepaymentDate.text = simpleDateFormat.format(currentLoanRepayment.Date)

            if(currentLoanRepayment.TransactionID.isEmpty()){
                holder.itemTransactionId.isGone = true}
            else {
                holder.itemTransactionId.text = transactionId
            }
            if(currentLoanRepayment.PaymentMethod.isEmpty()){
                holder.itemPaymentMethod.isGone = true}
            else {
                holder.itemPaymentMethod.text = paymentMethod
            }
            holder.itemLoanRepaymentAmount.text = amount
        }
        holder.itemMenu.setOnClickListener {
            mListener.onDeleteLoanRepaymentClickListener(currentLoanRepayment, position)
        }
        holder.itemView.setOnClickListener {
            mListener.onItemClickListener(currentLoanRepayment)
        }
    }

    override fun getItemCount(): Int {
        return loanRepaymentList.size
    }

    fun setData(loanRepayment: List<LoanRepayment>){
        this.loanRepaymentList = loanRepayment
        notifyDataSetChanged()
    }
}
