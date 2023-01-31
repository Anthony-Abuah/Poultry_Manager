package com.example.pkompoultrymanager.tables.my_farm

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import java.sql.Date
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class MyFarmRepository(private val myFarmDao: MyFarmDao) {

    val displayAllOfMyFarms: LiveData<List<MyFarm>> = myFarmDao.displayMyFarm()


    suspend fun addMyFarm(myFarm: MyFarm){
        myFarmDao.addMyFarm(myFarm)
    }

    suspend fun updateMyFarmInfo(myFarmName: String, myFarmContact: String, myFarmLocation: String, myFarmCurrency: String, myFarmOwner: String, myFarmMeasuringUnit: String, myFarmNumberOfEggsPerCrate: Int, myFarmNumberOfEmployees: Int, shortNotes: String, myFarmNumber: Int){
        myFarmDao.updateMyFarmInfo(myFarmName, myFarmContact, myFarmLocation, myFarmCurrency, myFarmOwner, myFarmMeasuringUnit, myFarmNumberOfEggsPerCrate, myFarmNumberOfEmployees, shortNotes, myFarmNumber)
    }

     fun deleteAll(){
        myFarmDao.deleteAll()
    }
    suspend fun delete(myFarm: MyFarm){
        myFarmDao.delete(myFarm)
    }
    fun deleteMyFarm(myFarmId: Int){
        myFarmDao.deleteMyFarm(myFarmId)
    }





}