package com.example.pkompoultrymanager.health.vaccination

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.pkompoultrymanager.R
import java.text.SimpleDateFormat
import java.util.*

class VaccinationAdapter(currency: String): RecyclerView.Adapter<VaccinationAdapter.MyViewHolder>() {
    private lateinit var mListener: OnItemClickListener
    private var myCurrency = currency


    interface OnItemClickListener{
        fun onDeleteVaccinationClickListener(vaccination: Vaccination, position: Int)
        fun onItemClickListener(vaccination: Vaccination)
    }
    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener=listener
    }

    private var vaccinationList = emptyList<Vaccination>()


    inner class MyViewHolder(itemView: View, listener: OnItemClickListener): RecyclerView.ViewHolder(itemView){
        val itemMenu: ImageView
        val itemVaccinationName: TextView
        val itemDate: TextView
        val itemVaccinationAdministrator: TextView
        val itemCost: TextView
        val itemDisease: TextView
        val itemFlock: TextView
        val itemNumberOfBirds: TextView

        init {
            itemMenu = itemView.findViewById(R.id.ivUpdate_Delete_Menu_MedLayout)
            itemDate = itemView.findViewById(R.id.tvMedicationDate_MedLayout)
            itemVaccinationAdministrator = itemView.findViewById(R.id.tvMedicationAdministrator_MedLayout)
            itemCost = itemView.findViewById(R.id.tvMedicationCost_MedLayout)
            itemVaccinationName = itemView.findViewById(R.id.tvMedicationName_MedLayout)
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
        val currentVaccination= vaccinationList[position]
        val myFormat =  "EEE, dd MMM, yyyy"
        val simpleDateFormat = SimpleDateFormat(myFormat, Locale.UK)
        val currency = myCurrency

        holder.itemView.apply {
            val name = currentVaccination.vaccinationName
            val date = "Vaccinated against ${ simpleDateFormat.format(currentVaccination.vaccinationDate) }"
            val disease = "Vaccinated Against ${currentVaccination.vaccinatedDisease}"
            val administrator = "Administered by ${currentVaccination.vaccinationAdministrator}"
            val cost = "Vaccination costs $currency ${currentVaccination.vaccinationCost}"
            val flock = "Flock ${currentVaccination.vaccinatedFlock}"
            val number = "${currentVaccination.numberOfVaccinatedBirds} birds vaccinated"
            
            
            holder.itemDisease.text= disease
            holder.itemDate.text = date
            holder.itemVaccinationAdministrator.text = administrator
            holder.itemCost.text = cost
            holder.itemFlock.text = flock
            holder.itemVaccinationName.text = name
            holder.itemNumberOfBirds.text = number
        }
        holder.itemMenu.setOnClickListener {
            mListener.onDeleteVaccinationClickListener(currentVaccination, position)
        }
        holder.itemView.setOnClickListener {
            mListener.onItemClickListener(currentVaccination)
        }
    }

    override fun getItemCount(): Int {
        return vaccinationList.size
    }

    fun setData(vaccination: List<Vaccination>){
        this.vaccinationList = vaccination
        notifyDataSetChanged()
    }
}
