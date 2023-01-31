package com.example.pkompoultrymanager.transactions.cash_out.capital_expenses.fragments

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
import com.example.pkompoultrymanager.transactions.cash_out.capital_expenses.CapitalExpenses
import com.example.pkompoultrymanager.transactions.cash_out.capital_expenses.CapitalExpensesViewModel
import java.text.SimpleDateFormat
import java.util.*

class CapitalExpensesForm : Fragment() {
    private lateinit var tvDatePicker: TextView
    private lateinit var mCapitalExpensesViewModel: CapitalExpensesViewModel

    @SuppressLint("ClickableViewAccessibility")
     override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_capital_expenses_form, container, false)

        mCapitalExpensesViewModel = ViewModelProvider(this)[CapitalExpensesViewModel::class.java]


        // Declaring the various Views
        //display views
        val tvEnterDate = view.findViewById<TextView>(R.id.tvEnterDate_CapitalExpensesForm)
        val tvExpenseAmount = view.findViewById<TextView>(R.id.tvExpenseAmount_CapitalExpensesForm)
        val tvSelectExpenses = view.findViewById<TextView>(R.id.tvSelectExpenses_CapitalExpensesForm)
        val tvSelectPaymentMethod = view.findViewById<TextView>(R.id.tvSelectPaymentMethod_CapitalExpensesForm)
        val save = view.findViewById<Button>(R.id.btnSave_CapitalExpensesForm)

        // value holder views
        tvDatePicker = view.findViewById(R.id.tvDatePicker_CapitalExpensesForm)
        val etExpenseAmount = view.findViewById<EditText>(R.id.etExpenseAmount_CapitalExpensesForm)
        val etTransactionId = view.findViewById<EditText>(R.id.etTransactionID_CapitalExpensesForm)
        val etShortNotes = view.findViewById<EditText>(R.id.etShortNotes_CapitalExpensesForm)
        val actvSelectExpenses = view.findViewById<AutoCompleteTextView>(R.id.actvSelectExpenses_CapitalExpensesForm)
        val actvPaymentMethod = view.findViewById<AutoCompleteTextView>(R.id.actvSelectPaymentMethod_CapitalExpensesForm)

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

        // Expenses Suggestion
         val expenseSuggestions = arrayOf("Structure", "Electricity Generator","Water Tank", "Water Pump","Battery Cage")
         val selectExpensesAdapter = ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, expenseSuggestions)
         actvSelectExpenses.setAdapter(selectExpensesAdapter)

        // Payment Method Suggestion
         val paymentMethodSuggestions = arrayOf("Cash", "Cheque","Mobile Money", "Bank Transaction")
         val selectPaymentMethodAdapter = ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, paymentMethodSuggestions)
         actvPaymentMethod.setAdapter(selectPaymentMethodAdapter)


        // more info on the expense amount question
         etExpenseAmount.setOnTouchListener(View.OnTouchListener { v, event ->
             val DRAWABLE_RIGHT = 2
             if (event.action == MotionEvent.ACTION_UP) {
                 if (event.rawX >= etExpenseAmount.getRight() - etExpenseAmount.getCompoundDrawables()
                         .get(DRAWABLE_RIGHT).getBounds().width()
                 ) {
                     val builder = AlertDialog.Builder(context)
                     val inflater = layoutInflater
                     val dialogLayout = inflater.inflate(R.layout.delete_prompt_layout, null)
                     with(builder) {
                         setTitle(R.string.ExpenseAmount_info)
                         setPositiveButton("Ok") { dialog, where -> }
                         setView(dialogLayout)
                         show()
                     }
                 }
             }
             false
         })

        // more info on the transaction id
         etTransactionId.setOnTouchListener(View.OnTouchListener { v, event ->
             val DRAWABLE_RIGHT = 2
             if (event.action == MotionEvent.ACTION_UP) {
                 if (event.rawX >= etTransactionId.getRight() - etTransactionId.getCompoundDrawables()
                         .get(DRAWABLE_RIGHT).getBounds().width()
                 ) {
                     val builder = AlertDialog.Builder(context)
                     val inflater = layoutInflater
                     val dialogLayout = inflater.inflate(R.layout.delete_prompt_layout, null)
                     with(builder) {
                         setTitle(R.string.TransactionID_info)
                         setPositiveButton("Ok") { dialog, where -> }
                         setView(dialogLayout)
                         show()
                     }
                 }
             }
             false
         })

        // save expenses into database
        save.setOnClickListener {
            insertDataToDatabase(
                tvDatePicker,
                tvEnterDate,
                tvSelectExpenses,
                actvSelectExpenses,
                actvPaymentMethod,
                tvExpenseAmount,
                etExpenseAmount,
                etTransactionId,
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
        tvSelectExpenses: TextView,
        actvSelectExpenses: AutoCompleteTextView,
        actvPaymentMethod: AutoCompleteTextView,
        tvExpenseAmount: TextView,
        etExpenseAmount: EditText,
        etTransactionId: EditText,
        etShortNotes: EditText,
    ) {
        val date = tvDatePicker.text.toString()
        val expenseName = actvSelectExpenses.text.toString()
        val capitalExpensesAmount = etExpenseAmount.text.toString()
        val paymentMethod = actvPaymentMethod.text.toString()
        val transactionId = etTransactionId.text.toString()
        val shortNotes = etShortNotes.text.toString()


        if (checkIfEmpty(date)) {
            tvEnterDate.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Enter Date", Toast.LENGTH_LONG).show()}
        else if (checkIfEmpty(expenseName)){
            tvEnterDate.setTextColor(resources.getColor(R.color.black))
            tvSelectExpenses.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Select Expense", Toast.LENGTH_LONG).show()
        }
        else if (checkIfEmpty(capitalExpensesAmount)){
            tvSelectExpenses.setTextColor(resources.getColor(R.color.black))
            tvExpenseAmount.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Enter Capital Expenses Amount", Toast.LENGTH_LONG).show()
        }
        else {
            val myFormat =  "dd-MM-yyyy"
            val simpleDateFormat = SimpleDateFormat(myFormat, Locale.UK)
            val capitalExpensesDate = simpleDateFormat.parse(date)
            //
            val capitalExpenses = capitalExpensesDate?.let {
                CapitalExpenses(0,
                    it,expenseName,paymentMethod,capitalExpensesAmount.toDouble(),transactionId,shortNotes)
            }
            //
            if (capitalExpenses != null) {
                mCapitalExpensesViewModel.addCapitalExpenses(capitalExpenses)
            }

            Toast.makeText(requireContext(), "Successfully Added", Toast.LENGTH_LONG).show()

            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flCashOutActivity,
                CapitalExpensesList()
            ).commit()
        }
    }
    private fun checkIfEmpty(value: String): Boolean {
        return (TextUtils.isEmpty(value))
    }
}