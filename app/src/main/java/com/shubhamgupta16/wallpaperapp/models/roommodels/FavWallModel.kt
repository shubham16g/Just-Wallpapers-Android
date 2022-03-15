package com.shubhamgupta16.wallpaperapp.models.roommodels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
class FavWallModel(
    @PrimaryKey(autoGenerate = true)
    val wallId: Int,
)