package com.example.taskmanagerapp

import android.os.Bundle
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginScreen()
        }
    }
}

@Composable
fun LoginScreen() {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoggedIn by remember { mutableStateOf(false) }

    if (isLoggedIn) {
        DashboardScreen(username)
    } else {
        Column(modifier = Modifier.padding(16.dp)) {

            TextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") }
            )

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") }
            )

            Button(onClick = { isLoggedIn = true }) {
                Text("Login")
            }
        }
    }
}

@Composable
fun DashboardScreen(username: String) {
    var showWeb by remember { mutableStateOf(false) }

    if (showWeb) {
        WebScreen()
    } else {
        Column(modifier = Modifier.padding(16.dp)) {

            Text("Welcome $username")

            TaskList(
                onOpenWeb = { showWeb = true }
            )
        }
    }
}

@Composable
fun TaskList(onOpenWeb: () -> Unit) {

    val tasks = remember {
        mutableStateListOf("Task 1", "Task 2")
    }

    var selectedIndex by remember { mutableStateOf(-1) }
    var showAddDialog by remember { mutableStateOf(false) }
    var newTask by remember { mutableStateOf("") }

    Column {

        Button(onClick = { showAddDialog = true }) {
            Text("ADD TASK")
        }

        Button(onClick = { onOpenWeb() }) {
            Text("OPEN WEB")
        }

        LazyColumn {
            itemsIndexed(tasks) { index, task ->
                Text(
                    text = task,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            selectedIndex = index
                        }
                )
            }
        }

        // Add Task Dialog
        if (showAddDialog) {
            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                title = { Text("Enter Task") },
                text = {
                    TextField(
                        value = newTask,
                        onValueChange = { newTask = it }
                    )
                },
                confirmButton = {
                    Button(onClick = {
                        tasks.add(newTask)
                        newTask = ""
                        showAddDialog = false
                    }) {
                        Text("SAVE")
                    }
                }
            )
        }

        // Task Options Dialog
        if (selectedIndex != -1) {
            AlertDialog(
                onDismissRequest = { selectedIndex = -1 },
                title = { Text("Task Options") },
                text = { Text("Edit or Delete") },
                confirmButton = {
                    Button(onClick = {
                        tasks[selectedIndex] += " (edited)"
                        selectedIndex = -1
                    }) {
                        Text("Edit")
                    }
                },
                dismissButton = {
                    Button(onClick = {
                        tasks.removeAt(selectedIndex)
                        selectedIndex = -1
                    }) {
                        Text("Delete")
                    }
                }
            )
        }
    }
}

@Composable
fun WebScreen() {
    AndroidView(factory = {
        WebView(it).apply {
            loadUrl("https://www.google.com")
        }
    })
}