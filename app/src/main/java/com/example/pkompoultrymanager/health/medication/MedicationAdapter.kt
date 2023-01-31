package com.example.pkompoultrymanager.health.medication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.pkompoultrymanager.R
import java.text.SimpleDateFormat
import java.util.*

class MedicationAdapter(currency: String): RecyclerView.Adapter<MedicationAdapter.MyViewHolder>() {
    private lateinit var mListener: OnItemClickListener

    private val myCurrency = currency

    interface OnItemClickListener{
        fun onDeleteMedicationClickListener(medication: Medication, position: Int)
        fun onItemClickListener(medication: Medication)
    }
    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener=listener
    }

    private var medicationList = emptyList<Medication>()


    inner class MyViewHolder(itemView: View, listener: OnItemClickListener): RecyclerView.ViewHolder(itemView){
        val itemMenu: ImageView
        val itemMedicationName: TextView
        val itemDate: TextView
        val itemMedicationAdministrator: TextView
        val itemCost: TextView
        val itemDisease: TextView
        val itemFlock: TextView
        val itemNumberOfBirds: TextView

        init {
            itemMenu = itemView.findViewById(R.id.ivUpdate_Delete_Menu_MedLayout)
            itemDate = itemView.findViewById(R.id.tvMedicationDate_MedLayout)
            itemMedicationAdministrator = itemView.findViewById(R.id.tvMedicationAdministrator_MedLayout)
            itemCost = itemView.findViewById(R.id.tvMedicationCost_MedLayout)
            itemMedicationName = itemView.findViewById(R.id.tvMedicationName_MedLayout)
            itemDisease = itemView.findViewById(R.id.tvMedicatedDisease_MedLayout)
            itemFlock = itemView.findViewById(R.id.tvMedicatedFlock_MedLayout)
            itemNumberOfBirds = itemView.findViewById(R.id.tvNumberOfBirds_MedLayout)
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.medication_layout,parent,false)
        return MyViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentMedication= medicationList[position]
        val myFormat =  "EEE, dd MMM, yyyy"
        val simpleDateFormat = SimpleDateFormat(myFormat, Locale.UK)
        val currency = myCurrency

        holder.itemView.apply {
            val name = currentMedication.medicationName
            val date = "Medicated on ${ simpleDateFormat.format(currentMedication.medicationDate) }"
            val disease = "Medicated against ${currentMedication.medicatedDisease}"
            val administrator = "Administered by ${currentMedication.medicationAdministrator}"
            val cost = "Medication costs $currency ${currentMedication.medicationCost}"
            val flock = "Flock ${currentMedication.medicatedFlock}"
            val number = "${currentMedication.numberOfMedicatedBirds} birds medicated"



            holder.itemDisease.text= disease
            holder.itemDate.text = date
            holder.itemMedicationAdministrator.text = administrator
            holder.itemCost.text = cost
            holder.itemFlock.text = flock
            holder.itemMedicationName.text = name
            holder.itemNumberOfBirds.text = number
        }
        holder.itemMenu.setOnClickListener {
            mListener.onDeleteMedicationClickListener(currentMedication, position)
        }
        holder.itemView.setOnClickListener {
            mListener.onItemClickListener(currentMedication)
        }
    }

    override fun getItemCount(): Int {
        return medicationList.size
    }

    fun setData(medication: List<Medication>){
        this.medicationList = medication
        notifyDataSetChanged()
    }
}
