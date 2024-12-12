package tests
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

@Serializable
data class QuizQuestion(
    val question: String,
    val options: List<String>,
    val answer: String
)
@Composable
fun QuizCreatorApp() {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.CategoryInput) }
    var quizCategory by remember { mutableStateOf("") }
    var questions by remember { mutableStateOf(mutableListOf<QuizQuestion>()) }
    var questionNumber by remember { mutableStateOf(0) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quiz Creator") }
            )
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
                    currentQuestionNumber = questionNumber + 1,
                    onQuestionAdded = { question ->
                        questions.add(question)
                        questionNumber++
                        if (questionNumber == 10) {
                            saveQuizToFile(quizCategory, questions)
                            currentScreen = Screen.QuizSummary
                        }
                    }
                )
                Screen.QuizSummary -> QuizSummaryScreen(
                    category = quizCategory,
                    questions = questions,
                    onSave = { saveQuizToFile(quizCategory, questions) },
                    onReset = {
                        questions.clear()
                        questionNumber = 0
                        currentScreen = Screen.CategoryInput
                    }
                )
            }
        }
    }
}
// ...
fun saveQuizToFile(category: String, questions: List<QuizQuestion>) {
    val json = Json { prettyPrint = true }
    val jsonString = json.encodeToString(questions)
    // Récupérer le chemin absolu du dossier resources
    val resourcesDir = File(System.getProperty("user.dir") + "/src/main/resources")
    println("Chemin absolu du dossier resources : " + resourcesDir.absolutePath)
    // Créer un dossier pour les quiz s'il n'existe pas
    val quizDir = File(resourcesDir, "quiz")
    if (!quizDir.exists()) {
        quizDir.mkdirs()
    }
    // Sauvegarder le fichier JSON
    val file = File(quizDir, "$category.json")
    file.writeText(jsonString)
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
@Composable
fun QuestionCreationScreen(
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
        Text("Question $currentQuestionNumber/10", style = MaterialTheme.typography.h6)
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
fun QuizSummaryScreen(
    category: String,
    questions: List<QuizQuestion>,
    onSave: () -> Unit,
    onReset: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Résumé du Quiz", style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Catégorie: $category", style = MaterialTheme.typography.subtitle1)
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(questions) { question ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(question.question, style = MaterialTheme.typography.body1)
                        Text("Réponse correcte: ${question.answer}", style = MaterialTheme.typography.caption)
                    }
                }
            }
        }
        Row {
            Button(onClick = onSave) {
                Text("Sauvegarder le Quiz")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = onReset) {
                Text("Recommencer")
            }
        }
    }
}
// Énumération des écrans
enum class Screen {
    CategoryInput,
    QuestionCreation,
    QuizSummary
}