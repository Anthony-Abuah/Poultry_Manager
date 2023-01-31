package com.example.pkompoultrymanager.tables.feed

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import java.sql.Date
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class FeedRepository(private val feedDao: FeedDao) {

    val displayAllFeed: LiveData<List<Feed>> = feedDao.displayFeed()


    suspend fun addFeed(feed: Feed){
        feedDao.addFeed(feed)
    }

    suspend fun updateFeed(feedNumber: Int, feedName: String, feedType: String, feedBrand: String, feedSource: String, shortNotes:String){
        feedDao.updateFeed(feedNumber, feedName, feedType, feedBrand, feedSource, shortNotes)
    }

     fun deleteAll(){
        feedDao.deleteAll()
    }
    suspend fun delete(feed: Feed){
        feedDao.delete(feed)
    }
    fun deleteFeed(feedId: Int){
        feedDao.deleteFeed(feedId)
    }


}