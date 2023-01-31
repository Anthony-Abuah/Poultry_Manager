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
import com.example.pkompoultrymanager.transactions.cash_out.capital_expenses.CapitalExpensesViewModel
import java.text.SimpleDateFormat
import java.util.*


class UpdateCapitalExpensesForm : Fragment() {
    private lateinit var tvDatePicker: TextView
    private lateinit var mCapitalExpensesViewModel: CapitalExpensesViewModel
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_update_capital_expenses_form, container, false)

        mCapitalExpensesViewModel = ViewModelProvider(this)[CapitalExpensesViewModel::class.java]


        // Declaring the various Views
        //display views
        val tvEnterDate = view.findViewById<TextView>(R.id.tvEnterDate_UpdateCapitalExpensesForm)
        val tvExpenseAmount = view.findViewById<TextView>(R.id.tvExpenseAmount_UpdateCapitalExpensesForm)
        val tvSelectExpenses = view.findViewById<TextView>(R.id.tvSelectExpenses_UpdateCapitalExpensesForm)
        val tvSelectPaymentMethod = view.findViewById<TextView>(R.id.tvSelectPaymentMethod_UpdateCapitalExpensesForm)
        val save = view.findViewById<Button>(R.id.btnSave_UpdateCapitalExpensesForm)

        // value holder views
        tvDatePicker = view.findViewById(R.id.tvDatePicker_UpdateCapitalExpensesForm)
        val etExpenseAmount = view.findViewById<EditText>(R.id.etExpenseAmount_UpdateCapitalExpensesForm)
        val etTransactionId = view.findViewById<EditText>(R.id.etTransactionID_UpdateCapitalExpensesForm)
        val etShortNotes = view.findViewById<EditText>(R.id.etShortNotes_UpdateCapitalExpensesForm)
        val actvSelectExpenses = view.findViewById<AutoCompleteTextView>(R.id.actvSelectExpenses_UpdateCapitalExpensesForm)
        val actvPaymentMethod = view.findViewById<AutoCompleteTextView>(R.id.actvSelectPaymentMethod_UpdateCapitalExpensesForm)

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

        // get the values from the communicator and store them in variables
        val theCapitalExpenseId = arguments?.getInt("capitalExpensesId")
        val theExpenseDate = arguments?.getString("expenseDate")
        val theExpenseName = arguments?.getString("expenseName")
        val thePaymentMethod = arguments?.getString("paymentMethod")
        val theExpenseAmount = arguments?.getString("expenseAmount")
        val theTransactionId = arguments?.getString("transactionID")
        val theShortNotes = arguments?.getString("shortNotes")


        // bind the views to the communicator values
        tvDatePicker.text = theExpenseDate
        etExpenseAmount.setText(theExpenseAmount)
        etTransactionId.setText(theTransactionId)
        actvSelectExpenses.setText(theExpenseName)
        actvPaymentMethod.setText(thePaymentMethod)
        etShortNotes.setText(theShortNotes)

        // save expenses into database
        save.setOnClickListener {
            if (theCapitalExpenseId != null) {
                insertDataToDatabase(
                    theCapitalExpenseId.toInt(),
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
        }



        return view
    }
    private fun updateDate(myCalendar: Calendar) {
        val myFormat =  "dd-MM-yyyy"
        val simpleDateFormat = SimpleDateFormat(myFormat, Locale.UK)
        tvDatePicker.text = simpleDateFormat.format(myCalendar.time)
    }
    private fun insertDataToDatabase(
        capitalExpenseId: Int,
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
            val newDate = simpleDateFormat.parse(date)
            val capitalExpensesDate = newDate?.time?.let { java.sql.Date(it)}
            //

            if (capitalExpensesDate != null) {
                mCapitalExpensesViewModel.updateCapitalExpensesInfo(capitalExpenseId,capitalExpensesDate,expenseName,paymentMethod, capitalExpensesAmount.toDouble(),transactionId, shortNotes)
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