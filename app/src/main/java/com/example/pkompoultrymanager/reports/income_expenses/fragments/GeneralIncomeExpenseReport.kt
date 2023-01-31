package com.example.pkompoultrymanager.reports.income_expenses.fragments

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
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
import com.example.pkompoultrymanager.R
import com.example.pkompoultrymanager.health.medication.MedicationViewModel
import com.example.pkompoultrymanager.health.vaccination.VaccinationViewModel
import com.example.pkompoultrymanager.inventory.eggs.addition.EggInventoryAdditionViewModel
import com.example.pkompoultrymanager.inventory.eggs.reduction.EggInventoryReductionViewModel
import com.example.pkompoultrymanager.inventory.feed.addition.FeedInventoryAdditionViewModel
import com.example.pkompoultrymanager.inventory.feed.reduction.FeedInventoryReductionViewModel
import com.example.pkompoultrymanager.inventory.flock.addition.FlockInventoryAdditionViewModel
import com.example.pkompoultrymanager.inventory.flock.reduction.FlockInventoryReductionViewModel
import com.example.pkompoultrymanager.tables.egg_type.EggTypeViewModel
import com.example.pkompoultrymanager.transactions.cash_in.alternative_income.AlternativeIncomeViewModel
import com.example.pkompoultrymanager.transactions.cash_out.operational_expenses.OperationalExpensesViewModel
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*


class GeneralIncomeExpenseReport : Fragment() {
    private lateinit var mEggInventoryAdditionViewModel: EggInventoryAdditionViewModel
    private lateinit var mEggInventoryReductionViewModel: EggInventoryReductionViewModel
    private lateinit var mFlockInventoryAdditionViewModel: FlockInventoryAdditionViewModel
    private lateinit var mFlockInventoryReductionViewModel: FlockInventoryReductionViewModel
    private lateinit var mFeedInventoryAdditionViewModel: FeedInventoryAdditionViewModel
    private lateinit var mFeedInventoryReductionViewModel: FeedInventoryReductionViewModel
    private lateinit var mVaccinationViewModel: VaccinationViewModel
    private lateinit var mMedicationViewModel: MedicationViewModel
    private lateinit var mAlternativeIncomeViewModel: AlternativeIncomeViewModel
    private lateinit var mOperationalExpensesViewModel: OperationalExpensesViewModel
    private lateinit var myFarmInfo: SharedPreferences
    private lateinit var mEggTypeViewModel: EggTypeViewModel


    private var eggIncome = 0.0

    var flockIncome = 0.0
    var alternativeIncome = 0.0
    var feedCost = 0.0
    var operationalExpenses = 0.0
    var medicationCost = 0.0
    var vaccinationCost = 0.0

   override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
   ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_general_income_expense_report, container, false)

       val date: LocalDate = LocalDate.now()
       val today: LocalDate = LocalDate.now().minusDays(1)
       val week: LocalDate = LocalDate.now().minusWeeks(1)
       val month: LocalDate = LocalDate.now().minusDays(LocalDate.now().dayOfMonth.toLong())
       val threeMonths: LocalDate = LocalDate.now().minusMonths(3)
       val sixMonths: LocalDate = LocalDate.now().minusMonths(6)
       val year: LocalDate = LocalDate.now().minusYears(1)
       val twoYears: LocalDate = LocalDate.now().minusYears(2)

       val theDate = java.sql.Date.valueOf(date.toString())
       val theCurrentDay = java.sql.Date.valueOf(today.toString())
       val theWeek = java.sql.Date.valueOf(week.toString())
       val theMonth = java.sql.Date.valueOf(month.toString())
       val thePastThreeMonths = java.sql.Date.valueOf(threeMonths.toString())
       val thePastSixMonths = java.sql.Date.valueOf(sixMonths.toString())
       val thePastYear = java.sql.Date.valueOf(year.toString())
       val thePastTwoYears = java.sql.Date.valueOf(twoYears.toString())


       mEggInventoryAdditionViewModel = ViewModelProvider(this)[EggInventoryAdditionViewModel::class.java]
       mEggInventoryReductionViewModel = ViewModelProvider(this)[EggInventoryReductionViewModel::class.java]
       mFlockInventoryAdditionViewModel = ViewModelProvider(this)[FlockInventoryAdditionViewModel::class.java]
       mFlockInventoryReductionViewModel = ViewModelProvider(this)[FlockInventoryReductionViewModel::class.java]
       mFeedInventoryAdditionViewModel = ViewModelProvider(this)[FeedInventoryAdditionViewModel::class.java]
       mFeedInventoryReductionViewModel = ViewModelProvider(this)[FeedInventoryReductionViewModel::class.java]
       mVaccinationViewModel = ViewModelProvider(this)[VaccinationViewModel::class.java]
       mMedicationViewModel = ViewModelProvider(this)[MedicationViewModel::class.java]
       mAlternativeIncomeViewModel = ViewModelProvider(this)[AlternativeIncomeViewModel::class.java]
       mOperationalExpensesViewModel = ViewModelProvider(this)[OperationalExpensesViewModel::class.java]
       mEggTypeViewModel = ViewModelProvider(this)[EggTypeViewModel::class.java]


       val generalOverviewPieChart = view.findViewById<PieChart>(R.id.pcGeneralOverview_GeneralIncomeExpensesReport)
       val operationalExpensesPieChart = view.findViewById<PieChart>(R.id.pcOperationalExpenses_GeneralIncomeExpensesReport)
       val incomePieChart = view.findViewById<PieChart>(R.id.pcIncome_GeneralIncomeExpensesReport)
       val tvShowCharts = view.findViewById<TextView>(R.id.tvShowCharts_GeneralIncomeExpensesReport)
       val llGeneralOverviewLegendContainer = view.findViewById<LinearLayout>(R.id.llGeneralOverviewLegendContainer_GeneralIncomeExpensesReport)
       val llOperationalExpensesLegendContainer = view.findViewById<LinearLayout>(R.id.llOperationalExpensesLegendContainer_GeneralIncomeExpensesReport)
       val llIncomeLegendContainer = view.findViewById<LinearLayout>(R.id.llIncomeLegendContainer_GeneralIncomeExpensesReport)
       val ivDateFilter = view.findViewById<ImageView>(R.id.ivDateFilter_GeneralIncomeExpensesReport)

       myFarmInfo = context?.getSharedPreferences("MyFarmInfo", Context.MODE_PRIVATE)!!
       val numberOfFlocksPerCrates = myFarmInfo.getInt("numberOfFlocksPerCrate", 30)
       val currency = myFarmInfo.getString("currency", "GHS")!!
       val measuringUnit = myFarmInfo.getString("measuringUnit", "kg")

       val generalOverviewPieEntry = mutableListOf<PieEntry>()
       val operationalExpensesPieEntry = mutableListOf<PieEntry>()
       val incomePieEntry = mutableListOf<PieEntry>()
       val generalOverviewLegendPieEntry = mutableListOf<PieEntry>()

       val colors = mutableListOf<Int>()
       for (theColors in ColorTemplate.MATERIAL_COLORS){
           colors.add(theColors)
       }


       var generalOverviewPieDataSet =  PieDataSet(generalOverviewPieEntry,"General Overview")
       var generalOverviewPieData = PieData(generalOverviewPieDataSet)
       val generalOverviewLegend = generalOverviewPieChart.legend

       val operationalExpensesPieDataSet = PieDataSet(operationalExpensesPieEntry,"Operational Expenses")
       val operationalExpensesPieData = PieData(operationalExpensesPieDataSet)
       val operationalExpensesLegend = operationalExpensesPieChart.legend

       val incomePieDataSet = PieDataSet(incomePieEntry,"Income")
       val incomePieData = PieData(incomePieDataSet)
       val incomeLegend = incomePieChart.legend

       var income = 0.0
       var expenses = 0.0
       var netIncome = 0.0
