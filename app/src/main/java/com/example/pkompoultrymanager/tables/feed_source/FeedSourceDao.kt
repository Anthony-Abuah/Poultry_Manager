package com.example.pkompoultrymanager.tables.feed_source

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface FeedSourceDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addFeedSource(feedSource: FeedSource)

    @Query("SELECT * FROM FeedSource ORDER BY FeedSourceId ASC")
    fun displayFeedSource(): LiveData<List<FeedSource>>

    @Query("UPDATE FeedSource SET FeedSourceName=:feedSourceName, FeedSourceContact=:feedSourceContact, FeedSourceLocation=:feedSourceLocation, shortNotes=:shortNotes WHERE FeedSourceId LIKE :feedSourceNumber")
    suspend fun updateFeedSourceInfo(feedSourceNumber: Int, feedSourceName: String, feedSourceContact: String, feedSourceLocation: String, shortNotes:String)

    @Query("DELETE FROM FeedSource WHERE FeedSourceId =:feedSourceId")
    fun deleteFeedSource(feedSourceId: Int)

    @Delete
    suspend fun delete(feedSource: FeedSource)

    @Query("DELETE FROM FeedSource")
    fun deleteAll()



}