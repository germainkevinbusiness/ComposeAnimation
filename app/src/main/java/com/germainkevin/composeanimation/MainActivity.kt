package com.germainkevin.composeanimation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.germainkevin.composeanimation.ui.theme.ComposeAnimationTheme
import com.germainkevin.composeanimation.ui.theme.DARK00
import com.germainkevin.composeanimation.ui.theme.DARK01
import com.germainkevin.composeanimation.ui.theme.Orange

/**
 * Playing around with animation here
 * */
@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeAnimationTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {

                    // user post variables
                    val userPostToggle = remember { mutableStateOf(false) }
                    val borderRadius by animateDpAsState(targetValue = if (userPostToggle.value) 30.dp else 0.dp)
                    val backgroundColor by animateColorAsState(if (userPostToggle.value) Color.White else DARK01)
                    val imageVectorColor by animateColorAsState(targetValue = if (userPostToggle.value) DARK00 else Color.White)
                    val firstTextColor by animateColorAsState(targetValue = if (userPostToggle.value) DARK00 else Color.White)
                    val secondTextColor by animateColorAsState(targetValue = if (userPostToggle.value) DARK01 else Color.LightGray)

                    // Simple box variables
                    val simpleBoxToggle = remember { mutableStateOf(false) }
                    val boxState = remember { mutableStateOf(BoxState.SMALL) }
                    if (simpleBoxToggle.value) boxState.value = BoxState.LARGE else BoxState.SMALL
                    val transition =
                        updateTransition(targetState = boxState.value, label = "Box Transition")

                    val boxColor = transition.animateColor(label = "Box Color") {
                        when (it) {
                            BoxState.SMALL -> Color.Blue
                            BoxState.LARGE -> Orange
                        }
                    }
                    val boxSize = transition.animateDp(label = "Box Size") {
                        when (it) {
                            BoxState.SMALL -> 32.dp
                            BoxState.LARGE -> 128.dp
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    ) {
                        // For UserPost
                        Row {
                            Text(text = "Switch UserPost Theme")
                            Spacer(modifier = Modifier.width(8.dp))
                            Switch(
                                checked = userPostToggle.value,
                                onCheckedChange = { userPostToggle.value = it }
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        UserPost(
                            name = "Kevin Germain",
                            imageVectorColor = imageVectorColor,
                            firstTextColor = firstTextColor,
                            secondTextColor = secondTextColor,
                            borderRadius = borderRadius,
                            backgroundColor = backgroundColor
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        // For SimpleBox
                        Row {
                            Text(text = "Animate this SimpleBox")
                            Spacer(modifier = Modifier.width(8.dp))
                            Switch(
                                checked = simpleBoxToggle.value,
                                onCheckedChange = { simpleBoxToggle.value = it }
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        SimpleBox(boxColor = boxColor, boxSize = boxSize)
                    }
                }
            }
        }
    }
}

private enum class BoxState { SMALL, LARGE }

@Composable
private fun SimpleBox(
    boxColor: State<Color>,
    boxSize: State<Dp>
) {
    Box(
        modifier = Modifier
            .background(boxColor.value)
            .size(boxSize.value)
    ) {}
}

@Composable
private fun UserPost(
    name: String,
    imageVectorColor: Color,
    backgroundColor: Color,
    firstTextColor: Color,
    secondTextColor: Color,
    borderRadius: Dp
) {
    Card(
        shape = RoundedCornerShape(borderRadius),
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor),
        elevation = 10.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                imageVector = Icons.Default.Person,
                contentDescription = "",
                colorFilter = ColorFilter.tint(imageVectorColor),
                modifier = Modifier.padding(16.dp)
            )
            Column(modifier = Modifier.padding(end = 16.dp, bottom = 16.dp)) {
                Text(
                    text = buildAnnotatedString {
                        append("Hello, ")
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold,
                                fontStyle = FontStyle.Italic
                            )
                        ) {
                            append(name)
                        }
                        append("!")
                    },
                    color = firstTextColor,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )
                Text(
                    text = "For so long, we have been saying when? When will it happen!? But thankfully today, we know",
                    color = secondTextColor, maxLines = 3, overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeAnimationTheme {
    }
}