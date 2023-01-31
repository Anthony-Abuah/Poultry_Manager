package com.example.pkompoultrymanager.inventory.flock.addition.fragments

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
import com.example.pkompoultrymanager.inventory.flock.addition.FlockInventoryAddition
import com.example.pkompoultrymanager.inventory.flock.addition.FlockInventoryAdditionViewModel
import com.example.pkompoultrymanager.tables.flock_source.FlockSourceViewModel
import com.example.pkompoultrymanager.tables.flock_source.fragments.AddFlockSource
import java.text.SimpleDateFormat
import java.util.*

class FlockAdditionForm : Fragment() {
    private lateinit var mFlockInventoryAdditionViewModel: FlockInventoryAdditionViewModel
    private lateinit var mFlockSourceViewModel: FlockSourceViewModel

    private lateinit var tvDatePicker: TextView

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_flock_addition_form, container, false)

        // initialize the viewModel
        mFlockInventoryAdditionViewModel = ViewModelProvider(this)[FlockInventoryAdditionViewModel::class.java]
        mFlockSourceViewModel = ViewModelProvider(this)[FlockSourceViewModel::class.java]

        // Instantiating the calendar and getting current system date
        val myCalendar = Calendar.getInstance()
        val day =  myCalendar.get(Calendar.DAY_OF_MONTH)
        val month = myCalendar.get(Calendar.MONTH)
        val year = myCalendar.get(Calendar.YEAR)
        val dateToday = "$day-${month+1}-$year"


        // Declaring the Views
        // display views
        val tvSelectFlockPurpose = view.findViewById<TextView>(R.id.tvFlockPurpose_FlockInventoryAdditionForm)
        val tvSelectFlockSource = view.findViewById<TextView>(R.id.tvFlockSource_FlockInventoryAdditionForm)
        val tvEnterDate = view.findViewById<TextView>(R.id.tvEnterDate_FlockInventoryAdditionForm)
        val tvFlockName = view.findViewById<TextView>(R.id.tvFlockName_FlockInventoryAdditionForm)
        val tvFlockAge = view.findViewById<TextView>(R.id.tvFlockAge_FlockInventoryAdditionForm)
        val tvFlockQuantity = view.findViewById<TextView>(R.id.tvFlockQuantity_FlockInventoryAdditionForm)
        val tvFlockBreed = view.findViewById<TextView>(R.id.tvFlockBreed_FlockInventoryAdditionForm)
        val tvFlockCost = view.findViewById<TextView>(R.id.tvFlockCost_FlockInventoryAdditionForm)
        val ivAddFlockSource = view.findViewById<ImageView>(R.id.ivAddFlockSource_FlockInventoryAdditionForm)
        val back = view.findViewById<ImageButton>(R.id.ibBack_FlockInventoryAdditionForm)
        val save = view.findViewById<Button>(R.id.btnSave_FlockInventoryAdditionForm)

        // value holder views
        tvDatePicker = view.findViewById(R.id.tvDatePicker_FlockInventoryAdditionForm)
        val etFlockName = view.findViewById<EditText>(R.id.etFlockName_FlockInventoryAdditionForm)
        val etFlockAge = view.findViewById<EditText>(R.id.etFlockAge_FlockInventoryAdditionForm)
        val etFlockQuantity = view.findViewById<EditText>(R.id.etFlockQuantity_FlockInventoryAdditionForm)
        val etFlockBreed = view.findViewById<EditText>(R.id.etFlockBreed_FlockInventoryAdditionForm)
        val etFlockCost = view.findViewById<EditText>(R.id.etFlockCost_FlockInventoryAdditionForm)
        val etShortNotes = view.findViewById<EditText>(R.id.etShortNotes_FlockInventoryAdditionForm)
        val actvSelectFlockPurpose = view.findViewById<AutoCompleteTextView>(R.id.actvFlockPurpose_FlockInventoryAdditionForm)
        val actvSelectFlockSource = view.findViewById<AutoCompleteTextView>(R.id.actvFlockSource_FlockInventoryAdditionForm)

        // display and select date
        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDate(myCalendar)
        }
        tvDatePicker.setOnClickListener { DatePickerDialog(requireContext(), datePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show() }

        val flockNames = mutableListOf<String>()
        mFlockInventoryAdditionViewModel.allFlockNames.observe(viewLifecycleOwner) {
                flocks -> flocks.let {
            for (allFlocks in it) { flockNames.add(allFlocks) }
        }}
        // FlockSource Suggestions
        mFlockSourceViewModel.displayAllFlockSources.observe(viewLifecycleOwner) {
            flockSources -> flockSources.let {
            val flockSourceNames = mutableListOf<String>()
            for (FlockSource in it) { flockSourceNames.add(FlockSource.FlockSourceName) }
            val flockSourceAdapter = ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, flockSourceNames)
            actvSelectFlockSource.setAdapter(flockSourceAdapter) }
        }

        // Flock Purpose Suggestion
        val flockPurposeSuggestions = arrayOf("Laying", "Meat", "Both")
        val selectFlockPurposeAdapter = ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, flockPurposeSuggestions)
        actvSelectFlockPurpose.setAdapter(selectFlockPurposeAdapter)

        // Flock Source Suggestion
        val flockSourceSuggestions = arrayOf("Darko Farms", "Holland Warehouse", "Foreign birds")
        val selectFlockSourceAdapter = ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, flockSourceSuggestions)
        actvSelectFlockSource.setAdapter(selectFlockSourceAdapter)

        // More info on the select Flock Purpose Question
        tvSelectFlockPurpose.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.delete_prompt_layout, null)
            with(builder) {
                setTitle(R.string.FlockPurpose_info)
                setPositiveButton("Ok") { dialog, where -> }
                setView(dialogLayout)
                show()
            }
        }

        // More info on the Select Flock Source Question
        tvSelectFlockSource.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.delete_prompt_layout, null)
            with(builder) {
                setTitle(R.string.FlockSource_info)
                setPositiveButton("Ok") { dialog, where -> }
                setView(dialogLayout)
                show()
            }
        }

        // More info on the Flock Name Question
        etFlockName.setOnTouchListener(View.OnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= etFlockName.right - etFlockName.compoundDrawables[DRAWABLE_RIGHT].bounds.width()
                ) {
                    val builder = AlertDialog.Builder(context)
                    val inflater = layoutInflater
                    val dialogLayout = inflater.inflate(R.layout.delete_prompt_layout, null)
                    with(builder) {
                        setTitle(R.string.FlockName_info)
                        setPositiveButton("Ok") { dialog, where -> }
                        setView(dialogLayout)
                        show()
                    }
                }
            }
            false
        })

        // more info on the Flock Age Question
        etFlockAge.setOnTouchListener(View.OnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= etFlockAge.right - etFlockAge.compoundDrawables[DRAWABLE_RIGHT].bounds.width()
                ) {
                    val builder = AlertDialog.Builder(context)
                    val inflater = layoutInflater
                    val dialogLayout = inflater.inflate(R.layout.delete_prompt_layout, null)
                    with(builder) {
                        setTitle(R.string.FlockAge_info)
                        setPositiveButton("Ok") { dialog, where -> }
                        setView(dialogLayout)
                        show()
                    }
                }
            }
            false
        })

        // more info on the Flock Breed Question
        etFlockBreed.setOnTouchListener(View.OnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= etFlockBreed.right - etFlockBreed.compoundDrawables[DRAWABLE_RIGHT].bounds.width()
                ) {
                    val builder = AlertDialog.Builder(context)
                    val inflater = layoutInflater
                    val dialogLayout = inflater.inflate(R.layout.delete_prompt_layout, null)
                    with(builder) {
                        setTitle(R.string.FlockBreed_info)
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
                    val dialogLayout = inflater.inflate(R.layout.delete_prompt_layout, null)
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

        // more info on the Flock Quantity Question
        etFlockQuantity.setOnTouchListener(View.OnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= etFlockQuantity.right - etFlockQuantity.compoundDrawables[DRAWABLE_RIGHT].bounds.width()
                ) {
                    val builder = AlertDialog.Builder(context)
                    val inflater = layoutInflater
                    val dialogLayout = inflater.inflate(R.layout.delete_prompt_layout, null)
                    with(builder) {
                        setTitle(R.string.FlockQuantity_info)
                        setPositiveButton("Ok") { dialog, where -> }
                        setView(dialogLayout)
                        show()
                    }
                }
            }
            false
        })

        // Add flock Source
        ivAddFlockSource.setOnClickListener {
            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flFlockActivity,
                AddFlockSource()
            ).commit()
        }

        // go back to the previous Activity or Layout or view
        back.setOnClickListener {
            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.popBackStackImmediate()

        }


        // save or insert data into the database
        save.setOnClickListener {
            insertDataToDatabase(
                tvDatePicker, tvEnterDate, tvFlockName, etFlockName, tvFlockQuantity, etFlockQuantity, tvFlockAge, etFlockAge, etFlockBreed, tvSelectFlockPurpose, actvSelectFlockPurpose, tvFlockCost, etFlockCost, actvSelectFlockSource, etShortNotes,flockNames, dateToday)
        }



        return view
    }

    private fun updateDate(myCalendar: Calendar) {
        val myFormat = "EEE, dd MMM, yyyy"
        val simpleDateFormat = SimpleDateFormat(myFormat, Locale.UK)
        tvDatePicker.text = simpleDateFormat.format(myCalendar.time)
    }

    private fun insertDataToDatabase(
        tvDatePicker: TextView,
        tvEnterDate: TextView,
        tvFlockName: TextView,
        etFlockName: EditText,
        tvFlockQuantity: TextView,
        etFlockQuantity: EditText,
        tvFlockAge: TextView,
        etFlockAge: EditText,
        etFlockBreed: EditText,
        tvSelectFlockPurpose: TextView,
        actvSelectFlockPurpose: AutoCompleteTextView,
        tvFlockCost: TextView,
        etFlockCost: EditText,
        actvSelectFlockSource: AutoCompleteTextView,
        etShortNotes: EditText,
        flockNames: MutableList<String>,
        dateToday:String
    )
    {
        val date = tvDatePicker.text.toString()
        val flockName = etFlockName.text.toString()
        val flockQuantity = etFlockQuantity.text.toString()
        val flockAge = etFlockAge.text.toString()
        val flockBreed = etFlockBreed.text.toString()
        val flockPurpose = actvSelectFlockPurpose.text.toString()
        val flockCost = etFlockCost.text.toString()
        val flockSource = actvSelectFlockSource.text.toString()
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
            Toast.makeText(requireContext(), "You can only add $flockName on $dateToday or before", Toast.LENGTH_SHORT).show()
        } else if (checkIfEmpty(flockName)) {
            tvEnterDate.setTextColor(resources.getColor(R.color.black))
            tvFlockName.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Select Flock Type", Toast.LENGTH_LONG).show()
        } else if (checkIfEmpty(flockQuantity)) {
            tvFlockName.setTextColor(resources.getColor(R.color.black))
            tvFlockQuantity.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Select Flock Type", Toast.LENGTH_LONG).show()
        } else if (checkIfEmpty(flockAge)) {
            tvFlockQuantity.setTextColor(resources.getColor(R.color.black))
            tvFlockAge.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Select Flock Type", Toast.LENGTH_LONG).show()
        } else if (checkIfEmpty(flockCost)) {
            tvFlockAge.setTextColor(resources.getColor(R.color.black))
            tvFlockCost.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Select Flock Type", Toast.LENGTH_LONG).show()
        } else if (checkIfEmpty(flockPurpose)) {
            tvFlockCost.setTextColor(resources.getColor(R.color.black))
            tvSelectFlockPurpose.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Select Flock Type", Toast.LENGTH_LONG).show()
        } else {
            var flockNameAlreadyExists=false
            for (allFlockNames in flockNames){
                if(flockName == allFlockNames){
                    flockNameAlreadyExists = true
                }
            }
            if (flockNameAlreadyExists){
                tvFlockName.setTextColor(resources.getColor(R.color.red))
                Toast.makeText(requireContext(), "$flockName already exists", Toast.LENGTH_LONG).show()
                Toast.makeText(requireContext(), "Please enter a different Flock Name", Toast.LENGTH_LONG).show()
            }
            else {
                // parse the date from string to date
                val flockInventoryAdditionDate = simpleDateFormat.parse(date)

                val flockInventoryAddition = flockInventoryAdditionDate?.let {
                    FlockInventoryAddition(
                        0,
                        it,
                        flockName,
                        flockBreed,
                        flockSource,
                        flockPurpose,
                        flockQuantity.toInt(),
                        flockAge.toInt(),
                        flockCost.toDouble(),
                        shortNotes
                    )
                }
                // insert the data via the viewModel
                if (flockInventoryAddition != null) {
                    mFlockInventoryAdditionViewModel.addFlockInventoryAddition(
                        flockInventoryAddition
                    )
                }
                Toast.makeText(requireContext(), "Successfully Added", Toast.LENGTH_LONG).show()

                // navigate back to the flock inventory addition list
                val fragmentManager: FragmentManager = parentFragmentManager
                fragmentManager.beginTransaction().replace(
                    R.id.flFlockActivity,
                    FlockAdditionList()
                ).commit()
            }
        }
    }

    private fun checkIfEmpty(value: String): Boolean {
        return (TextUtils.isEmpty(value))
    }
}