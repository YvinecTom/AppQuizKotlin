import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import tests.QuizQuestion
import java.io.InputStreamReader
import java.nio.charset.Charset

// Fonction de lecture du fichier JSON
fun readQuizQuestionsFromFile(filename: String): List<QuizQuestion> {
    val inputStream = object {}.javaClass.getResourceAsStream("quiz/$filename")
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