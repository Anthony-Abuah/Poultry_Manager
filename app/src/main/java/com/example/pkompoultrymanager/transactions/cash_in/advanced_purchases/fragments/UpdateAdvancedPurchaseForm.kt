package com.example.pkompoultrymanager.transactions.cash_in.advanced_purchases.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.core.view.isGone
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.pkompoultrymanager.R
import com.example.pkompoultrymanager.tables.customer.fragments.AddCustomer
import com.example.pkompoultrymanager.transactions.cash_in.advanced_purchases.AdvancePurchaseViewModel
import java.text.SimpleDateFormat
import java.util.*


class UpdateAdvancedPurchaseForm : Fragment() {
    private lateinit var tvDatePicker: TextView
    private lateinit var tvDeliveryDatePicker: TextView
    private lateinit var mAdvancePurchaseViewModel: AdvancePurchaseViewModel

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_update_advanced_purchase_form, container, false)

        mAdvancePurchaseViewModel = ViewModelProvider(this)[AdvancePurchaseViewModel::class.java]

        // Declaring the various Views
        //display views
        val tvEnterDeliveryDate = view.findViewById<TextView>(R.id.tvEnterDeliveryDate_UpdateAdvancePurchaseForm)
        val tvEnterDate = view.findViewById<TextView>(R.id.tvEnterDate_UpdateAdvancePurchaseForm)
        val tvMoneyReturnedDate = view.findViewById<TextView>(R.id.tvMoneyReturnedDate_UpdateAdvancePurchaseForm)
        val tvAmountTheCustomerPaid = view.findViewById<TextView>(R.id.tvAmountTheCustomerPaid_UpdateAdvancePurchaseForm)
        val tvItemPurchasedCost = view.findViewById<TextView>(R.id.tvItemPurchasedCost_UpdateAdvancePurchaseForm)
        val tvAmountReturned = view.findViewById<TextView>(R.id.tvAmountReturned_UpdateAdvancePurchaseForm)
        val tvItemPurchased = view.findViewById<TextView>(R.id.tvItemPurchased_UpdateAdvancePurchaseForm)
        val tvSelectCustomer = view.findViewById<TextView>(R.id.tvSelectCustomer_UpdateAdvancePurchaseForm)
        val tvReceiptNumber = view.findViewById<TextView>(R.id.tvReceiptNumber_UpdateAdvancePurchaseForm)
        val tvCheckIfMoneyIsReturned = view.findViewById<TextView>(R.id.tvCheckIfMoneyIsReturned_UpdateAdvancePurchaseForm)
        val tvCheckIfProductsAreDelivered = view.findViewById<TextView>(R.id.tvCheckIfProductsAreDelivered_UpdateAdvancePurchaseForm)
        val save = view.findViewById<Button>(R.id.btnUpdate_UpdateAdvancePurchaseForm)
        val ivAddCustomer = view.findViewById<ImageView>(R.id.ivAddCustomer_UpdateAdvancePurchaseForm)
        val llMoneyReturned = view.findViewById<LinearLayout>(R.id.llMoneyReturned_UpdateAdvancePurchaseForm)

        // value holder views
        tvDatePicker = view.findViewById(R.id.tvDatePicker_UpdateAdvancePurchaseForm)
        val tvMoneyReturnedDatePicker = view.findViewById<TextView>(R.id.tvMoneyReturnedDatePicker_UpdateAdvancePurchaseForm)
        tvDeliveryDatePicker = view.findViewById(R.id.tvDeliveryDatePicker_UpdateAdvancePurchaseForm)
        val etAmountTheCustomerPaid = view.findViewById<EditText>(R.id.etAmountTheCustomerPaid_UpdateAdvancePurchaseForm)
        val etAmountReturned = view.findViewById<EditText>(R.id.etAmountReturned_UpdateAdvancePurchaseForm)
        val etItemPurchasedCost = view.findViewById<EditText>(R.id.etItemPurchasedCost_UpdateAdvancePurchaseForm)
        val etReceiptNumber = view.findViewById<EditText>(R.id.etReceiptNumber_UpdateAdvancePurchaseForm)
        val etShortNotes = view.findViewById<EditText>(R.id.etShortNotes_UpdateAdvancePurchaseForm)
        val actvItemPurchased = view.findViewById<AutoCompleteTextView>(R.id.actvItemPurchased_UpdateAdvancePurchaseForm)
        val actvSelectCustomer = view.findViewById<AutoCompleteTextView>(R.id.actvSelectCustomer_UpdateAdvancePurchaseForm)
        val rgCheckIfMoneyIsReturned = view.findViewById<RadioGroup>(R.id.rgCheckIfMoneyIsReturned_UpdateAdvancePurchaseForm)
        val rgCheckIfProductsAreDelivered = view.findViewById<RadioGroup>(R.id.rgCheckIfProductIsDelivered_UpdateAdvancePurchaseForm)
        val rbProductDeliveredYes = view.findViewById<RadioButton>(R.id.rbProductDeliveredYes_UpdateAdvancePurchaseForm)
        val rbProductDeliveredNo = view.findViewById<RadioButton>(R.id.rbProductDeliveredNo_UpdateAdvancePurchaseForm)
        val rbMoneyReturnedYes = view.findViewById<RadioButton>(R.id.rbMoneyReturnedYes_UpdateAdvancePurchaseForm)
        val rbMoneyReturnedNo = view.findViewById<RadioButton>(R.id.rbMoneyReturnedNo_UpdateAdvancePurchaseForm)



        // instantiate the calendar
        val myCalendar = Calendar.getInstance()
        val day =  myCalendar.get(Calendar.DAY_OF_MONTH)
        val month = myCalendar.get(Calendar.MONTH - 1)
        val year = myCalendar.get(Calendar.YEAR)
        val dateToday = "$day-$month-$year"


        // display and select the date
        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDate(myCalendar,tvDatePicker)
        }
        tvDatePicker.setOnClickListener {
            DatePickerDialog(requireContext(), datePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(
                Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }
        // display and select the date
        val deliveryDatePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDate(myCalendar,tvDeliveryDatePicker)
        }
        tvDeliveryDatePicker.setOnClickListener {
            DatePickerDialog(requireContext(), deliveryDatePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(
                Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        // display and select the money Returned Date
        val moneyReturnedDatePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDate(myCalendar,tvMoneyReturnedDatePicker)
        }
        tvMoneyReturnedDatePicker.setOnClickListener {
            DatePickerDialog(requireContext(), moneyReturnedDatePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(
                Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        // Customer Suggestion
        val customerSuggestions = arrayOf("Dedee", "Kwame", "Sister Akos","Madam Aisha")
        val selectCustomerAdapter = ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, customerSuggestions)
        actvSelectCustomer.setAdapter(selectCustomerAdapter)

        // Item Purchased Suggestion
        val itemPurchasedSuggestion = arrayOf("Eggs", "Birds", "Eggs and Birds")
        val adapter1 = ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, itemPurchasedSuggestion)
        actvItemPurchased.setAdapter(adapter1)

        // more info on the amount received question
        etAmountTheCustomerPaid.setOnTouchListener(View.OnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= etAmountTheCustomerPaid.getRight() - etAmountTheCustomerPaid.getCompoundDrawables()
                        .get(DRAWABLE_RIGHT).getBounds().width()
                ) {
                    val builder = AlertDialog.Builder(context)
                    val inflater = layoutInflater
                    val dialogLayout = inflater.inflate(R.layout.delete_prompt_layout, null)
                    with(builder) {
                        setTitle(R.string.AmountReceived_info)
                        setPositiveButton("Ok") { dialog, where -> }
                        setView(dialogLayout)
                        show()
                    }
                }
            }
            false
        })

        // more info on the item purchased cost question
        etItemPurchasedCost.setOnTouchListener(View.OnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= etItemPurchasedCost.getRight() - etItemPurchasedCost.getCompoundDrawables()
                        .get(DRAWABLE_RIGHT).getBounds().width()
                ) {
                    val builder = AlertDialog.Builder(context)
                    val inflater = layoutInflater
                    val dialogLayout = inflater.inflate(R.layout.delete_prompt_layout, null)
                    with(builder) {
                        setTitle(R.string.ItemPurchasedCost_info)
                        setPositiveButton("Ok") { dialog, where -> }
                        setView(dialogLayout)
                        show()
                    }
                }
            }
            false
        })

        // more info on the receipt number question
        etReceiptNumber.setOnTouchListener(View.OnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= etReceiptNumber.getRight() - etReceiptNumber.getCompoundDrawables()
                        .get(DRAWABLE_RIGHT).getBounds().width()
                ) {
                    val builder = AlertDialog.Builder(context)
                    val inflater = layoutInflater
                    val dialogLayout = inflater.inflate(R.layout.delete_prompt_layout, null)
                    with(builder) {
                        setTitle(R.string.ReceiptNumber_info)
                        setPositiveButton("Ok") { dialog, where -> }
                        setView(dialogLayout)
                        show()
                    }
                }
            }
            false
        })

        // add a new customer
        ivAddCustomer.setOnClickListener {
            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flCashInActivity,
                AddCustomer()
            ).commit()
        }


        // get the values from the communicator and store them in variables
        val theAlternativeIncomeId = arguments?.getString("id")?.toInt()
        val theDate = arguments?.getString("advancedPurchaseDate")
        val theProductPurchased = arguments?.getString("productPurchased")
        val theCustomer = arguments?.getString("customer")
        val theItemPurchasedCost = arguments?.getString("productPurchasedCost")
        val theAmountTheCustomerPaid = arguments?.getString("amountReceived")
        val theReceiptNumber = arguments?.getString("receiptNumber")
        val theProductDeliveredDate = arguments?.getString("productDeliveredDate")
        val theAmountReturned = arguments?.getString("amountReturned")
        val theCheckIfDelivered = arguments?.getBoolean("checkIfDelivered")!!
        val theCheckIfMoneyIsReturned = arguments?.getBoolean("checkIfMoneyIsReturned")!!
        val theShortNotes = arguments?.getString("shortNotes")


        // bind the views to the communicator values
        tvDatePicker.text = theDate
        if ((theProductDeliveredDate != null) || (theProductDeliveredDate != "null")) { tvDeliveryDatePicker.text = theProductDeliveredDate}
        etAmountTheCustomerPaid.setText(theAmountTheCustomerPaid)
        etReceiptNumber.setText(theReceiptNumber)
        etItemPurchasedCost.setText(theItemPurchasedCost)
        etAmountReturned.setText(theAmountReturned)
        actvSelectCustomer.setText(theCustomer)
        actvItemPurchased.setText(theProductPurchased)
        etShortNotes.setText(theShortNotes)
        if(theCheckIfDelivered){rbProductDeliveredYes.isChecked = true} else rbProductDeliveredNo.isChecked = true
        if(theCheckIfMoneyIsReturned){rbMoneyReturnedYes.isChecked = true} else rbMoneyReturnedNo.isChecked = true


        if (rbProductDeliveredNo.isChecked){
            tvEnterDeliveryDate.isGone = true
            tvDeliveryDatePicker.isGone = true
            tvCheckIfMoneyIsReturned.isGone = false
            rgCheckIfMoneyIsReturned.isGone = false
        }
        else if (rbProductDeliveredYes.isChecked){
            tvCheckIfMoneyIsReturned.isGone = true
            rgCheckIfMoneyIsReturned.isGone =true
        }
        if (rbMoneyReturnedNo.isChecked){
            etAmountReturned.setText("0.0")
            llMoneyReturned.isGone =true
        }


        rgCheckIfProductsAreDelivered.setOnCheckedChangeListener { group, checkedId ->
            kotlin.run {
                when(checkedId){
                    R.id.rbProductDeliveredYes_UpdateAdvancePurchaseForm -> {
                        tvEnterDeliveryDate.setTextColor(resources.getColor(R.color.black))
                        tvDeliveryDatePicker.setTextColor(resources.getColor(R.color.black))
                        tvEnterDeliveryDate.isGone = false
                        tvDeliveryDatePicker.isGone = false
                        tvCheckIfMoneyIsReturned.isGone = true
                        rgCheckIfMoneyIsReturned.isGone =true
                        tvAmountReturned.setTextColor(resources.getColor(R.color.light_black))
                        etAmountReturned.setTextColor(resources.getColor(R.color.light_black))
                        etAmountReturned.setText("0.0")
                        etAmountReturned.inputType = EditorInfo.TYPE_NULL
                    }
                    R.id.rbProductDeliveredNo_UpdateAdvancePurchaseForm -> {
                        tvDeliveryDatePicker.text = ""
                        tvEnterDeliveryDate.isGone = true
                        tvDeliveryDatePicker.isGone = true
                        tvCheckIfMoneyIsReturned.isGone = false
                        rgCheckIfMoneyIsReturned.isGone = false
                        tvAmountReturned.setTextColor(resources.getColor(R.color.black))
                        etAmountReturned.setTextColor(resources.getColor(R.color.black))
                        etAmountReturned.inputType = (EditorInfo.TYPE_NUMBER_FLAG_SIGNED + EditorInfo.TYPE_CLASS_NUMBER + EditorInfo.TYPE_NUMBER_FLAG_DECIMAL )
                    }
                }
            }
        }
        rgCheckIfMoneyIsReturned.setOnCheckedChangeListener { group, checkedId ->
            kotlin.run {
                when(checkedId){
                    R.id.rbMoneyReturnedYes_UpdateAdvancePurchaseForm -> {
                        tvAmountReturned.setTextColor(resources.getColor(R.color.black))
                        etAmountReturned.setTextColor(resources.getColor(R.color.black))
                        llMoneyReturned.isGone= false
                        etAmountReturned.inputType = EditorInfo.TYPE_NUMBER_FLAG_SIGNED + EditorInfo.TYPE_CLASS_NUMBER + EditorInfo.TYPE_NUMBER_FLAG_DECIMAL
                    }
                    R.id.rbMoneyReturnedNo_UpdateAdvancePurchaseForm -> {
                        etAmountReturned.setText("0.0")
                        llMoneyReturned.isGone =true
                    }
                }
            }
        }






        // insert advance purchase info into the database
        save.setOnClickListener {
            if (theAlternativeIncomeId != null) {
                insertDataToDatabase(
                    theAlternativeIncomeId,
                    tvDatePicker,
                    tvEnterDate,
                    tvSelectCustomer,
                    actvSelectCustomer,
                    tvItemPurchased,
                    actvItemPurchased,
                    tvAmountTheCustomerPaid,
                    etAmountTheCustomerPaid,
                    tvItemPurchasedCost,
                    etItemPurchasedCost,
                    tvEnterDeliveryDate,
                    tvDeliveryDatePicker,
                    rbProductDeliveredYes,
                    rbMoneyReturnedYes,
                    tvAmountReturned,
                    etAmountReturned,
                    etReceiptNumber,
                    tvMoneyReturnedDate,
                    tvMoneyReturnedDatePicker,
                    dateToday,
                    etShortNotes
                )
            }
        }





        return view
    }
    private fun updateDate(myCalendar: Calendar, datePicker: TextView) {
        val myFormat =  "EEE, dd MMM, yyyy"
        val simpleDateFormat = SimpleDateFormat(myFormat, Locale.UK)
        datePicker.text = simpleDateFormat.format(myCalendar.time)
    }
    private fun insertDataToDatabase(
        advancePurchaseId: Int,
        tvDatePicker: TextView,
        tvEnterDate: TextView,
        tvSelectCustomer: TextView,
        actvSelectCustomer: AutoCompleteTextView,
        tvItemPurchased: TextView,
        actvItemPurchased: AutoCompleteTextView,
        tvAmountTheCustomerPaid: TextView,
        etAmountTheCustomerPaid: EditText,
        tvItemPurchasedCost: TextView,
        etItemPurchasedCost: EditText,
        tvEnterDeliveredDate: TextView,
        tvDeliveredDatePicker: TextView,
        rbProductIsDelivered: RadioButton,
        rbMoneyIsReturned: RadioButton,
        tvAmountReturned: TextView,
        etAmountReturned: EditText,
        etReceiptNumber: EditText,
        tvMoneyReturnedDate: TextView,
        tvMoneyReturnedDatePicker: TextView,
        dateToday: String,
        etShortNotes: EditText
    ) {
        val date = tvDatePicker.text.toString()
        val customer = actvSelectCustomer.text.toString()
        val amountReceived = etAmountTheCustomerPaid.text.toString()
        val itemPurchased = actvItemPurchased.text.toString()
        val itemPurchasedCost = etItemPurchasedCost.text.toString()
        val receiptId = etReceiptNumber.text.toString()
        val shortNotes = etShortNotes.text.toString()

        val myFormat =  "EEE, dd MMM, yyyy"
        val simpleDateFormat = SimpleDateFormat(myFormat, Locale.UK)

        val dateTodayFormat =  "dd-MM-yyyy"
        val simpleDateTodayFormat = SimpleDateFormat(dateTodayFormat, Locale.UK)
        val theCurrentDate = simpleDateTodayFormat.parse(dateToday)


        if (checkIfEmpty(date)) {
            tvEnterDate.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Enter Date", Toast.LENGTH_LONG).show()}
        else if (checkIfDateIsNotBefore(theCurrentDate!!,simpleDateFormat.parse(date)!!)){
            tvEnterDate.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "$date isn't here yet", Toast.LENGTH_SHORT).show()
            Toast.makeText(requireContext(), "Use $dateToday or before", Toast.LENGTH_SHORT).show()
        }else if (checkIfEmpty(customer)){
            tvEnterDate.setTextColor(resources.getColor(R.color.black))
            tvSelectCustomer.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Select Customer", Toast.LENGTH_LONG).show()
        }
        else if (checkIfEmpty(itemPurchased)){
            tvSelectCustomer.setTextColor(resources.getColor(R.color.black))
            tvItemPurchased.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Select Item Purchased", Toast.LENGTH_LONG).show()
        }
        else if (checkIfEmpty(itemPurchasedCost)){
            tvItemPurchased.setTextColor(resources.getColor(R.color.black))
            tvItemPurchasedCost.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Enter Cost of Item Purchased", Toast.LENGTH_LONG).show()
        }
        else if (checkIfEmpty(amountReceived)){
            tvItemPurchasedCost.setTextColor(resources.getColor(R.color.black))
            tvAmountTheCustomerPaid.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Enter Amount Received", Toast.LENGTH_LONG).show()
        }
        else if (amountReceived.toDouble() != itemPurchasedCost.toDouble()){
            tvAmountTheCustomerPaid.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Cost of Products purchased must be equal to the amount received", Toast.LENGTH_LONG).show()
        }
        else if (rbProductIsDelivered.isChecked && checkIfEmpty(tvDeliveredDatePicker.text.toString())){
            tvAmountTheCustomerPaid.setTextColor(resources.getColor(R.color.black))
            tvEnterDeliveredDate.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Enter the date products were delivered", Toast.LENGTH_LONG).show()
        }
        else if (!rbProductIsDelivered.isChecked && rbMoneyIsReturned.isChecked && (checkIfEmpty(etAmountReturned.text.toString()) || etAmountReturned.text.toString().toDouble() <= 0.0 )){
            tvEnterDeliveredDate.setTextColor(resources.getColor(R.color.black))
            tvAmountReturned.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Enter the amount returned", Toast.LENGTH_LONG).show()
        }
        else if (!rbProductIsDelivered.isChecked && rbMoneyIsReturned.isChecked && (checkIfEmpty(tvMoneyReturnedDatePicker.text.toString()))){
            tvAmountReturned.setTextColor(resources.getColor(R.color.black))
            tvMoneyReturnedDate.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Enter the date the money was returned", Toast.LENGTH_LONG).show()
        }
        else {
            val deliveryDate = if (rbProductIsDelivered.isChecked){tvDeliveredDatePicker.text.toString()} else if(rbMoneyIsReturned.isChecked){tvMoneyReturnedDatePicker.text.toString()} else null
            val hasItemBeenDelivered = rbProductIsDelivered.isChecked

            val hasMoneyBeenReturned = if (rbProductIsDelivered.isChecked) {false} else rbMoneyIsReturned.isChecked
            val amountOfMoneyReturned = if (rbMoneyIsReturned.isChecked) {etAmountReturned.text.toString().toDouble()} else 0.0


            val newDate = simpleDateFormat.parse(date)
            val newDeliveredDate = if (deliveryDate != null )simpleDateFormat.parse(deliveryDate) else null
            val advancePurchaseDate = newDate?.time?.let { java.sql.Date(it)}
            val productDeliveredDate = newDeliveredDate?.time?.let { java.sql.Date(it)}

            if (advancePurchaseDate != null) {
                if (productDeliveredDate != null) {
                    if (checkIfDateIsNotBefore(advancePurchaseDate,productDeliveredDate)) {
                        mAdvancePurchaseViewModel.updateAdvancePurchase(
                            advancePurchaseId,
                            advancePurchaseDate,
                            customer, itemPurchased,
                            itemPurchasedCost,
                            amountReceived.toDouble(),
                            receiptId,
                            hasItemBeenDelivered,
                            productDeliveredDate,
                            hasMoneyBeenReturned,
                            amountOfMoneyReturned,
                            shortNotes
                        )
                        Toast.makeText(requireContext(), "Successfully Updated", Toast.LENGTH_LONG).show()

                        val fragmentManager: FragmentManager = parentFragmentManager
                        fragmentManager.beginTransaction().replace(
                            R.id.flCashInActivity,
                            AdvancedPurchaseList()
                        ).commit()
                    }
                    else Toast.makeText(requireContext(), "Product can not be delivered before or on ${simpleDateFormat.format(advancePurchaseDate)}", Toast.LENGTH_LONG).show()
                }
                else{
                    mAdvancePurchaseViewModel.updateAdvancePurchase(
                        advancePurchaseId,
                        advancePurchaseDate,
                        customer, itemPurchased,
                        itemPurchasedCost,
                        amountReceived.toDouble(),
                        receiptId,
                        hasItemBeenDelivered,
                        productDeliveredDate,
                        hasMoneyBeenReturned,
                        amountOfMoneyReturned,
                        shortNotes
                    )
                    Toast.makeText(requireContext(), "Successfully Updated", Toast.LENGTH_LONG).show()

                    val fragmentManager: FragmentManager = parentFragmentManager
                    fragmentManager.beginTransaction().replace(
                        R.id.flCashInActivity,
                        AdvancedPurchaseList()
                    ).commit()
                }

            }
        }
    }

    private fun checkIfEmpty(value: String): Boolean {
        return (TextUtils.isEmpty(value))
    }
    private fun checkIfDateIsNotBefore(principalDate: Date, comparisonDate: Date): Boolean {
        return (principalDate < comparisonDate)
    }
}