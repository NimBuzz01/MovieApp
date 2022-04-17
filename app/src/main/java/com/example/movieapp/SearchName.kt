package com.example.movieapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class SearchName : AppCompatActivity() {

    private lateinit var nameBtn: Button
    private lateinit var getName: EditText
    private lateinit var nameTv: TextView

    lateinit var title: String

    // https://www.omdbapi.com/?s=moviename&apikey=7086918a

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_name)

        nameBtn = findViewById(R.id.nameBtn)
        getName = findViewById(R.id.getName)
        nameTv = findViewById(R.id.nameTv)

        // collecting all the JSON string
        val stb = StringBuilder()

        nameBtn.setOnClickListener {
            nameTv.text = ""
            val movieName = getName.text.toString()
            val urlString = "https://www.omdbapi.com/?s=${movieName}&apikey=7086918a"
            val url = URL(urlString)
            val con: HttpURLConnection = url.openConnection() as HttpURLConnection

            runBlocking {
                launch {
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
    }

    private suspend fun parseJSON(stb: java.lang.StringBuilder) {
        // this contains the full JSON returned by the Web Service
        val json = JSONObject(stb.toString())
        // Information about all the books extracted by this function
        val movieDetails = java.lang.StringBuilder()
        val jsonArray:JSONArray = json.getJSONArray("Search")
        // extract all the books from the JSON array
        for (i in 0 until jsonArray.length()) {
            val movie: JSONObject = jsonArray[i] as JSONObject // this is a json object
            // extract the title
            val title = movie["Title"] as String
            movieDetails.append("${i+1}) \"$title\" \n")
        }
        nameTv.text = movieDetails
    }
}