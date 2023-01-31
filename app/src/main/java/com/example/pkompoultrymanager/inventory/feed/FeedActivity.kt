package com.example.pkompoultrymanager.inventory.feed

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.pkompoultrymanager.R
import com.example.pkompoultrymanager.inventory.eggs.reduction.fragments.UpdateEggsReductionForm
import com.example.pkompoultrymanager.inventory.feed.addition.fragments.FeedInventoryAdditionList
import com.example.pkompoultrymanager.inventory.feed.addition.fragments.UpdateFeedInventoryAdditionForm
import com.example.pkompoultrymanager.inventory.feed.reduction.fragments.FeedInventoryReductionList
import com.example.pkompoultrymanager.inventory.feed.reduction.fragments.UpdateFeedInventoryReductionForm
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class FeedActivity : AppCompatActivity(), FeedCommunicator {

    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)



        //setting the initial fragment
        val feedInventoryAdditionList = FeedInventoryAdditionList()
        val feedInventoryReductionList = FeedInventoryReductionList()
        setCurrentFragment(feedInventoryAdditionList)

        //bottomNavigation menu
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView_FeedActivity)
        bottomNavigationView.setOnItemSelectedListener{
            when(it.itemId){
                R.id.bnmAddition -> setCurrentFragment(feedInventoryAdditionList)
                R.id.bnmReduction -> setCurrentFragment(feedInventoryReductionList)
            }
            true
        }

        //drawer view for the various menus
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout_FeedActivity)
        val navView = findViewById<NavigationView>(R.id.navView_FeedActivity)
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
            replace(R.id.flFeedActivity, fragment)
            commit()
        }
    }

    override fun passFeedInventoryAddition(
        FeedInventoryAdditionId: String,
        DateAcquired: String,
        FeedName: String,
        FeedQuantity: String,
        FeedCost: String,
        FeedSource: String,
        ShortNotes: String
    ) {
        val bundle = Bundle()
        val updateFeedInventoryAddition = UpdateFeedInventoryAdditionForm()

        bundle.putString("feedInventoryAdditionId", FeedInventoryAdditionId)
        bundle.putString("dateAcquired", DateAcquired)
        bundle.putString("feedName", FeedName)
        bundle.putString("feedQuantity", FeedQuantity)
        bundle.putString("feedCost", FeedCost)
        bundle.putString("feedSource", FeedSource)
        bundle.putString("shortNotes", ShortNotes)

        updateFeedInventoryAddition.arguments = bundle

        supportFragmentManager.beginTransaction().replace(
            R.id.flFeedActivity,
            updateFeedInventoryAddition
        ).commit()
    }

    override fun passFeedInventoryReduction(
        FeedInventoryReductionId: String,
        ReductionDate: String,
        FeedName: String,
        FlockName: String,
        ReductionQuantity: String,
        ReductionReason: String,
        ShortNotes: String
    ) {
        val bundle = Bundle()
        val updateFeedInventoryReduction = UpdateFeedInventoryReductionForm()

        bundle.putString("feedInventoryReductionId", FeedInventoryReductionId)
        bundle.putString("reductionDate", ReductionDate)
        bundle.putString("feedName", FeedName)
        bundle.putString("flockName", FlockName)
        bundle.putString("reductionQuantity", ReductionQuantity)
        bundle.putString("reductionReason", ReductionReason)
        bundle.putString("shortNotes", ShortNotes)

        updateFeedInventoryReduction.arguments = bundle

        supportFragmentManager.beginTransaction().replace(
            R.id.flFeedActivity,
            updateFeedInventoryReduction
        ).commit()    }
}