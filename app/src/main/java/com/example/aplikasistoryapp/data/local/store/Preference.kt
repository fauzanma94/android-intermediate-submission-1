package com.example.aplikasistoryapp.data.local.store

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.aplikasistoryapp.data.entity.UsersEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class Preference private constructor(private val dataStore: DataStore<Preferences>){
    companion object{
        @Volatile
        private var INSTANCE: Preference? = null
        private val USER_ID_KEY = stringPreferencesKey("userId")
        private val NAME_KEY = stringPreferencesKey("name")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val LOGIN_STATE_KEY = booleanPreferencesKey("login_state")

        fun getInstance(dataStore: DataStore<Preferences>): Preference{
            return INSTANCE ?: synchronized(this){
                val instance = Preference(dataStore)
                INSTANCE = instance
                instance
            }
        }

    }

    suspend fun storeUser(user: UsersEntity){
        dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = user.userId
            preferences[NAME_KEY] = user.name
            preferences[TOKEN_KEY] = user.token
            preferences[LOGIN_STATE_KEY] = user._isLogin

        }
    }

    fun getUser(): Flow<UsersEntity> {
        return dataStore.data.map { preferences ->
            UsersEntity(
                preferences[USER_ID_KEY] ?: "",
                preferences[NAME_KEY] ?: "",
                preferences[TOKEN_KEY] ?: "",
                preferences[LOGIN_STATE_KEY] ?: false
            )

        }
    }

    suspend fun userLogout(){
        dataStore.edit { preferences ->
            preferences[LOGIN_STATE_KEY] = false
            preferences.remove(LOGIN_STATE_KEY)
//            preferences.remove(TOKEN_KEY)
        }
    }
}