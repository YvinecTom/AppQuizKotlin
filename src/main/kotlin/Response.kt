import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import QuizCreator.QuizQuestion
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Response(
    options: List<String>,
    currentQuestion: QuizQuestion,
    onAnswerSelected: (String, Boolean) -> Unit
) {
    Column {
        // Supprimez le Row avec weight et remplacez-le par une Row standard
        Row(
            modifier = Modifier
                .fillMaxWidth() // Assurez-vous que la Row prend toute la largeur
        ) {
            currentQuestion.options.forEach { option ->
                val interactionSource = remember { MutableInteractionSource() }
                val isHovered by interactionSource.collectIsHoveredAsState()
                val buttonColor by animateColorAsState(
                    targetValue = if (isHovered) secondaryContainerLight else tertiaryContainerLight
                )
                Button(
                    onClick = { onAnswerSelected(option, option == currentQuestion.answer) },
                    colors = ButtonDefaults.buttonColors(backgroundColor = buttonColor),
                    interactionSource = interactionSource,
                    modifier = Modifier
                        .weight(1f) // Distribuez l'espace également entre les boutons
                        .padding(8.dp)
                ) {
                    Text(
                        option,
                        modifier = Modifier.padding(6.dp),
                        fontSize = 15.sp
                        )
                }
            }
        }
    }
}