package com.example.pkompoultrymanager.health.medication

import androidx.lifecycle.LiveData
import androidx.room.*
import java.sql.Date


@Dao
interface MedicationDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addMedication(medication: Medication)

    @Query("SELECT * FROM Medication ORDER BY medicationId DESC ")
    fun displayAllMedication(): LiveData<List<Medication>>

    @Query("SELECT * FROM Medication WHERE MedicationDate BETWEEN :firstDate AND :lastDate ORDER BY medicationId DESC LIMIT :number ")
    fun displayMedication(number: Int, firstDate: Date, lastDate: Date): LiveData<List<Medication>>

    @Query("UPDATE Medication SET medicationName=:medicationName, medicationDate=:medicationDate, medicatedDisease=:medicatedDisease, medicationCost=:medicationCost, medicatedFlock=:medicatedFlock, numberOfMedicatedBirds=:numberOfMedicatedBirds, medicationAdministrator=:medicationAdministrator, shortNotes=:shortNotes WHERE medicationId LIKE :medicationID")
    suspend fun updateMedication( medicationID: Int, medicationName: String, medicationDate: Date, medicatedDisease: String, medicationCost:Double, medicatedFlock: String, numberOfMedicatedBirds:Int, medicationAdministrator:String, shortNotes:String)

    @Query("DELETE FROM Medication WHERE medicationId LIKE :medicationId")
    suspend fun deleteFrom(medicationId: Int)

    @Query("DELETE FROM Medication")
    fun deleteAll()

    @Delete
    suspend fun delete(medication: Medication)


    @Query("SELECT * FROM Medication WHERE medicatedFlock LIKE :medicatedFlock AND medicationDate BETWEEN :firstDate AND :lastDate")
    fun displayFlockMedicated(medicatedFlock:String, firstDate: Date, lastDate: Date): LiveData<List<Medication>>


    @Query("SELECT * FROM Medication WHERE medicationName LIKE :medicationName AND medicationDate BETWEEN :firstDate AND :lastDate")
    fun displayMedicationApplied(medicationName: String, firstDate: Date, lastDate: Date): LiveData<List<Medication>>


    @Query("SELECT * FROM Medication WHERE medicationCost >= :medicationCost AND medicationDate BETWEEN :firstDate AND :lastDate")
    fun displayMedicationInfoByCost(medicationCost: Double, firstDate: Date, lastDate: Date): LiveData<List<Medication>>


    @Query("SELECT SUM(medicationCost) FROM Medication WHERE medicationDate BETWEEN :firstDate AND :lastDate")
    fun costOfMedicationPerDuration(firstDate: Date, lastDate: Date): LiveData<Double>

    @Query("SELECT SUM(medicationCost) FROM Medication")
    fun costOfAllMedication(): LiveData<Double>

    @Query("SELECT COUNT(medicationCost) FROM Medication WHERE medicationCost >= :medicationCost AND medicationDate BETWEEN :firstDate AND :lastDate")
    fun numberOfMedicationByCost(medicationCost: Double, firstDate: Date, lastDate: Date): LiveData<Int>

}