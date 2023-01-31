package com.example.pkompoultrymanager.tables.feed

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.pkompoultrymanager.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FeedViewModel(application: Application) : AndroidViewModel(application) {


    val displayAllFeeds: LiveData<List<Feed>>
    private val repository: FeedRepository

    init {
        val feedDao = AppDatabase.getDatabase(application).feedDao()
        repository = FeedRepository(feedDao)
        displayAllFeeds = repository.displayAllFeed
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }

    fun addFeed(feed: Feed) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addFeed(feed)
        }
    }

    fun delete(feed: Feed) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(feed)
        }
    }

    fun updateFeed(
        feedNumber: Int, feedName: String, feedType: String, feedBrand: String, feedSource: String, shortNotes:String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateFeed(
                feedNumber, feedName, feedType, feedBrand, feedSource, shortNotes
            )
        }
    }

    fun deleteFeed(feedId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteFeed(feedId)

        }
    }

}
