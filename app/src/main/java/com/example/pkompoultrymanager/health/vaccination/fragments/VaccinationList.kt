package com.example.pkompoultrymanager.health.vaccination.fragments

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
import com.example.pkompoultrymanager.health.vaccination.Vaccination
import com.example.pkompoultrymanager.health.vaccination.VaccinationAdapter
import com.example.pkompoultrymanager.health.vaccination.VaccinationViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*

class VaccinationList : Fragment() {

    private lateinit var mVaccinationViewModel: VaccinationViewModel
    private lateinit var myFarmInfo: SharedPreferences
    private lateinit var communicator: HealthCommunicator


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_vaccination_list, container, false)

        myFarmInfo = context?.getSharedPreferences("MyFarmInfo", Context.MODE_PRIVATE)!!
        val currency = myFarmInfo.getString("currency", "GH₵")!!

        val vaccinationList = view.findViewById<RecyclerView>(R.id.rvVaccinationList)
        val adapter = VaccinationAdapter(currency)
        vaccinationList.adapter = adapter
        vaccinationList.layoutManager = LinearLayoutManager(requireContext())

        mVaccinationViewModel = ViewModelProvider(this)[VaccinationViewModel::class.java]
        mVaccinationViewModel.displayAllVaccinations.observe(viewLifecycleOwner) {
                vaccination -> adapter.setData(vaccination) }

        adapter.setOnItemClickListener(object : VaccinationAdapter.OnItemClickListener {
            val myFormat = "EEE, dd MMM, yyyy"
            val simpleDateFormat = SimpleDateFormat(myFormat, Locale.UK)
            override fun onDeleteVaccinationClickListener(vaccination: Vaccination, position: Int) {
                val popupMenu = PopupMenu(
                    requireContext(),
                    vaccinationList[position].findViewById(R.id.ivUpdate_Delete_Menu_MedLayout)
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
                                setTitle("Are you sure you want to permanently delete this vaccination record?")

                                setNegativeButton("No") { dialog, which ->
                                    vaccinationList.adapter = adapter
                                    vaccinationList.layoutManager =
                                        LinearLayoutManager(requireContext())
                                    Toast.makeText(
                                        requireContext(), "vaccination record not removed",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                setPositiveButton("Yes") { dialog, where ->
                                    mVaccinationViewModel.delete(vaccination)
                                    Toast.makeText(
                                        requireContext(), "vaccination record removed",
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
                            val id = vaccination.vaccinationId.toString()
                            val date = vaccination.vaccinationDate.let { it1 ->
                                simpleDateFormat.format(it1) }
                            val name = vaccination.vaccinationName
                            val administrator = vaccination.vaccinationAdministrator
                            val cost = vaccination.vaccinationCost.toString()
                            val flock = vaccination.vaccinatedFlock
                            val number = vaccination.numberOfVaccinatedBirds.toString()
                            val disease = vaccination.vaccinatedDisease
                            val shortNotes = vaccination.shortNotes
                            communicator = activity as HealthCommunicator
                            communicator.passVaccination(
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

            override fun onItemClickListener(vaccination: Vaccination) {
                val id = vaccination.vaccinationId.toString()
                val date = vaccination.vaccinationDate.let { it1 ->
                    simpleDateFormat.format(it1) }
                val name = vaccination.vaccinationName
                val administrator = vaccination.vaccinationAdministrator
                val cost = vaccination.vaccinationCost.toString()
                val flock = vaccination.vaccinatedFlock
                val number = vaccination.numberOfVaccinatedBirds.toString()
                val disease = vaccination.vaccinatedDisease
                val shortNotes = vaccination.shortNotes
                communicator = activity as HealthCommunicator
                communicator.passVaccination(
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
        
        val addVaccination = view.findViewById<FloatingActionButton>(R.id.fabAddVaccination)
        addVaccination.setOnClickListener {
            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flHealthActivity,
                VaccinationForm()
            ).commit()
        }


        return view
    }

}