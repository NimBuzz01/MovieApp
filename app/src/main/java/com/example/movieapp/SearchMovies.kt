package com.example.movieapp

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class SearchMovies : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences

    // initialize view variables
    private lateinit var movieBtn: Button
    private lateinit var movieToDB: Button
    private lateinit var getMovie: EditText
    private lateinit var movieTv: TextView

    // initialize db variables
    var id: Int = 0
    private lateinit var title: String
    private lateinit var year: String
    private lateinit var rated: String
    private lateinit var released: String
    private lateinit var runtime: String
    private lateinit var genre: String
    private lateinit var director: String
    private lateinit var writer: String
    private lateinit var actors: String
    private lateinit var plot: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_movies)

        //Loading from Shared Preferences
        prefs = getSharedPreferences("saveData", MODE_PRIVATE)
        id = prefs.getInt("id", 0)

        // collecting all the JSON string
        val stb = StringBuilder()

        // assign views to variables
        movieBtn = findViewById(R.id.movieBtn)
        movieToDB = findViewById(R.id.movieToDB)
        getMovie = findViewById(R.id.getMovie)
        movieTv = findViewById(R.id.movieTv)

        // initialize database and dao
        val db = Room.databaseBuilder(
            this, MoviesDatabase::class.java,
            "myDatabase"
        ).build()
        val moviesDao = db.moviesDao()

        movieBtn.setOnClickListener {
            movieTv.text = ""
            val movieName = getMovie.text.toString()
            // setting url to api and opening http connection
            val urlString = "https://www.omdbapi.com/?t=${movieName}&apikey=7086918a"
            val url = URL(urlString)
            val con: HttpURLConnection = url.openConnection() as HttpURLConnection

            runBlocking {
                launch {
                    // input stream reader
                    withContext(Dispatchers.IO) {
                        val bf = BufferedReader(InputStreamReader(con.inputStream))
                        var line: String? = bf.readLine()
                        while (line != null) {
                            stb.append(line + "\n")
                            line = bf.readLine()
                        }
                        parseJSON(stb)
                    }
                }
            }

        }
        movieToDB.setOnClickListener {
            runBlocking {
                launch {
                    withContext(Dispatchers.IO) {
                        id++

                        // assigning shared preferences
                        val editor = prefs.edit()
                        editor.putInt("id", id)
                        editor.apply()

                        // setting a new entry to db
                        val movie = Movies(
                            id,
                            title,
                            year,
                            rated,
                            released,
                            runtime,
                            genre,
                            director,
                            writer,
                            actors,
                            plot
                        )
                        moviesDao.insertMovies(movie)

                    }
                }
            }
            Toast.makeText(applicationContext, "Movie Added to DB!", Toast.LENGTH_SHORT).show()
        }

    }

    private fun parseJSON(stb: java.lang.StringBuilder) {
        // this contains the full JSON returned by the Web Service
        val movie = JSONObject(stb.toString())
        // Information about the movie
        val movieDetails = java.lang.StringBuilder()
        // extract the necessary details
        title = movie["Title"] as String
        movieDetails.append("Title: $title \n")
        year = movie["Year"] as String
        movieDetails.append("Year: $year \n")
        rated = movie["Rated"] as String
        movieDetails.append("Rated: $rated \n")
        released = movie["Released"] as String
        movieDetails.append("Released: $released \n")
        runtime = movie["Runtime"] as String
        movieDetails.append("Runtime: $runtime \n")
        genre = movie["Genre"] as String
        movieDetails.append("Genre: $genre \n")
        director = movie["Director"] as String
        movieDetails.append("Director: $director \n")
        writer = movie["Writer"] as String
        movieDetails.append("Writer: $writer \n")
        actors = movie["Actors"] as String
        movieDetails.append("Actors: $actors \n \n")
        plot = movie["Plot"] as String
        movieDetails.append("Plot: $plot")

        movieTv.text = movieDetails
    }
}