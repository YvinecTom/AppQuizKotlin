package QuizCreator

import QuizApp
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import secondaryContainerLight

val nombreQuestions = 3


// Mise à jour de l'énumération des écrans
enum class Screen {
    CategoryInput,
    QuestionCreation,
    QuizSummary,
    QuizSelection, // Nouvel écran ajouté,
    QuizApp
}

@Composable
fun QuizCreatorApp(
    userName: String
) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.CategoryInput) }
    var quizCategory by remember { mutableStateOf("") }
    var questions by remember { mutableStateOf(mutableListOf<QuizQuestion>()) }

    var questionNumber by remember { mutableStateOf(0) }
    Scaffold(
        topBar = {
            // N'afficher la topBar que pour certains écrans
            if (currentScreen != Screen.QuizApp && currentScreen != Screen.QuizSummary) {
                TopAppBar(
                    title = { Text("Prout") },
                    navigationIcon = {
                        IconButton(onClick = {
                            // Revenir à l'écran de sélection des quiz
                            if (currentScreen == Screen.CategoryInput) {
                                currentScreen = Screen.QuizApp
                            } else {
                                currentScreen = Screen.CategoryInput
                            }
                        }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Retour")
                        }
                    },
                    modifier = Modifier
                        .background(secondaryContainerLight)
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (currentScreen) {
                Screen.CategoryInput -> CategoryInputScreen(
                    onCategoryEntered = { category ->
                        quizCategory = category
                        currentScreen = Screen.QuestionCreation
                    }
                )
                Screen.QuestionCreation -> QuestionCreationScreen(
                    userName = userName,
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
                        currentScreen = Screen.QuizSelection // Changement ici
                    },
                    onReset = {
                        questions.clear()
                        questionNumber = 0
                        currentScreen = Screen.CategoryInput
                    }
                )
                // Nouvel écran de sélection de quiz à implémenter
                Screen.QuizSelection -> QuizSelectionScreen(
                    onCreateNewQuiz = {
                        currentScreen = Screen.CategoryInput
                    }
                )

                Screen.QuizApp -> QuizApp(
                    userName = userName
                )
            }
        }
    }
}

@Composable
fun QuestionCreationScreen(
    userName: String,
    currentQuestionNumber: Int,
    onQuestionAdded: (QuizQuestion) -> Unit
) {
    var question by remember { mutableStateOf("") }
    var options by remember { mutableStateOf(List(4) { "" }) }
    var correctOptionIndex by remember { mutableStateOf<Int?>(null) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Question $currentQuestionNumber/$nombreQuestions", style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = question,
            onValueChange = { question = it },
            label = { Text("Énoncé de la question") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        // Options de réponse
        options.forEachIndexed { index, optionText ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
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
                    onValueChange = {
                        val newOptions = options.toMutableList()
                        newOptions[index] = it
                        options = newOptions
                    },
                    label = { Text("Option ${index + 1}") },
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
        Button(
            onClick = {
                // Validation
                if (question.isBlank()) {
                    // Gérer l'erreur de question vide
                    return@Button
                }
                if (options.any { it.isBlank() }) {
                    // Gérer l'erreur d'option vide
                    return@Button
                }
                if (correctOptionIndex == null) {
                    // Gérer l'erreur de pas de bonne réponse
                    return@Button
                }
                // Créer la question
                val quizQuestion = QuizQuestion(
                    question = question,
                    options = options,
                    answer = options[correctOptionIndex!!]
                )
                // Réinitialiser les champs
                onQuestionAdded(quizQuestion)
                question = ""
                options = List(4) { "" }
                correctOptionIndex = null
            },
            enabled = question.isNotBlank() &&
                    options.none { it.isBlank() } &&
                    correctOptionIndex != null
        ) {
            Text("Ajouter la question")
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
            label = { Text("Nom de la catégorie") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (category.isNotBlank()) onCategoryEntered(category)
            },
            enabled = category.isNotBlank()
        ) {
            Text("Commencer la création")
        }
    }
}