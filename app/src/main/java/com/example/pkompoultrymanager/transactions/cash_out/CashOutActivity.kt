package com.example.pkompoultrymanager.transactions.cash_out

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.pkompoultrymanager.R
import com.example.pkompoultrymanager.transactions.cash_in.CashInCommunicator
import com.example.pkompoultrymanager.transactions.cash_in.CashInFragment
import com.example.pkompoultrymanager.transactions.cash_in.alternative_income.fragments.UpdateAlternativeIncomeForm
import com.example.pkompoultrymanager.transactions.cash_out.capital_expenses.fragments.UpdateCapitalExpensesForm
import com.example.pkompoultrymanager.transactions.cash_out.loan_repayment.fragments.UpdateLoanRepaymentForm
import com.example.pkompoultrymanager.transactions.cash_out.operational_expenses.fragments.UpdateOperationalExpensesForm
import com.google.android.material.navigation.NavigationView

class CashOutActivity : AppCompatActivity(), CashOutCommunicator {

    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cash_out)


        //setting the current fragment
        val cashOutFragment = CashOutFragment()
        setCurrentFragment(cashOutFragment)


        //drawer view for the various menus
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout_CashOutActivity)
        val navView = findViewById<NavigationView>(R.id.navView_CashOutActivity)
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
            replace(R.id.flCashOutActivity, fragment)
            commit()
        }
    }

    override fun passCapitalExpenses(
        CapitalExpensesId: String,
        ExpenseDate: String,
        ExpenseName: String,
        PaymentMethod: String,
        ExpenseAmount: String,
        TransactionID: String,
        ShortNotes: String
    ) {
        val bundle = Bundle()
        val updateCapitalExpensesForm = UpdateCapitalExpensesForm()

        bundle.putString("capitalExpensesId", CapitalExpensesId)
        bundle.putString("expenseDate", ExpenseDate)
        bundle.putString("expenseName", ExpenseName)
        bundle.putString("paymentMethod", PaymentMethod)
        bundle.putString("expenseAmount", ExpenseAmount)
        bundle.putString("transactionID", TransactionID)
        bundle.putString("shortNotes", ShortNotes)

        updateCapitalExpensesForm.arguments = bundle

        supportFragmentManager.beginTransaction().replace(
            R.id.flCashOutActivity,
            updateCapitalExpensesForm
        ).commit()
    }

    override fun passOperationalExpenses(
        OperationalExpensesId: String,
        ExpenseDate: String,
        ExpenseName: String,
        PaymentMethod: String,
        ExpenseAmount: String,
        TransactionID: String,
        ShortNotes: String
    ) {
        val bundle = Bundle()
        val updateOperationalExpensesForm = UpdateOperationalExpensesForm()

        bundle.putString("capitalExpensesId", OperationalExpensesId)
        bundle.putString("expenseDate", ExpenseDate)
        bundle.putString("expenseName", ExpenseName)
        bundle.putString("paymentMethod", PaymentMethod)
        bundle.putString("expenseAmount", ExpenseAmount)
        bundle.putString("transactionID", TransactionID)
        bundle.putString("shortNotes", ShortNotes)

        updateOperationalExpensesForm.arguments = bundle

        supportFragmentManager.beginTransaction().replace(
            R.id.flCashOutActivity,
            updateOperationalExpensesForm
        ).commit()
    }

    override fun passLoanRepayment(
        LoanRepaymentId: String,
        LoanRepaymentDate: String,
        Lender: String,
        PaymentMethod: String,
        LoanAmountRepaid: String,
        TransactionID: String,
        ShortNotes: String
    ) {
        val bundle = Bundle()
        val updateLoanRepaymentForm = UpdateLoanRepaymentForm()

        bundle.putString("loanRepaymentId", LoanRepaymentId)
        bundle.putString("loanRepaymentDate", LoanRepaymentDate)
        bundle.putString("lender", Lender)
        bundle.putString("paymentMethod", PaymentMethod)
        bundle.putString("amountRepaid", LoanAmountRepaid)
        bundle.putString("transactionID", TransactionID)
        bundle.putString("shortNotes", ShortNotes)

        updateLoanRepaymentForm.arguments = bundle

        supportFragmentManager.beginTransaction().replace(
            R.id.flCashOutActivity,
            updateLoanRepaymentForm
        ).commit()
    }
}