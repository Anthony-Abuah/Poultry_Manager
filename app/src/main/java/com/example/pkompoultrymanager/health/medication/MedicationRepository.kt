package com.example.pkompoultrymanager.health.medication

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import java.sql.Date
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class MedicationRepository(private val medicationDao: MedicationDao) {

    /*val number:Int = 100

    @RequiresApi(Build.VERSION_CODES.O)
    val firstDate:java.sql.Date = Date.valueOf(LocalDate.now().toString())
    @RequiresApi(Build.VERSION_CODES.O)
    val lastDate:java.sql.Date = Date.valueOf(LocalDate.now().toString())


    @RequiresApi(Build.VERSION_CODES.O)
    val displayMedication: LiveData<List<Medication>> = medicationDao.displayAllMedication(number, firstDate, lastDate )
*/
    val displayAllMedication: LiveData<List<Medication>> = medicationDao.displayAllMedication()

    val costOfAllMedication: LiveData<Double> = medicationDao.costOfAllMedication()

    suspend fun addMedication(medication: Medication){
        medicationDao.addMedication(medication)
    }

    suspend fun updateMedication(medicationID: Int, medicationName: String, medicationDate: Date, medicatedDisease: String, medicationCost:Double, medicatedFlock: String, numberOfMedicatedBirds:Int, medicationAdministrator:String, shortNotes:String){
        medicationDao.updateMedication(medicationID, medicationName, medicationDate, medicatedDisease, medicationCost, medicatedFlock, numberOfMedicatedBirds, medicationAdministrator, shortNotes)
    }

     fun deleteAll(){
        medicationDao.deleteAll()
    }

    suspend fun delete(medication: Medication){
        medicationDao.delete(medication)
    }


    fun displayMedicatedFlock(medicatedFlock: String, firstDate: Date, lastDate: Date){
        medicationDao.displayFlockMedicated( medicatedFlock, firstDate, lastDate)
    }

    fun displayMedicationApplied(medicationName: String, firstDate: Date, lastDate: Date){
        medicationDao.displayMedicationApplied( medicationName, firstDate, lastDate)
    }

    fun costOfMedicationPerDuration (firstDate: Date, lastDate: Date): LiveData<Double> =
        medicationDao.costOfMedicationPerDuration(firstDate, lastDate)


    fun numberOfMedicationByCost(medicationCost: Double, firstDate: Date, lastDate: Date){
        medicationDao.numberOfMedicationByCost(medicationCost, firstDate, lastDate)
    }

    fun displayMedicationInfoByCost(medicationCost: Double, firstDate: Date, lastDate: Date){
        medicationDao.displayMedicationInfoByCost(medicationCost, firstDate, lastDate)
    }


}