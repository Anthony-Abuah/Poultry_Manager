package com.example.pkompoultrymanager.tables.egg_type

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface EggTypeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addEggType(eggType: EggType)

    @Query("SELECT * FROM EggType ORDER BY EggTypeId ASC")
    fun displayEggType(): LiveData<List<EggType>>

    @Query("UPDATE EggType SET EggTypeName=:eggTypeName, EggTypeDescription=:description WHERE EggTypeId LIKE :eggTypeNumber")
    suspend fun updateEggType(eggTypeNumber: Int, eggTypeName: String,description:String)

    @Query("DELETE FROM EggType WHERE EggTypeId =:eggTypeId")
    fun deleteEggType(eggTypeId: Int)

    @Delete
    suspend fun delete(eggType: EggType)

    @Query("DELETE FROM EggType")
    fun deleteAll()



}