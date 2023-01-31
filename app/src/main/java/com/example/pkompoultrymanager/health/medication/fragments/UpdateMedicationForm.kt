package com.example.pkompoultrymanager.health.medication.fragments

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
import com.example.pkompoultrymanager.health.medication.Medication
import com.example.pkompoultrymanager.health.medication.MedicationViewModel
import com.example.pkompoultrymanager.inventory.flock.addition.FlockInventoryAdditionViewModel
import com.example.pkompoultrymanager.tables.vaccine_administrator.VaccineAdministratorViewModel
import com.example.pkompoultrymanager.tables.vaccine_administrator.fragments.AddAdministrator
import java.text.SimpleDateFormat
import java.util.*

class UpdateMedicationForm : Fragment() {
    private lateinit var mMedicationViewModel: MedicationViewModel
    private lateinit var mVaccineAddAdministratorViewModel: VaccineAdministratorViewModel
    private lateinit var mFlockInventoryAdditionViewModelViewModel: FlockInventoryAdditionViewModel
    private lateinit var tvDatePicker: TextView

        @SuppressLint("ClickableViewAccessibility")
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            // Inflate the layout for this fragment
            val view = inflater.inflate(R.layout.fragment_update_medication_form, container, false)

            // initialize the viewModel
            mMedicationViewModel = ViewModelProvider(this)[MedicationViewModel::class.java]
            mVaccineAddAdministratorViewModel = ViewModelProvider(this)[VaccineAdministratorViewModel::class.java]
            mFlockInventoryAdditionViewModelViewModel = ViewModelProvider(this)[FlockInventoryAdditionViewModel::class.java]

            // Declaring the various Views
            //display views
            val tvMedicationName = view.findViewById<TextView>(R.id.tvMedicationName_UpdateMedicationForm)
            val tvMedicationCost = view.findViewById<TextView>(R.id.tvMedicationCost_UpdateMedicationForm)
            val tvEnterDate = view.findViewById<TextView>(R.id.tvEnterDate_UpdateMedicationForm)
            val tvSelectFlock = view.findViewById<TextView>(R.id.tvSelectFlock_UpdateMedicationForm)
            val tvSelectDisease = view.findViewById<TextView>(R.id.tvSelectDisease_UpdateMedicationForm)
            val tvNumberOfMedicatedBirds = view.findViewById<TextView>(R.id.tvNumberOfMedicatedBirds_UpdateMedicationForm)
            val tvSelectMedicationAdministrator = view.findViewById<TextView>(R.id.tvSelectMedicationAdministrator_UpdateMedicationForm)
            val addMedicationAdministrator = view.findViewById<ImageView>(R.id.ivAddMedicationAdministrator_UpdateMedicationForm)
            val save = view.findViewById<Button>(R.id.btnSave_UpdateMedicationForm)
            val back = view.findViewById<ImageButton>(R.id.ibBack_UpdateMedicationForm)

            // value holder views
            tvDatePicker = view.findViewById(R.id.tvDatePicker_UpdateMedicationForm)
            val etMedicationName = view.findViewById<EditText>(R.id.etMedicationName_UpdateMedicationForm)
            val etNumberOfMedicatedBirds = view.findViewById<EditText>(R.id.etNumberOfMedicatedBirds_UpdateMedicationForm)
            val etMedicationCost = view.findViewById<EditText>(R.id.etMedicationCost_UpdateMedicationForm)
            val actvSelectDisease = view.findViewById<AutoCompleteTextView>(R.id.actvSelectDisease_UpdateMedicationForm)
            val actvSelectMedicationAdministrator = view.findViewById<AutoCompleteTextView>(R.id.actvSelectMedicationAdministrator_UpdateMedicationForm)
            val actvSelectFlock = view.findViewById<AutoCompleteTextView>(R.id.actvSelectFlock_UpdateMedicationForm)
            val etShortNotes = view.findViewById<EditText>(R.id.etShortNotes_UpdateMedicationForm)


            // instantiate the calender and display/ select date
            val myCalendar = Calendar.getInstance()
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

