package com.example.pkompoultrymanager.tables.egg_type.fragments


import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider

import com.example.pkompoultrymanager.R
import com.example.pkompoultrymanager.tables.egg_type.EggTypeViewModel

class UpdateEggType : Fragment() {

    private lateinit var mEggTypeViewModel: EggTypeViewModel

    var feedId: String? =""
    var feedName: String? =""
    var feedType: String? =""
    var feedBrand: String? =""
    var feedSource: String? =""
    var shortNotes: String? =""
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_update_egg_type, container, false)

        mEggTypeViewModel = ViewModelProvider(this)[EggTypeViewModel::class.java]

        val tvEggTypeName = view.findViewById<TextView>(R.id.tvEggTypeName_UpdateEggTypeForm)

        val etEggTypeName = view.findViewById<EditText>(R.id.etEggTypeName_UpdateEggTypeForm)
        val etDescription = view.findViewById<EditText>(R.id.etDescription_UpdateEggTypeForm)

        val eggTypeId = arguments?.getString("id")
        val eggTypeName = arguments?.getString("eggTypeName")
        val description = arguments?.getString("description")

        etEggTypeName.setText(eggTypeName)
        etDescription.setText(description)

        val update = view.findViewById<Button>(R.id.btnUpdate_UpdateEggTypeForm)
        update.setOnClickListener {
            if (eggTypeId != null) {
                insertDataToDatabase(tvEggTypeName, etEggTypeName,etDescription, eggTypeId.toInt())
            }
        }


        return view
    }

    private fun insertDataToDatabase(
        tvEggTypeName: TextView,
        etEggTypeName: EditText,
        etDescription: EditText,
        eggTypeId: Int
    ) {
        val eggTypeName = etEggTypeName.text.toString()
        val description = etDescription.text.toString()


        if (checkIfEmpty(eggTypeName)) {
            tvEggTypeName.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Add EggType Name", Toast.LENGTH_LONG).show()
        }  else {
            mEggTypeViewModel.updateEggType(eggTypeId, eggTypeName,description)

            Toast.makeText(requireContext(), "Successfully Updated", Toast.LENGTH_LONG).show()


            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.popBackStack()
        }
    }

    private fun checkIfEmpty(eggTypeName: String): Boolean {
        return (TextUtils.isEmpty(eggTypeName))
    }

}