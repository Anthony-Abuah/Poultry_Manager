package com.example.pkompoultrymanager.inventory.eggs.reduction.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isGone
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.pkompoultrymanager.R
import com.example.pkompoultrymanager.inventory.eggs.addition.EggInventoryAdditionViewModel
import com.example.pkompoultrymanager.inventory.eggs.reduction.EggInventoryReduction
import com.example.pkompoultrymanager.inventory.eggs.reduction.EggInventoryReductionViewModel
import com.example.pkompoultrymanager.tables.customer.CustomerViewModel
import com.example.pkompoultrymanager.tables.egg_type.EggTypeViewModel
import java.text.SimpleDateFormat
import java.util.*

class EggsReductionForm : Fragment() {
    private lateinit var mEggInventoryReductionViewModel: EggInventoryReductionViewModel
    private lateinit var mEggInventoryAdditionViewModel: EggInventoryAdditionViewModel
    private lateinit var mCustomerViewModel: CustomerViewModel
    private lateinit var mEggTypeViewModel: EggTypeViewModel
    private lateinit var tvDatePicker: TextView
    private lateinit var myFarmInfo: SharedPreferences


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_eggs_reduction_form, container, false)
        mEggInventoryReductionViewModel = ViewModelProvider(this)[EggInventoryReductionViewModel::class.java]
        mEggInventoryAdditionViewModel = ViewModelProvider(this)[EggInventoryAdditionViewModel::class.java]
        mCustomerViewModel = ViewModelProvider(this)[CustomerViewModel::class.java]
        mEggTypeViewModel = ViewModelProvider(this)[EggTypeViewModel::class.java]

        // Initializing shared preferences
        myFarmInfo = context?.getSharedPreferences("MyFarmInfo", Context.MODE_PRIVATE)!!
        val numberOfEggs = myFarmInfo.getString("numberOfEggsPerCrate","30")
        val numberOfEggsPerCrate = numberOfEggs!!.toInt()

        // Instantiating the calendar and getting current system date
        val myCalendar = Calendar.getInstance()
        val day =  myCalendar.get(Calendar.DAY_OF_MONTH)
        val month = myCalendar.get(Calendar.MONTH)
        val year = myCalendar.get(Calendar.YEAR)
        val dateToday = "$day-${month+1}-$year"

        // Declaring the various Views
        //display views
        val save = view.findViewById<Button>(R.id.btnSave_EggsInventoryReductionForm)
        val back = view.findViewById<ImageButton>(R.id.ibBack_EggInventoryReductionForm)
        val tvEggType = view.findViewById<TextView>(R.id.tvEggsType_EggsInventoryReductionForm)
        val tvReductionReason = view.findViewById<TextView>(R.id.tvReductionReason_EggsInventoryReductionForm)
        val tvEnterDate = view.findViewById<TextView>(R.id.tvEnterDate_EggsInventoryReductionForm)
        val tvNumberOfCrates = view.findViewById<TextView>(R.id.tvNumberOfEggCrates_EggsInventoryReductionForm)
        val tvNumberEggsNotInCrates = view.findViewById<TextView>(R.id.tvNumberOfEggsNotInCrates_EggsInventoryReductionForm)
        //value holder views
        tvDatePicker = view.findViewById(R.id.tvDatePicker_EggsInventoryReductionForm)
        val etNumberOfCrates = view.findViewById<EditText>(R.id.etNumberOfEggCrates_EggsInventoryReductionForm)
        val etShortNotes = view.findViewById<EditText>(R.id.etShortNotes_EggsInventoryReductionForm)
        val etNumberOfEggsNotInCrates = view.findViewById<EditText>(R.id.etNumberOfEggsNotInCrates_EggsInventoryReductionForm)
        val actvSelectReductionReason = view.findViewById<AutoCompleteTextView>(R.id.actvEggReductionReason_EggsInventoryReductionForm)
        val actvSelectEggType = view.findViewById<AutoCompleteTextView>(R.id.actvEggsType_EggsInventoryReductionForm)


        //declaring Add Customer Views
        val llAddCustomer = view.findViewById<LinearLayout>(R.id.llAddCustomer_EggsInventoryReductionForm)
        val llSelectCustomer = view.findViewById<LinearLayout>(R.id.llSelectCustomer_EggsInventoryReductionForm)
        val etEggCost = view.findViewById<EditText>(R.id.etCost_EggsInventoryReductionForm)
        val actvSelectCustomer = view.findViewById<AutoCompleteTextView>(R.id.actvSelectCustomer_EggsInventoryReductionForm)
        val tvAmountTheCustomerPaid = view.findViewById<EditText>(R.id.etAmountPaid_EggsInventoryReductionForm)
        val tvEggCost = view.findViewById<TextView>(R.id.tvCost_EggsInventoryReductionForm)
        val tvCustomer = view.findViewById<TextView>(R.id.tvCustomer_EggsInventoryReductionForm)


        // hide the customer layout
        llAddCustomer.isGone = true
        // add text changed listener to add customer layout
        actvSelectReductionReason.addTextChangedListener(reductionReasonTextWatcher(llAddCustomer))
        actvSelectEggType.addTextChangedListener(selectEggTypeTextWatcher())


        // EggType Suggestions
        mEggTypeViewModel.displayAllEggTypes.observe(viewLifecycleOwner) {
        eggTypes -> eggTypes.let {
        val eggTypeNames = mutableListOf<String>()
        for (EggType in it) { eggTypeNames.add(EggType.EggTypeName) }
        val eggTypeAdapter = ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, eggTypeNames)
        actvSelectEggType.setAdapter(eggTypeAdapter) } }

        var theTotalNumberEggsReduced = 0
        mEggInventoryReductionViewModel.eggsReducedByEggType.observe(viewLifecycleOwner){
                reduction->reduction.let { reductionSum-> theTotalNumberEggsReduced = reductionSum ?: 0
        } }
        var theNumberOfEggsAdded = 0
        mEggInventoryAdditionViewModel.eggsAddedByEggType.observe(viewLifecycleOwner) {
                quantity->quantity.let{ quantityAdded-> theNumberOfEggsAdded = quantityAdded ?:0
        } }


            val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDate(myCalendar)
        }

        // Show date picker and display date
        tvDatePicker.setOnClickListener {
            DatePickerDialog(
                requireContext(), datePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(
                    Calendar.MONTH
                ),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }


        // Code to go back to previous Window
        back.setOnClickListener {
            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.popBackStackImmediate()
        }

        // Suggestions For Egg ReductionReason
        val eggReductionReasonSuggestions = arrayOf("Sold", "Damaged", "Spoilt", "Donated")
        val eggReductionReasonAdapter = ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, eggReductionReasonSuggestions)
        actvSelectReductionReason.setAdapter(eggReductionReasonAdapter)

        // Suggestions For Egg Type
        val eggTypeSuggestions = arrayOf("Intact", "Cracked")
        val eggTypeAdapter = ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, eggTypeSuggestions)
        actvSelectEggType.setAdapter(eggTypeAdapter)

        // Dynamically Add the customer Layout based on reduction reason
        actvSelectReductionReason.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
                val selectedItem = parent?.getItemAtPosition(position).toString()
                if (selectedItem == "Sold" || selectedItem == "sold") {
                    llAddCustomer.isGone = false

                    // give more info about the amount paid by customer
                    tvAmountTheCustomerPaid.setOnTouchListener(View.OnTouchListener { v, event ->
                        val DRAWABLE_RIGHT = 2

                        if (event.action == MotionEvent.ACTION_UP) {
                            if (event.rawX >= tvAmountTheCustomerPaid.right - tvAmountTheCustomerPaid.compoundDrawables[DRAWABLE_RIGHT].bounds.width()
                            ) {
                                val builder = AlertDialog.Builder(context)
                                val inflater = layoutInflater
                                val dialogLayout =
                                    inflater.inflate(R.layout.delete_prompt_layout, null)
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

                    //give more info about the total cost of eggs
                    etEggCost.setOnTouchListener(View.OnTouchListener { v, event ->
                        val DRAWABLE_RIGHT = 2
                        if (event.action == MotionEvent.ACTION_UP) {
                            if (event.rawX >= etEggCost.right - etEggCost.compoundDrawables[DRAWABLE_RIGHT].bounds.width()
                            ) {
                                val builder = AlertDialog.Builder(context)
                                val inflater = layoutInflater
                                val dialogLayout =
                                    inflater.inflate(R.layout.delete_prompt_layout, null)
                                with(builder) {
                                    setTitle(R.string.EggCost_info)
                                    setPositiveButton("Ok") { dialog, where -> }
                                    setView(dialogLayout)
                                    show()
                                }
                            }
                        }
                        false
                    })

                    // Customer Suggestions
                    mCustomerViewModel.displayAllCustomers.observe(viewLifecycleOwner) {
                            customers -> customers.let {
                        val customerNames = mutableListOf<String>()
                        for (Customer in it) { customerNames.add(Customer.CustomerName) }
                        val customerAdapter = ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, customerNames)
                        actvSelectCustomer.setAdapter(customerAdapter) }
                    }

                } else {
                    // remove customer layout based on reduction reason
                    llAddCustomer.isGone = true
                }
            }

        // more info on egg type
        tvEggType.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.delete_prompt_layout, null)
            with(builder) {
                setTitle(R.string.EggType_info)
                setPositiveButton("Ok") { dialog, where -> }
                setView(dialogLayout)
                show()
            }
        }

        // more info on reduction reason
        tvReductionReason.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.delete_prompt_layout, null)
            with(builder) {
                setTitle(R.string.EggsReductionReason_info)
                setPositiveButton("Ok") { dialog, where -> }
                setView(dialogLayout)
                show()
            }
        }

        // more info on number of crates reduced
        etNumberOfCrates.setOnTouchListener(View.OnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= etNumberOfCrates.right - etNumberOfCrates.compoundDrawables[DRAWABLE_RIGHT].bounds.width()
                ) {
                    val builder = AlertDialog.Builder(context)
                    val inflater = layoutInflater
                    val dialogLayout = inflater.inflate(R.layout.delete_prompt_layout, null)
                    with(builder) {
                        setTitle(R.string.NumberOfCrates_info)
                        setPositiveButton("Ok") { dialog, where -> }
                        setView(dialogLayout)
                        show()
                    }
                }
            }
            false
        })

        // more info on number of eggs not in crates
        etNumberOfEggsNotInCrates.setOnTouchListener(View.OnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= etNumberOfEggsNotInCrates.right - etNumberOfEggsNotInCrates.compoundDrawables[DRAWABLE_RIGHT].bounds.width()
                ) {
                    val builder = AlertDialog.Builder(context)
                    val inflater = layoutInflater
                    val dialogLayout = inflater.inflate(R.layout.delete_prompt_layout, null)
                    with(builder) {
                        setTitle(R.string.NumberOfEggsNotInCrates_info)
                        setPositiveButton("Ok") { dialog, where -> }
                        setView(dialogLayout)
                        show()
                    }
                }
            }
            false
        })

        // add to the egg reduction inventory
        save.setOnClickListener {
            insertDataToDatabase(
                tvDatePicker,
                tvEnterDate,
                tvEggType,
                actvSelectEggType,
                tvReductionReason,
                actvSelectReductionReason,
                tvCustomer,
                actvSelectCustomer,
                tvEggCost,
                etEggCost,
                tvAmountTheCustomerPaid,
                tvNumberOfCrates,
                etNumberOfCrates,
                tvNumberEggsNotInCrates,
                etNumberOfEggsNotInCrates,
                etShortNotes,
                numberOfEggsPerCrate,
                dateToday,
                theNumberOfEggsAdded,
                theTotalNumberEggsReduced
            )
        }


        return view
    }

    private fun updateDate(myCalendar: Calendar) {
        val myFormat = "EEE, dd MMM, yyyy"
        val simpleDateFormat = SimpleDateFormat(myFormat, Locale.UK)
        tvDatePicker.text = simpleDateFormat.format(myCalendar.time)
    }

    private fun insertDataToDatabase(
        tvDatePicker:TextView,
        tvEnterDate: TextView,
        tvEggType: TextView,
        actvEggType: AutoCompleteTextView,
        tvReductionReason: TextView,
        actvReductionReason: AutoCompleteTextView,
        tvSelectCustomer: TextView,
        actvSelectCustomer: AutoCompleteTextView,
        tvEggCost: TextView,
        etEggCost: EditText,
        etAmountTheCustomerPaid: EditText,
        tvNumberOfCrates: TextView,
        etNumberOfCrates: EditText,
        tvNumberOfEggsNotInCrate: TextView,
        etNumberOfEggsNotInCrate: EditText,
        etShortNotes: EditText,
        numberOfEggsPerCrate: Int,
        dateToday: String,
        theTotalNumberOfEggsAdded: Int,
        theTotalNumberOfEggsReduced: Int
    ) {
        val date = tvDatePicker.text.toString()
        val eggType = actvEggType.text.toString()
        val reductionReason = actvReductionReason.text.toString()
        val customer = if(reductionReason == "Sold" || reductionReason == "sold"){actvSelectCustomer.text.toString()} else ""
        val eggCost = if(reductionReason == "Sold" || reductionReason == "sold"){etEggCost.text.toString()} else "0.0"
        val amountPaidByCustomer = if(reductionReason == "Sold" || reductionReason == "sold"){etAmountTheCustomerPaid.text.toString()} else "0.0"
        val eggCrates = if(checkIfEmpty(etNumberOfCrates.text.toString())){0} else etNumberOfCrates.text.toString().toInt()
        val eggsNotInCrates = if(checkIfEmpty(etNumberOfEggsNotInCrate.text.toString())){0} else etNumberOfEggsNotInCrate.text.toString().toInt()
        val shortNotes = etShortNotes.text.toString()

        // set date format and get the current system date
        val myFormat = "EEE, dd MMM, yyyy"
        val simpleDateFormat = SimpleDateFormat(myFormat, Locale.UK)

        val systemFormat = "dd-MM-yyyy"
        val systemDateFormat = SimpleDateFormat(systemFormat, Locale.UK)
        val theCurrentDate = systemDateFormat.parse(dateToday)

        // set constraints
        if (checkIfEmpty(date)) {
            tvEnterDate.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Enter Date", Toast.LENGTH_LONG).show()
        }else if (theCurrentDate!! < simpleDateFormat.parse(date)!!){
            tvEnterDate.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "It is not $date yet", Toast.LENGTH_LONG).show()
            Toast.makeText(requireContext(), "You can only reduce $eggType on $dateToday or before", Toast.LENGTH_SHORT).show()
        } else if (checkIfEmpty(eggType)) {
            tvEnterDate.setTextColor(resources.getColor(R.color.black))
            tvEggType.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Select Egg Type", Toast.LENGTH_LONG).show()
        } else if (checkIfEmpty(reductionReason)) {
            tvEggType.setTextColor(resources.getColor(R.color.black))
            tvReductionReason.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please select Reduction Reason", Toast.LENGTH_LONG).show()
        } else if (checkIfEggsAreNotBeingReduced(etNumberOfCrates.text.toString(), etNumberOfEggsNotInCrate.text.toString())) {
            tvReductionReason.setTextColor(resources.getColor(R.color.black))
            tvNumberOfCrates.setTextColor(resources.getColor(R.color.red))
            tvNumberOfEggsNotInCrate.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Enter Number of Eggs Reduced", Toast.LENGTH_LONG).show()
        } else if (reductionReason == "Sold" || reductionReason == "sold") {
            if (checkIfEmpty(customer)) {
                tvNumberOfCrates.setTextColor(resources.getColor(R.color.black))
                tvNumberOfEggsNotInCrate.setTextColor(resources.getColor(R.color.black))
                tvSelectCustomer.setTextColor(resources.getColor(R.color.red))
            } else if (eggCost.toDouble() <= 0.0) {
                tvSelectCustomer.setTextColor(resources.getColor(R.color.black))
                tvEggCost.setTextColor(resources.getColor(R.color.red))
                Toast.makeText(requireContext(), "The cost of eggs cannot be $eggCost", Toast.LENGTH_LONG).show()
            }
            else {
                val eggQuantity = eggsNotInCrates + (eggCrates * numberOfEggsPerCrate)
                val eggQuantityRemaining = theTotalNumberOfEggsAdded - theTotalNumberOfEggsReduced

                if (eggQuantityRemaining < eggQuantity){
                    tvNumberOfEggsNotInCrate.setTextColor(resources.getColor(R.color.red))
                    tvNumberOfCrates.setTextColor(resources.getColor(R.color.red))
                    Toast.makeText(requireContext(), "You can not reduce more than ${eggQuantityRemaining/numberOfEggsPerCrate} crates and ${eggQuantityRemaining%numberOfEggsPerCrate} eggs of $eggType", Toast.LENGTH_LONG).show()
                }
                else {
                val eggInventoryReductionDate = simpleDateFormat.parse(date)
                val amountOwed = (eggCost.toDouble() - amountPaidByCustomer.toDouble())
                val eggInventoryReduction = eggInventoryReductionDate?.let {
                    EggInventoryReduction(0,
                        it,
                        reductionReason,
                        eggType,
                        eggQuantity,
                        eggCost.toDouble(),
                        amountPaidByCustomer.toDouble(),
                        amountOwed,
                        customer,
                        shortNotes
                    )
                }
                if (eggInventoryReduction != null) {
                    mEggInventoryReductionViewModel.addEggInventoryReduction(eggInventoryReduction)
                }

                Toast.makeText(requireContext(), "Successfully Added", Toast.LENGTH_LONG).show()

                val fragmentManager: FragmentManager = parentFragmentManager
                fragmentManager.beginTransaction().replace(
                    R.id.flEggsActivity,
                    EggsReductionList()
                ).commit()}
            }
        } else {
            val eggQuantity = eggsNotInCrates + (eggCrates * numberOfEggsPerCrate)
            val eggQuantityRemaining = theTotalNumberOfEggsAdded - theTotalNumberOfEggsReduced

            if (eggQuantityRemaining < eggQuantity){
                tvNumberOfEggsNotInCrate.setTextColor(resources.getColor(R.color.red))
                tvNumberOfCrates.setTextColor(resources.getColor(R.color.red))
                Toast.makeText(requireContext(), "You can not reduce more than ${eggQuantityRemaining/numberOfEggsPerCrate} crates and ${eggQuantityRemaining%numberOfEggsPerCrate} eggs of $eggType", Toast.LENGTH_LONG).show()
            }
            else {
            val eggInventoryReductionDate = simpleDateFormat.parse(date)
            val amountOwed = (eggCost.toDouble() - amountPaidByCustomer.toDouble())
            val eggInventoryReduction = eggInventoryReductionDate?.let {
                EggInventoryReduction(0,
                    it,
                    reductionReason,
                    eggType,
                    eggQuantity,
                    eggCost.toDouble(),
                    amountPaidByCustomer.toDouble(),
                    amountOwed,
                    customer,
                    shortNotes
                )
            }

            if (eggInventoryReduction != null) {
                mEggInventoryReductionViewModel.addEggInventoryReduction(eggInventoryReduction)
            }

            Toast.makeText(requireContext(), "Successfully Added", Toast.LENGTH_LONG).show()

            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flEggsActivity,
                EggsReductionList()
            ).commit()}
        }
    }

    private fun checkIfEmpty(value: String): Boolean {
        return (TextUtils.isEmpty(value))
    }
    private fun checkIfEggsAreNotBeingReduced(value: String, value1: String): Boolean {
        return (TextUtils.isEmpty(value) && TextUtils.isEmpty(value1))
    }

    private fun reductionReasonTextWatcher(llAddCustomer: LinearLayout) = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            llAddCustomer.isGone = !(s.toString().contains("sold", true))
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            llAddCustomer.isGone = !(s.toString().contains("sold", true))
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            llAddCustomer.isGone = !(s.toString().contains("sold", true))
        }
    }
    private fun selectEggTypeTextWatcher() = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            mEggInventoryAdditionViewModel.getEggsAddedByEggType(s.toString())
            mEggInventoryReductionViewModel.getEggsReducedByEggType(s.toString())
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }

}


