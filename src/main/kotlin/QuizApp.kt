import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import tests.QuizQuestion

val backgroundLight = Color(0xFFFFF8F7)
val primaryLight = Color(0xFF8E4957)
val primaryContainerLight = Color(0xFFFFD9DE)
val secondaryLight = Color(0xFF75565B)
val secondaryContainerLight = Color(0xFFFFE0E0)
val tertiaryLight = Color(0xFF795831)
val tertiaryContainerLight = Color(0xFFFFDDBA)

val nunito = FontFamily(
    Font(
        resource = "font/Nunito-VariableFont_wght.ttf",
        weight = FontWeight.Normal
    )
)
// Fonction de lecture du fichier JSON




@Preview
@Composable
fun QuizApp() {


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
fun QuizScreen(
    category: String,
    userName: String?,
    onFinishQuiz: (Int) -> Unit
) {
    var quiz by remember { mutableStateOf<Quiz?>(null) }
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var userAnswer by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var score by remember { mutableStateOf(0) }

    LaunchedEffect(category) {
        quiz = try {
            readQuizQuestionsFromFile("${category}.json")
        } catch (e: Exception) {
            null
        }
    }

    // Utilisez Modifier.fillMaxSize() avec contentAlignment.Center
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundLight), // Ajoutez le fond
        contentAlignment = Alignment.Center
    ) {
        quiz?.let { quiz ->
            if (quiz.questions.isEmpty()) {
                Text(
                    "Chargement des données...",
                    fontFamily = nunito,
                    style = MaterialTheme.typography.h6
                )
            } else {
                val currentQuestion = quiz.questions[currentQuestionIndex]

                // Utilisez Column avec verticalArrangement.Center
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth(0.9f) // Limitez la largeur
                        .fillMaxHeight()
                        .padding(16.dp)
                        .background(Color.Cyan)
                ) {
                    userName?.let {
                        Text(
                            "Joueur : $it",
                            style = MaterialTheme.typography.subtitle1,
                            fontFamily = nunito
                        )
                    }
                    Spacer(Modifier.height(16.dp))
                    Text(
                        currentQuestion.question,
                        style = MaterialTheme.typography.h6,
                        textAlign = TextAlign.Center,
                        fontFamily = nunito
                    )
                    Spacer(Modifier.height(16.dp))

                    // Centrez les réponses
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
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
                                if (currentQuestionIndex < quiz.questions.size - 1) {
                                    currentQuestionIndex++
                                } else {
                                    onFinishQuiz(score)
                                }
                            }
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    if (userAnswer.isNotEmpty()) {
                        Text(
                            message,
                            textAlign = TextAlign.Center,
                            fontFamily = nunito
                        )
                    }
                }
            }
        } ?: run {
            Text(
                "Chargement des données...",
                fontFamily = nunito,
                style = MaterialTheme.typography.h6
            )
        }
    }
}




