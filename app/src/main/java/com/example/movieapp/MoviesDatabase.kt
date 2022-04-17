package com.example.movieapp

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Movies::class], version = 1)
abstract class MoviesDatabase : RoomDatabase() {
    abstract fun moviesDao(): MoviesDao
}