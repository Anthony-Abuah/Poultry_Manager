package com.example.pkompoultrymanager.tables.lender_investor

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface LenderInvestorDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addLenderInvestor(lenderInvestor: LenderInvestor)

    @Query("SELECT * FROM LenderInvestor ORDER BY LenderInvestorId ASC")
    fun displayLenderInvestor(): LiveData<List<LenderInvestor>>

    @Query("UPDATE LenderInvestor SET LenderInvestorName=:lenderInvestorName, LenderInvestorContact=:lenderInvestorContact, LenderInvestorLocation=:lenderInvestorLocation, shortNotes=:shortNotes WHERE LenderInvestorId LIKE :lenderInvestorId")
    suspend fun updateLenderInvestor(lenderInvestorId: Int, lenderInvestorName: String, lenderInvestorContact: String, lenderInvestorLocation: String, shortNotes: String)

    @Query("DELETE FROM LenderInvestor WHERE LenderInvestorId =:lenderInvestorId")
    fun deleteLenderInvestor(lenderInvestorId: Int)

    @Delete
    suspend fun delete(lenderInvestor: LenderInvestor)

    @Query("DELETE FROM LenderInvestor")
    fun deleteAll()



}