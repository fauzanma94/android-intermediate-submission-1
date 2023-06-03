package com.example.aplikasistoryapp.data.local.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(tableName = "story")
@Parcelize
data class StoryUser(

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = false)
    val id: String,

    @ColumnInfo(name = "name")
    @field:SerializedName("name")
    val name: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "photoUrl")
    val photoUrl: String,

    @ColumnInfo(name = "createdAt")
    val createdAt: String,

    @SerializedName("lat")
    val latitude: Float?,

    @SerializedName("lon")
    val longitude: Float?

):Parcelable