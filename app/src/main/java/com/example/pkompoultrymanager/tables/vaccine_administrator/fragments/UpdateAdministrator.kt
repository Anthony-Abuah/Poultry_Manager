package com.example.pkompoultrymanager.tables.vaccine_administrator.fragments


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
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.pkompoultrymanager.R
import com.example.pkompoultrymanager.tables.vaccine_administrator.VaccineAdministratorViewModel

class UpdateAdministrator : Fragment() {
    private lateinit var mVaccineAdministratorViewModel: VaccineAdministratorViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_update_administrator, container, false)

        mVaccineAdministratorViewModel = ViewModelProvider(this)[VaccineAdministratorViewModel::class.java]

        // Value holder views
        val tvName= view.findViewById<TextView>(R.id.tvAdministratorName_UpdateAdministratorForm)
        val update= view.findViewById<Button>(R.id.btnUpdate_UpdateAdministratorForm)
        // Value holder views
        val etName= view.findViewById<EditText>(R.id.etAdministratorName_UpdateAdministratorForm)
        val etContact= view.findViewById<EditText>(R.id.etAdministratorContact_UpdateAdministratorForm)
        val etLocation= view.findViewById<EditText>(R.id.etAdministratorLocation_UpdateAdministratorForm)
        val etShortNotes= view.findViewById<EditText>(R.id.etShortNotes_UpdateAdministratorForm)

        // get the values from the communicator
        val id = arguments?.getString("id")
        val name = arguments?.getString("name")
        val contact = arguments?.getString("contact")
        val location = arguments?.getString("location")
        val shortNotes = arguments?.getString("shortNotes")

        // set the values to the views
        etName.setText(name)
        etContact.setText(contact)
        etLocation.setText(location)
        etShortNotes.setText(shortNotes)

        update.setOnClickListener {
            if (id != null) {
                updateDataInDatabase(tvName,etName,etContact,etLocation, etShortNotes, id.toInt())
            }
        }


        return view
    }
    private fun updateDataInDatabase(
        tvName: TextView,
        etName: EditText,
        etContact: EditText,
        etLocation: EditText,
        etShortNotes: EditText,
        id: Int
    ) {
        val name= etName.text.toString()
        val contact= etContact.text.toString()
        val location = etLocation.text.toString()
        val shortNotes= etShortNotes.text.toString()


        if (checkIfEmpty(name)) {
            tvName.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Add Administrator Name", Toast.LENGTH_LONG).show()
        } else {
            mVaccineAdministratorViewModel.updateVaccineAdministratorInfo(id,name,contact,location,shortNotes)

            Toast.makeText(requireContext(), "Successfully Updated", Toast.LENGTH_LONG).show()


            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flTablesActivity,
                AdministratorList()
            ).commit()
        }
    }
    private fun checkIfEmpty(value: String): Boolean {
        return (TextUtils.isEmpty(value))
    }

}