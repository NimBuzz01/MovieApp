package com.example.movieapp

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import org.w3c.dom.Text
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class SearchMovies : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences

    private lateinit var movieBtn: Button
    private lateinit var movieToDB: Button
    private lateinit var getMovie: EditText
    private lateinit var movieTv: TextView

    var id: Int = 0
    lateinit var title: String
    lateinit var year: String
    lateinit var rated: String
    lateinit var released: String
    lateinit var runtime: String
    lateinit var genre: String
    lateinit var director: String
    lateinit var writer: String
    lateinit var actors: String
    lateinit var plot: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_movies)

        //Loading from Shared Preferences
        prefs = getSharedPreferences("saveData", MODE_PRIVATE)
        id = prefs.getInt("id", 0)

        // collecting all the JSON string
        val stb = StringBuilder()

        movieBtn = findViewById(R.id.movieBtn)
        movieToDB = findViewById(R.id.movieToDB)
        getMovie = findViewById(R.id.getMovie)
        movieTv = findViewById(R.id.movieTv)

        val db = Room.databaseBuilder(
            this, MoviesDatabase::class.java,
            "myDatabase"
        ).build()
        val moviesDao = db.moviesDao()

        movieBtn.setOnClickListener {
            val movieName = getMovie.text.toString()
            print(movieName)
            val urlString = "https://www.omdbapi.com/?t=${movieName}&apikey=7086918a";
            val url = URL(urlString)
            val con: HttpURLConnection = url.openConnection() as HttpURLConnection

            runBlocking {
                launch {
                    // run the code of the coroutine in a new thread
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
                    // run the code of the coroutine in a new thread
                    withContext(Dispatchers.IO) {
                        id++
                        val editor = prefs.edit()
                        editor.putInt("id", id)
                        editor.apply()

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
        }

    }

    private suspend fun parseJSON(stb: java.lang.StringBuilder) {
        // this contains the full JSON returned by the Web Service
        val movie = JSONObject(stb.toString())
        // Information about all the movies
        val movieDetails = java.lang.StringBuilder()
        // extract only the first movie from the JSON array
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