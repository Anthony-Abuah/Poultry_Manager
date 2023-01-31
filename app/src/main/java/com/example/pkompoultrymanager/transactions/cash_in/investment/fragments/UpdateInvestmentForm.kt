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


class UpdateInvestmentForm : Fragment() {
    private lateinit var mInvestmentViewModel: InvestmentViewModel
    private lateinit var tvDatePicker: TextView

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_update_investment_form, container, false)

        mInvestmentViewModel = ViewModelProvider(this)[InvestmentViewModel::class.java]


        // Declaring the various Views
        //display views
        val tvEnterDate = view.findViewById<TextView>(R.id.tvEnterDate_UpdateInvestmentForm)
        val tvAmountInvested = view.findViewById<TextView>(R.id.tvAmountInvested_UpdateInvestmentForm)
        val tvSelectInvestor = view.findViewById<TextView>(R.id.tvSelectInvestor_UpdateInvestmentForm)
        val tvTransactionId = view.findViewById<TextView>(R.id.tvTransactionId_UpdateInvestmentForm)
        val ivAddInvestor = view.findViewById<ImageView>(R.id.ivAddInvestor_UpdateInvestmentForm)
        val save = view.findViewById<Button>(R.id.btnSave_UpdateInvestmentForm)

        // value holder views
        tvDatePicker = view.findViewById(R.id.tvDatePicker_UpdateInvestmentForm)
        val etAmountInvested = view.findViewById<EditText>(R.id.etAmountInvested_UpdateInvestmentForm)
        val etTransactionId = view.findViewById<EditText>(R.id.etTransactionId_UpdateInvestmentForm)
        val actvSelectInvestor = view.findViewById<AutoCompleteTextView>(R.id.actvSelectInvestor_UpdateInvestmentForm)
        val etShortNotes = view.findViewById<EditText>(R.id.etShortNotes_UpdateInvestmentForm)

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

        // get the values from the communicator and store them in variables
        val theInvestmentId = arguments?.getInt("investmentId")
        val theDate = arguments?.getString("date")
        val theInvestor = arguments?.getString("investor")
        val theAmountInvested = arguments?.getString("amountInvested")
        val theTransactionId = arguments?.getString("transactionID")
        val theShortNotes = arguments?.getString("shortNotes")

        // bind the views to the communicator values
        tvDatePicker.text = theDate
        actvSelectInvestor.setText(theInvestor)
        etAmountInvested.setText(theAmountInvested)
        etTransactionId.setText(theTransactionId)
        etShortNotes.setText(theShortNotes)






        // save investment info into database
        save.setOnClickListener {
            if (theInvestmentId != null) {
                insertDataToDatabase(theInvestmentId.toInt(), tvDatePicker, tvEnterDate, tvAmountInvested, etAmountInvested, tvSelectInvestor, actvSelectInvestor, etTransactionId, etShortNotes)
            }
        }




        return view
    }

    private fun updateDate(myCalendar: Calendar) {
        val myFormat = "dd-MM-yyyy"
        val simpleDateFormat = SimpleDateFormat(myFormat, Locale.UK)
        tvDatePicker.text = simpleDateFormat.format(myCalendar.time)
    }

    private fun insertDataToDatabase(
        investmentId: Int,
        tvDatePicker: TextView,
        tvEnterDate: TextView,
        tvAmountInvested: TextView,
        etAmountInvested: EditText,
        tvSelectInvestor: TextView,
        actvSelectInvestor: AutoCompleteTextView,
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
            val myFormat =  "dd-MM-yyyy"
            val simpleDateFormat = SimpleDateFormat(myFormat, Locale.UK)
            val newDate = simpleDateFormat.parse(date)
            val investmentDate = newDate?.time?.let { java.sql.Date(it)}

            if (investmentDate != null) {
                mInvestmentViewModel.updateInvestmentInfo(investmentId,investmentDate,investor,investmentAmount.toDouble(),transactionId,shortNotes)
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