package com.example.pkompoultrymanager.tables.feed

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface FeedDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addFeed(feed: Feed)

    @Query("SELECT * FROM Feed ORDER BY FeedId ASC")
    fun displayFeed(): LiveData<List<Feed>>

    @Query("UPDATE Feed SET FeedName=:feedName, FeedType=:feedType, FeedBrand=:feedBrand, FeedSource=:feedSource, ShortNotes=:shortNotes WHERE FeedId LIKE :feedNumber")
    suspend fun updateFeed(feedNumber: Int, feedName: String, feedType: String, feedBrand: String, feedSource: String, shortNotes:String)

    @Query("DELETE FROM Feed WHERE FeedId =:feedId")
    fun deleteFeed(feedId: Int)

    @Delete
    suspend fun delete(feed: Feed)

    @Query("DELETE FROM Feed")
    fun deleteAll()



}