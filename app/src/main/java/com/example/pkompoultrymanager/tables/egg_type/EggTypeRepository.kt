package com.example.pkompoultrymanager.tables.egg_type

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import java.sql.Date
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class EggTypeRepository(private val eggTypeDao: EggTypeDao) {

    val displayAllEggType: LiveData<List<EggType>> = eggTypeDao.displayEggType()


    suspend fun addEggType(eggType: EggType){
        eggTypeDao.addEggType(eggType)
    }

    suspend fun updateEggType(eggTypeNumber: Int, eggTypeName: String, description:String){
        eggTypeDao.updateEggType(eggTypeNumber, eggTypeName, description)
    }

     fun deleteAll(){
        eggTypeDao.deleteAll()
    }
    suspend fun delete(eggType: EggType){
        eggTypeDao.delete(eggType)
    }
    fun deleteEggType(eggTypeId: Int){
        eggTypeDao.deleteEggType(eggTypeId)
    }


}