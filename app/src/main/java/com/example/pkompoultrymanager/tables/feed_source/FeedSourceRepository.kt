package com.example.pkompoultrymanager.tables.feed_source

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import java.sql.Date
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class FeedSourceRepository(private val feedSourceDao: FeedSourceDao) {

    val displayAllFeedSources: LiveData<List<FeedSource>> = feedSourceDao.displayFeedSource()


    suspend fun addFeedSource(feedSource: FeedSource){
        feedSourceDao.addFeedSource(feedSource)
    }

    suspend fun updateFeedSourceInfo(feedSourceId: Int, feedSourceName: String, feedSourceContact: String, feedSourceLocation: String, shortNotes: String){
        feedSourceDao.updateFeedSourceInfo(feedSourceId, feedSourceName, feedSourceContact, feedSourceLocation, shortNotes)
    }

     fun deleteAll(){
        feedSourceDao.deleteAll()
    }
    suspend fun delete(feedSource: FeedSource){
        feedSourceDao.delete(feedSource)
    }
    fun deleteFeedSource(feedSourceId: Int){
        feedSourceDao.deleteFeedSource(feedSourceId)
    }





}