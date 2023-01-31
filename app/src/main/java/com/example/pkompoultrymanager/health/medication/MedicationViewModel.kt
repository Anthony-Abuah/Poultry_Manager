package com.example.pkompoultrymanager.health.medication

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
import kotlin.math.absoluteValue

class MedicationViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MedicationRepository


    val displayAllMedications: LiveData<List<Medication>>
    val costOfAllMedication: LiveData<Double>

    private val _costOfMedication: MutableLiveData<Double?> = MutableLiveData(0.0)
    val costOfMedication: LiveData<Double?> = _costOfMedication

    init {
        val medicationDao = AppDatabase.getDatabase(application).medicationDao()
        repository = MedicationRepository(medicationDao)
        displayAllMedications = repository.displayAllMedication
        costOfAllMedication = repository.costOfAllMedication
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }
    fun delete(medication: Medication) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(medication)
        }
    }

    fun addMedication(medication: Medication) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addMedication(medication)
        }
    }
    fun costOfMedicationPerDuration(firstDate: Date, lastDate: Date): LiveData<Double> {
        viewModelScope.launch(Dispatchers.IO) {
            repository.costOfMedicationPerDuration(firstDate, lastDate)
        }
        return repository.costOfMedicationPerDuration(firstDate, lastDate)
    }

    fun updateMedication(
        medicationID: Int, medicationName: String, medicationDate: Date, medicatedDisease: String, medicationCost:Double, medicatedFlock: String, numberOfMedicatedBirds:Int, medicationAdministrator:String, shortNotes:String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateMedication(
                medicationID, medicationName, medicationDate, medicatedDisease, medicationCost, medicatedFlock, numberOfMedicatedBirds, medicationAdministrator, shortNotes
            )
        }
    }
/*
    fun deleteMedication(medicationId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.de(medicationId)

        }
    }*/

}
