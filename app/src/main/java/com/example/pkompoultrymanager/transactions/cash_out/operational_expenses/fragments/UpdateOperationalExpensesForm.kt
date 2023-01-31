package com.example.pkompoultrymanager.transactions.cash_out.operational_expenses.fragments

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
import com.example.pkompoultrymanager.transactions.cash_out.operational_expenses.OperationalExpensesViewModel
import java.text.SimpleDateFormat
import java.util.*


class UpdateOperationalExpensesForm : Fragment() {
    private lateinit var tvDatePicker: TextView
    private lateinit var mOperationalExpensesViewModel: OperationalExpensesViewModel
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_update_operational_expenses_form, container, false)

        mOperationalExpensesViewModel = ViewModelProvider(this)[OperationalExpensesViewModel::class.java]

        // Declaring the various Views
        //display views
        val tvEnterDate = view.findViewById<TextView>(R.id.tvEnterDate_UpdateOperationalExpensesForm)
        val tvExpenseAmount = view.findViewById<TextView>(R.id.tvExpenseAmount_UpdateOperationalExpensesForm)
        val tvSelectExpenses = view.findViewById<TextView>(R.id.tvSelectExpenses_UpdateOperationalExpensesForm)
        val tvSelectPaymentMethod = view.findViewById<TextView>(R.id.tvSelectPaymentMethod_UpdateOperationalExpensesForm)
        val save = view.findViewById<Button>(R.id.btnSave_UpdateOperationalExpensesForm)

        // value holder views
        tvDatePicker = view.findViewById(R.id.tvDatePicker_UpdateOperationalExpensesForm)
        val etExpenseAmount = view.findViewById<EditText>(R.id.etExpenseAmount_UpdateOperationalExpensesForm)
        val etTransactionId = view.findViewById<EditText>(R.id.etTransactionID_UpdateOperationalExpensesForm)
        val etShortNotes = view.findViewById<EditText>(R.id.etShortNotes_UpdateOperationalExpensesForm)
        val actvSelectExpenses = view.findViewById<AutoCompleteTextView>(R.id.actvSelectExpenses_UpdateOperationalExpensesForm)
        val actvPaymentMethod = view.findViewById<AutoCompleteTextView>(R.id.actvSelectPaymentMethod_UpdateOperationalExpensesForm)

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
        val theOperationalExpensesId = arguments?.getInt("operationalExpensesId")
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
            if (theOperationalExpensesId != null) {
                insertDataToDatabase(
                    theOperationalExpensesId.toInt(),
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
        operationalExpensesId: Int,
        tvDatePicker: TextView,
        tvEnterDate: TextView,
        tvSelectExpenses: TextView,
        actvSelectExpenses: AutoCompleteTextView,
        actvPaymentMethod: AutoCompleteTextView,
        tvExpenseAmount: TextView,
        etExpenseAmount: EditText,
        etTransactionId: EditText,
        etShortNotes: EditText,
    ){
        val date = tvDatePicker.text.toString()
        val expenseName = actvSelectExpenses.text.toString()
        val operationalExpensesAmount = etExpenseAmount.text.toString()
        val paymentMethod = actvPaymentMethod.text.toString()
        val transactionId = etTransactionId.text.toString()
        val shortNotes = etShortNotes.text.toString()


        if (checkIfEmpty(date)) {
            tvEnterDate.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Enter Date", Toast.LENGTH_LONG).show()
        } else if (checkIfEmpty(expenseName)) {
            tvEnterDate.setTextColor(resources.getColor(R.color.black))
            tvSelectExpenses.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Select Expense", Toast.LENGTH_LONG).show()
        } else if (checkIfEmpty(operationalExpensesAmount)) {
            tvSelectExpenses.setTextColor(resources.getColor(R.color.black))
            tvExpenseAmount.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Enter Operational Expenses Amount", Toast.LENGTH_LONG).show()
        }
        else {
            val myFormat =  "dd-MM-yyyy"
            val simpleDateFormat = SimpleDateFormat(myFormat, Locale.UK)
            val newDate = simpleDateFormat.parse(date)
            val operationalExpensesDate = newDate?.time?.let { java.sql.Date(it)}

            //
            if (operationalExpensesDate != null) {
                mOperationalExpensesViewModel.updateOperationalExpensesInfo(operationalExpensesId,operationalExpensesDate, expenseName, paymentMethod, operationalExpensesAmount.toDouble(), transactionId,shortNotes)
            }

            Toast.makeText(requireContext(), "Successfully Added", Toast.LENGTH_LONG).show()

            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flCashOutActivity,
                OperationalExpensesList()
            ).commit()
        }
    }
    private fun checkIfEmpty(value: String): Boolean {
        return (TextUtils.isEmpty(value))
    }
}