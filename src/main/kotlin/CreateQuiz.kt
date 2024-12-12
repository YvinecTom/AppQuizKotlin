import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CreateQuizScreen(onQuizCreated: () -> Unit) {
    var question by remember { mutableStateOf("") }
    var options by remember { mutableStateOf(listOf("", "", "", "")) }
    var answer by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    // Utilisation d'un Box pour centrer le contenu
    Box(
        modifier = Modifier
            .fillMaxSize() // Remplit tout l'espace disponible
            .padding(16.dp), // Ajoute un padding autour
        contentAlignment = Alignment.Center // Centre le contenu
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center // Centre verticalement
        ) {
            Text(
                "Créer votre propre quiz",
                style = MaterialTheme.typography.h4,
                textAlign = TextAlign.Center,
                fontFamily = nunito
            )
            Spacer(Modifier.height(16.dp))

            // Définir une largeur fixe ou un pourcentage de la largeur de l'écran
            val textFieldWidth = Modifier.width(300.dp) // Largeur fixe de 300 dp

            TextField(
                value = question,
                onValueChange = { question = it },
                label = { Text("Entrez votre question", fontFamily = nunito) },
                modifier = textFieldWidth
                    .background(secondaryContainerLight),
                textStyle = TextStyle(fontSize = 20.sp)
            )
            Spacer(Modifier.height(16.dp))

            options.forEachIndexed { index, option ->
                TextField(
                    value = option,
                    onValueChange = { newOption ->
                        options = options.toMutableList().apply { this[index] = newOption }
                    },
                    modifier = textFieldWidth
                        .background(secondaryContainerLight),
                    label = { Text("Option ${index + 1}", fontFamily = nunito) },
                    textStyle = TextStyle(fontSize = 20.sp)
                )
                Spacer(Modifier.height(8.dp))
            }

            TextField(
                value = answer,
                onValueChange = { answer = it },
                label = { Text("Réponse correcte", fontFamily = nunito) },
                modifier = textFieldWidth
                    .background(secondaryContainerLight),
                textStyle = TextStyle(fontSize = 20.sp)
            )
            Spacer(Modifier.height(16.dp))

            Button(onClick = {
                if (question.isNotEmpty() && answer.isNotEmpty() && options.all { it.isNotEmpty() }) {
                    // Logique pour enregistrer la question, les options et la réponse
                    message = "Quiz créé avec succès !"
                    onQuizCreated() // Retour à l'écran précédent
                } else {
                    message = "Veuillez remplir tous les champs."
                }
            }) {
                Text("Soumettre le Quiz", fontFamily = nunito, fontSize = 20.sp)
            }

            Spacer(Modifier.height(16.dp))
            Text(message, color = Color.Red, fontFamily = nunito)
        }
    }
}