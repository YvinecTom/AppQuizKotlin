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
import QuizCreator.QuizCreatorApp
import androidx.compose.ui.unit.sp

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



@Composable
fun QuizApp() {
    val userName by GlobalState.userName.collectAsState()
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var scores by remember { mutableStateOf(mapOf<String, Int>()) }
    var isQuizCreator by remember { mutableStateOf(false) }

    println("userName from GlobalState: $userName")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundLight)
    ) {
        when {
            userName == null -> WelcomeScreen { name ->
                GlobalState.setUserName(name)
            }
            isQuizCreator -> QuizCreatorApp()
            selectedCategory == null -> CategorySelectionScreen(
                onCategorySelected = { category -> selectedCategory = category },
                scores = scores,
                onQuizCreatorSelected = { isQuizCreator = true }
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
            readQuizQuestionsFromFileDirectly("${category}.json")
        } catch (e: Exception) {
            null
        }

    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        quiz?.let { quiz ->
            if (quiz.questions.isEmpty()) {
                Text(
                    "Chargement des données...",
                    fontFamily = nunito,
                    style = MaterialTheme.typography.h6
                )
            } else {
                val currentQuestion = quiz.questions[currentQuestionIndex]

                userName?.let {
                    Text(
                        "Joueur : $it",
                        style = MaterialTheme.typography.subtitle1,
                        fontFamily = nunito,
                        textAlign = TextAlign.Center,
                        fontSize = 25.sp
                    )
                }
                Spacer(Modifier.height(16.dp))
                Text(
                    currentQuestion.question,
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center,
                    fontFamily = nunito,
                    fontSize = 25.sp
                )
                Spacer(Modifier.height(16.dp))

                Response(
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

                Spacer(Modifier.height(16.dp))

                if (userAnswer.isNotEmpty()) {
                    Text(
                        message,
                        textAlign = TextAlign.Center,
                        fontFamily = nunito
                    )
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





