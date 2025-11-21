package com.example.team23_quizsystemtask

import android.os.Bundle
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.team23_quizsystemtask.ui.theme.Team23QuizSystemTaskTheme

class MainActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Team23QuizSystemTaskTheme {
                    MainScreen(
                    )
                }
            }
        }
    }

val question: String? =null
val Answer: String? =null
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DisplayJCSpinner()

        Button(
            onClick = { ShowQuestion() },
            modifier = Modifier
                .fillMaxWidth(0.96f)
                .padding(10.dp)
        ) {
            Text(
                text = question?: "Show Question",
                fontSize = 20.sp
            )
        }
        Button(
            onClick = { ShowAnswer() },
            modifier = Modifier
                .fillMaxWidth(0.96f)
                .padding(10.dp)
        ) {
            Text(
                text = Answer?: "Show Answer",
                fontSize = 20.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayJCSpinner() {
    val parentOptions = listOf("Geography", "Literature")
    var expandedState by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(parentOptions[0]) }
    val myContext = LocalContext.current

    ExposedDropdownMenuBox(
        expanded = expandedState,
        onExpandedChange = { expandedState = !expandedState }
    ) {
        TextField(
            value = selectedOption,
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
                        expandedState = false
                    }
                )
            }
        }
    }
}

fun ShowQuestion() {
    // TODO: Implement question logic
}
fun ShowAnswer(){

}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Team23QuizSystemTaskTheme {
        MainScreen()
    }
}
