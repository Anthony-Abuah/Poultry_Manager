package com.example.pkompoultrymanager.tables.my_farm


import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout

import com.example.pkompoultrymanager.R

class MyFarmForm : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_my_farm, container, false)

        val myFarmInfo = context?.getSharedPreferences("MyFarmInfo", Context.MODE_PRIVATE)
        val editor = myFarmInfo?.edit()

        val farmName = view.findViewById<ConstraintLayout>(R.id.clFarmName_MyFarmFragment)
        val farmContact = view.findViewById<ConstraintLayout>(R.id.clFarmContact_MyFarmFragment)
        val farmLocation = view.findViewById<ConstraintLayout>(R.id.clFarmLocation_MyFarmFragment)
        val farmOwner = view.findViewById<ConstraintLayout>(R.id.clFarmOwner_MyFarmFragment)
        val numberOfEggsPerCrate = view.findViewById<ConstraintLayout>(R.id.clFarmCrate_MyFarmFragment)
        val numberOfEmployees = view.findViewById<ConstraintLayout>(R.id.clFarmEmployees_MyFarmFragment)
        val measuringUnit = view.findViewById<ConstraintLayout>(R.id.clFarmMeasuringUnit_MyFarmFragment)
        val currency = view.findViewById<ConstraintLayout>(R.id.clFarmCurrency_MyFarmFragment)

        val tvFarmName = view.findViewById<TextView>(R.id.tvFarmNameValue_MyFarmFragment)
        val tvFarmContact = view.findViewById<TextView>(R.id.tvFarmContactValue_MyFarmFragment)
        val tvFarmLocation = view.findViewById<TextView>(R.id.tvFarmLocationValue_MyFarmFragment)
        val tvFarmOwner = view.findViewById<TextView>(R.id.tvFarmOwnerValue_MyFarmFragment)
        val tvNumberOfEggsPerCrate = view.findViewById<TextView>(R.id.tvFarmCrateValue_MyFarmFragment)
        val tvNumberOfEmployees = view.findViewById<TextView>(R.id.tvFarmEmployeesValue_MyFarmFragment)
        val tvMeasuringUnit = view.findViewById<TextView>(R.id.tvFarmMeasuringUnitValue_MyFarmFragment)
        val tvCurrency = view.findViewById<TextView>(R.id.tvFarmCurrencyValue_MyFarmFragment)


        var theFarmName = myFarmInfo?.getString("myFarmName","N/A")
        var theFarmContact = myFarmInfo?.getString("myFarmContact","N/A")
        var theFarmLocation = myFarmInfo?.getString("myFarmLocation","N/A")
        var theFarmOwner = myFarmInfo?.getString("myFarmOwner","N/A")
        var theFarmEmployeeNumber = myFarmInfo?.getString("myFarmEmployees","N/A")
        var theNumberOfEggsPerCrates = myFarmInfo?.getString("numberOfEggsPerCrate","30")
        var theCurrency = myFarmInfo?.getString("currency","GH₵")
        var theMeasuringUnit = myFarmInfo?.getString("measuringUnit","kg")

        tvFarmName.text = theFarmName
        tvFarmContact.text = theFarmContact
        tvFarmLocation.text = theFarmLocation
        tvFarmOwner.text = theFarmOwner
        tvNumberOfEmployees.text = theFarmEmployeeNumber
        tvNumberOfEggsPerCrate.text = theNumberOfEggsPerCrates
        tvCurrency.text = theCurrency
        tvMeasuringUnit.text = theMeasuringUnit

        farmName.setOnClickListener {
            val builder = AlertDialog.Builder(context, Gravity.END)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.input_layout, null)
            with(builder) {
                setTitle("Enter Farm Name")
                setPositiveButton("Save") { dialog, where ->
                    val etFarmName = dialogLayout.findViewById<EditText>(R.id.etInput_InputLayout)
                    val theNameOfFarm = etFarmName.text.toString()
                    if (theNameOfFarm.isNotEmpty()){
                        editor?.apply {
                            putString("myFarmName",theNameOfFarm)
                            apply()
                        }
                        theFarmName = myFarmInfo?.getString("myFarmName","N/A")
                        tvFarmName.text = theFarmName
                    }
                    else Toast.makeText(requireContext(), "Please Enter Farm Name", Toast.LENGTH_LONG).show()
                }
                setNegativeButton("Cancel") { dialog, which ->
                    Toast.makeText(requireContext(), "Farm name not set", Toast.LENGTH_LONG).show()
                }
                setView(dialogLayout)
                show() }
            }

        farmContact.setOnClickListener {
            val builder = AlertDialog.Builder(context, Gravity.END)
            val theInflater = layoutInflater
            val farmContactDialog = theInflater.inflate(R.layout.input_layout, null)
            with(builder) {
                setTitle("Enter Farm Contact")
                setPositiveButton("Save") { _, _ ->
                    val etFarmContact = farmContactDialog.findViewById<EditText>(R.id.etInput_InputLayout)
                    val theContactOfFarm = etFarmContact.text.toString()
                    if (theContactOfFarm.isNotEmpty()){
                        editor?.apply {
                            putString("myFarmContact",theContactOfFarm)
                            apply()
                        }
                        theFarmContact = myFarmInfo?.getString("myFarmContact","N/A")
                        tvFarmContact.text = theFarmContact
                    }
                    else Toast.makeText(requireContext(), "Please Enter Farm Contact", Toast.LENGTH_LONG).show()

                }
                setNegativeButton("Cancel") { dialog, which ->
                    Toast.makeText(requireContext(), "Farm contact not set", Toast.LENGTH_LONG).show()
                }
                setView(farmContactDialog)
                show() }
            }

        farmLocation.setOnClickListener {
            val builder = AlertDialog.Builder(context, Gravity.END)
            val theInflater = layoutInflater
            val farmLocationDialog = theInflater.inflate(R.layout.input_layout, null)
            with(builder) {
                setTitle("Enter Farm Location")
                setPositiveButton("Save") { _, _ ->
                    val etFarmLocation = farmLocationDialog.findViewById<EditText>(R.id.etInput_InputLayout)
                    val theLocationOfFarm = etFarmLocation.text.toString()
                    if (theLocationOfFarm.isNotEmpty()){
                        editor?.apply {
                            putString("myFarmLocation",theLocationOfFarm)
                            apply()
                        }
                        theFarmLocation = myFarmInfo?.getString("myFarmLocation","N/A")
                        tvFarmLocation.text = theFarmLocation
                    }
                    else Toast.makeText(requireContext(), "Please Enter Farm Location", Toast.LENGTH_LONG).show()

                }
                setNegativeButton("Cancel") { _, _ ->
                    Toast.makeText(requireContext(), "Farm location not set", Toast.LENGTH_LONG).show()
                }
                setView(farmLocationDialog)
                show() }
            }

        numberOfEmployees.setOnClickListener {
            val builder = AlertDialog.Builder(context, Gravity.END)
            val theInflater = layoutInflater
            val farmEmployeesDialog = theInflater.inflate(R.layout.input_layout, null)
            with(builder) {
                setTitle("Enter Number of Employees")
                setPositiveButton("Save") { _, _ ->
                    val etFarmEmployees = farmEmployeesDialog.findViewById<EditText>(R.id.etInput_InputLayout)
                    val theFarmEmployees = etFarmEmployees.text.toString()
                    if (theFarmEmployees.isNotEmpty()){
                        editor?.apply {
                            putString("myFarmEmployees",theFarmEmployees)
                            apply()
                        }
                        theFarmEmployeeNumber = myFarmInfo?.getString("myFarmEmployees","N/A")
                        tvNumberOfEmployees.text = theFarmEmployeeNumber
                    }
                    else Toast.makeText(requireContext(), "Please Enter Number of Employees", Toast.LENGTH_LONG).show()

                }
                setNegativeButton("Cancel") { _, _ ->
                    Toast.makeText(requireContext(), "Number of Employees not given", Toast.LENGTH_LONG).show()
                }
                setView(farmEmployeesDialog)
                show() }
            }

        farmOwner.setOnClickListener {
            val builder = AlertDialog.Builder(context, Gravity.END)
            val theInflater = layoutInflater
            val farmOwnerDialog = theInflater.inflate(R.layout.input_layout, null)
            with(builder) {
                setTitle("Enter Name of Farm Owner")
                setPositiveButton("Save") { _, _ ->
                    val etFarmOwner = farmOwnerDialog.findViewById<EditText>(R.id.etInput_InputLayout)
                    val theOwnerOfFarm = etFarmOwner.text.toString()
                    if (theOwnerOfFarm.isNotEmpty()){
                        editor?.apply {
                            putString("myFarmOwner",theOwnerOfFarm)
                            apply()
                        }
                        theFarmOwner = myFarmInfo?.getString("myFarmOwner","N/A")
                        tvFarmOwner.text = theFarmOwner
                    }
                    else Toast.makeText(requireContext(), "Please Enter Name of Farm Owner", Toast.LENGTH_LONG).show()

                }
                setNegativeButton("Cancel") { _, _ ->
                    Toast.makeText(requireContext(), "Farm owner name not set", Toast.LENGTH_LONG).show()
                }
                setView(farmOwnerDialog)
                show() }
            }

        numberOfEggsPerCrate.setOnClickListener {
            val builder = AlertDialog.Builder(context, Gravity.END)
            val theInflater = layoutInflater
            val farmCrateDialog = theInflater.inflate(R.layout.input_layout, null)
            with(builder) {
                setTitle("Enter number of Eggs per Crate")
                setPositiveButton("Save") { _, _ ->
                    val etFarmCrate = farmCrateDialog.findViewById<EditText>(R.id.etInput_InputLayout)
                    val theFarmCrate = etFarmCrate.text.toString()
                    if (theFarmCrate.isNotEmpty()){
                        editor?.apply {
                            putString("numberOfEggsPerCrate",theFarmCrate)
                            apply()
                        }
                        theNumberOfEggsPerCrates = myFarmInfo?.getString("numberOfEggsPerCrate","30")
                        tvNumberOfEggsPerCrate.text = theNumberOfEggsPerCrates
                    }
                    else Toast.makeText(requireContext(), "Please Enter the Number of Eggs per Crate", Toast.LENGTH_LONG).show()

                }
                setNegativeButton("Cancel") { _, _ ->
                    Toast.makeText(requireContext(), "Number Of Eggs per crate not set", Toast.LENGTH_LONG).show()
                }
                setView(farmCrateDialog)
                show() }
        }

        currency.setOnClickListener {
            val builder = AlertDialog.Builder(context, Gravity.END)
            val theInflater = layoutInflater
            val farmCurrencyDialog = theInflater.inflate(R.layout.select_input_layout, null)
            with(builder) {
                setTitle("Select Currency")
                val actvFarmCurrency = farmCurrencyDialog.findViewById<AutoCompleteTextView>(R.id.actvSelectInput_SelectInputLayout)
                val currencies = listOf("JPY", "CNY", "SDG", "RON", "MKD", "MXN", "CAD", "ZAR", "AUD", "NOK", "ILS", "ISK", "SYP", "LYD", "UYU", "YER", "CSD","EEK", "THB", "IDR", "LBP", "AED", "BOB", "QAR", "BHD", "HNL", "HRK", "COP", "ALL", "DKK", "MYR", "SEK", "RSD", "BGN", "DOP", "KRW", "LVL","VEF", "CZK", "TND", "KWD", "VND", "JOD", "NZD", "PAB", "CLP", "PEN","GBP", "DZD", "CHF", "RUB", "UAH", "ARS", "SAR", "EGP", "INR", "PYG","TWD", "TRY", "BAM", "OMR", "SGD", "MAD", "BYR", "NIO", "HKD", "LTL", "SKK", "GTQ", "BRL", "EUR", "HUF", "IQD", "CRC", "PHP", "SVC", "PLN", "USD", "XBB", "XBC", "XBD", "UGX", "MOP", "SHP", "TTD", "UYI", "KGS", "DJF", "BTN", "XBA","HTG", "BBD", "XAU", "FKP", "MWK", "PGK", "XCD", "COU", "RWF", "NGN", "BSD", "XTS", "TMT", "GEL", "VUV", "FJD", "MVR", "AZN", "MNT", "MGA", "WST", "KMF", "GNF", "SBD", "BDT", "MMK", "TJS", "CVE", "MDL", "KES", "SRD", "LRD", "MUR", "CDF", "BMD", "USN", "CUP", "USS", "GMD", "UZS", "CUC", "ZMK", "NPR", "NAD", "LAK", "SZL", "XDR", "BND", "TZS", "MXV", "LSL", "KYD", "LKR", "ANG", "PKR", "SLL", "SCR", "GH₵", "ERN", "BOV", "GIP", "IRR", "XPT", "BWP", "XFU", "CLF", "ETB", "STD", "XXX", "XPD", "AMD", "XPF", "JMD", "MRO", "BIF", "CHW", "ZWL", "AWG", "MZN", "CHE", "XOF", "KZT", "BZD","XAG", "KHR", "XAF","GYD", "AFN", "SOS", "TOP", "AOA", "KPW")
                val currencyAdapter = ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, currencies.sorted())
                actvFarmCurrency.setAdapter(currencyAdapter)
                val theFarmCurrency = actvFarmCurrency.text.toString()
                setPositiveButton("Save") { _, _ ->
                    if (!TextUtils.isEmpty(theFarmCurrency)){
                        editor?.apply {
                            putString("currency",theFarmCurrency)
                            apply()
                        }
                        theCurrency = myFarmInfo?.getString("currency","GH₵")
                        tvCurrency.text = theCurrency
                    }
                    else Toast.makeText(requireContext(), "Please Select Farm Currency", Toast.LENGTH_LONG).show()

                }
                setNegativeButton("Cancel") { _, _ ->
                    Toast.makeText(requireContext(), "Currency not selected", Toast.LENGTH_LONG).show()
                }
                setView(farmCurrencyDialog)
                show() }
        }

        measuringUnit.setOnClickListener {
            val builder = AlertDialog.Builder(context, Gravity.END)
            val theInflater = layoutInflater
            val farmMeasuringUnitDialog = theInflater.inflate(R.layout.select_input_layout, null)
            with(builder) {
                setTitle("Select MeasuringUnit")
                val actvFarmMeasuringUnit = farmMeasuringUnitDialog.findViewById<AutoCompleteTextView>(R.id.actvSelectInput_SelectInputLayout)
                val measuringUnits = listOf("kg", "lbs")
                val measuringUnitAdapter = ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, measuringUnits)
                actvFarmMeasuringUnit.setAdapter(measuringUnitAdapter)
                val theFarmMeasuringUnit = actvFarmMeasuringUnit.text.toString()
                setPositiveButton("Save") { _, _ ->

                    if (!TextUtils.isEmpty(theFarmMeasuringUnit)){
                        editor?.apply {
                            putString("measuringUnit",theFarmMeasuringUnit)
                            apply()
                        }
                        theMeasuringUnit = myFarmInfo?.getString("measuringUnit","kg")
                        tvMeasuringUnit.text = theMeasuringUnit
                    }
                    else Toast.makeText(requireContext(), "Please Select Farm Measuring Unit", Toast.LENGTH_LONG).show()

                }
                setNegativeButton("Cancel") { _, _ ->
                    Toast.makeText(requireContext(), "Measuring Unit not selected", Toast.LENGTH_LONG).show()
                }
                setView(farmMeasuringUnitDialog)
                show() }
        }




        return view
    }
}