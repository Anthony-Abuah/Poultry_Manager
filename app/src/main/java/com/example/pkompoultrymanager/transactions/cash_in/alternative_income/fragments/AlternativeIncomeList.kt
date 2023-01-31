package com.example.pkompoultrymanager.transactions.cash_in.alternative_income.fragments

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pkompoultrymanager.R
import com.example.pkompoultrymanager.transactions.cash_in.CashInCommunicator
import com.example.pkompoultrymanager.transactions.cash_in.alternative_income.AlternativeIncome
import com.example.pkompoultrymanager.transactions.cash_in.alternative_income.AlternativeIncomeAdapter
import com.example.pkompoultrymanager.transactions.cash_in.alternative_income.AlternativeIncomeViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*

class AlternativeIncomeList : Fragment() {
    private lateinit var mAlternativeIncomeViewModel: AlternativeIncomeViewModel
    private lateinit var communicator: CashInCommunicator


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_alternative_income_list, container, false)

        val numberOfList = 100
        val myFormat = "dd-MM-yyyy"
        val simpleDateFormat = SimpleDateFormat(myFormat, Locale.UK)

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
        val theCurrentSystemDate: Date? = simpleDateFormat.parse(currentDate)
        val thePreviousSystemDay: Date? = simpleDateFormat.parse(previousDay)
        val thePreviousSystemWeek: Date? = simpleDateFormat.parse(previousWeek)
        val thePreviousSystemMonth: Date? = simpleDateFormat.parse(previousMonth)
        val thePreviousThreeSystemMonths: Date? = simpleDateFormat.parse(pastThreeMonths)
        val thePreviousSixSystemMonths: Date? = simpleDateFormat.parse(pastSixMonths)
        val thePreviousSystemYear: Date? = simpleDateFormat.parse(pastYear)
        val thePreviousTwoSystemYears: Date? = simpleDateFormat.parse(pastTwoYears)

        // convert the java.util date to java.sql date
        val theCurrentDate = theCurrentSystemDate?.time?.let { java.sql.Date(it) }
        val thePreviousDay = thePreviousSystemDay?.time?.let { java.sql.Date(it) }
        val thePreviousWeek = thePreviousSystemWeek?.time?.let { java.sql.Date(it) }
        val thePreviousMonth = thePreviousSystemMonth?.time?.let { java.sql.Date(it) }
        val thePastThreeMonths = thePreviousThreeSystemMonths?.time?.let { java.sql.Date(it) }
        val thePastSixMonths = thePreviousSixSystemMonths?.time?.let { java.sql.Date(it) }
        val thePastYear = thePreviousSystemYear?.time?.let { java.sql.Date(it) }
        val thePastTwoYears = thePreviousTwoSystemYears?.time?.let { java.sql.Date(it) }


        val dateFilter = view.findViewById<ImageButton>(R.id.ibDateFilter_AlternativeIncomeList)


        val alternativeIncomeList = view.findViewById<RecyclerView>(R.id.rvAlternativeIncomeList)
        val adapter = AlternativeIncomeAdapter()
        alternativeIncomeList.adapter = adapter
        alternativeIncomeList.layoutManager = LinearLayoutManager(requireContext())
        mAlternativeIncomeViewModel = ViewModelProvider(this)[AlternativeIncomeViewModel::class.java]

        mAlternativeIncomeViewModel.displayAllAlternativeIncomes.observe(
            viewLifecycleOwner
        ) { alternativeIncome -> adapter.setData(alternativeIncome) }

        dateFilter.setOnClickListener {
            val popupDateFilterMenu = PopupMenu(requireContext(), dateFilter)
            popupDateFilterMenu.inflate(R.menu.date_ranges)
            popupDateFilterMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.miAllTime -> {
                        mAlternativeIncomeViewModel.displayAllAlternativeIncomes.observe(
                            viewLifecycleOwner
                        )
                        { alternativeIncome -> adapter.setData(alternativeIncome) }
                        true
                    }
                    R.id.miToday -> {
                                mAlternativeIncomeViewModel.displayAlternativeIncomeByDate(
                                    numberOfList,
                                    thePreviousDay!!,
                                    theCurrentDate!!
                                ).observe(viewLifecycleOwner)
                                { alternativeIncome -> adapter.setData(alternativeIncome) }

                        true
                    }
                    R.id.miThisWeek -> {
                                mAlternativeIncomeViewModel.displayAlternativeIncomeByDate(
                                    numberOfList,
                                    thePreviousWeek!!,
                                    theCurrentDate!!
                                ).observe(viewLifecycleOwner)
                                { alternativeIncome -> adapter.setData(alternativeIncome) }

                        true
                    }
                    R.id.miThisMonth -> {
                                mAlternativeIncomeViewModel.displayAlternativeIncomeByDate(
                                    numberOfList,
                                    thePreviousMonth!!,
                                    theCurrentDate!!
                                ).observe(viewLifecycleOwner)
                                { alternativeIncome -> adapter.setData(alternativeIncome) }

                        true
                    }
                    R.id.miPastThreeMonths -> {
                         mAlternativeIncomeViewModel.displayAlternativeIncomeByDate(
                                    numberOfList,
                                    thePastThreeMonths!!,
                                    theCurrentDate!!
                                ).observe(viewLifecycleOwner)
                                { alternativeIncome -> adapter.setData(alternativeIncome) }

                        true
                    }
                    R.id.miPastSixMonths -> {
                      mAlternativeIncomeViewModel.displayAlternativeIncomeByDate(
                                    numberOfList,
                                    thePastSixMonths!!,
                                    theCurrentDate!!
                                ).observe(viewLifecycleOwner)
                                { alternativeIncome -> adapter.setData(alternativeIncome) }

                        true
                    }
                    R.id.miPastYear -> {
                        mAlternativeIncomeViewModel.displayAlternativeIncomeByDate(
                                    numberOfList,
                                    thePastYear!!,
                                    theCurrentDate!!
                                ).observe(viewLifecycleOwner)
                                { alternativeIncome -> adapter.setData(alternativeIncome) }

                        true
                    }
                    R.id.miPastTwoYears -> {
                                mAlternativeIncomeViewModel.displayAlternativeIncomeByDate(
                                    numberOfList,
                                    thePastTwoYears!!,
                                    theCurrentDate!!
                                ).observe(viewLifecycleOwner)
                                { alternativeIncome -> adapter.setData(alternativeIncome) }

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
                                        mAlternativeIncomeViewModel.displayAlternativeIncomeByDate(listNumber, theFirstDate, theLastDate).observe(viewLifecycleOwner)
                                        { alternativeIncome -> adapter.setData(alternativeIncome) } } }
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

        adapter.setOnItemClickListener(object : AlternativeIncomeAdapter.OnItemClickListener {
            val myFormat = "dd-MM-yyyy"
            val simpleDateFormat = SimpleDateFormat(myFormat, Locale.UK)
            override fun onDeleteAlternativeIncomeClickListener(alternativeIncome: AlternativeIncome) {
                val popupMenu = PopupMenu(requireContext(), view)
                popupMenu.inflate(R.menu.update_delete_menu)
                popupMenu.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.miDelete -> {
                            Toast.makeText(context, "Delete", Toast.LENGTH_LONG).show()
                            val builder = AlertDialog.Builder(context, Gravity.END)
                            val inflater = layoutInflater
                            val dialogLayout = inflater.inflate(R.layout.delete_prompt_layout, null)
                            with(builder) {
                                setTitle("Are you sure you want to permanently delete this alternativeIncome?")

                                setNegativeButton("No") { dialog, which ->
                                    alternativeIncomeList.adapter = adapter
                                    alternativeIncomeList.layoutManager =
                                        LinearLayoutManager(requireContext())
                                    Toast.makeText(
                                        requireContext(), "AlternativeIncome not removed",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                setPositiveButton("Yes") { dialog, where ->
                                    mAlternativeIncomeViewModel.delete(alternativeIncome)
                                    Toast.makeText(
                                        requireContext(), "AlternativeIncome removed",
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

                            val alternativeIncomeId = alternativeIncome.AlternativeIncomeId.toString()
                            val date = simpleDateFormat.format(alternativeIncome.Date)
                            val itemPurchased = alternativeIncome.ItemPurchased
                            val customer = alternativeIncome.Customer
                            val itemPurchasedQuantity = alternativeIncome.ItemPurchased_Quantity.toString()
                            val itemPurchasedCost = alternativeIncome.ItemPurchased_Cost.toString()
                            val amountReceived = alternativeIncome.AmountReceived.toString()
                            val receiptNumber = alternativeIncome.ReceiptNumber
                            val shortNotes = alternativeIncome.shortNotes
                            communicator = activity as CashInCommunicator
                            communicator.passAlternativeIncome(
                                alternativeIncomeId,
                                date,
                                itemPurchased,
                                customer,
                                itemPurchasedQuantity,
                                itemPurchasedCost,
                                amountReceived,
                                receiptNumber,
                                shortNotes
                            )
                            true
                        }
                        else -> true
                    }
                }
                popupMenu.show()
                true

            }

            override fun onItemClickListener(alternativeIncome: AlternativeIncome) {
                val alternativeIncomeId = alternativeIncome.AlternativeIncomeId.toString()
                val date = simpleDateFormat.format(alternativeIncome.Date)
                val itemPurchased = alternativeIncome.ItemPurchased
                val customer = alternativeIncome.Customer
                val itemPurchasedQuantity = alternativeIncome.ItemPurchased_Quantity.toString()
                val itemPurchasedCost = alternativeIncome.ItemPurchased_Cost.toString()
                val amountReceived = alternativeIncome.AmountReceived.toString()
                val receiptNumber = alternativeIncome.ReceiptNumber
                val shortNotes = alternativeIncome.shortNotes
                communicator = activity as CashInCommunicator
                communicator.passAlternativeIncome(
                    alternativeIncomeId,
                    date,
                    itemPurchased,
                    customer,
                    itemPurchasedQuantity,
                    itemPurchasedCost,
                    amountReceived,
                    receiptNumber,
                    shortNotes
                )

            }
        })


        val addAlternativeIncome =
            view.findViewById<FloatingActionButton>(R.id.fabAddAlternativeIncome)
        addAlternativeIncome.setOnClickListener {
            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flCashInActivity,
                AlternativeIncomeForm()
            ).addToBackStack(null).commit()
        }

        return view
    }


    private fun updateDate(myCalendar: Calendar, tvDatePicker: TextView) {
        val myFormat = "dd-MM-yyyy"
        val simpleDateFormat = SimpleDateFormat(myFormat, Locale.UK)
        tvDatePicker.text = simpleDateFormat.format(myCalendar.time)
    }

    private fun checkIfEmpty(value: String): Boolean {
        return (TextUtils.isEmpty(value))
    }

}