package com.example.pkompoultrymanager.inventory.feed.addition.fragments

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
import com.example.pkompoultrymanager.inventory.feed.FeedCommunicator
import com.example.pkompoultrymanager.inventory.feed.addition.FeedInventoryAddition
import com.example.pkompoultrymanager.inventory.feed.addition.FeedInventoryAdditionAdapter
import com.example.pkompoultrymanager.inventory.feed.addition.FeedInventoryAdditionViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*

class FeedInventoryAdditionList : Fragment() {
    private lateinit var myFarmInfo: SharedPreferences
    private lateinit var mFeedInventoryAdditionViewModel: FeedInventoryAdditionViewModel
    private lateinit var communicator: FeedCommunicator


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =
            inflater.inflate(R.layout.fragment_feed_inventory_addition_list, container, false)

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
        val previousMonth = "${1}-${currentMonth + 1}-$currentYear"
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

        val dateFilter = view.findViewById<ImageButton>(R.id.ibDateFilter_FeedInventoryAdditionList)

        myFarmInfo = context?.getSharedPreferences("MyFarmInfo", Context.MODE_PRIVATE)!!
        val measuringUnit = myFarmInfo.getString("measuringUnit", "kg")!!
        val currency = myFarmInfo.getString("currency", "GHS")!!


        val feedInventoryAdditionList =
            view.findViewById<RecyclerView>(R.id.rvFeedInventoryAdditionList)
        val adapter = FeedInventoryAdditionAdapter(currency, measuringUnit)
        feedInventoryAdditionList.adapter = adapter
        feedInventoryAdditionList.layoutManager = LinearLayoutManager(requireContext())

        mFeedInventoryAdditionViewModel =
            ViewModelProvider(this)[FeedInventoryAdditionViewModel::class.java]
        mFeedInventoryAdditionViewModel.displayAllFeedInventoryAdditions.observe(viewLifecycleOwner)
        { feedInventoryAddition -> adapter.setData(feedInventoryAddition) }

