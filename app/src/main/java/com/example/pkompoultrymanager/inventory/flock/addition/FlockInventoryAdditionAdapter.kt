package com.example.pkompoultrymanager.inventory.flock.addition

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.example.pkompoultrymanager.R
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.util.*

class FlockInventoryAdditionAdapter(currency: String): RecyclerView.Adapter<FlockInventoryAdditionAdapter.MyViewHolder>() {
    private lateinit var mListener: OnItemClickListener
    private var myCurrency = currency


    interface OnItemClickListener{
        fun onDeleteFlockInventoryAdditionClickListener(flockInventoryAddition: FlockInventoryAddition, position: Int)
        fun onItemClickListener(flockInventoryAddition: FlockInventoryAddition)
    }
    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener=listener

    }
    private var flockInventoryAdditionList = emptyList<FlockInventoryAddition>()

    inner class MyViewHolder(itemView: View, listener: OnItemClickListener): RecyclerView.ViewHolder(itemView){
        val itemMenu: ImageView
        val itemFlockName: TextView
        val itemFlockQuantity: TextView
        val itemDate: TextView
        val itemFlockCost: TextView
        val itemFlockBreed: TextView
        val itemFlockSource: TextView
        val itemFlockPurpose: TextView
        val itemFlockAge: TextView

        init {
            itemMenu = itemView.findViewById(R.id.ivUpdate_Delete_Menu_FlockAdditionLayout)
            itemDate = itemView.findViewById(R.id.tvAdditionDate_FlockAdditionLayout)
            itemFlockCost = itemView.findViewById(R.id.tvFlockCost_FlockAdditionLayout)
            itemFlockBreed = itemView.findViewById(R.id.tvFlockBreed_FlockAdditionLayout)
            itemFlockSource = itemView.findViewById(R.id.tvFlockSource_FlockAdditionLayout)
            itemFlockName = itemView.findViewById(R.id.tvFlockName_FlockAdditionLayout)
            itemFlockQuantity = itemView.findViewById(R.id.tvFlockQuantity_FlockAdditionLayout)
            itemFlockPurpose = itemView.findViewById(R.id.tvFlockPurpose_FlockAdditionLayout)
            itemFlockAge = itemView.findViewById(R.id.tvFlockAge_FlockAdditionLayout)
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.flock_addition_layout,parent,false)
        return MyViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentFlockInventoryAddition= flockInventoryAdditionList[position]
        val myFormat = "EEE, dd MMM, yyyy"
        val format = "dd-MM-yyyy"
        val simpleDateFormat = SimpleDateFormat(myFormat, Locale.UK)
        val dateFormat = SimpleDateFormat(format, Locale.UK)
        val currency = myCurrency
        val milliSecondsPerMonth = 2592000000
        val milliSecondsPerWeek = 604800000
        val milliSecondsPerDay = 86400000
        val currentCalendar = Calendar.getInstance()
        val currentYear = currentCalendar.get(Calendar.YEAR)
        val currentMonth = currentCalendar.get(Calendar.MONTH)
        val currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH)
        val currentDate = "$currentDay-${currentMonth+1}-$currentYear"
        val dateNow:Date? = dateFormat.parse(currentDate)


        holder.itemView.apply {

            val flockAgeInYears = currentFlockInventoryAddition.FlockAge/52
            val flockAgeInMonths = currentFlockInventoryAddition.FlockAge%52 / 4
            val flockAgeInWeeks = currentFlockInventoryAddition.FlockAge %52 %4

            val month = (currentFlockInventoryAddition.DateAdded.let { (dateNow?.time )?.minus(it.time) })?.div(milliSecondsPerMonth)
            val week =  (currentFlockInventoryAddition.DateAdded.let { (dateNow?.time )?.minus(it.time)?.rem(milliSecondsPerMonth)?.div(milliSecondsPerWeek) })
            val day =  (currentFlockInventoryAddition.DateAdded.let { (dateNow?.time )?.minus(it.time)?.rem(milliSecondsPerMonth)?.rem(milliSecondsPerWeek)?.div(milliSecondsPerDay)})
            val totalMonths = ((flockAgeInWeeks + week!!)/4) + flockAgeInMonths + month!!
            val finalMonth = totalMonths.rem(12)
            val finalWeek = ((flockAgeInWeeks + week)%4)
            val finalYear = totalMonths.div(12) + flockAgeInYears
            val theMonth = if (finalMonth <1)"" else if(finalMonth<2)"$finalMonth month" else "$finalMonth months"
            val theWeek = if (finalMonth<1){if (finalWeek < 1) "" else if(finalWeek<2)"$finalWeek week" else "$finalWeek weeks"} else {if (finalWeek < 1) "" else if(finalWeek<2)", $finalWeek week" else ", $finalWeek weeks"}
            val theDay = if (day!! < 1)"" else if(day<2)" and $day day" else " and $day days"
            val theYear = if (finalYear <1)"" else if(finalYear<2)"$finalYear year" else "$finalYear years"
            val age = if (finalYear>0){"Present Age: $theYear, $theMonth$theWeek"} else {"Present Age: $theMonth$theWeek$theDay"}

            val flockName = currentFlockInventoryAddition.FlockName

            val quantity = if(currentFlockInventoryAddition.FlockAge>6)"${currentFlockInventoryAddition.FlockQuantity} birds" else "${currentFlockInventoryAddition.FlockQuantity} chicks"
            val quantityAge = if(currentFlockInventoryAddition.FlockAge==1)"$quantity at ${currentFlockInventoryAddition.FlockAge} week old" else "$quantity at ${currentFlockInventoryAddition.FlockAge} weeks old"

            val purpose = "Purpose: ${currentFlockInventoryAddition.FlockPurpose}"
            val cost = "$currency ${currentFlockInventoryAddition.FlockCost}"
            val source = "Purchased From ${currentFlockInventoryAddition.FlockSource}"
            val breed = "Breed: ${currentFlockInventoryAddition.FlockBreed}"
            val date = "Acquired on ${currentFlockInventoryAddition.DateAdded.let{simpleDateFormat.format(it)}}"

            holder.itemFlockBreed.isGone = currentFlockInventoryAddition.FlockBreed.isEmpty()
            holder.itemFlockSource.isGone = currentFlockInventoryAddition.FlockSource.isEmpty()

            holder.itemFlockName.text= flockName
            holder.itemFlockQuantity.text= quantityAge
            holder.itemDate.text = date
            holder.itemFlockBreed.text = breed
            holder.itemFlockSource.text = source
            holder.itemFlockPurpose.text = purpose
            holder.itemFlockCost.text = cost
            holder.itemFlockAge.text = age

        }
        holder.itemMenu.setOnClickListener {
            mListener.onDeleteFlockInventoryAdditionClickListener(currentFlockInventoryAddition, position)
        }
        holder.itemView.setOnClickListener {
            mListener.onItemClickListener(currentFlockInventoryAddition)
        }
    }
    override fun getItemCount(): Int {
        return flockInventoryAdditionList.size
    }

    fun setData(flockInventoryAddition: List<FlockInventoryAddition>){
        this.flockInventoryAdditionList = flockInventoryAddition
        notifyDataSetChanged()
    }
}

