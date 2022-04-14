package com.example.movieapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MoviesDao {
    @Query("Select * from Movies")
    suspend fun getAll(): List<Movies>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(vararg movie: Movies)
    @Insert
    suspend fun insertAll(vararg movies: Movies)
}
