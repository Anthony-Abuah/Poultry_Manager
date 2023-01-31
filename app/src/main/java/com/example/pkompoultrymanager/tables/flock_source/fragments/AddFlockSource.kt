package com.example.pkompoultrymanager.tables.flock_source.fragments


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
import com.example.pkompoultrymanager.tables.flock_source.FlockSource
import com.example.pkompoultrymanager.tables.flock_source.FlockSourceViewModel

class AddFlockSource : Fragment() {
    private lateinit var mFlockSourceViewModel: FlockSourceViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_flock_source, container, false)

        mFlockSourceViewModel = ViewModelProvider(this)[FlockSourceViewModel::class.java]

        // Value holder views
        val tvName= view.findViewById<TextView>(R.id.tvFlockSourceName_AddFlockSource)
        val save = view.findViewById<Button>(R.id.btnSave_AddFlockSource)

        // Value holder views
        val etName= view.findViewById<EditText>(R.id.etFlockSourceName_AddFlockSource)
        val etContact= view.findViewById<EditText>(R.id.etFlockSourceContact_AddFlockSource)
        val etLocation= view.findViewById<EditText>(R.id.etFlockSourceLocation_AddFlockSource)
        val etShortNotes= view.findViewById<EditText>(R.id.etShortNotes_AddFlockSource)



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
            Toast.makeText(requireContext(), "Please Add Flock Source Name", Toast.LENGTH_LONG).show()
        } else  {
            val flockSource = FlockSource(0, name, contact, location, shortNotes)
            mFlockSourceViewModel.addFlockSource(flockSource)

            Toast.makeText(requireContext(), "Successfully Added", Toast.LENGTH_LONG).show()


            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.popBackStackImmediate()
        }
    }

    private fun checkIfEmpty(value: String): Boolean {
        return (TextUtils.isEmpty(value))
    }
}