package com.example.pkompoultrymanager.tables.vaccine_administrator

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.pkompoultrymanager.database.AppDatabase
import com.example.pkompoultrymanager.tables.customer.VaccineAdministrator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VaccineAdministratorViewModel(application: Application) : AndroidViewModel(application) {


    val displayAllAdministrators: LiveData<List<VaccineAdministrator>>
    private val repository: VaccineAdministratorRepository

    init {
        val vaccineAdministratorDao = AppDatabase.getDatabase(application).vaccineAdministratorDao()
        repository = VaccineAdministratorRepository(vaccineAdministratorDao)
        displayAllAdministrators = repository.displayAllAdministrators
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }

    fun addVaccineAdministrator(vaccineAdministrator: VaccineAdministrator) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addVaccineAdministrator(vaccineAdministrator)
        }
    }

    fun delete(vaccineAdministrator: VaccineAdministrator) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(vaccineAdministrator)
        }
    }

    fun updateVaccineAdministratorInfo(
        vaccineAdministratorId: Int,
        vaccineAdministratorName: String,
        vaccineAdministratorContact: String,
        vaccineAdministratorLocation: String,
        shortNotes: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateVaccineAdministratorInfo(
                vaccineAdministratorId,
                vaccineAdministratorName,
                vaccineAdministratorContact,
                vaccineAdministratorLocation,
                shortNotes
            )
        }
    }

    fun deleteVaccineAdministrator(vaccineAdministratorId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteVaccineAdministrator(vaccineAdministratorId)

        }
    }

}
