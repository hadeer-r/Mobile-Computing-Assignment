package com.example.team23_quizsystemtask

import android.R
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flashcard")
data class FlashCard(
    @PrimaryKey(autoGenerate = true) val id : Int = 0,
    val question: String,
    val answer: String,
    val category: String,


)
