package com.example.movieapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = Room.databaseBuilder(this, MoviesDatabase::class.java,
            "myDatabase").build()
        val moviesDao = db.moviesDao()
    }
}