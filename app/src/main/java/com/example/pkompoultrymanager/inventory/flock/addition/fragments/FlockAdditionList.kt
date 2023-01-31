package com.example.pkompoultrymanager.inventory.flock.addition.fragments

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.get
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pkompoultrymanager.R
import com.example.pkompoultrymanager.inventory.InventoryActivity
import com.example.pkompoultrymanager.inventory.flock.FlockCommunicator
import com.example.pkompoultrymanager.inventory.flock.addition.FlockInventoryAddition
import com.example.pkompoultrymanager.inventory.flock.addition.FlockInventoryAdditionAdapter
import com.example.pkompoultrymanager.inventory.flock.addition.FlockInventoryAdditionViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*

class FlockAdditionList : Fragment() {
    private lateinit var myFarmInfo: SharedPreferences
    private lateinit var mFlockInventoryAdditionViewModel: FlockInventoryAdditionViewModel
    private lateinit var communicator: FlockCommunicator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_flock_addition_list, container, false)

        val numberOfList = 100
        val myFormat = "EEE, dd MMM, yyyy"
        val simpleDateFormat = SimpleDateFormat(myFormat, Locale.UK)

        val systemFormat = "dd-MM-yyyy"
        val systemDateFormat = SimpleDateFormat(systemFormat, Locale.UK)
        // get the date values from calendar instance
        val currentCalendar = Calendar.getInstance()
        val currentYear = currentCalendar.get(Calendar.YEAR)
        val currentMonth = currentCalendar.get(Calendar.MONTH)
        val currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH)
        val currentDayOfWeek = currentCalendar.get(Calendar.DAY_OF_WEEK)

        // put the date values in string format
        val currentDate = "$currentDay-${currentMonth + 1}-$currentYear"
        val previousDay = "${currentDay}-${currentMonth + 1}-$currentYear"
        val previousWeek = "${currentDay - currentDayOfWeek + 1}-${currentMonth + 1}-$currentYear"
        val previousMonth = "${1}-${currentMonth+1}-$currentYear"
        val pastThreeMonths = "${currentDay}-${currentMonth - 2}-$currentYear"
        val pastSixMonths = "${currentDay}-${currentMonth - 5}-$currentYear"
        val pastYear = "${currentDay}-${currentMonth + 1}-${currentYear - 1}"
        val pastTwoYears = "${currentDay}-${currentMonth + 1}-${currentYear - 2}"

        // parse the date into java.util date
        val theCurrentSystemDate: Date? = systemDateFormat.parse(currentDate)
        val thePreviousSystemDay: Date? = systemDateFormat.parse(previousDay)
        val thePreviousSystemWeek: Date? = systemDateFormat.parse(previousWeek)
        val thePreviousSystemMonth: Date? = systemDateFormat.parse(previousMonth)
        val thePreviousThreeSystemMonths: Date? = systemDateFormat.parse(pastThreeMonths)
        val thePreviousSixSystemMonths: Date? = systemDateFormat.parse(pastSixMonths)
        val thePreviousSystemYear: Date? = systemDateFormat.parse(pastYear)
        val thePreviousTwoSystemYears: Date? = systemDateFormat.parse(pastTwoYears)

        // convert the java.util date to java.sql date
        val theCurrentDate = theCurrentSystemDate?.time?.let { java.sql.Date(it) }
        val thePreviousDay = thePreviousSystemDay?.time?.let { java.sql.Date(it) }
        val thePreviousWeek = thePreviousSystemWeek?.time?.let { java.sql.Date(it) }
        val thePreviousMonth = thePreviousSystemMonth?.time?.let { java.sql.Date(it) }
        val thePastThreeMonths = thePreviousThreeSystemMonths?.time?.let { java.sql.Date(it) }
        val thePastSixMonths = thePreviousSixSystemMonths?.time?.let { java.sql.Date(it) }
        val thePastYear = thePreviousSystemYear?.time?.let { java.sql.Date(it) }
        val thePastTwoYears = thePreviousTwoSystemYears?.time?.let { java.sql.Date(it) }

        val dateFilter = view.findViewById<ImageButton>(R.id.ibDateFilter_FlockInventoryAdditionList)

        myFarmInfo = context?.getSharedPreferences("MyFarmInfo", Context.MODE_PRIVATE)!!
        val currency = myFarmInfo.getString("currency", "GH₵")!!

        val flockInventoryAdditionList = view.findViewById<RecyclerView>(R.id.rvFlockAdditionList)
        val adapter = FlockInventoryAdditionAdapter(currency)
        flockInventoryAdditionList.adapter = adapter
        flockInventoryAdditionList.layoutManager = LinearLayoutManager(requireContext())

        mFlockInventoryAdditionViewModel = ViewModelProvider(this)[FlockInventoryAdditionViewModel::class.java]
        mFlockInventoryAdditionViewModel.displayAllFlockInventoryAdditions.observe(viewLifecycleOwner)
        { flockInventoryAddition -> adapter.setData(flockInventoryAddition) }

        dateFilter.setOnClickListener {
            val popupDateFilterMenu = PopupMenu(requireContext(), dateFilter)
            popupDateFilterMenu.inflate(R.menu.date_ranges)
            popupDateFilterMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.miAllTime -> {
                        mFlockInventoryAdditionViewModel.displayAllFlockInventoryAdditions.observe(
                            viewLifecycleOwner
                        )
                        { flockInventoryAddition -> adapter.setData(flockInventoryAddition) }
                        true
                    }
                    R.id.miToday -> {
                        mFlockInventoryAdditionViewModel.displayFlockInventoryAdditionByDate(
                            numberOfList,
                            thePreviousDay!!,
                            theCurrentDate!!
                        ).observe(viewLifecycleOwner)
                        { flockInventoryAddition -> adapter.setData(flockInventoryAddition) }

                        true
                    }
                    R.id.miThisWeek -> {
                        mFlockInventoryAdditionViewModel.displayFlockInventoryAdditionByDate(
                            numberOfList,
                            thePreviousWeek!!,
                            theCurrentDate!!
                        ).observe(viewLifecycleOwner)
                        { flockInventoryAddition -> adapter.setData(flockInventoryAddition) }

                        true
                    }
                    R.id.miThisMonth -> {
                        mFlockInventoryAdditionViewModel.displayFlockInventoryAdditionByDate(
                            numberOfList,
                            thePreviousMonth!!,
                            theCurrentDate!!
                        ).observe(viewLifecycleOwner)
                        { flockInventoryAddition -> adapter.setData(flockInventoryAddition) }

                        true
                    }
                    R.id.miPastThreeMonths -> {
                        mFlockInventoryAdditionViewModel.displayFlockInventoryAdditionByDate(
                            numberOfList,
                            thePastThreeMonths!!,
                            theCurrentDate!!
                        ).observe(viewLifecycleOwner)
                        { flockInventoryAddition -> adapter.setData(flockInventoryAddition) }

                        true
                    }
                    R.id.miPastSixMonths -> {
                        mFlockInventoryAdditionViewModel.displayFlockInventoryAdditionByDate(
                            numberOfList,
                            thePastSixMonths!!,
                            theCurrentDate!!
                        ).observe(viewLifecycleOwner)
                        { flockInventoryAddition -> adapter.setData(flockInventoryAddition) }

                        true
                    }
                    R.id.miPastYear -> {
                        mFlockInventoryAdditionViewModel.displayFlockInventoryAdditionByDate(
                            numberOfList,
                            thePastYear!!,
                            theCurrentDate!!
                        ).observe(viewLifecycleOwner)
                        { flockInventoryAddition -> adapter.setData(flockInventoryAddition) }

                        true
                    }
                    R.id.miPastTwoYears -> {
                        mFlockInventoryAdditionViewModel.displayFlockInventoryAdditionByDate(
                            numberOfList,
                            thePastTwoYears!!,
                            theCurrentDate!!
                        ).observe(viewLifecycleOwner)
                        { flockInventoryAddition -> adapter.setData(flockInventoryAddition) }

                        true
                    }
                    R.id.miCustomDateRange -> {
                        // instantiate the calendar
                        val myCalendar = Calendar.getInstance()
                        Toast.makeText(context, "Custom Date", Toast.LENGTH_LONG).show()
                        val builder = AlertDialog.Builder(context, Gravity.END)
                        val inflater = layoutInflater
                        val dialogLayout = inflater.inflate(R.layout.custom_date_layout, null)
                        with(builder) {
                            val tvDatePickerFrom = dialogLayout.findViewById<TextView>(R.id.tvDatePickerFrom_CustomDateLayout)
                            val tvDatePickerTo = dialogLayout.findViewById<TextView>(R.id.tvDatePickerTo_CustomDateLayout)
                            val etListNumber = dialogLayout.findViewById<EditText>(R.id.etListNumber_CustomDateLayout)
                            // display and select the date
                            val datePickerFrom = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                                myCalendar.set(Calendar.YEAR, year)
                                myCalendar.set(Calendar.MONTH, month)
                                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                                updateDate(myCalendar, tvDatePickerFrom)
                            }
                            tvDatePickerFrom.setOnClickListener {
                                DatePickerDialog(
                                    requireContext(),
                                    datePickerFrom,
                                    myCalendar.get(Calendar.YEAR),
                                    myCalendar.get(
                                        Calendar.MONTH
                                    ),
                                    myCalendar.get(Calendar.DAY_OF_MONTH)
                                ).show()
                            }
                            val datePickerTo = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                                myCalendar.set(Calendar.YEAR, year)
                                myCalendar.set(Calendar.MONTH, month)
                                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                                updateDate(myCalendar, tvDatePickerTo)
                            }
                            tvDatePickerTo.setOnClickListener {
                                DatePickerDialog(
                                    requireContext(),
                                    datePickerTo,
                                    myCalendar.get(Calendar.YEAR),
                                    myCalendar.get(
                                        Calendar.MONTH
                                    ),
                                    myCalendar.get(Calendar.DAY_OF_MONTH)
                                ).show()
                            }

                            setTitle("Select the date range")
                            setPositiveButton("Ok") { dialog, where ->

                                if (checkIfEmpty(tvDatePickerFrom.text.toString())) {
                                    Toast.makeText(requireContext(), "Please Select Starting Date", Toast.LENGTH_LONG).show()
                                } else if (checkIfEmpty(tvDatePickerTo.text.toString())) {
                                    Toast.makeText(requireContext(), "Please Select Ending Date", Toast.LENGTH_LONG).show()
                                } else {
                                    val firstDate = simpleDateFormat.parse(tvDatePickerFrom.text.toString())
                                    val lastDate = simpleDateFormat.parse(tvDatePickerTo.text.toString())
                                    val theFirstDate = firstDate?.time?.let { java.sql.Date(it) }
                                    val theLastDate = lastDate?.time?.let { java.sql.Date(it) }
                                    val listNumber = if (checkIfEmpty(etListNumber.text.toString())) 100 else etListNumber.text.toString().toInt()

                                    if (theFirstDate != null) { if (theLastDate != null) {
                                        mFlockInventoryAdditionViewModel.displayFlockInventoryAdditionByDate(listNumber, theFirstDate, theLastDate).observe(viewLifecycleOwner)
                                        { flockInventoryAddition -> adapter.setData(flockInventoryAddition) } } }
                                }
                            }
                            setNegativeButton("Cancel") { dialog, which ->
                                Toast.makeText(requireContext(), "Date range not set", Toast.LENGTH_LONG).show()
                            }
                            setView(dialogLayout)
                            show()
                        }

                        true
                    }
                    else -> true
                }
            }
            popupDateFilterMenu.show()
            true

        }



        adapter.setOnItemClickListener(object : FlockInventoryAdditionAdapter.OnItemClickListener {
            override fun onDeleteFlockInventoryAdditionClickListener(flockInventoryAddition: FlockInventoryAddition, position: Int) {
                val popupMenu = PopupMenu(requireContext(), flockInventoryAdditionList[position].findViewById(R.id.ivUpdate_Delete_Menu_FlockAdditionLayout))
                popupMenu.inflate(R.menu.update_delete_menu)
                popupMenu.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.miDelete -> {
                            Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show()
                            val builder = AlertDialog.Builder(context, Gravity.END)
                            val inflater = layoutInflater
                            val dialogLayout = inflater.inflate(R.layout.delete_prompt_layout, null)
                            with(builder) {
                                setTitle("Are you sure you want to permanently delete this Flock Inventory?")

                                setNegativeButton("No") { dialog, which ->
                                    flockInventoryAdditionList.adapter = adapter
                                    flockInventoryAdditionList.layoutManager =
                                        LinearLayoutManager(requireContext())
                                    Toast.makeText(
                                        requireContext(), "Administrator not removed",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                setPositiveButton("Yes") { dialog, where ->
                                    mFlockInventoryAdditionViewModel.delete(flockInventoryAddition)
                                    Toast.makeText(
                                        requireContext(), "Administrator removed",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                setView(dialogLayout)
                                show()
                            }

                            true
                        }
                        R.id.miUpdate -> {
                            Toast.makeText(context, "Update", Toast.LENGTH_LONG).show()
                            val flockInventoryAdditionId = flockInventoryAddition.FlockInventoryAdditionId.toString()
                            val dateAdded = flockInventoryAddition.DateAdded.let { simpleDateFormat.format(it) }
                            val flockName = flockInventoryAddition.FlockName
                            val flockBreed = flockInventoryAddition.FlockBreed
                            val flockSource = flockInventoryAddition.FlockSource
                            val flockPurpose = flockInventoryAddition.FlockPurpose
                            val flockQuantity = flockInventoryAddition.FlockQuantity.toString()
                            val flockAge = flockInventoryAddition.FlockAge.toString()
                            val flockCost = flockInventoryAddition.FlockCost.toString()
                            val shortNotes = flockInventoryAddition.ShortNotes
                            communicator = activity as FlockCommunicator
                            communicator.passFlockInventoryAddition(flockInventoryAdditionId, dateAdded, flockName, flockBreed, flockSource,  flockPurpose, flockQuantity, flockAge, flockCost, shortNotes)
                            true
                        }
                        else -> true
                    }
                }
                popupMenu.show()
                true
            }
            override fun onItemClickListener(flockInventoryAddition: FlockInventoryAddition) {
                val flockInventoryAdditionId = flockInventoryAddition.FlockInventoryAdditionId.toString()
                val dateAdded = flockInventoryAddition.DateAdded.let { simpleDateFormat.format(it) }
                val flockName = flockInventoryAddition.FlockName
                val flockBreed = flockInventoryAddition.FlockBreed
                val flockSource = flockInventoryAddition.FlockSource
                val flockPurpose = flockInventoryAddition.FlockPurpose
                val flockQuantity = flockInventoryAddition.FlockQuantity.toString()
                val flockAge = flockInventoryAddition.FlockAge.toString()
                val flockCost = flockInventoryAddition.FlockCost.toString()
                val shortNotes = flockInventoryAddition.ShortNotes
                communicator = activity as FlockCommunicator
                communicator.passFlockInventoryAddition(flockInventoryAdditionId, dateAdded, flockName, flockBreed, flockSource,  flockPurpose, flockQuantity, flockAge, flockCost, shortNotes)

            }
        })

        val back = view.findViewById<ImageButton>(R.id.ibBack_FlockInventoryAdditionList)
        back.setOnClickListener {
            startActivity(Intent(context, InventoryActivity::class.java))
        }


        val addFlockInventory = view.findViewById<FloatingActionButton>(R.id.fabAddFlockInventory)
        addFlockInventory.setOnClickListener {
            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flFlockActivity,
                FlockAdditionForm()
            ).addToBackStack(null).commit()
        }


        return view
    }
    private fun updateDate(myCalendar: Calendar, tvDatePicker: TextView) {
        val myFormat = "EEE, dd MMM, yyyy"
        val simpleDateFormat = SimpleDateFormat(myFormat, Locale.UK)
        tvDatePicker.text = simpleDateFormat.format(myCalendar.time)
    }

    private fun checkIfEmpty(value: String): Boolean {
        return (TextUtils.isEmpty(value))
    }

}