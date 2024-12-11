import androidx.compose.animation.animateColorAsState
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.InputStreamReader
import java.nio.charset.Charset

// Fonction de lecture du fichier JSON
fun readQuizQuestionsFromFile(filename: String): List<QuizQuestion> {
    val inputStream = object {}.javaClass.getResourceAsStream("/$filename")

    if (inputStream == null) {
        throw IllegalArgumentException("Le fichier $filename n'a pas été trouvé dans les ressources.")
    }

    val reader = InputStreamReader(inputStream, Charset.defaultCharset())
    val fileContent = reader.readText()

    return Json.decodeFromString(fileContent)
}

@Serializable
data class QuizQuestion(
    val question: String,
    val options: List<String>,
    val answer: String
)

// Écran de sélection des catégories
@Composable
fun CategorySelectionScreen(onCategorySelected: (String) -> Unit, scores: Map<String, Int>) {
    Column(Modifier.padding(16.dp)) {
        Text(
            "Sélectionnez une catégorie",
            style = MaterialTheme.typography.h5,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(16.dp))

        // Liste des catégories disponibles
        val categories = listOf("cinema", "jeu_video", "geographie", "series", "sport")
        categories.forEach { category ->
            Column {
                Button(
                    onClick = { onCategorySelected(category) },
                    Modifier.fillMaxWidth().padding(8.dp)
                ) {
                    Text(category.capitalize())
                }
                // Afficher le score pour chaque catégorie si disponible
                scores[category]?.let {
                    Text("Votre score: $it", style = MaterialTheme.typography.body2)
                }
            }
        }
    }
}

@Composable
fun QuizScreen(category: String, onFinishQuiz: (Int) -> Unit) {
    var questions by remember { mutableStateOf<List<QuizQuestion>>(emptyList()) }
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var userAnswer by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var score by remember { mutableStateOf(0) }

    // Charger les questions du fichier correspondant à la catégorie
    LaunchedEffect(category) {
        questions = readQuizQuestionsFromFile("${category}.json")
    }

    Box(
        modifier = Modifier.fillMaxSize(), // Prend toute la taille disponible
        contentAlignment = Alignment.Center // Centre le contenu dans le Box
    ) {
        if (questions.isNotEmpty()) {
            val currentQuestion = questions[currentQuestionIndex]

            Column(
                Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = currentQuestion.question, style = MaterialTheme.typography.h6)
                Spacer(Modifier.height(16.dp))

                val interactionSource = remember { MutableInteractionSource() }
                val isHovered by interactionSource.collectIsHoveredAsState()
                Reponse(
                    options = currentQuestion.options,
                    currentQuestion = currentQuestion,
                    onAnswerSelected = { selectedOption, isCorrect ->
                        userAnswer = selectedOption
                        message = if (isCorrect) {
                            score++
                            "Correct! Score: $score"
                        } else {
                            "Incorrect. La bonne réponse est: ${currentQuestion.answer}. Score: $score"
                        }

                        // Passer à la question suivante
                        if (currentQuestionIndex < questions.size - 1) {
                            currentQuestionIndex++
                        } else {
                            // Fin du quiz
                            onFinishQuiz(score)
                        }
                    },
                    modifier = Modifier.background(Color.Red)
                )

                Spacer(Modifier.height(16.dp))

                if (userAnswer.isNotEmpty()) {
                    Text(message)
                }
            }
        } else {
            Text("Chargement des données...")
        }
    }
}


@Composable
fun QuizApp() {
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var scores by remember { mutableStateOf(mapOf<String, Int>()) }  // Stockage des scores

    if (selectedCategory == null) {
        CategorySelectionScreen(
            onCategorySelected = { category ->
                selectedCategory = category
            },
            scores = scores
        )
    } else {
        QuizScreen(category = selectedCategory!!) { score ->
            // Une fois le quiz terminé, on met à jour les scores et on revient au menu
            scores = scores + (selectedCategory!! to score)
            selectedCategory = null  // Retour au menu de sélection
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Reponse(
    options: List<String>,
    currentQuestion: QuizQuestion,
    onAnswerSelected: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        options.forEach { option ->
            // Créer une interaction source pour chaque bouton
            val interactionSource = remember { MutableInteractionSource() }
            val isHovered by interactionSource.collectIsHoveredAsState()

            // Animer la couleur du bouton
            val buttonColor by animateColorAsState(
                targetValue = if (isHovered) Color.Red else MaterialTheme.colors.primary
            )

            Button(
                onClick = {
                    // Vérifier si la réponse est correcte
                    val isCorrect = option == currentQuestion.answer

                    // Appeler le callback avec l'option sélectionnée et le résultat
                    onAnswerSelected(option, isCorrect)
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = buttonColor),
                interactionSource = interactionSource,
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Text(
                    text = option,
                    style = MaterialTheme.typography.button
                )
            }
        }
    }
}

@Preview
@Composable
fun HoverableSample() {
    // MutableInteractionSource to track changes of the component's interactions (like "hovered")
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    // the color will change depending on the presence of a hover
    Box(
        modifier =
            Modifier.size(128.dp)
                .background(if (isHovered) Color.Red else Color.Blue)
                .hoverable(interactionSource = interactionSource),
        contentAlignment = Alignment.Center
    ) {
        Text(if (isHovered) "Hovered" else "Unhovered")
    }
}