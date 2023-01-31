package com.example.pkompoultrymanager.tables.lender_investor

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import java.sql.Date
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class LenderInvestorRepository(private val lenderInvestorDao: LenderInvestorDao) {

    val displayAllLenderOrInvestors: LiveData<List<LenderInvestor>> = lenderInvestorDao.displayLenderInvestor()


    suspend fun addLenderInvestor(lenderInvestor: LenderInvestor){
        lenderInvestorDao.addLenderInvestor(lenderInvestor)
    }

    suspend fun updateLenderInvestor(lenderInvestorId:Int, lenderInvestorName: String, lenderInvestorContact: String, lenderInvestorLocation: String, shortNotes: String){
        lenderInvestorDao.updateLenderInvestor(lenderInvestorId, lenderInvestorName, lenderInvestorContact, lenderInvestorLocation, shortNotes)
    }

     fun deleteAll(){
        lenderInvestorDao.deleteAll()
    }
    suspend fun delete(lenderInvestor: LenderInvestor){
        lenderInvestorDao.delete(lenderInvestor)
    }
    fun deleteLenderInvestor(lenderInvestorId: Int){
        lenderInvestorDao.deleteLenderInvestor(lenderInvestorId)
    }





}