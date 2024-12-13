package QuizCreator
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn


// Nouvel écran de sélection de quiz
@Composable
fun QuizSelectionScreen(
    onCreateNewQuiz: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Sélection des Quiz", style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(16.dp))

        // Vous pouvez ajouter ici la logique pour lister les quiz existants
        // Par exemple, en scannant le dossier de quiz ou en utilisant une base de données

        Button(onClick = onCreateNewQuiz) {
            Text("Créer un nouveau Quiz")
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

        Text("Questions:", style = MaterialTheme.typography.subtitle1)

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(questions) { question ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = question.question,
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = "Réponse correcte: ${question.answer}",
                        style = MaterialTheme.typography.body2
                    )
                }
                Divider()
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = onSave) {
                Text("Sauvegarder")
            }
            Button(onClick = onReset) {
                Text("Recommencer")
            }
        }
    }
}
