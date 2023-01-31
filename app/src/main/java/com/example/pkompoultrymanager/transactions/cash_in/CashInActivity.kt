package com.example.pkompoultrymanager.transactions.cash_in

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.pkompoultrymanager.R
import com.example.pkompoultrymanager.tables.customer.fragments.UpdateCustomer
import com.example.pkompoultrymanager.transactions.TransactionFragment
import com.example.pkompoultrymanager.transactions.cash_in.advanced_purchases.fragments.UpdateAdvancedPurchaseForm
import com.example.pkompoultrymanager.transactions.cash_in.alternative_income.fragments.UpdateAlternativeIncomeForm
import com.example.pkompoultrymanager.transactions.cash_in.investment.fragments.UpdateInvestmentForm
import com.example.pkompoultrymanager.transactions.cash_in.loans.fragments.UpdateLoansForm
import com.google.android.material.navigation.NavigationView
import java.util.*

class CashInActivity : AppCompatActivity(), CashInCommunicator {

    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cash_in)

        //setting the current fragment
        val cashInFragment = CashInFragment()
        setCurrentFragment(cashInFragment)

        //drawer view for the various menus
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout_CashInActivity)
        val navView = findViewById<NavigationView>(R.id.navView_CashInActivity)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when(it.itemId){

            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        when(item.itemId){
            R.id.miHelp -> Toast.makeText(this,"Help and Feedback", Toast.LENGTH_SHORT).show()
            R.id.miSettings -> Toast.makeText(this,"Settings", Toast.LENGTH_SHORT).show()
            R.id.miPrivacy -> Toast.makeText(this,"Privacy", Toast.LENGTH_SHORT).show()
            R.id.miShare -> Toast.makeText(this,"Share with colleagues", Toast.LENGTH_SHORT).show()
        }
        return true
    }

    //App bar menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.app_bar_menu,menu)
        return true
    }
    private fun setCurrentFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flCashInActivity, fragment)
            commit()
        }
    }

    override fun passAdvancedPurchase(
        AdvancePurchaseId: String,
        Date: String,
        Customer: String,
        ProductPurchased: String,
        ProductPurchased_Cost: String,
        AmountReceived: String,
        ReceiptNumber: String,
        ProductDeliveredDate: String?,
        HasBeenDelivered: Boolean,
        MoneyReturned:Boolean,
        AmountReturned:String,
        shortNotes: String
    ) {
        val bundle = Bundle()
        val updateAdvancePurchase = UpdateAdvancedPurchaseForm()

        bundle.putString("id", AdvancePurchaseId)
        bundle.putString("advancedPurchaseDate", Date)
        bundle.putString("customer", Customer)
        bundle.putString("productPurchased", ProductPurchased)
        bundle.putString("productPurchasedCost", ProductPurchased_Cost)
        bundle.putString("amountReceived", AmountReceived)
        bundle.putString("receiptNumber", ReceiptNumber)
        bundle.putString("productDeliveredDate", ProductDeliveredDate)
        bundle.putBoolean("checkIfDelivered", HasBeenDelivered)
        bundle.putBoolean("checkIfMoneyIsReturned", MoneyReturned)
        bundle.putString("amountReturned", AmountReturned)
        bundle.putString("shortNotes", shortNotes)
        updateAdvancePurchase.arguments = bundle

        supportFragmentManager.beginTransaction().replace(
            R.id.flCashInActivity,
            updateAdvancePurchase
        ).commit()
    }

    override fun passAlternativeIncome(
        AlternativeIncomeId: String,
        Date: String,
        ItemPurchased: String,
        Customer: String,
        ItemPurchasedQuantity: String,
        ItemPurchasedCost: String,
        AmountReceived: String,
        ReceiptNumber: String,
        shortNotes: String
    ) {
        val bundle = Bundle()
        val updateAlternativeIncome = UpdateAlternativeIncomeForm()

        bundle.putString("alternativeIncomeId", AlternativeIncomeId)
        bundle.putString("date", Date)
        bundle.putString("itemPurchased", ItemPurchased)
        bundle.putString("customer", Customer)
        bundle.putString("itemPurchasedQuantity", ItemPurchasedQuantity)
        bundle.putString("itemPurchasedCost", ItemPurchasedCost)
        bundle.putString("amountReceived", AmountReceived)
        bundle.putString("receiptNumber", ReceiptNumber)
        bundle.putString("shortNotes", shortNotes)

        updateAlternativeIncome.arguments = bundle

        supportFragmentManager.beginTransaction().replace(
            R.id.flCashInActivity,
            updateAlternativeIncome
        ).commit()
    }


    override fun passInvestment(
        InvestmentId: String,
        Date: String,
        Investor: String,
        AmountInvested: String,
        TransactionID: String,
        ShortNotes: String
    ) {
        val bundle = Bundle()
        val updateInvestmentForm = UpdateInvestmentForm()

        bundle.putString("investmentId", InvestmentId)
        bundle.putString("date", Date)
        bundle.putString("investor", Investor)
        bundle.putString("amountInvested", AmountInvested)
        bundle.putString("transactionID", TransactionID)
        bundle.putString("shortNotes", ShortNotes)

        updateInvestmentForm.arguments = bundle

        supportFragmentManager.beginTransaction().replace(
            R.id.flCashInActivity,
            updateInvestmentForm
        ).commit()
    }

    override fun passLoans(
        LoansId: String,
        Date: String,
        Lender: String,
        LoanAmount: String,
        TransactionID: String,
        ShortNotes: String
    ) {
        val bundle = Bundle()
        val updateLoansForm = UpdateLoansForm()

        bundle.putString("loansId", LoansId)
        bundle.putString("date", Date)
        bundle.putString("lender", Lender)
        bundle.putString("loanAmount", LoanAmount)
        bundle.putString("transactionID", TransactionID)
        bundle.putString("shortNotes", ShortNotes)

        updateLoansForm.arguments = bundle

        supportFragmentManager.beginTransaction().replace(
            R.id.flCashInActivity,
            updateLoansForm
        ).commit()
    }
}
