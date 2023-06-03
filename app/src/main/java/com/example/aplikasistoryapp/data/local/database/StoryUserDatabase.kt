package com.example.aplikasistoryapp.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [StoryUser::class], version = 1)
abstract class StoryUserDatabase :RoomDatabase(){
    abstract fun storyUserDao(): StoryUserDao

    companion object{
        @Volatile
        private var INSTANCE: StoryUserDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): StoryUserDatabase{
            if(INSTANCE == null){
                synchronized(StoryUserDatabase::class.java){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                    StoryUserDatabase::class.java,"story")
                        .build()
                }
            }
            return INSTANCE as StoryUserDatabase
        }
    }
}