package com.example.pkompoultrymanager.health

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.pkompoultrymanager.R
import com.example.pkompoultrymanager.health.medication.fragments.UpdateMedicationForm
import com.example.pkompoultrymanager.health.vaccination.fragments.UpdateVaccinationForm
import com.example.pkompoultrymanager.inventory.InventoryFragment
import com.example.pkompoultrymanager.inventory.eggs.addition.fragments.UpdateEggsAdditionForm
import com.google.android.material.navigation.NavigationView

class HealthActivity : AppCompatActivity(), HealthCommunicator {

    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_health)


        //setting the current fragment
        val healthFragment = HealthFragment()
        setCurrentFragment(healthFragment)

        //drawer view for the various menus
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout_HealthActivity)
        val navView = findViewById<NavigationView>(R.id.navView_HealthActivity)
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
            replace(R.id.flHealthActivity, fragment)
            commit()
        }
    }

    override fun passMedication(
        MedicationId: String,
        MedicationName: String,
        MedicationDate: String,
        MedicationAdministrator: String,
        MedicationCost: String,
        MedicatedFlock: String,
        NumberOfMedicatedBirds: String,
        MedicatedDisease: String,
        ShortNotes: String
    ) {
        val bundle = Bundle()
        val updateMedicationForm = UpdateMedicationForm()

        bundle.putString("medicationId", MedicationId)
        bundle.putString("medicationName", MedicationName)
        bundle.putString("medicationDate", MedicationDate)
        bundle.putString("medicationAdministrator", MedicationAdministrator)
        bundle.putString("medicationCost", MedicationCost)
        bundle.putString("medicatedFlock", MedicatedFlock)
        bundle.putString("numberOfMedicatedBirds", NumberOfMedicatedBirds)
        bundle.putString("medicatedDisease", MedicatedDisease)
        bundle.putString("shortNotes", ShortNotes)

        updateMedicationForm.arguments = bundle

        supportFragmentManager.beginTransaction().replace(
            R.id.flHealthActivity,
            updateMedicationForm
        ).addToBackStack(null).commit()
    }

    override fun passVaccination(
        VaccinationId: String,
        VaccinationName: String,
        VaccinationDate: String,
        VaccinationAdministrator: String,
        VaccinationCost: String,
        VaccinatedFlock: String,
        NumberOfVaccinatedBirds: String,
        VaccinatedDisease: String,
        ShortNotes: String
    ) {
        val bundle = Bundle()
        val updateVaccinationForm = UpdateVaccinationForm()

        bundle.putString("vaccinationId", VaccinationId)
        bundle.putString("vaccinationName", VaccinationName)
        bundle.putString("vaccinationDate", VaccinationDate)
        bundle.putString("vaccinationAdministrator", VaccinationAdministrator)
        bundle.putString("vaccinationCost", VaccinationCost)
        bundle.putString("vaccinatedFlock", VaccinatedFlock)
        bundle.putString("numberOfVaccinatedBirds", NumberOfVaccinatedBirds)
        bundle.putString("vaccinatedDisease", VaccinatedDisease)
        bundle.putString("shortNotes", ShortNotes)

        updateVaccinationForm.arguments = bundle

        supportFragmentManager.beginTransaction().replace(
            R.id.flHealthActivity,
            updateVaccinationForm
        ).addToBackStack(null).commit()    }
}