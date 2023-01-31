package com.example.pkompoultrymanager.tables

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
import com.example.pkompoultrymanager.tables.egg_type.fragments.UpdateEggType
import com.example.pkompoultrymanager.tables.feed.fragments.UpdateFeed
import com.example.pkompoultrymanager.tables.feed_source.fragments.UpdateFeedSource
import com.example.pkompoultrymanager.tables.flock_source.fragments.UpdateFlockSource
import com.example.pkompoultrymanager.tables.lender_investor.fragments.UpdateLenderInvestor
import com.example.pkompoultrymanager.tables.vaccine_administrator.fragments.UpdateAdministrator
import com.google.android.material.navigation.NavigationView

class TablesActivity : AppCompatActivity(), TablesCommunicator {

    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tables)

        //setting the current fragment
        val tablesFragment = TablesFragment()
        setCurrentFragment(tablesFragment)

        //drawer view for the various menus
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout_TablesActivity)
        val navView = findViewById<NavigationView>(R.id.navView_TablesActivity)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {

            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        when (item.itemId) {
            R.id.miHelp -> Toast.makeText(this, "Help and Feedback", Toast.LENGTH_SHORT).show()
            R.id.miSettings -> Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
            R.id.miPrivacy -> Toast.makeText(this, "Privacy", Toast.LENGTH_SHORT).show()
            R.id.miShare -> Toast.makeText(this, "Share with colleagues", Toast.LENGTH_SHORT).show()
        }
        return true
    }

    //App bar menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.app_bar_menu, menu)
        return true
    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flTablesActivity, fragment)
            commit()
        }
    }

    override fun passCustomer(
        Id: String,
        Name: String,
        Contact: String,
        Location: String,
        ShortNotes: String
    ) {
        val bundle = Bundle()
        val updateCustomer = UpdateCustomer()

        bundle.putString("name", Name)
        bundle.putString("contact", Contact)
        bundle.putString("location", Location)
        bundle.putString("shortNotes", ShortNotes)
        bundle.putString("id", Id)

        updateCustomer.arguments = bundle

        supportFragmentManager.beginTransaction().replace(
            R.id.flTablesActivity,
            updateCustomer
        ).commit()
    }

    override fun passFeedSource(
        Id: String,
        Name: String,
        Contact: String,
        Location: String,
        ShortNotes: String
    ) {
        val bundle = Bundle()
        val updateFeedSource = UpdateFeedSource()

        bundle.putString("name", Name)
        bundle.putString("contact", Contact)
        bundle.putString("location", Location)
        bundle.putString("shortNotes", ShortNotes)
        bundle.putString("id", Id)


        updateFeedSource.arguments = bundle


        supportFragmentManager.beginTransaction().replace(
            R.id.flTablesActivity,
            updateFeedSource
        ).commit()
    }

    override fun passFlockSource(
        Id: String,
        Name: String,
        Contact: String,
        Location: String,
        ShortNotes: String
    ) {
        val bundle = Bundle()
        val updateFlockSource = UpdateFlockSource()

        bundle.putString("name", Name)
        bundle.putString("contact", Contact)
        bundle.putString("location", Location)
        bundle.putString("shortNotes", ShortNotes)
        bundle.putString("id", Id)


        updateFlockSource.arguments = bundle


        supportFragmentManager.beginTransaction().replace(
            R.id.flTablesActivity,
            updateFlockSource
        ).commit()
    }

    override fun passLenderInvestor(
        Id: String,
        Name: String,
        Contact: String,
        Location: String,
        ShortNotes: String
    ) {
        val bundle = Bundle()
        val updateLenderInvestor = UpdateLenderInvestor()


        bundle.putString("name", Name)
        bundle.putString("contact", Contact)
        bundle.putString("location", Location)
        bundle.putString("shortNotes", ShortNotes)
        bundle.putString("id", Id)


        updateLenderInvestor.arguments = bundle


        supportFragmentManager.beginTransaction().replace(
            R.id.flTablesActivity,
            updateLenderInvestor
        ).commit()
    }

    override fun passEggType(Id: String, Name: String, Description: String) {
        val bundle = Bundle()
        val updateEggType = UpdateEggType()

        bundle.putString("eggTypeName", Name)
        bundle.putString("id", Id)
        bundle.putString("description", Description)

        updateEggType.arguments = bundle


        supportFragmentManager.beginTransaction().replace(
            R.id.flTablesActivity,
            updateEggType
        ).commit()
    }

    override fun passVaccineAdministrator(
        Id: String,
        Name: String,
        Contact: String,
        Location: String,
        ShortNotes: String
    ) {
        val bundle = Bundle()
        val updateAdministrator = UpdateAdministrator()

        bundle.putString("name", Name)
        bundle.putString("contact", Contact)
        bundle.putString("location", Location)
        bundle.putString("shortNotes", ShortNotes)
        bundle.putString("id", Id)


        updateAdministrator.arguments = bundle


        supportFragmentManager.beginTransaction().replace(
            R.id.flTablesActivity,
            updateAdministrator
        ).commit()
    }

    override fun passFeed(
        Id: String,
        Name: String,
        Type: String,
        Brand: String,
        Source: String,
        ShortNotes: String
    ) {
        val bundle = Bundle()
        val updateFeed = UpdateFeed()

        bundle.putString("name", Name)
        bundle.putString("type", Type)
        bundle.putString("brand", Brand)
        bundle.putString("source", Source)
        bundle.putString("shortNotes", ShortNotes)
        bundle.putString("id", Id)

        updateFeed.arguments = bundle

        supportFragmentManager.beginTransaction().replace(
            R.id.flTablesActivity,
            updateFeed
        ).commit()
    }
}