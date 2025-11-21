package com.example.team23_quizsystemtask;

import android.content.Context
import androidx.room.Database;
import androidx.room.Room
import androidx.room.RoomDatabase;

@Database(entities = [FlashCard::class], version = 1)
abstract class FlashCardDatabase : RoomDatabase() {
    abstract fun flashcardDao() : FlashCardsDao

    companion object{
        @Volatile
        private var INSTANCE : FlashCardDatabase? = null

        fun getInstance(context: Context): FlashCardDatabase{
            if(INSTANCE == null){
                synchronized(lock = FlashCardDatabase::class){
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        FlashCardDatabase::class.java,
                        "flashcard_db"
                    ).build()
                }
            }
            return INSTANCE!!
        }
    }

}
