package com.example.pkompoultrymanager.inventory.eggs.addition.fragments

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
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.pkompoultrymanager.R
import com.example.pkompoultrymanager.inventory.eggs.addition.EggInventoryAddition
import com.example.pkompoultrymanager.inventory.eggs.addition.EggInventoryAdditionViewModel
import com.example.pkompoultrymanager.inventory.flock.addition.FlockInventoryAdditionViewModel
import com.example.pkompoultrymanager.inventory.flock.addition.fragments.FlockAdditionForm
import com.example.pkompoultrymanager.tables.egg_type.EggTypeViewModel
import com.example.pkompoultrymanager.tables.egg_type.fragments.AddEggType
import java.text.SimpleDateFormat
import java.util.*

class EggsAdditionForm : Fragment() {
    private lateinit var mEggInventoryAdditionViewModel: EggInventoryAdditionViewModel
    private lateinit var mFlockInventoryAdditionViewModel: FlockInventoryAdditionViewModel
    private lateinit var mEggTypeViewModel: EggTypeViewModel
    private lateinit var tvDatePicker: TextView
    private lateinit var myFarmInfo: SharedPreferences

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_eggs_addition_form, container, false)
        mEggInventoryAdditionViewModel = ViewModelProvider(this)[EggInventoryAdditionViewModel::class.java]
        mFlockInventoryAdditionViewModel = ViewModelProvider(this)[FlockInventoryAdditionViewModel::class.java]
        mEggTypeViewModel = ViewModelProvider(this)[EggTypeViewModel::class.java]

        // initialize and get the shared Preferences
        myFarmInfo = context?.getSharedPreferences("MyFarmInfo", Context.MODE_PRIVATE)!!
        val numberOfEggs = myFarmInfo.getString("numberOfEggsPerCrate", "30")
        val numberOfEggsPerCrate = numberOfEggs!!.toInt()

        // Instantiating the calendar and getting current system date
        val myCalendar = Calendar.getInstance()
        val day =  myCalendar.get(Calendar.DAY_OF_MONTH)
        val month = myCalendar.get(Calendar.MONTH)
        val year = myCalendar.get(Calendar.YEAR)
        val dateToday = "$day-${month+1}-$year"

        // Declaring the various Views
        //display views
        val tvSelectFlock = view.findViewById<TextView>(R.id.tvSelectFlock_EggsInventoryAdditionForm)
        val tvEnterDate = view.findViewById<TextView>(R.id.tvEnterDate_EggsInventoryAdditionForm)
        val tvNumberOfCrates = view.findViewById<TextView>(R.id.tvNumberOfCrates_EggsInventoryAdditionForm)
        val tvNumberOfEggsNotInCrates = view.findViewById<TextView>(R.id.tvNumberOfEggsNotInCrates_EggsInventoryAdditionForm)
        val tvSelectEggType = view.findViewById<TextView>(R.id.tvSelectEggType_EggsInventoryAdditionForm)
        val ivAddFlock = view.findViewById<ImageView>(R.id.ivAddFlock_EggsInventoryAdditionForm)
        val ivAddEggType = view.findViewById<ImageView>(R.id.ivAddEggType_EggsInventoryAdditionForm)
        val back = view.findViewById<ImageButton>(R.id.ibBack_EggInventoryAdditionForm)
        val save = view.findViewById<Button>(R.id.btnSave_EggsInventoryAdditionForm)

        // value holder views
        tvDatePicker = view.findViewById(R.id.tvDatePicker_EggsInventoryAdditionForm)
        val etNumberOfCrates = view.findViewById<EditText>(R.id.etNumberOfCrates_EggsInventoryAdditionForm)
        val etNumberOfEggsNotInCrates = view.findViewById<EditText>(R.id.etNumberOfEggsNotInCrates_EggsInventoryAdditionForm)
        val actvSelectFlock = view.findViewById<AutoCompleteTextView>(R.id.actvSelectFlock_EggsInventoryAdditionForm)
        val actvSelectEggType = view.findViewById<AutoCompleteTextView>(R.id.actvSelectEggType_EggsInventoryAdditionForm)
        val etShortNotes = view.findViewById<EditText>(R.id.etShortNotes_EggsInventoryAdditionForm)

        actvSelectFlock.addTextChangedListener(selectFlockNameTextWatcher())

        var theFlockPurpose = ""
        mFlockInventoryAdditionViewModel.flockPurposeByFlockName.observe(viewLifecycleOwner){
                flockPurpose->flockPurpose.let { purpose-> theFlockPurpose = purpose ?: ""
        } }

        // EggType Suggestions
        mEggTypeViewModel.displayAllEggTypes.observe(viewLifecycleOwner) {
            eggTypes -> eggTypes.let {
            val eggTypeNames = mutableListOf<String>()
            for (EggType in it) { eggTypeNames.add(EggType.EggTypeName) }
            val eggTypeAdapter = ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, eggTypeNames)
            actvSelectEggType.setAdapter(eggTypeAdapter) }
        }

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

        val flockNames = mutableListOf<String>()
        // Flock Suggestions
        mFlockInventoryAdditionViewModel.displayAllFlockInventoryAdditions.observe(viewLifecycleOwner) { flocks ->
            flocks.let { FlockTable ->
                for (Flock in FlockTable) { flockNames.add(Flock.FlockName) }
                // set the suggestion of flock adapter to use the flockNames added
                val flockAdapter = ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, flockNames)
                actvSelectFlock.setAdapter(flockAdapter)
            }
        }

        // go back to the previous activity or view
        back.setOnClickListener {
            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.popBackStackImmediate()
        }

        // more info on the Select Flock question
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
        // more info on the Number Of Crates Question
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
        // more info on the Number of Eggs Not in Crates Question
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

        // add flock
        ivAddFlock.setOnClickListener {
            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flEggsActivity,
                FlockAdditionForm()
            ).commit()
        }
        // add EggType
        ivAddEggType.setOnClickListener {
            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flEggsActivity,
                AddEggType()
            ).commit()
        }

        // insert egg inventory into the database
        save.setOnClickListener {
            insertDataToDatabase(
                tvDatePicker,
                tvEnterDate,
                tvSelectFlock,
                actvSelectFlock,
                tvNumberOfCrates,
                etNumberOfCrates,
                tvNumberOfEggsNotInCrates,
                etNumberOfEggsNotInCrates,
                tvSelectEggType,
                actvSelectEggType,
                etShortNotes,
                numberOfEggsPerCrate,
                dateToday,
                theFlockPurpose,
                flockNames
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
        tvDatePicker: TextView,
        tvEnterDate: TextView,
        tvSelectFlock: TextView,
        actvSelectFlock: AutoCompleteTextView,
        tvNumberOfCrates: TextView,
        etNumberOfCrates: EditText,
        tvNumberOfEggsNotInCrates: TextView,
        etNumberOfEggsNotInCrates: EditText,
        tvSelectEggType: TextView,
        actvSelectEggType: AutoCompleteTextView,
        etShortNotes: EditText,
        numberOfEggsPerCrate: Int,
        dateToday: String,
        theFlockPurpose: String,
        flockNames: MutableList<String>
    ) {
        val date = tvDatePicker.text.toString()
        val flock = actvSelectFlock.text.toString()
        val eggType = actvSelectEggType.text.toString()
        val numberOfCrates = etNumberOfCrates.text.toString()
        val numberOfEggsNotInCrates = etNumberOfEggsNotInCrates.text.toString()
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
            Toast.makeText(requireContext(), "You can only add $eggType on $dateToday or before", Toast.LENGTH_SHORT).show()
        } else if (checkIfEmpty(flock)) {
            tvEnterDate.setTextColor(resources.getColor(R.color.black))
            tvSelectFlock.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Select Customer", Toast.LENGTH_LONG).show()
        } else if (checkIfEmpty(eggType)) {
            tvSelectFlock.setTextColor(resources.getColor(R.color.black))
            tvSelectEggType.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Select Customer", Toast.LENGTH_LONG).show()
        } else if (checkIfEggsAreCollected(numberOfCrates,numberOfEggsNotInCrates)) {
            tvSelectEggType.setTextColor(resources.getColor(R.color.black))
            tvNumberOfCrates.setTextColor(resources.getColor(R.color.red))
            tvNumberOfEggsNotInCrates.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Enter the number Eggs Collected", Toast.LENGTH_LONG).show()
        } else {
            var flockDoesNotExists = true
            var flockIsNotALayer = true

            for (flocks in flockNames){
                if(flock == flocks){
                    flockDoesNotExists = false
                }
            }
            if (theFlockPurpose.contains("lay", true)){
                flockIsNotALayer = false
            }
            if (flockDoesNotExists) {
                tvSelectFlock.setTextColor(resources.getColor(R.color.red))
                Toast.makeText(requireContext(), "This $flock flock does not exist", Toast.LENGTH_LONG).show()
                Toast.makeText(requireContext(), "Please select a flock which is laying", Toast.LENGTH_LONG).show()
            }
            else if(flockIsNotALayer){
                tvSelectFlock.setTextColor(resources.getColor(R.color.black))
                Toast.makeText(requireContext(), "$flock is not a laying flock", Toast.LENGTH_LONG).show()
                Toast.makeText(requireContext(), "Select a flock which lays", Toast.LENGTH_LONG).show()
            }
            else{
            val eggInventoryAdditionDate = simpleDateFormat.parse(date)
            val remainingEggs = if (checkIfEmpty(numberOfEggsNotInCrates)) { 0 } else {numberOfEggsNotInCrates.toInt() }
            val eggQuantity = if (checkIfEmpty(numberOfCrates)) { remainingEggs } else (numberOfCrates.toInt() * numberOfEggsPerCrate) + remainingEggs

            val eggInventoryAddition = eggInventoryAdditionDate?.let {
                EggInventoryAddition(
                    0,
                    it, flock, eggType, eggQuantity, shortNotes
                )
            }
            if (eggInventoryAddition != null) {
                mEggInventoryAdditionViewModel.addEggInventoryAddition(eggInventoryAddition)
            }


            Toast.makeText(requireContext(), "Successfully Added", Toast.LENGTH_LONG).show()

            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flEggsActivity,
                EggsAdditionList()
            ).commit()}
        }
    }

    private fun checkIfEmpty(value: String): Boolean {
        return (TextUtils.isEmpty(value))
    }

    private fun checkIfEggsAreCollected(value: String, value1: String): Boolean {
        return (TextUtils.isEmpty(value) && TextUtils.isEmpty(value1))
    }
    private fun selectFlockNameTextWatcher() = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            mFlockInventoryAdditionViewModel.getFlockPurposeByFlockName(s.toString())
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }

}