package com.example.pkompoultrymanager.tables.vaccine_administrator

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import com.example.pkompoultrymanager.tables.customer.VaccineAdministrator
import java.sql.Date
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class VaccineAdministratorRepository(private val vaccineAdministratorDao: VaccineAdministratorDao) {

    val displayAllAdministrators: LiveData<List<VaccineAdministrator>> = vaccineAdministratorDao.displayVaccineAdministrator()


    suspend fun addVaccineAdministrator(vaccineAdministrator: VaccineAdministrator){
        vaccineAdministratorDao.addVaccineAdministrator(vaccineAdministrator)
    }

    suspend fun updateVaccineAdministratorInfo(vaccineAdministratorId: Int, vaccineAdministratorName: String, vaccineAdministratorContact: String, vaccineAdministratorLocation: String, shortNotes: String){
        vaccineAdministratorDao.updateVaccineAdministrator(vaccineAdministratorId, vaccineAdministratorName, vaccineAdministratorContact, vaccineAdministratorLocation, shortNotes)
    }

     fun deleteAll(){
        vaccineAdministratorDao.deleteAll()
    }
    suspend fun delete(vaccineAdministrator: VaccineAdministrator){
        vaccineAdministratorDao.delete(vaccineAdministrator)
    }
    fun deleteVaccineAdministrator(vaccineAdministratorId: Int){
        vaccineAdministratorDao.deleteVaccineAdministrator(vaccineAdministratorId)
    }





}