package com.example.pkompoultrymanager.reports.feed_inventory.fragments

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
import com.example.pkompoultrymanager.inventory.feed.addition.FeedInventoryAdditionViewModel
import com.example.pkompoultrymanager.inventory.feed.reduction.FeedInventoryReductionViewModel
import com.example.pkompoultrymanager.reports.feed_inventory.FeedQuantityReportAdapter
import java.text.SimpleDateFormat
import java.util.*

class GeneralFeedReport : Fragment() {
    private lateinit var mFeedInventoryAdditionViewModel: FeedInventoryAdditionViewModel
    private lateinit var mFeedInventoryReductionViewModel: FeedInventoryReductionViewModel

    private lateinit var myFarmInfo: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_general_feed_report, container, false)

        mFeedInventoryAdditionViewModel =
            ViewModelProvider(this)[FeedInventoryAdditionViewModel::class.java]
        mFeedInventoryReductionViewModel =
            ViewModelProvider(this)[FeedInventoryReductionViewModel::class.java]

        myFarmInfo = context?.getSharedPreferences("MyFarmInfo", Context.MODE_PRIVATE)!!
        val numberOfFeedsPerCrates = myFarmInfo.getInt("numberOfFeedsPerCrate", 30)
        val currency = myFarmInfo.getString("currency", "GHS")
        val measuringUnit = myFarmInfo.getString("measuringUnit", "kg")


        val numberOfList = 100
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

        val dateFilter = view.findViewById<ImageButton>(R.id.ibDateFilter_GeneralFeedReport)

        // report headers
        val tvFeedNameQuantityHeader = view. findViewById<TextView>(R.id.tvFeedNameQuantityAddedHeaderName_GeneralFeedReport)
        val tvFlockNameFeedQuantityHeader = view. findViewById<TextView>(R.id.tvFlockNameFeedQuantityHeaderName_GeneralFeedReport)
        val tvFeedReductionReasonQuantityHeader = view. findViewById<TextView>(R.id.tvFeedReductionReasonQuantityHeaderName_GeneralFeedReport)
        val tvFeedNameQuantityReducedHeader = view. findViewById<TextView>(R.id.tvFeedNameQuantityReducedHeaderName_GeneralFeedReport)
        val tvFeedAcquisitionHeader = view. findViewById<TextView>(R.id.tvFeedAcquisitionHeaderName_GeneralFeedReport)
        val tvFeedReductionHeader = view. findViewById<TextView>(R.id.tvFeedReductionHeaderName_GeneralFeedReport)
        val tvFeedNameCostHeader = view. findViewById<TextView>(R.id.tvFeedNameCostHeaderName_GeneralFeedReport)


        // totals containers
        val llTotalFeedNameQuantityAddedContainer = view. findViewById<LinearLayout>(R.id.llTotalFeedNameQuantityAddedContainer_GeneralFeedReport)
        val llTotalFlockNameFeedQuantityContainer = view. findViewById<LinearLayout>(R.id.llTotalFlockNameFeedQuantityContainer_GeneralFeedReport)
        val llTotalFeedReductionReasonQuantityContainer = view. findViewById<LinearLayout>(R.id.llTotalFeedReductionReasonQuantityContainer_GeneralFeedReport)
        val llTotalFeedNameQuantityReducedContainer = view. findViewById<LinearLayout>(R.id.llTotalFeedNameQuantityReducedContainer_GeneralFeedReport)
        val llTotalFeedAcquisitionsContainer = view. findViewById<LinearLayout>(R.id.llTotalFeedAcquisitionsContainer_GeneralFeedReport)
        val llTotalFeedReductionContainer = view. findViewById<LinearLayout>(R.id.llTotalFeedReductionContainer_GeneralFeedReport)
        val llTotalFeedNameCostContainer = view. findViewById<LinearLayout>(R.id.llTotalFeedNameCostContainer_GeneralFeedReport)


        // Totals
        val tvTotalFeedNameQuantity = view. findViewById<TextView>(R.id.tvTotalFeedNameQuantityAddedValue_GeneralFeedReport)
        val tvTotalFlockNameFeedQuantityValue = view. findViewById<TextView>(R.id.tvTotalFlockNameFeedQuantityValue_GeneralFeedReport)
        val tvTotalFeedReductionReasonQuantityValue = view. findViewById<TextView>(R.id.tvTotalFeedReductionReasonQuantityValue_GeneralFeedReport)
        val tvTotalFeedNameQuantityReducedValue = view. findViewById<TextView>(R.id.tvTotalFeedNameQuantityReducedValue_GeneralFeedReport)
        val tvTotalFeedAcquisitionValue = view. findViewById<TextView>(R.id.tvTotalFeedAcquisitionValue_GeneralFeedReport)
        val tvTotalFeedReductionValue = view. findViewById<TextView>(R.id.tvTotalFeedReductionValue_GeneralFeedReport)
        val tvTotalFeedNameCostValue = view. findViewById<TextView>(R.id.tvTotalFeedNameCostValue_GeneralFeedReport)


       // recycler views
        val rvDailyFeedNameQuantityAddedReport = view.findViewById<RecyclerView>(R.id.rvDailyFeedNameQuantityAddedReport_GeneralFeedReport)
        val rvDailyFlockNameFeedQuantityReport = view.findViewById<RecyclerView>(R.id.rvDailyFlockNameFeedQuantityReport_GeneralFeedReport)
        val rvFeedReductionReasonQuantityReport = view.findViewById<RecyclerView>(R.id.rvFeedReductionReasonQuantityReport_GeneralFeedReport)
        val rvDailyFeedNameQuantityReducedReport = view.findViewById<RecyclerView>(R.id.rvDailyFeedNameQuantityReducedReport_GeneralFeedReport)
        val rvDailyFeedAcquisitionsReport = view.findViewById<RecyclerView>(R.id.rvDailyFeedAcquisitionsReport_GeneralFeedReport)
        val rvDailyFeedReductionReport = view.findViewById<RecyclerView>(R.id.rvDailyFeedReductionReport_GeneralFeedReport)
        val rvFeedNameCostReport = view.findViewById<RecyclerView>(R.id.rvFeedNameCostReport_GeneralFeedReport)


        val feedNameQuantityAddedReportAdapter = FeedQuantityReportAdapter(numberOfFeedsPerCrates)
        val flockNameFeedQuantityReportAdapter = FeedQuantityReportAdapter(numberOfFeedsPerCrates)
        val feedReductionReasonQuantityReportAdapter = FeedQuantityReportAdapter(numberOfFeedsPerCrates)
        val feedNameQuantityReducedReportAdapter = FeedQuantityReportAdapter(numberOfFeedsPerCrates)
        val feedAcquisitionsReportAdapter = FeedQuantityReportAdapter(numberOfFeedsPerCrates)
        val feedReductionReportAdapter = FeedQuantityReportAdapter(numberOfFeedsPerCrates)
        val feedNameCostReportAdapter = FeedQuantityReportAdapter(numberOfFeedsPerCrates)

         rvDailyFeedNameQuantityAddedReport.adapter = feedNameQuantityAddedReportAdapter
         rvDailyFlockNameFeedQuantityReport.adapter = flockNameFeedQuantityReportAdapter
         rvFeedReductionReasonQuantityReport.adapter = feedReductionReasonQuantityReportAdapter
         rvDailyFeedNameQuantityReducedReport.adapter = feedNameQuantityReducedReportAdapter
         rvDailyFeedAcquisitionsReport.adapter = feedAcquisitionsReportAdapter
         rvDailyFeedReductionReport.adapter = feedReductionReportAdapter
         rvFeedNameCostReport.adapter = feedNameCostReportAdapter


         rvDailyFeedNameQuantityAddedReport.layoutManager = LinearLayoutManager(requireContext())
         rvDailyFlockNameFeedQuantityReport.layoutManager = LinearLayoutManager(requireContext())
         rvFeedReductionReasonQuantityReport.layoutManager = LinearLayoutManager(requireContext())
         rvDailyFeedNameQuantityReducedReport.layoutManager = LinearLayoutManager(requireContext())
         rvDailyFeedAcquisitionsReport.layoutManager = LinearLayoutManager(requireContext())
         rvDailyFeedReductionReport.layoutManager = LinearLayoutManager(requireContext())
         rvFeedNameCostReport.layoutManager = LinearLayoutManager(requireContext())



        tvFeedNameQuantityHeader.setOnClickListener {
            val popupHideAndShowMenu = PopupMenu(requireContext(), tvFeedNameQuantityHeader, Gravity.END)
            popupHideAndShowMenu.inflate(R.menu.hide_show_menu)
            popupHideAndShowMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.miShow -> {
                        llTotalFeedNameQuantityAddedContainer.isGone = false
                        rvDailyFeedNameQuantityAddedReport.isGone = false
                        true
                    }
                    R.id.miHide -> {
                        llTotalFeedNameQuantityAddedContainer.isGone = true
                        rvDailyFeedNameQuantityAddedReport.isGone = true
                        true
                    }
                else -> true
                }
            }
            popupHideAndShowMenu.show()
            true
        }
        tvFlockNameFeedQuantityHeader.setOnClickListener {
            val popupHideAndShowMenu = PopupMenu(requireContext(), tvFlockNameFeedQuantityHeader, Gravity.END)
            popupHideAndShowMenu.inflate(R.menu.hide_show_menu)
            popupHideAndShowMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.miShow -> {
                        llTotalFlockNameFeedQuantityContainer.isGone = false
                        rvDailyFlockNameFeedQuantityReport.isGone = false
                        true
                    }
                    R.id.miHide -> {
                        llTotalFlockNameFeedQuantityContainer.isGone = true
                        rvDailyFlockNameFeedQuantityReport.isGone = true
                        true
                    }
                else -> true
                }
            }
            popupHideAndShowMenu.show()
            true
        }
        tvFeedReductionReasonQuantityHeader.setOnClickListener {
            val popupHideAndShowMenu = PopupMenu(requireContext(), tvFeedReductionReasonQuantityHeader, Gravity.END)
            popupHideAndShowMenu.inflate(R.menu.hide_show_menu)
            popupHideAndShowMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.miShow -> {
                        llTotalFeedReductionReasonQuantityContainer.isGone = false
                        rvFeedReductionReasonQuantityReport.isGone = false
                        true
                    }
                    R.id.miHide -> {
                        llTotalFeedReductionReasonQuantityContainer.isGone = true
                        rvFeedReductionReasonQuantityReport.isGone = true
                        true
                    }
                else -> true
                }
            }
            popupHideAndShowMenu.show()
            true
        }
        tvFeedNameQuantityReducedHeader.setOnClickListener {
            val popupHideAndShowMenu = PopupMenu(requireContext(), tvFeedNameQuantityReducedHeader, Gravity.END)
            popupHideAndShowMenu.inflate(R.menu.hide_show_menu)
            popupHideAndShowMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.miShow -> {
                        llTotalFeedNameQuantityReducedContainer.isGone = false
                        rvDailyFeedNameQuantityReducedReport.isGone = false
                        true
                    }
                    R.id.miHide -> {
                        llTotalFeedNameQuantityReducedContainer.isGone = true
                        rvDailyFeedNameQuantityReducedReport.isGone = true
                        true
                    }
                else -> true
                }
            }
            popupHideAndShowMenu.show()
            true
        }
        tvFeedAcquisitionHeader.setOnClickListener{
            val popupHideAndShowMenu = PopupMenu(requireContext(), tvFeedAcquisitionHeader, Gravity.END)
            popupHideAndShowMenu.inflate(R.menu.hide_show_menu)
            popupHideAndShowMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.miShow -> {
                        llTotalFeedAcquisitionsContainer.isGone = false
                        rvDailyFeedAcquisitionsReport.isGone = false
                        true
                    }
                    R.id.miHide -> {
                        llTotalFeedAcquisitionsContainer.isGone = true
                        rvDailyFeedAcquisitionsReport.isGone = true
                        true
                    }
                else -> true
                }
            }
            popupHideAndShowMenu.show()
            true
        }
        tvFeedReductionHeader.setOnClickListener{
            val popupHideAndShowMenu = PopupMenu(requireContext(), tvFeedReductionHeader, Gravity.END)
            popupHideAndShowMenu.inflate(R.menu.hide_show_menu)
            popupHideAndShowMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.miShow -> {
                        llTotalFeedReductionContainer.isGone = false
                        rvDailyFeedReductionReport.isGone = false
                        true
                    }
                    R.id.miHide -> {
                        llTotalFeedReductionContainer.isGone = true
                        rvDailyFeedReductionReport.isGone = true
                        true
                    }
                else -> true
                }
            }
            popupHideAndShowMenu.show()
            true
        }
        tvFeedNameCostHeader.setOnClickListener{
            val popupHideAndShowMenu = PopupMenu(requireContext(), tvFeedNameCostHeader, Gravity.END)
            popupHideAndShowMenu.inflate(R.menu.hide_show_menu)
            popupHideAndShowMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.miShow -> {
                        llTotalFeedNameCostContainer.isGone = false
                        rvFeedNameCostReport.isGone = false
                        true
                    }
                    R.id.miHide -> {
                        llTotalFeedNameCostContainer.isGone = true
                        rvFeedNameCostReport.isGone = true
                        true
                    }
                else -> true
                }
            }
            popupHideAndShowMenu.show()
            true
        }


        mFeedInventoryAdditionViewModel.getFeedQuantityForDate(thePreviousMonth, theCurrentDate)
        mFeedInventoryAdditionViewModel.feedQuantityForDate.observe(viewLifecycleOwner)
        { feedAddition -> feedAddition.let { FeedInventoryAddition ->
            val everyFeedInventoryAdditionDate = mutableListOf<String>()
            val everyFeedInventoryAdditionQuantity = mutableListOf<Double>()
            for (feedInventory in FeedInventoryAddition){
                everyFeedInventoryAdditionDate.add(simpleDateFormat.format(feedInventory.date))
                everyFeedInventoryAdditionQuantity.add(feedInventory.quantity)}

            feedAcquisitionsReportAdapter.setData(everyFeedInventoryAdditionDate, everyFeedInventoryAdditionQuantity)

            val totalQuantityOfFeedAdded = everyFeedInventoryAdditionQuantity.sum()
            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
            tvTotalFeedAcquisitionValue.text = sumOfFeedQuantity
        }}

        mFeedInventoryAdditionViewModel.getFeedQuantityForFeedName(thePreviousMonth, theCurrentDate)
        mFeedInventoryAdditionViewModel.feedQuantityForFeedName.observe(viewLifecycleOwner)
        { feedAddition -> feedAddition.let { FeedInventoryAddition ->
            val everyFeedInventoryAdditionName = mutableListOf<String>()
            val everyFeedInventoryAdditionQuantity = mutableListOf<Double>()
            for (feedInventory in FeedInventoryAddition){
                everyFeedInventoryAdditionName.add(feedInventory.name)
                everyFeedInventoryAdditionQuantity.add(feedInventory.quantity)}

            feedAcquisitionsReportAdapter.setData(everyFeedInventoryAdditionName, everyFeedInventoryAdditionQuantity)

            val totalQuantityOfFeedAdded = everyFeedInventoryAdditionQuantity.sum()
            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
            tvTotalFeedNameQuantity.text = sumOfFeedQuantity
        }}

        mFeedInventoryAdditionViewModel.getFeedCostForFeedName(thePreviousMonth, theCurrentDate)
        mFeedInventoryAdditionViewModel.feedCostForFeedName.observe(viewLifecycleOwner)
        { feedAddition -> feedAddition.let { FeedInventoryAddition ->
            val everyFeedInventoryAdditionName = mutableListOf<String>()
            val everyFeedInventoryAdditionCost = mutableListOf<Double>()
            for (feedInventory in FeedInventoryAddition){
                everyFeedInventoryAdditionName.add(feedInventory.name)
                everyFeedInventoryAdditionCost.add(feedInventory.quantity)}

            feedAcquisitionsReportAdapter.setData(everyFeedInventoryAdditionName, everyFeedInventoryAdditionCost)

            val totalQuantityOfFeedAdded = everyFeedInventoryAdditionCost.sum()
            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
            tvTotalFeedNameCostValue.text = sumOfFeedQuantity
        }}

        mFeedInventoryReductionViewModel.getFeedQuantityForDate(thePreviousMonth, theCurrentDate)
        mFeedInventoryReductionViewModel.feedQuantityForDate.observe(viewLifecycleOwner)
        { feedReduction -> feedReduction.let { FeedInventoryReduction ->
            val everyFeedInventoryReductionDate = mutableListOf<String>()
            val feedInventoryReductionQuantity = mutableListOf<Double>()
            for (feedInventory in FeedInventoryReduction){
                everyFeedInventoryReductionDate.add(simpleDateFormat.format(feedInventory.date))
                feedInventoryReductionQuantity.add(feedInventory.quantity)}

            feedReductionReportAdapter.setData(everyFeedInventoryReductionDate, feedInventoryReductionQuantity)

            val totalQuantityOfFeedAdded = feedInventoryReductionQuantity.sum()
            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
            tvTotalFeedReductionValue.text = sumOfFeedQuantity
        }}

        mFeedInventoryReductionViewModel.getFeedQuantityForFlockName(thePreviousMonth, theCurrentDate)
        mFeedInventoryReductionViewModel.feedQuantityForFlockName.observe(viewLifecycleOwner)
        { feedReduction -> feedReduction.let { FeedInventoryReduction ->
            val everyFlockName = mutableListOf<String>()
            val feedInventoryReductionQuantity = mutableListOf<Double>()
            for (feedInventory in FeedInventoryReduction){
                everyFlockName.add(feedInventory.name)
                feedInventoryReductionQuantity.add(feedInventory.quantity)}

            flockNameFeedQuantityReportAdapter.setData(everyFlockName, feedInventoryReductionQuantity)

            val totalQuantityOfFeedAdded = feedInventoryReductionQuantity.sum()
            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
            tvTotalFlockNameFeedQuantityValue.text = sumOfFeedQuantity
        }}

        mFeedInventoryReductionViewModel.getFeedQuantityForFeedName(thePreviousMonth, theCurrentDate)
        mFeedInventoryReductionViewModel.feedQuantityForFeedName.observe(viewLifecycleOwner)
        { feedReduction -> feedReduction.let { FeedInventoryReduction ->
            val everyFeedName = mutableListOf<String>()
            val feedInventoryReductionQuantity = mutableListOf<Double>()
            for (feedInventory in FeedInventoryReduction){
                everyFeedName.add(feedInventory.name)
                feedInventoryReductionQuantity.add(feedInventory.quantity)}

            feedNameQuantityReducedReportAdapter.setData(everyFeedName, feedInventoryReductionQuantity)

            val totalQuantityOfFeedAdded = feedInventoryReductionQuantity.sum()
            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
            tvTotalFeedNameQuantityReducedValue.text = sumOfFeedQuantity
        }}

        mFeedInventoryReductionViewModel.getFeedQuantityForReductionReason(thePreviousMonth, theCurrentDate)
        mFeedInventoryReductionViewModel.feedQuantityForReductionReason.observe(viewLifecycleOwner)
        { feedReduction -> feedReduction.let { FeedInventoryReduction ->
            val everyReductionReason = mutableListOf<String>()
            val feedInventoryReductionQuantity = mutableListOf<Double>()
            for (feedInventory in FeedInventoryReduction){
                everyReductionReason.add(feedInventory.name)
                feedInventoryReductionQuantity.add(feedInventory.quantity)}

            feedNameQuantityReducedReportAdapter.setData(everyReductionReason, feedInventoryReductionQuantity)

            val totalQuantityOfFeedAdded = feedInventoryReductionQuantity.sum()
            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
            tvTotalFeedReductionReasonQuantityValue.text = sumOfFeedQuantity
        }}

        dateFilter.setOnClickListener {
            val popupDateFilterMenu = PopupMenu(requireContext(), dateFilter)
            popupDateFilterMenu.inflate(R.menu.date_ranges)
            popupDateFilterMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.miAllTime -> {
                        mFeedInventoryAdditionViewModel.allFeedQuantityForDate.observe(viewLifecycleOwner)
                        { feedAddition -> feedAddition.let { FeedInventoryAddition ->
                            val everyFeedInventoryAdditionDate = mutableListOf<String>()
                            val everyFeedInventoryAdditionQuantity = mutableListOf<Double>()
                            for (feedInventory in FeedInventoryAddition){
                                everyFeedInventoryAdditionDate.add(simpleDateFormat.format(feedInventory.date))
                                everyFeedInventoryAdditionQuantity.add(feedInventory.quantity)}

                            feedAcquisitionsReportAdapter.setData(everyFeedInventoryAdditionDate, everyFeedInventoryAdditionQuantity)

                            val totalQuantityOfFeedAdded = everyFeedInventoryAdditionQuantity.sum()
                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                            tvTotalFeedAcquisitionValue.text = sumOfFeedQuantity
                        }}

                        mFeedInventoryAdditionViewModel.allFeedQuantityForFeedName.observe(viewLifecycleOwner)
                        { feedAddition -> feedAddition.let { FeedInventoryAddition ->
                            val everyFeedInventoryAdditionName = mutableListOf<String>()
                            val everyFeedInventoryAdditionQuantity = mutableListOf<Double>()
                            for (feedInventory in FeedInventoryAddition){
                                everyFeedInventoryAdditionName.add(feedInventory.name)
                                everyFeedInventoryAdditionQuantity.add(feedInventory.quantity)}

                            feedAcquisitionsReportAdapter.setData(everyFeedInventoryAdditionName, everyFeedInventoryAdditionQuantity)

                            val totalQuantityOfFeedAdded = everyFeedInventoryAdditionQuantity.sum()
                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                            tvTotalFeedNameQuantity.text = sumOfFeedQuantity
                        }}

                        mFeedInventoryAdditionViewModel.allFeedCostForFeedName.observe(viewLifecycleOwner)
                        { feedAddition -> feedAddition.let { FeedInventoryAddition ->
                            val everyFeedInventoryAdditionName = mutableListOf<String>()
                            val everyFeedInventoryAdditionCost = mutableListOf<Double>()
                            for (feedInventory in FeedInventoryAddition){
                                everyFeedInventoryAdditionName.add(feedInventory.name)
                                everyFeedInventoryAdditionCost.add(feedInventory.quantity)}

                            feedAcquisitionsReportAdapter.setData(everyFeedInventoryAdditionName, everyFeedInventoryAdditionCost)

                            val totalQuantityOfFeedAdded = everyFeedInventoryAdditionCost.sum()
                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                            tvTotalFeedNameCostValue.text = sumOfFeedQuantity
                        }}

                        mFeedInventoryReductionViewModel.allFeedQuantityForDate.observe(viewLifecycleOwner)
                        { feedReduction -> feedReduction.let { FeedInventoryReduction ->
                            val everyFeedInventoryReductionDate = mutableListOf<String>()
                            val feedInventoryReductionQuantity = mutableListOf<Double>()
                            for (feedInventory in FeedInventoryReduction){
                                everyFeedInventoryReductionDate.add(simpleDateFormat.format(feedInventory.date))
                                feedInventoryReductionQuantity.add(feedInventory.quantity)}

                            feedReductionReportAdapter.setData(everyFeedInventoryReductionDate, feedInventoryReductionQuantity)

                            val totalQuantityOfFeedAdded = feedInventoryReductionQuantity.sum()
                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                            tvTotalFeedReductionValue.text = sumOfFeedQuantity
                        }}

                        mFeedInventoryReductionViewModel.allFeedQuantityForFlockName.observe(viewLifecycleOwner)
                        { feedReduction -> feedReduction.let { FeedInventoryReduction ->
                            val everyFlockName = mutableListOf<String>()
                            val feedInventoryReductionQuantity = mutableListOf<Double>()
                            for (feedInventory in FeedInventoryReduction){
                                everyFlockName.add(feedInventory.name)
                                feedInventoryReductionQuantity.add(feedInventory.quantity)}

                            flockNameFeedQuantityReportAdapter.setData(everyFlockName, feedInventoryReductionQuantity)

                            val totalQuantityOfFeedAdded = feedInventoryReductionQuantity.sum()
                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                            tvTotalFlockNameFeedQuantityValue.text = sumOfFeedQuantity
                        }}

                        mFeedInventoryReductionViewModel.allFeedQuantityForFeedName.observe(viewLifecycleOwner)
                        { feedReduction -> feedReduction.let { FeedInventoryReduction ->
                            val everyFeedName = mutableListOf<String>()
                            val feedInventoryReductionQuantity = mutableListOf<Double>()
                            for (feedInventory in FeedInventoryReduction){
                                everyFeedName.add(feedInventory.name)
                                feedInventoryReductionQuantity.add(feedInventory.quantity)}

                            feedNameQuantityReducedReportAdapter.setData(everyFeedName, feedInventoryReductionQuantity)

                            val totalQuantityOfFeedAdded = feedInventoryReductionQuantity.sum()
                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                            tvTotalFeedNameQuantityReducedValue.text = sumOfFeedQuantity
                        }}

                        mFeedInventoryReductionViewModel.allFeedQuantityForReductionReason.observe(viewLifecycleOwner)
                        { feedReduction -> feedReduction.let { FeedInventoryReduction ->
                            val everyReductionReason = mutableListOf<String>()
                            val feedInventoryReductionQuantity = mutableListOf<Double>()
                            for (feedInventory in FeedInventoryReduction){
                                everyReductionReason.add(feedInventory.name)
                                feedInventoryReductionQuantity.add(feedInventory.quantity)}

                            feedNameQuantityReducedReportAdapter.setData(everyReductionReason, feedInventoryReductionQuantity)

                            val totalQuantityOfFeedAdded = feedInventoryReductionQuantity.sum()
                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                            tvTotalFeedReductionReasonQuantityValue.text = sumOfFeedQuantity
                        }}
                        true
                    }
                    R.id.miToday -> {
                        mFeedInventoryAdditionViewModel.getFeedQuantityForDate(theCurrentDate, theCurrentDate)
                        mFeedInventoryAdditionViewModel.feedQuantityForDate.observe(viewLifecycleOwner)
                        { feedAddition -> feedAddition.let { FeedInventoryAddition ->
                            val everyFeedInventoryAdditionDate = mutableListOf<String>()
                            val everyFeedInventoryAdditionQuantity = mutableListOf<Double>()
                            for (feedInventory in FeedInventoryAddition){
                                everyFeedInventoryAdditionDate.add(simpleDateFormat.format(feedInventory.date))
                                everyFeedInventoryAdditionQuantity.add(feedInventory.quantity)}

                            feedAcquisitionsReportAdapter.setData(everyFeedInventoryAdditionDate, everyFeedInventoryAdditionQuantity)

                            val totalQuantityOfFeedAdded = everyFeedInventoryAdditionQuantity.sum()
                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                            tvTotalFeedAcquisitionValue.text = sumOfFeedQuantity
                        }}

                        mFeedInventoryAdditionViewModel.getFeedQuantityForFeedName(theCurrentDate, theCurrentDate)
                        mFeedInventoryAdditionViewModel.feedQuantityForFeedName.observe(viewLifecycleOwner)
                        { feedAddition -> feedAddition.let { FeedInventoryAddition ->
                            val everyFeedInventoryAdditionName = mutableListOf<String>()
                            val everyFeedInventoryAdditionQuantity = mutableListOf<Double>()
                            for (feedInventory in FeedInventoryAddition){
                                everyFeedInventoryAdditionName.add(feedInventory.name)
                                everyFeedInventoryAdditionQuantity.add(feedInventory.quantity)}

                            feedAcquisitionsReportAdapter.setData(everyFeedInventoryAdditionName, everyFeedInventoryAdditionQuantity)

                            val totalQuantityOfFeedAdded = everyFeedInventoryAdditionQuantity.sum()
                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                            tvTotalFeedNameQuantity.text = sumOfFeedQuantity
                        }}

                        mFeedInventoryAdditionViewModel.getFeedCostForFeedName(theCurrentDate, theCurrentDate)
                        mFeedInventoryAdditionViewModel.feedCostForFeedName.observe(viewLifecycleOwner)
                        { feedAddition -> feedAddition.let { FeedInventoryAddition ->
                            val everyFeedInventoryAdditionName = mutableListOf<String>()
                            val everyFeedInventoryAdditionCost = mutableListOf<Double>()
                            for (feedInventory in FeedInventoryAddition){
                                everyFeedInventoryAdditionName.add(feedInventory.name)
                                everyFeedInventoryAdditionCost.add(feedInventory.quantity)}

                            feedAcquisitionsReportAdapter.setData(everyFeedInventoryAdditionName, everyFeedInventoryAdditionCost)

                            val totalQuantityOfFeedAdded = everyFeedInventoryAdditionCost.sum()
                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                            tvTotalFeedNameCostValue.text = sumOfFeedQuantity
                        }}

                        mFeedInventoryReductionViewModel.getFeedQuantityForDate(theCurrentDate, theCurrentDate)
                        mFeedInventoryReductionViewModel.feedQuantityForDate.observe(viewLifecycleOwner)
                        { feedReduction -> feedReduction.let { FeedInventoryReduction ->
                            val everyFeedInventoryReductionDate = mutableListOf<String>()
                            val feedInventoryReductionQuantity = mutableListOf<Double>()
                            for (feedInventory in FeedInventoryReduction){
                                everyFeedInventoryReductionDate.add(simpleDateFormat.format(feedInventory.date))
                                feedInventoryReductionQuantity.add(feedInventory.quantity)}

                            feedReductionReportAdapter.setData(everyFeedInventoryReductionDate, feedInventoryReductionQuantity)

                            val totalQuantityOfFeedAdded = feedInventoryReductionQuantity.sum()
                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                            tvTotalFeedReductionValue.text = sumOfFeedQuantity
                        }}

                        mFeedInventoryReductionViewModel.getFeedQuantityForFlockName(theCurrentDate, theCurrentDate)
                        mFeedInventoryReductionViewModel.feedQuantityForFlockName.observe(viewLifecycleOwner)
                        { feedReduction -> feedReduction.let { FeedInventoryReduction ->
                            val everyFlockName = mutableListOf<String>()
                            val feedInventoryReductionQuantity = mutableListOf<Double>()
                            for (feedInventory in FeedInventoryReduction){
                                everyFlockName.add(feedInventory.name)
                                feedInventoryReductionQuantity.add(feedInventory.quantity)}

                            flockNameFeedQuantityReportAdapter.setData(everyFlockName, feedInventoryReductionQuantity)

                            val totalQuantityOfFeedAdded = feedInventoryReductionQuantity.sum()
                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                            tvTotalFlockNameFeedQuantityValue.text = sumOfFeedQuantity
                        }}

                        mFeedInventoryReductionViewModel.getFeedQuantityForFeedName(theCurrentDate, theCurrentDate)
                        mFeedInventoryReductionViewModel.feedQuantityForFeedName.observe(viewLifecycleOwner)
                        { feedReduction -> feedReduction.let { FeedInventoryReduction ->
                            val everyFeedName = mutableListOf<String>()
                            val feedInventoryReductionQuantity = mutableListOf<Double>()
                            for (feedInventory in FeedInventoryReduction){
                                everyFeedName.add(feedInventory.name)
                                feedInventoryReductionQuantity.add(feedInventory.quantity)}

                            feedNameQuantityReducedReportAdapter.setData(everyFeedName, feedInventoryReductionQuantity)

                            val totalQuantityOfFeedAdded = feedInventoryReductionQuantity.sum()
                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                            tvTotalFeedNameQuantityReducedValue.text = sumOfFeedQuantity
                        }}

                        mFeedInventoryReductionViewModel.getFeedQuantityForReductionReason(theCurrentDate, theCurrentDate)
                        mFeedInventoryReductionViewModel.feedQuantityForReductionReason.observe(viewLifecycleOwner)
                        { feedReduction -> feedReduction.let { FeedInventoryReduction ->
                            val everyReductionReason = mutableListOf<String>()
                            val feedInventoryReductionQuantity = mutableListOf<Double>()
                            for (feedInventory in FeedInventoryReduction){
                                everyReductionReason.add(feedInventory.name)
                                feedInventoryReductionQuantity.add(feedInventory.quantity)}

                            feedNameQuantityReducedReportAdapter.setData(everyReductionReason, feedInventoryReductionQuantity)

                            val totalQuantityOfFeedAdded = feedInventoryReductionQuantity.sum()
                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                            tvTotalFeedReductionReasonQuantityValue.text = sumOfFeedQuantity
                        }}
                        true
                    }
                    R.id.miThisWeek -> {
                        mFeedInventoryAdditionViewModel.getFeedQuantityForDate(thePreviousWeek, theCurrentDate)
                        mFeedInventoryAdditionViewModel.feedQuantityForDate.observe(viewLifecycleOwner)
                        { feedAddition -> feedAddition.let { FeedInventoryAddition ->
                            val everyFeedInventoryAdditionDate = mutableListOf<String>()
                            val everyFeedInventoryAdditionQuantity = mutableListOf<Double>()
                            for (feedInventory in FeedInventoryAddition){
                                everyFeedInventoryAdditionDate.add(simpleDateFormat.format(feedInventory.date))
                                everyFeedInventoryAdditionQuantity.add(feedInventory.quantity)}

                            feedAcquisitionsReportAdapter.setData(everyFeedInventoryAdditionDate, everyFeedInventoryAdditionQuantity)

                            val totalQuantityOfFeedAdded = everyFeedInventoryAdditionQuantity.sum()
                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                            tvTotalFeedAcquisitionValue.text = sumOfFeedQuantity
                        }}

                        mFeedInventoryAdditionViewModel.getFeedQuantityForFeedName(thePreviousWeek, theCurrentDate)
                        mFeedInventoryAdditionViewModel.feedQuantityForFeedName.observe(viewLifecycleOwner)
                        { feedAddition -> feedAddition.let { FeedInventoryAddition ->
                            val everyFeedInventoryAdditionName = mutableListOf<String>()
                            val everyFeedInventoryAdditionQuantity = mutableListOf<Double>()
                            for (feedInventory in FeedInventoryAddition){
                                everyFeedInventoryAdditionName.add(feedInventory.name)
                                everyFeedInventoryAdditionQuantity.add(feedInventory.quantity)}

                            feedAcquisitionsReportAdapter.setData(everyFeedInventoryAdditionName, everyFeedInventoryAdditionQuantity)

                            val totalQuantityOfFeedAdded = everyFeedInventoryAdditionQuantity.sum()
                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                            tvTotalFeedNameQuantity.text = sumOfFeedQuantity
                        }}

                        mFeedInventoryAdditionViewModel.getFeedCostForFeedName(thePreviousWeek, theCurrentDate)
                        mFeedInventoryAdditionViewModel.feedCostForFeedName.observe(viewLifecycleOwner)
                        { feedAddition -> feedAddition.let { FeedInventoryAddition ->
                            val everyFeedInventoryAdditionName = mutableListOf<String>()
                            val everyFeedInventoryAdditionCost = mutableListOf<Double>()
                            for (feedInventory in FeedInventoryAddition){
                                everyFeedInventoryAdditionName.add(feedInventory.name)
                                everyFeedInventoryAdditionCost.add(feedInventory.quantity)}

                            feedAcquisitionsReportAdapter.setData(everyFeedInventoryAdditionName, everyFeedInventoryAdditionCost)

                            val totalQuantityOfFeedAdded = everyFeedInventoryAdditionCost.sum()
                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                            tvTotalFeedNameCostValue.text = sumOfFeedQuantity
                        }}

                        mFeedInventoryReductionViewModel.getFeedQuantityForDate(thePreviousWeek, theCurrentDate)
                        mFeedInventoryReductionViewModel.feedQuantityForDate.observe(viewLifecycleOwner)
                        { feedReduction -> feedReduction.let { FeedInventoryReduction ->
                            val everyFeedInventoryReductionDate = mutableListOf<String>()
                            val feedInventoryReductionQuantity = mutableListOf<Double>()
                            for (feedInventory in FeedInventoryReduction){
                                everyFeedInventoryReductionDate.add(simpleDateFormat.format(feedInventory.date))
                                feedInventoryReductionQuantity.add(feedInventory.quantity)}

                            feedReductionReportAdapter.setData(everyFeedInventoryReductionDate, feedInventoryReductionQuantity)

                            val totalQuantityOfFeedAdded = feedInventoryReductionQuantity.sum()
                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                            tvTotalFeedReductionValue.text = sumOfFeedQuantity
                        }}

                        mFeedInventoryReductionViewModel.getFeedQuantityForFlockName(thePreviousWeek, theCurrentDate)
                        mFeedInventoryReductionViewModel.feedQuantityForFlockName.observe(viewLifecycleOwner)
                        { feedReduction -> feedReduction.let { FeedInventoryReduction ->
                            val everyFlockName = mutableListOf<String>()
                            val feedInventoryReductionQuantity = mutableListOf<Double>()
                            for (feedInventory in FeedInventoryReduction){
                                everyFlockName.add(feedInventory.name)
                                feedInventoryReductionQuantity.add(feedInventory.quantity)}

                            flockNameFeedQuantityReportAdapter.setData(everyFlockName, feedInventoryReductionQuantity)

                            val totalQuantityOfFeedAdded = feedInventoryReductionQuantity.sum()
                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                            tvTotalFlockNameFeedQuantityValue.text = sumOfFeedQuantity
                        }}

                        mFeedInventoryReductionViewModel.getFeedQuantityForFeedName(thePreviousWeek, theCurrentDate)
                        mFeedInventoryReductionViewModel.feedQuantityForFeedName.observe(viewLifecycleOwner)
                        { feedReduction -> feedReduction.let { FeedInventoryReduction ->
                            val everyFeedName = mutableListOf<String>()
                            val feedInventoryReductionQuantity = mutableListOf<Double>()
                            for (feedInventory in FeedInventoryReduction){
                                everyFeedName.add(feedInventory.name)
                                feedInventoryReductionQuantity.add(feedInventory.quantity)}

                            feedNameQuantityReducedReportAdapter.setData(everyFeedName, feedInventoryReductionQuantity)

                            val totalQuantityOfFeedAdded = feedInventoryReductionQuantity.sum()
                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                            tvTotalFeedNameQuantityReducedValue.text = sumOfFeedQuantity
                        }}

                        mFeedInventoryReductionViewModel.getFeedQuantityForReductionReason(thePreviousWeek, theCurrentDate)
                        mFeedInventoryReductionViewModel.feedQuantityForReductionReason.observe(viewLifecycleOwner)
                        { feedReduction -> feedReduction.let { FeedInventoryReduction ->
                            val everyReductionReason = mutableListOf<String>()
                            val feedInventoryReductionQuantity = mutableListOf<Double>()
                            for (feedInventory in FeedInventoryReduction){
                                everyReductionReason.add(feedInventory.name)
                                feedInventoryReductionQuantity.add(feedInventory.quantity)}

                            feedNameQuantityReducedReportAdapter.setData(everyReductionReason, feedInventoryReductionQuantity)

                            val totalQuantityOfFeedAdded = feedInventoryReductionQuantity.sum()
                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                            tvTotalFeedReductionReasonQuantityValue.text = sumOfFeedQuantity
                        }}
                        true
                    }
                    R.id.miThisMonth -> {
                        mFeedInventoryAdditionViewModel.getFeedQuantityForDate(thePreviousMonth, theCurrentDate)
                        mFeedInventoryAdditionViewModel.feedQuantityForDate.observe(viewLifecycleOwner)
                        { feedAddition -> feedAddition.let { FeedInventoryAddition ->
                            val everyFeedInventoryAdditionDate = mutableListOf<String>()
                            val everyFeedInventoryAdditionQuantity = mutableListOf<Double>()
                            for (feedInventory in FeedInventoryAddition){
                                everyFeedInventoryAdditionDate.add(simpleDateFormat.format(feedInventory.date))
                                everyFeedInventoryAdditionQuantity.add(feedInventory.quantity)}

                            feedAcquisitionsReportAdapter.setData(everyFeedInventoryAdditionDate, everyFeedInventoryAdditionQuantity)

                            val totalQuantityOfFeedAdded = everyFeedInventoryAdditionQuantity.sum()
                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                            tvTotalFeedAcquisitionValue.text = sumOfFeedQuantity
                        }}

                        mFeedInventoryAdditionViewModel.getFeedQuantityForFeedName(thePreviousMonth, theCurrentDate)
                        mFeedInventoryAdditionViewModel.feedQuantityForFeedName.observe(viewLifecycleOwner)
                        { feedAddition -> feedAddition.let { FeedInventoryAddition ->
                            val everyFeedInventoryAdditionName = mutableListOf<String>()
                            val everyFeedInventoryAdditionQuantity = mutableListOf<Double>()
                            for (feedInventory in FeedInventoryAddition){
                                everyFeedInventoryAdditionName.add(feedInventory.name)
                                everyFeedInventoryAdditionQuantity.add(feedInventory.quantity)}

                            feedAcquisitionsReportAdapter.setData(everyFeedInventoryAdditionName, everyFeedInventoryAdditionQuantity)

                            val totalQuantityOfFeedAdded = everyFeedInventoryAdditionQuantity.sum()
                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                            tvTotalFeedNameQuantity.text = sumOfFeedQuantity
                        }}

                        mFeedInventoryAdditionViewModel.getFeedCostForFeedName(thePreviousMonth, theCurrentDate)
                        mFeedInventoryAdditionViewModel.feedCostForFeedName.observe(viewLifecycleOwner)
                        { feedAddition -> feedAddition.let { FeedInventoryAddition ->
                            val everyFeedInventoryAdditionName = mutableListOf<String>()
                            val everyFeedInventoryAdditionCost = mutableListOf<Double>()
                            for (feedInventory in FeedInventoryAddition){
                                everyFeedInventoryAdditionName.add(feedInventory.name)
                                everyFeedInventoryAdditionCost.add(feedInventory.quantity)}

                            feedAcquisitionsReportAdapter.setData(everyFeedInventoryAdditionName, everyFeedInventoryAdditionCost)

                            val totalQuantityOfFeedAdded = everyFeedInventoryAdditionCost.sum()
                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                            tvTotalFeedNameCostValue.text = sumOfFeedQuantity
                        }}

                        mFeedInventoryReductionViewModel.getFeedQuantityForDate(thePreviousMonth, theCurrentDate)
                        mFeedInventoryReductionViewModel.feedQuantityForDate.observe(viewLifecycleOwner)
                        { feedReduction -> feedReduction.let { FeedInventoryReduction ->
                            val everyFeedInventoryReductionDate = mutableListOf<String>()
                            val feedInventoryReductionQuantity = mutableListOf<Double>()
                            for (feedInventory in FeedInventoryReduction){
                                everyFeedInventoryReductionDate.add(simpleDateFormat.format(feedInventory.date))
                                feedInventoryReductionQuantity.add(feedInventory.quantity)}

                            feedReductionReportAdapter.setData(everyFeedInventoryReductionDate, feedInventoryReductionQuantity)

                            val totalQuantityOfFeedAdded = feedInventoryReductionQuantity.sum()
                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                            tvTotalFeedReductionValue.text = sumOfFeedQuantity
                        }}

                        mFeedInventoryReductionViewModel.getFeedQuantityForFlockName(thePreviousMonth, theCurrentDate)
                        mFeedInventoryReductionViewModel.feedQuantityForFlockName.observe(viewLifecycleOwner)
                        { feedReduction -> feedReduction.let { FeedInventoryReduction ->
                            val everyFlockName = mutableListOf<String>()
                            val feedInventoryReductionQuantity = mutableListOf<Double>()
                            for (feedInventory in FeedInventoryReduction){
                                everyFlockName.add(feedInventory.name)
                                feedInventoryReductionQuantity.add(feedInventory.quantity)}

                            flockNameFeedQuantityReportAdapter.setData(everyFlockName, feedInventoryReductionQuantity)

                            val totalQuantityOfFeedAdded = feedInventoryReductionQuantity.sum()
                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                            tvTotalFlockNameFeedQuantityValue.text = sumOfFeedQuantity
                        }}

                        mFeedInventoryReductionViewModel.getFeedQuantityForFeedName(thePreviousMonth, theCurrentDate)
                        mFeedInventoryReductionViewModel.feedQuantityForFeedName.observe(viewLifecycleOwner)
                        { feedReduction -> feedReduction.let { FeedInventoryReduction ->
                            val everyFeedName = mutableListOf<String>()
                            val feedInventoryReductionQuantity = mutableListOf<Double>()
                            for (feedInventory in FeedInventoryReduction){
                                everyFeedName.add(feedInventory.name)
                                feedInventoryReductionQuantity.add(feedInventory.quantity)}

                            feedNameQuantityReducedReportAdapter.setData(everyFeedName, feedInventoryReductionQuantity)

                            val totalQuantityOfFeedAdded = feedInventoryReductionQuantity.sum()
                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                            tvTotalFeedNameQuantityReducedValue.text = sumOfFeedQuantity
                        }}

                        mFeedInventoryReductionViewModel.getFeedQuantityForReductionReason(thePreviousMonth, theCurrentDate)
                        mFeedInventoryReductionViewModel.feedQuantityForReductionReason.observe(viewLifecycleOwner)
                        { feedReduction -> feedReduction.let { FeedInventoryReduction ->
                            val everyReductionReason = mutableListOf<String>()
                            val feedInventoryReductionQuantity = mutableListOf<Double>()
                            for (feedInventory in FeedInventoryReduction){
                                everyReductionReason.add(feedInventory.name)
                                feedInventoryReductionQuantity.add(feedInventory.quantity)}

                            feedNameQuantityReducedReportAdapter.setData(everyReductionReason, feedInventoryReductionQuantity)

                            val totalQuantityOfFeedAdded = feedInventoryReductionQuantity.sum()
                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                            tvTotalFeedReductionReasonQuantityValue.text = sumOfFeedQuantity
                        }}
                        true
                    }
                    R.id.miPastThreeMonths -> {
                        mFeedInventoryAdditionViewModel.getFeedQuantityForDate(thePastThreeMonths, theCurrentDate)
                        mFeedInventoryAdditionViewModel.feedQuantityForDate.observe(viewLifecycleOwner)
                        { feedAddition -> feedAddition.let { FeedInventoryAddition ->
                            val everyFeedInventoryAdditionDate = mutableListOf<String>()
                            val everyFeedInventoryAdditionQuantity = mutableListOf<Double>()
                            for (feedInventory in FeedInventoryAddition){
                                everyFeedInventoryAdditionDate.add(simpleDateFormat.format(feedInventory.date))
                                everyFeedInventoryAdditionQuantity.add(feedInventory.quantity)}

                            feedAcquisitionsReportAdapter.setData(everyFeedInventoryAdditionDate, everyFeedInventoryAdditionQuantity)

                            val totalQuantityOfFeedAdded = everyFeedInventoryAdditionQuantity.sum()
                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                            tvTotalFeedAcquisitionValue.text = sumOfFeedQuantity
                        }}

                        mFeedInventoryAdditionViewModel.getFeedQuantityForFeedName(thePastThreeMonths, theCurrentDate)
                        mFeedInventoryAdditionViewModel.feedQuantityForFeedName.observe(viewLifecycleOwner)
                        { feedAddition -> feedAddition.let { FeedInventoryAddition ->
                            val everyFeedInventoryAdditionName = mutableListOf<String>()
                            val everyFeedInventoryAdditionQuantity = mutableListOf<Double>()
                            for (feedInventory in FeedInventoryAddition){
                                everyFeedInventoryAdditionName.add(feedInventory.name)
                                everyFeedInventoryAdditionQuantity.add(feedInventory.quantity)}

                            feedAcquisitionsReportAdapter.setData(everyFeedInventoryAdditionName, everyFeedInventoryAdditionQuantity)

                            val totalQuantityOfFeedAdded = everyFeedInventoryAdditionQuantity.sum()
                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                            tvTotalFeedNameQuantity.text = sumOfFeedQuantity
                        }}

                        mFeedInventoryAdditionViewModel.getFeedCostForFeedName(thePastThreeMonths, theCurrentDate)
                        mFeedInventoryAdditionViewModel.feedCostForFeedName.observe(viewLifecycleOwner)
                        { feedAddition -> feedAddition.let { FeedInventoryAddition ->
                            val everyFeedInventoryAdditionName = mutableListOf<String>()
                            val everyFeedInventoryAdditionCost = mutableListOf<Double>()
                            for (feedInventory in FeedInventoryAddition){
                                everyFeedInventoryAdditionName.add(feedInventory.name)
                                everyFeedInventoryAdditionCost.add(feedInventory.quantity)}

                            feedAcquisitionsReportAdapter.setData(everyFeedInventoryAdditionName, everyFeedInventoryAdditionCost)

                            val totalQuantityOfFeedAdded = everyFeedInventoryAdditionCost.sum()
                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                            tvTotalFeedNameCostValue.text = sumOfFeedQuantity
                        }}

                        mFeedInventoryReductionViewModel.getFeedQuantityForDate(thePastThreeMonths, theCurrentDate)
                        mFeedInventoryReductionViewModel.feedQuantityForDate.observe(viewLifecycleOwner)
                        { feedReduction -> feedReduction.let { FeedInventoryReduction ->
                            val everyFeedInventoryReductionDate = mutableListOf<String>()
                            val feedInventoryReductionQuantity = mutableListOf<Double>()
                            for (feedInventory in FeedInventoryReduction){
                                everyFeedInventoryReductionDate.add(simpleDateFormat.format(feedInventory.date))
                                feedInventoryReductionQuantity.add(feedInventory.quantity)}

                            feedReductionReportAdapter.setData(everyFeedInventoryReductionDate, feedInventoryReductionQuantity)

                            val totalQuantityOfFeedAdded = feedInventoryReductionQuantity.sum()
                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                            tvTotalFeedReductionValue.text = sumOfFeedQuantity
                        }}

                        mFeedInventoryReductionViewModel.getFeedQuantityForFlockName(thePastThreeMonths, theCurrentDate)
                        mFeedInventoryReductionViewModel.feedQuantityForFlockName.observe(viewLifecycleOwner)
                        { feedReduction -> feedReduction.let { FeedInventoryReduction ->
                            val everyFlockName = mutableListOf<String>()
                            val feedInventoryReductionQuantity = mutableListOf<Double>()
                            for (feedInventory in FeedInventoryReduction){
                                everyFlockName.add(feedInventory.name)
                                feedInventoryReductionQuantity.add(feedInventory.quantity)}

                            flockNameFeedQuantityReportAdapter.setData(everyFlockName, feedInventoryReductionQuantity)

                            val totalQuantityOfFeedAdded = feedInventoryReductionQuantity.sum()
                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                            tvTotalFlockNameFeedQuantityValue.text = sumOfFeedQuantity
                        }}

                        mFeedInventoryReductionViewModel.getFeedQuantityForFeedName(thePastThreeMonths, theCurrentDate)
                        mFeedInventoryReductionViewModel.feedQuantityForFeedName.observe(viewLifecycleOwner)
                        { feedReduction -> feedReduction.let { FeedInventoryReduction ->
                            val everyFeedName = mutableListOf<String>()
                            val feedInventoryReductionQuantity = mutableListOf<Double>()
                            for (feedInventory in FeedInventoryReduction){
                                everyFeedName.add(feedInventory.name)
                                feedInventoryReductionQuantity.add(feedInventory.quantity)}

                            feedNameQuantityReducedReportAdapter.setData(everyFeedName, feedInventoryReductionQuantity)

                            val totalQuantityOfFeedAdded = feedInventoryReductionQuantity.sum()
                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                            tvTotalFeedNameQuantityReducedValue.text = sumOfFeedQuantity
                        }}

                        mFeedInventoryReductionViewModel.getFeedQuantityForReductionReason(thePastThreeMonths, theCurrentDate)
                        mFeedInventoryReductionViewModel.feedQuantityForReductionReason.observe(viewLifecycleOwner)
                        { feedReduction -> feedReduction.let { FeedInventoryReduction ->
                            val everyReductionReason = mutableListOf<String>()
                            val feedInventoryReductionQuantity = mutableListOf<Double>()
                            for (feedInventory in FeedInventoryReduction){
                                everyReductionReason.add(feedInventory.name)
                                feedInventoryReductionQuantity.add(feedInventory.quantity)}

                            feedNameQuantityReducedReportAdapter.setData(everyReductionReason, feedInventoryReductionQuantity)

                            val totalQuantityOfFeedAdded = feedInventoryReductionQuantity.sum()
                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                            tvTotalFeedReductionReasonQuantityValue.text = sumOfFeedQuantity
                        }}
                        true
                    }
                    R.id.miPastSixMonths -> {
                        mFeedInventoryAdditionViewModel.getFeedQuantityForDate(thePastSixMonths, theCurrentDate)
                        mFeedInventoryAdditionViewModel.feedQuantityForDate.observe(viewLifecycleOwner)
                        { feedAddition -> feedAddition.let { FeedInventoryAddition ->
                            val everyFeedInventoryAdditionDate = mutableListOf<String>()
                            val everyFeedInventoryAdditionQuantity = mutableListOf<Double>()
                            for (feedInventory in FeedInventoryAddition){
                                everyFeedInventoryAdditionDate.add(simpleDateFormat.format(feedInventory.date))
                                everyFeedInventoryAdditionQuantity.add(feedInventory.quantity)}

                            feedAcquisitionsReportAdapter.setData(everyFeedInventoryAdditionDate, everyFeedInventoryAdditionQuantity)

                            val totalQuantityOfFeedAdded = everyFeedInventoryAdditionQuantity.sum()
                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                            tvTotalFeedAcquisitionValue.text = sumOfFeedQuantity
                        }}

                        mFeedInventoryAdditionViewModel.getFeedQuantityForFeedName(thePastSixMonths, theCurrentDate)
                        mFeedInventoryAdditionViewModel.feedQuantityForFeedName.observe(viewLifecycleOwner)
                        { feedAddition -> feedAddition.let { FeedInventoryAddition ->
                            val everyFeedInventoryAdditionName = mutableListOf<String>()
                            val everyFeedInventoryAdditionQuantity = mutableListOf<Double>()
                            for (feedInventory in FeedInventoryAddition){
                                everyFeedInventoryAdditionName.add(feedInventory.name)
                                everyFeedInventoryAdditionQuantity.add(feedInventory.quantity)}

                            feedAcquisitionsReportAdapter.setData(everyFeedInventoryAdditionName, everyFeedInventoryAdditionQuantity)

                            val totalQuantityOfFeedAdded = everyFeedInventoryAdditionQuantity.sum()
                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                            tvTotalFeedNameQuantity.text = sumOfFeedQuantity
                        }}

                        mFeedInventoryAdditionViewModel.getFeedCostForFeedName(thePastSixMonths, theCurrentDate)
                        mFeedInventoryAdditionViewModel.feedCostForFeedName.observe(viewLifecycleOwner)
                        { feedAddition -> feedAddition.let { FeedInventoryAddition ->
                            val everyFeedInventoryAdditionName = mutableListOf<String>()
                            val everyFeedInventoryAdditionCost = mutableListOf<Double>()
                            for (feedInventory in FeedInventoryAddition){
                                everyFeedInventoryAdditionName.add(feedInventory.name)
                                everyFeedInventoryAdditionCost.add(feedInventory.quantity)}

                            feedAcquisitionsReportAdapter.setData(everyFeedInventoryAdditionName, everyFeedInventoryAdditionCost)

                            val totalQuantityOfFeedAdded = everyFeedInventoryAdditionCost.sum()
                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                            tvTotalFeedNameCostValue.text = sumOfFeedQuantity
                        }}

                        mFeedInventoryReductionViewModel.getFeedQuantityForDate(thePastSixMonths, theCurrentDate)
                        mFeedInventoryReductionViewModel.feedQuantityForDate.observe(viewLifecycleOwner)
                        { feedReduction -> feedReduction.let { FeedInventoryReduction ->
                            val everyFeedInventoryReductionDate = mutableListOf<String>()
                            val feedInventoryReductionQuantity = mutableListOf<Double>()
                            for (feedInventory in FeedInventoryReduction){
                                everyFeedInventoryReductionDate.add(simpleDateFormat.format(feedInventory.date))
                                feedInventoryReductionQuantity.add(feedInventory.quantity)}

                            feedReductionReportAdapter.setData(everyFeedInventoryReductionDate, feedInventoryReductionQuantity)

                            val totalQuantityOfFeedAdded = feedInventoryReductionQuantity.sum()
                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                            tvTotalFeedReductionValue.text = sumOfFeedQuantity
                        }}

                        mFeedInventoryReductionViewModel.getFeedQuantityForFlockName(thePastSixMonths, theCurrentDate)
                        mFeedInventoryReductionViewModel.feedQuantityForFlockName.observe(viewLifecycleOwner)
                        { feedReduction -> feedReduction.let { FeedInventoryReduction ->
                            val everyFlockName = mutableListOf<String>()
                            val feedInventoryReductionQuantity = mutableListOf<Double>()
                            for (feedInventory in FeedInventoryReduction){
                                everyFlockName.add(feedInventory.name)
                                feedInventoryReductionQuantity.add(feedInventory.quantity)}

                            flockNameFeedQuantityReportAdapter.setData(everyFlockName, feedInventoryReductionQuantity)

                            val totalQuantityOfFeedAdded = feedInventoryReductionQuantity.sum()
                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                            tvTotalFlockNameFeedQuantityValue.text = sumOfFeedQuantity
                        }}

                        mFeedInventoryReductionViewModel.getFeedQuantityForFeedName(thePastSixMonths, theCurrentDate)
                        mFeedInventoryReductionViewModel.feedQuantityForFeedName.observe(viewLifecycleOwner)
                        { feedReduction -> feedReduction.let { FeedInventoryReduction ->
                            val everyFeedName = mutableListOf<String>()
                            val feedInventoryReductionQuantity = mutableListOf<Double>()
                            for (feedInventory in FeedInventoryReduction){
                                everyFeedName.add(feedInventory.name)
                                feedInventoryReductionQuantity.add(feedInventory.quantity)}

                            feedNameQuantityReducedReportAdapter.setData(everyFeedName, feedInventoryReductionQuantity)

                            val totalQuantityOfFeedAdded = feedInventoryReductionQuantity.sum()
                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                            tvTotalFeedNameQuantityReducedValue.text = sumOfFeedQuantity
                        }}

                        mFeedInventoryReductionViewModel.getFeedQuantityForReductionReason(thePastSixMonths, theCurrentDate)
                        mFeedInventoryReductionViewModel.feedQuantityForReductionReason.observe(viewLifecycleOwner)
                        { feedReduction -> feedReduction.let { FeedInventoryReduction ->
                            val everyReductionReason = mutableListOf<String>()
                            val feedInventoryReductionQuantity = mutableListOf<Double>()
                            for (feedInventory in FeedInventoryReduction){
                                everyReductionReason.add(feedInventory.name)
                                feedInventoryReductionQuantity.add(feedInventory.quantity)}

                            feedNameQuantityReducedReportAdapter.setData(everyReductionReason, feedInventoryReductionQuantity)

                            val totalQuantityOfFeedAdded = feedInventoryReductionQuantity.sum()
                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                            tvTotalFeedReductionReasonQuantityValue.text = sumOfFeedQuantity
                        }}

                        true
                    }
                    R.id.miPastYear -> {
                        mFeedInventoryAdditionViewModel.getFeedQuantityForDate(thePastYear, theCurrentDate)
                        mFeedInventoryAdditionViewModel.feedQuantityForDate.observe(viewLifecycleOwner)
                        { feedAddition -> feedAddition.let { FeedInventoryAddition ->
                            val everyFeedInventoryAdditionDate = mutableListOf<String>()
                            val everyFeedInventoryAdditionQuantity = mutableListOf<Double>()
                            for (feedInventory in FeedInventoryAddition){
                                everyFeedInventoryAdditionDate.add(simpleDateFormat.format(feedInventory.date))
                                everyFeedInventoryAdditionQuantity.add(feedInventory.quantity)}

                            feedAcquisitionsReportAdapter.setData(everyFeedInventoryAdditionDate, everyFeedInventoryAdditionQuantity)

                            val totalQuantityOfFeedAdded = everyFeedInventoryAdditionQuantity.sum()
                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                            tvTotalFeedAcquisitionValue.text = sumOfFeedQuantity
                        }}

                        mFeedInventoryAdditionViewModel.getFeedQuantityForFeedName(thePastYear, theCurrentDate)
                        mFeedInventoryAdditionViewModel.feedQuantityForFeedName.observe(viewLifecycleOwner)
                        { feedAddition -> feedAddition.let { FeedInventoryAddition ->
                            val everyFeedInventoryAdditionName = mutableListOf<String>()
                            val everyFeedInventoryAdditionQuantity = mutableListOf<Double>()
                            for (feedInventory in FeedInventoryAddition){
                                everyFeedInventoryAdditionName.add(feedInventory.name)
                                everyFeedInventoryAdditionQuantity.add(feedInventory.quantity)}

                            feedAcquisitionsReportAdapter.setData(everyFeedInventoryAdditionName, everyFeedInventoryAdditionQuantity)

                            val totalQuantityOfFeedAdded = everyFeedInventoryAdditionQuantity.sum()
                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                            tvTotalFeedNameQuantity.text = sumOfFeedQuantity
                        }}

                        mFeedInventoryAdditionViewModel.getFeedCostForFeedName(thePastYear, theCurrentDate)
                        mFeedInventoryAdditionViewModel.feedCostForFeedName.observe(viewLifecycleOwner)
                        { feedAddition -> feedAddition.let { FeedInventoryAddition ->
                            val everyFeedInventoryAdditionName = mutableListOf<String>()
                            val everyFeedInventoryAdditionCost = mutableListOf<Double>()
                            for (feedInventory in FeedInventoryAddition){
                                everyFeedInventoryAdditionName.add(feedInventory.name)
                                everyFeedInventoryAdditionCost.add(feedInventory.quantity)}

                            feedAcquisitionsReportAdapter.setData(everyFeedInventoryAdditionName, everyFeedInventoryAdditionCost)

                            val totalQuantityOfFeedAdded = everyFeedInventoryAdditionCost.sum()
                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                            tvTotalFeedNameCostValue.text = sumOfFeedQuantity
                        }}

                        mFeedInventoryReductionViewModel.getFeedQuantityForDate(thePastYear, theCurrentDate)
                        mFeedInventoryReductionViewModel.feedQuantityForDate.observe(viewLifecycleOwner)
                        { feedReduction -> feedReduction.let { FeedInventoryReduction ->
                            val everyFeedInventoryReductionDate = mutableListOf<String>()
                            val feedInventoryReductionQuantity = mutableListOf<Double>()
                            for (feedInventory in FeedInventoryReduction){
                                everyFeedInventoryReductionDate.add(simpleDateFormat.format(feedInventory.date))
                                feedInventoryReductionQuantity.add(feedInventory.quantity)}

                            feedReductionReportAdapter.setData(everyFeedInventoryReductionDate, feedInventoryReductionQuantity)

                            val totalQuantityOfFeedAdded = feedInventoryReductionQuantity.sum()
                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                            tvTotalFeedReductionValue.text = sumOfFeedQuantity
                        }}

                        mFeedInventoryReductionViewModel.getFeedQuantityForFlockName(thePastYear, theCurrentDate)
                        mFeedInventoryReductionViewModel.feedQuantityForFlockName.observe(viewLifecycleOwner)
                        { feedReduction -> feedReduction.let { FeedInventoryReduction ->
                            val everyFlockName = mutableListOf<String>()
                            val feedInventoryReductionQuantity = mutableListOf<Double>()
                            for (feedInventory in FeedInventoryReduction){
                                everyFlockName.add(feedInventory.name)
                                feedInventoryReductionQuantity.add(feedInventory.quantity)}

                            flockNameFeedQuantityReportAdapter.setData(everyFlockName, feedInventoryReductionQuantity)

                            val totalQuantityOfFeedAdded = feedInventoryReductionQuantity.sum()
                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                            tvTotalFlockNameFeedQuantityValue.text = sumOfFeedQuantity
                        }}

                        mFeedInventoryReductionViewModel.getFeedQuantityForFeedName(thePastYear, theCurrentDate)
                        mFeedInventoryReductionViewModel.feedQuantityForFeedName.observe(viewLifecycleOwner)
                        { feedReduction -> feedReduction.let { FeedInventoryReduction ->
                            val everyFeedName = mutableListOf<String>()
                            val feedInventoryReductionQuantity = mutableListOf<Double>()
                            for (feedInventory in FeedInventoryReduction){
                                everyFeedName.add(feedInventory.name)
                                feedInventoryReductionQuantity.add(feedInventory.quantity)}

                            feedNameQuantityReducedReportAdapter.setData(everyFeedName, feedInventoryReductionQuantity)

                            val totalQuantityOfFeedAdded = feedInventoryReductionQuantity.sum()
                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                            tvTotalFeedNameQuantityReducedValue.text = sumOfFeedQuantity
                        }}

                        mFeedInventoryReductionViewModel.getFeedQuantityForReductionReason(thePastYear, theCurrentDate)
                        mFeedInventoryReductionViewModel.feedQuantityForReductionReason.observe(viewLifecycleOwner)
                        { feedReduction -> feedReduction.let { FeedInventoryReduction ->
                            val everyReductionReason = mutableListOf<String>()
                            val feedInventoryReductionQuantity = mutableListOf<Double>()
                            for (feedInventory in FeedInventoryReduction){
                                everyReductionReason.add(feedInventory.name)
                                feedInventoryReductionQuantity.add(feedInventory.quantity)}

                            feedNameQuantityReducedReportAdapter.setData(everyReductionReason, feedInventoryReductionQuantity)

                            val totalQuantityOfFeedAdded = feedInventoryReductionQuantity.sum()
                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                            tvTotalFeedReductionReasonQuantityValue.text = sumOfFeedQuantity
                        }}

                        true
                    }
                    R.id.miPastTwoYears -> {
                        mFeedInventoryAdditionViewModel.getFeedQuantityForDate(thePastTwoYears, theCurrentDate)
                        mFeedInventoryAdditionViewModel.feedQuantityForDate.observe(viewLifecycleOwner)
                        { feedAddition -> feedAddition.let { FeedInventoryAddition ->
                            val everyFeedInventoryAdditionDate = mutableListOf<String>()
                            val everyFeedInventoryAdditionQuantity = mutableListOf<Double>()
                            for (feedInventory in FeedInventoryAddition){
                                everyFeedInventoryAdditionDate.add(simpleDateFormat.format(feedInventory.date))
                                everyFeedInventoryAdditionQuantity.add(feedInventory.quantity)}

                            feedAcquisitionsReportAdapter.setData(everyFeedInventoryAdditionDate, everyFeedInventoryAdditionQuantity)

                            val totalQuantityOfFeedAdded = everyFeedInventoryAdditionQuantity.sum()
                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                            tvTotalFeedAcquisitionValue.text = sumOfFeedQuantity
                        }}

                        mFeedInventoryAdditionViewModel.getFeedQuantityForFeedName(thePastTwoYears, theCurrentDate)
                        mFeedInventoryAdditionViewModel.feedQuantityForFeedName.observe(viewLifecycleOwner)
                        { feedAddition -> feedAddition.let { FeedInventoryAddition ->
                            val everyFeedInventoryAdditionName = mutableListOf<String>()
                            val everyFeedInventoryAdditionQuantity = mutableListOf<Double>()
                            for (feedInventory in FeedInventoryAddition){
                                everyFeedInventoryAdditionName.add(feedInventory.name)
                                everyFeedInventoryAdditionQuantity.add(feedInventory.quantity)}

                            feedAcquisitionsReportAdapter.setData(everyFeedInventoryAdditionName, everyFeedInventoryAdditionQuantity)

                            val totalQuantityOfFeedAdded = everyFeedInventoryAdditionQuantity.sum()
                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                            tvTotalFeedNameQuantity.text = sumOfFeedQuantity
                        }}

                        mFeedInventoryAdditionViewModel.getFeedCostForFeedName(thePastTwoYears, theCurrentDate)
                        mFeedInventoryAdditionViewModel.feedCostForFeedName.observe(viewLifecycleOwner)
                        { feedAddition -> feedAddition.let { FeedInventoryAddition ->
                            val everyFeedInventoryAdditionName = mutableListOf<String>()
                            val everyFeedInventoryAdditionCost = mutableListOf<Double>()
                            for (feedInventory in FeedInventoryAddition){
                                everyFeedInventoryAdditionName.add(feedInventory.name)
                                everyFeedInventoryAdditionCost.add(feedInventory.quantity)}

                            feedAcquisitionsReportAdapter.setData(everyFeedInventoryAdditionName, everyFeedInventoryAdditionCost)

                            val totalQuantityOfFeedAdded = everyFeedInventoryAdditionCost.sum()
                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                            tvTotalFeedNameCostValue.text = sumOfFeedQuantity
                        }}

                        mFeedInventoryReductionViewModel.getFeedQuantityForDate(thePastTwoYears, theCurrentDate)
                        mFeedInventoryReductionViewModel.feedQuantityForDate.observe(viewLifecycleOwner)
                        { feedReduction -> feedReduction.let { FeedInventoryReduction ->
                            val everyFeedInventoryReductionDate = mutableListOf<String>()
                            val feedInventoryReductionQuantity = mutableListOf<Double>()
                            for (feedInventory in FeedInventoryReduction){
                                everyFeedInventoryReductionDate.add(simpleDateFormat.format(feedInventory.date))
                                feedInventoryReductionQuantity.add(feedInventory.quantity)}

                            feedReductionReportAdapter.setData(everyFeedInventoryReductionDate, feedInventoryReductionQuantity)

                            val totalQuantityOfFeedAdded = feedInventoryReductionQuantity.sum()
                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                            tvTotalFeedReductionValue.text = sumOfFeedQuantity
                        }}

                        mFeedInventoryReductionViewModel.getFeedQuantityForFlockName(thePastTwoYears, theCurrentDate)
                        mFeedInventoryReductionViewModel.feedQuantityForFlockName.observe(viewLifecycleOwner)
                        { feedReduction -> feedReduction.let { FeedInventoryReduction ->
                            val everyFlockName = mutableListOf<String>()
                            val feedInventoryReductionQuantity = mutableListOf<Double>()
                            for (feedInventory in FeedInventoryReduction){
                                everyFlockName.add(feedInventory.name)
                                feedInventoryReductionQuantity.add(feedInventory.quantity)}

                            flockNameFeedQuantityReportAdapter.setData(everyFlockName, feedInventoryReductionQuantity)

                            val totalQuantityOfFeedAdded = feedInventoryReductionQuantity.sum()
                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                            tvTotalFlockNameFeedQuantityValue.text = sumOfFeedQuantity
                        }}

                        mFeedInventoryReductionViewModel.getFeedQuantityForFeedName(thePastTwoYears, theCurrentDate)
                        mFeedInventoryReductionViewModel.feedQuantityForFeedName.observe(viewLifecycleOwner)
                        { feedReduction -> feedReduction.let { FeedInventoryReduction ->
                            val everyFeedName = mutableListOf<String>()
                            val feedInventoryReductionQuantity = mutableListOf<Double>()
                            for (feedInventory in FeedInventoryReduction){
                                everyFeedName.add(feedInventory.name)
                                feedInventoryReductionQuantity.add(feedInventory.quantity)}

                            feedNameQuantityReducedReportAdapter.setData(everyFeedName, feedInventoryReductionQuantity)

                            val totalQuantityOfFeedAdded = feedInventoryReductionQuantity.sum()
                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                            tvTotalFeedNameQuantityReducedValue.text = sumOfFeedQuantity
                        }}

                        mFeedInventoryReductionViewModel.getFeedQuantityForReductionReason(thePastTwoYears, theCurrentDate)
                        mFeedInventoryReductionViewModel.feedQuantityForReductionReason.observe(viewLifecycleOwner)
                        { feedReduction -> feedReduction.let { FeedInventoryReduction ->
                            val everyReductionReason = mutableListOf<String>()
                            val feedInventoryReductionQuantity = mutableListOf<Double>()
                            for (feedInventory in FeedInventoryReduction){
                                everyReductionReason.add(feedInventory.name)
                                feedInventoryReductionQuantity.add(feedInventory.quantity)}

                            feedNameQuantityReducedReportAdapter.setData(everyReductionReason, feedInventoryReductionQuantity)

                            val totalQuantityOfFeedAdded = feedInventoryReductionQuantity.sum()
                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                            tvTotalFeedReductionReasonQuantityValue.text = sumOfFeedQuantity
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
                                        mFeedInventoryAdditionViewModel.getFeedQuantityForDate(theFirstDate, theLastDate)
                                        mFeedInventoryAdditionViewModel.feedQuantityForDate.observe(viewLifecycleOwner)
                                        { feedAddition -> feedAddition.let { FeedInventoryAddition ->
                                            val everyFeedInventoryAdditionDate = mutableListOf<String>()
                                            val everyFeedInventoryAdditionQuantity = mutableListOf<Double>()
                                            for (feedInventory in FeedInventoryAddition){
                                                everyFeedInventoryAdditionDate.add(simpleDateFormat.format(feedInventory.date))
                                                everyFeedInventoryAdditionQuantity.add(feedInventory.quantity)}

                                            feedAcquisitionsReportAdapter.setData(everyFeedInventoryAdditionDate, everyFeedInventoryAdditionQuantity)

                                            val totalQuantityOfFeedAdded = everyFeedInventoryAdditionQuantity.sum()
                                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                                            tvTotalFeedAcquisitionValue.text = sumOfFeedQuantity
                                        }}

                                        mFeedInventoryAdditionViewModel.getFeedQuantityForFeedName(theFirstDate, theLastDate)
                                        mFeedInventoryAdditionViewModel.feedQuantityForFeedName.observe(viewLifecycleOwner)
                                        { feedAddition -> feedAddition.let { FeedInventoryAddition ->
                                            val everyFeedInventoryAdditionName = mutableListOf<String>()
                                            val everyFeedInventoryAdditionQuantity = mutableListOf<Double>()
                                            for (feedInventory in FeedInventoryAddition){
                                                everyFeedInventoryAdditionName.add(feedInventory.name)
                                                everyFeedInventoryAdditionQuantity.add(feedInventory.quantity)}

                                            feedAcquisitionsReportAdapter.setData(everyFeedInventoryAdditionName, everyFeedInventoryAdditionQuantity)

                                            val totalQuantityOfFeedAdded = everyFeedInventoryAdditionQuantity.sum()
                                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                                            tvTotalFeedNameQuantity.text = sumOfFeedQuantity
                                        }}

                                        mFeedInventoryAdditionViewModel.getFeedCostForFeedName(theFirstDate, theLastDate)
                                        mFeedInventoryAdditionViewModel.feedCostForFeedName.observe(viewLifecycleOwner)
                                        { feedAddition -> feedAddition.let { FeedInventoryAddition ->
                                            val everyFeedInventoryAdditionName = mutableListOf<String>()
                                            val everyFeedInventoryAdditionCost = mutableListOf<Double>()
                                            for (feedInventory in FeedInventoryAddition){
                                                everyFeedInventoryAdditionName.add(feedInventory.name)
                                                everyFeedInventoryAdditionCost.add(feedInventory.quantity)}

                                            feedAcquisitionsReportAdapter.setData(everyFeedInventoryAdditionName, everyFeedInventoryAdditionCost)

                                            val totalQuantityOfFeedAdded = everyFeedInventoryAdditionCost.sum()
                                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                                            tvTotalFeedNameCostValue.text = sumOfFeedQuantity
                                        }}

                                        mFeedInventoryReductionViewModel.getFeedQuantityForDate(theFirstDate, theLastDate)
                                        mFeedInventoryReductionViewModel.feedQuantityForDate.observe(viewLifecycleOwner)
                                        { feedReduction -> feedReduction.let { FeedInventoryReduction ->
                                            val everyFeedInventoryReductionDate = mutableListOf<String>()
                                            val feedInventoryReductionQuantity = mutableListOf<Double>()
                                            for (feedInventory in FeedInventoryReduction){
                                                everyFeedInventoryReductionDate.add(simpleDateFormat.format(feedInventory.date))
                                                feedInventoryReductionQuantity.add(feedInventory.quantity)}

                                            feedReductionReportAdapter.setData(everyFeedInventoryReductionDate, feedInventoryReductionQuantity)

                                            val totalQuantityOfFeedAdded = feedInventoryReductionQuantity.sum()
                                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                                            tvTotalFeedReductionValue.text = sumOfFeedQuantity
                                        }}

                                        mFeedInventoryReductionViewModel.getFeedQuantityForFlockName(theFirstDate, theLastDate)
                                        mFeedInventoryReductionViewModel.feedQuantityForFlockName.observe(viewLifecycleOwner)
                                        { feedReduction -> feedReduction.let { FeedInventoryReduction ->
                                            val everyFlockName = mutableListOf<String>()
                                            val feedInventoryReductionQuantity = mutableListOf<Double>()
                                            for (feedInventory in FeedInventoryReduction){
                                                everyFlockName.add(feedInventory.name)
                                                feedInventoryReductionQuantity.add(feedInventory.quantity)}

                                            flockNameFeedQuantityReportAdapter.setData(everyFlockName, feedInventoryReductionQuantity)

                                            val totalQuantityOfFeedAdded = feedInventoryReductionQuantity.sum()
                                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                                            tvTotalFlockNameFeedQuantityValue.text = sumOfFeedQuantity
                                        }}

                                        mFeedInventoryReductionViewModel.getFeedQuantityForFeedName(theFirstDate, theLastDate)
                                        mFeedInventoryReductionViewModel.feedQuantityForFeedName.observe(viewLifecycleOwner)
                                        { feedReduction -> feedReduction.let { FeedInventoryReduction ->
                                            val everyFeedName = mutableListOf<String>()
                                            val feedInventoryReductionQuantity = mutableListOf<Double>()
                                            for (feedInventory in FeedInventoryReduction){
                                                everyFeedName.add(feedInventory.name)
                                                feedInventoryReductionQuantity.add(feedInventory.quantity)}

                                            feedNameQuantityReducedReportAdapter.setData(everyFeedName, feedInventoryReductionQuantity)

                                            val totalQuantityOfFeedAdded = feedInventoryReductionQuantity.sum()
                                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                                            tvTotalFeedNameQuantityReducedValue.text = sumOfFeedQuantity
                                        }}

                                        mFeedInventoryReductionViewModel.getFeedQuantityForReductionReason(theFirstDate, theLastDate)
                                        mFeedInventoryReductionViewModel.feedQuantityForReductionReason.observe(viewLifecycleOwner)
                                        { feedReduction -> feedReduction.let { FeedInventoryReduction ->
                                            val everyReductionReason = mutableListOf<String>()
                                            val feedInventoryReductionQuantity = mutableListOf<Double>()
                                            for (feedInventory in FeedInventoryReduction){
                                                everyReductionReason.add(feedInventory.name)
                                                feedInventoryReductionQuantity.add(feedInventory.quantity)}

                                            feedNameQuantityReducedReportAdapter.setData(everyReductionReason, feedInventoryReductionQuantity)

                                            val totalQuantityOfFeedAdded = feedInventoryReductionQuantity.sum()
                                            val sumOfFeedQuantity = "$totalQuantityOfFeedAdded $measuringUnit"
                                            tvTotalFeedReductionReasonQuantityValue.text = sumOfFeedQuantity
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

        feedNameQuantityAddedReportAdapter.setOnItemClickListener(object : FeedQuantityReportAdapter.OnItemClickListener{
            override fun onItemDateClickListener(date: String) {
                TODO("Not yet implemented")
            }

            override fun onItemQuantityClickListener(quantity: Double) {
                TODO("Not yet implemented")
            }

        })

        flockNameFeedQuantityReportAdapter.setOnItemClickListener(object : FeedQuantityReportAdapter.OnItemClickListener{
            override fun onItemDateClickListener(date: String) {
                TODO("Not yet implemented")
            }

            override fun onItemQuantityClickListener(quantity: Double) {
                TODO("Not yet implemented")
            }

        })

        feedReductionReasonQuantityReportAdapter.setOnItemClickListener(object : FeedQuantityReportAdapter.OnItemClickListener{
            override fun onItemDateClickListener(date: String) {
                TODO("Not yet implemented")
            }

            override fun onItemQuantityClickListener(quantity: Double) {
                TODO("Not yet implemented")
            }

        })

        feedNameQuantityReducedReportAdapter.setOnItemClickListener(object : FeedQuantityReportAdapter.OnItemClickListener{
            override fun onItemDateClickListener(date: String) {
                TODO("Not yet implemented")
            }

            override fun onItemQuantityClickListener(quantity: Double) {
                TODO("Not yet implemented")
            }

        })

        feedAcquisitionsReportAdapter.setOnItemClickListener(object : FeedQuantityReportAdapter.OnItemClickListener{
            override fun onItemDateClickListener(date: String) {
                TODO("Not yet implemented")
            }

            override fun onItemQuantityClickListener(quantity: Double) {
                TODO("Not yet implemented")
            }

        })

        feedReductionReportAdapter.setOnItemClickListener(object : FeedQuantityReportAdapter.OnItemClickListener{
            override fun onItemDateClickListener(date: String) {
                TODO("Not yet implemented")
            }

            override fun onItemQuantityClickListener(quantity: Double) {
                TODO("Not yet implemented")
            }

        })


        feedNameCostReportAdapter.setOnItemClickListener(object : FeedQuantityReportAdapter.OnItemClickListener{
            override fun onItemDateClickListener(date: String) {
                TODO("Not yet implemented")
            }

            override fun onItemQuantityClickListener(quantity: Double) {
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