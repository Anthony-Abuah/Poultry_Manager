package com.example.pkompoultrymanager.tables.egg_type.fragments


import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider

import com.example.pkompoultrymanager.R
import com.example.pkompoultrymanager.tables.egg_type.EggType
import com.example.pkompoultrymanager.tables.egg_type.EggTypeViewModel

class AddEggType : Fragment() {
    private lateinit var mEggTypeViewModel: EggTypeViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_egg_type, container, false)

        mEggTypeViewModel = ViewModelProvider(this)[EggTypeViewModel::class.java]

        val tvEggTypeName = view.findViewById<TextView>(R.id.tvEggTypeName_AddEggTypeForm)
        val etEggTypeName = view.findViewById<EditText>(R.id.etEggTypeName_AddEggTypeForm)
        val etDescription = view.findViewById<EditText>(R.id.etDescription_AddEggTypeForm)


        val save = view.findViewById<Button>(R.id.btnSave_AddEggTypeForm)
        save.setOnClickListener {
            insertDataToDatabase(tvEggTypeName, etEggTypeName,etDescription)
        }


        return view
    }
    private fun insertDataToDatabase(
        tvEggTypeName: TextView,
        etEggTypeName: EditText,
        etDescription: EditText
    ) {
        val eggTypeName = etEggTypeName.text.toString()
        val description = etDescription.text.toString()


        if (checkIfEmpty(eggTypeName)) {
            tvEggTypeName.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Add EggType Name", Toast.LENGTH_LONG).show()
        }  else {
            val eggType = EggType(0, eggTypeName, description)
            mEggTypeViewModel.addEggType(eggType)

            Toast.makeText(requireContext(), "Successfully Added", Toast.LENGTH_LONG).show()


            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.popBackStackImmediate()
        }
    }

    private fun checkIfEmpty(eggTypeName: String): Boolean {
        return (TextUtils.isEmpty(eggTypeName))
    }

}