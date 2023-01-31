package com.example.pkompoultrymanager.transactions.cash_in.investment.fragments

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
import com.example.pkompoultrymanager.transactions.cash_in.investment.Investment
import com.example.pkompoultrymanager.transactions.cash_in.investment.InvestmentViewModel
import java.text.SimpleDateFormat
import java.util.*

class InvestmentForm : Fragment() {
    private lateinit var tvDatePicker: TextView
    private lateinit var mInvestmentViewModel: InvestmentViewModel

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_investment_form, container, false)

        mInvestmentViewModel = ViewModelProvider(this)[InvestmentViewModel::class.java]


        // Declaring the various Views
        //display views
        val tvEnterDate = view.findViewById<TextView>(R.id.tvEnterDate_InvestmentForm)
        val tvAmountInvested = view.findViewById<TextView>(R.id.tvAmountInvested_InvestmentForm)
        val tvSelectInvestor = view.findViewById<TextView>(R.id.tvSelectInvestor_InvestmentForm)
        val tvTransactionId = view.findViewById<TextView>(R.id.tvTransactionId_InvestmentForm)
        val ivAddInvestor = view.findViewById<ImageView>(R.id.ivAddInvestor_InvestmentForm)
        val save = view.findViewById<Button>(R.id.btnSave_InvestmentForm)

        // value holder views
        tvDatePicker = view.findViewById(R.id.tvDatePicker_InvestmentForm)
        val etAmountInvested = view.findViewById<EditText>(R.id.etAmountInvested_InvestmentForm)
        val etTransactionId = view.findViewById<EditText>(R.id.etInvestmentTransactionId_InvestmentForm)
        val actvSelectInvestor = view.findViewById<AutoCompleteTextView>(R.id.actvSelectInvestor_InvestmentForm)
        val etShortNotes = view.findViewById<EditText>(R.id.etShortNotes_InvestmentForm)

        // instantiate the calendar
        val myCalendar = Calendar.getInstance()
        // select and display date
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

        // Investor Suggestion
        val investorSuggestions = arrayOf("Dedee", "Kwame", "Sister Akos", "Madam Aisha")
        val selectInvestorAdapter = ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, investorSuggestions)
        actvSelectInvestor.setAdapter(selectInvestorAdapter)

        // more info on the amount invested question
        etAmountInvested.setOnTouchListener(View.OnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= etAmountInvested.getRight() - etAmountInvested.getCompoundDrawables()
                        .get(DRAWABLE_RIGHT).getBounds().width()
                ) {
                    val builder = AlertDialog.Builder(context)
                    val inflater = layoutInflater
                    val dialogLayout = inflater.inflate(R.layout.delete_prompt_layout, null)
                    with(builder) {
                        setTitle(R.string.AmountInvested_info)
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


        ivAddInvestor.setOnClickListener {
            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flCashInActivity,
                AddLenderInvestor()
            ).commit()
        }

        // save investment info into database
        save.setOnClickListener {
            insertDataToDatabase(tvDatePicker, tvEnterDate, tvAmountInvested, etAmountInvested, tvSelectInvestor, actvSelectInvestor, tvTransactionId, etTransactionId, etShortNotes)
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
        tvAmountInvested: TextView,
        etAmountInvested: EditText,
        tvSelectInvestor: TextView,
        actvSelectInvestor: AutoCompleteTextView,
        tvTransactionId: TextView,
        etTransactionId: EditText,
        etShortNotes: EditText
    ) {
        val date = tvDatePicker.text.toString()
        val investor = actvSelectInvestor.text.toString()
        val investmentAmount = etAmountInvested.text.toString()
        val transactionId = etTransactionId.text.toString()
        val shortNotes = etShortNotes.text.toString()


        if (checkIfEmpty(date)) {
            tvEnterDate.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Enter Date", Toast.LENGTH_LONG).show()
        } else if (checkIfEmpty(investor)) {
            tvEnterDate.setTextColor(resources.getColor(R.color.black))
            tvSelectInvestor.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Select Investor", Toast.LENGTH_LONG).show()
        } else if (checkIfEmpty(investmentAmount)) {
            tvSelectInvestor.setTextColor(resources.getColor(R.color.black))
            tvAmountInvested.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Enter Amount Invested", Toast.LENGTH_LONG)
                .show()
        } else {
            val myFormat = "dd-MM-yyyy"
            val simpleDateFormat = SimpleDateFormat(myFormat, Locale.UK)
            val investmentDate = simpleDateFormat.parse(date)
            // initialize the investment record
            val investment = investmentDate?.let {
                Investment(
                    0,
                    it, investor, investmentAmount.toDouble(), transactionId, shortNotes
                )
            }

            if (investment != null) {
                mInvestmentViewModel.addInvestment(investment)
            }


            Toast.makeText(requireContext(), "Successfully Added", Toast.LENGTH_LONG).show()

            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flCashInActivity,
                InvestmentList()
            ).commit()
        }
    }

    private fun checkIfEmpty(value: String): Boolean {
        return (TextUtils.isEmpty(value))
    }
}