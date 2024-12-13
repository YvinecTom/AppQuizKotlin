package QuizCreator
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import primaryLight
import secondaryContainerLight

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
        Text("Félicitations pour cette création de quiz !", style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(16.dp))

        Text("Voici son résumé :", style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(16.dp))

        Text("Catégorie: $category", style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(16.dp))

        Text("Questions:", style = MaterialTheme.typography.h5)

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
                        style = MaterialTheme.typography.subtitle1
                    )
                    Text(
                        text = "Réponse correcte: ${question.answer}",
                        style = MaterialTheme.typography.subtitle1
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
            Button(
                onClick = onSave,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = secondaryContainerLight,
                    contentColor = primaryLight
                )
            ){
                Text("Retour à Quiz Mania")
            }
            Button(
                onClick = onReset,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = secondaryContainerLight,
                    contentColor = primaryLight
                )) {
                Text("Recommencer")
            }
        }
    }
}
