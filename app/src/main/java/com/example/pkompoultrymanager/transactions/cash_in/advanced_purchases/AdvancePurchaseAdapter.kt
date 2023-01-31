package com.example.pkompoultrymanager.transactions.cash_in.advanced_purchases

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.example.pkompoultrymanager.R
import java.text.SimpleDateFormat
import java.util.*

class AdvancePurchaseAdapter(value: String): RecyclerView.Adapter<AdvancePurchaseAdapter.MyViewHolder>() {
    private lateinit var mListener: OnItemClickListener
    private val myCurrency = value


    interface OnItemClickListener{
        fun onDeleteAdvancePurchaseClickListener(advancePurchase: AdvancePurchase, position: Int)
        fun onItemClickListener(advancePurchase: AdvancePurchase)
    }
    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener=listener
    }

    private var advancePurchaseList = emptyList<AdvancePurchase>()


    inner class MyViewHolder(itemView: View, listener: OnItemClickListener): RecyclerView.ViewHolder(itemView){
        val itemMenu: ImageView
        val itemAmountReceived: TextView
        val itemDate: TextView
        val itemProduct: TextView
        val itemReceiptId: TextView
        val itemSupplyStatus: TextView
        //val itemCurrencyUnit:TextView

        init {
            itemMenu = itemView.findViewById(R.id.ivUpdate_Delete_Menu_AdvancePurchaseLayout)
            itemDate = itemView.findViewById(R.id.tvAdvancePurchaseDate_AdvancePurchaseLayout)
            itemProduct = itemView.findViewById(R.id.tvItemPurchased_AdvancePurchaseLayout)
            itemReceiptId = itemView.findViewById(R.id.tvReceiptNumber_AdvancePurchaseLayout)
            itemSupplyStatus = itemView.findViewById(R.id.tvAdvancePurchaseDeliveryStatus_AdvancePurchaseLayout)
            itemAmountReceived = itemView.findViewById(R.id.tvAmountReceived_AdvancePurchaseLayout)
            //itemCurrencyUnit = itemView.findViewById(R.id.tvAmountReceivedUnit_AdvancePurchaseLayout)
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.advance_purchases_layout,parent,false)
        return MyViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentAdvancePurchase= advancePurchaseList[position]
        val myFormat =  "EEE, dd MMM, yyyy"
        val simpleDateFormat = SimpleDateFormat(myFormat, Locale.UK)

        holder.itemView.apply {
            val date = simpleDateFormat.format(currentAdvancePurchase.Date)
            val suppliedDate = currentAdvancePurchase.ItemDeliveryDate?.let { simpleDateFormat.format(it) }
            val moneyIsReturned = currentAdvancePurchase.MoneyReturned
            val receipt = "Receipt Id: ${currentAdvancePurchase.ReceiptNumber}"
            val amountReceived = "$myCurrency ${currentAdvancePurchase.AmountReceived} received"
            val cost = "$myCurrency ${currentAdvancePurchase.ItemPurchased_Cost}"
            val amountReturned = "$myCurrency ${currentAdvancePurchase.AmountReturned}"
            val hasBeenSupplied = currentAdvancePurchase.HasBeenDelivered
            val notSupplied = "Products not Supplied"
            val supplied = "Supplied on $suppliedDate"
            val returned = "$amountReturned returned\nDate: $suppliedDate"
            val products = "${currentAdvancePurchase.ItemPurchased} at $cost"

            if (hasBeenSupplied){
                holder.itemSupplyStatus.setTextColor(resources.getColor(R.color.main_green))
                holder.itemSupplyStatus.text = supplied
            }else if (moneyIsReturned){
                holder.itemSupplyStatus.setTextColor(resources.getColor(R.color.black))
                holder.itemSupplyStatus.text = returned
            }else holder.itemSupplyStatus.text = notSupplied

            holder.itemAmountReceived.text= amountReceived
            holder.itemDate.text = simpleDateFormat.format(currentAdvancePurchase.Date)
            holder.itemProduct.text = products
            if (currentAdvancePurchase.ReceiptNumber.isEmpty()){
                holder.itemReceiptId.isGone = true
            }else holder.itemReceiptId.text = receipt

        }
        holder.itemMenu.setOnClickListener {
            mListener.onDeleteAdvancePurchaseClickListener(currentAdvancePurchase,position)
        }
        holder.itemView.setOnClickListener {
            mListener.onItemClickListener(currentAdvancePurchase)
        }
    }

    override fun getItemCount(): Int {
        return advancePurchaseList.size
    }

    fun setData(advancePurchase: List<AdvancePurchase>){
        this.advancePurchaseList = advancePurchase
        notifyDataSetChanged()
    }
}
