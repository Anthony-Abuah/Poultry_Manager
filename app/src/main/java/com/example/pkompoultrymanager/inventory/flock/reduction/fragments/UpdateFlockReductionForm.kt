package com.example.pkompoultrymanager.inventory.flock.reduction.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
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
import com.example.pkompoultrymanager.inventory.flock.addition.FlockInventoryAdditionViewModel
import com.example.pkompoultrymanager.inventory.flock.reduction.FlockInventoryReductionViewModel
import com.example.pkompoultrymanager.tables.customer.CustomerViewModel
import com.example.pkompoultrymanager.tables.customer.fragments.AddCustomer
import java.text.SimpleDateFormat
import java.util.*

class UpdateFlockReductionForm : Fragment() {
    private lateinit var mFlockInventoryReductionViewModel: FlockInventoryReductionViewModel
    private lateinit var mFlockInventoryAdditionViewModel: FlockInventoryAdditionViewModel
    private lateinit var mCustomerViewModel: CustomerViewModel
    private lateinit var tvDatePicker: TextView


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_update_flock_reduction_form, container, false)

        // initialize the viewModels
        mFlockInventoryReductionViewModel = ViewModelProvider(this)[FlockInventoryReductionViewModel::class.java]
        mFlockInventoryAdditionViewModel = ViewModelProvider(this)[FlockInventoryAdditionViewModel::class.java]
        mCustomerViewModel = ViewModelProvider(this)[CustomerViewModel::class.java]


        // Instantiating the calendar and getting current system date
        val myCalendar = Calendar.getInstance()
        val day =  myCalendar.get(Calendar.DAY_OF_MONTH)
        val month = myCalendar.get(Calendar.MONTH)
        val year = myCalendar.get(Calendar.YEAR)
        val dateToday = "$day-${month+1}-$year"

        // Declaring the Views
        // display views
        val tvSelectFlock = view.findViewById<TextView>(R.id.tvSelectFlock_UpdateFlockInventoryReductionForm)
        val tvEnterDate = view.findViewById<TextView>(R.id.tvEnterDate_UpdateFlockInventoryReductionForm)
        val tvFlockReductionQuantity = view.findViewById<TextView>(R.id.tvFlockReductionQuantity_UpdateFlockInventoryReductionForm)
        val tvSelectReductionReason = view.findViewById<TextView>(R.id.tvReductionReason_UpdateFlockInventoryReductionForm)
        val update = view.findViewById<Button>(R.id.btnUpdate_UpdateFlockInventoryReductionForm)
        val back = view.findViewById<ImageButton>(R.id.ibBack_UpdateFlockInventoryReductionForm)

        // value holder views
        tvDatePicker = view.findViewById(R.id.tvDatePicker_UpdateFlockInventoryReductionForm)
        val etFlockReductionQuantity = view.findViewById<EditText>(R.id.etFlockReductionQuantity_UpdateFlockInventoryReductionForm)
        val etShortNotes = view.findViewById<EditText>(R.id.etShortNotes_UpdateFlockInventoryReductionForm)
        val actvSelectFlock = view.findViewById<AutoCompleteTextView>(R.id.actvSelectFlock_UpdateFlockInventoryReductionForm)
        val actvSelectReductionReason = view.findViewById<AutoCompleteTextView>(R.id.actvReductionReason_UpdateFlockInventoryReductionForm)


        // Declaring and initializing the Add Customer Layout Views
        val llAddCustomer = view.findViewById<LinearLayout>(R.id.llAddCustomer_UpdateFlockInventoryReductionForm)
        val llSelectCustomer = view.findViewById<LinearLayout>(R.id.llSelectCustomer_UpdateFlockInventoryReductionForm)
        val etFlockCost = view.findViewById<EditText>(R.id.etCost_UpdateFlockInventoryReductionForm)
        val actvSelectCustomer = view.findViewById<AutoCompleteTextView>(R.id.actvCustomer_UpdateFlockInventoryReductionForm)
        val etAmountTheCustomerPaid = view.findViewById<EditText>(R.id.etAmountPaid_UpdateFlockInventoryReductionForm)
        val tvFlockCost = view.findViewById<TextView>(R.id.tvCost_UpdateFlockInventoryReductionForm)
        val tvCustomer = view.findViewById<TextView>(R.id.tvCustomer_UpdateFlockInventoryReductionForm)
        val tvAmountReceived = view.findViewById<TextView>(R.id.tvAmountPaid_UpdateFlockInventoryReductionForm)
        val ivAddCustomer = view.findViewById<ImageView>(R.id.ivAddCustomer_UpdateFlockInventoryReductionForm)

        val flockAdditionQuantity = mutableMapOf<String,Int>()
        val flockAdditionDate = mutableMapOf<String,String>()
        val flockReductionQuantity = mutableMapOf<String,Int>()

        llAddCustomer.isGone = true

        // flock names suggestion
        mFlockInventoryAdditionViewModel.allFlockNames.observe(viewLifecycleOwner) {
                flocks -> flocks.let {
            val flockNames = mutableListOf<String>()
            for (allFlocks in it) { flockNames.add(allFlocks) }
            val flockNameAdapter = ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, flockNames)
            actvSelectFlock.setAdapter(flockNameAdapter) }
        }
        // textWatcher to hide/show customer purchase info
        actvSelectReductionReason.addTextChangedListener(reductionReasonTextWatcher(llAddCustomer))
        // textWatcher to get the sum values for the flock selected
        actvSelectFlock.addTextChangedListener(selectFlockNameTextWatcher())

        var theTotalNumberOfFlockQuantityReduced = 0
        mFlockInventoryReductionViewModel.reductionSum.observe(viewLifecycleOwner){
                reduction->reduction.let { reductionSum-> theTotalNumberOfFlockQuantityReduced = reductionSum ?: 0
        } }
        var theNumberOfBirdsAdded = 0
        mFlockInventoryAdditionViewModel.flockAddedByFlockName.observe(viewLifecycleOwner) {
                quantity->quantity.let{ quantityAdded-> theNumberOfBirdsAdded = quantityAdded ?:0
        }
        }

        // Customer Suggestions
        mCustomerViewModel.displayAllCustomers.observe(viewLifecycleOwner) {
                customers -> customers.let {
            val customerNames = mutableListOf<String>()
            for (Customer in it) { customerNames.add(Customer.CustomerName) }
            val customerAdapter = ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, customerNames)
            actvSelectCustomer.setAdapter(customerAdapter) }
        }

        // Reduction Reason Suggestions
        val reductionReasonSuggestions = arrayOf("Death", "Sold")
        val selectReductionReasonAdapter = ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, reductionReasonSuggestions)
        actvSelectReductionReason.setAdapter(selectReductionReasonAdapter)

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

        // more info on the Select Flock Question
        tvSelectFlock.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.delete_prompt_layout, null)
            with(builder) {
                setTitle(R.string.SelectFlock_info)
                setPositiveButton("Ok") { dialog, where -> }
                setView(dialogLayout)
                show()
            }
        }

        // more info on the Select Reduction Reason Question
        tvSelectReductionReason.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.delete_prompt_layout, null)
            with(builder) {
                setTitle(R.string.FlockReductionReason_info)
                setPositiveButton("Ok") { dialog, where -> }
                setView(dialogLayout)
                show()
            }
        }
        // more info on the Flock Reduction Quantity Question
        etFlockReductionQuantity.setOnTouchListener(View.OnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= etFlockReductionQuantity.right - etFlockReductionQuantity.compoundDrawables[DRAWABLE_RIGHT].bounds.width()
                ) {
                    val builder = AlertDialog.Builder(context)
                    val inflater = layoutInflater
                    val dialogLayout = inflater.inflate(R.layout.delete_prompt_layout, null)
                    with(builder) {
                        setTitle(R.string.FlockReductionQuantity_info)
                        setPositiveButton("Ok") { dialog, where -> }
                        setView(dialogLayout)
                        show()
                    }
                }
            }
            false
        })

        // more info on the Amount Paid by Customer Question
        etAmountTheCustomerPaid.setOnTouchListener(View.OnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= etAmountTheCustomerPaid.right - etAmountTheCustomerPaid.compoundDrawables[DRAWABLE_RIGHT].bounds.width()
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
        // more info on the Flock Cost Question
        etFlockCost.setOnTouchListener(View.OnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= etFlockCost.right - etFlockCost.compoundDrawables[DRAWABLE_RIGHT].bounds.width()
                ) {
                    val builder = AlertDialog.Builder(context)
                    val inflater = layoutInflater
                    val dialogLayout =
                        inflater.inflate(R.layout.delete_prompt_layout, null)
                    with(builder) {
                        setTitle(R.string.FlockCost_info)
                        setPositiveButton("Ok") { dialog, where -> }
                        setView(dialogLayout)
                        show()
                    }
                }
            }
            false
        })

        // code to add Customer
        ivAddCustomer.setOnClickListener {
            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flFlockActivity,
                AddCustomer()
            ).addToBackStack(null).commit()
        }


        // setting on click listener to the reduction reason to add the customer layout when item selected matches a certain criteria
        actvSelectReductionReason.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val selectedItem = parent?.getItemAtPosition(position).toString()
                llAddCustomer.isGone = !(selectedItem.contains("sold", true))
            }


        // get the values from the communicator
        val theFlockInventoryReductionId = arguments?.getString("flockInventoryReductionId")!!
        val theDateReduced = arguments?.getString("dateReduced")!!
        val theFlockName = arguments?.getString("flockName")!!
        val theCustomer = arguments?.getString("customer")!!
        val theReductionReason = arguments?.getString("reductionReason")!!
        val theReductionQuantity = arguments?.getString("reductionQuantity")!!
        val theFlockCost = arguments?.getString("flockCost")!!
        val theAmountTheCustomerPaid = arguments?.getString("amountReceived")!!
        val theShortNotes = arguments?.getString("shortNotes")!!

        // bind the values to the views
        tvDatePicker.text = theDateReduced
        actvSelectFlock.setText(theFlockName)
        actvSelectReductionReason.setText(theReductionReason)
        etFlockReductionQuantity.setText(theReductionQuantity)
        etShortNotes.setText(theShortNotes)
        if(actvSelectReductionReason.text.toString() =="sold" || actvSelectReductionReason.text.toString() == "Sold"){
            actvSelectCustomer.setText(theCustomer)
            etFlockCost.setText(theFlockCost)
            etAmountTheCustomerPaid.setText(theAmountTheCustomerPaid)
        }

        back.setOnClickListener {
            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.popBackStackImmediate()

        }

        update.setOnClickListener {
            updateFlockInventoryReduction(
                theFlockInventoryReductionId.toInt(),
                tvDatePicker,
                tvEnterDate,
                tvSelectFlock,
                actvSelectFlock,
                tvFlockReductionQuantity,
                etFlockReductionQuantity,
                tvSelectReductionReason,
                actvSelectReductionReason,
                etShortNotes,
                actvSelectCustomer,
                etFlockCost,
                etAmountTheCustomerPaid,
                tvCustomer,
                tvFlockCost,
                tvAmountReceived,
                dateToday,
                theTotalNumberOfFlockQuantityReduced,
                theNumberOfBirdsAdded
            )
        }




        return view
    }
    private fun updateDate(myCalendar: Calendar) {
        val myFormat = "EEE, dd MMM, yyyy"
        val simpleDateFormat = SimpleDateFormat(myFormat, Locale.UK)
        tvDatePicker.text = simpleDateFormat.format(myCalendar.time)
    }

    private fun updateFlockInventoryReduction(
        flockInventoryReductionId: Int,
        tvDatePicker: TextView,
        tvEnterDate: TextView,
        tvSelectFlock: TextView,
        actvSelectFlock: AutoCompleteTextView,
        tvFlockReductionQuantity: TextView,
        etFlockReductionQuantity: EditText,
        tvSelectReductionReason: TextView,
        actvSelectReductionReason: AutoCompleteTextView,
        etShortNotes: EditText,
        actvSelectCustomer: AutoCompleteTextView,
        etFlockCost: EditText,
        etAmountTheCustomerPaid: EditText,
        tvCustomer: TextView,
        tvFlockCost: TextView,
        tvAmountTheCustomerPaid: TextView,
        dateToday: String,
        theTotalNumberOfFlockQuantityReduced: Int,
        theNumberOfBirdsAdded: Int
    ) {
        val date = tvDatePicker.text.toString()
        val flock = actvSelectFlock.text.toString()
        val reductionReason = actvSelectReductionReason.text.toString()
        val customer = if(reductionReason == "Sold" || reductionReason == "sold"){actvSelectCustomer.text.toString()} else ""
        val flockCost = if(reductionReason == "Sold" || reductionReason == "sold"){etFlockCost.text.toString()} else "0.0"
        val amountTheCustomerPaid = if(reductionReason == "Sold" || reductionReason == "sold"){etAmountTheCustomerPaid.text.toString()} else "0.0"
        val flockReductionQuantity = etFlockReductionQuantity.text.toString()
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
            Toast.makeText(requireContext(), "You can only reduce $flock on $dateToday or before", Toast.LENGTH_SHORT).show()
        }else if (checkIfEmpty(flockReductionQuantity)) {
            tvEnterDate.setTextColor(resources.getColor(R.color.black))
            tvFlockReductionQuantity.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Enter Number Of Birds reduced", Toast.LENGTH_LONG).show()
        }else if (checkIfEmpty(reductionReason)) {
            tvFlockReductionQuantity.setTextColor(resources.getColor(R.color.black))
            tvSelectReductionReason.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Select Reduction Reason", Toast.LENGTH_LONG).show()
        }else if (checkIfEmpty(flock)) {
            tvSelectReductionReason.setTextColor(resources.getColor(R.color.black))
            tvSelectFlock.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Select flock", Toast.LENGTH_LONG).show()
        }else if (reductionReason.contains("sold",true)) {
            if (checkIfEmpty(customer)) {
                tvSelectReductionReason.setTextColor(resources.getColor(R.color.black))
                tvCustomer.setTextColor(resources.getColor(R.color.red))
            } else if (checkIfEmpty(flockCost)) {
                tvCustomer.setTextColor(resources.getColor(R.color.black))
                tvFlockCost.setTextColor(resources.getColor(R.color.red))
            } else if (checkIfEmpty(amountTheCustomerPaid)) {
                tvFlockCost.setTextColor(resources.getColor(R.color.black))
                tvAmountTheCustomerPaid.setTextColor(resources.getColor(R.color.red))
            }
            else {

                val numberOfRemainingFlock = theNumberOfBirdsAdded - theTotalNumberOfFlockQuantityReduced
                if (flockReductionQuantity.toInt() > numberOfRemainingFlock)
                {
                    tvFlockReductionQuantity.setTextColor(resources.getColor(R.color.red))
                    Toast.makeText(requireContext(), "You can not sell more than $numberOfRemainingFlock of $flock", Toast.LENGTH_LONG).show()
                }
                else{
                val newDate = simpleDateFormat.parse(date)
                val flockInventoryReductionDate = newDate?.time?.let { java.sql.Date(it)}!!
                val amountOwed = (flockCost.toDouble() - amountTheCustomerPaid.toDouble())

                mFlockInventoryReductionViewModel.updateFlockInventoryReduction(
                    flockInventoryReductionId,
                    flock,
                    flockInventoryReductionDate,
                    flockReductionQuantity.toInt(),
                    customer,
                    flockCost.toDouble(),
                    amountTheCustomerPaid.toDouble(),
                    amountOwed,
                    reductionReason,
                    shortNotes
                )
                Toast.makeText(requireContext(), "Successfully Updated", Toast.LENGTH_LONG).show()

                val fragmentManager: FragmentManager = parentFragmentManager
                fragmentManager.beginTransaction().replace(
                    R.id.flFlockActivity,
                    FlockReductionList()
                ).addToBackStack(null).commit()
                }
            }
        } else {
            val numberOfRemainingFlock = theNumberOfBirdsAdded - theTotalNumberOfFlockQuantityReduced

            if (flockReductionQuantity.toInt() > numberOfRemainingFlock)
            {
                tvFlockReductionQuantity.setTextColor(resources.getColor(R.color.red))
                Toast.makeText(requireContext(), "You can not reduce more than $numberOfRemainingFlock of $flock", Toast.LENGTH_LONG).show()
            }
            else{
            val newDate = simpleDateFormat.parse(date)
            val flockInventoryReductionDate = newDate?.time?.let { java.sql.Date(it)}!!
            val amountOwed = (flockCost.toDouble() - amountTheCustomerPaid.toDouble())

            mFlockInventoryReductionViewModel.updateFlockInventoryReduction(
                flockInventoryReductionId,
                flock,
                flockInventoryReductionDate,
                flockReductionQuantity.toInt(),
                customer,
                flockCost.toDouble(),
                amountTheCustomerPaid.toDouble(),
                amountOwed,
                reductionReason,
                shortNotes
            )
            Toast.makeText(requireContext(), "Successfully Updated", Toast.LENGTH_LONG).show()

            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flFlockActivity,
                FlockReductionList()
            ).addToBackStack(null).commit()
            }
        }
    }

    private fun checkIfEmpty(value: String): Boolean {
        return (TextUtils.isEmpty(value))
    }

    private fun reductionReasonTextWatcher(llAddCustomer: LinearLayout) = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            llAddCustomer.isGone = !(s.toString().contains("sold",true))
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            llAddCustomer.isGone = !(s.toString().contains("sold",true))
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            llAddCustomer.isGone = !(s.toString().contains("sold",true))
        }
    }

    private fun selectFlockNameTextWatcher() = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            mFlockInventoryReductionViewModel.getSum(s.toString())
            mFlockInventoryAdditionViewModel.getFlockAddedByFlockName(s.toString())
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }
}