/*

       tvShowCharts.setOnClickListener {

           val x = 100f
           // Getting the total Income
           mEggInventoryReductionViewModel.allEggSales.observe(viewLifecycleOwner) { eggSales ->
               eggIncome = _eggSales ?: 0.0
           }

           mFlockInventoryReductionViewModel.allFlockSales.observe(viewLifecycleOwner) { flockSales ->
               flockIncome = _flockSales ?: 0.0
           }

           mAlternativeIncomeViewModel.allAlternativeIncomeRevenue.observe(viewLifecycleOwner) { _alternativeIncome ->
               alternativeIncome = _alternativeIncome ?: 0.0
           }
           // Get all the Expenses
           mFeedInventoryAdditionViewModel.allFeedCost.observe(viewLifecycleOwner) { _feedCost ->
               feedCost = _feedCost ?: 0.0
           }

           mOperationalExpensesViewModel.amountOfAllOperationalExpenses.observe(viewLifecycleOwner) { expenses ->
               operationalExpenses = _operationalExpenses ?: 0.0
           }

           mMedicationViewModel.costOfAllMedication.observe(viewLifecycleOwner) { _medicationCost ->
               medicationCost = _medicationCost ?: 0.0
           }

           mVaccinationViewModel.costOfAllVaccinations.observe(viewLifecycleOwner) { _vaccinationCost ->
               vaccinationCost = _vaccinationCost ?: 0.0
           }
           mOperationalExpensesViewModel.allOperationalExpensesAmountByName.observe(viewLifecycleOwner) {}
           mAlternativeIncomeViewModel.allAlternativeIncomeRevenue.observe(viewLifecycleOwner) {}

           val popUpShowChartsMenu = PopupMenu(requireContext(), ivDateFilter)
           popUpShowChartsMenu.inflate(R.menu.hide_show_menu)
           popUpShowChartsMenu.setOnMenuItemClickListener {
               when (it.itemId) {
                   R.id.miShow -> {
                       generalOverviewPieEntry.clear()
                       generalOverviewLegendPieEntry.clear()
                       operationalExpensesPieEntry.clear()
                       incomePieEntry.clear()

                       llGeneralOverviewLegendContainer.removeAllViews()
                       llOperationalExpensesLegendContainer.removeAllViews()
                       llIncomeLegendContainer.removeAllViews()


                       income = eggIncome + flockIncome + alternativeIncome
                       expenses = operationalExpenses + feedCost + medicationCost + vaccinationCost
                       netIncome = if((income > expenses)){(income - expenses)}else (expenses - income)

                       generalOverviewLegendPieEntry.add(PieEntry(income.toFloat(), "Income"))
                       generalOverviewLegendPieEntry.add(PieEntry(expenses.toFloat(), "Expenses"))
                       generalOverviewLegendPieEntry.add(PieEntry((income - expenses).toFloat(), "NetIncome"))

                       generalOverviewPieEntry.add(PieEntry(income.toFloat(),"Income"))
                       generalOverviewPieEntry.add(PieEntry(expenses.toFloat(),"Expenses"))
                       generalOverviewPieEntry.add(PieEntry(netIncome.toFloat(),"Net Income"))

                       pieChartSetUp(generalOverviewPieChart, generalOverviewPieDataSet, generalOverviewPieData, colors, generalOverviewLegend)
                       inflateLegend(inflater, colors, generalOverviewLegendPieEntry, llGeneralOverviewLegendContainer, currency)

                       val x = 100f
                       // Operational Expenses Pie Chart
                       operationalExpensesPieEntry.add(PieEntry(feedCost.toFloat(), "Feeding"))
                       if (vaccinationCost > 0.0 ){operationalExpensesPieEntry.add(PieEntry(vaccinationCost.toFloat(), "Vaccine Cost"))}
                       if (medicationCost > 0.0){operationalExpensesPieEntry.add(PieEntry(medicationCost.toFloat(), "Medicine Cost"))}
                       mOperationalExpensesViewModel.allOperationalExpensesAmountByName.observe(viewLifecycleOwner) {
                               operationalExpensesList ->
                           for (everyExpenses in operationalExpensesList){
                               operationalExpensesPieEntry.add(PieEntry(everyExpenses.quantity.toFloat(), everyExpenses.name))
                           }
                       }
                       pieChartSetUp(operationalExpensesPieChart, operationalExpensesPieDataSet, operationalExpensesPieData, colors, operationalExpensesLegend)
                       inflateLegend(inflater, colors, operationalExpensesPieEntry, llOperationalExpensesLegendContainer, currency)

                       // Income Pie Chart

                       incomePieEntry.add(PieEntry(eggIncome.toFloat(), "Egg Sales"))
                       incomePieEntry.add(PieEntry(flockIncome.toFloat(), "Flock Sales"))
                       mAlternativeIncomeViewModel.allAlternativeIncomeRevenueByName.observe(viewLifecycleOwner) {
                               allAlternativeIncome->
                           for (everyAlternativeIncome in allAlternativeIncome){
                               incomePieEntry.add(PieEntry(everyAlternativeIncome.quantity.toFloat(), everyAlternativeIncome.name))
                           }
                       }
                       pieChartSetUp(incomePieChart, incomePieDataSet, incomePieData, colors, incomeLegend)
                       inflateLegend(inflater, colors, incomePieEntry, llIncomeLegendContainer, currency)

                       true
                   }

                   R.id.miHide ->{
                       Toast.makeText(requireContext(), "Will not display pie chart", Toast.LENGTH_LONG).show()
                       true
                   }
                   else -> true
               }
           }
           popUpShowChartsMenu.show()
           true
           tvShowCharts.isGone = true
       }
*/

       // Getting the total Income
       mEggInventoryReductionViewModel.allEggSales.observe(viewLifecycleOwner) { _eggSales ->
           eggIncome = _eggSales ?: 0.0


       mFlockInventoryReductionViewModel.allFlockSales.observe(viewLifecycleOwner) { _flockSales ->
           flockIncome = _flockSales ?: 0.0


       mAlternativeIncomeViewModel.allAlternativeIncomeRevenue.observe(viewLifecycleOwner) { _alternativeIncome ->
           alternativeIncome = _alternativeIncome ?: 0.0

       // Get all the Expenses
       mFeedInventoryAdditionViewModel.allFeedCost.observe(viewLifecycleOwner) { _feedCost ->
           feedCost = _feedCost ?: 0.0


       mOperationalExpensesViewModel.amountOfAllOperationalExpenses.observe(viewLifecycleOwner) { _operationalExpenses ->
           operationalExpenses = _operationalExpenses ?: 0.0


       mMedicationViewModel.costOfAllMedication.observe(viewLifecycleOwner) { _medicationCost ->
           medicationCost = _medicationCost ?: 0.0


       mVaccinationViewModel.costOfAllVaccinations.observe(viewLifecycleOwner) { _vaccinationCost ->
           vaccinationCost = _vaccinationCost ?: 0.0

       /*generalOverviewPieEntry.clear()
       generalOverviewLegendPieEntry.clear()
       operationalExpensesPieEntry.clear()
       incomePieEntry.clear()*/

       llGeneralOverviewLegendContainer.removeAllViews()
       llOperationalExpensesLegendContainer.removeAllViews()
       llIncomeLegendContainer.removeAllViews()


       income = eggIncome + flockIncome + alternativeIncome
       expenses = operationalExpenses + feedCost + medicationCost + vaccinationCost
       netIncome = if((income > expenses)){(income - expenses)}else (expenses - income)

       generalOverviewLegendPieEntry.add(PieEntry(income.toFloat(), "Income"))
       generalOverviewLegendPieEntry.add(PieEntry(expenses.toFloat(), "Expenses"))
       generalOverviewLegendPieEntry.add(PieEntry((income - expenses).toFloat(), "NetIncome"))

       generalOverviewPieEntry.add(PieEntry(income.toFloat(),"Income"))
       generalOverviewPieEntry.add(PieEntry(expenses.toFloat(),"Expenses"))
       generalOverviewPieEntry.add(PieEntry(netIncome.toFloat(),"Net Income"))

       pieChartSetUp(generalOverviewPieChart, generalOverviewPieDataSet, generalOverviewPieData, colors, generalOverviewLegend)
       inflateLegend(inflater, colors, generalOverviewLegendPieEntry, llGeneralOverviewLegendContainer, currency)

       // Operational Expenses Pie Chart
       operationalExpensesPieEntry.add(PieEntry(feedCost.toFloat(), "Feeding"))
       if (vaccinationCost > 0.0 ){operationalExpensesPieEntry.add(PieEntry(vaccinationCost.toFloat(), "Vaccine Cost"))}
       if (medicationCost > 0.0){operationalExpensesPieEntry.add(PieEntry(medicationCost.toFloat(), "Medicine Cost"))}
       mOperationalExpensesViewModel.allOperationalExpensesAmountByName.observe(viewLifecycleOwner) {
               operationalExpensesList ->
           for (everyExpenses in operationalExpensesList){
               operationalExpensesPieEntry.add(PieEntry(everyExpenses.quantity.toFloat(), everyExpenses.name))
           }

       pieChartSetUp(operationalExpensesPieChart, operationalExpensesPieDataSet, operationalExpensesPieData, colors, operationalExpensesLegend)
       inflateLegend(inflater, colors, operationalExpensesPieEntry, llOperationalExpensesLegendContainer, currency)

       // Income Pie Chart
       mAlternativeIncomeViewModel.allAlternativeIncomeRevenueByName.observe(viewLifecycleOwner) {
               allAlternativeIncome->
           for (everyAlternativeIncome in allAlternativeIncome){
               incomePieEntry.add(PieEntry(everyAlternativeIncome.quantity.toFloat(), everyAlternativeIncome.name))
           }

       incomePieEntry.add(PieEntry(eggIncome.toFloat(), "Egg Sales"))
       incomePieEntry.add(PieEntry(flockIncome.toFloat(), "Flock Sales"))

       pieChartSetUp(incomePieChart, incomePieDataSet, incomePieData, colors, incomeLegend)
       inflateLegend(inflater, colors, incomePieEntry, llIncomeLegendContainer, currency)


       }}}}}}}}}



       ivDateFilter.setOnClickListener {
           tvShowCharts.isGone = true
           
           val popupDateFilterMenu = PopupMenu(requireContext(), ivDateFilter)
           popupDateFilterMenu.inflate(R.menu.date_ranges)
           popupDateFilterMenu.setOnMenuItemClickListener {
               when (it.itemId) {
                   R.id.miAllTime -> {
                       llGeneralOverviewLegendContainer.removeAllViews()
                       llOperationalExpensesLegendContainer.removeAllViews()
                       llIncomeLegendContainer.removeAllViews()

                       generalOverviewPieEntry.clear()
                       generalOverviewLegendPieEntry.clear()
                       operationalExpensesPieEntry.clear()
                       incomePieEntry.clear()

                       mEggInventoryReductionViewModel.allEggSales.observe(viewLifecycleOwner) { _eggSales ->
                           eggIncome = _eggSales ?: 0.0


                       mFlockInventoryReductionViewModel.allFlockSales.observe(viewLifecycleOwner) { _flockSales ->
                           flockIncome = _flockSales ?: 0.0


                       mAlternativeIncomeViewModel.allAlternativeIncomeRevenue.observe(viewLifecycleOwner) { _alternativeIncome ->
                           alternativeIncome = _alternativeIncome ?: 0.0

                       // Get all the Expenses
                       mFeedInventoryAdditionViewModel.allFeedCost.observe(viewLifecycleOwner) { _feedCost ->
                           feedCost = _feedCost ?: 0.0


                       mOperationalExpensesViewModel.amountOfAllOperationalExpenses.observe(viewLifecycleOwner) { _operationalExpenses ->
                           operationalExpenses = _operationalExpenses ?: 0.0


                       mMedicationViewModel.costOfAllMedication.observe(viewLifecycleOwner) { _medicationCost ->
                           medicationCost = _medicationCost ?: 0.0


                       mVaccinationViewModel.costOfAllVaccinations.observe(viewLifecycleOwner) { _vaccinationCost ->
                           vaccinationCost = _vaccinationCost ?: 0.0


                       llGeneralOverviewLegendContainer.removeAllViews()
                       llOperationalExpensesLegendContainer.removeAllViews()
                       llIncomeLegendContainer.removeAllViews()

                       income = eggIncome + flockIncome + alternativeIncome
                       expenses = operationalExpenses + feedCost + medicationCost + vaccinationCost
                       netIncome = if((income > expenses)){(income - expenses)}else (expenses - income)

                       generalOverviewLegendPieEntry.add(PieEntry(income.toFloat(), "Income"))
                       generalOverviewLegendPieEntry.add(PieEntry(expenses.toFloat(), "Expenses"))
                       generalOverviewLegendPieEntry.add(PieEntry((income - expenses).toFloat(), "NetIncome"))

                       generalOverviewPieEntry.add(PieEntry(income.toFloat(),"Income"))
                       generalOverviewPieEntry.add(PieEntry(expenses.toFloat(),"Expenses"))
                       generalOverviewPieEntry.add(PieEntry(netIncome.toFloat(),"Net Income"))

                       pieChartSetUp(generalOverviewPieChart, generalOverviewPieDataSet, generalOverviewPieData, colors, generalOverviewLegend)
                       inflateLegend(inflater, colors, generalOverviewLegendPieEntry, llGeneralOverviewLegendContainer, currency)

                       val x = 100f
                       // Operational Expenses Pie Chart
                       operationalExpensesPieEntry.add(PieEntry(feedCost.toFloat(), "Feeding"))
                       if (vaccinationCost > 0.0 ){operationalExpensesPieEntry.add(PieEntry(vaccinationCost.toFloat(), "Vaccine Cost"))}
                       if (medicationCost > 0.0){operationalExpensesPieEntry.add(PieEntry(medicationCost.toFloat(), "Medicine Cost"))}
                       mOperationalExpensesViewModel.allOperationalExpensesAmountByName.observe(viewLifecycleOwner) {
                               operationalExpensesList ->
                           for (everyExpenses in operationalExpensesList){
                               operationalExpensesPieEntry.add(PieEntry(everyExpenses.quantity.toFloat(), everyExpenses.name))
                           }

                       pieChartSetUp(operationalExpensesPieChart, operationalExpensesPieDataSet, operationalExpensesPieData, colors, operationalExpensesLegend)
                       inflateLegend(inflater, colors, operationalExpensesPieEntry, llOperationalExpensesLegendContainer, currency)


                       // Income Pie Chart
                       incomePieEntry.add(PieEntry(eggIncome.toFloat(), "Egg Sales"))
                       incomePieEntry.add(PieEntry(flockIncome.toFloat(), "Flock Sales"))
                       mAlternativeIncomeViewModel.allAlternativeIncomeRevenueByName.observe(viewLifecycleOwner) {
                               allAlternativeIncome->
                           for (everyAlternativeIncome in allAlternativeIncome){
                               incomePieEntry.add(PieEntry(everyAlternativeIncome.quantity.toFloat(), everyAlternativeIncome.name))
                           }

                       pieChartSetUp(incomePieChart, incomePieDataSet, incomePieData, colors, incomeLegend)
                       inflateLegend(inflater, colors, incomePieEntry, llIncomeLegendContainer, currency)

                       }}}}}}}}}

                       true
                   }
                   R.id.miToday -> {
                       llGeneralOverviewLegendContainer.removeAllViews()
                       llOperationalExpensesLegendContainer.removeAllViews()
                       llIncomeLegendContainer.removeAllViews()

                       generalOverviewPieEntry.clear()
                       generalOverviewLegendPieEntry.clear()
                       operationalExpensesPieEntry.clear()
                       incomePieEntry.clear()

                       // Get all the Income
                       mEggInventoryReductionViewModel.eggSalesPerDuration(theCurrentDay, theDate).observe(viewLifecycleOwner){
                               _eggSales-> eggIncome = _eggSales ?: 0.0

                       mFlockInventoryReductionViewModel.flockSalesPerDuration(theCurrentDay, theDate).observe(viewLifecycleOwner){
                               _flockSales-> flockIncome = _flockSales ?: 0.0

                       mAlternativeIncomeViewModel.alternativeIncomeRevenuePerDuration(theCurrentDay, theDate).observe(viewLifecycleOwner){
                               _alternativeIncome-> alternativeIncome = _alternativeIncome ?: 0.0

                       // Get all the Expenses
                       mFeedInventoryAdditionViewModel.feedCostPerDuration(theCurrentDay, theDate).observe(viewLifecycleOwner){
                               _feedCost -> feedCost = _feedCost ?: 0.0

                       mOperationalExpensesViewModel.amountOfOperationalExpensesPerDuration(theCurrentDay, theDate).observe(viewLifecycleOwner) {
                               _operationalExpenses -> operationalExpenses = _operationalExpenses ?: 0.0

                       mMedicationViewModel.costOfMedicationPerDuration(theCurrentDay, theDate).observe(viewLifecycleOwner){
                               _medicationCost -> medicationCost = _medicationCost ?: 0.0

                       mVaccinationViewModel.costOfVaccinationPerDuration(theCurrentDay, theDate).observe(viewLifecycleOwner){
                               _vaccinationCost -> vaccinationCost = _vaccinationCost ?: 0.0


                       income = eggIncome + flockIncome + alternativeIncome
                       expenses  = operationalExpenses + feedCost + medicationCost + vaccinationCost
                       netIncome  = if((income > expenses)){(income - expenses)}else (expenses - income)

                       generalOverviewLegendPieEntry.add(PieEntry(income.toFloat(), "Income"))
                       generalOverviewLegendPieEntry.add(PieEntry(expenses.toFloat(), "Expenses"))
                       generalOverviewLegendPieEntry.add(PieEntry((income - expenses).toFloat(), "NetIncome"))

                       generalOverviewPieEntry.add(PieEntry(income.toFloat(),"Income"))
                       generalOverviewPieEntry.add(PieEntry(expenses.toFloat(),"Expenses"))
                       generalOverviewPieEntry.add(PieEntry(netIncome.toFloat(),"Net Income"))

                       generalOverviewPieDataSet = PieDataSet(generalOverviewPieEntry,"General Overview")
                       generalOverviewPieData = PieData(generalOverviewPieDataSet)

                       pieChartSetUp(generalOverviewPieChart, generalOverviewPieDataSet, generalOverviewPieData, colors, generalOverviewLegend)
                       inflateLegend(inflater, colors, generalOverviewLegendPieEntry, llGeneralOverviewLegendContainer, currency)

                       // Operational Expenses Pie Chart
                       operationalExpensesPieEntry.add(PieEntry(feedCost.toFloat(), "Feeding"))
                       if (vaccinationCost > 0.0 ){operationalExpensesPieEntry.add(PieEntry(vaccinationCost.toFloat(), "Vaccine Cost"))}
                       if (medicationCost > 0.0){operationalExpensesPieEntry.add(PieEntry(medicationCost.toFloat(), "Medicine Cost"))}
                       mOperationalExpensesViewModel.operationalExpensesAmountByName(theCurrentDay, theDate).observe(viewLifecycleOwner) {
                               operationalExpensesList ->
                           for (everyExpenses in operationalExpensesList){
                               operationalExpensesPieEntry.add(PieEntry(everyExpenses.quantity.toFloat(), everyExpenses.name))
                           }

                       pieChartSetUp(operationalExpensesPieChart, operationalExpensesPieDataSet, operationalExpensesPieData, colors, operationalExpensesLegend)
                       inflateLegend(inflater, colors, operationalExpensesPieEntry, llOperationalExpensesLegendContainer, currency)

                       // Income Pie Chart
                       incomePieEntry.add(PieEntry(eggIncome.toFloat(), "Egg Sales"))
                       incomePieEntry.add(PieEntry(flockIncome.toFloat(), "Flock Sales"))
                       mAlternativeIncomeViewModel.alternativeIncomeRevenueByName(theCurrentDay, theDate).observe(viewLifecycleOwner) {
                               alternativeIncome->
                           for (everyAlternativeIncome in alternativeIncome){
                               incomePieEntry.add(PieEntry(everyAlternativeIncome.quantity.toFloat(), everyAlternativeIncome.name))
                           }

                       pieChartSetUp(incomePieChart, incomePieDataSet, incomePieData, colors, incomeLegend)
                       inflateLegend(inflater, colors, incomePieEntry, llIncomeLegendContainer, currency)

                       }}}}}}}}}

                       true
                   }
                   R.id.miThisWeek -> {
                       llGeneralOverviewLegendContainer.removeAllViews()
                       llOperationalExpensesLegendContainer.removeAllViews()
                       llIncomeLegendContainer.removeAllViews()

                       generalOverviewPieEntry.clear()
                       generalOverviewLegendPieEntry.clear()
                       operationalExpensesPieEntry.clear()
                       incomePieEntry.clear()
                       // Get all the Income
                       mEggInventoryReductionViewModel.eggSalesPerDuration(theWeek, theDate).observe(viewLifecycleOwner){
                               _eggSales-> eggIncome = _eggSales ?: 0.0

                       mFlockInventoryReductionViewModel.flockSalesPerDuration(theWeek, theDate).observe(viewLifecycleOwner){
                               _flockSales-> flockIncome = _flockSales ?: 0.0

                       mAlternativeIncomeViewModel.alternativeIncomeRevenuePerDuration(theWeek, theDate).observe(viewLifecycleOwner){
                               _alternativeIncome-> alternativeIncome = _alternativeIncome ?: 0.0

                       // Get all the Expenses
                       mFeedInventoryAdditionViewModel.feedCostPerDuration(theWeek, theDate).observe(viewLifecycleOwner){
                               _feedCost -> feedCost = _feedCost ?: 0.0

                       mOperationalExpensesViewModel.amountOfOperationalExpensesPerDuration(theWeek, theDate).observe(viewLifecycleOwner) {
                               _operationalExpenses -> operationalExpenses = _operationalExpenses ?: 0.0

                       mMedicationViewModel.costOfMedicationPerDuration(theWeek, theDate).observe(viewLifecycleOwner){
                               _medicationCost -> medicationCost = _medicationCost ?: 0.0

                       mVaccinationViewModel.costOfVaccinationPerDuration(theWeek, theDate).observe(viewLifecycleOwner){
                               _vaccinationCost -> vaccinationCost = _vaccinationCost ?: 0.0


                       income = eggIncome + flockIncome + alternativeIncome
                       expenses  = operationalExpenses + feedCost + medicationCost + vaccinationCost
                       netIncome  = if((income > expenses)){(income - expenses)}else (expenses - income)

                       generalOverviewLegendPieEntry.add(PieEntry(income.toFloat(), "Income"))
                       generalOverviewLegendPieEntry.add(PieEntry(expenses.toFloat(), "Expenses"))
                       generalOverviewLegendPieEntry.add(PieEntry((income - expenses).toFloat(), "NetIncome"))

                       generalOverviewPieEntry.add(PieEntry(income.toFloat(),"Income"))
                       generalOverviewPieEntry.add(PieEntry(expenses.toFloat(),"Expenses"))
                       generalOverviewPieEntry.add(PieEntry(netIncome.toFloat(),"Net Income"))

                       generalOverviewPieDataSet = PieDataSet(generalOverviewPieEntry,"General Overview")
                       generalOverviewPieData = PieData(generalOverviewPieDataSet)

                       pieChartSetUp(generalOverviewPieChart, generalOverviewPieDataSet, generalOverviewPieData, colors, generalOverviewLegend)
                       inflateLegend(inflater, colors, generalOverviewLegendPieEntry, llGeneralOverviewLegendContainer, currency)

                       // Operational Expenses Pie Chart
                       operationalExpensesPieEntry.add(PieEntry(feedCost.toFloat(), "Feeding"))
                       if (vaccinationCost > 0.0 ){operationalExpensesPieEntry.add(PieEntry(vaccinationCost.toFloat(), "Vaccine Cost"))}
                       if (medicationCost > 0.0){operationalExpensesPieEntry.add(PieEntry(medicationCost.toFloat(), "Medicine Cost"))}
                       mOperationalExpensesViewModel.operationalExpensesAmountByName(theWeek, theDate).observe(viewLifecycleOwner) {
                               operationalExpensesList ->
                           for (everyExpenses in operationalExpensesList){
                               operationalExpensesPieEntry.add(PieEntry(everyExpenses.quantity.toFloat(), everyExpenses.name))
                           }

                       pieChartSetUp(operationalExpensesPieChart, operationalExpensesPieDataSet, operationalExpensesPieData, colors, operationalExpensesLegend)
                       inflateLegend(inflater, colors, operationalExpensesPieEntry, llOperationalExpensesLegendContainer, currency)

                       // Income Pie Chart
                       incomePieEntry.add(PieEntry(eggIncome.toFloat(), "Egg Sales"))
                       incomePieEntry.add(PieEntry(flockIncome.toFloat(), "Flock Sales"))
                       mAlternativeIncomeViewModel.alternativeIncomeRevenueByName(theWeek, theDate).observe(viewLifecycleOwner) {
                               alternativeIncome->
                           for (everyAlternativeIncome in alternativeIncome){
                               incomePieEntry.add(PieEntry(everyAlternativeIncome.quantity.toFloat(), everyAlternativeIncome.name))
                           }

                       pieChartSetUp(incomePieChart, incomePieDataSet, incomePieData, colors, incomeLegend)
                       inflateLegend(inflater, colors, incomePieEntry, llIncomeLegendContainer, currency)

                   }}}}}}}}}
                       true
                   }
                   R.id.miThisMonth -> {
                       llGeneralOverviewLegendContainer.removeAllViews()
                       llOperationalExpensesLegendContainer.removeAllViews()
                       llIncomeLegendContainer.removeAllViews()


                       generalOverviewPieEntry.clear()
                           generalOverviewLegendPieEntry.clear()
                           operationalExpensesPieEntry.clear()
                           incomePieEntry.clear()
                           // Get all the Income
                           mEggInventoryReductionViewModel.eggSalesPerDuration(theMonth, theDate).observe(viewLifecycleOwner){
                                   _eggSales-> eggIncome = _eggSales ?: 0.0

                           mFlockInventoryReductionViewModel.flockSalesPerDuration(theMonth, theDate).observe(viewLifecycleOwner){
                                   _flockSales-> flockIncome = _flockSales ?: 0.0

                           mAlternativeIncomeViewModel.alternativeIncomeRevenuePerDuration(theMonth, theDate).observe(viewLifecycleOwner){
                                   _alternativeIncome-> alternativeIncome = _alternativeIncome ?: 0.0

                           // Get all the Expenses
                           mFeedInventoryAdditionViewModel.feedCostPerDuration(theMonth, theDate).observe(viewLifecycleOwner){
                                   _feedCost -> feedCost = _feedCost ?: 0.0

                           mOperationalExpensesViewModel.amountOfOperationalExpensesPerDuration(theMonth, theDate).observe(viewLifecycleOwner) {
                                   _operationalExpenses -> operationalExpenses = _operationalExpenses ?: 0.0

                           mMedicationViewModel.costOfMedicationPerDuration(theMonth, theDate).observe(viewLifecycleOwner){
                                   _medicationCost -> medicationCost = _medicationCost ?: 0.0

                           mVaccinationViewModel.costOfVaccinationPerDuration(theMonth, theDate).observe(viewLifecycleOwner){
                                   _vaccinationCost -> vaccinationCost = _vaccinationCost ?: 0.0


                           income = eggIncome + flockIncome + alternativeIncome
                           expenses  = operationalExpenses + feedCost + medicationCost + vaccinationCost
                           netIncome  = if((income > expenses)){(income - expenses)}else (expenses - income)

                           generalOverviewLegendPieEntry.add(PieEntry(income.toFloat(), "Income"))
                           generalOverviewLegendPieEntry.add(PieEntry(expenses.toFloat(), "Expenses"))
                           generalOverviewLegendPieEntry.add(PieEntry((income - expenses).toFloat(), "NetIncome"))

                           generalOverviewPieEntry.add(PieEntry(income.toFloat(),"Income"))
                           generalOverviewPieEntry.add(PieEntry(expenses.toFloat(),"Expenses"))
                           generalOverviewPieEntry.add(PieEntry(netIncome.toFloat(),"Net Income"))

                           generalOverviewPieDataSet = PieDataSet(generalOverviewPieEntry,"General Overview")
                           generalOverviewPieData = PieData(generalOverviewPieDataSet)

                           pieChartSetUp(generalOverviewPieChart, generalOverviewPieDataSet, generalOverviewPieData, colors, generalOverviewLegend)
                           inflateLegend(inflater, colors, generalOverviewLegendPieEntry, llGeneralOverviewLegendContainer, currency)


                           val x = 100f
                           // Operational Expenses Pie Chart
                           operationalExpensesPieEntry.add(PieEntry(feedCost.toFloat(), "Feeding"))
                           if (vaccinationCost > 0.0 ){operationalExpensesPieEntry.add(PieEntry(vaccinationCost.toFloat(), "Vaccine Cost"))}
                           if (medicationCost > 0.0){operationalExpensesPieEntry.add(PieEntry(medicationCost.toFloat(), "Medicine Cost"))}
                           mOperationalExpensesViewModel.operationalExpensesAmountByName(theMonth, theDate).observe(viewLifecycleOwner) {
                                   operationalExpensesList ->
                               for (everyExpenses in operationalExpensesList){
                                   operationalExpensesPieEntry.add(PieEntry(everyExpenses.quantity.toFloat(), everyExpenses.name))
                               }

                           pieChartSetUp(operationalExpensesPieChart, operationalExpensesPieDataSet, operationalExpensesPieData, colors, operationalExpensesLegend)
                           inflateLegend(inflater, colors, operationalExpensesPieEntry, llOperationalExpensesLegendContainer, currency)

                           // Income Pie Chart
                           incomePieEntry.add(PieEntry(eggIncome.toFloat(), "Egg Sales"))
                           incomePieEntry.add(PieEntry(flockIncome.toFloat(), "Flock Sales"))
                           mAlternativeIncomeViewModel.alternativeIncomeRevenueByName(theMonth, theDate).observe(viewLifecycleOwner) {
                                   alternativeIncome->
                               for (everyAlternativeIncome in alternativeIncome){
                                   incomePieEntry.add(PieEntry(everyAlternativeIncome.quantity.toFloat(), everyAlternativeIncome.name))
                               }

                           pieChartSetUp(incomePieChart, incomePieDataSet, incomePieData, colors, incomeLegend)
                           inflateLegend(inflater, colors, incomePieEntry, llIncomeLegendContainer, currency)

                   }}}}}}}}}
                       true
                   }
                   R.id.miPastThreeMonths -> {
                       llGeneralOverviewLegendContainer.removeAllViews()
                       llOperationalExpensesLegendContainer.removeAllViews()
                       llIncomeLegendContainer.removeAllViews()

                       generalOverviewPieEntry.clear()
                           generalOverviewLegendPieEntry.clear()
                           operationalExpensesPieEntry.clear()
                           incomePieEntry.clear()
                           // Get all the Income
                           mEggInventoryReductionViewModel.eggSalesPerDuration(thePastThreeMonths, theDate).observe(viewLifecycleOwner){
                                   _eggSales-> eggIncome = _eggSales ?: 0.0

                           mFlockInventoryReductionViewModel.flockSalesPerDuration(thePastThreeMonths, theDate).observe(viewLifecycleOwner){
                                   _flockSales-> flockIncome = _flockSales ?: 0.0

                           mAlternativeIncomeViewModel.alternativeIncomeRevenuePerDuration(thePastThreeMonths, theDate).observe(viewLifecycleOwner){
                                   _alternativeIncome-> alternativeIncome = _alternativeIncome ?: 0.0

                           // Get all the Expenses
                           mFeedInventoryAdditionViewModel.feedCostPerDuration(thePastThreeMonths, theDate).observe(viewLifecycleOwner){
                                   _feedCost -> feedCost = _feedCost ?: 0.0

                           mOperationalExpensesViewModel.amountOfOperationalExpensesPerDuration(thePastThreeMonths, theDate).observe(viewLifecycleOwner) {
                                   _operationalExpenses -> operationalExpenses = _operationalExpenses ?: 0.0
                           mMedicationViewModel.costOfMedicationPerDuration(thePastThreeMonths, theDate).observe(viewLifecycleOwner){
                                   _medicationCost -> medicationCost = _medicationCost ?: 0.0

                           mVaccinationViewModel.costOfVaccinationPerDuration(thePastThreeMonths, theDate).observe(viewLifecycleOwner){
                                   _vaccinationCost -> vaccinationCost = _vaccinationCost ?: 0.0


                           income = eggIncome + flockIncome + alternativeIncome
                           expenses  = operationalExpenses + feedCost + medicationCost + vaccinationCost
                           netIncome  = if((income > expenses)){(income - expenses)}else (expenses - income)

                           generalOverviewLegendPieEntry.add(PieEntry(income.toFloat(), "Income"))
                           generalOverviewLegendPieEntry.add(PieEntry(expenses.toFloat(), "Expenses"))
                           generalOverviewLegendPieEntry.add(PieEntry((income - expenses).toFloat(), "NetIncome"))

                           generalOverviewPieEntry.add(PieEntry(income.toFloat(),"Income"))
                           generalOverviewPieEntry.add(PieEntry(expenses.toFloat(),"Expenses"))
                           generalOverviewPieEntry.add(PieEntry(netIncome.toFloat(),"Net Income"))

                           generalOverviewPieDataSet = PieDataSet(generalOverviewPieEntry,"General Overview")
                           generalOverviewPieData = PieData(generalOverviewPieDataSet)

                           pieChartSetUp(generalOverviewPieChart, generalOverviewPieDataSet, generalOverviewPieData, colors, generalOverviewLegend)
                           inflateLegend(inflater, colors, generalOverviewLegendPieEntry, llGeneralOverviewLegendContainer, currency)


                           val x = 100f
                           // Operational Expenses Pie Chart
                           operationalExpensesPieEntry.add(PieEntry(feedCost.toFloat(), "Feeding"))
                           if (vaccinationCost > 0.0 ){operationalExpensesPieEntry.add(PieEntry(vaccinationCost.toFloat(), "Vaccine Cost"))}
                           if (medicationCost > 0.0){operationalExpensesPieEntry.add(PieEntry(medicationCost.toFloat(), "Medicine Cost"))}
                           mOperationalExpensesViewModel.operationalExpensesAmountByName(thePastThreeMonths, theDate).observe(viewLifecycleOwner) {
                                   operationalExpensesList ->
                               for (everyExpenses in operationalExpensesList){
                                   operationalExpensesPieEntry.add(PieEntry(everyExpenses.quantity.toFloat(), everyExpenses.name))
                               }

                           pieChartSetUp(operationalExpensesPieChart, operationalExpensesPieDataSet, operationalExpensesPieData, colors, operationalExpensesLegend)
                           inflateLegend(inflater, colors, operationalExpensesPieEntry, llOperationalExpensesLegendContainer, currency)

                           // Income Pie Chart
                           incomePieEntry.add(PieEntry(eggIncome.toFloat(), "Egg Sales"))
                           incomePieEntry.add(PieEntry(flockIncome.toFloat(), "Flock Sales"))
                           mAlternativeIncomeViewModel.alternativeIncomeRevenueByName(thePastThreeMonths, theDate).observe(viewLifecycleOwner) {
                                   alternativeIncome->
                               for (everyAlternativeIncome in alternativeIncome){
                                   incomePieEntry.add(PieEntry(everyAlternativeIncome.quantity.toFloat(), everyAlternativeIncome.name))
                               }

                           pieChartSetUp(incomePieChart, incomePieDataSet, incomePieData, colors, incomeLegend)
                           inflateLegend(inflater, colors, incomePieEntry, llIncomeLegendContainer, currency)

                       }}}}}}}}}
                       true
                   }
                   R.id.miPastSixMonths -> {
                       llGeneralOverviewLegendContainer.removeAllViews()
                       llOperationalExpensesLegendContainer.removeAllViews()
                       llIncomeLegendContainer.removeAllViews()


                       generalOverviewPieEntry.clear()
                           generalOverviewLegendPieEntry.clear()
                           operationalExpensesPieEntry.clear()
                           incomePieEntry.clear()
                           // Get all the Income
                           mEggInventoryReductionViewModel.eggSalesPerDuration(thePastSixMonths, theDate).observe(viewLifecycleOwner){
                                   _eggSales-> eggIncome = _eggSales ?: 0.0

                           mFlockInventoryReductionViewModel.flockSalesPerDuration(thePastSixMonths, theDate).observe(viewLifecycleOwner){
                                   _flockSales-> flockIncome = _flockSales ?: 0.0

                           mAlternativeIncomeViewModel.alternativeIncomeRevenuePerDuration(thePastSixMonths, theDate).observe(viewLifecycleOwner){
                                   _alternativeIncome-> alternativeIncome = _alternativeIncome ?: 0.0

                           // Get all the Expenses
                           mFeedInventoryAdditionViewModel.feedCostPerDuration(thePastSixMonths, theDate).observe(viewLifecycleOwner){
                                   _feedCost -> feedCost = _feedCost ?: 0.0

                           mOperationalExpensesViewModel.amountOfOperationalExpensesPerDuration(thePastSixMonths, theDate).observe(viewLifecycleOwner) {
                                   _operationalExpenses -> operationalExpenses = _operationalExpenses ?: 0.0

                           mMedicationViewModel.costOfMedicationPerDuration(thePastSixMonths, theDate).observe(viewLifecycleOwner){
                                   _medicationCost -> medicationCost = _medicationCost ?: 0.0

                           mVaccinationViewModel.costOfVaccinationPerDuration(thePastSixMonths, theDate).observe(viewLifecycleOwner){
                                   _vaccinationCost -> vaccinationCost = _vaccinationCost ?: 0.0


                           income = eggIncome + flockIncome + alternativeIncome
                           expenses  = operationalExpenses + feedCost + medicationCost + vaccinationCost
                           netIncome  = if((income > expenses)){(income - expenses)}else (expenses - income)

                           generalOverviewLegendPieEntry.add(PieEntry(income.toFloat(), "Income"))
                           generalOverviewLegendPieEntry.add(PieEntry(expenses.toFloat(), "Expenses"))
                           generalOverviewLegendPieEntry.add(PieEntry((income - expenses).toFloat(), "NetIncome"))

                           generalOverviewPieEntry.add(PieEntry(income.toFloat(),"Income"))
                           generalOverviewPieEntry.add(PieEntry(expenses.toFloat(),"Expenses"))
                           generalOverviewPieEntry.add(PieEntry(netIncome.toFloat(),"Net Income"))

                           generalOverviewPieDataSet = PieDataSet(generalOverviewPieEntry,"General Overview")
                           generalOverviewPieData = PieData(generalOverviewPieDataSet)

                           pieChartSetUp(generalOverviewPieChart, generalOverviewPieDataSet, generalOverviewPieData, colors, generalOverviewLegend)
                           inflateLegend(inflater, colors, generalOverviewLegendPieEntry, llGeneralOverviewLegendContainer, currency)

                           // Operational Expenses Pie Chart
                           operationalExpensesPieEntry.add(PieEntry(feedCost.toFloat(), "Feeding"))
                           if (vaccinationCost > 0.0 ){operationalExpensesPieEntry.add(PieEntry(vaccinationCost.toFloat(), "Vaccine Cost"))}
                           if (medicationCost > 0.0){operationalExpensesPieEntry.add(PieEntry(medicationCost.toFloat(), "Medicine Cost"))}
                           mOperationalExpensesViewModel.operationalExpensesAmountByName(thePastSixMonths, theDate).observe(viewLifecycleOwner) {
                                   operationalExpensesList ->
                               for (everyExpenses in operationalExpensesList){
                                   operationalExpensesPieEntry.add(PieEntry(everyExpenses.quantity.toFloat(), everyExpenses.name))
                               }

                           pieChartSetUp(operationalExpensesPieChart, operationalExpensesPieDataSet, operationalExpensesPieData, colors, operationalExpensesLegend)
                           inflateLegend(inflater, colors, operationalExpensesPieEntry, llOperationalExpensesLegendContainer, currency)

                           // Income Pie Chart
                           incomePieEntry.add(PieEntry(eggIncome.toFloat(), "Egg Sales"))
                           incomePieEntry.add(PieEntry(flockIncome.toFloat(), "Flock Sales"))
                           mAlternativeIncomeViewModel.alternativeIncomeRevenueByName(thePastSixMonths, theDate).observe(viewLifecycleOwner) {
                                   alternativeIncome->
                               for (everyAlternativeIncome in alternativeIncome){
                                   incomePieEntry.add(PieEntry(everyAlternativeIncome.quantity.toFloat(), everyAlternativeIncome.name))
                               }

                           pieChartSetUp(incomePieChart, incomePieDataSet, incomePieData, colors, incomeLegend)
                           inflateLegend(inflater, colors, incomePieEntry, llIncomeLegendContainer, currency)

                       }}}}}}}}}

                       true
                   }
                   R.id.miPastYear -> {

                       llGeneralOverviewLegendContainer.removeAllViews()
                       llOperationalExpensesLegendContainer.removeAllViews()
                       llIncomeLegendContainer.removeAllViews()

                       generalOverviewPieEntry.clear()
                           generalOverviewLegendPieEntry.clear()
                           operationalExpensesPieEntry.clear()
                           incomePieEntry.clear()
                           // Get all the Income
                           mEggInventoryReductionViewModel.eggSalesPerDuration(thePastYear, theDate).observe(viewLifecycleOwner){
                                   _eggSales-> eggIncome = _eggSales ?: 0.0

                           mFlockInventoryReductionViewModel.flockSalesPerDuration(thePastYear, theDate).observe(viewLifecycleOwner){
                                   _flockSales-> flockIncome = _flockSales ?: 0.0

                           mAlternativeIncomeViewModel.alternativeIncomeRevenuePerDuration(thePastYear, theDate).observe(viewLifecycleOwner){
                                   _alternativeIncome-> alternativeIncome = _alternativeIncome ?: 0.0

                           // Get all the Expenses
                           mFeedInventoryAdditionViewModel.feedCostPerDuration(thePastYear, theDate).observe(viewLifecycleOwner){
                                   _feedCost -> feedCost = _feedCost ?: 0.0

                           mOperationalExpensesViewModel.amountOfOperationalExpensesPerDuration(thePastYear, theDate).observe(viewLifecycleOwner) {
                                   _operationalExpenses -> operationalExpenses = _operationalExpenses ?: 0.0

                           mMedicationViewModel.costOfMedicationPerDuration(thePastYear, theDate).observe(viewLifecycleOwner){
                                   _medicationCost -> medicationCost = _medicationCost ?: 0.0

                           mVaccinationViewModel.costOfVaccinationPerDuration(thePastYear, theDate).observe(viewLifecycleOwner){
                                   _vaccinationCost -> vaccinationCost = _vaccinationCost ?: 0.0


                           income = eggIncome + flockIncome + alternativeIncome
                           expenses  = operationalExpenses + feedCost + medicationCost + vaccinationCost
                           netIncome  = if((income > expenses)){(income - expenses)}else (expenses - income)

                           generalOverviewLegendPieEntry.add(PieEntry(income.toFloat(), "Income"))
                           generalOverviewLegendPieEntry.add(PieEntry(expenses.toFloat(), "Expenses"))
                           generalOverviewLegendPieEntry.add(PieEntry((income - expenses).toFloat(), "NetIncome"))

                           generalOverviewPieEntry.add(PieEntry(income.toFloat(),"Income"))
                           generalOverviewPieEntry.add(PieEntry(expenses.toFloat(),"Expenses"))
                           generalOverviewPieEntry.add(PieEntry(netIncome.toFloat(),"Net Income"))

                           generalOverviewPieDataSet = PieDataSet(generalOverviewPieEntry,"General Overview")
                           generalOverviewPieData = PieData(generalOverviewPieDataSet)

                           pieChartSetUp(generalOverviewPieChart, generalOverviewPieDataSet, generalOverviewPieData, colors, generalOverviewLegend)
                           inflateLegend(inflater, colors, generalOverviewLegendPieEntry, llGeneralOverviewLegendContainer, currency)


                           // Operational Expenses Pie Chart
                           operationalExpensesPieEntry.add(PieEntry(feedCost.toFloat(), "Feeding"))
                           if (vaccinationCost > 0.0 ){operationalExpensesPieEntry.add(PieEntry(vaccinationCost.toFloat(), "Vaccine Cost"))}
                           if (medicationCost > 0.0){operationalExpensesPieEntry.add(PieEntry(medicationCost.toFloat(), "Medicine Cost"))}
                           mOperationalExpensesViewModel.operationalExpensesAmountByName(thePastYear, theDate).observe(viewLifecycleOwner) {
                                   operationalExpensesList ->
                               for (everyExpenses in operationalExpensesList){
                                   operationalExpensesPieEntry.add(PieEntry(everyExpenses.quantity.toFloat(), everyExpenses.name))
                               }

                           pieChartSetUp(operationalExpensesPieChart, operationalExpensesPieDataSet, operationalExpensesPieData, colors, operationalExpensesLegend)
                           inflateLegend(inflater, colors, operationalExpensesPieEntry, llOperationalExpensesLegendContainer, currency)

                           // Income Pie Chart
                           incomePieEntry.add(PieEntry(eggIncome.toFloat(), "Egg Sales"))
                           incomePieEntry.add(PieEntry(flockIncome.toFloat(), "Flock Sales"))
                           mAlternativeIncomeViewModel.alternativeIncomeRevenueByName(thePastYear, theDate).observe(viewLifecycleOwner) {
                                   alternativeIncome->
                               for (everyAlternativeIncome in alternativeIncome){
                                   incomePieEntry.add(PieEntry(everyAlternativeIncome.quantity.toFloat(), everyAlternativeIncome.name))
                               }

                           pieChartSetUp(incomePieChart, incomePieDataSet, incomePieData, colors, incomeLegend)
                           inflateLegend(inflater, colors, incomePieEntry, llIncomeLegendContainer, currency)


                           }}}}}}}}}
                       true
                   }
                   R.id.miPastTwoYears -> {
                       llGeneralOverviewLegendContainer.removeAllViews()
                       llOperationalExpensesLegendContainer.removeAllViews()
                       llIncomeLegendContainer.removeAllViews()

                       generalOverviewPieEntry.clear()
                       generalOverviewLegendPieEntry.clear()
                       operationalExpensesPieEntry.clear()
                       incomePieEntry.clear()
                       // Get all the Income
                       mEggInventoryReductionViewModel.eggSalesPerDuration(thePastTwoYears, theDate).observe(viewLifecycleOwner){
                               _eggSales-> eggIncome = _eggSales ?: 0.0

                       mFlockInventoryReductionViewModel.flockSalesPerDuration(thePastTwoYears, theDate).observe(viewLifecycleOwner){
                               _flockSales-> flockIncome = _flockSales ?: 0.0

                       mAlternativeIncomeViewModel.alternativeIncomeRevenuePerDuration(thePastTwoYears, theDate).observe(viewLifecycleOwner){
                               _alternativeIncome-> alternativeIncome = _alternativeIncome ?: 0.0

                       // Get all the Expenses
                       mFeedInventoryAdditionViewModel.feedCostPerDuration(thePastTwoYears, theDate).observe(viewLifecycleOwner){
                               _feedCost -> feedCost = _feedCost ?: 0.0

                       mOperationalExpensesViewModel.amountOfOperationalExpensesPerDuration(thePastTwoYears, theDate).observe(viewLifecycleOwner) {
                               _operationalExpenses -> operationalExpenses = _operationalExpenses ?: 0.0

                       mMedicationViewModel.costOfMedicationPerDuration(thePastTwoYears, theDate).observe(viewLifecycleOwner){
                               _medicationCost -> medicationCost = _medicationCost ?: 0.0

                       mVaccinationViewModel.costOfVaccinationPerDuration(thePastTwoYears, theDate).observe(viewLifecycleOwner){
                               _vaccinationCost -> vaccinationCost = _vaccinationCost ?: 0.0


                       income = eggIncome + flockIncome + alternativeIncome
                       expenses  = operationalExpenses + feedCost + medicationCost + vaccinationCost
                       netIncome  = if((income > expenses)){(income - expenses)}else (expenses - income)

                       generalOverviewLegendPieEntry.add(PieEntry(income.toFloat(), "Income"))
                       generalOverviewLegendPieEntry.add(PieEntry(expenses.toFloat(), "Expenses"))
                       generalOverviewLegendPieEntry.add(PieEntry((income - expenses).toFloat(), "NetIncome"))

                       generalOverviewPieEntry.add(PieEntry(income.toFloat(),"Income"))
                       generalOverviewPieEntry.add(PieEntry(expenses.toFloat(),"Expenses"))
                       generalOverviewPieEntry.add(PieEntry(netIncome.toFloat(),"Net Income"))

                       generalOverviewPieDataSet = PieDataSet(generalOverviewPieEntry,"General Overview")
                       generalOverviewPieData = PieData(generalOverviewPieDataSet)

                       pieChartSetUp(generalOverviewPieChart, generalOverviewPieDataSet, generalOverviewPieData, colors, generalOverviewLegend)
                       inflateLegend(inflater, colors, generalOverviewLegendPieEntry, llGeneralOverviewLegendContainer, currency)


                       // Operational Expenses Pie Chart
                       operationalExpensesPieEntry.add(PieEntry(feedCost.toFloat(), "Feeding"))
                       if (vaccinationCost > 0.0 ){operationalExpensesPieEntry.add(PieEntry(vaccinationCost.toFloat(), "Vaccine Cost"))}
                       if (medicationCost > 0.0){operationalExpensesPieEntry.add(PieEntry(medicationCost.toFloat(), "Medicine Cost"))}
                       mOperationalExpensesViewModel.operationalExpensesAmountByName(thePastTwoYears, theDate).observe(viewLifecycleOwner) {
                               operationalExpensesList ->
                           for (everyExpenses in operationalExpensesList){
                               operationalExpensesPieEntry.add(PieEntry(everyExpenses.quantity.toFloat(), everyExpenses.name))
                           }

                       pieChartSetUp(operationalExpensesPieChart, operationalExpensesPieDataSet, operationalExpensesPieData, colors, operationalExpensesLegend)
                       inflateLegend(inflater, colors, operationalExpensesPieEntry, llOperationalExpensesLegendContainer, currency)

                       // Income Pie Chart
                       incomePieEntry.add(PieEntry(eggIncome.toFloat(), "Egg Sales"))
                       incomePieEntry.add(PieEntry(flockIncome.toFloat(), "Flock Sales"))
                       mAlternativeIncomeViewModel.alternativeIncomeRevenueByName(thePastTwoYears, theDate).observe(viewLifecycleOwner) {
                               alternativeIncome->
                           for (everyAlternativeIncome in alternativeIncome){
                               incomePieEntry.add(PieEntry(everyAlternativeIncome.quantity.toFloat(), everyAlternativeIncome.name))
                           }

                       pieChartSetUp(incomePieChart, incomePieDataSet, incomePieData, colors, incomeLegend)
                       inflateLegend(inflater, colors, incomePieEntry, llIncomeLegendContainer, currency)


                   }}}}}}}}}
                       true
                   }
                   R.id.miCustomDateRange -> {
                       // instantiate the calendar
                       val myCalendar = Calendar.getInstance()
                       val myFormat = "EEE, dd MMM, yyyy"
                       val simpleDateFormat = SimpleDateFormat(myFormat, Locale.UK)
                       Toast.makeText(context, "Custom Date", Toast.LENGTH_LONG).show()
                       val builder = AlertDialog.Builder(context, Gravity.END)
                       val dialogLayout = inflater.inflate(R.layout.custom_date_layout, null)
                       with(builder) {
                           val tvDatePickerFrom = dialogLayout.findViewById<TextView>(R.id.tvDatePickerFrom_CustomDateLayout)
                           val tvDatePickerTo = dialogLayout.findViewById<TextView>(R.id.tvDatePickerTo_CustomDateLayout)
                           val etListNumber = dialogLayout.findViewById<EditText>(R.id.etListNumber_CustomDateLayout)
                           etListNumber.isGone = true
                           // display and select the date
                           val datePickerFrom =
                               DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
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
                           val datePickerTo =
                               DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
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

                       setTitle("Select the date range")
                       setPositiveButton("Ok") { dialog, where ->

                           if (checkIfEmpty(tvDatePickerFrom.text.toString())) {
                               Toast.makeText(requireContext(), "Please Select Starting Date", Toast.LENGTH_LONG).show()
                           } else if (checkIfEmpty(tvDatePickerTo.text.toString())) {
                               Toast.makeText(requireContext(), "Please Select Ending Date", Toast.LENGTH_LONG).show()
                           } else {
                               val firstDate = simpleDateFormat.parse(tvDatePickerFrom.text.toString())
                               val lastDate = simpleDateFormat.parse(tvDatePickerTo.text.toString())
                               val theFirstDate = firstDate?.time?.let { _firstDate->java.sql.Date(_firstDate) }
                               val theLastDate = lastDate?.time?.let { _lastDate->java.sql.Date(_lastDate) }

                               if (theFirstDate != null) {
                                   if (theLastDate != null) {
                                       llGeneralOverviewLegendContainer.removeAllViews()
                                       llOperationalExpensesLegendContainer.removeAllViews()
                                       llIncomeLegendContainer.removeAllViews()

                                       generalOverviewPieEntry.clear()
                                       generalOverviewLegendPieEntry.clear()
                                       operationalExpensesPieEntry.clear()
                                       incomePieEntry.clear()
                                       // Get all the Income
                                       mEggInventoryReductionViewModel.eggSalesPerDuration(theFirstDate, theLastDate).observe(viewLifecycleOwner){
                                               _eggSales-> eggIncome = _eggSales ?: 0.0

                                       mFlockInventoryReductionViewModel.flockSalesPerDuration(theFirstDate, theLastDate).observe(viewLifecycleOwner){
                                               _flockSales-> flockIncome = _flockSales ?: 0.0

                                       mAlternativeIncomeViewModel.alternativeIncomeRevenuePerDuration(theFirstDate, theLastDate).observe(viewLifecycleOwner){
                                               _alternativeIncome-> alternativeIncome = _alternativeIncome ?: 0.0

                                       // Get all the Expenses
                                       mFeedInventoryAdditionViewModel.feedCostPerDuration(theFirstDate, theLastDate).observe(viewLifecycleOwner){
                                               _feedCost -> feedCost = _feedCost ?: 0.0

                                       mOperationalExpensesViewModel.amountOfOperationalExpensesPerDuration(theFirstDate, theLastDate).observe(viewLifecycleOwner) {
                                               _operationalExpenses -> operationalExpenses = _operationalExpenses ?: 0.0

                                       mMedicationViewModel.costOfMedicationPerDuration(theFirstDate, theLastDate).observe(viewLifecycleOwner){
                                               _medicationCost -> medicationCost = _medicationCost ?: 0.0

                                       mVaccinationViewModel.costOfVaccinationPerDuration(theFirstDate, theLastDate).observe(viewLifecycleOwner){
                                               _vaccinationCost -> vaccinationCost = _vaccinationCost ?: 0.0


                                       income = eggIncome + flockIncome + alternativeIncome
                                       expenses  = operationalExpenses + feedCost + medicationCost + vaccinationCost
                                       netIncome  = if((income > expenses)){(income - expenses)}else (expenses - income)

                                       generalOverviewLegendPieEntry.add(PieEntry(income.toFloat(), "Income"))
                                       generalOverviewLegendPieEntry.add(PieEntry(expenses.toFloat(), "Expenses"))
                                       generalOverviewLegendPieEntry.add(PieEntry((income - expenses).toFloat(), "NetIncome"))

                                       generalOverviewPieEntry.add(PieEntry(income.toFloat(),"Income"))
                                       generalOverviewPieEntry.add(PieEntry(expenses.toFloat(),"Expenses"))
                                       generalOverviewPieEntry.add(PieEntry(netIncome.toFloat(),"Net Income"))

                                       generalOverviewPieDataSet = PieDataSet(generalOverviewPieEntry,"General Overview")
                                       generalOverviewPieData = PieData(generalOverviewPieDataSet)

                                       pieChartSetUp(generalOverviewPieChart, generalOverviewPieDataSet, generalOverviewPieData, colors, generalOverviewLegend)
                                       inflateLegend(inflater, colors, generalOverviewLegendPieEntry, llGeneralOverviewLegendContainer, currency)


                                       // Operational Expenses Pie Chart
                                       operationalExpensesPieEntry.add(PieEntry(feedCost.toFloat(), "Feeding"))
                                       if (vaccinationCost > 0.0 ){operationalExpensesPieEntry.add(PieEntry(vaccinationCost.toFloat(), "Vaccine Cost"))}
                                       if (medicationCost > 0.0){operationalExpensesPieEntry.add(PieEntry(medicationCost.toFloat(), "Medicine Cost"))}
                                       mOperationalExpensesViewModel.operationalExpensesAmountByName(theFirstDate, theLastDate).observe(viewLifecycleOwner) {
                                               operationalExpensesList ->
                                           for (everyExpenses in operationalExpensesList){
                                               operationalExpensesPieEntry.add(PieEntry(everyExpenses.quantity.toFloat(), everyExpenses.name))
                                           }

                                           pieChartSetUp(operationalExpensesPieChart, operationalExpensesPieDataSet, operationalExpensesPieData, colors, operationalExpensesLegend)
                                           inflateLegend(inflater, colors, operationalExpensesPieEntry, llOperationalExpensesLegendContainer, currency)

                                           // Income Pie Chart
                                           incomePieEntry.add(PieEntry(eggIncome.toFloat(), "Egg Sales"))
                                           incomePieEntry.add(PieEntry(flockIncome.toFloat(), "Flock Sales"))
                                           mAlternativeIncomeViewModel.alternativeIncomeRevenueByName(theFirstDate, theLastDate).observe(viewLifecycleOwner) {
                                                   alternativeIncome->
                                               for (everyAlternativeIncome in alternativeIncome){
                                                   incomePieEntry.add(PieEntry(everyAlternativeIncome.quantity.toFloat(), everyAlternativeIncome.name))
                                               }

                                               pieChartSetUp(incomePieChart, incomePieDataSet, incomePieData, colors, incomeLegend)
                                               inflateLegend(inflater, colors, incomePieEntry, llIncomeLegendContainer, currency)


                                           }}}}}}}}}
                                   }
                                   }
                               }
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


       return view
   }

    private fun updateDate(myCalendar: Calendar, tvDatePicker: TextView) {
        val myFormat = "EEE, dd MMM, yyyy"
        val simpleDateFormat = SimpleDateFormat(myFormat, Locale.UK)
        tvDatePicker.text = simpleDateFormat.format(myCalendar.time)
    }
    private fun checkIfEmpty(value: String): Boolean {
        return (TextUtils.isEmpty(value))
    }
    private fun pieChartSetUp(pieChart: PieChart, pieDataSet: PieDataSet, pieData: PieData, colors: MutableList<Int>, legend: Legend){
        pieDataSet.colors = colors
        pieDataSet.valueTextSize = 16f
        if (pieDataSet.entryCount > 5){
            pieDataSet.valueTextColor = Color.TRANSPARENT
            pieChart.setDrawEntryLabels(false)
        }else{
            pieDataSet.valueTextColor = Color.BLACK
            pieChart.setDrawEntryLabels(true)
        }

        pieChart.data = pieData
        pieChart.description.isEnabled = false
        pieChart.centerText = pieDataSet.label
        pieChart.setCenterTextSize(24f)
        pieChart.setHoleColor(Color.TRANSPARENT)
        pieChart.animate()
        pieChart.invalidate()
        pieChart.isDrawHoleEnabled = true
        pieChart.setEntryLabelColor(Color.BLACK)


        legend.isEnabled = false

    }
    private fun inflateLegend(inflater:LayoutInflater, colors: MutableList<Int>, pieEntry: MutableList<PieEntry>, container: LinearLayout, currency: String){
        for (legend in pieEntry.indices) {
            val view= inflater.inflate(R.layout.general_overview_legend, null)
            val legendBox = view.findViewById<View>(R.id.vLegendBox_GeneralOverviewLegend)
            val legendTitle = view.findViewById<TextView>(R.id.tvLegendTitle_GeneralOverviewLegend)
            val legendValue = view.findViewById<TextView>(R.id.tvLegendValue_GeneralOverviewLegend)
            val theLegendValue = "$currency ${pieEntry[legend].value}"
            legendBox.setBackgroundColor(colors[legend%4])
            legendTitle.text=pieEntry[legend].label
            legendValue.text= theLegendValue
            container.addView(view)
        }
    }
}