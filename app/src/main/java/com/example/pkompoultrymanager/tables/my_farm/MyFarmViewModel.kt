package com.example.pkompoultrymanager.tables.my_farm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.pkompoultrymanager.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyFarmViewModel(application: Application) : AndroidViewModel(application) {


    val displayAllMyFarms: LiveData<List<MyFarm>>
    private val repository: MyFarmRepository

    init {
        val myFarmDao = AppDatabase.getDatabase(application).myFarmDao()
        repository = MyFarmRepository(myFarmDao)
        displayAllMyFarms = repository.displayAllOfMyFarms
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }

    fun addMyFarm(myFarm: MyFarm) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addMyFarm(myFarm)
        }
    }

    fun delete(myFarm: MyFarm) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(myFarm)
        }
    }

    fun updateMyFarmInfo(
        myFarmName: String,
        myFarmContact: String,
        myFarmLocation: String,
        myFarmCurrency: String,
        myFarmOwner: String,
        myFarmMeasuringUnit: String,
        myFarmNumberOfEggsPerCrate: Int,
        myFarmNumberOfEmployees: Int,
        shortNotes: String,
        myFarmNumber: Int
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateMyFarmInfo(
                myFarmName,
                myFarmContact,
                myFarmLocation,
                myFarmCurrency,
                myFarmOwner,
                myFarmMeasuringUnit,
                myFarmNumberOfEggsPerCrate,
                myFarmNumberOfEmployees,
                shortNotes,
                myFarmNumber
            )
        }
    }

    fun deleteMyFarm(myFarmId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteMyFarm(myFarmId)

        }
    }

}
