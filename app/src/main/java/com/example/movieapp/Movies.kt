package com.example.movieapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Movies(
    @PrimaryKey val id: Int,
    val title: String?,
    val year: Int?,
    val rated: String?,
    val released: String?,
    val runtime: String?,
    val genre: String?,
    val director: String?,
    val writer: String?,
    val actors: String?,
    val plot: String?
)