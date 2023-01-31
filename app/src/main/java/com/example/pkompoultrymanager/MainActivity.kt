package com.example.pkompoultrymanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pkompoultrymanager.health.HealthActivity
import com.example.pkompoultrymanager.health.vaccination.fragments.VaccinationForm
import com.example.pkompoultrymanager.inventory.InventoryActivity
import com.example.pkompoultrymanager.inventory.eggs.EggsActivity
import com.example.pkompoultrymanager.inventory.feed.FeedActivity
import com.example.pkompoultrymanager.reminder.ReminderActivity
import com.example.pkompoultrymanager.tables.TablesActivity
import com.example.pkompoultrymanager.tables.my_farm.MyFarmForm
import com.example.pkompoultrymanager.transactions.TransactionActivity
import com.example.pkompoultrymanager.transactions.cash_in.CashInActivity
import com.example.pkompoultrymanager.transactions.cash_out.operational_expenses.fragments.OperationalExpensesForm
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainActivityToolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.tbMainActivityToolbar)
        setSupportActionBar(mainActivityToolbar)

        val mainFragment = MainFragment()
        setCurrentFragment(mainFragment)






        //drawer view for the various menus
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout_MainActivity)
        val navView = findViewById<NavigationView>(R.id.navView_MainActivity)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.dmEggCollection -> {startActivity(Intent(this@MainActivity, EggsActivity::class.java))
                Toast.makeText(this,"Collect Eggs", Toast.LENGTH_SHORT).show()}

                R.id.dmFeeding -> {startActivity(Intent(this@MainActivity, FeedActivity::class.java))
                Toast.makeText(this,"Feeding", Toast.LENGTH_SHORT).show()}

                R.id.dmOperatingExpenses -> {setCurrentFragment(OperationalExpensesForm())
                Toast.makeText(this,"Operating Expenses", Toast.LENGTH_SHORT).show()}

                R.id.dmVaccination -> {setCurrentFragment(VaccinationForm())
                Toast.makeText(this,"Vaccination", Toast.LENGTH_SHORT).show()}

                R.id.dmHome -> {setCurrentFragment(MainFragment())
                Toast.makeText(this,"Home", Toast.LENGTH_SHORT).show()}

                R.id.dmPeople -> {startActivity(Intent(this@MainActivity, TablesActivity::class.java))
                Toast.makeText(this,"People", Toast.LENGTH_SHORT).show()}

                R.id.dmSetUp -> {setCurrentFragment(MyFarmForm())
                Toast.makeText(this,"Set Up Farm", Toast.LENGTH_SHORT).show()}

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
            replace(R.id.flMainActivity, fragment)
            commit()
        }
    }
}