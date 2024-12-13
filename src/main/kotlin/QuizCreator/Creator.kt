package QuizCreator

import QuizApp
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import backgroundLight
import nunito
import primaryContainerLight
import primaryLight
import secondaryContainerLight
import secondaryLight
import tertiaryContainerLight

val nombreQuestions = 3


enum class Screen {
    CategoryInput,
    QuestionCreation,
    QuizSummary,
    QuizApp
}

@Composable
fun QuizCreatorApp() {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.CategoryInput) }
    var quizCategory by remember { mutableStateOf("") }
    var questions by remember { mutableStateOf(mutableListOf<QuizQuestion>()) }

    var questionNumber by remember { mutableStateOf(0) }
    Scaffold(
        topBar = {
            if (currentScreen != Screen.QuizApp && currentScreen != Screen.QuizSummary) {
                TopAppBar(
                    title = { Text("QuizCreator", fontFamily = nunito) },
                    navigationIcon = {
                        IconButton(onClick = {
                            when (currentScreen) {
                                Screen.CategoryInput -> currentScreen = Screen.QuizApp
                                Screen.QuestionCreation -> currentScreen = Screen.CategoryInput
                                else -> currentScreen = Screen.QuizApp
                            }
                        }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
                        }
                    },
                    backgroundColor = primaryContainerLight
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(backgroundLight)
        ) {
            when (currentScreen) {
                Screen.CategoryInput -> CategoryInputScreen(
                    onCategoryEntered = { category ->
                        quizCategory = category
                        currentScreen = Screen.QuestionCreation
                    }
                )
                Screen.QuestionCreation -> QuestionCreationScreen(
                    currentQuestionNumber = questionNumber + 1,
                    onQuestionAdded = { question ->
                        questions.add(question)
                        questionNumber++
                        if (questionNumber == nombreQuestions) {
                            saveQuizToFile(quizCategory, questions)
                            currentScreen = Screen.QuizSummary
                        }
                    }
                )
                Screen.QuizSummary -> QuizSummaryScreen(
                    category = quizCategory,
                    questions = questions,
                    onSave = {
                        saveQuizToFile(quizCategory, questions)
                        questions.clear()
                        questionNumber = 0
                        currentScreen = Screen.QuizApp
                    },
                    onReset = {
                        questions.clear()
                        questionNumber = 0
                        currentScreen = Screen.CategoryInput
                    }
                )
                Screen.QuizApp -> QuizApp()
            }
        }
    }
}

@Composable
fun QuestionCreationScreen(
    currentQuestionNumber: Int,
    onQuestionAdded: (QuizQuestion) -> Unit
) {
    var question by remember { mutableStateOf("") }
    var options by remember { mutableStateOf(List(4) { "" }) }
    var correctOptionIndex by remember { mutableStateOf<Int?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Question $currentQuestionNumber/$nombreQuestions", style = MaterialTheme.typography.h6, fontFamily = nunito)
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = question,
            onValueChange = { question = it },
            label = { Text("Énoncé de la question", fontFamily = nunito,
                fontSize = 15.sp,
                color = secondaryLight
            ) },
            modifier = Modifier.fillMaxWidth(0.5f),
            textStyle = TextStyle(fontSize = 20.sp),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = primaryLight,
                unfocusedIndicatorColor = secondaryLight
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Options de réponse
        options.forEachIndexed { index, optionText ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = correctOptionIndex == index,
                    onCheckedChange = {
                        correctOptionIndex = if (it) index else null
                    }
                )
                TextField(
                    value = optionText,
                    onValueChange = { newValue ->
                        val newOptions = options.toMutableList()
                        newOptions[index] = newValue

                        val duplicates = newOptions.filter { it.isNotBlank() }.groupingBy { it }.eachCount()
                        val hasDuplicates = duplicates.any { it.value > 1 }

                        if (hasDuplicates) {
                            errorMessage = "Les réponses doivent être différentes"
                        } else {
                            errorMessage = null
                            options = newOptions
                        }
                    },
                    label = { Text("Option ${index + 1}", fontSize = 15.sp,
                        color = secondaryLight
                    ) },
                    modifier = Modifier.fillMaxWidth(0.5f),
                    textStyle = TextStyle(fontSize = 20.sp),
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = primaryLight,
                        unfocusedIndicatorColor = secondaryLight
                    )
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        errorMessage?.let {
            Text(
                text = it,
                color = Color.Red,
                fontFamily = nunito,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Button(
            onClick = {
                if (question.isBlank()) {
                    errorMessage = "La question ne peut pas être vide"
                    return@Button
                }
                if (options.any { it.isBlank() }) {
                    errorMessage = "Toutes les options doivent être remplies"
                    return@Button
                }
                if (correctOptionIndex == null) {
                    errorMessage = "Sélectionnez une réponse correcte"
                    return@Button
                }

                val duplicates = options.filter { it.isNotBlank() }.groupingBy { it }.eachCount()
                if (duplicates.any { it.value > 1 }) {
                    errorMessage = "Les réponses doivent être différentes"
                    return@Button
                }

                val quizQuestion = QuizQuestion(
                    question = question,
                    options = options,
                    answer = options[correctOptionIndex!!]
                )
                onQuestionAdded(quizQuestion)
                question = ""
                options = List(4) { "" }
                correctOptionIndex = null
                errorMessage = null
            },
            enabled = question.isNotBlank() &&
                    options.none { it.isBlank() } &&
                    correctOptionIndex != null &&
                    errorMessage == null,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = secondaryContainerLight,
                contentColor = primaryLight
            )
        ) {
            Text("Ajouter la question", fontFamily = nunito)
        }
    }
}

@Composable
fun CategoryInputScreen(
    onCategoryEntered: (String) -> Unit
) {
    var category by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Créer un nouveau Quiz", style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = category,
            onValueChange = { category = it },
            label = { Text(
                text = "Nom de la catégorie",
                fontFamily = nunito,
                fontSize = 15.sp,
                color = secondaryLight
            ) },
            modifier = Modifier.fillMaxWidth(0.5f),
            textStyle = TextStyle(fontSize = 20.sp),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = primaryLight,
                unfocusedIndicatorColor = secondaryLight
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (category.isNotBlank()) onCategoryEntered(category)
            },
            enabled = category.isNotBlank(),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = secondaryContainerLight,
                contentColor = primaryLight
            ),
        ) {
            Text(
                "Commencer la création",
                fontFamily = nunito,
                fontSize = 20.sp
            )
        }
    }
}