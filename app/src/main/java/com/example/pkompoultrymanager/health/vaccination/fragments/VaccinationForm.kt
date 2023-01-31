package com.example.pkompoultrymanager.health.vaccination.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
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
import com.example.pkompoultrymanager.health.vaccination.Vaccination
import com.example.pkompoultrymanager.health.vaccination.VaccinationViewModel
import com.example.pkompoultrymanager.inventory.flock.addition.FlockInventoryAdditionViewModel
import com.example.pkompoultrymanager.tables.vaccine_administrator.VaccineAdministratorViewModel
import com.example.pkompoultrymanager.tables.vaccine_administrator.fragments.AddAdministrator
import java.text.SimpleDateFormat
import java.util.*

class VaccinationForm : Fragment() {
    private lateinit var mVaccinationViewModel: VaccinationViewModel
    private lateinit var mVaccineAddAdministratorViewModel: VaccineAdministratorViewModel
    private lateinit var mFlockInventoryAdditionViewModelViewModel: FlockInventoryAdditionViewModel
    private lateinit var tvDatePicker: TextView
    private lateinit var myFarmInfo: SharedPreferences
    @SuppressLint("ClickableViewAccessibility")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_vaccination_form, container, false)

        mVaccinationViewModel = ViewModelProvider(this)[VaccinationViewModel::class.java]
        mVaccineAddAdministratorViewModel = ViewModelProvider(this)[VaccineAdministratorViewModel::class.java]
        mFlockInventoryAdditionViewModelViewModel = ViewModelProvider(this)[FlockInventoryAdditionViewModel::class.java]

        myFarmInfo = context?.getSharedPreferences("MyFarmInfo", Context.MODE_PRIVATE)!!



        // Declaring the various Views
        //display views
        val tvSelectFlock = view.findViewById<TextView>(R.id.tvSelectFlock_VaccinationForm)
        val tvEnterDate = view.findViewById<TextView>(R.id.tvEnterDate_VaccinationForm)
        val tvSelectDisease = view.findViewById<TextView>(R.id.tvSelectDisease_VaccinationForm)
        val tvNumberOfVaccinatedBirds = view.findViewById<TextView>(R.id.tvNumberOfVaccinatedBirds_VaccinationForm)
        val tvVaccinationName = view.findViewById<TextView>(R.id.tvVaccinationName_VaccinationForm)
        val tvVaccinationCost = view.findViewById<TextView>(R.id.tvVaccinationCost_VaccinationForm)
        val tvSelectVaccinationAdministrator = view.findViewById<TextView>(R.id.tvSelectVaccinationAdministrator_VaccinationForm)
        val ivAddVaccinationAdministrator = view.findViewById<ImageView>(R.id.ivAddVaccinationAdministrator_VaccinationForm)
        val save = view.findViewById<Button>(R.id.btnSave_VaccinationForm)

        // value holder views
        tvDatePicker = view.findViewById(R.id.tvDatePicker_VaccinationForm)
        val etVaccinationName = view.findViewById<EditText>(R.id.etVaccinationName_VaccinationForm)
        val etNumberOfVaccinatedBirds = view.findViewById<EditText>(R.id.etNumberOfVaccinatedfBirds_VaccinationForm)
        val etVaccinationCost = view.findViewById<EditText>(R.id.etVaccinationCost_VaccinationForm)
        val etShortNotes = view.findViewById<EditText>(R.id.etShortNotes_VaccinationForm)
        val actvSelectDisease = view.findViewById<AutoCompleteTextView>(R.id.actvSelectDisease_VaccinationForm)
        val actvSelectVaccinationAdministrator = view.findViewById<AutoCompleteTextView>(R.id.actvSelectVaccinationAdministrator_VaccinationForm)
        val actvSelectFlock = view.findViewById<AutoCompleteTextView>(R.id.actvSelectFlock_VaccinationForm)


        // Instantiate the calendar
        val myCalendar = Calendar.getInstance()

        // initialize the date picker and select date
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

        // Disease Selection
        val diseaseSuggestions = arrayOf("Gumboro", "Chicken pox", "Newcastle","BirdFlu")
        val selectDiseaseAdapter = ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, diseaseSuggestions)
        actvSelectDisease.setAdapter(selectDiseaseAdapter)

        // Vaccine Administrator Suggestion
        mVaccineAddAdministratorViewModel.displayAllAdministrators.observe(viewLifecycleOwner) {
                administrator->administrator.let {
            val vaccineAdministrators = mutableListOf<String>()
            for (administrators in it){ vaccineAdministrators.add(administrators.AdministratorName) }
            val selectVaccineAdministratorAdapter = ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, vaccineAdministrators)
            actvSelectVaccinationAdministrator.setAdapter(selectVaccineAdministratorAdapter)
        }}

        // Flock Suggestion
        mFlockInventoryAdditionViewModelViewModel.displayAllFlockInventoryAdditions.observe(viewLifecycleOwner) {
                administrator->administrator.let {
            val flockSuggestions = mutableListOf<String>()
            for (flock in it){ flockSuggestions.add(flock.FlockName) }
            val selectFlockAdapter = ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, flockSuggestions)
            actvSelectFlock.setAdapter(selectFlockAdapter)
        }}

        // more info on the select flock question
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

        // more info on the select disease question
        tvSelectDisease.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.delete_prompt_layout, null)
            with(builder) {
                setTitle(R.string.SelectDisease_info)
                setPositiveButton("Ok") { dialog, where -> }
                setView(dialogLayout)
                show()
            }
        }

        // more info on the select vaccination administrator question
        tvSelectVaccinationAdministrator.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.delete_prompt_layout, null)
            with(builder) {
                setTitle(R.string.SelectAdministrator_info)
                setPositiveButton("Ok") { dialog, where -> }
                setView(dialogLayout)
                show()
            }
        }

        // more info on the vaccination name question
        etVaccinationName.setOnTouchListener(View.OnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= etVaccinationName.right - etVaccinationName.compoundDrawables[DRAWABLE_RIGHT].bounds.width()
                ) {
                    val builder = AlertDialog.Builder(context)
                    val inflater = layoutInflater
                    val dialogLayout = inflater.inflate(R.layout.delete_prompt_layout, null)
                    with(builder) {
                        setTitle(R.string.SelectMedicineName_info)
                        setPositiveButton("Ok") { dialog, where -> }
                        setView(dialogLayout)
                        show()
                    }
                }
            }
            false
        })

        // more info on the number of vaccinated birds question
        etNumberOfVaccinatedBirds.setOnTouchListener(View.OnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= etNumberOfVaccinatedBirds.right - etNumberOfVaccinatedBirds.compoundDrawables[DRAWABLE_RIGHT].bounds.width()
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

        // more info on the vaccination cost question
        etVaccinationCost.setOnTouchListener(View.OnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= etVaccinationCost.right - etVaccinationCost.compoundDrawables[DRAWABLE_RIGHT].bounds.width()
                ) {
                    val builder = AlertDialog.Builder(context)
                    val inflater = layoutInflater
                    val dialogLayout = inflater.inflate(R.layout.delete_prompt_layout, null)
                    with(builder) {
                        setTitle(R.string.MedicationCost_info)
                        setPositiveButton("Ok") { dialog, where -> }
                        setView(dialogLayout)
                        show()
                    }
                }
            }
            false
        })

        // add vaccination administrator
        ivAddVaccinationAdministrator.setOnClickListener {
            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flHealthActivity,
                AddAdministrator()
            ).commit()
        }

        // save vaccination info into database
        save.setOnClickListener {
            insertDataToDatabase(
                tvDatePicker,
                tvEnterDate,
                tvSelectFlock,
                actvSelectFlock,
                tvSelectDisease,
                actvSelectDisease,
                tvVaccinationName,
                etVaccinationName,
                tvNumberOfVaccinatedBirds,
                etNumberOfVaccinatedBirds,
                tvVaccinationCost,
                etVaccinationCost,
                actvSelectVaccinationAdministrator,
                etShortNotes
            )
        }

        return view
    }
    private fun updateDate(myCalendar: Calendar) {
        val myFormat =  "EEE, dd MMM, yyyy"
        val simpleDateFormat = SimpleDateFormat(myFormat, Locale.UK)
        tvDatePicker.text = simpleDateFormat.format(myCalendar.time)
    }
    private fun insertDataToDatabase(
        tvDatePicker: TextView,
        tvEnterDate: TextView,
        tvSelectFlock: TextView,
        actvSelectFlock: AutoCompleteTextView,
        tvSelectDisease: TextView,
        actvSelectDisease: AutoCompleteTextView,
        tvVaccinationName: TextView,
        etVaccinationName: EditText,
        tvNumberOfVaccinatedBirds: TextView,
        etNumberOfVaccinatedBirds: EditText,
        tvVaccinationCost: TextView,
        etVaccinationCost: EditText,
        actvSelectVaccinationAdministrator: AutoCompleteTextView,
        etShortNotes: EditText
    ) {
        val date = tvDatePicker.text.toString()
        val flock = actvSelectFlock.text.toString()
        val disease = actvSelectDisease.text.toString()
        val vaccinationName = etVaccinationName.text.toString()
        val numberOfBirds = etNumberOfVaccinatedBirds.text.toString()
        val costOfVaccination = etVaccinationCost.text.toString()
        val vaccinationAdministrator = actvSelectVaccinationAdministrator.text.toString()
        val shortNotes = etShortNotes.text.toString()

        if (checkIfEmpty(date)) {
            tvEnterDate.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Enter Date", Toast.LENGTH_LONG).show()}
        else if (checkIfEmpty(flock)){
            tvEnterDate.setTextColor(resources.getColor(R.color.black))
            tvSelectFlock.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Select Flock", Toast.LENGTH_LONG).show()
        }
        else if (checkIfEmpty(disease)){
            tvSelectFlock.setTextColor(resources.getColor(R.color.black))
            tvSelectDisease.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Select Disease", Toast.LENGTH_LONG).show()
        }
        else if (checkIfEmpty(vaccinationName)){
            tvSelectDisease.setTextColor(resources.getColor(R.color.black))
            tvVaccinationName.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Enter Vaccination Name", Toast.LENGTH_LONG).show()
        }
        else if (checkIfEmpty(numberOfBirds)){
            tvVaccinationName.setTextColor(resources.getColor(R.color.black))
            tvNumberOfVaccinatedBirds.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Enter Number Of Birds", Toast.LENGTH_LONG).show()
        }
        else if (checkIfEmpty(costOfVaccination)){
            tvNumberOfVaccinatedBirds.setTextColor(resources.getColor(R.color.black))
            tvVaccinationCost.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Enter Cost Of Vaccination", Toast.LENGTH_LONG).show()
        }else {
            val myFormat =  "EEE, dd MMM, yyyy"
            val simpleDateFormat = SimpleDateFormat(myFormat, Locale.UK)
            val vaccinationDate = simpleDateFormat.parse(date)

            val vaccination = vaccinationDate?.let {
                Vaccination(0,vaccinationName,it,vaccinationAdministrator,costOfVaccination.toDouble(),flock,numberOfBirds.toInt(),disease,shortNotes )
            }
            // add vaccination record to the database via a viewModel
            if (vaccination != null) {
                mVaccinationViewModel.addVaccination(vaccination)
            }

            Toast.makeText(requireContext(), "Successfully Added", Toast.LENGTH_LONG).show()

            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flHealthActivity,
                VaccinationList()
            ).commit()
        }
    }
    private fun checkIfEmpty(value: String): Boolean {
        return (TextUtils.isEmpty(value))
    }

}