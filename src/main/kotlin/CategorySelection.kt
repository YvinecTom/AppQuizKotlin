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

@Composable
fun CategorySelectionScreen(
    onCategorySelected: (String) -> Unit,
    scores: Map<String, Int>,
    userName: String?
) {
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        userName?.let {
            Text("Bonjour $it", style = MaterialTheme.typography.h6, modifier = Modifier.padding(bottom = 25.dp), fontFamily = nunito)
        }
        Text("Sélectionnez une catégorie", style = MaterialTheme.typography.h5, textAlign = TextAlign.Center, fontFamily = nunito)
        Spacer(Modifier.height(16.dp))
        val categories = listOf("cinema", "jeu_video", "geographie", "series", "sport")
        Row {
            categories.forEach { category ->
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(10.dp)) {
                    Button(
                        onClick = { onCategorySelected(category) },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = secondaryContainerLight // Remplacez par la couleur de votre choix
                        )
                    ) {
                        Text(
                            category.capitalize(),
                            fontFamily = nunito,
                            color = secondaryLight,
                            fontSize = 20.sp
                        )
                    }
                    scores[category]?.let { Text("Score: $it", fontSize = 16.sp, fontFamily = nunito) }
                }
            }
        }
    }
}