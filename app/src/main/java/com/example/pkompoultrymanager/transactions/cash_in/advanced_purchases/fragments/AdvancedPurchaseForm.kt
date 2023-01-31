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
import android.widget.*
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.pkompoultrymanager.R
import com.example.pkompoultrymanager.tables.customer.fragments.AddCustomer
import com.example.pkompoultrymanager.transactions.cash_in.advanced_purchases.AdvancePurchase
import com.example.pkompoultrymanager.transactions.cash_in.advanced_purchases.AdvancePurchaseViewModel
import java.text.SimpleDateFormat
import java.util.*

class AdvancedPurchaseForm : Fragment() {
    private lateinit var tvDatePicker: TextView
    private lateinit var mAdvancePurchaseViewModel: AdvancePurchaseViewModel

    @SuppressLint("ClickableViewAccessibility")
     override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_advanced_purchase_form, container, false)

        mAdvancePurchaseViewModel = ViewModelProvider(this)[AdvancePurchaseViewModel::class.java]

        // Declaring the various Views
        //display views
        val tvEnterDate = view.findViewById<TextView>(R.id.tvEnterDate_AdvancePurchaseForm)
        val tvAmountTheCustomerPaid = view.findViewById<TextView>(R.id.tvAmountTheCustomerPaid_AdvancePurchaseForm)
        val tvItemPurchasedCost = view.findViewById<TextView>(R.id.tvItemPurchasedCost_AdvancePurchaseForm)
        val tvItemPurchased = view.findViewById<TextView>(R.id.tvItemPurchased_AdvancePurchaseForm)
        val tvSelectCustomer = view.findViewById<TextView>(R.id.tvSelectCustomer_AdvancePurchaseForm)
        val tvReceiptNumber = view.findViewById<TextView>(R.id.tvReceiptNumber_AdvancePurchaseForm)
        val save = view.findViewById<Button>(R.id.btnSave_AdvancePurchaseForm)
        val ivAddCustomer = view.findViewById<ImageView>(R.id.ivAddCustomer_AdvancePurchaseForm)

        // value holder views
        tvDatePicker = view.findViewById(R.id.tvDatePicker_AdvancePurchaseForm)
        val etAmountTheCustomerPaid = view.findViewById<EditText>(R.id.etAmountTheCustomerPaid_AdvancePurchaseForm)
        val etItemPurchasedCost = view.findViewById<EditText>(R.id.etItemPurchasedCost_AdvancePurchaseForm)
        val etReceiptNumber = view.findViewById<EditText>(R.id.etReceiptNumber_AdvancePurchaseForm)
        val etShortNotes = view.findViewById<EditText>(R.id.etShortNotes_AdvancePurchaseForm)
        val actvItemPurchased = view.findViewById<AutoCompleteTextView>(R.id.actvItemPurchased_AdvancePurchaseForm)
        val actvSelectCustomer = view.findViewById<AutoCompleteTextView>(R.id.actvSelectCustomer_AdvancePurchaseForm)

        // instantiate the calendar
        val myCalendar = Calendar.getInstance()

        // display and select the date
        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDate(myCalendar)
        }
        tvDatePicker.setOnClickListener {
            DatePickerDialog(requireContext(), datePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(
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

        // insert advance purchase info into the database
        save.setOnClickListener {
             insertDataToDatabase(
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
                 etReceiptNumber,
                 etShortNotes
             )
        }


        return view
    }
    private fun updateDate(myCalendar: Calendar) {
        val myFormat =  "dd-MM-yyyy"
        val simpleDateFormat = SimpleDateFormat(myFormat, Locale.UK)
        tvDatePicker.text = simpleDateFormat.format(myCalendar.time)
    }
    private fun insertDataToDatabase(
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
        etReceiptNumber: EditText,
        etShortNotes: EditText
    ) {
        val date = tvDatePicker.text.toString()
        val customer = actvSelectCustomer.text.toString()
        val amountReceived = etAmountTheCustomerPaid.text.toString()
        val itemPurchased = actvItemPurchased.text.toString()
        val itemPurchasedCost = etItemPurchasedCost.text.toString()
        val receiptId = etReceiptNumber.text.toString()
        val shortNotes = etShortNotes.text.toString()

        val itemDeliveryDate = null
        val hasItemBeenDelivered = false
        val hasMoneyBeenReturned = false
        val amountOfMoneyReturned = 0.00

        if (checkIfEmpty(date)) {
            tvEnterDate.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Enter Date", Toast.LENGTH_LONG).show()}
        else if (checkIfEmpty(customer)){
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
        else {
            val myFormat =  "dd-MM-yyyy"
            val simpleDateFormat = SimpleDateFormat(myFormat, Locale.UK)
            val advancePurchaseDate = simpleDateFormat.parse(date)


            val advancePurchase = advancePurchaseDate?.let {
                AdvancePurchase(0,
                    it,customer,itemPurchased,itemPurchasedCost.toDouble(),amountReceived.toDouble(),receiptId,itemDeliveryDate,hasItemBeenDelivered,hasMoneyBeenReturned,amountOfMoneyReturned,shortNotes)
            }

            if (advancePurchase != null) {
                mAdvancePurchaseViewModel.addAdvancePurchase(advancePurchase)
            }

            Toast.makeText(requireContext(), "Successfully Added", Toast.LENGTH_LONG).show()

            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flCashInActivity,
                AdvancedPurchaseList()
            ).commit()
        }
    }
    private fun checkIfEmpty(value: String): Boolean {
        return (TextUtils.isEmpty(value))
    }
}