package com.example.pkompoultrymanager.health.vaccination

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import java.sql.Date
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class VaccinationRepository(private val vaccinationDao: VaccinationDao) {

    val displayAllVaccination: LiveData<List<Vaccination>> =
        vaccinationDao.displayAllVaccination()

    val costOfAllVaccinations: LiveData<Double> =
        vaccinationDao.costOfAllVaccinations()

    val numberOfAllVaccinatedBirds: LiveData<Int> =
        vaccinationDao.numberOfAllVaccinatedBirds()

    fun costOfVaccinationPerDuration(firstDate: Date, lastDate: Date): LiveData<Double> =
        vaccinationDao.costOfVaccinationPerDuration(firstDate, lastDate)

    fun numberOfVaccinatedBirdsPerDuration(firstDate: Date, lastDate: Date): LiveData<Int> =
        vaccinationDao.numberOfVaccinatedBirdsPerDuration(firstDate, lastDate)


    suspend fun addVaccination(vaccination: Vaccination) {
        vaccinationDao.addVaccination(vaccination)
    }

    suspend fun updateVaccination(
        vaccinationID: Int,
        vaccinationName: String,
        vaccinationDate: Date,
        vaccinatedDisease: String,
        vaccinationCost: Double,
        vaccinatedFlock: String,
        numberOfMedicatedBirds: Int,
        vaccinationAdministrator: String,
        shortNotes: String
    ) {
        vaccinationDao.updateVaccination(
            vaccinationID,
            vaccinationName,
            vaccinationDate,
            vaccinatedDisease,
            vaccinationCost,
            vaccinatedFlock,
            numberOfMedicatedBirds,
            vaccinationAdministrator,
            shortNotes
        )
    }

    fun deleteAll() {
        vaccinationDao.deleteAll()
    }

    suspend fun delete(vaccination: Vaccination) {
        vaccinationDao.delete(vaccination)
    }

    fun deleteVaccination(vaccinationId: Int) {
        vaccinationDao.deleteVaccination(vaccinationId)
    }

        fun displayMedicatedFlock(vaccinatedFlock: String, firstDate: Date, lastDate: Date) {
            vaccinationDao.displayFlockMedicated(vaccinatedFlock, firstDate, lastDate)
        }

        fun displayVaccinationApplied(vaccinationName: String, firstDate: Date, lastDate: Date) {
            vaccinationDao.displayVaccinationApplied(vaccinationName, firstDate, lastDate)
        }

        fun numberOfVaccinationByCost(vaccinationCost: Double, firstDate: Date, lastDate: Date) {
            vaccinationDao.numberOfVaccinationByCost(vaccinationCost, firstDate, lastDate)
        }

        fun displayVaccinationInfoByCost(vaccinationCost: Double, firstDate: Date, lastDate: Date) {
            vaccinationDao.displayVaccinationInfoByCost(vaccinationCost, firstDate, lastDate)
        }

}