package com.example.pkompoultrymanager.reports

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.pkompoultrymanager.R
import com.google.android.material.navigation.NavigationView


class ReportActivity : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)


        //setting the current fragment
        val reportFragment = ReportFragment()
        setCurrentFragment(reportFragment)


        //drawer view for the various menus
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout_ReportActivity)
        val navView = findViewById<NavigationView>(R.id.navView_ReportActivity)
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
            replace(R.id.flReportActivity, fragment)
            commit()
        }
    }
}
