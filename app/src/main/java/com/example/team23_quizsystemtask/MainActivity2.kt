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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.team23_quizsystemtask.ui.theme.Team23QuizSystemTaskTheme
import kotlinx.coroutines.CoroutineScope
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

// FIX 1: Use mutableStateOf for global variables to trigger recomposition
var question by mutableStateOf<String?>("Show Question")
var answer by mutableStateOf<String?>("Show Answer")
var fullRow: FlashCard? = null // This doesn't need to be stateful if it's just a data holder

@Composable
fun MainScreen(db: FlashCardDatabase, modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    // FIX 2: Create state for the selected category within the composable scope
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Pass the state and a lambda to update it
        DisplayJCSpinner(
            db = db,
            onCategorySelected = { category ->
                selectedCategory = category
                // Reset question/answer when category changes
                question = "Show Question"
                answer = "Show Answer"
            }
        )

        Button(
            onClick = { ShowQuestion(scope, selectedCategory, db) },
            modifier = Modifier
                .fillMaxWidth(0.96f)
                .padding(10.dp),
            // Enable button only if a category has been selected
            enabled = selectedCategory != null
        ) {
            Text(
                text = question ?: "Show Question",
                fontSize = 20.sp
            )
        }
        Button(
            onClick = { ShowAnswer() },
            modifier = Modifier
                .fillMaxWidth(0.96f)
                .padding(10.dp),
            // Enable only after a question has been shown
            enabled = fullRow != null
        ) {
            Text(
                text = answer ?: "Show Answer",
                fontSize = 20.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayJCSpinner(db: FlashCardDatabase, onCategorySelected: (String) -> Unit) {
    // FIX 3: This holds the list of categories from the DB
    var parentOptions by remember { mutableStateOf<List<String>>(emptyList()) }
    var expandedState by remember { mutableStateOf(false) }
    // FIX 4: selectedOption is now stateful and can be null initially
    var selectedOption by remember { mutableStateOf<String?>(null) }
    val myContext = LocalContext.current

    // FIX 5: Use LaunchedEffect to load data once when the composable enters the screen
    LaunchedEffect(key1 = true) {
        val categories = db.flashcardDao().GetCategories()
        if (categories.isNotEmpty()) {
            parentOptions = categories
            // Set initial selection
            val firstCategory = categories[0]
            selectedOption = firstCategory
            onCategorySelected(firstCategory)
        } else {
            Toast.makeText(myContext, "No categories found in the database.", Toast.LENGTH_LONG).show()
        }
    }

    ExposedDropdownMenuBox(
        expanded = expandedState,
        onExpandedChange = { expandedState = !expandedState }
    ) {
        TextField(
            // FIX 6: Provide a default value while loading
            value = selectedOption ?: "Loading...",
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
                        onCategorySelected(option) // Notify the parent
                        expandedState = false
                    }
                )
            }
        }
    }
}

fun ShowQuestion(scope: CoroutineScope, category: String?, db: FlashCardDatabase) {
    if (category == null) return // Don't do anything if no category is selected

    scope.launch {
        fullRow = db.flashcardDao().GetRandomQuestionByCategory(category)
        question = fullRow?.question ?: "No questions in this category"
        // Reset answer when showing a new question
        answer = "Show Answer"
    }
}

fun ShowAnswer() {
    // Only update if there's a question loaded
    if (fullRow != null) {
        answer = fullRow?.answer
    }
}
