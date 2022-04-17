package com.example.movieapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.room.Room
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ViewDB : AppCompatActivity() {

    private lateinit var table: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_db)

        table = findViewById(R.id.table)

        val db = Room.databaseBuilder(
            this, MoviesDatabase::class.java,
            "myDatabase"
        ).build()
        val moviesDao = db.moviesDao()

        runBlocking {
            launch {
                val movies: List<Movies> = moviesDao.getAll()
                for (m in movies) {
                    table.append("\n ${m.title} , ${m.year}")
                }
            }
        }
    }
}