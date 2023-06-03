package com.example.aplikasistoryapp.data.local.repository

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.aplikasistoryapp.data.api.ApiService
import com.example.aplikasistoryapp.data.local.database.StoryUser
import com.example.aplikasistoryapp.data.local.database.StoryUserDao
import com.example.aplikasistoryapp.data.response.story.AllStoriesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllStoryRepository (
    private val storyUserDao: StoryUserDao,
    private val appExecutors: Executors,
    private val apiService: ApiService
){
    private val result = MediatorLiveData<Result<List<StoryUser>>>()

    fun getAllStory(token: String): LiveData<Result<List<StoryUser>>>{

        val client = apiService.getAllStories(token, null ,null)
        client.enqueue(object : Callback<AllStoriesResponse>{
            override fun onResponse(
                call: Call<AllStoriesResponse>,
                response: Response<AllStoriesResponse>
            ) {
                if(response.isSuccessful){
                    val allStory = response.body()?.listStory
                    val storyList = ArrayList<StoryUser>()
                    appExecutors.diskIO.execute{
                        allStory?.forEach { allStory ->
                            val story = StoryUser(
                                allStory.id,
                                allStory.name,
                                allStory.description,
                                allStory.photoUrl,
                                allStory.createdAt,
                                allStory.lat,
                                allStory.lon
                            )
                            storyList.add(story)
                        }
                        storyUserDao.delete()
                        storyUserDao.insertStories(storyList)
                    }
                }
                Log.e(TAG,response.message())
            }

            override fun onFailure(call: Call<AllStoriesResponse>, t: Throwable) {
                result.value = Result.Error(t.message.toString())
                t.message?.let { Log.e(TAG,it) }
            }
        })

        val data = storyUserDao.getAllStories()
        result.addSource(data){storyData: List<StoryUser> ->
            result.value = Result.Success(storyData)
        }
        return result

    }

    companion object {
        @Volatile
        private var instance: AllStoryRepository? = null
        fun getInstance(
            apiService: ApiService,
            storyDao: StoryUserDao,
            appExecutors: Executors
        ) : AllStoryRepository =
            instance ?: synchronized(this) {
                instance ?: AllStoryRepository(storyDao, appExecutors, apiService)
            }.also { instance = it }
    }
}