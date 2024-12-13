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





@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CategorySelectionScreen(
    onCategorySelected: (String) -> Unit,
    scores: Map<String, Int>,
    onQuizCreatorSelected: () -> Unit
) {
    val resourcesDir = File(System.getProperty("user.dir") + "/src/main/resources")
    val quizDir = File(resourcesDir, "quiz")
    val categories = quizDir.listFiles().map { it.nameWithoutExtension }
    val userName = GlobalState.userName.value

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        userName?.let {
            Text(
                "Bonjour $it",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(bottom = 25.dp),
                fontFamily = nunito,
                fontSize = 40.sp
            )
        }
        Text(
            "Veuillez sélectionner une catégorie",
            style = MaterialTheme.typography.h5,
            textAlign = TextAlign.Center,
            fontFamily = nunito,
            fontSize = 30.sp
        )

        Spacer(Modifier.height(16.dp))

        // Utilisation de FlowRow pour un agencement dynamique
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.Center,
            maxItemsInEachRow = 5 // Optionnel, ajustez selon vos besoins
        ) {
            categories.forEach { category ->
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
                            fontSize = 20.sp,
                            fontFamily = nunito
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        Button(
            onClick = onQuizCreatorSelected,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = primaryLight,
                contentColor =  secondaryContainerLight
            )
        ){
            Text("Créer un nouveau quiz")
        }
    }
}





