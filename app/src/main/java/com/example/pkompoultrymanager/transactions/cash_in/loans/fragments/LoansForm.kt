package com.example.pkompoultrymanager.transactions.cash_in.loans.fragments

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
import com.example.pkompoultrymanager.tables.lender_investor.fragments.AddLenderInvestor
import com.example.pkompoultrymanager.transactions.cash_in.loans.Loans
import com.example.pkompoultrymanager.transactions.cash_in.loans.LoansViewModel
import java.text.SimpleDateFormat
import java.util.*

class LoansForm : Fragment() {
    private lateinit var tvDatePicker: TextView
    private lateinit var mLoansViewModel: LoansViewModel

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_loans_form, container, false)

        mLoansViewModel = ViewModelProvider(this)[LoansViewModel::class.java]

        // Declaring the various Views
        //display views
        val tvEnterDate = view.findViewById<TextView>(R.id.tvEnterDate_LoansForm)
        val tvLoanAmount = view.findViewById<TextView>(R.id.tvLoanAmount_LoansForm)
        val tvTransactionId = view.findViewById<TextView>(R.id.tvTransactionId_LoansForm)
        val tvSelectLender = view.findViewById<TextView>(R.id.tvSelectLender_LoansForm)
        val ivAddLender = view.findViewById<ImageView>(R.id.ivAddLender_LoansForm)
        val save = view.findViewById<Button>(R.id.btnSave_LoansForm)

        // value holder views
        tvDatePicker = view.findViewById(R.id.tvDatePicker_LoansForm)
        val etLoanAmount = view.findViewById<EditText>(R.id.etLoanAmount_LoansForm)
        val etTransactionId = view.findViewById<EditText>(R.id.etTransactionId_LoansForm)
        val etShortNotes = view.findViewById<EditText>(R.id.etShortNotes_LoansForm)
        val actvSelectLender = view.findViewById<AutoCompleteTextView>(R.id.actvSelectLender_LoansForm)

        // instantiate the calendar
        val myCalendar = Calendar.getInstance()

        // display and select date
        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDate(myCalendar)
        }
        tvDatePicker.setOnClickListener {
            DatePickerDialog(
                requireContext(), datePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(
                    Calendar.MONTH
                ),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // LenderSuggestion
        val lenderSuggestions = arrayOf("Absa Bank", "Lower Pra", "Ahantaman Bank", "Dedee")
        val selectLenderAdapter = ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, lenderSuggestions)
        actvSelectLender.setAdapter(selectLenderAdapter)

        // more info on the loan amount question
        etLoanAmount.setOnTouchListener(View.OnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= etLoanAmount.getRight() - etLoanAmount.getCompoundDrawables()
                        .get(DRAWABLE_RIGHT).getBounds().width()
                ) {
                    val builder = AlertDialog.Builder(context)
                    val inflater = layoutInflater
                    val dialogLayout = inflater.inflate(R.layout.delete_prompt_layout, null)
                    with(builder) {
                        setTitle(R.string.LoanAmountReceived_info)
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

        // add lender
        ivAddLender.setOnClickListener {
            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flCashInActivity,
                AddLenderInvestor()
            ).commit()
        }


        // save loan investment into database
        save.setOnClickListener {
            insertDataToDatabase(
                tvDatePicker,
                tvEnterDate,
                tvSelectLender,
                actvSelectLender,
                tvLoanAmount,
                etLoanAmount,
                etTransactionId,
                etShortNotes
            )
        }



        return view
    }

    private fun updateDate(myCalendar: Calendar) {
        val myFormat = "dd-MM-yyyy"
        val simpleDateFormat = SimpleDateFormat(myFormat, Locale.UK)
        tvDatePicker.text = simpleDateFormat.format(myCalendar.time)
    }
    private fun insertDataToDatabase(
        tvDatePicker: TextView,
        tvEnterDate: TextView,
        tvSelectLender: TextView,
        actvSelectLender: AutoCompleteTextView,
        tvLoanAmount: TextView,
        etLoanAmount: EditText,
        etTransactionId: EditText,
        etShortNotes: EditText,
    ) {
        val date = tvDatePicker.text.toString()
        val lender = actvSelectLender.text.toString()
        val loanAmount = etLoanAmount.text.toString()
        val transactionId = etTransactionId.text.toString()
        val shortNotes = etShortNotes.text.toString()

        if (checkIfEmpty(date)) {
            tvEnterDate.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Enter Date", Toast.LENGTH_LONG).show()
        } else if (checkIfEmpty(lender)) {
            tvEnterDate.setTextColor(resources.getColor(R.color.black))
            tvSelectLender.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Select Lender", Toast.LENGTH_LONG).show()
        } else if (checkIfEmpty(loanAmount)) {
            tvSelectLender.setTextColor(resources.getColor(R.color.black))
            tvLoanAmount.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Enter Loans Amount", Toast.LENGTH_LONG).show()
        }
        else {
            val myFormat =  "dd-MM-yyyy"
            val simpleDateFormat = SimpleDateFormat(myFormat, Locale.UK)
            val loanDate = simpleDateFormat.parse(date)
            // declare loan record
            val loan = loanDate?.let {
                Loans(0,
                    it,lender,loanAmount.toDouble(),transactionId,shortNotes)
            }

            if (loan != null) {
                mLoansViewModel.addLoans(loan)
            }

            Toast.makeText(requireContext(), "Successfully Added", Toast.LENGTH_LONG).show()

            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flCashInActivity,
                LoansList()
            ).commit()
        }
    }
    private fun checkIfEmpty(value: String): Boolean {
        return (TextUtils.isEmpty(value))
    }
}