package com.example.team23_quizsystemtask

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.team23_quizsystemtask.ui.theme.Team23QuizSystemTaskTheme
import kotlinx.coroutines.launch

class MainActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val db = FlashCardDatabase.getInstance(this)

        setContent {
            Team23QuizSystemTaskTheme {

                MainScreen(db)
            }
        }
    }
}

@Composable
fun MainScreen(db: FlashCardDatabase, modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()


    var selectedCategory by remember { mutableStateOf<String?>(null) }

    var questionText by remember { mutableStateOf("") }
    var answerText by remember { mutableStateOf("") }
    var currentFlashCard by remember { mutableStateOf<FlashCard?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DisplayJCSpinner(
            db = db,
            onCategorySelected = { category ->
                selectedCategory = category

                questionText = ""
                answerText = ""
                currentFlashCard = null
            }
        )

        Button(
            onClick = {
                scope.launch {
                    if (selectedCategory != null) {
                        val flashcard = db.flashcardDao().GetRandomQuestionByCategory(selectedCategory!!)
                        currentFlashCard = flashcard
                        questionText = flashcard?.question ?: "No questions in this category"
                        answerText = ""
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth(0.96f)
                .padding(10.dp),
            enabled = selectedCategory != null
        ) {
            Text(
                text = "Show Question",
                fontSize = 20.sp
            )
        }

        Text(
            text = questionText,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            fontSize = 18.sp
        )

        Button(
            onClick = {
                if (currentFlashCard != null) {
                    answerText = currentFlashCard?.answer ?: "No answer available"
                }
            },
            modifier = Modifier
                .fillMaxWidth(0.96f)
                .padding(10.dp),
            enabled = currentFlashCard != null
        ) {
            Text(
                text = "Show Answer",
                fontSize = 20.sp
            )
        }

        Text(
            text = answerText,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            fontSize = 18.sp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayJCSpinner(db: FlashCardDatabase, onCategorySelected: (String) -> Unit) {
    var parentOptions by remember { mutableStateOf<List<String>>(emptyList()) }
    var expandedState by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf<String?>(null) }
    val myContext = LocalContext.current

    LaunchedEffect(key1 = true) {
        val categories = db.flashcardDao().GetCategories()

        if (categories.isNotEmpty()) {
            parentOptions = categories
            val firstCategory = categories[0]
            selectedOption = firstCategory
            onCategorySelected(firstCategory)
        } else {
            Toast.makeText(myContext, "No categories found. Please add flashcards first.", Toast.LENGTH_LONG).show()
        }
    }

    ExposedDropdownMenuBox(

        expanded = expandedState,
        onExpandedChange = { expandedState = !expandedState }
    ) {
        TextField(
            value = selectedOption ?: "Loading Categories...",
            onValueChange = {},
            readOnly = true,
            label = { Text("Select Category") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedState) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expandedState,
            onDismissRequest = { expandedState = false }
        ) {
            parentOptions.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        selectedOption = option
                        onCategorySelected(option)
                        expandedState = false
                    }
                )
            }
        }
    }
}
