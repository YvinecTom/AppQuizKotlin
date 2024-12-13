package QuizCreator

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

@Serializable
data class QuizData(
    val category: String,
    val questions: List<QuizQuestion>
)

fun saveQuizToFile(category: String, questions: List<QuizQuestion>) {
    println("Sauvegarde du quiz dans le fichier $category.json")
    val quizData = QuizData(category = category, questions = questions)
    val json = Json { prettyPrint = true }
    val jsonString = json.encodeToString(quizData)
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