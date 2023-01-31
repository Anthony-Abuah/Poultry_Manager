package com.example.pkompoultrymanager.transactions.cash_in.advanced_purchases.fragments

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
import com.example.pkompoultrymanager.transactions.cash_in.CashInCommunicator
import com.example.pkompoultrymanager.transactions.cash_in.advanced_purchases.AdvancePurchase
import com.example.pkompoultrymanager.transactions.cash_in.advanced_purchases.AdvancePurchaseAdapter
import com.example.pkompoultrymanager.transactions.cash_in.advanced_purchases.AdvancePurchaseViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*

class AdvancedPurchaseList : Fragment() {

    private lateinit var mAdvancePurchaseViewModel: AdvancePurchaseViewModel
    private lateinit var communicator: CashInCommunicator
    private lateinit var myFarmInfo: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_advanced_purchase_list, container, false)

        myFarmInfo = context?.getSharedPreferences("MyFarmInfo", Context.MODE_PRIVATE)!!
        val currency = myFarmInfo.getString("currency", "GH₵")

        val numberOfList = 100
        val dateFormat = "dd-MM-yyyy"
        val systemDateFormat = SimpleDateFormat(dateFormat, Locale.UK)

        val myFormat = "EEE, dd MMM, yyyy"
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


        val dateFilter = view.findViewById<ImageButton>(R.id.ibDateFilter_AdvancePurchaseList)

        val advancePurchaseList = view.findViewById<RecyclerView>(R.id.rvAdvancedPurchaseList)
        val adapter = AdvancePurchaseAdapter(currency!!)
        advancePurchaseList.adapter = adapter
        advancePurchaseList.layoutManager = LinearLayoutManager(requireContext())

        mAdvancePurchaseViewModel = ViewModelProvider(this)[AdvancePurchaseViewModel::class.java]
        mAdvancePurchaseViewModel.displayAllAdvancePurchases.observe(viewLifecycleOwner)
        { advancePurchase -> adapter.setData(advancePurchase) }


        dateFilter.setOnClickListener {
            val popupDateFilterMenu = PopupMenu(requireContext(), dateFilter)
            popupDateFilterMenu.inflate(R.menu.date_ranges)
            popupDateFilterMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.miAllTime -> {
                        mAdvancePurchaseViewModel.displayAllAdvancePurchases.observe(
                            viewLifecycleOwner
                        )
                        { advancePurchase -> adapter.setData(advancePurchase) }
                        true
                    }
                    R.id.miToday -> {
                                mAdvancePurchaseViewModel.displayAdvancePurchaseByDate(
                                    numberOfList,
                                    thePreviousDay!!,
                                    theCurrentDate!!
                                ).observe(viewLifecycleOwner)
                                { advancePurchase -> adapter.setData(advancePurchase) }

                        true
                    }
                    R.id.miThisWeek -> {
                                mAdvancePurchaseViewModel.displayAdvancePurchaseByDate(
                                    numberOfList,
                                    thePreviousWeek!!,
                                    theCurrentDate!!
                                ).observe(viewLifecycleOwner)
                                { advancePurchase -> adapter.setData(advancePurchase) }

                        true
                    }
                    R.id.miThisMonth -> {
                                mAdvancePurchaseViewModel.displayAdvancePurchaseByDate(
                                    numberOfList,
                                    thePreviousMonth!!,
                                    theCurrentDate!!
                                ).observe(viewLifecycleOwner)
                                { advancePurchase -> adapter.setData(advancePurchase) }

                        true
                    }
                    R.id.miPastThreeMonths -> {
                        mAdvancePurchaseViewModel.displayAdvancePurchaseByDate(
                                    numberOfList,
                                    thePastThreeMonths!!,
                                    theCurrentDate!!
                                ).observe(viewLifecycleOwner)
                                { advancePurchase -> adapter.setData(advancePurchase) }

                        true
                    }
                    R.id.miPastSixMonths -> {
                                mAdvancePurchaseViewModel.displayAdvancePurchaseByDate(
                                    numberOfList,
                                    thePastSixMonths!!,
                                    theCurrentDate!!
                                ).observe(viewLifecycleOwner)
                                { advancePurchase -> adapter.setData(advancePurchase) }

                        true
                    }
                    R.id.miPastYear -> {
                                mAdvancePurchaseViewModel.displayAdvancePurchaseByDate(
                                    numberOfList,
                                    thePastYear!!,
                                    theCurrentDate!!
                                ).observe(viewLifecycleOwner)
                                { advancePurchase -> adapter.setData(advancePurchase) }

                        true
                    }
                    R.id.miPastTwoYears -> {
                                mAdvancePurchaseViewModel.displayAdvancePurchaseByDate(
                                    numberOfList,
                                    thePastTwoYears!!,
                                    theCurrentDate!!
                                ).observe(viewLifecycleOwner)
                                { advancePurchase -> adapter.setData(advancePurchase) }

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
                                        mAdvancePurchaseViewModel.displayAdvancePurchaseByDate(listNumber, theFirstDate, theLastDate).observe(viewLifecycleOwner)
                                        { advancePurchase -> adapter.setData(advancePurchase) } } }
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

