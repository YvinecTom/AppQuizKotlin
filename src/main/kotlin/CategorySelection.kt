import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.io.File

@Composable
fun CategorySelectionScreen(
    onCategorySelected: (String) -> Unit,
    scores: Map<String, Int>,
    userName: String?,
    onQuizCreatorSelected: () -> Unit
) {
    val resourcesDir = File(System.getProperty("user.dir") + "/src/main/resources")
    val quizDir = File(resourcesDir, "quiz")
    val categories = quizDir.listFiles().map { it.nameWithoutExtension }

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        userName?.let {
            Text(
                "Bonjour ENFOIRE $it",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(bottom = 25.dp),
                fontFamily = nunito
            )
        }
        Text(
            "Sélectionnez une catégorie",
            style = MaterialTheme.typography.h5,
            textAlign = TextAlign.Center,
            fontFamily = nunito
        )

        Spacer(Modifier.height(16.dp))

        // Divisez les catégories en lignes de 5
        categories.chunked(5).forEach { categoryRow ->
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                categoryRow.forEach { category ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(10.dp)
                    ) {
                        Button(
                            onClick = { onCategorySelected(category) },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = secondaryContainerLight
                            )
                        ) {
                            Text(
                                category.capitalize(),
                                fontFamily = nunito,
                                color = secondaryLight,
                                fontSize = 20.sp
                            )
                        }
                        scores[category]?.let {
                            Text(
                                "Score: $it",
                                fontSize = 16.sp,
                                fontFamily = nunito
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        Button(onClick = onQuizCreatorSelected) {
            Text("Créer un nouveau quiz")
        }
    }
}


