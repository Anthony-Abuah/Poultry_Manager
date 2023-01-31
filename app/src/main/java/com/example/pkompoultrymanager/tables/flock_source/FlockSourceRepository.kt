package com.example.pkompoultrymanager.tables.flock_source

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import java.sql.Date
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class FlockSourceRepository(private val flockSourceDao: FlockSourceDao) {

    val displayAllFlockSources: LiveData<List<FlockSource>> = flockSourceDao.displayFlockSource()


    suspend fun addFlockSource(flockSource: FlockSource){
        flockSourceDao.addFlockSource(flockSource)
    }

    suspend fun updateFlockSource(flockSourceId: Int, flockSourceName: String, flockSourceContact: String, flockSourceLocation: String, shortNotes: String,){
        flockSourceDao.updateFlockSource(flockSourceId, flockSourceName, flockSourceContact, flockSourceLocation, shortNotes)
    }

     fun deleteAll(){
        flockSourceDao.deleteAll()
    }
    suspend fun delete(flockSource: FlockSource){
        flockSourceDao.delete(flockSource)
    }
    fun deleteFlockSource(flockSourceId: Int){
        flockSourceDao.deleteFlockSource(flockSourceId)
    }





}