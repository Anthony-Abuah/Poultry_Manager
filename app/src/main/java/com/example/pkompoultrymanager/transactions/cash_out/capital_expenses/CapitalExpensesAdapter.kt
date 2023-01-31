package com.example.pkompoultrymanager.transactions.cash_out.capital_expenses

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.example.pkompoultrymanager.R
import java.text.SimpleDateFormat
import java.util.*

class CapitalExpensesAdapter(value:String): RecyclerView.Adapter<CapitalExpensesAdapter.MyViewHolder>() {
    private lateinit var mListener: OnItemClickListener
    private val myCurrency = value


    interface OnItemClickListener{
        fun onDeleteCapitalExpensesClickListener(capitalExpenses: CapitalExpenses, position: Int)
        fun onItemClickListener(capitalExpenses: CapitalExpenses)
    }
    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener=listener
    }

    private var capitalExpensesList = emptyList<CapitalExpenses>()


    inner class MyViewHolder(itemView: View, listener: OnItemClickListener): RecyclerView.ViewHolder(itemView){
        val itemMenu: ImageView
        val itemExpenseName: TextView
        val itemExpenseDate: TextView
        val itemExpenseAmount: TextView
        val itemPaymentMethod: TextView
        val itemTransactionId: TextView

        init {
            itemMenu = itemView.findViewById(R.id.ivExpensesImage_ExpensesLayout)
            itemExpenseDate = itemView.findViewById(R.id.tvExpensesDate_ExpensesLayout)
            itemExpenseAmount = itemView.findViewById(R.id.tvExpenseAmount_ExpensesLayout)
            itemTransactionId = itemView.findViewById(R.id.tvTransactionID_ExpenseLayout)
            itemExpenseName = itemView.findViewById(R.id.tvExpenseName_ExpensesLayout)
            itemPaymentMethod = itemView.findViewById(R.id.tvPaymentMethod_ExpensesLayout)
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.expenses_layout,parent,false)
        return MyViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentCapitalExpenses= capitalExpensesList[position]
        val myFormat =  "EEE, dd MMM, yyyy"
        val simpleDateFormat = SimpleDateFormat(myFormat, Locale.UK)

        holder.itemView.apply {
            val amount = "$myCurrency ${currentCapitalExpenses.ExpenseAmount}"
            val transactionId = "Transaction ID: ${currentCapitalExpenses.TransactionID}"
            val paymentMethod = "Payed via ${currentCapitalExpenses.PaymentMethod}"
            holder.itemExpenseName.text= currentCapitalExpenses.ExpenseName
            holder.itemExpenseDate.text = simpleDateFormat.format(currentCapitalExpenses.Date)

            if(currentCapitalExpenses.TransactionID.isEmpty()){
                holder.itemTransactionId.isGone = true}
            else {
                holder.itemTransactionId.text = transactionId
            }
            if(currentCapitalExpenses.PaymentMethod.isEmpty()){
                holder.itemPaymentMethod.isGone = true}
            else {
                holder.itemPaymentMethod.text = paymentMethod
            }
            holder.itemExpenseAmount.text = amount
        }
        holder.itemMenu.setOnClickListener {
            mListener.onDeleteCapitalExpensesClickListener(currentCapitalExpenses, position)
        }
        holder.itemView.setOnClickListener {
            mListener.onItemClickListener(currentCapitalExpenses)
        }
    }

    override fun getItemCount(): Int {
        return capitalExpensesList.size
    }

    fun setData(capitalExpenses: List<CapitalExpenses>){
        this.capitalExpensesList = capitalExpenses
        notifyDataSetChanged()
    }
}
