package com.example.pkompoultrymanager.reports

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.pkompoultrymanager.R
import com.example.pkompoultrymanager.health.medication.MedicationViewModel
import com.example.pkompoultrymanager.health.vaccination.VaccinationViewModel
import com.example.pkompoultrymanager.inventory.eggs.addition.EggInventoryAdditionViewModel
import com.example.pkompoultrymanager.inventory.eggs.addition.fragments.EggsAdditionForm
import com.example.pkompoultrymanager.inventory.eggs.reduction.EggInventoryReductionViewModel
import com.example.pkompoultrymanager.inventory.feed.addition.FeedInventoryAdditionViewModel
import com.example.pkompoultrymanager.inventory.feed.reduction.FeedInventoryReductionViewModel
import com.example.pkompoultrymanager.inventory.flock.addition.FlockInventoryAdditionViewModel
import com.example.pkompoultrymanager.inventory.flock.reduction.FlockInventoryReductionViewModel
import com.example.pkompoultrymanager.reports.egg_inventory.fragments.GeneralEggReport
import com.example.pkompoultrymanager.reports.feed_inventory.fragments.GeneralFeedReport
import com.example.pkompoultrymanager.reports.flock_inventory.fragments.GeneralFlockReport
import com.example.pkompoultrymanager.reports.income_expenses.fragments.GeneralIncomeExpenseReport
import com.example.pkompoultrymanager.transactions.cash_in.alternative_income.AlternativeIncomeViewModel
import com.example.pkompoultrymanager.transactions.cash_out.operational_expenses.OperationalExpensesViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class ReportFragment : Fragment() {
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

    private var alternativeIncome = 0.0
    private var vaccinationCost = 0.0
    private var medicationCost = 0.0
    private var operationalExpenses = 0.0
    private var eggIncome = 0.0
    private var feedCost = 0.0
    private var flock = 0
    private var flockIncome = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_report, container, false)

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

        myFarmInfo = context?.getSharedPreferences("MyFarmInfo", Context.MODE_PRIVATE)!!
        val numberOfEggsPerCrate = myFarmInfo.getInt("numberOfFlocksPerCrate", 30)
        val currency = myFarmInfo.getString("currency", "GHS")
        val measuringUnit = myFarmInfo.getString("measuringUnit", "kg")


        val systemFormat = "dd-MM-yyyy"
        val systemDateFormat = SimpleDateFormat(systemFormat, Locale.UK)

        // get the date values from calendar instance
        val currentCalendar = Calendar.getInstance()
        val currentYear = currentCalendar.get(Calendar.YEAR)
        val currentMonth = currentCalendar.get(Calendar.MONTH)
        val currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH)

        val month = LocalDate.now().month.toString()


        val currentDate = "$currentDay-${currentMonth + 1}-$currentYear"
        val previousMonth = "${1}-${currentMonth + 1}-$currentYear"
        val theCurrentSystemDate: Date? = systemDateFormat.parse(currentDate)
        val thePreviousSystemMonth: Date? = systemDateFormat.parse(previousMonth)
        val theCurrentDate = theCurrentSystemDate?.time?.let { java.sql.Date(it) }!!
        val thePreviousMonth = thePreviousSystemMonth?.time?.let { java.sql.Date(it) }!!

        val tvEggsCollected = view.findViewById<TextView>(R.id.tvEggsCollected_ReportFragment)
        val tvEggsSold = view.findViewById<TextView>(R.id.tvEggsSold_ReportFragment)
        val tvEggCollectionPeriod = view.findViewById<TextView>(R.id.tvEggCollectionPeriod_ReportFragment)

        val tvFlocksSold = view.findViewById<TextView>(R.id.tvFlockSold_ReportFragment)
        val tvFlocksDead = view.findViewById<TextView>(R.id.tvDeadFlock_ReportFragment)
        val tvFlockPeriod = view.findViewById<TextView>(R.id.tvFlockSalesPeriod_ReportFragment)

        val tvVaccinatedBirds = view.findViewById<TextView>(R.id.tvTreatedBirds_ReportFragment)
        val tvHealthCost = view.findViewById<TextView>(R.id.tvHealthCost_ReportFragment)
        val tvHealthPeriod = view.findViewById<TextView>(R.id.tvHealthPeriod_ReportFragment)

        val tvIncome = view.findViewById<TextView>(R.id.tvIncomeValue_ReportFragment)
        val tvExpense = view.findViewById<TextView>(R.id.tvExpensesValue_ReportFragment)
        val tvNetIncome = view.findViewById<TextView>(R.id.tvNetIncomeValue_ReportFragment)
        val tvIncomePeriod = view.findViewById<TextView>(R.id.tvIncomeStatementPeriod_ReportFragment)

        val tvFeedingQuantity = view.findViewById<TextView>(R.id.tvFeedQuantity_ReportFragment)
        val tvFeedAcquired = view.findViewById<TextView>(R.id.tvFeedAcquired_ReportFragment)
        val tvMeasuringUnit = view.findViewById<TextView>(R.id.tvMeasuringUnit_ReportFragment)
        val tvFeedingPeriod = view.findViewById<TextView>(R.id.tvFeedingPeriod_ReportFragment)

        val clEggReport = view.findViewById<ConstraintLayout>(R.id.clEggCollection_ReportFragment)
        clEggReport.setOnClickListener {
            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flReportActivity,
                GeneralEggReport()
            ).commit()
        }

        val clFeedReport = view.findViewById<ConstraintLayout>(R.id.clFeeding_ReportFragment)
        clFeedReport.setOnClickListener {
            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flReportActivity,
                GeneralFeedReport()
            ).commit()
        }
        val clFlockReport = view.findViewById<ConstraintLayout>(R.id.clFlockSales_ReportFragment)
        clFlockReport.setOnClickListener {
            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flReportActivity,
                GeneralFlockReport()
            ).commit()
        }

        val clIncomeStatement = view.findViewById<ConstraintLayout>(R.id.clIncomeStatement_ReportFragment)
        clIncomeStatement.setOnClickListener {
            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flReportActivity,
                GeneralIncomeExpenseReport()
            ).commit()
        }

        // Egg Report
        tvEggCollectionPeriod.text = month
        mEggInventoryAdditionViewModel.numberOfEggsAddedPerDuration(thePreviousMonth, theCurrentDate).observe(viewLifecycleOwner){
            eggsCollected -> eggsCollected.let{
            val eggs = it ?: 0
            val theEggsCollected = eggs.div(numberOfEggsPerCrate)
            tvEggsCollected.text= theEggsCollected.toString() }
        }

        mEggInventoryReductionViewModel.numberOfEggsSoldPerDuration(thePreviousMonth, theCurrentDate).observe(viewLifecycleOwner){
            _eggsSold -> val eggs = _eggsSold ?: 0
            val eggSold = "${eggs/numberOfEggsPerCrate} crates sold"
            tvEggsSold.text= eggSold
        }

        // Flock Report
        tvFlockPeriod.text = month
        mFlockInventoryReductionViewModel.numberOfDeadFlockPerDuration(thePreviousMonth, theCurrentDate).observe(viewLifecycleOwner){
            _numberOfDeadFlock-> flock = _numberOfDeadFlock ?: 0
        }
        val deadFlock = "$flock birds dead"
        tvFlocksDead.text= deadFlock

        mFlockInventoryReductionViewModel.numberOfSoldFlockPerDuration(thePreviousMonth, theCurrentDate).observe(viewLifecycleOwner){
            _numberOfSoldFlock-> tvFlocksSold.text = "$_numberOfSoldFlock"
        }

        // Health Report
        tvHealthPeriod.text = month
        mVaccinationViewModel.numberOfVaccinatedBirdsPerDuration(thePreviousMonth, theCurrentDate).observe(viewLifecycleOwner){
            _numberOfVaccinatedBirds -> val vaccinatedBirds = _numberOfVaccinatedBirds ?: 0
            tvVaccinatedBirds.text= vaccinatedBirds.toString()
        }

        mVaccinationViewModel.costOfVaccinationPerDuration(thePreviousMonth, theCurrentDate).observe(viewLifecycleOwner){
            _vaccinationCost -> vaccinationCost = _vaccinationCost ?: 0.0
            val theVaccinationCost = "$currency $vaccinationCost spent on vaccination"
            tvHealthCost.text= theVaccinationCost
        }

        // Feed Report
        tvFeedingPeriod.text = month
        tvMeasuringUnit.text = measuringUnit
        mFeedInventoryAdditionViewModel.quantityOfFeedAddedPerDuration(thePreviousMonth, theCurrentDate).observe(viewLifecycleOwner){
            _quantityOfFeedAcquired -> val feedAcquired = "$_quantityOfFeedAcquired $measuringUnit acquired"
            tvFeedAcquired.text= feedAcquired
        }

        mFeedInventoryReductionViewModel.feedQuantityUsedForFeedingPerDuration(thePreviousMonth, theCurrentDate).observe(viewLifecycleOwner){
            _quantityOfFeedUsedForFeeding -> val feedingQuantity = "${_quantityOfFeedUsedForFeeding ?: 0.0}"
            tvFeedingQuantity.text= feedingQuantity
        }

        // Getting the total Income Statement
        tvIncomePeriod.text = month
        //mEggInventoryReductionViewModel.getEggSales(thePreviousMonth, theCurrentDate)
        mEggInventoryReductionViewModel.eggSalesPerDuration(thePreviousMonth, theCurrentDate).observe(viewLifecycleOwner){
                _eggIncome-> eggIncome = _eggIncome ?: 0.0


        mFlockInventoryReductionViewModel.flockSalesPerDuration(thePreviousMonth, theCurrentDate).observe(viewLifecycleOwner){
                _flockIncome-> flockIncome = _flockIncome ?: 0.0


        mAlternativeIncomeViewModel.alternativeIncomeRevenuePerDuration(thePreviousMonth, theCurrentDate).observe(viewLifecycleOwner){
            _alternativeIncome-> alternativeIncome = _alternativeIncome ?: 0.0


        // Get all the Expenses
        mFeedInventoryAdditionViewModel.feedCostPerDuration(thePreviousMonth, theCurrentDate).observe(viewLifecycleOwner){
            _feedCost -> feedCost = _feedCost ?: 0.0


        mOperationalExpensesViewModel.amountOfOperationalExpensesPerDuration(thePreviousMonth, theCurrentDate).observe(viewLifecycleOwner) {
            _operationalExpenses -> operationalExpenses = _operationalExpenses ?: 0.0


        mVaccinationViewModel.costOfVaccinationPerDuration(thePreviousMonth, theCurrentDate).observe(viewLifecycleOwner){
            _vaccinationCost -> vaccinationCost = _vaccinationCost ?: 0.0

        mMedicationViewModel.costOfMedicationPerDuration(thePreviousMonth, theCurrentDate).observe(viewLifecycleOwner){
            _medicationCost -> medicationCost = _medicationCost ?: 0.0

            // Total Income
            val income = eggIncome + flockIncome + alternativeIncome
            val theIncome = "$currency $income"

            tvIncome.text = theIncome

            // Total Expenses
            val theOperationalExpenses = feedCost + operationalExpenses + vaccinationCost + medicationCost
            val theExpenses = "$currency $theOperationalExpenses"
            tvExpense.text = theExpenses

            // Getting the Total NetIncome
            val netIncome = income - theOperationalExpenses
            val theNetIncome = "$currency $netIncome"
            tvNetIncome.text = theNetIncome.toString()

            tvIncome.text = theIncome

        }}}}}}}



        


        return view
    }


}