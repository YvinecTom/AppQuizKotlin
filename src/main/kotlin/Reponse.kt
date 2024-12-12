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

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Reponse(
    options: List<String>,
    currentQuestion: QuizQuestion,
    onAnswerSelected: (String, Boolean) -> Unit
) {
    Column {
        Row (
            modifier = Modifier.weight(1f)
        ){
            options.forEach { option ->
                val interactionSource = remember { MutableInteractionSource() }
                val isHovered by interactionSource.collectIsHoveredAsState()
                val buttonColor by animateColorAsState(
                    targetValue = if (isHovered) Color.Red else MaterialTheme.colors.primary
                )
                Button(
                    onClick = { onAnswerSelected(option, option == currentQuestion.answer) },
                    colors = ButtonDefaults.buttonColors(backgroundColor = buttonColor),
                    interactionSource = interactionSource,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(option)
                }
            }
        }

    }
}