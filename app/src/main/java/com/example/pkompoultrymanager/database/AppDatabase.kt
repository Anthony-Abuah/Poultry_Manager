package com.example.pkompoultrymanager.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.pkompoultrymanager.health.medication.Medication
import com.example.pkompoultrymanager.health.medication.MedicationDao
import com.example.pkompoultrymanager.health.vaccination.Vaccination
import com.example.pkompoultrymanager.health.vaccination.VaccinationDao
import com.example.pkompoultrymanager.inventory.eggs.addition.EggInventoryAddition
import com.example.pkompoultrymanager.inventory.eggs.addition.EggInventoryAdditionDao
import com.example.pkompoultrymanager.inventory.eggs.reduction.EggInventoryReduction
import com.example.pkompoultrymanager.inventory.eggs.reduction.EggInventoryReductionDao
import com.example.pkompoultrymanager.inventory.feed.addition.FeedInventoryAddition
import com.example.pkompoultrymanager.inventory.feed.addition.FeedInventoryAdditionDao
import com.example.pkompoultrymanager.inventory.feed.reduction.FeedInventoryReduction
import com.example.pkompoultrymanager.inventory.feed.reduction.FeedInventoryReductionDao
import com.example.pkompoultrymanager.inventory.flock.addition.FlockInventoryAddition
import com.example.pkompoultrymanager.inventory.flock.addition.FlockInventoryAdditionDao
import com.example.pkompoultrymanager.inventory.flock.reduction.FlockInventoryReduction
import com.example.pkompoultrymanager.inventory.flock.reduction.FlockInventoryReductionDao
import com.example.pkompoultrymanager.reminder.Reminder
import com.example.pkompoultrymanager.tables.customer.Customer
import com.example.pkompoultrymanager.tables.customer.CustomerDao
import com.example.pkompoultrymanager.tables.customer.VaccineAdministrator
import com.example.pkompoultrymanager.tables.egg_type.EggType
import com.example.pkompoultrymanager.tables.egg_type.EggTypeDao
import com.example.pkompoultrymanager.tables.feed.Feed
import com.example.pkompoultrymanager.tables.feed.FeedDao
import com.example.pkompoultrymanager.tables.feed_source.FeedSource
import com.example.pkompoultrymanager.tables.feed_source.FeedSourceDao
import com.example.pkompoultrymanager.tables.flock_source.FlockSource
import com.example.pkompoultrymanager.tables.flock_source.FlockSourceDao
import com.example.pkompoultrymanager.tables.lender_investor.LenderInvestor
import com.example.pkompoultrymanager.tables.lender_investor.LenderInvestorDao
import com.example.pkompoultrymanager.tables.my_farm.MyFarm
import com.example.pkompoultrymanager.tables.my_farm.MyFarmDao
import com.example.pkompoultrymanager.tables.user_info.UserInfo
import com.example.pkompoultrymanager.tables.vaccine_administrator.VaccineAdministratorDao
import com.example.pkompoultrymanager.transactions.cash_in.advanced_purchases.AdvancePurchase
import com.example.pkompoultrymanager.transactions.cash_in.advanced_purchases.AdvancePurchaseDao
import com.example.pkompoultrymanager.transactions.cash_in.alternative_income.AlternativeIncome
import com.example.pkompoultrymanager.transactions.cash_in.alternative_income.AlternativeIncomeDao
import com.example.pkompoultrymanager.transactions.cash_in.investment.Investment
import com.example.pkompoultrymanager.transactions.cash_in.investment.InvestmentDao
import com.example.pkompoultrymanager.transactions.cash_in.loans.Loans
import com.example.pkompoultrymanager.transactions.cash_in.loans.LoansDao
import com.example.pkompoultrymanager.transactions.cash_out.capital_expenses.CapitalExpenses
import com.example.pkompoultrymanager.transactions.cash_out.capital_expenses.CapitalExpensesDao
import com.example.pkompoultrymanager.transactions.cash_out.loan_repayment.LoanRepayment
import com.example.pkompoultrymanager.transactions.cash_out.loan_repayment.LoanRepaymentDao
import com.example.pkompoultrymanager.transactions.cash_out.operational_expenses.OperationalExpenses
import com.example.pkompoultrymanager.transactions.cash_out.operational_expenses.OperationalExpensesDao


@Database(entities = [Customer::class,
    Feed::class,
    FeedSource::class,
    FlockSource::class,
    LenderInvestor::class,
    MyFarm::class,
    EggType::class,
    VaccineAdministrator::class,
    Medication::class,
    Vaccination::class,
    EggInventoryAddition::class,
    EggInventoryReduction::class,
    FlockInventoryAddition::class,
    FlockInventoryReduction::class,
    FeedInventoryAddition::class,
    FeedInventoryReduction::class,
    UserInfo::class,
    Reminder::class,
    AlternativeIncome::class,
    Investment::class,
    Loans::class,
    CapitalExpenses::class,
    OperationalExpenses::class,
    LoanRepayment::class,
    AdvancePurchase::class],
    version = 3,
    exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase(){

    abstract fun customerDao(): CustomerDao
    abstract fun feedDao(): FeedDao
    abstract fun feedSourceDao(): FeedSourceDao
    abstract fun flockSourceDao(): FlockSourceDao
    abstract fun lenderInvestorDao(): LenderInvestorDao
    abstract fun eggTypeDao(): EggTypeDao
    abstract fun vaccineAdministratorDao(): VaccineAdministratorDao
    abstract fun myFarmDao(): MyFarmDao
    abstract fun medicationDao(): MedicationDao
    abstract fun vaccinationDao(): VaccinationDao
    abstract fun eggInventoryAdditionDao(): EggInventoryAdditionDao
    abstract fun eggInventoryReductionDao(): EggInventoryReductionDao
    abstract fun feedInventoryAdditionDao(): FeedInventoryAdditionDao
    abstract fun feedInventoryReductionDao(): FeedInventoryReductionDao
    abstract fun flockInventoryAdditionDao(): FlockInventoryAdditionDao
    abstract fun flockInventoryReductionDao(): FlockInventoryReductionDao
    abstract fun advancePurchaseDao(): AdvancePurchaseDao
    abstract fun alternativeIncomeDao(): AlternativeIncomeDao
    abstract fun investmentDao(): InvestmentDao
    abstract fun loansDao(): LoansDao
    abstract fun capitalExpensesDao(): CapitalExpensesDao
    abstract fun operationalExpensesDao(): OperationalExpensesDao
    abstract fun loanRepaymentDao(): LoanRepaymentDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase?= null

        fun getDatabase(context: Context): AppDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "UserDatabase"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }

        }

    }

}