        dateFilter.setOnClickListener {
            val popupDateFilterMenu = PopupMenu(requireContext(), dateFilter)
            popupDateFilterMenu.inflate(R.menu.date_ranges)
            popupDateFilterMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.miAllTime -> {
                        mFeedInventoryAdditionViewModel.displayAllFeedInventoryAdditions.observe(
                            viewLifecycleOwner
                        )
                        { feedInventoryAddition -> adapter.setData(feedInventoryAddition) }
                        true
                    }
                    R.id.miToday -> {
                        mFeedInventoryAdditionViewModel.displayFeedInventoryAdditionByDate(
                            numberOfList,
                            thePreviousDay!!,
                            theCurrentDate!!
                        ).observe(viewLifecycleOwner)
                        { feedInventoryAddition -> adapter.setData(feedInventoryAddition) }

                        true
                    }
                    R.id.miThisWeek -> {
                        mFeedInventoryAdditionViewModel.displayFeedInventoryAdditionByDate(
                            numberOfList,
                            thePreviousWeek!!,
                            theCurrentDate!!
                        ).observe(viewLifecycleOwner)
                        { feedInventoryAddition -> adapter.setData(feedInventoryAddition) }

                        true
                    }
                    R.id.miThisMonth -> {
                        mFeedInventoryAdditionViewModel.displayFeedInventoryAdditionByDate(
                            numberOfList,
                            thePreviousMonth!!,
                            theCurrentDate!!
                        ).observe(viewLifecycleOwner)
                        { feedInventoryAddition -> adapter.setData(feedInventoryAddition) }

                        true
                    }
                    R.id.miPastThreeMonths -> {
                        mFeedInventoryAdditionViewModel.displayFeedInventoryAdditionByDate(
                            numberOfList,
                            thePastThreeMonths!!,
                            theCurrentDate!!
                        ).observe(viewLifecycleOwner)
                        { feedInventoryAddition -> adapter.setData(feedInventoryAddition) }

                        true
                    }
                    R.id.miPastSixMonths -> {
                        mFeedInventoryAdditionViewModel.displayFeedInventoryAdditionByDate(
                            numberOfList,
                            thePastSixMonths!!,
                            theCurrentDate!!
                        ).observe(viewLifecycleOwner)
                        { feedInventoryAddition -> adapter.setData(feedInventoryAddition) }

                        true
                    }
                    R.id.miPastYear -> {
                        mFeedInventoryAdditionViewModel.displayFeedInventoryAdditionByDate(
                            numberOfList,
                            thePastYear!!,
                            theCurrentDate!!
                        ).observe(viewLifecycleOwner)
                        { feedInventoryAddition -> adapter.setData(feedInventoryAddition) }

                        true
                    }
                    R.id.miPastTwoYears -> {
                        mFeedInventoryAdditionViewModel.displayFeedInventoryAdditionByDate(
                            numberOfList,
                            thePastTwoYears!!,
                            theCurrentDate!!
                        ).observe(viewLifecycleOwner)
                        { feedInventoryAddition -> adapter.setData(feedInventoryAddition) }

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
                            val tvDatePickerFrom =
                                dialogLayout.findViewById<TextView>(R.id.tvDatePickerFrom_CustomDateLayout)
                            val tvDatePickerTo =
                                dialogLayout.findViewById<TextView>(R.id.tvDatePickerTo_CustomDateLayout)
                            val etListNumber =
                                dialogLayout.findViewById<EditText>(R.id.etListNumber_CustomDateLayout)
                            // display and select the date
                            val datePickerFrom =
                                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
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
                            val datePickerTo =
                                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
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
                                    Toast.makeText(
                                        requireContext(),
                                        "Please Select Starting Date",
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else if (checkIfEmpty(tvDatePickerTo.text.toString())) {
                                    Toast.makeText(
                                        requireContext(),
                                        "Please Select Ending Date",
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else {
                                    val firstDate =
                                        simpleDateFormat.parse(tvDatePickerFrom.text.toString())
                                    val lastDate =
                                        simpleDateFormat.parse(tvDatePickerTo.text.toString())
                                    val theFirstDate = firstDate?.time?.let { java.sql.Date(it) }
                                    val theLastDate = lastDate?.time?.let { java.sql.Date(it) }
                                    val listNumber =
                                        if (checkIfEmpty(etListNumber.text.toString())) 100 else etListNumber.text.toString()
                                            .toInt()

                                    if (theFirstDate != null) {
                                        if (theLastDate != null) {
                                            mFeedInventoryAdditionViewModel.displayFeedInventoryAdditionByDate(
                                                listNumber,
                                                theFirstDate,
                                                theLastDate
                                            ).observe(viewLifecycleOwner)
                                            { feedInventoryAddition ->
                                                adapter.setData(
                                                    feedInventoryAddition
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            setNegativeButton("Cancel") { dialog, which ->
                                Toast.makeText(
                                    requireContext(),
                                    "Date range not set",
                                    Toast.LENGTH_LONG
                                ).show()
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

        adapter.setOnItemClickListener(object : FeedInventoryAdditionAdapter.OnItemClickListener {
            override fun onDeleteFeedInventoryAdditionClickListener(feedInventoryAddition: FeedInventoryAddition, position: Int) {
                val popupMenu = PopupMenu(requireContext(), feedInventoryAdditionList[position].findViewById(R.id.ivUpdate_Delete_Menu_FeedInventoryAdditionLayout))
                popupMenu.inflate(R.menu.update_delete_menu)
                popupMenu.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.miDelete -> {
                            Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show()
                            val builder = AlertDialog.Builder(context, Gravity.END)
                            val inflater = layoutInflater
                            val dialogLayout = inflater.inflate(R.layout.delete_prompt_layout, null)
                            with(builder) {
                                setTitle("Are you sure you want to permanently delete this Egg Inventory?")
                                setNegativeButton("No") { dialog, which ->
                                    feedInventoryAdditionList.adapter = adapter
                                    feedInventoryAdditionList.layoutManager =
                                        LinearLayoutManager(requireContext())
                                    Toast.makeText(
                                        requireContext(), "Administrator not removed",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                setPositiveButton("Yes") { dialog, where ->
                                    mFeedInventoryAdditionViewModel.delete(feedInventoryAddition)
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
                            val feedInventoryAdditionId =
                                feedInventoryAddition.FeedInventoryAdditionId.toString()
                            val dateAcquired =
                                feedInventoryAddition.DateAcquired.let { simpleDateFormat.format(it) }
                            val feedName = feedInventoryAddition.FeedName
                            val feedQuantity = feedInventoryAddition.FeedQuantity.toString()
                            val feedCost = feedInventoryAddition.FeedCost.toString()
                            val feedSource = feedInventoryAddition.FeedSource
                            val shortNotes = feedInventoryAddition.ShortNotes
                            communicator = activity as FeedCommunicator
                            communicator.passFeedInventoryAddition(
                                feedInventoryAdditionId,
                                dateAcquired,
                                feedName,
                                feedQuantity,
                                feedCost,
                                feedSource,
                                shortNotes
                            )
                            true
                        }
                        else -> true
                    }
                }
                popupMenu.show()
            }

            override fun onItemClickListener(feedInventoryAddition: FeedInventoryAddition) {
                val feedInventoryAdditionId =
                    feedInventoryAddition.FeedInventoryAdditionId.toString()
                val dateAcquired =
                    feedInventoryAddition.DateAcquired.let { simpleDateFormat.format(it) }
                val feedName = feedInventoryAddition.FeedName
                val feedQuantity = feedInventoryAddition.FeedQuantity.toString()
                val feedCost = feedInventoryAddition.FeedCost.toString()
                val feedSource = feedInventoryAddition.FeedSource
                val shortNotes = feedInventoryAddition.ShortNotes
                communicator = activity as FeedCommunicator
                communicator.passFeedInventoryAddition(
                    feedInventoryAdditionId,
                    dateAcquired,
                    feedName,
                    feedQuantity,
                    feedCost,
                    feedSource,
                    shortNotes
                )
            }
        })

        val back = view.findViewById<ImageButton>(R.id.ibBack_FeedInventoryAdditionList)
        back.setOnClickListener {
            startActivity(Intent(context, InventoryActivity::class.java))
        }

        val addFeedInventory = view.findViewById<FloatingActionButton>(R.id.fabAddFeedInventory)
        addFeedInventory.setOnClickListener {
            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flFeedActivity,
                FeedInventoryAdditionForm()
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