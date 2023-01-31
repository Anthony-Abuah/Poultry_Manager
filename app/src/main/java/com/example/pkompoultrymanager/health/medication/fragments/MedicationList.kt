package com.example.pkompoultrymanager.health.medication.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pkompoultrymanager.R
import com.example.pkompoultrymanager.health.HealthCommunicator
import com.example.pkompoultrymanager.health.medication.Medication
import com.example.pkompoultrymanager.health.medication.MedicationAdapter
import com.example.pkompoultrymanager.health.medication.MedicationViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*

class MedicationList : Fragment() {

    private lateinit var mMedicationViewModel: MedicationViewModel
    private lateinit var myFarmInfo: SharedPreferences
    private lateinit var communicator: HealthCommunicator


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_medication_list, container, false)

        myFarmInfo = context?.getSharedPreferences("MyFarmInfo", Context.MODE_PRIVATE)!!
        val currency = myFarmInfo.getString("currency", "GH₵")!!

        val medicationList = view.findViewById<RecyclerView>(R.id.rvMedicationList)
        val adapter = MedicationAdapter(currency)
        medicationList.adapter = adapter
        medicationList.layoutManager = LinearLayoutManager(requireContext())

        mMedicationViewModel = ViewModelProvider(this)[MedicationViewModel::class.java]
        mMedicationViewModel.displayAllMedications.observe(viewLifecycleOwner) {
                medication -> adapter.setData(medication) }

        adapter.setOnItemClickListener(object : MedicationAdapter.OnItemClickListener {
            val myFormat = "EEE, dd MMM, yyyy"
            val simpleDateFormat = SimpleDateFormat(myFormat, Locale.UK)
            override fun onDeleteMedicationClickListener(medication: Medication, position: Int) {
                val popupMenu = PopupMenu(
                    requireContext(),
                    medicationList[position].findViewById(R.id.ivUpdate_Delete_Menu_MedLayout)
                )
                popupMenu.inflate(R.menu.update_delete_menu)
                popupMenu.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.miDelete -> {
                            Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show()
                            val builder = AlertDialog.Builder(context, Gravity.END)
                            val inflater = layoutInflater
                            val dialogLayout = inflater.inflate(R.layout.delete_prompt_layout, null)
                            with(builder) {
                                setTitle("Are you sure you want to permanently delete this medication record?")

                                setNegativeButton("No") { dialog, which ->
                                    medicationList.adapter = adapter
                                    medicationList.layoutManager =
                                        LinearLayoutManager(requireContext())
                                    Toast.makeText(
                                        requireContext(), "medication record not removed",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                setPositiveButton("Yes") { dialog, where ->
                                    mMedicationViewModel.delete(medication)
                                    Toast.makeText(
                                        requireContext(), "medication record removed",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                setView(dialogLayout)
                                show()
                            }

                            true
                        }
                        R.id.miUpdate -> {
                            Toast.makeText(context, "Update", Toast.LENGTH_LONG).show()
                            val id = medication.medicationId.toString()
                            val date = medication.medicationDate.let { it1 ->
                                simpleDateFormat.format(it1) }
                            val name = medication.medicationName
                            val administrator = medication.medicationAdministrator
                            val cost = medication.medicationCost.toString()
                            val flock = medication.medicatedFlock
                            val number = medication.numberOfMedicatedBirds.toString()
                            val disease = medication.medicatedDisease
                            val shortNotes = medication.shortNotes
                            communicator = activity as HealthCommunicator
                            communicator.passMedication(
                                id,
                                name,
                                date,
                                administrator,
                                cost,
                                flock,
                                number,
                                disease,
                                shortNotes
                            )
                            true
                        }
                        else -> true
                    }
                }
                popupMenu.show()
                true
            }

            override fun onItemClickListener(medication: Medication) {
                val id = medication.medicationId.toString()
                val date = medication.medicationDate.let { it1 ->
                    simpleDateFormat.format(it1) }
                val name = medication.medicationName
                val administrator = medication.medicationAdministrator
                val cost = medication.medicationCost.toString()
                val flock = medication.medicatedFlock
                val number = medication.numberOfMedicatedBirds.toString()
                val disease = medication.medicatedDisease
                val shortNotes = medication.shortNotes
                communicator = activity as HealthCommunicator
                communicator.passMedication(
                    id,
                    name,
                    date,
                    administrator,
                    cost,
                    flock,
                    number,
                    disease,
                    shortNotes
                )
            }
        })
        
        val addMedication = view.findViewById<FloatingActionButton>(R.id.fabAddMedication)
        addMedication.setOnClickListener {
            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flHealthActivity,
                MedicationForm()
            ).commit()
        }

        return view
    }

}