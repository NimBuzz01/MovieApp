package com.example.movieapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.room.Room
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SearchActors : AppCompatActivity() {

    private lateinit var actorBtn: Button
    private lateinit var getActor: EditText
    private lateinit var actorTv: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_actors)

        actorBtn = findViewById(R.id.actorBtn)
        getActor = findViewById(R.id.getActor)
        actorTv = findViewById(R.id.actorTv)

        val db = Room.databaseBuilder(
            this, MoviesDatabase::class.java,
            "myDatabase"
        ).build()
        val moviesDao = db.moviesDao()

        actorBtn.setOnClickListener {
            actorTv.text = ""
            val actorName = getActor.text.toString()

            runBlocking {
                launch {
                    val movies: List<Movies> = moviesDao.getAll()
                    for (m in movies) {
                        if(m.actors.toString().contains(actorName, ignoreCase = true)){
                            actorTv.append("\n ${m.title} , ${m.year}")
                        }
                    }
                }
            }
        }

    }
}