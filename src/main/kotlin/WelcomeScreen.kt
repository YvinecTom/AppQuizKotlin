import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WelcomeScreen(onContinue: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var text by remember { mutableStateOf("") }
        Text("Bienvenue dans QuizMania :", style = MaterialTheme.typography.h4, textAlign = TextAlign.Center, fontFamily = nunito)
        Spacer(Modifier.height(16.dp))
        Text("Veuillez entrer votre nom pour jouer !", style = MaterialTheme.typography.h4, textAlign = TextAlign.Center, fontFamily = nunito)
        Spacer(Modifier.height(25.dp))
        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text(
                text = "Entrez votre nom",
                fontFamily = nunito,
                fontSize = 15.sp
            ) },
            modifier = Modifier.fillMaxWidth(0.5f),
            textStyle = TextStyle(fontSize = 20.sp) // Taille de texte fixe
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                if (text.isNotEmpty()) {
                    GlobalState.setUserName(text)
                    onContinue(text)
                }
            }
        ) {
            Text(
                text = "Commencer",
                fontFamily = nunito,
                fontSize = 20.sp
            )
        }
    }
}