package com.example.pkompoultrymanager.tables.lender_investor.fragments


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
import com.example.pkompoultrymanager.tables.lender_investor.LenderInvestor
import com.example.pkompoultrymanager.tables.lender_investor.LenderInvestorViewModel

class AddLenderInvestor : Fragment() {
    private lateinit var mLenderInvestorViewModel: LenderInvestorViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_lender_investor, container, false)

        mLenderInvestorViewModel = ViewModelProvider(this)[LenderInvestorViewModel::class.java]

        // Value holder views
        val tvName= view.findViewById<TextView>(R.id.tvLenderInvestorName_AddLenderInvestor)
        val save= view.findViewById<Button>(R.id.btnSave_AddLenderInvestor)
        // Value holder views
        val etName= view.findViewById<EditText>(R.id.etLenderInvestorName_AddLenderInvestor)
        val etContact= view.findViewById<EditText>(R.id.etLenderInvestorContact_AddLenderInvestor)
        val etLocation= view.findViewById<EditText>(R.id.etLenderInvestorLocation_AddLenderInvestor)
        val etShortNotes= view.findViewById<EditText>(R.id.etShortNotes_AddLenderInvestor)


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
            Toast.makeText(requireContext(), "Please Add Lender/Investor Name", Toast.LENGTH_LONG).show()
        } else {
            val lenderInvestor = LenderInvestor(0, name, contact, location, shortNotes)
            mLenderInvestorViewModel.addLenderInvestor(lenderInvestor)

            Toast.makeText(requireContext(), "Successfully Added", Toast.LENGTH_LONG).show()


            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.popBackStackImmediate()
        }
    }

    private fun checkIfEmpty(value: String): Boolean {
        return (TextUtils.isEmpty(value))
    }
}