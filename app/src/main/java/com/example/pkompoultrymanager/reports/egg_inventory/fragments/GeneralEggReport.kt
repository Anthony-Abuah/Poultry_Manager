package com.example.pkompoultrymanager.reports.egg_inventory.fragments

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
import com.example.pkompoultrymanager.inventory.eggs.addition.EggInventoryAdditionViewModel
import com.example.pkompoultrymanager.inventory.eggs.reduction.EggInventoryReductionViewModel
import com.example.pkompoultrymanager.inventory.flock.addition.FlockInventoryAdditionViewModel
import com.example.pkompoultrymanager.reports.DateQuantityReportAdapter
import com.example.pkompoultrymanager.tables.egg_type.EggType
import com.example.pkompoultrymanager.tables.egg_type.EggTypeViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class GeneralEggReport : Fragment() {
    private lateinit var mEggInventoryAdditionViewModel: EggInventoryAdditionViewModel
    private lateinit var mEggInventoryReductionViewModel: EggInventoryReductionViewModel
    private lateinit var mFlockInventoryAdditionViewModel: FlockInventoryAdditionViewModel
    private lateinit var mEggTypeViewModel: EggTypeViewModel

    private lateinit var myFarmInfo: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_general_egg_report, container, false)

        mEggInventoryAdditionViewModel =
            ViewModelProvider(this)[EggInventoryAdditionViewModel::class.java]
        mEggInventoryReductionViewModel =
            ViewModelProvider(this)[EggInventoryReductionViewModel::class.java]
        mFlockInventoryAdditionViewModel =
            ViewModelProvider(this)[FlockInventoryAdditionViewModel::class.java]
        mEggTypeViewModel = ViewModelProvider(this)[EggTypeViewModel::class.java]

        val numberOfList = 100
        val myFormat = "dd-MM-yyyy"
        val simpleDateFormat = SimpleDateFormat(myFormat, Locale.UK)

        val date: LocalDate = LocalDate.now()
        val today: LocalDate = LocalDate.now().minusDays(1)
        val week: LocalDate = LocalDate.now().minusWeeks(1)
        val month: LocalDate = LocalDate.now().minusDays(LocalDate.now().dayOfMonth.toLong())
        val threeMonths: LocalDate = LocalDate.now().minusMonths(3)
        val sixMonths: LocalDate = LocalDate.now().minusMonths(6)
        val year: LocalDate = LocalDate.now().minusYears(1)
        val twoYears: LocalDate = LocalDate.now().minusYears(2)

        val theCurrentDate = java.sql.Date.valueOf(date.toString())
        val thePreviousDay = java.sql.Date.valueOf(today.toString())
        val thePreviousWeek = java.sql.Date.valueOf(week.toString())
        val thePreviousMonth = java.sql.Date.valueOf(month.toString())
        val thePastThreeMonths = java.sql.Date.valueOf(threeMonths.toString())
        val thePastSixMonths = java.sql.Date.valueOf(sixMonths.toString())
        val thePastYear = java.sql.Date.valueOf(year.toString())
        val thePastTwoYears = java.sql.Date.valueOf(twoYears.toString())

        val dateFilter = view.findViewById<ImageButton>(R.id.ibDateFilter_GeneralEggReport)

        val tvSummary = view. findViewById<TextView>(R.id.tvEggReductionSummary_GeneralEggReport)
        val tvEggInventoryAddition = view. findViewById<TextView>(R.id.tvEggAdditions_GeneralEggReport)
        val tvEggInventoryReduction = view. findViewById<TextView>(R.id.tvEggReductions_GeneralEggReport)

        val llSummary = view. findViewById<LinearLayout>(R.id.llSummary_GeneralEggReport)
        val llEggInventoryAddition = view. findViewById<LinearLayout>(R.id.llEggInventoryAdditions_GeneralEggReport)
        val llEggInventoryReduction = view. findViewById<LinearLayout>(R.id.llEggInventoryReductions_GeneralEggReport)

        val tvTotalEggsAdded = view. findViewById<TextView>(R.id.tvTotalAdditionQuantityValue_DateQuantityLayout)
        val tvTotalEggsReduced = view. findViewById<TextView>(R.id.tvTotalReductionQuantityValue_DateQuantityLayout)
        val tvTotalReductionReasonEggsReduced = view. findViewById<TextView>(R.id.tvReductionReasonQuantityValue_DateQuantityLayout)

        myFarmInfo = context?.getSharedPreferences("MyFarmInfo", Context.MODE_PRIVATE)!!
        val numberOfEggsPerCrates = myFarmInfo.getInt("numberOfEggsPerCrate", 30)

        val eggReductionReasonReport =
            view.findViewById<RecyclerView>(R.id.rvEggReductionReasonReport_GeneralEggReport)
        val eggInventoryAdditionReport =
            view.findViewById<RecyclerView>(R.id.rvEggInventoryAdditionReport_GeneralEggReport)
        val eggInventoryReductionReport =
            view.findViewById<RecyclerView>(R.id.rvEggInventoryReductionReport_GeneralEggReport)

        val eggReductionReasonReportAdapter = DateQuantityReportAdapter(numberOfEggsPerCrates)
        val eggInventoryAdditionReportAdapter = DateQuantityReportAdapter(numberOfEggsPerCrates)
        val eggInventoryReductionReportAdapter = DateQuantityReportAdapter(numberOfEggsPerCrates)

        eggReductionReasonReport.adapter = eggReductionReasonReportAdapter
        eggInventoryAdditionReport.adapter = eggInventoryAdditionReportAdapter
        eggInventoryReductionReport.adapter = eggInventoryReductionReportAdapter

        eggReductionReasonReport.layoutManager = LinearLayoutManager(requireContext())
        eggInventoryAdditionReport.layoutManager = LinearLayoutManager(requireContext())
        eggInventoryReductionReport.layoutManager = LinearLayoutManager(requireContext())

        tvSummary.setOnClickListener {
            val popupHideAndShowMenu = PopupMenu(requireContext(), tvSummary, Gravity.END)
            popupHideAndShowMenu.inflate(R.menu.hide_show_menu)
            popupHideAndShowMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.miShow -> {
                        llSummary.isGone = false
                        true
                    }
                    R.id.miHide -> {
                        llSummary.isGone = true
                        true
                    }
                else -> true
                }
            }
            popupHideAndShowMenu.show()
            true
        }
        tvEggInventoryAddition.setOnClickListener {
            val popupHideAndShowMenu = PopupMenu(requireContext(), tvEggInventoryAddition, Gravity.END)
            popupHideAndShowMenu.inflate(R.menu.hide_show_menu)
            popupHideAndShowMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.miShow -> {
                        llEggInventoryAddition.isGone = false
                        true
                    }
                    R.id.miHide -> {
                        llEggInventoryAddition.isGone = true
                        true
                    }
                else -> true
                }
            }
            popupHideAndShowMenu.show()
            true
        }
        tvEggInventoryReduction.setOnClickListener {
            val popupHideAndShowMenu = PopupMenu(requireContext(), tvEggInventoryReduction, Gravity.END)
            popupHideAndShowMenu.inflate(R.menu.hide_show_menu)
            popupHideAndShowMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.miShow -> {
                        llEggInventoryReduction.isGone = false
                        true
                    }
                    R.id.miHide -> {
                        llEggInventoryReduction.isGone = true
                        true
                    }
                else -> true
                }
            }
            popupHideAndShowMenu.show()
            true
        }

        mEggInventoryAdditionViewModel.getEggAdditionsByDate(thePreviousMonth, theCurrentDate)
        mEggInventoryAdditionViewModel.eggAdditionsByDate.observe(viewLifecycleOwner)
        { eggAdditions -> eggAdditions.let { eggInventoryAddition ->
            val everyEggInventoryAdditionDate = mutableListOf<String>()
            val everyEggInventoryAdditionQuantity = mutableListOf<Int>()
            for (additionDates in eggInventoryAddition){
                everyEggInventoryAdditionDate.add(simpleDateFormat.format(additionDates.date))
                everyEggInventoryAdditionQuantity.add(additionDates.quantity)}

            eggInventoryAdditionReportAdapter.setData(everyEggInventoryAdditionDate, everyEggInventoryAdditionQuantity)

            val totalEggInventoryAdditions = everyEggInventoryAdditionQuantity.sum()
            val numberOfEggCrates = totalEggInventoryAdditions.div(numberOfEggsPerCrates)
            val numberOfEggsRemaining = totalEggInventoryAdditions.rem(numberOfEggsPerCrates)
            val totalEggsAddedString = "$numberOfEggCrates crates, $numberOfEggsRemaining eggs"
            tvTotalEggsAdded.text = totalEggsAddedString
        }}

        mEggInventoryReductionViewModel.getEggReductionsByDate(thePreviousMonth, theCurrentDate)
        mEggInventoryReductionViewModel.eggReductionsByDate.observe(viewLifecycleOwner)
        { eggReduction -> eggReduction.let { EggInventoryReduction ->
            //declare the lists
            val everyEggInventoryReductionQuantity = mutableListOf<Int>()
            val everyEggInventoryReductionDate = mutableListOf<String>()
            for (eggReductions in EggInventoryReduction){
                everyEggInventoryReductionDate.add(simpleDateFormat.format(eggReductions.date))
                everyEggInventoryReductionQuantity.add(eggReductions.quantity)
            }
            eggInventoryReductionReportAdapter.setData(everyEggInventoryReductionDate, everyEggInventoryReductionQuantity)

            val totalEggInventoryReduction = everyEggInventoryReductionQuantity.sum()
            val numberOfEggCrates = totalEggInventoryReduction.div(numberOfEggsPerCrates)
            val numberOfEggsRemaining = totalEggInventoryReduction.rem(numberOfEggsPerCrates)
            val totalEggsReducedString = "$numberOfEggCrates crates, $numberOfEggsRemaining eggs"
            tvTotalEggsReduced.text = totalEggsReducedString

        }}

        mEggInventoryReductionViewModel.getEggReductionsByReductionReason(thePreviousMonth, theCurrentDate)
        mEggInventoryReductionViewModel.eggReductionsByReductionReason.observe(viewLifecycleOwner)
        { eggReduction -> eggReduction.let { EggInventoryReduction ->
            //declare the lists
            var totalEggReduction:Int = 0
            val everyEggInventoryReductionQuantity = mutableListOf<Int>()
            val everyEggInventoryReductionReason = mutableListOf<String>()
            for (eggReductions in EggInventoryReduction){
                everyEggInventoryReductionReason.add(eggReductions.name)
                everyEggInventoryReductionQuantity.add(eggReductions.quantity)
            }
            eggReductionReasonReportAdapter.setData(everyEggInventoryReductionReason, everyEggInventoryReductionQuantity)

            val totalEggInventoryReduction = everyEggInventoryReductionQuantity.sum()
            val numberOfEggCrates = totalEggInventoryReduction.div(numberOfEggsPerCrates)
            val numberOfEggsRemaining = totalEggInventoryReduction.rem(numberOfEggsPerCrates)
            val totalEggsReducedString = "$numberOfEggCrates crates, $numberOfEggsRemaining eggs"
            tvTotalReductionReasonEggsReduced.text = totalEggsReducedString
        }}


        dateFilter.setOnClickListener {
            val popupDateFilterMenu = PopupMenu(requireContext(), dateFilter)
            popupDateFilterMenu.inflate(R.menu.date_ranges)
            popupDateFilterMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.miAllTime -> {
                        mEggInventoryAdditionViewModel.allTheEggAdditionsByDate.observe(viewLifecycleOwner)
                        { eggAdditions -> eggAdditions.let { eggInventoryAddition ->
                            val everyEggInventoryAdditionDate = mutableListOf<String>()
                            val everyEggInventoryAdditionQuantity = mutableListOf<Int>()
                            for (additionDates in eggInventoryAddition){
                                everyEggInventoryAdditionDate.add(simpleDateFormat.format(additionDates.date))
                                everyEggInventoryAdditionQuantity.add(additionDates.quantity)}

                            eggInventoryAdditionReportAdapter.setData(everyEggInventoryAdditionDate, everyEggInventoryAdditionQuantity)

                            val totalEggInventoryAdditions = everyEggInventoryAdditionQuantity.sum()
                            val numberOfEggCrates = totalEggInventoryAdditions.div(numberOfEggsPerCrates)
                            val numberOfEggsRemaining = totalEggInventoryAdditions.rem(numberOfEggsPerCrates)
                            val totalEggsAddedString = "$numberOfEggCrates crates, $numberOfEggsRemaining eggs"
                            tvTotalEggsAdded.text = totalEggsAddedString
                        }}

                        mEggInventoryReductionViewModel.allTheEggReductionsByDate.observe(viewLifecycleOwner)
                        { eggReduction -> eggReduction.let { EggInventoryReduction ->
                            //declare the lists
                            var totalEggReduction:Int = 0
                            val everyEggInventoryReductionQuantity = mutableListOf<Int>()
                            val everyEggInventoryReductionDate = mutableListOf<String>()
                            for (eggReductions in EggInventoryReduction){
                                everyEggInventoryReductionDate.add(simpleDateFormat.format(eggReductions.date))
                                everyEggInventoryReductionQuantity.add(eggReductions.quantity)
                            }
                            eggInventoryReductionReportAdapter.setData(everyEggInventoryReductionDate, everyEggInventoryReductionQuantity)

                            val totalEggInventoryReduction = everyEggInventoryReductionQuantity.sum()
                            val numberOfEggCrates = totalEggInventoryReduction.div(numberOfEggsPerCrates)
                            val numberOfEggsRemaining = totalEggInventoryReduction.rem(numberOfEggsPerCrates)
                            val totalEggsReducedString = "$numberOfEggCrates crates, $numberOfEggsRemaining eggs"
                            tvTotalEggsReduced.text = totalEggsReducedString

                            }}

                        mEggInventoryReductionViewModel.allTheEggReductionsByReductionReason.observe(viewLifecycleOwner)
                        { eggReduction -> eggReduction.let { EggInventoryReduction ->
                            //declare the lists
                            var totalEggReduction:Int = 0
                            val everyEggInventoryReductionQuantity = mutableListOf<Int>()
                            val everyEggInventoryReductionReason = mutableListOf<String>()
                            for (eggReductions in EggInventoryReduction){
                                everyEggInventoryReductionReason.add(eggReductions.name)
                                everyEggInventoryReductionQuantity.add(eggReductions.quantity)
                            }
                            eggReductionReasonReportAdapter.setData(everyEggInventoryReductionReason, everyEggInventoryReductionQuantity)

                            val totalEggInventoryReduction = everyEggInventoryReductionQuantity.sum()
                            val numberOfEggCrates = totalEggInventoryReduction.div(numberOfEggsPerCrates)
                            val numberOfEggsRemaining = totalEggInventoryReduction.rem(numberOfEggsPerCrates)
                            val totalEggsReducedString = "$numberOfEggCrates crates, $numberOfEggsRemaining eggs"
                            tvTotalReductionReasonEggsReduced.text = totalEggsReducedString
                    }}

                        true
                    }
                    R.id.miToday -> {
                        mEggInventoryAdditionViewModel.getEggAdditionsByDate(theCurrentDate, theCurrentDate)
                        mEggInventoryAdditionViewModel.eggAdditionsByDate.observe(viewLifecycleOwner)
                        { eggAdditions -> eggAdditions.let { eggInventoryAddition ->
                            val everyEggInventoryAdditionDate = mutableListOf<String>()
                            val everyEggInventoryAdditionQuantity = mutableListOf<Int>()
                            for (additionDates in eggInventoryAddition){
                                everyEggInventoryAdditionDate.add(simpleDateFormat.format(additionDates.date))
                                everyEggInventoryAdditionQuantity.add(additionDates.quantity)}

                            eggInventoryAdditionReportAdapter.setData(everyEggInventoryAdditionDate, everyEggInventoryAdditionQuantity)

                            val totalEggInventoryAdditions = everyEggInventoryAdditionQuantity.sum()
                            val numberOfEggCrates = totalEggInventoryAdditions.div(numberOfEggsPerCrates)
                            val numberOfEggsRemaining = totalEggInventoryAdditions.rem(numberOfEggsPerCrates)
                            val totalEggsAddedString = "$numberOfEggCrates crates, $numberOfEggsRemaining eggs"
                            tvTotalEggsAdded.text = totalEggsAddedString
                        }}

                        mEggInventoryReductionViewModel.getEggReductionsByDate(theCurrentDate, theCurrentDate)
                        mEggInventoryReductionViewModel.eggReductionsByDate.observe(viewLifecycleOwner)
                        { eggReduction -> eggReduction.let { EggInventoryReduction ->
                            //declare the lists
                            var totalEggReduction:Int = 0
                            val everyEggInventoryReductionQuantity = mutableListOf<Int>()
                            val everyEggInventoryReductionDate = mutableListOf<String>()
                            for (eggReductions in EggInventoryReduction){
                                everyEggInventoryReductionDate.add(simpleDateFormat.format(eggReductions.date))
                                everyEggInventoryReductionQuantity.add(eggReductions.quantity)
                            }
                            eggInventoryReductionReportAdapter.setData(everyEggInventoryReductionDate, everyEggInventoryReductionQuantity)

                            val totalEggInventoryReduction = everyEggInventoryReductionQuantity.sum()
                            val numberOfEggCrates = totalEggInventoryReduction.div(numberOfEggsPerCrates)
                            val numberOfEggsRemaining = totalEggInventoryReduction.rem(numberOfEggsPerCrates)
                            val totalEggsReducedString = "$numberOfEggCrates crates, $numberOfEggsRemaining eggs"
                            tvTotalEggsReduced.text = totalEggsReducedString

                        }}

                        mEggInventoryReductionViewModel.getEggReductionsByReductionReason(theCurrentDate, theCurrentDate)
                        mEggInventoryReductionViewModel.eggReductionsByReductionReason.observe(viewLifecycleOwner)
                        { eggReduction -> eggReduction.let { EggInventoryReduction ->
                            //declare the lists
                            var totalEggReduction:Int = 0
                            val everyEggInventoryReductionQuantity = mutableListOf<Int>()
                            val everyEggInventoryReductionReason = mutableListOf<String>()
                            for (eggReductions in EggInventoryReduction){
                                everyEggInventoryReductionReason.add(eggReductions.name)
                                everyEggInventoryReductionQuantity.add(eggReductions.quantity)
                            }
                            eggReductionReasonReportAdapter.setData(everyEggInventoryReductionReason, everyEggInventoryReductionQuantity)

                            val totalEggInventoryReduction = everyEggInventoryReductionQuantity.sum()
                            val numberOfEggCrates = totalEggInventoryReduction.div(numberOfEggsPerCrates)
                            val numberOfEggsRemaining = totalEggInventoryReduction.rem(numberOfEggsPerCrates)
                            val totalEggsReducedString = "$numberOfEggCrates crates, $numberOfEggsRemaining eggs"
                            tvTotalReductionReasonEggsReduced.text = totalEggsReducedString
                        }}

                        true
                    }
                    R.id.miThisWeek -> {
                        mEggInventoryAdditionViewModel.getEggAdditionsByDate(thePreviousWeek, theCurrentDate)
                        mEggInventoryAdditionViewModel.eggAdditionsByDate.observe(viewLifecycleOwner)
                        { eggAdditions -> eggAdditions.let { eggInventoryAddition ->
                            val everyEggInventoryAdditionDate = mutableListOf<String>()
                            val everyEggInventoryAdditionQuantity = mutableListOf<Int>()
                            for (additionDates in eggInventoryAddition){
                                everyEggInventoryAdditionDate.add(simpleDateFormat.format(additionDates.date))
                                everyEggInventoryAdditionQuantity.add(additionDates.quantity)}

                            eggInventoryAdditionReportAdapter.setData(everyEggInventoryAdditionDate, everyEggInventoryAdditionQuantity)

                            val totalEggInventoryAdditions = everyEggInventoryAdditionQuantity.sum()
                            val numberOfEggCrates = totalEggInventoryAdditions.div(numberOfEggsPerCrates)
                            val numberOfEggsRemaining = totalEggInventoryAdditions.rem(numberOfEggsPerCrates)
                            val totalEggsAddedString = "$numberOfEggCrates crates, $numberOfEggsRemaining eggs"
                            tvTotalEggsAdded.text = totalEggsAddedString
                        }}

                        mEggInventoryReductionViewModel.getEggReductionsByDate(thePreviousWeek, theCurrentDate)
                        mEggInventoryReductionViewModel.eggReductionsByDate.observe(viewLifecycleOwner)
                        { eggReduction -> eggReduction.let { EggInventoryReduction ->
                            //declare the lists
                            var totalEggReduction:Int = 0
                            val everyEggInventoryReductionQuantity = mutableListOf<Int>()
                            val everyEggInventoryReductionDate = mutableListOf<String>()
                            for (eggReductions in EggInventoryReduction){
                                everyEggInventoryReductionDate.add(simpleDateFormat.format(eggReductions.date))
                                everyEggInventoryReductionQuantity.add(eggReductions.quantity)
                            }
                            eggInventoryReductionReportAdapter.setData(everyEggInventoryReductionDate, everyEggInventoryReductionQuantity)

                            val totalEggInventoryReduction = everyEggInventoryReductionQuantity.sum()
                            val numberOfEggCrates = totalEggInventoryReduction.div(numberOfEggsPerCrates)
                            val numberOfEggsRemaining = totalEggInventoryReduction.rem(numberOfEggsPerCrates)
                            val totalEggsReducedString = "$numberOfEggCrates crates, $numberOfEggsRemaining eggs"
                            tvTotalEggsReduced.text = totalEggsReducedString

                        }}

                        mEggInventoryReductionViewModel.getEggReductionsByReductionReason(thePreviousWeek, theCurrentDate)
                        mEggInventoryReductionViewModel.eggReductionsByReductionReason.observe(viewLifecycleOwner)
                        { eggReduction -> eggReduction.let { EggInventoryReduction ->
                            //declare the lists
                            var totalEggReduction:Int = 0
                            val everyEggInventoryReductionQuantity = mutableListOf<Int>()
                            val everyEggInventoryReductionReason = mutableListOf<String>()
                            for (eggReductions in EggInventoryReduction){
                                everyEggInventoryReductionReason.add(eggReductions.name)
                                everyEggInventoryReductionQuantity.add(eggReductions.quantity)
                            }
                            eggReductionReasonReportAdapter.setData(everyEggInventoryReductionReason, everyEggInventoryReductionQuantity)

                            val totalEggInventoryReduction = everyEggInventoryReductionQuantity.sum()
                            val numberOfEggCrates = totalEggInventoryReduction.div(numberOfEggsPerCrates)
                            val numberOfEggsRemaining = totalEggInventoryReduction.rem(numberOfEggsPerCrates)
                            val totalEggsReducedString = "$numberOfEggCrates crates, $numberOfEggsRemaining eggs"
                            tvTotalReductionReasonEggsReduced.text = totalEggsReducedString
                        }}

                        true
                    }
                    R.id.miThisMonth -> {
                        mEggInventoryAdditionViewModel.getEggAdditionsByDate(thePreviousMonth, theCurrentDate)
                        mEggInventoryAdditionViewModel.eggAdditionsByDate.observe(viewLifecycleOwner)
                        { eggAdditions -> eggAdditions.let { eggInventoryAddition ->
                            val everyEggInventoryAdditionDate = mutableListOf<String>()
                            val everyEggInventoryAdditionQuantity = mutableListOf<Int>()
                            for (additionDates in eggInventoryAddition){
                                everyEggInventoryAdditionDate.add(simpleDateFormat.format(additionDates.date))
                                everyEggInventoryAdditionQuantity.add(additionDates.quantity)}

                            eggInventoryAdditionReportAdapter.setData(everyEggInventoryAdditionDate, everyEggInventoryAdditionQuantity)

                            val totalEggInventoryAdditions = everyEggInventoryAdditionQuantity.sum()
                            val numberOfEggCrates = totalEggInventoryAdditions.div(numberOfEggsPerCrates)
                            val numberOfEggsRemaining = totalEggInventoryAdditions.rem(numberOfEggsPerCrates)
                            val totalEggsAddedString = "$numberOfEggCrates crates, $numberOfEggsRemaining eggs"
                            tvTotalEggsAdded.text = totalEggsAddedString
                        }}

                        mEggInventoryReductionViewModel.getEggReductionsByDate(thePreviousMonth, theCurrentDate)
                        mEggInventoryReductionViewModel.eggReductionsByDate.observe(viewLifecycleOwner)
                        { eggReduction -> eggReduction.let { EggInventoryReduction ->
                            //declare the lists
                            var totalEggReduction:Int = 0
                            val everyEggInventoryReductionQuantity = mutableListOf<Int>()
                            val everyEggInventoryReductionDate = mutableListOf<String>()
                            for (eggReductions in EggInventoryReduction){
                                everyEggInventoryReductionDate.add(simpleDateFormat.format(eggReductions.date))
                                everyEggInventoryReductionQuantity.add(eggReductions.quantity)
                            }
                            eggInventoryReductionReportAdapter.setData(everyEggInventoryReductionDate, everyEggInventoryReductionQuantity)

                            val totalEggInventoryReduction = everyEggInventoryReductionQuantity.sum()
                            val numberOfEggCrates = totalEggInventoryReduction.div(numberOfEggsPerCrates)
                            val numberOfEggsRemaining = totalEggInventoryReduction.rem(numberOfEggsPerCrates)
                            val totalEggsReducedString = "$numberOfEggCrates crates, $numberOfEggsRemaining eggs"
                            tvTotalEggsReduced.text = totalEggsReducedString

                        }}

                        mEggInventoryReductionViewModel.getEggReductionsByReductionReason(thePreviousMonth, theCurrentDate)
                        mEggInventoryReductionViewModel.eggReductionsByReductionReason.observe(viewLifecycleOwner)
                        { eggReduction -> eggReduction.let { EggInventoryReduction ->
                            //declare the lists
                            var totalEggReduction:Int = 0
                            val everyEggInventoryReductionQuantity = mutableListOf<Int>()
                            val everyEggInventoryReductionReason = mutableListOf<String>()
                            for (eggReductions in EggInventoryReduction){
                                everyEggInventoryReductionReason.add(eggReductions.name)
                                everyEggInventoryReductionQuantity.add(eggReductions.quantity)
                            }
                            eggReductionReasonReportAdapter.setData(everyEggInventoryReductionReason, everyEggInventoryReductionQuantity)

                            val totalEggInventoryReduction = everyEggInventoryReductionQuantity.sum()
                            val numberOfEggCrates = totalEggInventoryReduction.div(numberOfEggsPerCrates)
                            val numberOfEggsRemaining = totalEggInventoryReduction.rem(numberOfEggsPerCrates)
                            val totalEggsReducedString = "$numberOfEggCrates crates, $numberOfEggsRemaining eggs"
                            tvTotalReductionReasonEggsReduced.text = totalEggsReducedString
                        }}
                        true
                    }
                    R.id.miPastThreeMonths -> {
                        mEggInventoryAdditionViewModel.getEggAdditionsByDate(thePastThreeMonths, theCurrentDate)
                        mEggInventoryAdditionViewModel.eggAdditionsByDate.observe(viewLifecycleOwner)
                        { eggAdditions -> eggAdditions.let { eggInventoryAddition ->
                            val everyEggInventoryAdditionDate = mutableListOf<String>()
                            val everyEggInventoryAdditionQuantity = mutableListOf<Int>()
                            for (additionDates in eggInventoryAddition){
                                everyEggInventoryAdditionDate.add(simpleDateFormat.format(additionDates.date))
                                everyEggInventoryAdditionQuantity.add(additionDates.quantity)}

                            eggInventoryAdditionReportAdapter.setData(everyEggInventoryAdditionDate, everyEggInventoryAdditionQuantity)

                            val totalEggInventoryAdditions = everyEggInventoryAdditionQuantity.sum()
                            val numberOfEggCrates = totalEggInventoryAdditions.div(numberOfEggsPerCrates)
                            val numberOfEggsRemaining = totalEggInventoryAdditions.rem(numberOfEggsPerCrates)
                            val totalEggsAddedString = "$numberOfEggCrates crates, $numberOfEggsRemaining eggs"
                            tvTotalEggsAdded.text = totalEggsAddedString
                        }}

                        mEggInventoryReductionViewModel.getEggReductionsByDate(thePastThreeMonths, theCurrentDate)
                        mEggInventoryReductionViewModel.eggReductionsByDate.observe(viewLifecycleOwner)
                        { eggReduction -> eggReduction.let { EggInventoryReduction ->
                            //declare the lists
                            var totalEggReduction:Int = 0
                            val everyEggInventoryReductionQuantity = mutableListOf<Int>()
                            val everyEggInventoryReductionDate = mutableListOf<String>()
                            for (eggReductions in EggInventoryReduction){
                                everyEggInventoryReductionDate.add(simpleDateFormat.format(eggReductions.date))
                                everyEggInventoryReductionQuantity.add(eggReductions.quantity)
                            }
                            eggInventoryReductionReportAdapter.setData(everyEggInventoryReductionDate, everyEggInventoryReductionQuantity)

                            val totalEggInventoryReduction = everyEggInventoryReductionQuantity.sum()
                            val numberOfEggCrates = totalEggInventoryReduction.div(numberOfEggsPerCrates)
                            val numberOfEggsRemaining = totalEggInventoryReduction.rem(numberOfEggsPerCrates)
                            val totalEggsReducedString = "$numberOfEggCrates crates, $numberOfEggsRemaining eggs"
                            tvTotalEggsReduced.text = totalEggsReducedString

                        }}

                        mEggInventoryReductionViewModel.getEggReductionsByReductionReason(thePastThreeMonths, theCurrentDate)
                        mEggInventoryReductionViewModel.eggReductionsByReductionReason.observe(viewLifecycleOwner)
                        { eggReduction -> eggReduction.let { EggInventoryReduction ->
                            //declare the lists
                            var totalEggReduction:Int = 0
                            val everyEggInventoryReductionQuantity = mutableListOf<Int>()
                            val everyEggInventoryReductionReason = mutableListOf<String>()
                            for (eggReductions in EggInventoryReduction){
                                everyEggInventoryReductionReason.add(eggReductions.name)
                                everyEggInventoryReductionQuantity.add(eggReductions.quantity)
                            }
                            eggReductionReasonReportAdapter.setData(everyEggInventoryReductionReason, everyEggInventoryReductionQuantity)

                            val totalEggInventoryReduction = everyEggInventoryReductionQuantity.sum()
                            val numberOfEggCrates = totalEggInventoryReduction.div(numberOfEggsPerCrates)
                            val numberOfEggsRemaining = totalEggInventoryReduction.rem(numberOfEggsPerCrates)
                            val totalEggsReducedString = "$numberOfEggCrates crates, $numberOfEggsRemaining eggs"
                            tvTotalReductionReasonEggsReduced.text = totalEggsReducedString
                        }}

                        true
                    }
                    R.id.miPastSixMonths -> {
                        mEggInventoryAdditionViewModel.getEggAdditionsByDate(thePastSixMonths, theCurrentDate)
                        mEggInventoryAdditionViewModel.eggAdditionsByDate.observe(viewLifecycleOwner)
                        { eggAdditions -> eggAdditions.let { eggInventoryAddition ->
                            val everyEggInventoryAdditionDate = mutableListOf<String>()
                            val everyEggInventoryAdditionQuantity = mutableListOf<Int>()
                            for (additionDates in eggInventoryAddition){
                                everyEggInventoryAdditionDate.add(simpleDateFormat.format(additionDates.date))
                                everyEggInventoryAdditionQuantity.add(additionDates.quantity)}

                            eggInventoryAdditionReportAdapter.setData(everyEggInventoryAdditionDate, everyEggInventoryAdditionQuantity)

                            val totalEggInventoryAdditions = everyEggInventoryAdditionQuantity.sum()
                            val numberOfEggCrates = totalEggInventoryAdditions.div(numberOfEggsPerCrates)
                            val numberOfEggsRemaining = totalEggInventoryAdditions.rem(numberOfEggsPerCrates)
                            val totalEggsAddedString = "$numberOfEggCrates crates, $numberOfEggsRemaining eggs"
                            tvTotalEggsAdded.text = totalEggsAddedString
                        }}

                        mEggInventoryReductionViewModel.getEggReductionsByDate(thePastSixMonths, theCurrentDate)
                        mEggInventoryReductionViewModel.eggReductionsByDate.observe(viewLifecycleOwner)
                        { eggReduction -> eggReduction.let { EggInventoryReduction ->
                            //declare the lists
                            var totalEggReduction:Int = 0
                            val everyEggInventoryReductionQuantity = mutableListOf<Int>()
                            val everyEggInventoryReductionDate = mutableListOf<String>()
                            for (eggReductions in EggInventoryReduction){
                                everyEggInventoryReductionDate.add(simpleDateFormat.format(eggReductions.date))
                                everyEggInventoryReductionQuantity.add(eggReductions.quantity)
                            }
                            eggInventoryReductionReportAdapter.setData(everyEggInventoryReductionDate, everyEggInventoryReductionQuantity)

                            val totalEggInventoryReduction = everyEggInventoryReductionQuantity.sum()
                            val numberOfEggCrates = totalEggInventoryReduction.div(numberOfEggsPerCrates)
                            val numberOfEggsRemaining = totalEggInventoryReduction.rem(numberOfEggsPerCrates)
                            val totalEggsReducedString = "$numberOfEggCrates crates, $numberOfEggsRemaining eggs"
                            tvTotalEggsReduced.text = totalEggsReducedString

                        }}

                        mEggInventoryReductionViewModel.getEggReductionsByReductionReason(thePastSixMonths, theCurrentDate)
                        mEggInventoryReductionViewModel.eggReductionsByReductionReason.observe(viewLifecycleOwner)
                        { eggReduction -> eggReduction.let { EggInventoryReduction ->
                            //declare the lists
                            var totalEggReduction:Int = 0
                            val everyEggInventoryReductionQuantity = mutableListOf<Int>()
                            val everyEggInventoryReductionReason = mutableListOf<String>()
                            for (eggReductions in EggInventoryReduction){
                                everyEggInventoryReductionReason.add(eggReductions.name)
                                everyEggInventoryReductionQuantity.add(eggReductions.quantity)
                            }
                            eggReductionReasonReportAdapter.setData(everyEggInventoryReductionReason, everyEggInventoryReductionQuantity)

                            val totalEggInventoryReduction = everyEggInventoryReductionQuantity.sum()
                            val numberOfEggCrates = totalEggInventoryReduction.div(numberOfEggsPerCrates)
                            val numberOfEggsRemaining = totalEggInventoryReduction.rem(numberOfEggsPerCrates)
                            val totalEggsReducedString = "$numberOfEggCrates crates, $numberOfEggsRemaining eggs"
                            tvTotalReductionReasonEggsReduced.text = totalEggsReducedString
                        }}

                        true
                    }
                    R.id.miPastYear -> {
                        mEggInventoryAdditionViewModel.getEggAdditionsByDate(thePastYear, theCurrentDate)
                        mEggInventoryAdditionViewModel.eggAdditionsByDate.observe(viewLifecycleOwner)
                        { eggAdditions -> eggAdditions.let { eggInventoryAddition ->
                            val everyEggInventoryAdditionDate = mutableListOf<String>()
                            val everyEggInventoryAdditionQuantity = mutableListOf<Int>()
                            for (additionDates in eggInventoryAddition){
                                everyEggInventoryAdditionDate.add(simpleDateFormat.format(additionDates.date))
                                everyEggInventoryAdditionQuantity.add(additionDates.quantity)}

                            eggInventoryAdditionReportAdapter.setData(everyEggInventoryAdditionDate, everyEggInventoryAdditionQuantity)

                            val totalEggInventoryAdditions = everyEggInventoryAdditionQuantity.sum()
                            val numberOfEggCrates = totalEggInventoryAdditions.div(numberOfEggsPerCrates)
                            val numberOfEggsRemaining = totalEggInventoryAdditions.rem(numberOfEggsPerCrates)
                            val totalEggsAddedString = "$numberOfEggCrates crates, $numberOfEggsRemaining eggs"
                            tvTotalEggsAdded.text = totalEggsAddedString
                        }}

                        mEggInventoryReductionViewModel.getEggReductionsByDate(thePastYear, theCurrentDate)
                        mEggInventoryReductionViewModel.eggReductionsByDate.observe(viewLifecycleOwner)
                        { eggReduction -> eggReduction.let { EggInventoryReduction ->
                            //declare the lists
                            var totalEggReduction:Int = 0
                            val everyEggInventoryReductionQuantity = mutableListOf<Int>()
                            val everyEggInventoryReductionDate = mutableListOf<String>()
                            for (eggReductions in EggInventoryReduction){
                                everyEggInventoryReductionDate.add(simpleDateFormat.format(eggReductions.date))
                                everyEggInventoryReductionQuantity.add(eggReductions.quantity)
                            }
                            eggInventoryReductionReportAdapter.setData(everyEggInventoryReductionDate, everyEggInventoryReductionQuantity)

                            val totalEggInventoryReduction = everyEggInventoryReductionQuantity.sum()
                            val numberOfEggCrates = totalEggInventoryReduction.div(numberOfEggsPerCrates)
                            val numberOfEggsRemaining = totalEggInventoryReduction.rem(numberOfEggsPerCrates)
                            val totalEggsReducedString = "$numberOfEggCrates crates, $numberOfEggsRemaining eggs"
                            tvTotalEggsReduced.text = totalEggsReducedString

                        }}

                        mEggInventoryReductionViewModel.getEggReductionsByReductionReason(thePastYear, theCurrentDate)
                        mEggInventoryReductionViewModel.eggReductionsByReductionReason.observe(viewLifecycleOwner)
                        { eggReduction -> eggReduction.let { EggInventoryReduction ->
                            //declare the lists
                            var totalEggReduction:Int = 0
                            val everyEggInventoryReductionQuantity = mutableListOf<Int>()
                            val everyEggInventoryReductionReason = mutableListOf<String>()
                            for (eggReductions in EggInventoryReduction){
                                everyEggInventoryReductionReason.add(eggReductions.name)
                                everyEggInventoryReductionQuantity.add(eggReductions.quantity)
                            }
                            eggReductionReasonReportAdapter.setData(everyEggInventoryReductionReason, everyEggInventoryReductionQuantity)

                            val totalEggInventoryReduction = everyEggInventoryReductionQuantity.sum()
                            val numberOfEggCrates = totalEggInventoryReduction.div(numberOfEggsPerCrates)
                            val numberOfEggsRemaining = totalEggInventoryReduction.rem(numberOfEggsPerCrates)
                            val totalEggsReducedString = "$numberOfEggCrates crates, $numberOfEggsRemaining eggs"
                            tvTotalReductionReasonEggsReduced.text = totalEggsReducedString
                        }}

                        true
                    }
                    R.id.miPastTwoYears -> {
                        mEggInventoryAdditionViewModel.getEggAdditionsByDate(thePastTwoYears, theCurrentDate)
                        mEggInventoryAdditionViewModel.eggAdditionsByDate.observe(viewLifecycleOwner)
                        { eggAdditions -> eggAdditions.let { eggInventoryAddition ->
                            val everyEggInventoryAdditionDate = mutableListOf<String>()
                            val everyEggInventoryAdditionQuantity = mutableListOf<Int>()
                            for (additionDates in eggInventoryAddition){
                                everyEggInventoryAdditionDate.add(simpleDateFormat.format(additionDates.date))
                                everyEggInventoryAdditionQuantity.add(additionDates.quantity)}

                            eggInventoryAdditionReportAdapter.setData(everyEggInventoryAdditionDate, everyEggInventoryAdditionQuantity)

                            val totalEggInventoryAdditions = everyEggInventoryAdditionQuantity.sum()
                            val numberOfEggCrates = totalEggInventoryAdditions.div(numberOfEggsPerCrates)
                            val numberOfEggsRemaining = totalEggInventoryAdditions.rem(numberOfEggsPerCrates)
                            val totalEggsAddedString = "$numberOfEggCrates crates, $numberOfEggsRemaining eggs"
                            tvTotalEggsAdded.text = totalEggsAddedString
                        }}

                        mEggInventoryReductionViewModel.getEggReductionsByDate(thePastTwoYears, theCurrentDate)
                        mEggInventoryReductionViewModel.eggReductionsByDate.observe(viewLifecycleOwner)
                        { eggReduction -> eggReduction.let { EggInventoryReduction ->
                            //declare the lists
                            var totalEggReduction:Int = 0
                            val everyEggInventoryReductionQuantity = mutableListOf<Int>()
                            val everyEggInventoryReductionDate = mutableListOf<String>()
                            for (eggReductions in EggInventoryReduction){
                                everyEggInventoryReductionDate.add(simpleDateFormat.format(eggReductions.date))
                                everyEggInventoryReductionQuantity.add(eggReductions.quantity)
                            }
                            eggInventoryReductionReportAdapter.setData(everyEggInventoryReductionDate, everyEggInventoryReductionQuantity)

                            val totalEggInventoryReduction = everyEggInventoryReductionQuantity.sum()
                            val numberOfEggCrates = totalEggInventoryReduction.div(numberOfEggsPerCrates)
                            val numberOfEggsRemaining = totalEggInventoryReduction.rem(numberOfEggsPerCrates)
                            val totalEggsReducedString = "$numberOfEggCrates crates, $numberOfEggsRemaining eggs"
                            tvTotalEggsReduced.text = totalEggsReducedString

                        }}

                        mEggInventoryReductionViewModel.getEggReductionsByReductionReason(thePastTwoYears, theCurrentDate)
                        mEggInventoryReductionViewModel.eggReductionsByReductionReason.observe(viewLifecycleOwner)
                        { eggReduction -> eggReduction.let { EggInventoryReduction ->
                            //declare the lists
                            var totalEggReduction:Int = 0
                            val everyEggInventoryReductionQuantity = mutableListOf<Int>()
                            val everyEggInventoryReductionReason = mutableListOf<String>()
                            for (eggReductions in EggInventoryReduction){
                                everyEggInventoryReductionReason.add(eggReductions.name)
                                everyEggInventoryReductionQuantity.add(eggReductions.quantity)
                            }
                            eggReductionReasonReportAdapter.setData(everyEggInventoryReductionReason, everyEggInventoryReductionQuantity)

                            val totalEggInventoryReduction = everyEggInventoryReductionQuantity.sum()
                            val numberOfEggCrates = totalEggInventoryReduction.div(numberOfEggsPerCrates)
                            val numberOfEggsRemaining = totalEggInventoryReduction.rem(numberOfEggsPerCrates)
                            val totalEggsReducedString = "$numberOfEggCrates crates, $numberOfEggsRemaining eggs"
                            tvTotalReductionReasonEggsReduced.text = totalEggsReducedString
                        }}

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
                                        mEggInventoryAdditionViewModel.getEggAdditionsByDate(theFirstDate, theLastDate)
                                        mEggInventoryAdditionViewModel.eggAdditionsByDate.observe(viewLifecycleOwner)
                                        { eggAdditions -> eggAdditions.let { eggInventoryAddition ->
                                            val everyEggInventoryAdditionDate = mutableListOf<String>()
                                            val everyEggInventoryAdditionQuantity = mutableListOf<Int>()
                                            for (additionDates in eggInventoryAddition){
                                                everyEggInventoryAdditionDate.add(simpleDateFormat.format(additionDates.date))
                                                everyEggInventoryAdditionQuantity.add(additionDates.quantity)}

                                            eggInventoryAdditionReportAdapter.setData(everyEggInventoryAdditionDate, everyEggInventoryAdditionQuantity)

                                            val totalEggInventoryAdditions = everyEggInventoryAdditionQuantity.sum()
                                            val numberOfEggCrates = totalEggInventoryAdditions.div(numberOfEggsPerCrates)
                                            val numberOfEggsRemaining = totalEggInventoryAdditions.rem(numberOfEggsPerCrates)
                                            val totalEggsAddedString = "$numberOfEggCrates crates, $numberOfEggsRemaining eggs"
                                            tvTotalEggsAdded.text = totalEggsAddedString
                                        }}


                                        mEggInventoryReductionViewModel.getEggReductionsByDate(theFirstDate, theLastDate)
                                        mEggInventoryReductionViewModel.eggReductionsByDate.observe(viewLifecycleOwner)
                                        { eggReduction -> eggReduction.let { EggInventoryReduction ->
                                            //declare the lists
                                            var totalEggReduction:Int = 0
                                            val everyEggInventoryReductionQuantity = mutableListOf<Int>()
                                            val everyEggInventoryReductionDate = mutableListOf<String>()
                                            for (eggReductions in EggInventoryReduction){
                                                everyEggInventoryReductionDate.add(simpleDateFormat.format(eggReductions.date))
                                                everyEggInventoryReductionQuantity.add(eggReductions.quantity)
                                            }
                                            eggInventoryReductionReportAdapter.setData(everyEggInventoryReductionDate, everyEggInventoryReductionQuantity)

                                            val totalEggInventoryReduction = everyEggInventoryReductionQuantity.sum()
                                            val numberOfEggCrates = totalEggInventoryReduction.div(numberOfEggsPerCrates)
                                            val numberOfEggsRemaining = totalEggInventoryReduction.rem(numberOfEggsPerCrates)
                                            val totalEggsReducedString = "$numberOfEggCrates crates, $numberOfEggsRemaining eggs"
                                            tvTotalEggsReduced.text = totalEggsReducedString

                                        }}

                                        mEggInventoryReductionViewModel.getEggReductionsByReductionReason(theFirstDate, theLastDate)
                                        mEggInventoryReductionViewModel.eggReductionsByReductionReason.observe(viewLifecycleOwner)
                                        { eggReduction -> eggReduction.let { EggInventoryReduction ->
                                            //declare the lists
                                            var totalEggReduction:Int = 0
                                            val everyEggInventoryReductionQuantity = mutableListOf<Int>()
                                            val everyEggInventoryReductionReason = mutableListOf<String>()
                                            for (eggReductions in EggInventoryReduction){
                                                everyEggInventoryReductionReason.add(eggReductions.name)
                                                everyEggInventoryReductionQuantity.add(eggReductions.quantity)
                                            }
                                            eggReductionReasonReportAdapter.setData(everyEggInventoryReductionReason, everyEggInventoryReductionQuantity)

                                            val totalEggInventoryReduction = everyEggInventoryReductionQuantity.sum()
                                            val numberOfEggCrates = totalEggInventoryReduction.div(numberOfEggsPerCrates)
                                            val numberOfEggsRemaining = totalEggInventoryReduction.rem(numberOfEggsPerCrates)
                                            val totalEggsReducedString = "$numberOfEggCrates crates, $numberOfEggsRemaining eggs"
                                            tvTotalReductionReasonEggsReduced.text = totalEggsReducedString
                                        }}
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

        eggReductionReasonReportAdapter.setOnItemClickListener(object : DateQuantityReportAdapter.OnItemClickListener{
            override fun onItemDateClickListener(date: String) {
                TODO("Not yet implemented")
            }

            override fun onItemQuantityClickListener(quantity: Int) {
                TODO("Not yet implemented")
            }
        })

        eggInventoryAdditionReportAdapter.setOnItemClickListener(object : DateQuantityReportAdapter.OnItemClickListener{
            override fun onItemDateClickListener(date: String) {
                TODO("Not yet implemented")
            }

            override fun onItemQuantityClickListener(quantity: Int) {
                TODO("Not yet implemented")
            }
        })
        eggInventoryReductionReportAdapter.setOnItemClickListener(object : DateQuantityReportAdapter.OnItemClickListener{
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