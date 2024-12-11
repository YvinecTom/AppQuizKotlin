import androidx.compose.animation.animateColorAsState
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.InputStreamReader
import java.nio.charset.Charset
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font

val belgrano = FontFamily(
    Font(
        resource = "font/Belgrano-Regular.ttf",
        weight = FontWeight.Normal
    )
)
// Fonction de lecture du fichier JSON
fun readQuizQuestionsFromFile(filename: String): List<QuizQuestion> {
    val inputStream = object {}.javaClass.getResourceAsStream("/$filename")
        ?: throw IllegalArgumentException("Le fichier $filename n'a pas été trouvé dans les ressources.")
    val reader = InputStreamReader(inputStream, Charset.defaultCharset())
    return Json.decodeFromString(reader.readText())
}

@Serializable
data class QuizQuestion(
    val question: String,
    val options: List<String>,
    val answer: String
)

@Preview
@Composable
fun QuizApp() {
    val backgroundLight = Color(0xFFFFF8F7)
    val primaryLight = Color(0xFF8E4957)
    val primaryContainerLight = Color(0xFFFFD9DE)
    val secondaryLight = Color(0xFF75565B)
    val secondaryContainerLight = Color(0xFFFFD9DE)
    val tertiaryLight = Color(0xFF795831)
    val tertiaryContainerLight = Color(0xFFFFDDBA)

    var userName by remember { mutableStateOf<String?>(null) }
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var scores by remember { mutableStateOf(mapOf<String, Int>()) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundLight)
    ) {
        when {
            userName == null -> WelcomeScreen { name -> userName = name }
            selectedCategory == null -> CategorySelectionScreen(
                onCategorySelected = { category -> selectedCategory = category },
                scores = scores,
                userName = userName
            )
            else -> QuizScreen(
                category = selectedCategory!!,
                userName = userName,
                onFinishQuiz = { score ->
                    scores = scores + (selectedCategory!! to score)
                    selectedCategory = null
                }
            )
        }
    }
}

@Composable
fun WelcomeScreen(onContinue: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var text by remember { mutableStateOf("") }
        Text("Bienvenue dans QuizMania !", style = MaterialTheme.typography.h4, textAlign = TextAlign.Center)
        Spacer(Modifier.height(16.dp))
        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text(
                text = "Entrez votre nom",
                fontFamily = belgrano
            ) },
            modifier = Modifier.fillMaxWidth(0.8f)
        )
        Spacer(Modifier.height(16.dp))
        Button(onClick = { if (text.isNotEmpty()) onContinue(text) }) {
            Text("Commencer")
        }
    }
}

@Composable
fun CategorySelectionScreen(
    onCategorySelected: (String) -> Unit,
    scores: Map<String, Int>,
    userName: String?
) {
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        userName?.let {
            Text("Bonjour $it", style = MaterialTheme.typography.h6, modifier = Modifier.padding(bottom = 16.dp))
        }
        Text("Sélectionnez une catégorie", style = MaterialTheme.typography.h5, textAlign = TextAlign.Center)
        Spacer(Modifier.height(16.dp))
        val categories = listOf("cinema", "jeu_video", "geographie", "series", "sport")
        categories.forEach { category ->
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(8.dp)) {
                Button(onClick = { onCategorySelected(category) }, Modifier.fillMaxWidth(0.8f)) {
                    Text(category.capitalize())
                }
                scores[category]?.let { Text("Score: $it", fontSize = 16.sp) }
            }
        }
    }
}

@Composable
fun QuizScreen(
    category: String,
    userName: String?,
    onFinishQuiz: (Int) -> Unit
) {
    var questions by remember { mutableStateOf<List<QuizQuestion>>(emptyList()) }
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var userAnswer by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var score by remember { mutableStateOf(0) }

    LaunchedEffect(category) {
        questions = try {
            readQuizQuestionsFromFile("${category}.json")
        } catch (e: Exception) {
            emptyList()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (questions.isEmpty()) {
            Text("Chargement des données...")
        } else {
            val currentQuestion = questions[currentQuestionIndex]
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                userName?.let { Text("Joueur : $it", style = MaterialTheme.typography.subtitle1) }
                Spacer(Modifier.height(16.dp))
                Text(currentQuestion.question, style = MaterialTheme.typography.h6)
                Spacer(Modifier.height(16.dp))
                Reponse(
                    options = currentQuestion.options,
                    currentQuestion = currentQuestion,
                    onAnswerSelected = { selectedOption, isCorrect ->
                        userAnswer = selectedOption
                        message = if (isCorrect) {
                            score++
                            "Correct! Score: $score"
                        } else {
                            "Incorrect. Réponse: ${currentQuestion.answer}. Score: $score"
                        }
                        if (currentQuestionIndex < questions.size - 1) {
                            currentQuestionIndex++
                        } else {
                            onFinishQuiz(score)
                        }
                    }
                )
                Spacer(Modifier.height(16.dp))
                if (userAnswer.isNotEmpty()) Text(message)
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Reponse(
    options: List<String>,
    currentQuestion: QuizQuestion,
    onAnswerSelected: (String, Boolean) -> Unit
) {
    Column {
        Row (
            modifier = Modifier.weight(1f)
        ){
            options.forEach { option ->
                val interactionSource = remember { MutableInteractionSource() }
                val isHovered by interactionSource.collectIsHoveredAsState()
                val buttonColor by animateColorAsState(
                    targetValue = if (isHovered) Color.Red else MaterialTheme.colors.primary
                )
                Button(
                    onClick = { onAnswerSelected(option, option == currentQuestion.answer) },
                    colors = ButtonDefaults.buttonColors(backgroundColor = buttonColor),
                    interactionSource = interactionSource,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(option)
                }
            }
        }

    }
}


