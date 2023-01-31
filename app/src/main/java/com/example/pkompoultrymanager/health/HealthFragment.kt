package com.example.pkompoultrymanager.health

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import com.example.pkompoultrymanager.R
import com.example.pkompoultrymanager.health.medication.fragments.MedicationList
import com.example.pkompoultrymanager.health.vaccination.fragments.VaccinationList

class HealthFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_health, container, false)


        val clVaccination = view.findViewById<ConstraintLayout>(R.id.clVaccination_HealthFragment)
        val clMedication = view.findViewById<ConstraintLayout>(R.id.clMedication_HealthFragment)
        clVaccination.setOnClickListener {
            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flHealthActivity,
                VaccinationList()
            ).addToBackStack(null).commit()
        }
        clMedication.setOnClickListener {
            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flHealthActivity,
                MedicationList()
            ).addToBackStack(null).commit()
        }


        return view
    }

}