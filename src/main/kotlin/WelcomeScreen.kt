import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
        Spacer(Modifier.height(35.dp))
        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text(
                text = "Entrez votre nom",
                fontFamily = nunito,
                fontSize = 15.sp,
                color = secondaryLight
            ) },
            modifier = Modifier.fillMaxWidth(0.5f),
            textStyle = TextStyle(fontSize = 20.sp),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = primaryLight,
                unfocusedIndicatorColor = secondaryLight
            )
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                if (text.isNotEmpty()) {
                    GlobalState.setUserName(text)
                    onContinue(text)
                }
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = secondaryContainerLight,
                contentColor = primaryLight // Couleur du texte
            ),
            modifier = Modifier.fillMaxWidth(0.5f) // Optionnel
        ) {
            Text(
                text = "Commencer",
                fontFamily = nunito,
                fontSize = 20.sp
            )
        }
    }
}