            // Disease Suggestion
            val diseaseSuggestions = arrayOf("Gumboro", "Chicken pox", "Newcastle","BirdFlu")
            val selectDiseaseAdapter = ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, diseaseSuggestions)
            actvSelectDisease.setAdapter(selectDiseaseAdapter)

            // Medication Administrator Suggestion
            mVaccineAddAdministratorViewModel.displayAllAdministrators.observe(viewLifecycleOwner) {
                    administrator->administrator.let {
                val medicationAdministrators = mutableListOf<String>()
                for (administrators in it){
                    medicationAdministrators.add(administrators.AdministratorName)
                }
                val selectMedicationAdministratorAdapter = ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, medicationAdministrators)
                actvSelectMedicationAdministrator.setAdapter(selectMedicationAdministratorAdapter)

            }}

            // Flock Suggestion
            mFlockInventoryAdditionViewModelViewModel.displayAllFlockInventoryAdditions.observe(viewLifecycleOwner) {
                    flock->flock.let {
                val flockSuggestions = mutableListOf<String>()
                for (flocks in it) { flockSuggestions.add(flocks.FlockName) }
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

            // more info on the select medicationAdministrator question
            tvSelectMedicationAdministrator.setOnClickListener {
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

            // more info on the medication name question
            etMedicationName.setOnTouchListener(View.OnTouchListener { v, event ->
                val DRAWABLE_RIGHT = 2
                if (event.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= etMedicationName.right - etMedicationName.compoundDrawables[DRAWABLE_RIGHT].bounds.width()
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

            // more info on the number of medicated birds question
            etNumberOfMedicatedBirds.setOnTouchListener(View.OnTouchListener { v, event ->
                val DRAWABLE_RIGHT = 2
                if (event.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= etNumberOfMedicatedBirds.right - etNumberOfMedicatedBirds.compoundDrawables[DRAWABLE_RIGHT].bounds.width()
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

            // more info on the medicated cost question
            etMedicationCost.setOnTouchListener(View.OnTouchListener { v, event ->
                val DRAWABLE_RIGHT = 2
                if (event.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= etMedicationCost.right - etMedicationCost.compoundDrawables[DRAWABLE_RIGHT].bounds.width()
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

            // add medication administrator
            addMedicationAdministrator.setOnClickListener {
                val fragmentManager: FragmentManager = parentFragmentManager
                fragmentManager.beginTransaction().replace(
                    R.id.flHealthActivity,
                    AddAdministrator()
                ).commit()
            }

            // get the values from the communicator
            val theMedicationId = arguments?.getInt("medicationId")
            val theMedicationName = arguments?.getString("medicationName")
            val theMedicationDate = arguments?.getString("medicationDate")
            val theMedicationAdministrator = arguments?.getString("medicationAdministrator")
            val theMedicationCost = arguments?.getString("medicationCost")
            val theMedicatedFlock = arguments?.getString("medicatedFlock")
            val theNumberOfMedicatedBirds = arguments?.getString("numberOfMedicatedBirds")
            val theMedicatedDisease = arguments?.getString("medicatedDisease")
            val theShortNotes = arguments?.getString("shortNotes")


            // bind the views to the communicator values
            tvDatePicker.text = theMedicationDate
            etMedicationName.setText(theMedicationName)
            etMedicationCost.setText(theMedicationCost)
            etNumberOfMedicatedBirds.setText(theNumberOfMedicatedBirds)
            actvSelectDisease.setText(theMedicatedDisease)
            actvSelectFlock.setText(theMedicatedFlock)
            actvSelectMedicationAdministrator.setText(theMedicationAdministrator)
            etShortNotes.setText(theShortNotes)


            // save medication record into database
            save.setOnClickListener {
                if (theMedicationId != null) {
                    insertDataToDatabase(
                        theMedicationId.toInt(),
                        tvDatePicker,
                        tvEnterDate,
                        tvSelectFlock,
                        actvSelectFlock,
                        tvSelectDisease,
                        actvSelectDisease,
                        tvMedicationName,
                        etMedicationName,
                        tvNumberOfMedicatedBirds,
                        etNumberOfMedicatedBirds,
                        tvMedicationCost,
                        etMedicationCost,
                        actvSelectMedicationAdministrator,
                        etShortNotes
                    )
                }
            }


            return view
        }
    private fun updateDate(myCalendar: Calendar) {
        val myFormat =  "EEE, dd MMM, yyyy"
        val simpleDateFormat = SimpleDateFormat(myFormat, Locale.UK)
        tvDatePicker.text = simpleDateFormat.format(myCalendar.time)
    }
    private fun insertDataToDatabase(
        medicationId: Int,
        tvDatePicker: TextView,
        tvEnterDate: TextView,
        tvSelectFlock: TextView,
        actvSelectFlock: AutoCompleteTextView,
        tvSelectDisease: TextView,
        actvSelectDisease: AutoCompleteTextView,
        tvMedicationName: TextView,
        etMedicationName: EditText,
        tvNumberOfMedicatedBirds: TextView,
        etNumberOfMedicatedBirds: EditText,
        tvMedicationCost: TextView,
        etMedicationCost: EditText,
        actvSelectMedicationAdministrator: AutoCompleteTextView,
        etShortNotes: EditText
    ) {
        val date = tvDatePicker.text.toString()
        val flock = actvSelectFlock.text.toString()
        val disease = actvSelectDisease.text.toString()
        val medicationName = etMedicationName.text.toString()
        val numberOfBirds = etNumberOfMedicatedBirds.text.toString()
        val costOfMedication = etMedicationCost.text.toString()
        val medicationAdministrator = actvSelectMedicationAdministrator.text.toString()
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
        else if (checkIfEmpty(medicationName)){
            tvSelectDisease.setTextColor(resources.getColor(R.color.black))
            tvMedicationName.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Enter Medication Name", Toast.LENGTH_LONG).show()
        }
        else if (checkIfEmpty(numberOfBirds)){
            tvMedicationName.setTextColor(resources.getColor(R.color.black))
            tvNumberOfMedicatedBirds.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Enter Number Of Birds", Toast.LENGTH_LONG).show()
        }
        else if (checkIfEmpty(costOfMedication)){
            tvNumberOfMedicatedBirds.setTextColor(resources.getColor(R.color.black))
            tvMedicationCost.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Enter Cost Of Medication", Toast.LENGTH_LONG).show()
        }
        else {
            val myFormat =  "EEE, dd MMM, yyyy"
            val simpleDateFormat = SimpleDateFormat(myFormat, Locale.UK)
            val newDate = simpleDateFormat.parse(date)
            val medicationDate = newDate?.time?.let { java.sql.Date(it)}

            if (medicationDate != null) {
                mMedicationViewModel.updateMedication(medicationId,medicationName,medicationDate,disease,costOfMedication.toDouble(),flock,numberOfBirds.toInt(),medicationAdministrator,shortNotes)
            }
            Toast.makeText(requireContext(), "Successfully Updated", Toast.LENGTH_LONG).show()

            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flHealthActivity,
                MedicationList()
            ).commit()
        }
    }
    private fun checkIfEmpty(value: String): Boolean {
        return (TextUtils.isEmpty(value))
    }

}