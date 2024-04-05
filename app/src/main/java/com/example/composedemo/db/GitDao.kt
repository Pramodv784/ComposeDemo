package com.example.composedemo.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.composedemo.model.LocalData


@Dao
interface GitDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
      suspend fun addRepo(localData: LocalData):Long

    @Query("SELECT * FROM localRepo")
     fun getAllRepo(): List<LocalData>
}