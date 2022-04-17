package com.example.movieapp

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.room.Room
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {

    // api key 7086918a
    private lateinit var prefs: SharedPreferences


    var id: Int = 0
    private lateinit var addMoviesBtn: Button
    private lateinit var searchActorsBtn: Button
    private lateinit var searchMoviesBtn: Button
    private lateinit var searchBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Loading from Shared Preferences
        prefs = getSharedPreferences("saveData", MODE_PRIVATE)
        id = prefs.getInt("id", 0)

        addMoviesBtn = findViewById(R.id.addMoviesBtn)
        searchActorsBtn = findViewById(R.id.searchActorsBtn)
        searchMoviesBtn = findViewById(R.id.searchMoviesBtn)
        searchBtn = findViewById(R.id.searchBtn)

        val db = Room.databaseBuilder(
            this, MoviesDatabase::class.java,
            "myDatabase"
        ).build()
        val moviesDao = db.moviesDao()

        addMoviesBtn.setOnClickListener{
            runBlocking {
                launch {
                    val movie1 = Movies(
                        1, "The Shawshank Redemption",
                        "1994",
                        "R",
                        "14 Oct 1994",
                        "142 min",
                        "Drama",
                        "Frank Darabont",
                        "Stephen King, Frank Darabont",
                        "Tim Robbins, Morgan Freeman, Bob Gunton",
                        "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency."
                    )
                    id++
                    val movie2 = Movies(
                        2, "Batman: The Dark Knight Returns, Part 1",
                        "2012",
                        "PG-13",
                        "25 Sep 2012",
                        "76 min",
                        "Animation, Action, Crime, Drama, Thriller",
                        "Jay Oliva",
                        "Bob Kane (character created by: Batman), Frank Miller (comic book), Klaus Janson (comic book), Bob Goodman",
                        "Peter Weller, Ariel Winter, David Selby, Wade Williams",
                        "Batman has not been seen for ten years. A new breed of criminal ravages Gotham City, forcing 55-year-old Bruce Wayne back into the cape and cowl. But, does he still have what it takes to fight crime in a new era?"
                    )
                    id++
                    val movie3 = Movies(
                        3, "The Lord of the Rings: The Return of the King",
                        "2003",
                        "PG-13",
                        "17 Dec 2003",
                        "201 min",
                        "Action, Adventure, Drama",
                        "Peter Jackson",
                        "J.R.R. Tolkien, Fran Walsh, Philippa Boyens",
                        "Elijah Wood, Viggo Mortensen, Ian McKellen",
                        "Gandalf and Aragorn lead the World of Men against Sauron's army to draw his gaze from Frodo and Sam as they approach Mount Doom with the One Ring."
                    )
                    id++
                    val movie4 = Movies(
                        4, "Inception",
                        "2010",
                        "PG-13",
                        "16 Jul 2010",
                        "148 min",
                        "Action, Adventure, Sci-Fi",
                        "Christopher Nolan",
                        "Christopher Nolan",
                        "Leonardo DiCaprio, Joseph Gordon-Levitt, Elliot Page",
                        "A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O., but his tragic past may doom the project and his team to disaster."
                    )
                    id++
                    val movie5 = Movies(
                        5, "The Matrix",
                        "1999",
                        "R",
                        "31 Mar 1999",
                        "136 min",
                        "Action, Sci-Fi",
                        "Lana Wachowski, Lilly Wachowski",
                        "Lilly Wachowski, Lana Wachowski",
                        "Keanu Reeves, Laurence Fishburne, Carrie-Anne Moss",
                        "When a beautiful stranger leads computer hacker Neo to a forbidding underworld, he discovers the shocking truth--the life he knows is the elaborate deception of an evil cyber-intelligence."
                    )
                    id++

                    val editor = prefs.edit()
                    editor.putInt("id", id)
                    editor.apply()

                    moviesDao.insertMovies(movie1, movie2, movie3, movie4, movie5)
                    //val movies: List<Movies> = moviesDao.getAll()
                }
            }
            val intent = Intent(this, AddMovies::class.java)
            startActivity(intent)
        }
        searchMoviesBtn.setOnClickListener{
            val intent = Intent(this, SearchMovies::class.java)
            startActivity(intent)
        }
        searchActorsBtn.setOnClickListener{
            val intent = Intent(this, SearchActors::class.java)
            startActivity(intent)
        }
        searchBtn.setOnClickListener{
            val intent = Intent(this, SearchName::class.java)
            startActivity(intent)
        }

    }
}