package com.example.pkompoultrymanager.tables.customer.fragments


import android.content.Intent
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
import com.example.pkompoultrymanager.inventory.eggs.addition.fragments.EggsAdditionForm
import com.example.pkompoultrymanager.tables.customer.Customer
import com.example.pkompoultrymanager.tables.customer.CustomerViewModel

class AddCustomer : Fragment() {
    private lateinit var mCustomerViewModel: CustomerViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_customer, container, false)

        mCustomerViewModel = ViewModelProvider(this)[CustomerViewModel::class.java]

        // Value holder views
        val tvName= view.findViewById<TextView>(R.id.tvCustomerName_AddCustomerForm)
        val save= view.findViewById<Button>(R.id.btnSave_AddCustomerForm)
        // Value holder views
        val etName= view.findViewById<EditText>(R.id.etCustomerName_AddCustomerForm)
        val etContact= view.findViewById<EditText>(R.id.etCustomerContact_AddCustomerForm)
        val etLocation= view.findViewById<EditText>(R.id.etCustomerLocation_AddCustomerForm)
        val etShortNotes= view.findViewById<EditText>(R.id.etShortNotes_AddCustomerForm)


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
            Toast.makeText(requireContext(), "Please Add Customer Name", Toast.LENGTH_LONG).show()
        } else {
            val customer = Customer(0, name, contact, location, shortNotes)
            mCustomerViewModel.addCustomer(customer)

            Toast.makeText(requireContext(), "Successfully Added", Toast.LENGTH_LONG).show()

            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.popBackStackImmediate()
        }
    }

    private fun checkIfEmpty(value: String): Boolean {
        return (TextUtils.isEmpty(value))
    }
}