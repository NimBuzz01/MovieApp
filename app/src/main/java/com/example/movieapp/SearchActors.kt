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

    // initialize view variables
    private lateinit var actorBtn: Button
    private lateinit var getActor: EditText
    private lateinit var actorTv: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_actors)

        // assign views to variables
        actorBtn = findViewById(R.id.actorBtn)
        getActor = findViewById(R.id.getActor)
        actorTv = findViewById(R.id.actorTv)

        // initialize database and dao
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
                    // get all movies from db and looping
                    val movies: List<Movies> = moviesDao.getAll()
                    for (m in movies) {
                        // compare input to value in db, ignoring case letters
                        if (m.actors.toString().contains(actorName, ignoreCase = true)) {
                            actorTv.append("\n ${m.title} , ${m.year}")
                        }
                    }
                }
            }
        }

    }
}