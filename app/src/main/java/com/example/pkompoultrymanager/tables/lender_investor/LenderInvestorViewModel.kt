package com.example.pkompoultrymanager.tables.lender_investor

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.pkompoultrymanager.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LenderInvestorViewModel(application: Application) : AndroidViewModel(application) {


    val displayAllLendersOrInvestors: LiveData<List<LenderInvestor>>
    private val repository: LenderInvestorRepository

    init {
        val lenderInvestorDao = AppDatabase.getDatabase(application).lenderInvestorDao()
        repository = LenderInvestorRepository(lenderInvestorDao)
        displayAllLendersOrInvestors = repository.displayAllLenderOrInvestors
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }

    fun addLenderInvestor(lenderInvestor: LenderInvestor) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addLenderInvestor(lenderInvestor)
        }
    }

    fun delete(lenderInvestor: LenderInvestor) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(lenderInvestor)
        }
    }

    fun updateLenderInvestorInfo(
        lenderInvestorId: Int,
        lenderInvestorName: String,
        lenderInvestorContact: String,
        lenderInvestorLocation: String,
        shortNotes: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateLenderInvestor(
                lenderInvestorId,
                lenderInvestorName,
                lenderInvestorContact,
                lenderInvestorLocation,
                shortNotes
            )
        }
    }

    fun deleteLenderInvestor(lenderInvestorId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteLenderInvestor(lenderInvestorId)

        }
    }

}
