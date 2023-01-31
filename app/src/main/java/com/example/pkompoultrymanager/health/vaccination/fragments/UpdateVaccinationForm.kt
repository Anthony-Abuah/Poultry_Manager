package com.example.pkompoultrymanager.health.vaccination.fragments

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
import com.example.pkompoultrymanager.health.vaccination.Vaccination
import com.example.pkompoultrymanager.health.vaccination.VaccinationViewModel
import com.example.pkompoultrymanager.inventory.flock.addition.FlockInventoryAdditionViewModel
import com.example.pkompoultrymanager.tables.vaccine_administrator.VaccineAdministratorViewModel
import com.example.pkompoultrymanager.tables.vaccine_administrator.fragments.AddAdministrator
import java.text.SimpleDateFormat
import java.util.*

class UpdateVaccinationForm : Fragment() {
    private lateinit var mVaccinationViewModel: VaccinationViewModel
    private lateinit var mVaccineAddAdministratorViewModel: VaccineAdministratorViewModel
    private lateinit var mFlockInventoryAdditionViewModelViewModel: FlockInventoryAdditionViewModel
    private lateinit var tvDatePicker: TextView

    @SuppressLint("ClickableViewAccessibility")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_update_vaccination_form, container, false)

        mVaccinationViewModel = ViewModelProvider(this)[VaccinationViewModel::class.java]
        mVaccineAddAdministratorViewModel = ViewModelProvider(this)[VaccineAdministratorViewModel::class.java]
        mFlockInventoryAdditionViewModelViewModel = ViewModelProvider(this)[FlockInventoryAdditionViewModel::class.java]


        // Declaring the various Views
        //display views
        val tvSelectFlock = view.findViewById<TextView>(R.id.tvSelectFlock_UpdateVaccinationForm)
        val tvEnterDate = view.findViewById<TextView>(R.id.tvEnterDate_UpdateVaccinationForm)
        val tvSelectDisease = view.findViewById<TextView>(R.id.tvSelectDisease_UpdateVaccinationForm)
        val tvNumberOfVaccinatedBirds = view.findViewById<TextView>(R.id.tvNumberOfVaccinatedBirds_UpdateVaccinationForm)
        val tvVaccinationName = view.findViewById<TextView>(R.id.tvVaccinationName_UpdateVaccinationForm)
        val tvVaccinationCost = view.findViewById<TextView>(R.id.tvVaccinationCost_UpdateVaccinationForm)
        val tvSelectVaccinationAdministrator = view.findViewById<TextView>(R.id.tvSelectVaccinationAdministrator_UpdateVaccinationForm)
        val ivAddVaccinationAdministrator = view.findViewById<ImageView>(R.id.ivAddVaccinationAdministrator_UpdateVaccinationForm)
        val update = view.findViewById<Button>(R.id.btnUpdate_UpdateVaccinationForm)

        // value holder views
        tvDatePicker = view.findViewById(R.id.tvDatePicker_UpdateVaccinationForm)
        val etVaccinationName = view.findViewById<EditText>(R.id.etVaccinationName_UpdateVaccinationForm)
        val etNumberOfVaccinatedBirds = view.findViewById<EditText>(R.id.etNumberOfVaccinatedBirds_UpdateVaccinationForm)
        val etVaccinationCost = view.findViewById<EditText>(R.id.etVaccinationCost_UpdateVaccinationForm)
        val etShortNotes = view.findViewById<EditText>(R.id.etShortNotes_UpdateVaccinationForm)
        val actvSelectDisease = view.findViewById<AutoCompleteTextView>(R.id.actvSelectDisease_UpdateVaccinationForm)
        val actvSelectVaccinationAdministrator = view.findViewById<AutoCompleteTextView>(R.id.actvSelectVaccinationAdministrator_UpdateVaccinationForm)
        val actvSelectFlock = view.findViewById<AutoCompleteTextView>(R.id.actvSelectFlock_UpdateVaccinationForm)


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


        // get the values from the communicator
        val theVaccinationId = arguments?.getInt("vaccinationId")
        val theVaccinationName = arguments?.getString("vaccinationName")
        val theVaccinationDate = arguments?.getString("vaccinationDate")
        val theVaccinationAdministrator = arguments?.getString("vaccinationAdministrator")
        val theVaccinationCost = arguments?.getString("vaccinationCost")
        val theVaccinatedFlock = arguments?.getString("vaccinatedFlock")
        val theNumberOfVaccinatedBirds = arguments?.getString("numberOfVaccinatedBirds")
        val theVaccinatedDisease = arguments?.getString("vaccinatedDisease")
        val theShortNotes = arguments?.getString("shortNotes")


        // bind the views to the communicator values
        tvDatePicker.text = theVaccinationDate
        etVaccinationName.setText(theVaccinationName)
        etVaccinationCost.setText(theVaccinationCost)
        etNumberOfVaccinatedBirds.setText(theNumberOfVaccinatedBirds)
        actvSelectDisease.setText(theVaccinatedDisease)
        actvSelectFlock.setText(theVaccinatedFlock)
        actvSelectVaccinationAdministrator.setText(theVaccinationAdministrator)
        etShortNotes.setText(theShortNotes)


        // save vaccination info into database
        update.setOnClickListener {
            if (theVaccinationId != null) {
                insertDataToDatabase(
                    theVaccinationId.toInt(),
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
        }

        return view
    }
    private fun updateDate(myCalendar: Calendar) {
        val myFormat =  "EEE, dd MMM, yyyy"
        val simpleDateFormat = SimpleDateFormat(myFormat, Locale.UK)
        tvDatePicker.text = simpleDateFormat.format(myCalendar.time)
    }
    private fun insertDataToDatabase(
        vaccinationId: Int,
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
            val newDate = simpleDateFormat.parse(date)
            val vaccinationDate = newDate?.time?.let { java.sql.Date(it)}

            if (vaccinationDate != null) {
                mVaccinationViewModel.updateVaccination(vaccinationId,vaccinationName,vaccinationDate,disease,costOfVaccination.toDouble(),flock,numberOfBirds.toInt(),vaccinationAdministrator,shortNotes)
            }
            Toast.makeText(requireContext(), "Successfully Updated", Toast.LENGTH_LONG).show()


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