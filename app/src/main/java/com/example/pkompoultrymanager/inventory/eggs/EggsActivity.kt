package com.example.pkompoultrymanager.inventory.eggs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.pkompoultrymanager.R
import com.example.pkompoultrymanager.inventory.eggs.addition.fragments.EggsAdditionList
import com.example.pkompoultrymanager.inventory.eggs.addition.fragments.UpdateEggsAdditionForm
import com.example.pkompoultrymanager.inventory.eggs.reduction.fragments.EggsReductionList
import com.example.pkompoultrymanager.inventory.eggs.reduction.fragments.UpdateEggsReductionForm
import com.example.pkompoultrymanager.inventory.feed.FeedActivity
import com.example.pkompoultrymanager.inventory.flock.FlockActivity
import com.example.pkompoultrymanager.tables.customer.fragments.UpdateCustomer
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import java.sql.Date

class EggsActivity : AppCompatActivity(), EggsCommunicator {

    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eggs)


        //setting the initial fragment
        val eggAdditionListFragment = EggsAdditionList()
        val eggReductionListFragment = EggsReductionList()
        setCurrentFragment(eggAdditionListFragment)

        //bottomNavigation menu
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView_EggsActivity)
        bottomNavigationView.setOnItemSelectedListener{
            when(it.itemId){
                R.id.bnmAddition -> setCurrentFragment(eggAdditionListFragment)
                R.id.bnmReduction -> setCurrentFragment(eggReductionListFragment)
            }
            true
        }

        //drawer view for the various menus
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout_EggsActivity)
        val navView = findViewById<NavigationView>(R.id.navView_EggsActivity)
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
            replace(R.id.flEggsActivity, fragment)
            commit()
        }
    }

    override fun passEggInventoryAddition(
        EggInventoryAdditionId: Int,
        DateCollected: String,
        Flock: String,
        EggType: String,
        NumberOfEggs: String,
        ShortNotes: String
    ) {
        val bundle = Bundle()
        val updateEggsInventoryAddition = UpdateEggsAdditionForm()

        bundle.putInt("eggInventoryId", EggInventoryAdditionId)
        bundle.putString("dateCollected", DateCollected)
        bundle.putString("flock", Flock)
        bundle.putString("eggType", EggType)
        bundle.putString("eggQuantity", NumberOfEggs)
        bundle.putString("shortNotes", ShortNotes)

        updateEggsInventoryAddition.arguments = bundle

        supportFragmentManager.beginTransaction().replace(
            R.id.flEggsActivity,
            updateEggsInventoryAddition
        ).addToBackStack(null).commit()
    }

    override fun passEggInventoryReduction(
        EggInventoryReductionId: String,
        DateReduced: String,
        ReductionReason: String,
        EggType: String,
        NumberOfEggs: String,
        EggCost: String,
        AmountPaid: String,
        Customer: String,
        ShortNotes: String
    ) {
        val bundle = Bundle()
        val updateEggsInventoryReduction = UpdateEggsReductionForm()

        bundle.putString("eggInventoryReductionId", EggInventoryReductionId)
        bundle.putString("dateReduced", DateReduced)
        bundle.putString("reductionReason", ReductionReason)
        bundle.putString("eggType", EggType)
        bundle.putString("numberOfEggs", NumberOfEggs)
        bundle.putString("eggCost", EggCost)
        bundle.putString("amountPaid", AmountPaid)
        bundle.putString("customer", Customer)
        bundle.putString("shortNotes", ShortNotes)

        updateEggsInventoryReduction.arguments = bundle

        supportFragmentManager.beginTransaction().replace(
            R.id.flEggsActivity,
            updateEggsInventoryReduction
        ).commit()
    }
}
