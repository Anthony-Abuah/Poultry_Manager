package com.example.pkompoultrymanager.tables.flock_source

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface FlockSourceDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addFlockSource(flockSource: FlockSource)

    @Query("SELECT * FROM FlockSource ORDER BY FlockSourceId ASC")
    fun displayFlockSource(): LiveData<List<FlockSource>>

    @Query("UPDATE FlockSource SET FlockSourceName=:flockSourceName, FlockSourceContact=:flockSourceContact, shortNotes=:shortNotes, FlockSourceLocation=:flockSourceLocation WHERE FlockSourceId LIKE :flockSourceNumber")
    suspend fun updateFlockSource(flockSourceNumber: Int, flockSourceName: String, flockSourceContact: String, flockSourceLocation: String, shortNotes:String)

    @Query("DELETE FROM FlockSource WHERE FlockSourceId =:flockSourceId")
    fun deleteFlockSource(flockSourceId: Int)

    @Delete
    suspend fun delete(flockSource: FlockSource)

    @Query("DELETE FROM FlockSource")
    fun deleteAll()



}