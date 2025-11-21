package com.example.team23_quizsystemtask

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FlashCardsDao {
    @Insert
    suspend fun AddQuestion(falshCard: FlashCard);

    @Query("select distinct(category) from flashcard")
    suspend fun GetCategories() : List<String>

    @Query("SELECT * FROM flashcard WHERE category=:category ORDER BY random() LIMIT 1")
    suspend fun GetRandomQuestionByCategory(category: String) : FlashCard

}