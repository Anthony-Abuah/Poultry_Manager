package com.example.pkompoultrymanager.health.vaccination

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pkompoultrymanager.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Date

class VaccinationViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: VaccinationRepository

    val displayAllVaccinations: LiveData<List<Vaccination>>
    val costOfAllVaccinations: LiveData<Double>
    val numberOfAllVaccinatedBirds: LiveData<Int>


    init {
        val vaccinationDao = AppDatabase.getDatabase(application).vaccinationDao()
        repository = VaccinationRepository(vaccinationDao)
        displayAllVaccinations = repository.displayAllVaccination
        costOfAllVaccinations = repository.costOfAllVaccinations
        numberOfAllVaccinatedBirds = repository.numberOfAllVaccinatedBirds
    }

    fun delete(vaccination: Vaccination) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(vaccination)
        }
    }
    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }

    fun addVaccination(vaccination: Vaccination) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addVaccination(vaccination)
        }
    }

    fun numberOfVaccinatedBirdsPerDuration(firstDate: Date, lastDate: Date): LiveData<Int> {
        viewModelScope.launch(Dispatchers.IO) {
            repository.numberOfVaccinatedBirdsPerDuration(firstDate, lastDate)
        }
        return repository.numberOfVaccinatedBirdsPerDuration(firstDate, lastDate)
    }

    fun costOfVaccinationPerDuration(firstDate: Date, lastDate: Date): LiveData<Double> {
        viewModelScope.launch(Dispatchers.IO) {
            repository.costOfVaccinationPerDuration(firstDate, lastDate)
        }
        return repository.costOfVaccinationPerDuration(firstDate, lastDate)
    }

    fun updateVaccination(
        vaccinationID: Int, vaccinationName: String, vaccinationDate: Date, medicatedDisease: String, vaccinationCost:Double, medicatedFlock: String, numberOfMedicatedBirds:Int, vaccinationAdministrator:String, shortNotes:String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateVaccination(
                vaccinationID, vaccinationName, vaccinationDate, medicatedDisease, vaccinationCost, medicatedFlock, numberOfMedicatedBirds, vaccinationAdministrator, shortNotes
            )
        }
    }

    fun deleteVaccination(vaccinationId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteVaccination(vaccinationId)

        }
    }

}
