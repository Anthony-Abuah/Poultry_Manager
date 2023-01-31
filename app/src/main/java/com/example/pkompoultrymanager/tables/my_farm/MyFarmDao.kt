package com.example.pkompoultrymanager.tables.my_farm

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface MyFarmDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addMyFarm(myFarm: MyFarm)

    @Query("SELECT * FROM MyFarm ORDER BY FarmId ASC")
    fun displayMyFarm(): LiveData<List<MyFarm>>

    @Query("UPDATE MyFarm SET FarmName=:myFarmName, FarmContact=:myFarmContact, FarmLocation=:myFarmLocation, Currency=:myFarmCurrency, FarmOwner=:myFarmOwner, MeasuringUnit=:myFarmMeasuringUnit, NumberOfEggsPerCrate=:myFarmNumberOfEggsPerCrate, NumberOfEmployees=:myFarmNumberOfEmployees, ShortNotes=:shortNotes WHERE FarmId LIKE :myFarmNumber")
    suspend fun updateMyFarmInfo(myFarmName: String, myFarmContact: String, myFarmLocation: String, myFarmCurrency: String, myFarmOwner: String, myFarmMeasuringUnit: String, myFarmNumberOfEggsPerCrate: Int, myFarmNumberOfEmployees: Int, shortNotes: String, myFarmNumber: Int)

    @Query("DELETE FROM MyFarm WHERE FarmId =:myFarmId")
    fun deleteMyFarm(myFarmId: Int)

    @Delete
    suspend fun delete(myFarm: MyFarm)

    @Query("DELETE FROM MyFarm")
    fun deleteAll()



}