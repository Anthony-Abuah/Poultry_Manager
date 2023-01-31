package com.example.pkompoultrymanager.tables.feed_source

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.pkompoultrymanager.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FeedSourceViewModel(application: Application) : AndroidViewModel(application) {


    val displayAllFeedSources: LiveData<List<FeedSource>>
    private val repository: FeedSourceRepository

    init {
        val feedSourceDao = AppDatabase.getDatabase(application).feedSourceDao()
        repository = FeedSourceRepository(feedSourceDao)
        displayAllFeedSources = repository.displayAllFeedSources
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }

    fun addFeedSource(feedSource: FeedSource) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addFeedSource(feedSource)
        }
    }

    fun delete(feedSource: FeedSource) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(feedSource)
        }
    }

    fun updateFeedSourceInfo(
        feedSourceId: Int,
        feedSourceName: String,
        feedSourceContact: String,
        feedSourceLocation: String,
        shortNotes: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateFeedSourceInfo(
                feedSourceId,
                feedSourceName,
                feedSourceContact,
                feedSourceLocation,
                shortNotes
            )
        }
    }

    fun deleteFeedSource(feedSourceId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteFeedSource(feedSourceId)

        }
    }

}
