package com.example.pkompoultrymanager.reports.flock_inventory.fragments

import android.app.AlertDialog
import android.app.DatePickerDialog
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
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pkompoultrymanager.R
import com.example.pkompoultrymanager.inventory.flock.addition.FlockInventoryAdditionViewModel
import com.example.pkompoultrymanager.inventory.flock.reduction.FlockInventoryReductionViewModel
import com.example.pkompoultrymanager.reports.feed_inventory.FeedQuantityReportAdapter
import com.example.pkompoultrymanager.reports.flock_inventory.NameQuantityCostReportAdapter
import com.example.pkompoultrymanager.reports.flock_inventory.DateQuantityReportAdapter
import java.text.SimpleDateFormat
import java.util.*

class GeneralFlockReport : Fragment() {
    private lateinit var mFlockInventoryAdditionViewModel: FlockInventoryAdditionViewModel
    private lateinit var mFlockInventoryReductionViewModel: FlockInventoryReductionViewModel

    private lateinit var myFarmInfo: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_general_flock_report, container, false)

        mFlockInventoryAdditionViewModel = ViewModelProvider(this)[FlockInventoryAdditionViewModel::class.java]
        mFlockInventoryReductionViewModel = ViewModelProvider(this)[FlockInventoryReductionViewModel::class.java]

        myFarmInfo = context?.getSharedPreferences("MyFarmInfo", Context.MODE_PRIVATE)!!
        val numberOfFlocksPerCrates = myFarmInfo.getInt("numberOfFlocksPerCrate", 30)
        val currency = myFarmInfo.getString("currency", "GHS")
        val measuringUnit = myFarmInfo.getString("measuringUnit", "kg")

        val myFormat = "dd-MM-yyyy"
        val simpleDateFormat = SimpleDateFormat(myFormat, Locale.UK)

        // get the date values from calendar instance
        val currentCalendar = Calendar.getInstance()
        val currentYear = currentCalendar.get(Calendar.YEAR)
        val currentMonth = currentCalendar.get(Calendar.MONTH)
        val currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH)
        val currentDayOfWeek = currentCalendar.get(Calendar.DAY_OF_WEEK)

        // put the date values in string format
        val currentDate = "$currentDay-${currentMonth + 1}-$currentYear"
        val previousDay = "${currentDay}-${currentMonth + 1}-$currentYear"
        val previousWeek = "${currentDay - currentDayOfWeek + 1}-${currentMonth + 1}-$currentYear"
        val previousMonth = "${1}-${currentMonth + 1}-$currentYear"
        val pastThreeMonths = "${currentDay}-${currentMonth - 2}-$currentYear"
        val pastSixMonths = "${currentDay}-${currentMonth - 5}-$currentYear"
        val pastYear = "${currentDay}-${currentMonth + 1}-${currentYear - 1}"
        val pastTwoYears = "${currentDay}-${currentMonth + 1}-${currentYear - 2}"

        // parse the date into java.util date
        val theCurrentSystemDate: Date? = simpleDateFormat.parse(currentDate)
        val thePreviousSystemDay: Date? = simpleDateFormat.parse(previousDay)
        val thePreviousSystemWeek: Date? = simpleDateFormat.parse(previousWeek)
        val thePreviousSystemMonth: Date? = simpleDateFormat.parse(previousMonth)
        val thePreviousThreeSystemMonths: Date? = simpleDateFormat.parse(pastThreeMonths)
        val thePreviousSixSystemMonths: Date? = simpleDateFormat.parse(pastSixMonths)
        val thePreviousSystemYear: Date? = simpleDateFormat.parse(pastYear)
        val thePreviousTwoSystemYears: Date? = simpleDateFormat.parse(pastTwoYears)

        // convert the java.util date to java.sql date
        val theCurrentDate = theCurrentSystemDate?.time?.let { java.sql.Date(it) }!!
        val thePreviousDay = thePreviousSystemDay?.time?.let { java.sql.Date(it) }!!
        val thePreviousWeek = thePreviousSystemWeek?.time?.let { java.sql.Date(it) }!!
        val thePreviousMonth = thePreviousSystemMonth?.time?.let { java.sql.Date(it) }!!
        val thePastThreeMonths = thePreviousThreeSystemMonths?.time?.let { java.sql.Date(it) }!!
        val thePastSixMonths = thePreviousSixSystemMonths?.time?.let { java.sql.Date(it) }!!
        val thePastYear = thePreviousSystemYear?.time?.let { java.sql.Date(it) }!!
        val thePastTwoYears = thePreviousTwoSystemYears?.time?.let { java.sql.Date(it) }!!

        val dateFilter = view.findViewById<ImageButton>(R.id.ibDateFilter_GeneralFlockReport)

        // report headers
        val tvFlockDateQuantityAcquiredHeader = view. findViewById<TextView>(R.id.tvFlockDateQuantityAcquiredHeaderDate_GeneralFlockReport)
        val tvFlockNameQuantityCostHeader = view. findViewById<TextView>(R.id.tvFlockNameQuantityCostHeaderName_GeneralFlockReport)
        val tvFlockPurposeQuantityCostHeader = view. findViewById<TextView>(R.id.tvFlockPurposeQuantityCostHeaderName_GeneralFlockReport)
        val tvFlockDateQuantityReducedHeader = view. findViewById<TextView>(R.id.tvFlockDateQuantityReducedHeaderName_GeneralFlockReport)
        val tvFlockReducedNameQuantityCostHeader = view. findViewById<TextView>(R.id.tvFlockReducedNameQuantityCostHeaderName_GeneralFlockReport)
        val tvCustomerNameQuantityCostHeader = view. findViewById<TextView>(R.id.tvCustomerNameQuantityCostHeaderName_GeneralFlockReport)
        val tvFlockReductionReasonQuantityHeader = view. findViewById<TextView>(R.id.tvFlockReductionReasonQuantityHeaderName_GeneralFlockReport)

        // totals containers
        val llTotalFlockDateQuantityAcquiredContainer = view. findViewById<LinearLayout>(R.id.llTotalFlockDateQuantityAcquiredContainer_GeneralFlockReport)
        val llTotalFlockNameQuantityCostContainer = view. findViewById<LinearLayout>(R.id.llTotalFlockNameQuantityCostContainer_GeneralFlockReport)
        val llTotalFlockPurposeQuantityCostContainer = view. findViewById<LinearLayout>(R.id.llTotalFlockPurposeQuantityCostContainer_GeneralFlockReport)
        val llTotalFlockDateQuantityReducedContainer = view. findViewById<LinearLayout>(R.id.llTotalFlockDateQuantityReducedContainer_GeneralFlockReport)
        val llTotalFlockReducedNameQuantityCostContainer = view. findViewById<LinearLayout>(R.id.llTotalFlockReducedNameQuantityCostContainer_GeneralFlockReport)
        val llTotalCustomerNameQuantityCostContainer = view. findViewById<LinearLayout>(R.id.llTotalCustomerNameQuantityCostContainer_GeneralFlockReport)
        val llTotalFlockReductionReasonQuantityContainer = view. findViewById<LinearLayout>(R.id.llTotalFlockReductionReasonQuantityContainer_GeneralFlockReport)

        // Totals
        val tvTotalFlockDateQuantityAcquiredValueQuantity = view. findViewById<TextView>(R.id.tvTotalFlockDateQuantityAcquiredValueQuantity_GeneralFlockReport)
        val tvTotalFlockNameQuantityCostValueCost = view. findViewById<TextView>(R.id.tvTotalFlockNameQuantityCostValueCost_GeneralFlockReport)
        val tvTotalFlockNameQuantityCostValueQuantity = view. findViewById<TextView>(R.id.tvTotalFlockNameQuantityCostValueQuantity_GeneralFlockReport)
        val tvTotalFlockPurposeQuantityCostValueQuantity = view. findViewById<TextView>(R.id.tvTotalFlockPurposeQuantityCostValueQuantity_GeneralFlockReport)
        val tvTotalFlockPurposeQuantityCostValueCost = view. findViewById<TextView>(R.id.tvTotalFlockPurposeQuantityCostValueCost_GeneralFlockReport)
        val tvTotalFlockDateQuantityReducedValueQuantity = view. findViewById<TextView>(R.id.tvTotalFlockDateQuantityReducedValueQuantity_GeneralFlockReport)
        val tvTotalFlockReducedNameQuantityCostValueQuantity = view. findViewById<TextView>(R.id.tvTotalFlockReducedNameQuantityCostValueQuantity_GeneralFlockReport)
        val tvTotalFlockReducedNameQuantityCostValueCost = view. findViewById<TextView>(R.id.tvTotalFlockReducedNameQuantityCostValueCost_GeneralFlockReport)
        val tvTotalCustomerNameQuantityCostValueQuantity = view. findViewById<TextView>(R.id.tvTotalCustomerNameQuantityCostValueQuantity_GeneralFlockReport)
        val tvTotalCustomerNameQuantityCostValueCost = view. findViewById<TextView>(R.id.tvTotalCustomerNameQuantityCostValueCost_GeneralFlockReport)
        val tvTotalFlockReductionReasonQuantityValueQuantity = view. findViewById<TextView>(R.id.tvTotalFlockReductionReasonQuantityValueQuantity_GeneralFlockReport)

       // recycler views
        val rvFlockDateQuantityAcquiredReport = view.findViewById<RecyclerView>(R.id.rvFlockDateQuantityAcquiredReport_GeneralFlockReport)
        val rvFlockNameQuantityCostReport = view.findViewById<RecyclerView>(R.id.rvFlockNameQuantityCostReport_GeneralFlockReport)
        val rvFlockPurposeQuantityCostReport = view.findViewById<RecyclerView>(R.id.rvFlockPurposeQuantityCostReport_GeneralFlockReport)
        val rvFlockDateQuantityReducedReport = view.findViewById<RecyclerView>(R.id.rvFlockDateQuantityReducedReport_GeneralFlockReport)
        val rvFlockReducedNameQuantityCostReport = view.findViewById<RecyclerView>(R.id.rvFlockReducedNameQuantityCostReport_GeneralFlockReport)
        val rvCustomerNameQuantityCostReport = view.findViewById<RecyclerView>(R.id.rvCustomerNameQuantityCostReport_GeneralFlockReport)
        val rvFlockReductionReasonQuantityReport = view.findViewById<RecyclerView>(R.id.rvFlockReductionReasonQuantityReport_GeneralFlockReport)


        // setting the recycler view adapters
        val flockDateQuantityAcquiredReportAdapter = DateQuantityReportAdapter(numberOfFlocksPerCrates)
        val flockNameQuantityCostReportAdapter = NameQuantityCostReportAdapter()
        val flockPurposeQuantityCostReportAdapter = NameQuantityCostReportAdapter()
        val flockDateQuantityReducedReportAdapter = DateQuantityReportAdapter(numberOfFlocksPerCrates)
        val flockReducedNameQuantityCostReportAdapter = NameQuantityCostReportAdapter()
        val customerNameQuantityCostReportAdapter = NameQuantityCostReportAdapter()
        val flockReductionReasonQuantityReportAdapter = DateQuantityReportAdapter(numberOfFlocksPerCrates)


        rvFlockDateQuantityAcquiredReport.adapter = flockDateQuantityAcquiredReportAdapter
        rvFlockNameQuantityCostReport.adapter = flockNameQuantityCostReportAdapter
        rvFlockPurposeQuantityCostReport.adapter = flockPurposeQuantityCostReportAdapter
        rvFlockDateQuantityReducedReport.adapter = flockDateQuantityReducedReportAdapter
        rvFlockReducedNameQuantityCostReport.adapter = flockReducedNameQuantityCostReportAdapter
        rvCustomerNameQuantityCostReport.adapter = customerNameQuantityCostReportAdapter
        rvFlockReductionReasonQuantityReport.adapter = flockReductionReasonQuantityReportAdapter


        rvFlockDateQuantityAcquiredReport.layoutManager = LinearLayoutManager(requireContext())
        rvFlockNameQuantityCostReport.layoutManager = LinearLayoutManager(requireContext())
        rvFlockPurposeQuantityCostReport.layoutManager = LinearLayoutManager(requireContext())
        rvFlockDateQuantityReducedReport.layoutManager = LinearLayoutManager(requireContext())
        rvFlockReducedNameQuantityCostReport.layoutManager = LinearLayoutManager(requireContext())
        rvCustomerNameQuantityCostReport.layoutManager = LinearLayoutManager(requireContext())
        rvFlockReductionReasonQuantityReport.layoutManager = LinearLayoutManager(requireContext())


        tvFlockDateQuantityAcquiredHeader.setOnClickListener {
            val popupHideAndShowMenu = PopupMenu(requireContext(), tvFlockDateQuantityAcquiredHeader, Gravity.END)
            popupHideAndShowMenu.inflate(R.menu.hide_show_menu)
            popupHideAndShowMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.miShow -> {
                        llTotalFlockDateQuantityAcquiredContainer.isGone = false
                        rvFlockDateQuantityAcquiredReport.isGone = false
                        true
                    }
                    R.id.miHide -> {
                        llTotalFlockDateQuantityAcquiredContainer.isGone = true
                        rvFlockDateQuantityAcquiredReport.isGone = true
                        true
                    }
                    else -> true
                }
            }
            popupHideAndShowMenu.show()
            true
        }
        tvFlockNameQuantityCostHeader.setOnClickListener {
            val popupHideAndShowMenu = PopupMenu(requireContext(), tvFlockNameQuantityCostHeader, Gravity.END)
            popupHideAndShowMenu.inflate(R.menu.hide_show_menu)
            popupHideAndShowMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.miShow -> {
                        llTotalFlockNameQuantityCostContainer.isGone = false
                        rvFlockNameQuantityCostReport.isGone = false
                        true
                    }
                    R.id.miHide -> {
                        llTotalFlockNameQuantityCostContainer.isGone = true
                        rvFlockNameQuantityCostReport.isGone = true
                        true
                    }
                else -> true
                }
            }
            popupHideAndShowMenu.show()
            true
        }
        tvFlockPurposeQuantityCostHeader.setOnClickListener {
            val popupHideAndShowMenu = PopupMenu(requireContext(), tvFlockPurposeQuantityCostHeader, Gravity.END)
            popupHideAndShowMenu.inflate(R.menu.hide_show_menu)
            popupHideAndShowMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.miShow -> {
                        llTotalFlockPurposeQuantityCostContainer.isGone = false
                        rvFlockPurposeQuantityCostReport.isGone = false
                        true
                    }
                    R.id.miHide -> {
                        llTotalFlockPurposeQuantityCostContainer.isGone = true
                        rvFlockPurposeQuantityCostReport.isGone = true
                        true
                    }
                else -> true
                }
            }
            popupHideAndShowMenu.show()
            true
        }
        tvFlockDateQuantityReducedHeader.setOnClickListener{
            val popupHideAndShowMenu = PopupMenu(requireContext(), tvFlockDateQuantityReducedHeader, Gravity.END)
            popupHideAndShowMenu.inflate(R.menu.hide_show_menu)
            popupHideAndShowMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.miShow -> {
                        llTotalFlockDateQuantityReducedContainer.isGone = false
                        rvFlockDateQuantityReducedReport.isGone = false
                        true
                    }
                    R.id.miHide -> {
                        llTotalFlockDateQuantityReducedContainer.isGone = true
                        rvFlockDateQuantityReducedReport.isGone = true
                        true
                    }
                    else -> true
                }
            }
            popupHideAndShowMenu.show()
            true
        }
        tvFlockReducedNameQuantityCostHeader.setOnClickListener {
            val popupHideAndShowMenu = PopupMenu(requireContext(), tvFlockReducedNameQuantityCostHeader, Gravity.END)
            popupHideAndShowMenu.inflate(R.menu.hide_show_menu)
            popupHideAndShowMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.miShow -> {
                        llTotalFlockReducedNameQuantityCostContainer.isGone = false
                        rvFlockReducedNameQuantityCostReport.isGone = false
                        true
                    }
                    R.id.miHide -> {
                        llTotalFlockReducedNameQuantityCostContainer.isGone = true
                        rvFlockReducedNameQuantityCostReport.isGone = true
                        true
                    }
                else -> true
                }
            }
            popupHideAndShowMenu.show()
            true
        }
        tvCustomerNameQuantityCostHeader.setOnClickListener{
            val popupHideAndShowMenu = PopupMenu(requireContext(), tvCustomerNameQuantityCostHeader, Gravity.END)
            popupHideAndShowMenu.inflate(R.menu.hide_show_menu)
            popupHideAndShowMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.miShow -> {
                        llTotalCustomerNameQuantityCostContainer.isGone = false
                        rvCustomerNameQuantityCostReport.isGone = false
                        true
                    }
                    R.id.miHide -> {
                        llTotalCustomerNameQuantityCostContainer.isGone = true
                        rvCustomerNameQuantityCostReport.isGone = true
                        true
                    }
                else -> true
                }
            }
            popupHideAndShowMenu.show()
            true
        }
        tvFlockReductionReasonQuantityHeader.setOnClickListener{
            val popupHideAndShowMenu = PopupMenu(requireContext(), tvFlockReductionReasonQuantityHeader, Gravity.END)
            popupHideAndShowMenu.inflate(R.menu.hide_show_menu)
            popupHideAndShowMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.miShow -> {
                        llTotalFlockReductionReasonQuantityContainer.isGone = false
                        rvFlockReductionReasonQuantityReport.isGone = false
                        true
                    }
                    R.id.miHide -> {
                        llTotalFlockReductionReasonQuantityContainer.isGone = true
                        rvFlockReductionReasonQuantityReport.isGone = true
                        true
                    }
                else -> true
                }
            }
            popupHideAndShowMenu.show()
            true
        }

        mFlockInventoryAdditionViewModel.getFlockQuantityForDate(thePreviousMonth, theCurrentDate)
        mFlockInventoryAdditionViewModel.flockQuantityForDate.observe(viewLifecycleOwner)
        { flockAddition -> flockAddition.let { FlockInventoryAddition ->
            val flockDates = mutableListOf<String>()
            val flockQuantities = mutableListOf<Int>()

            for (flockInventory in FlockInventoryAddition) {
                flockDates.add(simpleDateFormat.format(flockInventory.date))
                flockQuantities.add(flockInventory.quantity)
            }
            flockDateQuantityAcquiredReportAdapter.setData(flockDates, flockQuantities)

            val totalFlockQuantity = flockQuantities.sum()
            val sumOfFlockQuantity = "$totalFlockQuantity birds"

            tvTotalFlockDateQuantityAcquiredValueQuantity.text = sumOfFlockQuantity
        }
        }

        mFlockInventoryAdditionViewModel.getFlockQuantityCostForFlockPurpose(thePreviousMonth, theCurrentDate)
        mFlockInventoryAdditionViewModel.flockQuantityCostForFlockPurpose.observe(viewLifecycleOwner)
        { flockAddition -> flockAddition.let { FlockInventoryAddition ->
            val flockPurposes = mutableListOf<String>()
            val flockQuantities = mutableListOf<Int>()
            val flockCosts = mutableListOf<Double>()

            for (flockInventory in FlockInventoryAddition) {
                flockPurposes.add(flockInventory.name)
                flockQuantities.add(flockInventory.quantity)
                flockCosts.add(flockInventory.cost)
            }
            flockPurposeQuantityCostReportAdapter.setData(flockPurposes, flockQuantities, flockCosts)

            val totalFlockQuantity = flockQuantities.sum()
            val sumOfFlockQuantity = "$totalFlockQuantity birds"

            val totalFlockCost = flockCosts.sum()
            val sumOfFlockCost = "$currency $totalFlockCost"

            tvTotalFlockPurposeQuantityCostValueQuantity.text = sumOfFlockQuantity
            tvTotalFlockPurposeQuantityCostValueCost.text = sumOfFlockCost
        }}

        mFlockInventoryAdditionViewModel.getFlockQuantityCostForFlockName(thePreviousMonth, theCurrentDate)
        mFlockInventoryAdditionViewModel.flockQuantityCostForFlockName.observe(viewLifecycleOwner)
        { flockAddition -> flockAddition.let { FlockInventoryAddition ->
            val flockNames = mutableListOf<String>()
            val flockQuantities = mutableListOf<Int>()
            val flockCosts = mutableListOf<Double>()

            for (flockInventory in FlockInventoryAddition) {
                flockNames.add(flockInventory.name)
                flockQuantities.add(flockInventory.quantity)
                flockCosts.add(flockInventory.cost)
            }
            flockNameQuantityCostReportAdapter.setData(flockNames, flockQuantities, flockCosts)

            val totalFlockQuantity = flockQuantities.sum()
            val sumOfFlockQuantity = "$totalFlockQuantity birds"

            val totalFlockCost = flockCosts.sum()
            val sumOfFlockCost = "$currency $totalFlockCost"

            tvTotalFlockNameQuantityCostValueQuantity.text = sumOfFlockQuantity
            tvTotalFlockNameQuantityCostValueCost.text = sumOfFlockCost
        }
        }


        mFlockInventoryReductionViewModel.getFlockQuantityForDate(thePreviousMonth, theCurrentDate)
        mFlockInventoryReductionViewModel.flockQuantityForDate.observe(viewLifecycleOwner)
        { flockReduction -> flockReduction.let { FlockInventoryReduction ->
            val flockDates = mutableListOf<String>()
            val flockQuantities = mutableListOf<Int>()

            for (flockInventory in FlockInventoryReduction) {
                flockDates.add(simpleDateFormat.format(flockInventory.date))
                flockQuantities.add(flockInventory.quantity)
            }
            flockDateQuantityReducedReportAdapter.setData(flockDates, flockQuantities)

            val totalFlockQuantity = flockQuantities.sum()
            val sumOfFlockQuantity = "$totalFlockQuantity birds"

            tvTotalFlockDateQuantityReducedValueQuantity.text = sumOfFlockQuantity
        }
        }

        mFlockInventoryReductionViewModel.getFlockQuantityForReductionReason(thePreviousMonth, theCurrentDate)
        mFlockInventoryReductionViewModel.flockQuantityForReductionReason.observe(viewLifecycleOwner)
        { flockReduction -> flockReduction.let { FlockInventoryReduction ->
            val reductionReasons = mutableListOf<String>()
            val flockQuantities = mutableListOf<Int>()

            for (flockInventory in FlockInventoryReduction) {
                reductionReasons.add(flockInventory.name)
                flockQuantities.add(flockInventory.quantity)
            }
            flockReductionReasonQuantityReportAdapter.setData(reductionReasons, flockQuantities)

            val totalFlockQuantity = flockQuantities.sum()
            val sumOfFlockQuantity = "$totalFlockQuantity birds"

            tvTotalFlockReductionReasonQuantityValueQuantity.text = sumOfFlockQuantity
        }
        }

        mFlockInventoryReductionViewModel.getFlockQuantityCostForFlockName(thePreviousMonth, theCurrentDate)
        mFlockInventoryReductionViewModel.flockQuantityCostForFlockName.observe(viewLifecycleOwner)
        { flockReduction -> flockReduction.let { FlockInventoryReduction ->
            val flockNames = mutableListOf<String>()
            val flockQuantities = mutableListOf<Int>()
            val flockCosts = mutableListOf<Double>()

            for (flockInventory in FlockInventoryReduction) {
                flockNames.add(flockInventory.name)
                flockQuantities.add(flockInventory.quantity)
                flockCosts.add(flockInventory.cost)
            }
            flockReducedNameQuantityCostReportAdapter.setData(flockNames, flockQuantities, flockCosts)

            val totalFlockQuantity = flockQuantities.sum()
            val sumOfFlockQuantity = "$totalFlockQuantity birds"

            val totalFlockCost = flockCosts.sum()
            val sumOfFlockCost = "$currency $totalFlockCost"

            tvTotalFlockReducedNameQuantityCostValueQuantity.text = sumOfFlockQuantity
            tvTotalFlockReducedNameQuantityCostValueCost.text = sumOfFlockCost
        }
        }

        mFlockInventoryReductionViewModel.getFlockQuantityCostForCustomer(thePreviousMonth, theCurrentDate)
        mFlockInventoryReductionViewModel.flockQuantityCostForCustomer.observe(viewLifecycleOwner)
        { flockReduction -> flockReduction.let { FlockInventoryReduction ->
            val customerNames = mutableListOf<String>()
            val flockQuantities = mutableListOf<Int>()
            val flockCosts = mutableListOf<Double>()

            for (flockInventory in FlockInventoryReduction) {
                customerNames.add(flockInventory.name)
                flockQuantities.add(flockInventory.quantity)
                flockCosts.add(flockInventory.cost)
            }
            flockReducedNameQuantityCostReportAdapter.setData(customerNames, flockQuantities, flockCosts)

            val totalFlockQuantity = flockQuantities.sum()
            val sumOfFlockQuantity = "$totalFlockQuantity birds"

            val totalFlockCost = flockCosts.sum()
            val sumOfFlockCost = "$currency $totalFlockCost"

            tvTotalCustomerNameQuantityCostValueQuantity.text = sumOfFlockQuantity
            tvTotalCustomerNameQuantityCostValueCost.text = sumOfFlockCost
        }
        }



        dateFilter.setOnClickListener {
            val popupDateFilterMenu = PopupMenu(requireContext(), dateFilter)
            popupDateFilterMenu.inflate(R.menu.date_ranges)
            popupDateFilterMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.miAllTime -> {
                        mFlockInventoryAdditionViewModel.allFlockQuantityForDate.observe(viewLifecycleOwner)
                        { flockAddition -> flockAddition.let { FlockInventoryAddition ->
                            val flockDates = mutableListOf<String>()
                            val flockQuantities = mutableListOf<Int>()

                            for (flockInventory in FlockInventoryAddition) {
                                flockDates.add(simpleDateFormat.format(flockInventory.date))
                                flockQuantities.add(flockInventory.quantity)
                            }
                            flockDateQuantityAcquiredReportAdapter.setData(flockDates, flockQuantities)

                            val totalFlockQuantity = flockQuantities.sum()
                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                            tvTotalFlockDateQuantityAcquiredValueQuantity.text = sumOfFlockQuantity
                        }
                        }

                        mFlockInventoryAdditionViewModel.allFlockQuantityCostForFlockPurpose.observe(viewLifecycleOwner)
                        { flockAddition -> flockAddition.let { FlockInventoryAddition ->
                            val flockPurposes = mutableListOf<String>()
                            val flockQuantities = mutableListOf<Int>()
                            val flockCosts = mutableListOf<Double>()

                            for (flockInventory in FlockInventoryAddition) {
                                flockPurposes.add(flockInventory.name)
                                flockQuantities.add(flockInventory.quantity)
                                flockCosts.add(flockInventory.cost)
                            }
                            flockPurposeQuantityCostReportAdapter.setData(flockPurposes, flockQuantities, flockCosts)

                            val totalFlockQuantity = flockQuantities.sum()
                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                            val totalFlockCost = flockCosts.sum()
                            val sumOfFlockCost = "$currency $totalFlockCost"

                            tvTotalFlockPurposeQuantityCostValueQuantity.text = sumOfFlockQuantity
                            tvTotalFlockPurposeQuantityCostValueCost.text = sumOfFlockCost
                        }}

                        mFlockInventoryAdditionViewModel.allFlockQuantityCostForFlockName.observe(viewLifecycleOwner)
                        { flockAddition -> flockAddition.let { FlockInventoryAddition ->
                            val flockNames = mutableListOf<String>()
                            val flockQuantities = mutableListOf<Int>()
                            val flockCosts = mutableListOf<Double>()

                            for (flockInventory in FlockInventoryAddition) {
                                flockNames.add(flockInventory.name)
                                flockQuantities.add(flockInventory.quantity)
                                flockCosts.add(flockInventory.cost)
                            }
                            flockNameQuantityCostReportAdapter.setData(flockNames, flockQuantities, flockCosts)

                            val totalFlockQuantity = flockQuantities.sum()
                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                            val totalFlockCost = flockCosts.sum()
                            val sumOfFlockCost = "$currency $totalFlockCost"

                            tvTotalFlockNameQuantityCostValueQuantity.text = sumOfFlockQuantity
                            tvTotalFlockNameQuantityCostValueCost.text = sumOfFlockCost
                        }
                        }


                        mFlockInventoryReductionViewModel.allFlockQuantityForDate.observe(viewLifecycleOwner)
                        { flockReduction -> flockReduction.let { FlockInventoryReduction ->
                            val flockDates = mutableListOf<String>()
                            val flockQuantities = mutableListOf<Int>()

                            for (flockInventory in FlockInventoryReduction) {
                                flockDates.add(simpleDateFormat.format(flockInventory.date))
                                flockQuantities.add(flockInventory.quantity)
                            }
                            flockDateQuantityReducedReportAdapter.setData(flockDates, flockQuantities)

                            val totalFlockQuantity = flockQuantities.sum()
                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                            tvTotalFlockDateQuantityReducedValueQuantity.text = sumOfFlockQuantity
                        }
                        }

                        mFlockInventoryReductionViewModel.allFlockQuantityForReductionReason.observe(viewLifecycleOwner)
                        { flockReduction -> flockReduction.let { FlockInventoryReduction ->
                            val reductionReasons = mutableListOf<String>()
                            val flockQuantities = mutableListOf<Int>()

                            for (flockInventory in FlockInventoryReduction) {
                                reductionReasons.add(flockInventory.name)
                                flockQuantities.add(flockInventory.quantity)
                            }
                            flockReductionReasonQuantityReportAdapter.setData(reductionReasons, flockQuantities)

                            val totalFlockQuantity = flockQuantities.sum()
                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                            tvTotalFlockReductionReasonQuantityValueQuantity.text = sumOfFlockQuantity
                        }
                        }

                        mFlockInventoryReductionViewModel.allFlockQuantityCostForFlockName.observe(viewLifecycleOwner)
                        { flockReduction -> flockReduction.let { FlockInventoryReduction ->
                            val flockNames = mutableListOf<String>()
                            val flockQuantities = mutableListOf<Int>()
                            val flockCosts = mutableListOf<Double>()

                            for (flockInventory in FlockInventoryReduction) {
                                flockNames.add(flockInventory.name)
                                flockQuantities.add(flockInventory.quantity)
                                flockCosts.add(flockInventory.cost)
                            }
                            flockReducedNameQuantityCostReportAdapter.setData(flockNames, flockQuantities, flockCosts)

                            val totalFlockQuantity = flockQuantities.sum()
                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                            val totalFlockCost = flockCosts.sum()
                            val sumOfFlockCost = "$currency $totalFlockCost"

                            tvTotalFlockReducedNameQuantityCostValueQuantity.text = sumOfFlockQuantity
                            tvTotalFlockReducedNameQuantityCostValueCost.text = sumOfFlockCost
                        }
                        }

                        mFlockInventoryReductionViewModel.allFlockQuantityCostForCustomer.observe(viewLifecycleOwner)
                        { flockReduction -> flockReduction.let { FlockInventoryReduction ->
                            val customerNames = mutableListOf<String>()
                            val flockQuantities = mutableListOf<Int>()
                            val flockCosts = mutableListOf<Double>()

                            for (flockInventory in FlockInventoryReduction) {
                                customerNames.add(flockInventory.name)
                                flockQuantities.add(flockInventory.quantity)
                                flockCosts.add(flockInventory.cost)
                            }
                            flockReducedNameQuantityCostReportAdapter.setData(customerNames, flockQuantities, flockCosts)

                            val totalFlockQuantity = flockQuantities.sum()
                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                            val totalFlockCost = flockCosts.sum()
                            val sumOfFlockCost = "$currency $totalFlockCost"

                            tvTotalCustomerNameQuantityCostValueQuantity.text = sumOfFlockQuantity
                            tvTotalCustomerNameQuantityCostValueCost.text = sumOfFlockCost
                        }
                        }
                        true
                    }
                    R.id.miToday -> {
                        mFlockInventoryAdditionViewModel.getFlockQuantityForDate(theCurrentDate, theCurrentDate)
                        mFlockInventoryAdditionViewModel.flockQuantityForDate.observe(viewLifecycleOwner)
                        { flockAddition -> flockAddition.let { FlockInventoryAddition ->
                            val flockDates = mutableListOf<String>()
                            val flockQuantities = mutableListOf<Int>()

                            for (flockInventory in FlockInventoryAddition) {
                                flockDates.add(simpleDateFormat.format(flockInventory.date))
                                flockQuantities.add(flockInventory.quantity)
                            }
                            flockDateQuantityAcquiredReportAdapter.setData(flockDates, flockQuantities)

                            val totalFlockQuantity = flockQuantities.sum()
                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                            tvTotalFlockDateQuantityAcquiredValueQuantity.text = sumOfFlockQuantity
                        }
                        }

                        mFlockInventoryAdditionViewModel.getFlockQuantityCostForFlockPurpose(theCurrentDate, theCurrentDate)
                        mFlockInventoryAdditionViewModel.flockQuantityCostForFlockPurpose.observe(viewLifecycleOwner)
                        { flockAddition -> flockAddition.let { FlockInventoryAddition ->
                            val flockPurposes = mutableListOf<String>()
                            val flockQuantities = mutableListOf<Int>()
                            val flockCosts = mutableListOf<Double>()

                            for (flockInventory in FlockInventoryAddition) {
                                flockPurposes.add(flockInventory.name)
                                flockQuantities.add(flockInventory.quantity)
                                flockCosts.add(flockInventory.cost)
                            }
                            flockPurposeQuantityCostReportAdapter.setData(flockPurposes, flockQuantities, flockCosts)

                            val totalFlockQuantity = flockQuantities.sum()
                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                            val totalFlockCost = flockCosts.sum()
                            val sumOfFlockCost = "$currency $totalFlockCost"

                            tvTotalFlockPurposeQuantityCostValueQuantity.text = sumOfFlockQuantity
                            tvTotalFlockPurposeQuantityCostValueCost.text = sumOfFlockCost
                        }}

                        mFlockInventoryAdditionViewModel.getFlockQuantityCostForFlockName(theCurrentDate, theCurrentDate)
                        mFlockInventoryAdditionViewModel.flockQuantityCostForFlockName.observe(viewLifecycleOwner)
                        { flockAddition -> flockAddition.let { FlockInventoryAddition ->
                            val flockNames = mutableListOf<String>()
                            val flockQuantities = mutableListOf<Int>()
                            val flockCosts = mutableListOf<Double>()

                            for (flockInventory in FlockInventoryAddition) {
                                flockNames.add(flockInventory.name)
                                flockQuantities.add(flockInventory.quantity)
                                flockCosts.add(flockInventory.cost)
                            }
                            flockNameQuantityCostReportAdapter.setData(flockNames, flockQuantities, flockCosts)

                            val totalFlockQuantity = flockQuantities.sum()
                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                            val totalFlockCost = flockCosts.sum()
                            val sumOfFlockCost = "$currency $totalFlockCost"

                            tvTotalFlockNameQuantityCostValueQuantity.text = sumOfFlockQuantity
                            tvTotalFlockNameQuantityCostValueCost.text = sumOfFlockCost
                        }
                        }


                        mFlockInventoryReductionViewModel.getFlockQuantityForDate(theCurrentDate, theCurrentDate)
                        mFlockInventoryReductionViewModel.flockQuantityForDate.observe(viewLifecycleOwner)
                        { flockReduction -> flockReduction.let { FlockInventoryReduction ->
                            val flockDates = mutableListOf<String>()
                            val flockQuantities = mutableListOf<Int>()

                            for (flockInventory in FlockInventoryReduction) {
                                flockDates.add(simpleDateFormat.format(flockInventory.date))
                                flockQuantities.add(flockInventory.quantity)
                            }
                            flockDateQuantityReducedReportAdapter.setData(flockDates, flockQuantities)

                            val totalFlockQuantity = flockQuantities.sum()
                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                            tvTotalFlockDateQuantityReducedValueQuantity.text = sumOfFlockQuantity
                        }
                        }

                        mFlockInventoryReductionViewModel.getFlockQuantityForReductionReason(theCurrentDate, theCurrentDate)
                        mFlockInventoryReductionViewModel.flockQuantityForReductionReason.observe(viewLifecycleOwner)
                        { flockReduction -> flockReduction.let { FlockInventoryReduction ->
                            val reductionReasons = mutableListOf<String>()
                            val flockQuantities = mutableListOf<Int>()

                            for (flockInventory in FlockInventoryReduction) {
                                reductionReasons.add(flockInventory.name)
                                flockQuantities.add(flockInventory.quantity)
                            }
                            flockReductionReasonQuantityReportAdapter.setData(reductionReasons, flockQuantities)

                            val totalFlockQuantity = flockQuantities.sum()
                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                            tvTotalFlockReductionReasonQuantityValueQuantity.text = sumOfFlockQuantity
                        }
                        }

                        mFlockInventoryReductionViewModel.getFlockQuantityCostForFlockName(theCurrentDate, theCurrentDate)
                        mFlockInventoryReductionViewModel.flockQuantityCostForFlockName.observe(viewLifecycleOwner)
                        { flockReduction -> flockReduction.let { FlockInventoryReduction ->
                            val flockNames = mutableListOf<String>()
                            val flockQuantities = mutableListOf<Int>()
                            val flockCosts = mutableListOf<Double>()

                            for (flockInventory in FlockInventoryReduction) {
                                flockNames.add(flockInventory.name)
                                flockQuantities.add(flockInventory.quantity)
                                flockCosts.add(flockInventory.cost)
                            }
                            flockReducedNameQuantityCostReportAdapter.setData(flockNames, flockQuantities, flockCosts)

                            val totalFlockQuantity = flockQuantities.sum()
                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                            val totalFlockCost = flockCosts.sum()
                            val sumOfFlockCost = "$currency $totalFlockCost"

                            tvTotalFlockReducedNameQuantityCostValueQuantity.text = sumOfFlockQuantity
                            tvTotalFlockReducedNameQuantityCostValueCost.text = sumOfFlockCost
                        }
                        }

                        mFlockInventoryReductionViewModel.getFlockQuantityCostForCustomer(theCurrentDate, theCurrentDate)
                        mFlockInventoryReductionViewModel.flockQuantityCostForCustomer.observe(viewLifecycleOwner)
                        { flockReduction -> flockReduction.let { FlockInventoryReduction ->
                            val customerNames = mutableListOf<String>()
                            val flockQuantities = mutableListOf<Int>()
                            val flockCosts = mutableListOf<Double>()

                            for (flockInventory in FlockInventoryReduction) {
                                customerNames.add(flockInventory.name)
                                flockQuantities.add(flockInventory.quantity)
                                flockCosts.add(flockInventory.cost)
                            }
                            flockReducedNameQuantityCostReportAdapter.setData(customerNames, flockQuantities, flockCosts)

                            val totalFlockQuantity = flockQuantities.sum()
                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                            val totalFlockCost = flockCosts.sum()
                            val sumOfFlockCost = "$currency $totalFlockCost"

                            tvTotalCustomerNameQuantityCostValueQuantity.text = sumOfFlockQuantity
                            tvTotalCustomerNameQuantityCostValueCost.text = sumOfFlockCost
                        }
                        }

                        true
                    }
                    R.id.miThisWeek -> {
                        mFlockInventoryAdditionViewModel.getFlockQuantityForDate(thePreviousWeek, theCurrentDate)
                        mFlockInventoryAdditionViewModel.flockQuantityForDate.observe(viewLifecycleOwner)
                        { flockAddition -> flockAddition.let { FlockInventoryAddition ->
                            val flockDates = mutableListOf<String>()
                            val flockQuantities = mutableListOf<Int>()

                            for (flockInventory in FlockInventoryAddition) {
                                flockDates.add(simpleDateFormat.format(flockInventory.date))
                                flockQuantities.add(flockInventory.quantity)
                            }
                            flockDateQuantityAcquiredReportAdapter.setData(flockDates, flockQuantities)

                            val totalFlockQuantity = flockQuantities.sum()
                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                            tvTotalFlockDateQuantityAcquiredValueQuantity.text = sumOfFlockQuantity
                        }
                        }

                        mFlockInventoryAdditionViewModel.getFlockQuantityCostForFlockPurpose(thePreviousWeek, theCurrentDate)
                        mFlockInventoryAdditionViewModel.flockQuantityCostForFlockPurpose.observe(viewLifecycleOwner)
                        { flockAddition -> flockAddition.let { FlockInventoryAddition ->
                            val flockPurposes = mutableListOf<String>()
                            val flockQuantities = mutableListOf<Int>()
                            val flockCosts = mutableListOf<Double>()

                            for (flockInventory in FlockInventoryAddition) {
                                flockPurposes.add(flockInventory.name)
                                flockQuantities.add(flockInventory.quantity)
                                flockCosts.add(flockInventory.cost)
                            }
                            flockPurposeQuantityCostReportAdapter.setData(flockPurposes, flockQuantities, flockCosts)

                            val totalFlockQuantity = flockQuantities.sum()
                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                            val totalFlockCost = flockCosts.sum()
                            val sumOfFlockCost = "$currency $totalFlockCost"

                            tvTotalFlockPurposeQuantityCostValueQuantity.text = sumOfFlockQuantity
                            tvTotalFlockPurposeQuantityCostValueCost.text = sumOfFlockCost
                        }}

                        mFlockInventoryAdditionViewModel.getFlockQuantityCostForFlockName(thePreviousWeek, theCurrentDate)
                        mFlockInventoryAdditionViewModel.flockQuantityCostForFlockName.observe(viewLifecycleOwner)
                        { flockAddition -> flockAddition.let { FlockInventoryAddition ->
                            val flockNames = mutableListOf<String>()
                            val flockQuantities = mutableListOf<Int>()
                            val flockCosts = mutableListOf<Double>()

                            for (flockInventory in FlockInventoryAddition) {
                                flockNames.add(flockInventory.name)
                                flockQuantities.add(flockInventory.quantity)
                                flockCosts.add(flockInventory.cost)
                            }
                            flockNameQuantityCostReportAdapter.setData(flockNames, flockQuantities, flockCosts)

                            val totalFlockQuantity = flockQuantities.sum()
                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                            val totalFlockCost = flockCosts.sum()
                            val sumOfFlockCost = "$currency $totalFlockCost"

                            tvTotalFlockNameQuantityCostValueQuantity.text = sumOfFlockQuantity
                            tvTotalFlockNameQuantityCostValueCost.text = sumOfFlockCost
                        }
                        }


                        mFlockInventoryReductionViewModel.getFlockQuantityForDate(thePreviousWeek, theCurrentDate)
                        mFlockInventoryReductionViewModel.flockQuantityForDate.observe(viewLifecycleOwner)
                        { flockReduction -> flockReduction.let { FlockInventoryReduction ->
                            val flockDates = mutableListOf<String>()
                            val flockQuantities = mutableListOf<Int>()

                            for (flockInventory in FlockInventoryReduction) {
                                flockDates.add(simpleDateFormat.format(flockInventory.date))
                                flockQuantities.add(flockInventory.quantity)
                            }
                            flockDateQuantityReducedReportAdapter.setData(flockDates, flockQuantities)

                            val totalFlockQuantity = flockQuantities.sum()
                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                            tvTotalFlockDateQuantityReducedValueQuantity.text = sumOfFlockQuantity
                        }
                        }

                        mFlockInventoryReductionViewModel.getFlockQuantityForReductionReason(thePreviousWeek, theCurrentDate)
                        mFlockInventoryReductionViewModel.flockQuantityForReductionReason.observe(viewLifecycleOwner)
                        { flockReduction -> flockReduction.let { FlockInventoryReduction ->
                            val reductionReasons = mutableListOf<String>()
                            val flockQuantities = mutableListOf<Int>()

                            for (flockInventory in FlockInventoryReduction) {
                                reductionReasons.add(flockInventory.name)
                                flockQuantities.add(flockInventory.quantity)
                            }
                            flockReductionReasonQuantityReportAdapter.setData(reductionReasons, flockQuantities)

                            val totalFlockQuantity = flockQuantities.sum()
                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                            tvTotalFlockReductionReasonQuantityValueQuantity.text = sumOfFlockQuantity
                        }
                        }

                        mFlockInventoryReductionViewModel.getFlockQuantityCostForFlockName(thePreviousWeek, theCurrentDate)
                        mFlockInventoryReductionViewModel.flockQuantityCostForFlockName.observe(viewLifecycleOwner)
                        { flockReduction -> flockReduction.let { FlockInventoryReduction ->
                            val flockNames = mutableListOf<String>()
                            val flockQuantities = mutableListOf<Int>()
                            val flockCosts = mutableListOf<Double>()

                            for (flockInventory in FlockInventoryReduction) {
                                flockNames.add(flockInventory.name)
                                flockQuantities.add(flockInventory.quantity)
                                flockCosts.add(flockInventory.cost)
                            }
                            flockReducedNameQuantityCostReportAdapter.setData(flockNames, flockQuantities, flockCosts)

                            val totalFlockQuantity = flockQuantities.sum()
                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                            val totalFlockCost = flockCosts.sum()
                            val sumOfFlockCost = "$currency $totalFlockCost"

                            tvTotalFlockReducedNameQuantityCostValueQuantity.text = sumOfFlockQuantity
                            tvTotalFlockReducedNameQuantityCostValueCost.text = sumOfFlockCost
                        }
                        }

                        mFlockInventoryReductionViewModel.getFlockQuantityCostForCustomer(thePreviousWeek, theCurrentDate)
                        mFlockInventoryReductionViewModel.flockQuantityCostForCustomer.observe(viewLifecycleOwner)
                        { flockReduction -> flockReduction.let { FlockInventoryReduction ->
                            val customerNames = mutableListOf<String>()
                            val flockQuantities = mutableListOf<Int>()
                            val flockCosts = mutableListOf<Double>()

                            for (flockInventory in FlockInventoryReduction) {
                                customerNames.add(flockInventory.name)
                                flockQuantities.add(flockInventory.quantity)
                                flockCosts.add(flockInventory.cost)
                            }
                            flockReducedNameQuantityCostReportAdapter.setData(customerNames, flockQuantities, flockCosts)

                            val totalFlockQuantity = flockQuantities.sum()
                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                            val totalFlockCost = flockCosts.sum()
                            val sumOfFlockCost = "$currency $totalFlockCost"

                            tvTotalCustomerNameQuantityCostValueQuantity.text = sumOfFlockQuantity
                            tvTotalCustomerNameQuantityCostValueCost.text = sumOfFlockCost
                        }
                        }

                        true
                    }
                    R.id.miThisMonth -> {
                        mFlockInventoryAdditionViewModel.getFlockQuantityForDate(thePreviousMonth, theCurrentDate)
                        mFlockInventoryAdditionViewModel.flockQuantityForDate.observe(viewLifecycleOwner)
                        { flockAddition -> flockAddition.let { FlockInventoryAddition ->
                            val flockDates = mutableListOf<String>()
                            val flockQuantities = mutableListOf<Int>()

                            for (flockInventory in FlockInventoryAddition) {
                                flockDates.add(simpleDateFormat.format(flockInventory.date))
                                flockQuantities.add(flockInventory.quantity)
                            }
                            flockDateQuantityAcquiredReportAdapter.setData(flockDates, flockQuantities)

                            val totalFlockQuantity = flockQuantities.sum()
                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                            tvTotalFlockDateQuantityAcquiredValueQuantity.text = sumOfFlockQuantity
                        }
                        }

                        mFlockInventoryAdditionViewModel.getFlockQuantityCostForFlockPurpose(thePreviousMonth, theCurrentDate)
                        mFlockInventoryAdditionViewModel.flockQuantityCostForFlockPurpose.observe(viewLifecycleOwner)
                        { flockAddition -> flockAddition.let { FlockInventoryAddition ->
                            val flockPurposes = mutableListOf<String>()
                            val flockQuantities = mutableListOf<Int>()
                            val flockCosts = mutableListOf<Double>()

                            for (flockInventory in FlockInventoryAddition) {
                                flockPurposes.add(flockInventory.name)
                                flockQuantities.add(flockInventory.quantity)
                                flockCosts.add(flockInventory.cost)
                            }
                            flockPurposeQuantityCostReportAdapter.setData(flockPurposes, flockQuantities, flockCosts)

                            val totalFlockQuantity = flockQuantities.sum()
                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                            val totalFlockCost = flockCosts.sum()
                            val sumOfFlockCost = "$currency $totalFlockCost"

                            tvTotalFlockPurposeQuantityCostValueQuantity.text = sumOfFlockQuantity
                            tvTotalFlockPurposeQuantityCostValueCost.text = sumOfFlockCost
                        }}

                        mFlockInventoryAdditionViewModel.getFlockQuantityCostForFlockName(thePreviousMonth, theCurrentDate)
                        mFlockInventoryAdditionViewModel.flockQuantityCostForFlockName.observe(viewLifecycleOwner)
                        { flockAddition -> flockAddition.let { FlockInventoryAddition ->
                            val flockNames = mutableListOf<String>()
                            val flockQuantities = mutableListOf<Int>()
                            val flockCosts = mutableListOf<Double>()

                            for (flockInventory in FlockInventoryAddition) {
                                flockNames.add(flockInventory.name)
                                flockQuantities.add(flockInventory.quantity)
                                flockCosts.add(flockInventory.cost)
                            }
                            flockNameQuantityCostReportAdapter.setData(flockNames, flockQuantities, flockCosts)

                            val totalFlockQuantity = flockQuantities.sum()
                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                            val totalFlockCost = flockCosts.sum()
                            val sumOfFlockCost = "$currency $totalFlockCost"

                            tvTotalFlockNameQuantityCostValueQuantity.text = sumOfFlockQuantity
                            tvTotalFlockNameQuantityCostValueCost.text = sumOfFlockCost
                        }
                        }


                        mFlockInventoryReductionViewModel.getFlockQuantityForDate(thePreviousMonth, theCurrentDate)
                        mFlockInventoryReductionViewModel.flockQuantityForDate.observe(viewLifecycleOwner)
                        { flockReduction -> flockReduction.let { FlockInventoryReduction ->
                            val flockDates = mutableListOf<String>()
                            val flockQuantities = mutableListOf<Int>()

                            for (flockInventory in FlockInventoryReduction) {
                                flockDates.add(simpleDateFormat.format(flockInventory.date))
                                flockQuantities.add(flockInventory.quantity)
                            }
                            flockDateQuantityReducedReportAdapter.setData(flockDates, flockQuantities)

                            val totalFlockQuantity = flockQuantities.sum()
                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                            tvTotalFlockDateQuantityReducedValueQuantity.text = sumOfFlockQuantity
                        }
                        }

                        mFlockInventoryReductionViewModel.getFlockQuantityForReductionReason(thePreviousMonth, theCurrentDate)
                        mFlockInventoryReductionViewModel.flockQuantityForReductionReason.observe(viewLifecycleOwner)
                        { flockReduction -> flockReduction.let { FlockInventoryReduction ->
                            val reductionReasons = mutableListOf<String>()
                            val flockQuantities = mutableListOf<Int>()

                            for (flockInventory in FlockInventoryReduction) {
                                reductionReasons.add(flockInventory.name)
                                flockQuantities.add(flockInventory.quantity)
                            }
                            flockReductionReasonQuantityReportAdapter.setData(reductionReasons, flockQuantities)

                            val totalFlockQuantity = flockQuantities.sum()
                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                            tvTotalFlockReductionReasonQuantityValueQuantity.text = sumOfFlockQuantity
                        }
                        }

                        mFlockInventoryReductionViewModel.getFlockQuantityCostForFlockName(thePreviousMonth, theCurrentDate)
                        mFlockInventoryReductionViewModel.flockQuantityCostForFlockName.observe(viewLifecycleOwner)
                        { flockReduction -> flockReduction.let { FlockInventoryReduction ->
                            val flockNames = mutableListOf<String>()
                            val flockQuantities = mutableListOf<Int>()
                            val flockCosts = mutableListOf<Double>()

                            for (flockInventory in FlockInventoryReduction) {
                                flockNames.add(flockInventory.name)
                                flockQuantities.add(flockInventory.quantity)
                                flockCosts.add(flockInventory.cost)
                            }
                            flockReducedNameQuantityCostReportAdapter.setData(flockNames, flockQuantities, flockCosts)

                            val totalFlockQuantity = flockQuantities.sum()
                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                            val totalFlockCost = flockCosts.sum()
                            val sumOfFlockCost = "$currency $totalFlockCost"

                            tvTotalFlockReducedNameQuantityCostValueQuantity.text = sumOfFlockQuantity
                            tvTotalFlockReducedNameQuantityCostValueCost.text = sumOfFlockCost
                        }
                        }

                        mFlockInventoryReductionViewModel.getFlockQuantityCostForCustomer(thePreviousMonth, theCurrentDate)
                        mFlockInventoryReductionViewModel.flockQuantityCostForCustomer.observe(viewLifecycleOwner)
                        { flockReduction -> flockReduction.let { FlockInventoryReduction ->
                            val customerNames = mutableListOf<String>()
                            val flockQuantities = mutableListOf<Int>()
                            val flockCosts = mutableListOf<Double>()

                            for (flockInventory in FlockInventoryReduction) {
                                customerNames.add(flockInventory.name)
                                flockQuantities.add(flockInventory.quantity)
                                flockCosts.add(flockInventory.cost)
                            }
                            flockReducedNameQuantityCostReportAdapter.setData(customerNames, flockQuantities, flockCosts)

                            val totalFlockQuantity = flockQuantities.sum()
                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                            val totalFlockCost = flockCosts.sum()
                            val sumOfFlockCost = "$currency $totalFlockCost"

                            tvTotalCustomerNameQuantityCostValueQuantity.text = sumOfFlockQuantity
                            tvTotalCustomerNameQuantityCostValueCost.text = sumOfFlockCost
                        }
                        }

                        true
                    }
                    R.id.miPastThreeMonths -> {
                        mFlockInventoryAdditionViewModel.getFlockQuantityForDate(thePastThreeMonths, theCurrentDate)
                        mFlockInventoryAdditionViewModel.flockQuantityForDate.observe(viewLifecycleOwner)
                        { flockAddition -> flockAddition.let { FlockInventoryAddition ->
                            val flockDates = mutableListOf<String>()
                            val flockQuantities = mutableListOf<Int>()

                            for (flockInventory in FlockInventoryAddition) {
                                flockDates.add(simpleDateFormat.format(flockInventory.date))
                                flockQuantities.add(flockInventory.quantity)
                            }
                            flockDateQuantityAcquiredReportAdapter.setData(flockDates, flockQuantities)

                            val totalFlockQuantity = flockQuantities.sum()
                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                            tvTotalFlockDateQuantityAcquiredValueQuantity.text = sumOfFlockQuantity
                        }
                        }

                        mFlockInventoryAdditionViewModel.getFlockQuantityCostForFlockPurpose(thePastThreeMonths, theCurrentDate)
                        mFlockInventoryAdditionViewModel.flockQuantityCostForFlockPurpose.observe(viewLifecycleOwner)
                        { flockAddition -> flockAddition.let { FlockInventoryAddition ->
                            val flockPurposes = mutableListOf<String>()
                            val flockQuantities = mutableListOf<Int>()
                            val flockCosts = mutableListOf<Double>()

                            for (flockInventory in FlockInventoryAddition) {
                                flockPurposes.add(flockInventory.name)
                                flockQuantities.add(flockInventory.quantity)
                                flockCosts.add(flockInventory.cost)
                            }
                            flockPurposeQuantityCostReportAdapter.setData(flockPurposes, flockQuantities, flockCosts)

                            val totalFlockQuantity = flockQuantities.sum()
                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                            val totalFlockCost = flockCosts.sum()
                            val sumOfFlockCost = "$currency $totalFlockCost"

                            tvTotalFlockPurposeQuantityCostValueQuantity.text = sumOfFlockQuantity
                            tvTotalFlockPurposeQuantityCostValueCost.text = sumOfFlockCost
                        }}

                        mFlockInventoryAdditionViewModel.getFlockQuantityCostForFlockName(thePastThreeMonths, theCurrentDate)
                        mFlockInventoryAdditionViewModel.flockQuantityCostForFlockName.observe(viewLifecycleOwner)
                        { flockAddition -> flockAddition.let { FlockInventoryAddition ->
                            val flockNames = mutableListOf<String>()
                            val flockQuantities = mutableListOf<Int>()
                            val flockCosts = mutableListOf<Double>()

                            for (flockInventory in FlockInventoryAddition) {
                                flockNames.add(flockInventory.name)
                                flockQuantities.add(flockInventory.quantity)
                                flockCosts.add(flockInventory.cost)
                            }
                            flockNameQuantityCostReportAdapter.setData(flockNames, flockQuantities, flockCosts)

                            val totalFlockQuantity = flockQuantities.sum()
                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                            val totalFlockCost = flockCosts.sum()
                            val sumOfFlockCost = "$currency $totalFlockCost"

                            tvTotalFlockNameQuantityCostValueQuantity.text = sumOfFlockQuantity
                            tvTotalFlockNameQuantityCostValueCost.text = sumOfFlockCost
                        }
                        }


                        mFlockInventoryReductionViewModel.getFlockQuantityForDate(thePastThreeMonths, theCurrentDate)
                        mFlockInventoryReductionViewModel.flockQuantityForDate.observe(viewLifecycleOwner)
                        { flockReduction -> flockReduction.let { FlockInventoryReduction ->
                            val flockDates = mutableListOf<String>()
                            val flockQuantities = mutableListOf<Int>()

                            for (flockInventory in FlockInventoryReduction) {
                                flockDates.add(simpleDateFormat.format(flockInventory.date))
                                flockQuantities.add(flockInventory.quantity)
                            }
                            flockDateQuantityReducedReportAdapter.setData(flockDates, flockQuantities)

                            val totalFlockQuantity = flockQuantities.sum()
                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                            tvTotalFlockDateQuantityReducedValueQuantity.text = sumOfFlockQuantity
                        }
                        }

                        mFlockInventoryReductionViewModel.getFlockQuantityForReductionReason(thePastThreeMonths, theCurrentDate)
                        mFlockInventoryReductionViewModel.flockQuantityForReductionReason.observe(viewLifecycleOwner)
                        { flockReduction -> flockReduction.let { FlockInventoryReduction ->
                            val reductionReasons = mutableListOf<String>()
                            val flockQuantities = mutableListOf<Int>()

                            for (flockInventory in FlockInventoryReduction) {
                                reductionReasons.add(flockInventory.name)
                                flockQuantities.add(flockInventory.quantity)
                            }
                            flockReductionReasonQuantityReportAdapter.setData(reductionReasons, flockQuantities)

                            val totalFlockQuantity = flockQuantities.sum()
                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                            tvTotalFlockReductionReasonQuantityValueQuantity.text = sumOfFlockQuantity
                        }
                        }

                        mFlockInventoryReductionViewModel.getFlockQuantityCostForFlockName(thePastThreeMonths, theCurrentDate)
                        mFlockInventoryReductionViewModel.flockQuantityCostForFlockName.observe(viewLifecycleOwner)
                        { flockReduction -> flockReduction.let { FlockInventoryReduction ->
                            val flockNames = mutableListOf<String>()
                            val flockQuantities = mutableListOf<Int>()
                            val flockCosts = mutableListOf<Double>()

                            for (flockInventory in FlockInventoryReduction) {
                                flockNames.add(flockInventory.name)
                                flockQuantities.add(flockInventory.quantity)
                                flockCosts.add(flockInventory.cost)
                            }
                            flockReducedNameQuantityCostReportAdapter.setData(flockNames, flockQuantities, flockCosts)

                            val totalFlockQuantity = flockQuantities.sum()
                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                            val totalFlockCost = flockCosts.sum()
                            val sumOfFlockCost = "$currency $totalFlockCost"

                            tvTotalFlockReducedNameQuantityCostValueQuantity.text = sumOfFlockQuantity
                            tvTotalFlockReducedNameQuantityCostValueCost.text = sumOfFlockCost
                        }
                        }

                        mFlockInventoryReductionViewModel.getFlockQuantityCostForCustomer(thePastThreeMonths, theCurrentDate)
                        mFlockInventoryReductionViewModel.flockQuantityCostForCustomer.observe(viewLifecycleOwner)
                        { flockReduction -> flockReduction.let { FlockInventoryReduction ->
                            val customerNames = mutableListOf<String>()
                            val flockQuantities = mutableListOf<Int>()
                            val flockCosts = mutableListOf<Double>()

                            for (flockInventory in FlockInventoryReduction) {
                                customerNames.add(flockInventory.name)
                                flockQuantities.add(flockInventory.quantity)
                                flockCosts.add(flockInventory.cost)
                            }
                            flockReducedNameQuantityCostReportAdapter.setData(customerNames, flockQuantities, flockCosts)

                            val totalFlockQuantity = flockQuantities.sum()
                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                            val totalFlockCost = flockCosts.sum()
                            val sumOfFlockCost = "$currency $totalFlockCost"

                            tvTotalCustomerNameQuantityCostValueQuantity.text = sumOfFlockQuantity
                            tvTotalCustomerNameQuantityCostValueCost.text = sumOfFlockCost
                        }
                        }

                        true
                    }
                    R.id.miPastSixMonths -> {
                        mFlockInventoryAdditionViewModel.getFlockQuantityForDate(thePastSixMonths, theCurrentDate)
                        mFlockInventoryAdditionViewModel.flockQuantityForDate.observe(viewLifecycleOwner)
                        { flockAddition -> flockAddition.let { FlockInventoryAddition ->
                            val flockDates = mutableListOf<String>()
                            val flockQuantities = mutableListOf<Int>()

                            for (flockInventory in FlockInventoryAddition) {
                                flockDates.add(simpleDateFormat.format(flockInventory.date))
                                flockQuantities.add(flockInventory.quantity)
                            }
                            flockDateQuantityAcquiredReportAdapter.setData(flockDates, flockQuantities)

                            val totalFlockQuantity = flockQuantities.sum()
                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                            tvTotalFlockDateQuantityAcquiredValueQuantity.text = sumOfFlockQuantity
                        }
                        }

                        mFlockInventoryAdditionViewModel.getFlockQuantityCostForFlockPurpose(thePastSixMonths, theCurrentDate)
                        mFlockInventoryAdditionViewModel.flockQuantityCostForFlockPurpose.observe(viewLifecycleOwner)
                        { flockAddition -> flockAddition.let { FlockInventoryAddition ->
                            val flockPurposes = mutableListOf<String>()
                            val flockQuantities = mutableListOf<Int>()
                            val flockCosts = mutableListOf<Double>()

                            for (flockInventory in FlockInventoryAddition) {
                                flockPurposes.add(flockInventory.name)
                                flockQuantities.add(flockInventory.quantity)
                                flockCosts.add(flockInventory.cost)
                            }
                            flockPurposeQuantityCostReportAdapter.setData(flockPurposes, flockQuantities, flockCosts)

                            val totalFlockQuantity = flockQuantities.sum()
                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                            val totalFlockCost = flockCosts.sum()
                            val sumOfFlockCost = "$currency $totalFlockCost"

                            tvTotalFlockPurposeQuantityCostValueQuantity.text = sumOfFlockQuantity
                            tvTotalFlockPurposeQuantityCostValueCost.text = sumOfFlockCost
                        }}

                        mFlockInventoryAdditionViewModel.getFlockQuantityCostForFlockName(thePastSixMonths, theCurrentDate)
                        mFlockInventoryAdditionViewModel.flockQuantityCostForFlockName.observe(viewLifecycleOwner)
                        { flockAddition -> flockAddition.let { FlockInventoryAddition ->
                            val flockNames = mutableListOf<String>()
                            val flockQuantities = mutableListOf<Int>()
                            val flockCosts = mutableListOf<Double>()

                            for (flockInventory in FlockInventoryAddition) {
                                flockNames.add(flockInventory.name)
                                flockQuantities.add(flockInventory.quantity)
                                flockCosts.add(flockInventory.cost)
                            }
                            flockNameQuantityCostReportAdapter.setData(flockNames, flockQuantities, flockCosts)

                            val totalFlockQuantity = flockQuantities.sum()
                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                            val totalFlockCost = flockCosts.sum()
                            val sumOfFlockCost = "$currency $totalFlockCost"

                            tvTotalFlockNameQuantityCostValueQuantity.text = sumOfFlockQuantity
                            tvTotalFlockNameQuantityCostValueCost.text = sumOfFlockCost
                        }
                        }


                        mFlockInventoryReductionViewModel.getFlockQuantityForDate(thePastSixMonths, theCurrentDate)
                        mFlockInventoryReductionViewModel.flockQuantityForDate.observe(viewLifecycleOwner)
                        { flockReduction -> flockReduction.let { FlockInventoryReduction ->
                            val flockDates = mutableListOf<String>()
                            val flockQuantities = mutableListOf<Int>()

                            for (flockInventory in FlockInventoryReduction) {
                                flockDates.add(simpleDateFormat.format(flockInventory.date))
                                flockQuantities.add(flockInventory.quantity)
                            }
                            flockDateQuantityReducedReportAdapter.setData(flockDates, flockQuantities)

                            val totalFlockQuantity = flockQuantities.sum()
                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                            tvTotalFlockDateQuantityReducedValueQuantity.text = sumOfFlockQuantity
                        }
                        }

                        mFlockInventoryReductionViewModel.getFlockQuantityForReductionReason(thePastSixMonths, theCurrentDate)
                        mFlockInventoryReductionViewModel.flockQuantityForReductionReason.observe(viewLifecycleOwner)
                        { flockReduction -> flockReduction.let { FlockInventoryReduction ->
                            val reductionReasons = mutableListOf<String>()
                            val flockQuantities = mutableListOf<Int>()

                            for (flockInventory in FlockInventoryReduction) {
                                reductionReasons.add(flockInventory.name)
                                flockQuantities.add(flockInventory.quantity)
                            }
                            flockReductionReasonQuantityReportAdapter.setData(reductionReasons, flockQuantities)

                            val totalFlockQuantity = flockQuantities.sum()
                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                            tvTotalFlockReductionReasonQuantityValueQuantity.text = sumOfFlockQuantity
                        }
                        }

                        mFlockInventoryReductionViewModel.getFlockQuantityCostForFlockName(thePastSixMonths, theCurrentDate)
                        mFlockInventoryReductionViewModel.flockQuantityCostForFlockName.observe(viewLifecycleOwner)
                        { flockReduction -> flockReduction.let { FlockInventoryReduction ->
                            val flockNames = mutableListOf<String>()
                            val flockQuantities = mutableListOf<Int>()
                            val flockCosts = mutableListOf<Double>()

                            for (flockInventory in FlockInventoryReduction) {
                                flockNames.add(flockInventory.name)
                                flockQuantities.add(flockInventory.quantity)
                                flockCosts.add(flockInventory.cost)
                            }
                            flockReducedNameQuantityCostReportAdapter.setData(flockNames, flockQuantities, flockCosts)

                            val totalFlockQuantity = flockQuantities.sum()
                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                            val totalFlockCost = flockCosts.sum()
                            val sumOfFlockCost = "$currency $totalFlockCost"

                            tvTotalFlockReducedNameQuantityCostValueQuantity.text = sumOfFlockQuantity
                            tvTotalFlockReducedNameQuantityCostValueCost.text = sumOfFlockCost
                        }
                        }

                        mFlockInventoryReductionViewModel.getFlockQuantityCostForCustomer(thePastSixMonths, theCurrentDate)
                        mFlockInventoryReductionViewModel.flockQuantityCostForCustomer.observe(viewLifecycleOwner)
                        { flockReduction -> flockReduction.let { FlockInventoryReduction ->
                            val customerNames = mutableListOf<String>()
                            val flockQuantities = mutableListOf<Int>()
                            val flockCosts = mutableListOf<Double>()

                            for (flockInventory in FlockInventoryReduction) {
                                customerNames.add(flockInventory.name)
                                flockQuantities.add(flockInventory.quantity)
                                flockCosts.add(flockInventory.cost)
                            }
                            flockReducedNameQuantityCostReportAdapter.setData(customerNames, flockQuantities, flockCosts)

                            val totalFlockQuantity = flockQuantities.sum()
                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                            val totalFlockCost = flockCosts.sum()
                            val sumOfFlockCost = "$currency $totalFlockCost"

                            tvTotalCustomerNameQuantityCostValueQuantity.text = sumOfFlockQuantity
                            tvTotalCustomerNameQuantityCostValueCost.text = sumOfFlockCost
                        }
                        }

                        true
                    }
                    R.id.miPastYear -> {
                        mFlockInventoryAdditionViewModel.getFlockQuantityForDate(thePastYear, theCurrentDate)
                        mFlockInventoryAdditionViewModel.flockQuantityForDate.observe(viewLifecycleOwner)
                        { flockAddition -> flockAddition.let { FlockInventoryAddition ->
                            val flockDates = mutableListOf<String>()
                            val flockQuantities = mutableListOf<Int>()

                            for (flockInventory in FlockInventoryAddition) {
                                flockDates.add(simpleDateFormat.format(flockInventory.date))
                                flockQuantities.add(flockInventory.quantity)
                            }
                            flockDateQuantityAcquiredReportAdapter.setData(flockDates, flockQuantities)

                            val totalFlockQuantity = flockQuantities.sum()
                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                            tvTotalFlockDateQuantityAcquiredValueQuantity.text = sumOfFlockQuantity
                        }
                        }

                        mFlockInventoryAdditionViewModel.getFlockQuantityCostForFlockPurpose(thePastYear, theCurrentDate)
                        mFlockInventoryAdditionViewModel.flockQuantityCostForFlockPurpose.observe(viewLifecycleOwner)
                        { flockAddition -> flockAddition.let { FlockInventoryAddition ->
                            val flockPurposes = mutableListOf<String>()
                            val flockQuantities = mutableListOf<Int>()
                            val flockCosts = mutableListOf<Double>()

                            for (flockInventory in FlockInventoryAddition) {
                                flockPurposes.add(flockInventory.name)
                                flockQuantities.add(flockInventory.quantity)
                                flockCosts.add(flockInventory.cost)
                            }
                            flockPurposeQuantityCostReportAdapter.setData(flockPurposes, flockQuantities, flockCosts)

                            val totalFlockQuantity = flockQuantities.sum()
                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                            val totalFlockCost = flockCosts.sum()
                            val sumOfFlockCost = "$currency $totalFlockCost"

                            tvTotalFlockPurposeQuantityCostValueQuantity.text = sumOfFlockQuantity
                            tvTotalFlockPurposeQuantityCostValueCost.text = sumOfFlockCost
                        }}

                        mFlockInventoryAdditionViewModel.getFlockQuantityCostForFlockName(thePastYear, theCurrentDate)
                        mFlockInventoryAdditionViewModel.flockQuantityCostForFlockName.observe(viewLifecycleOwner)
                        { flockAddition -> flockAddition.let { FlockInventoryAddition ->
                            val flockNames = mutableListOf<String>()
                            val flockQuantities = mutableListOf<Int>()
                            val flockCosts = mutableListOf<Double>()

                            for (flockInventory in FlockInventoryAddition) {
                                flockNames.add(flockInventory.name)
                                flockQuantities.add(flockInventory.quantity)
                                flockCosts.add(flockInventory.cost)
                            }
                            flockNameQuantityCostReportAdapter.setData(flockNames, flockQuantities, flockCosts)

                            val totalFlockQuantity = flockQuantities.sum()
                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                            val totalFlockCost = flockCosts.sum()
                            val sumOfFlockCost = "$currency $totalFlockCost"

                            tvTotalFlockNameQuantityCostValueQuantity.text = sumOfFlockQuantity
                            tvTotalFlockNameQuantityCostValueCost.text = sumOfFlockCost
                        }
                        }


                        mFlockInventoryReductionViewModel.getFlockQuantityForDate(thePastYear, theCurrentDate)
                        mFlockInventoryReductionViewModel.flockQuantityForDate.observe(viewLifecycleOwner)
                        { flockReduction -> flockReduction.let { FlockInventoryReduction ->
                            val flockDates = mutableListOf<String>()
                            val flockQuantities = mutableListOf<Int>()

                            for (flockInventory in FlockInventoryReduction) {
                                flockDates.add(simpleDateFormat.format(flockInventory.date))
                                flockQuantities.add(flockInventory.quantity)
                            }
                            flockDateQuantityReducedReportAdapter.setData(flockDates, flockQuantities)

                            val totalFlockQuantity = flockQuantities.sum()
                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                            tvTotalFlockDateQuantityReducedValueQuantity.text = sumOfFlockQuantity
                        }
                        }

                        mFlockInventoryReductionViewModel.getFlockQuantityForReductionReason(thePastYear, theCurrentDate)
                        mFlockInventoryReductionViewModel.flockQuantityForReductionReason.observe(viewLifecycleOwner)
                        { flockReduction -> flockReduction.let { FlockInventoryReduction ->
                            val reductionReasons = mutableListOf<String>()
                            val flockQuantities = mutableListOf<Int>()

                            for (flockInventory in FlockInventoryReduction) {
                                reductionReasons.add(flockInventory.name)
                                flockQuantities.add(flockInventory.quantity)
                            }
                            flockReductionReasonQuantityReportAdapter.setData(reductionReasons, flockQuantities)

                            val totalFlockQuantity = flockQuantities.sum()
                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                            tvTotalFlockReductionReasonQuantityValueQuantity.text = sumOfFlockQuantity
                        }
                        }

                        mFlockInventoryReductionViewModel.getFlockQuantityCostForFlockName(thePastYear, theCurrentDate)
                        mFlockInventoryReductionViewModel.flockQuantityCostForFlockName.observe(viewLifecycleOwner)
                        { flockReduction -> flockReduction.let { FlockInventoryReduction ->
                            val flockNames = mutableListOf<String>()
                            val flockQuantities = mutableListOf<Int>()
                            val flockCosts = mutableListOf<Double>()

                            for (flockInventory in FlockInventoryReduction) {
                                flockNames.add(flockInventory.name)
                                flockQuantities.add(flockInventory.quantity)
                                flockCosts.add(flockInventory.cost)
                            }
                            flockReducedNameQuantityCostReportAdapter.setData(flockNames, flockQuantities, flockCosts)

                            val totalFlockQuantity = flockQuantities.sum()
                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                            val totalFlockCost = flockCosts.sum()
                            val sumOfFlockCost = "$currency $totalFlockCost"

                            tvTotalFlockReducedNameQuantityCostValueQuantity.text = sumOfFlockQuantity
                            tvTotalFlockReducedNameQuantityCostValueCost.text = sumOfFlockCost
                        }
                        }

                        mFlockInventoryReductionViewModel.getFlockQuantityCostForCustomer(thePastYear, theCurrentDate)
                        mFlockInventoryReductionViewModel.flockQuantityCostForCustomer.observe(viewLifecycleOwner)
                        { flockReduction -> flockReduction.let { FlockInventoryReduction ->
                            val customerNames = mutableListOf<String>()
                            val flockQuantities = mutableListOf<Int>()
                            val flockCosts = mutableListOf<Double>()

                            for (flockInventory in FlockInventoryReduction) {
                                customerNames.add(flockInventory.name)
                                flockQuantities.add(flockInventory.quantity)
                                flockCosts.add(flockInventory.cost)
                            }
                            flockReducedNameQuantityCostReportAdapter.setData(customerNames, flockQuantities, flockCosts)

                            val totalFlockQuantity = flockQuantities.sum()
                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                            val totalFlockCost = flockCosts.sum()
                            val sumOfFlockCost = "$currency $totalFlockCost"

                            tvTotalCustomerNameQuantityCostValueQuantity.text = sumOfFlockQuantity
                            tvTotalCustomerNameQuantityCostValueCost.text = sumOfFlockCost
                        }
                        }

                        true
                    }
                    R.id.miPastTwoYears -> {
                        mFlockInventoryAdditionViewModel.getFlockQuantityForDate(thePastTwoYears, theCurrentDate)
                        mFlockInventoryAdditionViewModel.flockQuantityForDate.observe(viewLifecycleOwner)
                        { flockAddition -> flockAddition.let { FlockInventoryAddition ->
                            val flockDates = mutableListOf<String>()
                            val flockQuantities = mutableListOf<Int>()

                            for (flockInventory in FlockInventoryAddition) {
                                flockDates.add(simpleDateFormat.format(flockInventory.date))
                                flockQuantities.add(flockInventory.quantity)
                            }
                            flockDateQuantityAcquiredReportAdapter.setData(flockDates, flockQuantities)

                            val totalFlockQuantity = flockQuantities.sum()
                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                            tvTotalFlockDateQuantityAcquiredValueQuantity.text = sumOfFlockQuantity
                        }
                        }

                        mFlockInventoryAdditionViewModel.getFlockQuantityCostForFlockPurpose(thePastTwoYears, theCurrentDate)
                        mFlockInventoryAdditionViewModel.flockQuantityCostForFlockPurpose.observe(viewLifecycleOwner)
                        { flockAddition -> flockAddition.let { FlockInventoryAddition ->
                            val flockPurposes = mutableListOf<String>()
                            val flockQuantities = mutableListOf<Int>()
                            val flockCosts = mutableListOf<Double>()

                            for (flockInventory in FlockInventoryAddition) {
                                flockPurposes.add(flockInventory.name)
                                flockQuantities.add(flockInventory.quantity)
                                flockCosts.add(flockInventory.cost)
                            }
                            flockPurposeQuantityCostReportAdapter.setData(flockPurposes, flockQuantities, flockCosts)

                            val totalFlockQuantity = flockQuantities.sum()
                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                            val totalFlockCost = flockCosts.sum()
                            val sumOfFlockCost = "$currency $totalFlockCost"

                            tvTotalFlockPurposeQuantityCostValueQuantity.text = sumOfFlockQuantity
                            tvTotalFlockPurposeQuantityCostValueCost.text = sumOfFlockCost
                        }}

                        mFlockInventoryAdditionViewModel.getFlockQuantityCostForFlockName(thePastTwoYears, theCurrentDate)
                        mFlockInventoryAdditionViewModel.flockQuantityCostForFlockName.observe(viewLifecycleOwner)
                        { flockAddition -> flockAddition.let { FlockInventoryAddition ->
                            val flockNames = mutableListOf<String>()
                            val flockQuantities = mutableListOf<Int>()
                            val flockCosts = mutableListOf<Double>()

                            for (flockInventory in FlockInventoryAddition) {
                                flockNames.add(flockInventory.name)
                                flockQuantities.add(flockInventory.quantity)
                                flockCosts.add(flockInventory.cost)
                            }
                            flockNameQuantityCostReportAdapter.setData(flockNames, flockQuantities, flockCosts)

                            val totalFlockQuantity = flockQuantities.sum()
                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                            val totalFlockCost = flockCosts.sum()
                            val sumOfFlockCost = "$currency $totalFlockCost"

                            tvTotalFlockNameQuantityCostValueQuantity.text = sumOfFlockQuantity
                            tvTotalFlockNameQuantityCostValueCost.text = sumOfFlockCost
                        }
                        }


                        mFlockInventoryReductionViewModel.getFlockQuantityForDate(thePastTwoYears, theCurrentDate)
                        mFlockInventoryReductionViewModel.flockQuantityForDate.observe(viewLifecycleOwner)
                        { flockReduction -> flockReduction.let { FlockInventoryReduction ->
                            val flockDates = mutableListOf<String>()
                            val flockQuantities = mutableListOf<Int>()

                            for (flockInventory in FlockInventoryReduction) {
                                flockDates.add(simpleDateFormat.format(flockInventory.date))
                                flockQuantities.add(flockInventory.quantity)
                            }
                            flockDateQuantityReducedReportAdapter.setData(flockDates, flockQuantities)

                            val totalFlockQuantity = flockQuantities.sum()
                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                            tvTotalFlockDateQuantityReducedValueQuantity.text = sumOfFlockQuantity
                        }
                        }

                        mFlockInventoryReductionViewModel.getFlockQuantityForReductionReason(thePastTwoYears, theCurrentDate)
                        mFlockInventoryReductionViewModel.flockQuantityForReductionReason.observe(viewLifecycleOwner)
                        { flockReduction -> flockReduction.let { FlockInventoryReduction ->
                            val reductionReasons = mutableListOf<String>()
                            val flockQuantities = mutableListOf<Int>()

                            for (flockInventory in FlockInventoryReduction) {
                                reductionReasons.add(flockInventory.name)
                                flockQuantities.add(flockInventory.quantity)
                            }
                            flockReductionReasonQuantityReportAdapter.setData(reductionReasons, flockQuantities)

                            val totalFlockQuantity = flockQuantities.sum()
                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                            tvTotalFlockReductionReasonQuantityValueQuantity.text = sumOfFlockQuantity
                        }
                        }

                        mFlockInventoryReductionViewModel.getFlockQuantityCostForFlockName(thePastTwoYears, theCurrentDate)
                        mFlockInventoryReductionViewModel.flockQuantityCostForFlockName.observe(viewLifecycleOwner)
                        { flockReduction -> flockReduction.let { FlockInventoryReduction ->
                            val flockNames = mutableListOf<String>()
                            val flockQuantities = mutableListOf<Int>()
                            val flockCosts = mutableListOf<Double>()

                            for (flockInventory in FlockInventoryReduction) {
                                flockNames.add(flockInventory.name)
                                flockQuantities.add(flockInventory.quantity)
                                flockCosts.add(flockInventory.cost)
                            }
                            flockReducedNameQuantityCostReportAdapter.setData(flockNames, flockQuantities, flockCosts)

                            val totalFlockQuantity = flockQuantities.sum()
                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                            val totalFlockCost = flockCosts.sum()
                            val sumOfFlockCost = "$currency $totalFlockCost"

                            tvTotalFlockReducedNameQuantityCostValueQuantity.text = sumOfFlockQuantity
                            tvTotalFlockReducedNameQuantityCostValueCost.text = sumOfFlockCost
                        }
                        }

                        mFlockInventoryReductionViewModel.getFlockQuantityCostForCustomer(thePastTwoYears, theCurrentDate)
                        mFlockInventoryReductionViewModel.flockQuantityCostForCustomer.observe(viewLifecycleOwner)
                        { flockReduction -> flockReduction.let { FlockInventoryReduction ->
                            val customerNames = mutableListOf<String>()
                            val flockQuantities = mutableListOf<Int>()
                            val flockCosts = mutableListOf<Double>()

                            for (flockInventory in FlockInventoryReduction) {
                                customerNames.add(flockInventory.name)
                                flockQuantities.add(flockInventory.quantity)
                                flockCosts.add(flockInventory.cost)
                            }
                            flockReducedNameQuantityCostReportAdapter.setData(customerNames, flockQuantities, flockCosts)

                            val totalFlockQuantity = flockQuantities.sum()
                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                            val totalFlockCost = flockCosts.sum()
                            val sumOfFlockCost = "$currency $totalFlockCost"

                            tvTotalCustomerNameQuantityCostValueQuantity.text = sumOfFlockQuantity
                            tvTotalCustomerNameQuantityCostValueCost.text = sumOfFlockCost
                        }
                        }

                        true
                    }
                    R.id.miCustomDateRange -> {
                        // instantiate the calendar
                        val myCalendar = Calendar.getInstance()
                        Toast.makeText(context, "Custom Date", Toast.LENGTH_LONG).show()
                        val builder = AlertDialog.Builder(context, Gravity.END)
                        val inflater = layoutInflater
                        val dialogLayout = inflater.inflate(R.layout.custom_date_layout, null)
                        with(builder) {
                            val tvDatePickerFrom = dialogLayout.findViewById<TextView>(R.id.tvDatePickerFrom_CustomDateLayout)
                            val tvDatePickerTo = dialogLayout.findViewById<TextView>(R.id.tvDatePickerTo_CustomDateLayout)
                            val etListNumber = dialogLayout.findViewById<EditText>(R.id.etListNumber_CustomDateLayout)
                            // display and select the date
                            val datePickerFrom = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                                myCalendar.set(Calendar.YEAR, year)
                                myCalendar.set(Calendar.MONTH, month)
                                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                                updateDate(myCalendar, tvDatePickerFrom)
                            }
                            tvDatePickerFrom.setOnClickListener {
                                DatePickerDialog(
                                    requireContext(),
                                    datePickerFrom,
                                    myCalendar.get(Calendar.YEAR),
                                    myCalendar.get(
                                        Calendar.MONTH
                                    ),
                                    myCalendar.get(Calendar.DAY_OF_MONTH)
                                ).show()
                            }
                            val datePickerTo = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                                myCalendar.set(Calendar.YEAR, year)
                                myCalendar.set(Calendar.MONTH, month)
                                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                                updateDate(myCalendar, tvDatePickerTo)
                            }
                            tvDatePickerTo.setOnClickListener {
                                DatePickerDialog(
                                    requireContext(),
                                    datePickerTo,
                                    myCalendar.get(Calendar.YEAR),
                                    myCalendar.get(
                                        Calendar.MONTH
                                    ),
                                    myCalendar.get(Calendar.DAY_OF_MONTH)
                                ).show()
                            }
                            etListNumber.isGone = true

                            setTitle("Select the date range")
                            setPositiveButton("Ok") { dialog, where ->

                                if (checkIfEmpty(tvDatePickerFrom.text.toString())) {
                                    Toast.makeText(requireContext(), "Please Select Starting Date", Toast.LENGTH_LONG).show()
                                } else if (checkIfEmpty(tvDatePickerTo.text.toString())) {
                                    Toast.makeText(requireContext(), "Please Select Ending Date", Toast.LENGTH_LONG).show()
                                } else {
                                    val firstDate = simpleDateFormat.parse(tvDatePickerFrom.text.toString())
                                    val lastDate = simpleDateFormat.parse(tvDatePickerTo.text.toString())
                                    val theFirstDate = firstDate?.time?.let { java.sql.Date(it) }
                                    val theLastDate = lastDate?.time?.let { java.sql.Date(it) }

                                    if (theFirstDate != null) { if (theLastDate != null) {

                                        mFlockInventoryAdditionViewModel.getFlockQuantityForDate(theFirstDate, theLastDate)
                                        mFlockInventoryAdditionViewModel.flockQuantityForDate.observe(viewLifecycleOwner)
                                        { flockAddition -> flockAddition.let { FlockInventoryAddition ->
                                            val flockDates = mutableListOf<String>()
                                            val flockQuantities = mutableListOf<Int>()

                                            for (flockInventory in FlockInventoryAddition) {
                                                flockDates.add(simpleDateFormat.format(flockInventory.date))
                                                flockQuantities.add(flockInventory.quantity)
                                            }
                                            flockDateQuantityAcquiredReportAdapter.setData(flockDates, flockQuantities)

                                            val totalFlockQuantity = flockQuantities.sum()
                                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                                            tvTotalFlockDateQuantityAcquiredValueQuantity.text = sumOfFlockQuantity
                                        }
                                        }

                                        mFlockInventoryAdditionViewModel.getFlockQuantityCostForFlockPurpose(theFirstDate, theLastDate)
                                        mFlockInventoryAdditionViewModel.flockQuantityCostForFlockPurpose.observe(viewLifecycleOwner)
                                        { flockAddition -> flockAddition.let { FlockInventoryAddition ->
                                            val flockPurposes = mutableListOf<String>()
                                            val flockQuantities = mutableListOf<Int>()
                                            val flockCosts = mutableListOf<Double>()

                                            for (flockInventory in FlockInventoryAddition) {
                                                flockPurposes.add(flockInventory.name)
                                                flockQuantities.add(flockInventory.quantity)
                                                flockCosts.add(flockInventory.cost)
                                            }
                                            flockPurposeQuantityCostReportAdapter.setData(flockPurposes, flockQuantities, flockCosts)

                                            val totalFlockQuantity = flockQuantities.sum()
                                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                                            val totalFlockCost = flockCosts.sum()
                                            val sumOfFlockCost = "$currency $totalFlockCost"

                                            tvTotalFlockPurposeQuantityCostValueQuantity.text = sumOfFlockQuantity
                                            tvTotalFlockPurposeQuantityCostValueCost.text = sumOfFlockCost
                                        }}

                                        mFlockInventoryAdditionViewModel.getFlockQuantityCostForFlockName(theFirstDate, theLastDate)
                                        mFlockInventoryAdditionViewModel.flockQuantityCostForFlockName.observe(viewLifecycleOwner)
                                        { flockAddition -> flockAddition.let { FlockInventoryAddition ->
                                            val flockNames = mutableListOf<String>()
                                            val flockQuantities = mutableListOf<Int>()
                                            val flockCosts = mutableListOf<Double>()

                                            for (flockInventory in FlockInventoryAddition) {
                                                flockNames.add(flockInventory.name)
                                                flockQuantities.add(flockInventory.quantity)
                                                flockCosts.add(flockInventory.cost)
                                            }
                                            flockNameQuantityCostReportAdapter.setData(flockNames, flockQuantities, flockCosts)

                                            val totalFlockQuantity = flockQuantities.sum()
                                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                                            val totalFlockCost = flockCosts.sum()
                                            val sumOfFlockCost = "$currency $totalFlockCost"

                                            tvTotalFlockNameQuantityCostValueQuantity.text = sumOfFlockQuantity
                                            tvTotalFlockNameQuantityCostValueCost.text = sumOfFlockCost
                                        }
                                        }


                                        mFlockInventoryReductionViewModel.getFlockQuantityForDate(theFirstDate, theLastDate)
                                        mFlockInventoryReductionViewModel.flockQuantityForDate.observe(viewLifecycleOwner)
                                        { flockReduction -> flockReduction.let { FlockInventoryReduction ->
                                            val flockDates = mutableListOf<String>()
                                            val flockQuantities = mutableListOf<Int>()

                                            for (flockInventory in FlockInventoryReduction) {
                                                flockDates.add(simpleDateFormat.format(flockInventory.date))
                                                flockQuantities.add(flockInventory.quantity)
                                            }
                                            flockDateQuantityReducedReportAdapter.setData(flockDates, flockQuantities)

                                            val totalFlockQuantity = flockQuantities.sum()
                                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                                            tvTotalFlockDateQuantityReducedValueQuantity.text = sumOfFlockQuantity
                                        }
                                        }

                                        mFlockInventoryReductionViewModel.getFlockQuantityForReductionReason(theFirstDate, theLastDate)
                                        mFlockInventoryReductionViewModel.flockQuantityForReductionReason.observe(viewLifecycleOwner)
                                        { flockReduction -> flockReduction.let { FlockInventoryReduction ->
                                            val reductionReasons = mutableListOf<String>()
                                            val flockQuantities = mutableListOf<Int>()

                                            for (flockInventory in FlockInventoryReduction) {
                                                reductionReasons.add(flockInventory.name)
                                                flockQuantities.add(flockInventory.quantity)
                                            }
                                            flockReductionReasonQuantityReportAdapter.setData(reductionReasons, flockQuantities)

                                            val totalFlockQuantity = flockQuantities.sum()
                                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                                            tvTotalFlockReductionReasonQuantityValueQuantity.text = sumOfFlockQuantity
                                        }
                                        }

                                        mFlockInventoryReductionViewModel.getFlockQuantityCostForFlockName(theFirstDate, theLastDate)
                                        mFlockInventoryReductionViewModel.flockQuantityCostForFlockName.observe(viewLifecycleOwner)
                                        { flockReduction -> flockReduction.let { FlockInventoryReduction ->
                                            val flockNames = mutableListOf<String>()
                                            val flockQuantities = mutableListOf<Int>()
                                            val flockCosts = mutableListOf<Double>()

                                            for (flockInventory in FlockInventoryReduction) {
                                                flockNames.add(flockInventory.name)
                                                flockQuantities.add(flockInventory.quantity)
                                                flockCosts.add(flockInventory.cost)
                                            }
                                            flockReducedNameQuantityCostReportAdapter.setData(flockNames, flockQuantities, flockCosts)

                                            val totalFlockQuantity = flockQuantities.sum()
                                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                                            val totalFlockCost = flockCosts.sum()
                                            val sumOfFlockCost = "$currency $totalFlockCost"

                                            tvTotalFlockReducedNameQuantityCostValueQuantity.text = sumOfFlockQuantity
                                            tvTotalFlockReducedNameQuantityCostValueCost.text = sumOfFlockCost
                                        }
                                        }

                                        mFlockInventoryReductionViewModel.getFlockQuantityCostForCustomer(theFirstDate, theLastDate)
                                        mFlockInventoryReductionViewModel.flockQuantityCostForCustomer.observe(viewLifecycleOwner)
                                        { flockReduction -> flockReduction.let { FlockInventoryReduction ->
                                            val customerNames = mutableListOf<String>()
                                            val flockQuantities = mutableListOf<Int>()
                                            val flockCosts = mutableListOf<Double>()

                                            for (flockInventory in FlockInventoryReduction) {
                                                customerNames.add(flockInventory.name)
                                                flockQuantities.add(flockInventory.quantity)
                                                flockCosts.add(flockInventory.cost)
                                            }
                                            flockReducedNameQuantityCostReportAdapter.setData(customerNames, flockQuantities, flockCosts)

                                            val totalFlockQuantity = flockQuantities.sum()
                                            val sumOfFlockQuantity = "$totalFlockQuantity birds"

                                            val totalFlockCost = flockCosts.sum()
                                            val sumOfFlockCost = "$currency $totalFlockCost"

                                            tvTotalCustomerNameQuantityCostValueQuantity.text = sumOfFlockQuantity
                                            tvTotalCustomerNameQuantityCostValueCost.text = sumOfFlockCost
                                        }
                                        }

                                    }

                                    }
                                }
                            }
                            setNegativeButton("Cancel") { dialog, which ->
                                Toast.makeText(requireContext(), "Date range not set", Toast.LENGTH_LONG).show()
                            }
                            setView(dialogLayout)
                            show()
                        }

                        true
                    }
                    else -> true
                }
            }
            popupDateFilterMenu.show()
            true
        }

        flockReductionReasonQuantityReportAdapter.setOnItemClickListener(object : DateQuantityReportAdapter.OnItemClickListener{
            override fun onItemDateClickListener(date: String) {
                TODO("Not yet implemented")
            }

            override fun onItemQuantityClickListener(quantity: Int) {
                TODO("Not yet implemented")
            }
        })
        flockDateQuantityAcquiredReportAdapter.setOnItemClickListener(object : DateQuantityReportAdapter.OnItemClickListener{
            override fun onItemDateClickListener(date: String) {
                TODO("Not yet implemented")
            }

            override fun onItemQuantityClickListener(quantity: Int) {
                TODO("Not yet implemented")
            }
        })
        flockDateQuantityReducedReportAdapter.setOnItemClickListener(object : DateQuantityReportAdapter.OnItemClickListener{
            override fun onItemDateClickListener(date: String) {
                TODO("Not yet implemented")
            }

            override fun onItemQuantityClickListener(quantity: Int) {
                TODO("Not yet implemented")
            }

        })

        return view
    }

    private fun updateDate(myCalendar: Calendar, tvDatePicker: TextView) {
        val myFormat = "dd-MM-yyyy"
        val simpleDateFormat = SimpleDateFormat(myFormat, Locale.UK)
        tvDatePicker.text = simpleDateFormat.format(myCalendar.time)
    }
    private fun checkIfEmpty(value: String): Boolean {
        return (TextUtils.isEmpty(value))
    }
}