package com.example.pkompoultrymanager.tables.flock_source

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.pkompoultrymanager.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FlockSourceViewModel(application: Application) : AndroidViewModel(application) {


    val displayAllFlockSources: LiveData<List<FlockSource>>
    private val repository: FlockSourceRepository

    init {
        val flockSourceDao = AppDatabase.getDatabase(application).flockSourceDao()
        repository = FlockSourceRepository(flockSourceDao)
        displayAllFlockSources = repository.displayAllFlockSources
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }

    fun addFlockSource(flockSource: FlockSource) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addFlockSource(flockSource)
        }
    }

    fun delete(flockSource: FlockSource) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(flockSource)
        }
    }

    fun updateFlockSourceInfo(
        flockSourceId: Int,
        flockSourceName: String,
        flockSourceContact: String,
        flockSourceLocation: String,
        shortNotes: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateFlockSource(
                flockSourceId,
                flockSourceName,
                flockSourceContact,
                flockSourceLocation,
                shortNotes
            )
        }
    }

    fun deleteFlockSource(flockSourceId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteFlockSource(flockSourceId)

        }
    }

}