adapter.setOnItemClickListener(object : AdvancePurchaseAdapter.OnItemClickListener {
    override fun onDeleteAdvancePurchaseClickListener(advancePurchase: AdvancePurchase, position: Int) {
        val popupMenu = PopupMenu(
            requireContext(), advancePurchaseList[position].findViewById(R.id.ivUpdate_Delete_Menu_AdvancePurchaseLayout))
        popupMenu.inflate(R.menu.update_delete_menu)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.miDelete -> {
                    Toast.makeText(context, "Delete", Toast.LENGTH_LONG).show()
                    val builder = AlertDialog.Builder(context, Gravity.END)
                    val inflater = layoutInflater
                    val dialogLayout = inflater.inflate(R.layout.delete_prompt_layout, null)
                    with(builder) {
                        setTitle("Are you sure you want to permanently delete this advancePurchase?")

                        setNegativeButton("No") { dialog, which ->
                            advancePurchaseList.adapter = adapter
                            advancePurchaseList.layoutManager =
                                LinearLayoutManager(requireContext())
                            Toast.makeText(
                                requireContext(), "AdvancePurchase not removed",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        setPositiveButton("Yes") { dialog, where ->
                            mAdvancePurchaseViewModel.delete(advancePurchase)
                            Toast.makeText(
                                requireContext(), "AdvancePurchase removed",
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
                    val advancePurchaseId = advancePurchase.AdvancePurchaseId.toString()
                    val date = simpleDateFormat.format(advancePurchase.Date)
                    val customer = advancePurchase.Customer
                    val itemPurchased = advancePurchase.ItemPurchased
                    val itemPurchasedCost = advancePurchase.ItemPurchased_Cost.toString()
                    val amountReceived = advancePurchase.AmountReceived.toString()
                    val receiptNumber = advancePurchase.ReceiptNumber
                    val itemDeliveryDate = advancePurchase.ItemDeliveryDate?.let { it1 ->
                        simpleDateFormat.format(
                            it1
                        )
                    }
                    val checkIfDelivered = advancePurchase.HasBeenDelivered
                    val checkIfMoneyReturned = advancePurchase.MoneyReturned
                    val amountReturned = advancePurchase.AmountReturned.toString()
                    val shortNotes = advancePurchase.shortNotes
                    communicator = activity as CashInCommunicator
                    communicator.passAdvancedPurchase(
                        advancePurchaseId,
                        date,
                        customer,
                        itemPurchased,
                        itemPurchasedCost,
                        amountReceived,
                        receiptNumber,
                        itemDeliveryDate,
                        checkIfDelivered,
                        checkIfMoneyReturned,
                        amountReturned,
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

    override fun onItemClickListener(advancePurchase: AdvancePurchase) {
        val advancePurchaseId = advancePurchase.AdvancePurchaseId.toString()
        val date = simpleDateFormat.format(advancePurchase.Date)
        val customer = advancePurchase.Customer
        val itemPurchased = advancePurchase.ItemPurchased
        val itemPurchasedCost = advancePurchase.ItemPurchased_Cost.toString()
        val amountReceived = advancePurchase.AmountReceived.toString()
        val receiptNumber = advancePurchase.ReceiptNumber
        val itemDeliveryDate = advancePurchase.ItemDeliveryDate?.let { it1 ->
            simpleDateFormat.format(
                it1
            )
        }
        val checkIfDelivered = advancePurchase.HasBeenDelivered
        val checkIfMoneyReturned = advancePurchase.MoneyReturned
        val amountReturned = advancePurchase.AmountReturned.toString()
        val shortNotes = advancePurchase.shortNotes
        communicator = activity as CashInCommunicator
        communicator.passAdvancedPurchase(
            advancePurchaseId,
            date,
            customer,
            itemPurchased,
            itemPurchasedCost,
            amountReceived,
            receiptNumber,
            itemDeliveryDate,
            checkIfDelivered,
            checkIfMoneyReturned,
            amountReturned,
            shortNotes
        )
    }

})


val addAdvancedPurchase =
    view.findViewById<FloatingActionButton>(R.id.fabAddAdvancedPurchase)
addAdvancedPurchase.setOnClickListener {
    val fragmentManager: FragmentManager = parentFragmentManager
    fragmentManager.beginTransaction().replace(
        R.id.flCashInActivity,
        AdvancedPurchaseForm()
    ).commit()
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