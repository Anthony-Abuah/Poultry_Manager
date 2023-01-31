package com.example.pkompoultrymanager.tables.vaccine_administrator.fragments


import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider

import com.example.pkompoultrymanager.R
import com.example.pkompoultrymanager.tables.customer.VaccineAdministrator
import com.example.pkompoultrymanager.tables.vaccine_administrator.VaccineAdministratorViewModel

class AddAdministrator : Fragment() {
    private lateinit var mVaccineAdministratorViewModel: VaccineAdministratorViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_administrator, container, false)

        mVaccineAdministratorViewModel = ViewModelProvider(this)[VaccineAdministratorViewModel::class.java]

        // Value holder views
        val tvName= view.findViewById<TextView>(R.id.tvAdministratorName_AddAdministratorForm)
        val save = view.findViewById<Button>(R.id.btnSave_AddAdministratorForm)
        // Value holder views
        val etName= view.findViewById<EditText>(R.id.etAdministratorName_AddAdministratorForm)
        val etContact= view.findViewById<EditText>(R.id.etAdministratorContact_AddAdministratorForm)
        val etLocation= view.findViewById<EditText>(R.id.etAdministratorLocation_AddAdministratorForm)
        val etShortNotes= view.findViewById<EditText>(R.id.etShortNotes_AddAdministratorForm)


        save.setOnClickListener {
            insertDataToDatabase(tvName, etName, etContact, etLocation, etShortNotes)
        }


        save.setOnClickListener {
            insertDataToDatabase(tvName, etName, etContact, etLocation, etShortNotes)
        }


        return view
    }
    private fun insertDataToDatabase(
        tvName: TextView,
        etName: EditText,
        etContact: EditText,
        etLocation: EditText,
        etShortNotes: EditText
    ) {
        val name= etName.text.toString()
        val contact= etContact.text.toString()
        val location = etLocation.text.toString()
        val shortNotes= etShortNotes.text.toString()

        if (checkIfEmpty(name)) {
            tvName.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Add Administrator Name", Toast.LENGTH_LONG).show()
        } else {
            val vaccineAdministrator = VaccineAdministrator(0, name, contact, location, shortNotes)
            mVaccineAdministratorViewModel.addVaccineAdministrator(vaccineAdministrator)

            Toast.makeText(requireContext(), "Successfully Added", Toast.LENGTH_LONG).show()


            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.popBackStackImmediate()
        }
    }

    private fun checkIfEmpty(vaccineAdministratorName: String): Boolean {
        return (TextUtils.isEmpty(vaccineAdministratorName))
    }
}