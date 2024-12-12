import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import tests.QuizQuestion
import java.io.InputStreamReader
import java.nio.charset.Charset

@Serializable
data class Quiz(
    val category: String,
    val questions: List<QuizQuestion>
)

fun readQuizQuestionsFromFile(filename: String): Quiz {
    val inputStream = object {}.javaClass.getResourceAsStream("quiz/$filename")
        ?: throw IllegalArgumentException("Le fichier $filename n'a pas été trouvé dans les ressources.")
    val reader = InputStreamReader(inputStream, Charset.defaultCharset())
    return Json.decodeFromString(reader.readText())
}