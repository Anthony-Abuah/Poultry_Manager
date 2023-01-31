package com.example.pkompoultrymanager.inventory.feed.reduction.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
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
import com.example.pkompoultrymanager.inventory.feed.FeedActivity
import com.example.pkompoultrymanager.inventory.feed.addition.FeedInventoryAdditionViewModel
import com.example.pkompoultrymanager.inventory.feed.reduction.FeedInventoryReductionViewModel
import com.example.pkompoultrymanager.inventory.flock.addition.FlockInventoryAdditionViewModel
import com.example.pkompoultrymanager.tables.feed.FeedViewModel
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*

class UpdateFeedInventoryReductionForm : Fragment() {

    private lateinit var mFeedInventoryReductionViewModel: FeedInventoryReductionViewModel
    private lateinit var mFeedInventoryAdditionViewModel: FeedInventoryAdditionViewModel
    private lateinit var mFlockInventoryAdditionViewModel: FlockInventoryAdditionViewModel
    private lateinit var mFeedViewModel: FeedViewModel
    private lateinit var tvDatePicker: TextView
    private lateinit var myFarmInfo: SharedPreferences

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_update_feed_inventory_reduction_form, container, false)


        // Instantiating the viewModel
        mFeedInventoryReductionViewModel =
            ViewModelProvider(this)[FeedInventoryReductionViewModel::class.java]
        mFeedInventoryAdditionViewModel =
            ViewModelProvider(this)[FeedInventoryAdditionViewModel::class.java]
        mFlockInventoryAdditionViewModel = ViewModelProvider(this)[FlockInventoryAdditionViewModel::class.java]
        mFeedViewModel = ViewModelProvider(this)[FeedViewModel::class.java]

        // Instantiating the shared Preferences
        myFarmInfo = context?.getSharedPreferences("MyFarmInfo", Context.MODE_PRIVATE)!!
        val measuringUnit = myFarmInfo.getString("measuringUnit", "kg")!!

        // Instantiating the calendar and getting current system date
        val myCalendar = Calendar.getInstance()
        val day = myCalendar.get(Calendar.DAY_OF_MONTH)
        val month = myCalendar.get(Calendar.MONTH)
        val year = myCalendar.get(Calendar.YEAR)
        val dateToday = "$day-${month+1}-$year"

        // Declaring the various Views
        // display views
        val back = view.findViewById<ImageButton>(R.id.ibBack_UpdateFeedInventoryReductionForm)
        val save = view.findViewById<Button>(R.id.btnSave_UpdateFeedInventoryReductionForm)
        val tvEnterDate = view.findViewById<TextView>(R.id.tvEnterDate_UpdateFeedInventoryReductionForm)
        val llSelectFlock = view.findViewById<LinearLayout>(R.id.llSelectFlock_UpdateFeedInventoryReductionForm)
        val tvSelectFeed = view.findViewById<TextView>(R.id.tvSelectFeed_UpdateFeedInventoryReductionForm)
        val tvSelectFlock = view.findViewById<TextView>(R.id.tvSelectFlock_UpdateFeedInventoryReductionForm)
        val tvReductionReason = view.findViewById<TextView>(R.id.tvReductionReason_UpdateFeedInventoryReductionForm)
        val tvFeedQuantity = view.findViewById<TextView>(R.id.tvFeedQuantity_UpdateFeedInventoryReductionForm)
        // value holder views
        tvDatePicker = view.findViewById(R.id.tvDatePicker_UpdateFeedInventoryReductionForm)
        val etFeedQuantityUsed = view.findViewById<EditText>(R.id.etFeedQuantity_UpdateFeedInventoryReductionForm)
        val actvSelectFeed = view.findViewById<AutoCompleteTextView>(R.id.actvSelectFeed_UpdateFeedInventoryReductionForm)
        val actvSelectReductionReason = view.findViewById<AutoCompleteTextView>(R.id.actvReductionReason_UpdateFeedInventoryReductionForm)
        val actvSelectFlock = view.findViewById<AutoCompleteTextView>(R.id.actvSelectFlock_UpdateFeedInventoryReductionForm)
        val etShortNotes = view.findViewById<EditText>(R.id.etShortNotes_UpdateFeedInventoryReductionForm)

        actvSelectFeed.addTextChangedListener(selectFeedNameTextWatcher())
        actvSelectReductionReason.addTextChangedListener(selectReductionReasonTextWatcher(llSelectFlock))

        var sumTotalOfFeedAdded = 0.0
        mFeedInventoryAdditionViewModel.sumOfFeedInventory.observe(viewLifecycleOwner){
                totalFeed->totalFeed.let{
            sumTotalOfFeedAdded = it ?:0.0
        }
        }
        var sumTotalOfFeedReduced = 0.0
        mFeedInventoryReductionViewModel.sumOfFeedReduction.observe(viewLifecycleOwner){
                totalFeed->totalFeed.let{
            sumTotalOfFeedReduced = it ?:0.0
        }
        }

        // Feed Addition ViewModel Usages
        mFeedInventoryAdditionViewModel.displayAllFeedInventoryAdditions.observe(viewLifecycleOwner) { feed ->
            feed.let { FeedTable ->
                val theFeedNames = mutableListOf<String>()
                for (feedNames in FeedTable){
                    theFeedNames.add(feedNames.FeedName)
                }
                // set the suggestion of feed adapter to use the feedNames added
                val feedAdapter = ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, theFeedNames)
                actvSelectFeed.setAdapter(feedAdapter)
            }
        }

        // Display the date picker
        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDate(myCalendar)
        }
        // Select the date
        tvDatePicker.setOnClickListener {
            DatePickerDialog(
                requireContext(), datePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(
                    Calendar.MONTH
                ),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // Feed Suggestions
        mFeedViewModel.displayAllFeeds.observe(viewLifecycleOwner) {
                feeds -> feeds.let {
            val feedNames = mutableListOf<String>()
            for (Feed in it) { feedNames.add(Feed.FeedName) }
            val feedAdapter = ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, feedNames)
            actvSelectFeed.setAdapter(feedAdapter) }
        }
        // Reduction Reason Suggestions
        val reductionReasonSuggestion = arrayOf("Feeding", "Spoilt", "Spilt",)
        val selectReductionReasonAdapter = ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, reductionReasonSuggestion)
        actvSelectReductionReason.setAdapter(selectReductionReasonAdapter)

        val flockNames = mutableListOf<String>()
        // Flock Suggestions
        mFlockInventoryAdditionViewModel.displayAllFlockInventoryAdditions.observe(viewLifecycleOwner) { flocks ->
            flocks.let { _flocks ->
                for (flock in _flocks) { flockNames.add(flock.FlockName) }
                // set the suggestion of flock adapter to use the flockNames added
                val flockAdapter = ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, flockNames.distinct())
                actvSelectFlock.setAdapter(flockAdapter)
            }
        }

        // More info on the select Feed Question
        tvSelectFeed.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.delete_prompt_layout, null)
            with(builder) {
                setTitle(R.string.SelectFeed_info)
                setPositiveButton("Ok") { dialog, where -> }
                setView(dialogLayout)
                show()
            }
        }

        // More info on the select Reduction Reason Question
        tvReductionReason.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.delete_prompt_layout, null)
            with(builder) {
                setTitle(R.string.FeedReductionReason_info)
                setPositiveButton("Ok") { dialog, where -> }
                setView(dialogLayout)
                show()
            }
        }

        // More info on the select Flock Question
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

        // More info on the feed Quantity used Question
        etFeedQuantityUsed.setOnTouchListener(View.OnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= etFeedQuantityUsed.right - etFeedQuantityUsed.compoundDrawables[DRAWABLE_RIGHT].bounds.width()
                ) {
                    val builder = AlertDialog.Builder(context)
                    val inflater = layoutInflater
                    val dialogLayout = inflater.inflate(R.layout.delete_prompt_layout, null)
                    with(builder) {
                        setTitle(R.string.FeedQuantity_info)
                        setPositiveButton("Ok") { dialog, where -> }
                        setView(dialogLayout)
                        show()
                    }
                }
            }
            false
        })

        // Go back to previous Activity or Layout or View
        back.setOnClickListener {
            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.popBackStackImmediate()
        }


        // get the value from the communicator
        val theFeedInventoryReductionId = arguments?.getString("feedInventoryReductionId")!!
        val theDateReduced = arguments?.getString("reductionDate")!!
        val theFeedName = arguments?.getString("feedName")!!
        val theReductionQuantity = arguments?.getString("reductionQuantity")!!
        val theFlockName = arguments?.getString("flockName")!!
        val theReductionReason = arguments?.getString("reductionReason")!!
        val theShortNotes = arguments?.getString("shortNotes")!!

        // bind the values to the views
        tvDatePicker.text = theDateReduced
        actvSelectFeed.setText(theFeedName)
        actvSelectFlock.setText(theFlockName)
        actvSelectReductionReason.setText(theReductionReason)
        etFeedQuantityUsed.setText(theReductionQuantity)
        etShortNotes.setText(theShortNotes)

        // Save feed inventory reduction into the database
        save.setOnClickListener {
            insertDataToDatabase(
                theFeedInventoryReductionId.toInt(),
                tvDatePicker,
                tvEnterDate,
                tvSelectFlock,
                actvSelectFlock,
                tvReductionReason,
                actvSelectReductionReason,
                tvSelectFeed,
                actvSelectFeed,
                tvFeedQuantity,
                etFeedQuantityUsed,
                etShortNotes,
                measuringUnit,
                sumTotalOfFeedAdded,
                sumTotalOfFeedReduced,
                dateToday
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
        feedInventoryReductionId: Int,
        tvDatePicker: TextView,
        tvEnterDate: TextView,
        tvSelectFlock: TextView,
        actvSelectFlock: AutoCompleteTextView,
        tvReductionReason: TextView,
        actvReductionReason: AutoCompleteTextView,
        tvSelectFeed: TextView,
        actvSelectFeed: AutoCompleteTextView,
        tvFeedQuantity: TextView,
        etFeedQuantity: EditText,
        etShortNotes: EditText,
        measuringUnit: String,
        sumTotalOfFeedAdded: Double,
        sumTotalOfFeedReduced: Double,
        dateToday: String
    ) {
        val date = tvDatePicker.text.toString()
        val flockName = actvSelectFlock.text.toString()
        val reason = actvReductionReason.text.toString()
        val feedName = actvSelectFeed.text.toString()
        val quantity = etFeedQuantity.text.toString()
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
            Toast.makeText(requireContext(), "You can only add $feedName on $dateToday or before", Toast.LENGTH_SHORT).show()
        }else if (checkIfEmpty(feedName)) {
            tvSelectFlock.setTextColor(resources.getColor(R.color.black))
            tvSelectFeed.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Select Feed", Toast.LENGTH_LONG).show()
        } else if (checkIfEmpty(quantity)) {
            tvSelectFeed.setTextColor(resources.getColor(R.color.black))
            tvFeedQuantity.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Enter Feed Quantity", Toast.LENGTH_LONG).show()
        }else if (checkIfEmpty(reason)) {
            tvFeedQuantity.setTextColor(resources.getColor(R.color.black))
            tvReductionReason.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Select Reduction Reason", Toast.LENGTH_LONG).show()
        } else if (reason.contains("feed", true)) {

                // cast the java.util date to java.sql date
                val newDate = simpleDateFormat.parse(date)
                val feedInventoryReductionDate = newDate?.time?.let { java.sql.Date(it)}!!
                // convert feed weight to kilograms
                val measuringUnitValue = if (measuringUnit == "kg") {1.0} else 2.20462
                val feedQuantity = quantity.toDouble() / measuringUnitValue
                val feedQuantityRemaining = sumTotalOfFeedAdded - sumTotalOfFeedReduced

                if (checkIfEmpty(flockName)) {
                    tvReductionReason.setTextColor(resources.getColor(R.color.black))
                    tvSelectFlock.setTextColor(resources.getColor(R.color.red))
                    Toast.makeText(requireContext(), "Please Select Flock", Toast.LENGTH_LONG).show()
                }else if (feedQuantityRemaining < feedQuantity){
                    tvFeedQuantity.setTextColor(resources.getColor(R.color.red))
                    Toast.makeText(requireContext(), "You can not reduce more than $feedQuantityRemaining of $feedName feed", Toast.LENGTH_LONG).show()
                }
                else {
                    // update the feed inventory reduction
                    mFeedInventoryReductionViewModel.updateFeedInventoryReduction(
                        feedInventoryReductionId,
                        feedName,
                        feedInventoryReductionDate,
                        feedQuantity,
                        flockName,
                        reason,
                        shortNotes
                    )

                    Toast.makeText(requireContext(), "Successfully Added", Toast.LENGTH_LONG).show()

                    val fragmentManager: FragmentManager = parentFragmentManager
                    fragmentManager.beginTransaction().replace(
                        R.id.flFeedActivity,
                        FeedInventoryReductionList()
                    ).commit()}
            }

            else {
            // cast the java.util date to java.sql date
            val newDate = simpleDateFormat.parse(date)
            val feedInventoryReductionDate = newDate?.time?.let { java.sql.Date(it)}!!
            // convert feed weight to kilograms
            val measuringUnitValue = if (measuringUnit == "kg") {1.0} else 2.20462
            val feedQuantity = quantity.toDouble() / measuringUnitValue

            val feedQuantityRemaining = sumTotalOfFeedAdded - sumTotalOfFeedReduced

            if (checkIfEmpty(flockName)) {
                tvReductionReason.setTextColor(resources.getColor(R.color.black))
                tvSelectFlock.setTextColor(resources.getColor(R.color.red))
                Toast.makeText(requireContext(), "Please Select Flock", Toast.LENGTH_LONG).show()
            }else if (feedQuantityRemaining < feedQuantity){
                tvFeedQuantity.setTextColor(resources.getColor(R.color.red))
                Toast.makeText(requireContext(), "You can not reduce more than $feedQuantityRemaining of $feedName feed", Toast.LENGTH_LONG).show()
            }
            else {
            // update the feed inventory reduction
            mFeedInventoryReductionViewModel.updateFeedInventoryReduction(
                feedInventoryReductionId,
                feedName,
                feedInventoryReductionDate,
                feedQuantity,
                flockName,
                reason,
                shortNotes
            )

            Toast.makeText(requireContext(), "Successfully Added", Toast.LENGTH_LONG).show()

            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flFeedActivity,
                FeedInventoryReductionList()
            ).commit()}
        }
    }

    private fun checkIfEmpty(value: String): Boolean {
        return (TextUtils.isEmpty(value))
    }
    private fun selectFeedNameTextWatcher() = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            mFeedInventoryAdditionViewModel.getTotalFeedQuantityByFeedName(s.toString())
            mFeedInventoryReductionViewModel.getTotalFeedReductionQuantityByFeedName(s.toString())
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }
    private fun selectReductionReasonTextWatcher(llSelectFlock: LinearLayout) = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            if (s != null) {
                llSelectFlock.isGone = !s.contains("feed",true)
            }
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }
}