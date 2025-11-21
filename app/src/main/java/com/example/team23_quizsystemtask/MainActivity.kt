package com.example.team23_quizsystemtask

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.launch
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat.enableEdgeToEdge
import com.example.team23_quizsystemtask.ui.theme.Team23QuizSystemTaskTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.TextField

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = FlashCardDatabase.getInstance(this);

        setContent {
            Team23QuizSystemTaskTheme{
                Page1Screen(db)
            }
        }

    }
}




@Composable
fun Page1Screen(db: FlashCardDatabase) {
    var Question by remember { mutableStateOf("") }
    var Answer by remember { mutableStateOf("") }
    var Category by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    Column (modifier = Modifier.background(Color.White).fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        )
    {
        val context= LocalContext.current
        TextField(value = Question,
            onValueChange = { Question = it },
            placeholder = {
                Text("Enter Question")
            },
            modifier = Modifier
                .background(Color.White)
                .padding(6.dp),
            colors = TextFieldDefaults.colors( // Use TextFieldDefaults.colors
                unfocusedTextColor = Color.Gray,
                focusedTextColor = Color.Black,
                unfocusedPlaceholderColor = Color.Gray,

                unfocusedContainerColor = Color.White, // Use containerColor instead of backgroundColor
                focusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Gray,
                unfocusedIndicatorColor = Color.Gray,
                cursorColor = Color.Black
            )
                )
        TextField(value = Answer,
            onValueChange = { Answer = it },
            placeholder = {
                Text("Enter Answer")
            },
            modifier = Modifier
                .background(Color.White)
                .padding(6.dp),
            colors = TextFieldDefaults.colors( // Use TextFieldDefaults.colors
                unfocusedTextColor = Color.Gray,
                unfocusedPlaceholderColor = Color.Gray,

                focusedTextColor = Color.Black,
                unfocusedContainerColor = Color.White, // Use containerColor instead of backgroundColor
                focusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Gray,
                unfocusedIndicatorColor = Color.Gray,
                cursorColor = Color.Black
            )
        )
        TextField(value = Category,
            onValueChange = { Category = it },
            placeholder = {
                Text("Enter Category")
            },
            modifier = Modifier
                .background(Color.White)
                .padding(6.dp),
            colors = TextFieldDefaults.colors( // Use TextFieldDefaults.colors
                unfocusedTextColor = Color.Gray,
                focusedTextColor = Color.Black,
                unfocusedContainerColor = Color.White, // Use containerColor instead of backgroundColor
                focusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Gray,
                unfocusedIndicatorColor = Color.Gray,
                unfocusedPlaceholderColor = Color.Gray,
                cursorColor = Color.Black
            )
        )
        Button(onClick ={
            if(Question == ""||Answer == ""||Category=="")
            {
                // In an Activity or Context-aware function:
                Toast.makeText(context, "All inputs required !!", Toast.LENGTH_SHORT).show()
            }
            else{addFlashCard(scope, Question, Answer, Category, db)
                Question="";
                Category="";
                Answer=""
            }
        }
            ,
            modifier = Modifier.fillMaxWidth(.96f)
                .padding( 10.dp)
                )
        {
            Text(text = "Add Flash Card", fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center)
        }
        Button(onClick = {goToQuiz()},
            modifier = Modifier.fillMaxWidth(.96f)
                .padding( 10.dp)
        )
        {
            Text(text = "Go To Quiz", fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center)
        }

    }


}
fun addFlashCard(scope: CoroutineScope, question: String, answer: String, category: String, db: FlashCardDatabase)
{
    scope.launch{
        db.flashcardDao().AddQuestion(FlashCard(question = question, answer = answer, category = category))
        Log.d("category",db.flashcardDao().GetCategories().toString())
    }
}
fun goToQuiz()
{
//   val intent= Intent(this,Page2::class.java)
//    startActivity(intent)
}