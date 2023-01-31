package com.example.pkompoultrymanager.inventory.flock

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.pkompoultrymanager.R
import com.example.pkompoultrymanager.inventory.feed.reduction.fragments.UpdateFeedInventoryReductionForm
import com.example.pkompoultrymanager.inventory.flock.addition.fragments.FlockAdditionList
import com.example.pkompoultrymanager.inventory.flock.addition.fragments.UpdateFlockAdditionForm
import com.example.pkompoultrymanager.inventory.flock.reduction.fragments.FlockReductionList
import com.example.pkompoultrymanager.inventory.flock.reduction.fragments.UpdateFlockReductionForm
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class FlockActivity : AppCompatActivity(), FlockCommunicator {

    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flock)

        //setting the initial fragment
        val flockAdditionList = FlockAdditionList()
        val flockReductionList = FlockReductionList()
        setCurrentFragment(flockAdditionList)

        //bottomNavigation menu
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView_FlockActivity)
        bottomNavigationView.setOnItemSelectedListener{
            when(it.itemId){
                R.id.bnmAddition -> setCurrentFragment(flockAdditionList)
                R.id.bnmReduction -> setCurrentFragment(flockReductionList)
            }
            true
        }

        //drawer view for the various menus
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout_FlockActivity)
        val navView = findViewById<NavigationView>(R.id.navView_FlockActivity)
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
            replace(R.id.flFlockActivity, fragment)
            commit()
        }
    }

    override fun passFlockInventoryAddition(
        FlockInventoryAdditionId: String,
        DateAdded: String,
        FlockName: String,
        FlockBreed: String,
        FlockSource: String,
        FlockPurpose: String,
        FlockQuantity: String,
        FlockAge: String,
        FlockCost: String,
        ShortNotes: String
    ) {
        val bundle = Bundle()
        val updateFlockInventoryAddition = UpdateFlockAdditionForm()

        bundle.putString("flockInventoryAdditionId", FlockInventoryAdditionId)
        bundle.putString("dateAdded", DateAdded)
        bundle.putString("flockName", FlockName)
        bundle.putString("flockBreed", FlockBreed)
        bundle.putString("flockSource", FlockSource)
        bundle.putString("flockPurpose", FlockPurpose)
        bundle.putString("flockQuantity", FlockQuantity)
        bundle.putString("flockAge", FlockAge)
        bundle.putString("flockCost", FlockCost)
        bundle.putString("shortNotes", ShortNotes)

        updateFlockInventoryAddition.arguments = bundle

        supportFragmentManager.beginTransaction().replace(
            R.id.flFlockActivity,
            updateFlockInventoryAddition
        ).commit()
    }

    override fun passFlockInventoryReduction(
        FlockInventoryReductionId: String,
        DateReduced: String,
        FlockName: String,
        Customer: String,
        ReductionReason: String,
        ReductionQuantity: String,
        FlockCost: String,
        AmountReceived: String,
        ShortNotes: String
    ) {
        val bundle = Bundle()
        val updateFlockInventoryReduction = UpdateFlockReductionForm()

        bundle.putString("flockInventoryReductionId", FlockInventoryReductionId)
        bundle.putString("dateReduced", DateReduced)
        bundle.putString("flockName", FlockName)
        bundle.putString("customer", Customer)
        bundle.putString("reductionReason", ReductionReason)
        bundle.putString("reductionQuantity", ReductionQuantity)
        bundle.putString("flockCost", FlockCost)
        bundle.putString("amountReceived", AmountReceived)
        bundle.putString("shortNotes", ShortNotes)

        updateFlockInventoryReduction.arguments = bundle

        supportFragmentManager.beginTransaction().replace(
            R.id.flFlockActivity,
            updateFlockInventoryReduction
        ).commit()    }
}
