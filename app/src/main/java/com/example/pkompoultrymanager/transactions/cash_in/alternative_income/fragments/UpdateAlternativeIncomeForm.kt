package com.example.pkompoultrymanager.transactions.cash_in.alternative_income.fragments

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
import com.example.pkompoultrymanager.transactions.cash_in.alternative_income.AlternativeIncome
import com.example.pkompoultrymanager.transactions.cash_in.alternative_income.AlternativeIncomeViewModel
import java.text.SimpleDateFormat
import java.util.*


class UpdateAlternativeIncomeForm : Fragment() {
    private lateinit var mAlternativeIncomeViewModel: AlternativeIncomeViewModel
    private lateinit var tvDatePicker: TextView
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_update_advanced_purchase_form, container, false)

        mAlternativeIncomeViewModel = ViewModelProvider(this)[AlternativeIncomeViewModel::class.java]


        // Declaring the various Views
        //display views
        val tvEnterDate = view.findViewById<TextView>(R.id.tvEnterDate_UpdateAlternativeIncomeForm)
        val tvAmountTheCustomerPaid = view.findViewById<TextView>(R.id.tvAmountTheCustomerPaid_UpdateAlternativeIncomeForm)
        val tvQuantityOfProduct = view.findViewById<TextView>(R.id.tvQuantityOfProduct_UpdateAlternativeIncomeForm)
        val tvSelectCustomer = view.findViewById<TextView>(R.id.tvSelectCustomer_UpdateAlternativeIncomeForm)
        val tvItemPurchasedCost = view.findViewById<TextView>(R.id.tvCostOfProduct_UpdateAlternativeIncomeForm)
        val tvSelectProductOrService = view.findViewById<TextView>(R.id.tvProductOrService_UpdateAlternativeIncomeForm)
        val ivAddCustomer = view.findViewById<ImageView>(R.id.ivAddCustomer_UpdateAlternativeIncomeForm)
        val back = view.findViewById<ImageButton>(R.id.ibBack_UpdateAlternativeIncomeForm)
        val save = view.findViewById<Button>(R.id.btnSave_UpdateAlternativeIncomeForm)

        // value holder view
        tvDatePicker = view.findViewById(R.id.tvDatePicker_UpdateAlternativeIncomeForm)
        val etAmountTheCustomerPaid = view.findViewById<EditText>(R.id.etAmountTheCustomerPaid_UpdateAlternativeIncomeForm)
        val etReceiptNumber = view.findViewById<EditText>(R.id.etReceiptNumber_UpdateAlternativeIncomeForm)
        val etQuantityOfProduct = view.findViewById<EditText>(R.id.etQuantityOfProduct_UpdateAlternativeIncomeForm)
        val etItemPurchasedCost = view.findViewById<EditText>(R.id.etCostOfProduct_UpdateAlternativeIncomeForm)
        val etShortNotes = view.findViewById<EditText>(R.id.etShortNotes_UpdateAlternativeIncomeForm)
        val actvSelectProductOrService = view.findViewById<AutoCompleteTextView>(R.id.actvProductOrService_UpdateAlternativeIncomeForm)
        val actvSelectCustomer = view.findViewById<AutoCompleteTextView>(R.id.actvSelectCustomer_UpdateAlternativeIncomeForm)

        // Instantiate the calendar
        val myCalendar = Calendar.getInstance()

        // display and select date
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

        // Product or Services Suggestion
        val productOrServicesSuggestions = arrayOf("Egg Shells", "Feathers", "Labour","Expertise")
        val selectProductOrServicesAdapter = ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, productOrServicesSuggestions)
        actvSelectProductOrService.setAdapter(selectProductOrServicesAdapter)

        // Customer Suggestion
        val customerSuggestions = arrayOf("Dedee", "Kwame", "Sister Akos","Madam Aisha")
        val selectCustomerAdapter = ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, customerSuggestions)
        actvSelectCustomer.setAdapter(selectCustomerAdapter)

        back.setOnClickListener {
            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flCashInActivity,
                AlternativeIncomeList()
            ).commit()
        }

        // more info on the amount paid by customer question
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
                        setTitle("Enter the amount received from the sale of products or services")
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

        // more info on the quantity of product question
        etQuantityOfProduct.setOnTouchListener(View.OnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= etQuantityOfProduct.getRight() - etQuantityOfProduct.getCompoundDrawables()
                        .get(DRAWABLE_RIGHT).getBounds().width()
                ) {
                    val builder = AlertDialog.Builder(context)
                    val inflater = layoutInflater
                    val dialogLayout = inflater.inflate(R.layout.delete_prompt_layout, null)
                    with(builder) {
                        setTitle(R.string.ProductQuantity_info)
                        setPositiveButton("Ok") { dialog, where -> }
                        setView(dialogLayout)
                        show()
                    }
                }
            }
            false
        })

        // add customer
        ivAddCustomer.setOnClickListener {
            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flCashInActivity,
                AddCustomer()
            ).commit()
        }

        // get the values from the communicator and store them in variables
        val theAlternativeIncomeId = arguments?.getInt("alternativeIncomeId")
        val theDate = arguments?.getString("date")
        val theProductPurchased = arguments?.getString("itemPurchased")
        val theCustomer = arguments?.getString("customer")
        val theProductPurchasedCost = arguments?.getString("productPurchasedCost")
        val theAmountTheCustomerPaid = arguments?.getString("amountReceived")
        val theReceiptNumber = arguments?.getString("receiptNumber")
        val theShortNotes = arguments?.getString("shortNotes")


        // bind the views to the communicator values
        tvDatePicker.text = theDate
        etAmountTheCustomerPaid.setText(theAmountTheCustomerPaid)
        etReceiptNumber.setText(theReceiptNumber)
        etItemPurchasedCost.setText(theProductPurchasedCost)
        actvSelectCustomer.setText(theCustomer)
        actvSelectProductOrService.setText(theProductPurchased)
        etShortNotes.setText(theShortNotes)




        // save the alternative income info into the database
        save.setOnClickListener {
            if (theAlternativeIncomeId != null) {
                updateDataInDatabase(theAlternativeIncomeId.toInt(),tvDatePicker, tvEnterDate, tvSelectCustomer, actvSelectCustomer, tvSelectProductOrService, actvSelectProductOrService, tvItemPurchasedCost, etItemPurchasedCost, etQuantityOfProduct, tvAmountTheCustomerPaid, etAmountTheCustomerPaid, etReceiptNumber, etShortNotes)
            }
        }



        return view
    }
    private fun updateDate(myCalendar: Calendar) {
        val myFormat =  "dd-MM-yyyy"
        val simpleDateFormat = SimpleDateFormat(myFormat, Locale.UK)
        tvDatePicker.text = simpleDateFormat.format(myCalendar.time)
    }
    private fun updateDataInDatabase(
        alternativeIncomeId: Int,
        tvDatePicker: TextView,
        tvEnterDate: TextView,
        tvSelectCustomer: TextView,
        actvSelectCustomer: AutoCompleteTextView,
        tvSelectProductOrService: TextView,
        actvSelectProductOrService: AutoCompleteTextView,
        tvItemPurchasedCost: TextView,
        etItemPurchasedCost: EditText,
        etQuantityOfProduct: EditText,
        tvAmountTheCustomerPaid: TextView,
        etAmountTheCustomerPaid: EditText,
        etReceiptNumber: EditText,
        etShortNotes: EditText
    ) {
        val date = tvDatePicker.text.toString()
        val customer = actvSelectCustomer.text.toString()
        val amountTheCustomerPaid = etAmountTheCustomerPaid.text.toString()
        val itemPurchased = actvSelectProductOrService.text.toString()
        val itemPurchasedCost = etItemPurchasedCost.text.toString()
        val itemPurchasedQuantity = etQuantityOfProduct.text.toString()
        val receiptNumber = etReceiptNumber.text.toString()
        val shortNotes = etShortNotes.text.toString()


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
            tvSelectProductOrService.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Select Product", Toast.LENGTH_LONG).show()
        }
        else if (checkIfEmpty(itemPurchasedCost)){
            tvSelectProductOrService.setTextColor(resources.getColor(R.color.black))
            tvItemPurchasedCost.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Enter Cost of Item Purchased", Toast.LENGTH_LONG).show()
        }
        else if (checkIfEmpty(amountTheCustomerPaid)){
            tvItemPurchasedCost.setTextColor(resources.getColor(R.color.black))
            tvAmountTheCustomerPaid.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Enter Amount Received", Toast.LENGTH_LONG).show()
        }
        else {
            val myFormat =  "dd-MM-yyyy"
            val simpleDateFormat = SimpleDateFormat(myFormat, Locale.UK)
            val newDate = simpleDateFormat.parse(date)
            val alternativeIncomeDate = newDate?.time?.let { java.sql.Date(it)}

            val amountOwed = itemPurchasedCost.toDouble()-amountTheCustomerPaid.toDouble()

            // add alternative income to database via viewModel
            if (alternativeIncomeDate != null) {
                mAlternativeIncomeViewModel.updateAlternativeIncome(alternativeIncomeId, alternativeIncomeDate, customer, itemPurchased, itemPurchasedQuantity.toInt(), itemPurchasedCost.toDouble(), amountTheCustomerPaid.toDouble(),amountOwed, receiptNumber, shortNotes)
            }

            Toast.makeText(requireContext(), "Successfully Added", Toast.LENGTH_LONG).show()

            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flCashInActivity,
                AlternativeIncomeList()
            ).commit()
        }
    }
    private fun checkIfEmpty(value: String): Boolean {
        return (TextUtils.isEmpty(value))
    }

}