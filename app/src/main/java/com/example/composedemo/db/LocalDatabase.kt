package com.example.composedemo.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.composedemo.model.LocalData

@Database(entities = [(LocalData::class)], version = 1, exportSchema = false)
abstract class LocalDatabase :RoomDatabase() {
    abstract fun gitDao(): GitDao


}