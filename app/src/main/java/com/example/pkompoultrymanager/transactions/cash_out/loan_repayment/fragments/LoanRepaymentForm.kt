package com.example.pkompoultrymanager.transactions.cash_out.loan_repayment.fragments

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
import com.example.pkompoultrymanager.transactions.cash_out.loan_repayment.LoanRepayment
import com.example.pkompoultrymanager.transactions.cash_out.loan_repayment.LoanRepaymentViewModel
import java.text.SimpleDateFormat
import java.util.*

class LoanRepaymentForm : Fragment() {
    private lateinit var tvDatePicker: TextView
    private lateinit var mLoanRepaymentViewModel: LoanRepaymentViewModel
    @SuppressLint("ClickableViewAccessibility")
     override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_loan_repayment_form, container, false)

        mLoanRepaymentViewModel = ViewModelProvider(this)[LoanRepaymentViewModel::class.java]

        // Declaring the various Views
        //display views
        val tvEnterDate = view.findViewById<TextView>(R.id.tvEnterDate_LoanRepaymentForm)
        val tvAmountRepaid = view.findViewById<TextView>(R.id.tvAmountRepaid_LoanRepaymentForm)
        val tvSelectLender = view.findViewById<TextView>(R.id.tvLender_LoanRepaymentForm)
        val tvTransactionId = view.findViewById<TextView>(R.id.tvTransactionId_LoanRepaymentForm)
        val tvSelectPaymentMethod = view.findViewById<TextView>(R.id.tvPaymentMethod_LoanRepaymentForm)
        val save = view.findViewById<Button>(R.id.btnSave_LoanRepaymentForm)


        // value holder view
        tvDatePicker = view.findViewById(R.id.tvDatePicker_LoanRepaymentForm)
        val etAmountRepaid = view.findViewById<EditText>(R.id.etAmountRepaid_LoanRepaymentForm)
        val etTransactionId = view.findViewById<EditText>(R.id.etTransactionId_LoanRepaymentForm)
        val etShortNotes = view.findViewById<EditText>(R.id.etShortNotes_LoanRepaymentForm)
        val actvSelectLender = view.findViewById<AutoCompleteTextView>(R.id.actvLoanRecipient_LoanRepaymentForm)
        val actvSelectPaymentMethod = view.findViewById<AutoCompleteTextView>(R.id.actvPaymentMethod_LoanRepaymentForm)


        // instantiate the calendar
        val myCalendar = Calendar.getInstance()

        // select and enter date
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

        // Loan Recipient Suggestion
         val lenderSuggestion = arrayOf("Absa Bank", "Lower Pra", "Ahantaman Bank","Dedee")
         val selectLenderAdapter = ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, lenderSuggestion)
         actvSelectLender.setAdapter(selectLenderAdapter)

        // Payment Method Suggestion
         val paymentMethodSuggestion = arrayOf("Cash", "Cheque", "Mobile Money","Bank Transfer")
         val selectPaymentMethodAdapter = ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, paymentMethodSuggestion)
         actvSelectPaymentMethod.setAdapter(selectPaymentMethodAdapter)

        // more info on the amount repaid question
         etAmountRepaid.setOnTouchListener(View.OnTouchListener { v, event ->
             val DRAWABLE_RIGHT = 2
             if (event.action == MotionEvent.ACTION_UP) {
                 if (event.rawX >= etAmountRepaid.getRight() - etAmountRepaid.getCompoundDrawables()
                         .get(DRAWABLE_RIGHT).getBounds().width()
                 ) {
                     val builder = AlertDialog.Builder(context)
                     val inflater = layoutInflater
                     val dialogLayout = inflater.inflate(R.layout.delete_prompt_layout, null)
                     with(builder) {
                         setTitle(R.string.AmountRepaid_info)
                         setPositiveButton("Ok") { dialog, where -> }
                         setView(dialogLayout)
                         show()
                     }
                 }
             }
             false
         })

        // more info on the transaction id question
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

        // save loan repayment info into the database
        save.setOnClickListener {
            insertDataToDatabase(
                tvDatePicker,
                tvEnterDate,
                tvSelectLender,
                actvSelectLender,
                actvSelectPaymentMethod,
                tvAmountRepaid,
                etAmountRepaid,
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
        tvSelectLender: TextView,
        actvSelectLender: AutoCompleteTextView,
        actvSelectPaymentMethod: AutoCompleteTextView,
        tvAmountRepaid: TextView,
        etAmountRepaid: EditText,
        etTransactionId: EditText,
        etShortNotes: EditText
    ) {
        val date = tvDatePicker.text.toString()
        val lender = actvSelectLender.text.toString()
        val amountRepaid = etAmountRepaid.text.toString()
        val paymentMethod = actvSelectPaymentMethod.text.toString()
        val transactionId = etTransactionId.text.toString()
        val shortNotes = etShortNotes.text.toString()


        if (checkIfEmpty(date)) {
            tvEnterDate.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Enter Date", Toast.LENGTH_LONG).show()
        } else if (checkIfEmpty(lender)) {
            tvEnterDate.setTextColor(resources.getColor(R.color.black))
            tvSelectLender.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Select Lender", Toast.LENGTH_LONG).show()
        } else if (checkIfEmpty(amountRepaid)) {
            tvSelectLender.setTextColor(resources.getColor(R.color.black))
            tvAmountRepaid.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Enter Loan Repayment Amount", Toast.LENGTH_LONG).show()
        }
        else {
            val myFormat =  "dd-MM-yyyy"
            val simpleDateFormat = SimpleDateFormat(myFormat, Locale.UK)
            val loanRepaymentDate = simpleDateFormat.parse(date)


            val loanRepayment = loanRepaymentDate?.let {
                LoanRepayment(0,
                    it,lender,paymentMethod,amountRepaid.toDouble(),transactionId,shortNotes)
            }

            if (loanRepayment != null) {
                mLoanRepaymentViewModel.addLoanRepayment(loanRepayment)
            }

            Toast.makeText(requireContext(), "Successfully Added", Toast.LENGTH_LONG).show()

            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flCashOutActivity,
                LoanRepaymentList()
            ).commit()
        }
    }
    private fun checkIfEmpty(value: String): Boolean {
        return (TextUtils.isEmpty(value))
    }
}