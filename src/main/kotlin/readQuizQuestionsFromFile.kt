import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import QuizCreator.QuizQuestion
import java.io.File
import java.io.InputStreamReader
import java.nio.charset.Charset

@Serializable
data class Quiz(
    val category: String,
    val questions: List<QuizQuestion>
)

fun readQuizQuestionsFromFileDirectly(filename: String): Quiz {
    val file = File("src/main/resources/quiz/$filename")
    if (!file.exists()) {
        throw IllegalArgumentException("Le fichier $filename n'existe pas.")
    }
    val fileContent = file.readText(Charset.defaultCharset())
    return Json.decodeFromString(fileContent)
}