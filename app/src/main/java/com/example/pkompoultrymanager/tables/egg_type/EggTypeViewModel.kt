package com.example.pkompoultrymanager.tables.egg_type

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.pkompoultrymanager.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EggTypeViewModel(application: Application) : AndroidViewModel(application) {


    val displayAllEggTypes: LiveData<List<EggType>>
    private val repository: EggTypeRepository

    init {
        val eggTypeDao = AppDatabase.getDatabase(application).eggTypeDao()
        repository = EggTypeRepository(eggTypeDao)
        displayAllEggTypes = repository.displayAllEggType
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }

    fun addEggType(eggType: EggType) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addEggType(eggType)
        }
    }

    fun delete(eggType: EggType) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(eggType)
        }
    }

    fun updateEggType(
        eggTypeNumber: Int, eggTypeName: String, description:String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateEggType(
                eggTypeNumber, eggTypeName,description
            )
        }
    }

    fun deleteEggType(eggTypeId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteEggType(eggTypeId)

        }
    }

}
