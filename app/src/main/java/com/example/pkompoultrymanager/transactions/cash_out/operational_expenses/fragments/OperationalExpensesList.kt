package com.example.pkompoultrymanager.transactions.cash_out.operational_expenses.fragments

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
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
import com.example.pkompoultrymanager.transactions.cash_out.CashOutCommunicator
import com.example.pkompoultrymanager.transactions.cash_out.operational_expenses.OperationalExpenses
import com.example.pkompoultrymanager.transactions.cash_out.operational_expenses.OperationalExpensesAdapter
import com.example.pkompoultrymanager.transactions.cash_out.operational_expenses.OperationalExpensesViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*

class OperationalExpensesList : Fragment() {
    private lateinit var mOperationalExpensesViewModel: OperationalExpensesViewModel
    private lateinit var communicator: CashOutCommunicator
    private lateinit var myFarmInfo: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_operational_expenses_list, container, false)

        val numberOfList = 100
        val myFormat = "dd-MM-yyyy"
        val simpleDateFormat = SimpleDateFormat(myFormat, Locale.UK)


        myFarmInfo = context?.getSharedPreferences("MyFarmInfo", Context.MODE_PRIVATE)!!
        val currency = myFarmInfo.getString("currency", "GH₵")

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

        val dateFilter = view.findViewById<ImageButton>(R.id.ibDateFilter_OperationalExpensesList)


        val operationalExpensesList = view.findViewById<RecyclerView>(R.id.rvOperationalExpensesList)
        val adapter = OperationalExpensesAdapter(currency!!)

        operationalExpensesList.adapter = adapter
        operationalExpensesList.layoutManager= LinearLayoutManager(requireContext())
        mOperationalExpensesViewModel = ViewModelProvider(this)[OperationalExpensesViewModel::class.java]

        mOperationalExpensesViewModel = ViewModelProvider(this)[OperationalExpensesViewModel::class.java]
        mOperationalExpensesViewModel.displayAllOperationalExpensess.observe(viewLifecycleOwner
        ) { operationalExpenses -> adapter.setData(operationalExpenses) }

        dateFilter.setOnClickListener {
            val popupDateFilterMenu = PopupMenu(requireContext(), dateFilter)
            popupDateFilterMenu.inflate(R.menu.date_ranges)
            popupDateFilterMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.miAllTime -> {
                        mOperationalExpensesViewModel.displayAllOperationalExpensess.observe(
                            viewLifecycleOwner
                        )
                        { operationalExpenses -> adapter.setData(operationalExpenses) }
                        true
                    }
                    R.id.miToday -> {
                        mOperationalExpensesViewModel.displayOperationalExpensesByDate(
                            numberOfList,
                            thePreviousDay!!,
                            theCurrentDate!!
                        ).observe(viewLifecycleOwner)
                        { operationalExpenses -> adapter.setData(operationalExpenses) }

                        true
                    }
                    R.id.miThisWeek -> {
                        mOperationalExpensesViewModel.displayOperationalExpensesByDate(
                            numberOfList,
                            thePreviousWeek!!,
                            theCurrentDate!!
                        ).observe(viewLifecycleOwner)
                        { operationalExpenses -> adapter.setData(operationalExpenses) }

                        true
                    }
                    R.id.miThisMonth -> {
                        mOperationalExpensesViewModel.displayOperationalExpensesByDate(
                            numberOfList,
                            thePreviousMonth!!,
                            theCurrentDate!!
                        ).observe(viewLifecycleOwner)
                        { operationalExpenses -> adapter.setData(operationalExpenses) }

                        true
                    }
                    R.id.miPastThreeMonths -> {
                        mOperationalExpensesViewModel.displayOperationalExpensesByDate(
                            numberOfList,
                            thePastThreeMonths!!,
                            theCurrentDate!!
                        ).observe(viewLifecycleOwner)
                        { operationalExpenses -> adapter.setData(operationalExpenses) }

                        true
                    }
                    R.id.miPastSixMonths -> {
                        mOperationalExpensesViewModel.displayOperationalExpensesByDate(
                            numberOfList,
                            thePastSixMonths!!,
                            theCurrentDate!!
                        ).observe(viewLifecycleOwner)
                        { operationalExpenses -> adapter.setData(operationalExpenses) }

                        true
                    }
                    R.id.miPastYear -> {
                        mOperationalExpensesViewModel.displayOperationalExpensesByDate(
                            numberOfList,
                            thePastYear!!,
                            theCurrentDate!!
                        ).observe(viewLifecycleOwner)
                        { operationalExpenses -> adapter.setData(operationalExpenses) }

                        true
                    }
                    R.id.miPastTwoYears -> {
                        mOperationalExpensesViewModel.displayOperationalExpensesByDate(
                            numberOfList,
                            thePastTwoYears!!,
                            theCurrentDate!!
                        ).observe(viewLifecycleOwner)
                        { operationalExpenses -> adapter.setData(operationalExpenses) }

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
                                        mOperationalExpensesViewModel.displayOperationalExpensesByDate(listNumber, theFirstDate, theLastDate).observe(viewLifecycleOwner)
                                        { operationalExpenses -> adapter.setData(operationalExpenses) } } }
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


        adapter.setOnItemClickListener(object : OperationalExpensesAdapter.OnItemClickListener {
            override fun onDeleteOperationalExpensesClickListener(operationalExpenses: OperationalExpenses, position: Int) {
                val popupMenu = PopupMenu(requireContext(), operationalExpensesList[position].findViewById(R.id.ivUpdate_Delete_Menu_ExpensesLayout))
                popupMenu.inflate(R.menu.update_delete_menu)
                popupMenu.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.miDelete -> {
                            Toast.makeText(context, "Delete", Toast.LENGTH_LONG).show()
                            val builder = AlertDialog.Builder(context, Gravity.END)
                            val inflater = layoutInflater
                            val dialogLayout = inflater.inflate(R.layout.delete_prompt_layout, null)
                            with(builder) {
                                setTitle("Are you sure you want to permanently delete this operational Expenses?")

                                setNegativeButton("No") { dialog, which ->
                                    operationalExpensesList.adapter = adapter
                                    operationalExpensesList.layoutManager =
                                        LinearLayoutManager(requireContext())
                                    Toast.makeText(
                                        requireContext(), "Operational Expenses not removed",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                setPositiveButton("Yes") { dialog, where ->
                                    mOperationalExpensesViewModel.delete(operationalExpenses)
                                    Toast.makeText(
                                        requireContext(), "OperationalExpenses removed",
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

                            val id = operationalExpenses.OperationalExpensesId.toString()
                            val date = simpleDateFormat.format(operationalExpenses.Date)
                            val name = operationalExpenses.ExpenseName
                            val amount = operationalExpenses.ExpenseAmount.toString()
                            val transactionId = operationalExpenses.TransactionID
                            val paymentMethod = operationalExpenses.PaymentMethod
                            val shortNotes = operationalExpenses.shortNotes
                            communicator = activity as CashOutCommunicator
                            communicator.passOperationalExpenses(
                                id,
                                date,
                                name,
                                paymentMethod,
                                amount,
                                transactionId,
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
            override fun onItemClickListener(operationalExpenses: OperationalExpenses) {
                val id = operationalExpenses.OperationalExpensesId.toString()
                val date = simpleDateFormat.format(operationalExpenses.Date)
                val name = operationalExpenses.ExpenseName
                val amount = operationalExpenses.ExpenseAmount.toString()
                val transactionId = operationalExpenses.TransactionID
                val paymentMethod = operationalExpenses.PaymentMethod
                val shortNotes = operationalExpenses.shortNotes
                communicator = activity as CashOutCommunicator
                communicator.passOperationalExpenses(
                    id,
                    date,
                    name,
                    paymentMethod,
                    amount,
                    transactionId,
                    shortNotes
                )
            }
        })



            val addOperationalExpense = view.findViewById<FloatingActionButton>(R.id.fabAddOperationalExpense)
        addOperationalExpense.setOnClickListener {
            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flCashOutActivity,
                OperationalExpensesForm()
            ).commit()
        }

        return view
    }

    private fun updateDate(myCalendar: Calendar, tvDatePicker: TextView) {
        val myFormat = "EEE dd MM, yyyy"
        val simpleDateFormat = SimpleDateFormat(myFormat, Locale.UK)
        tvDatePicker.text = simpleDateFormat.format(myCalendar.time)
    }

    private fun checkIfEmpty(value: String): Boolean {
        return (TextUtils.isEmpty(value))
    }


}