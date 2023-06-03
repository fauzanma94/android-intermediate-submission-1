package com.example.aplikasistoryapp.data.local.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StoryUserDao {
    @Query("SELECT * FROM story")
    fun getAllStories(): LiveData<List<StoryUser>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertStories(stories: List<StoryUser>)

    @Query("DELETE FROM story")
    fun delete()
}