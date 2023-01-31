package com.example.pkompoultrymanager.inventory.feed.addition.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import com.example.pkompoultrymanager.inventory.feed.FeedActivity
import com.example.pkompoultrymanager.inventory.feed.addition.FeedInventoryAddition
import com.example.pkompoultrymanager.inventory.feed.addition.FeedInventoryAdditionViewModel
import com.example.pkompoultrymanager.tables.feed.FeedViewModel
import com.example.pkompoultrymanager.tables.feed.fragments.AddFeed
import com.example.pkompoultrymanager.tables.feed_source.FeedSourceViewModel
import com.example.pkompoultrymanager.tables.feed_source.fragments.AddFeedSource
import java.text.SimpleDateFormat
import java.util.*

class UpdateFeedInventoryAdditionForm : Fragment() {

    private lateinit var mFeedInventoryAdditionViewModel: FeedInventoryAdditionViewModel
    private lateinit var mFeedViewModel: FeedViewModel
    private lateinit var mFeedSourceViewModel: FeedSourceViewModel
    private lateinit var tvDatePicker: TextView
    private lateinit var myFarmInfo: SharedPreferences

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_update_feed_inventory_addition_form, container, false)
// Instantiate the viewModel
        mFeedInventoryAdditionViewModel = ViewModelProvider(this)[FeedInventoryAdditionViewModel::class.java]
        mFeedViewModel = ViewModelProvider(this)[FeedViewModel::class.java]
        mFeedSourceViewModel = ViewModelProvider(this)[FeedSourceViewModel::class.java]

        // Instantiate the shared Preferences
        myFarmInfo = context?.getSharedPreferences("MyFarmInfo", Context.MODE_PRIVATE)!!
        val measuringUnit = myFarmInfo.getString("measuringUnit","kg")!!


        // Instantiating the calendar and getting current system date
        val myCalendar = Calendar.getInstance()
        val day =  myCalendar.get(Calendar.DAY_OF_MONTH)
        val month = myCalendar.get(Calendar.MONTH)
        val year = myCalendar.get(Calendar.YEAR)
        val dateToday = "$day-${month+1}-$year"


        // Declaring the various Views
        // display views
        val tvSelectFeed = view.findViewById<TextView>(R.id.tvSelectFeed_UpdateFeedInventoryAdditionForm)
        val tvSelectFeedSource = view.findViewById<TextView>(R.id.tvSelectFeedSource_UpdateFeedInventoryAdditionForm)
        val tvEnterDate = view.findViewById<TextView>(R.id.tvEnterDate_UpdateFeedInventoryAdditionForm)
        val tvFeedQuantity = view.findViewById<TextView>(R.id.tvFeedQuantity_UpdateFeedInventoryAdditionForm)
        val tvFeedCost = view.findViewById<TextView>(R.id.tvFeedCost_UpdateFeedInventoryAdditionForm)
        val ivAddFeed = view.findViewById<ImageView>(R.id.ivAddFeed_UpdateFeedInventoryAdditionForm)
        val ivAddFeedSource = view.findViewById<ImageView>(R.id.ivAddFeedSource_UpdateFeedInventoryAdditionForm)
        val update = view.findViewById<Button>(R.id.btnUpdate_UpdateFeedInventoryAdditionForm)
        val back = view.findViewById<ImageButton>(R.id.ibBack_UpdateFeedInventoryAdditionForm)

        // value holder views
        tvDatePicker = view.findViewById(R.id.tvDatePicker_UpdateFeedInventoryAdditionForm)
        val etFeedQuantity = view.findViewById<EditText>(R.id.etFeedQuantity_UpdateFeedInventoryAdditionForm)
        val etFeedCost = view.findViewById<EditText>(R.id.etFeedCost_UpdateFeedInventoryAdditionForm)
        val etShortNotes = view.findViewById<EditText>(R.id.etShortNotes_UpdateFeedInventoryAdditionForm)
        val actvSelectFeed = view.findViewById<AutoCompleteTextView>(R.id.actvSelectFeed_UpdateFeedInventoryAdditionForm)
        val actvSelectFeedSource = view.findViewById<AutoCompleteTextView>(R.id.actvSelectFeedSource_UpdateFeedInventoryAdditionForm)



        // display date picker
        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDate(myCalendar)
        }

        // select date
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

        // FeedSource Suggestions
        mFeedSourceViewModel.displayAllFeedSources.observe(viewLifecycleOwner) {
                feedSources -> feedSources.let {
            val feedSourceNames = mutableListOf<String>()
            for (FeedSource in it) { feedSourceNames.add(FeedSource.FeedSourceName) }
            val feedSourceAdapter = ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, feedSourceNames)
            actvSelectFeedSource.setAdapter(feedSourceAdapter) }
        }

        // Go back to previous Activity or Layout or View
        back.setOnClickListener {
            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.popBackStackImmediate()
        }

        // More info of the select Feed Question
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

        // More info on the select Feed Source Question
        tvSelectFeedSource.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.delete_prompt_layout, null)
            with(builder) {
                setTitle(R.string.FeedSource_info)
                setPositiveButton("Ok") { dialog, where -> }
                setView(dialogLayout)
                show()
            }
        }

        // More info on the feed Quantity Question
        etFeedQuantity.setOnTouchListener(View.OnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= etFeedQuantity.right - etFeedQuantity.compoundDrawables[DRAWABLE_RIGHT].bounds.width()
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

        // More info on the feed Cost Question
        etFeedCost.setOnTouchListener(View.OnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= etFeedCost.right - etFeedCost.compoundDrawables[DRAWABLE_RIGHT].bounds.width()
                ) {
                    val builder = AlertDialog.Builder(context)
                    val inflater = layoutInflater
                    val dialogLayout = inflater.inflate(R.layout.delete_prompt_layout, null)
                    with(builder) {
                        setTitle(R.string.FeedCost_info)
                        setPositiveButton("Ok") { dialog, where -> }
                        setView(dialogLayout)
                        show()
                    }
                }
            }
            false
        })

        // Code to Add Feed
        ivAddFeed.setOnClickListener {
            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flFeedActivity,
                AddFeed()
            ).commit()
        }

        // Code to Add Feed Source
        ivAddFeedSource.setOnClickListener {
            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flFeedActivity,
                AddFeedSource()
            ).commit()
        }
        // get the values from the communicator
        val theFeedInventoryAdditionId = arguments?.getString("feedInventoryAdditionId")!!
        val theDateAcquired = arguments?.getString("dateAcquired")!!
        val theFeedName = arguments?.getString("feedName")!!
        val theFeedQuantity = arguments?.getString("feedQuantity")!!
        val theFeedCost = arguments?.getString("feedCost")!!
        val theFeedSource = arguments?.getString("feedSource")!!
        val theShortNotes = arguments?.getString("shortNotes")!!

        // bind the values to the views
        tvDatePicker.text = theDateAcquired
        actvSelectFeed.setText(theFeedName)
        etFeedQuantity.setText(theFeedQuantity)
        etFeedCost.setText(theFeedCost)
        actvSelectFeedSource.setText(theFeedSource)
        etShortNotes.setText(theShortNotes)

        // code to Save Feed Inventory Into the Database
        update.setOnClickListener {
            insertFeedInventoryIntoDatabase(
                theFeedInventoryAdditionId.toInt(),
                tvDatePicker,
                tvEnterDate,
                tvSelectFeed,
                actvSelectFeed,
                tvFeedQuantity,
                etFeedQuantity,
                tvFeedCost,
                etFeedCost,
                actvSelectFeedSource,
                etShortNotes,
                measuringUnit,
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


    private fun insertFeedInventoryIntoDatabase(
        feedInventoryAdditionId: Int,
        tvDatePicker: TextView,
        tvEnterDate: TextView,
        tvSelectFeed: TextView,
        actvSelectFeed: AutoCompleteTextView,
        tvFeedQuantity: TextView,
        etFeedQuantity: EditText,
        tvFeedCost: TextView,
        etFeedCost: EditText,
        actvSelectFeedSource: AutoCompleteTextView,
        etShortNotes: EditText,
        measuringUnit: String,
        dateToday: String
    ) {
        val date = tvDatePicker.text.toString()
        val feed = actvSelectFeed.text.toString()
        val quantity = etFeedQuantity.text.toString()
        val cost = etFeedCost.text.toString()
        val source = actvSelectFeedSource.text.toString()
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
            Toast.makeText(requireContext(), "You can only add $feed on $dateToday or before", Toast.LENGTH_SHORT).show()
        } else if (checkIfEmpty(feed)) {
            tvEnterDate.setTextColor(resources.getColor(R.color.black))
            tvSelectFeed.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Select Feed", Toast.LENGTH_LONG).show()
        } else if (checkIfEmpty(quantity)) {
            tvSelectFeed.setTextColor(resources.getColor(R.color.black))
            tvFeedQuantity.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Enter Feed Quantity", Toast.LENGTH_LONG).show()
        } else if (checkIfEmpty(cost)) {
            tvFeedQuantity.setTextColor(resources.getColor(R.color.black))
            tvFeedCost.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Enter Feed Cost", Toast.LENGTH_LONG).show()
        } else {
            // get the measuring unit and ensure that the weight entered is converted  to kg
            val measuringUnitValue = if (measuringUnit == "kg" || measuringUnit == "kilograms(kg)" ) {1.0} else 2.20462
            val feedQuantity = quantity.toDouble() / measuringUnitValue

            // cast the java.util date to java.sql date
            val newDate = simpleDateFormat.parse(date)
            val feedInventoryAdditionDate = newDate?.time?.let { java.sql.Date(it)}!!

            // add feed inventory to the database via the viewModel
            mFeedInventoryAdditionViewModel.updateFeedInventoryAddition(feedInventoryAdditionId, feed, feedInventoryAdditionDate, feedQuantity, source,cost.toDouble(),shortNotes )

            Toast.makeText(requireContext(), "Successfully Added", Toast.LENGTH_LONG).show()

            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flFeedActivity,
                FeedInventoryAdditionList()
            ).commit()
        }
    }

    private fun checkIfEmpty(value: String): Boolean {
        return (TextUtils.isEmpty(value))
    }

}