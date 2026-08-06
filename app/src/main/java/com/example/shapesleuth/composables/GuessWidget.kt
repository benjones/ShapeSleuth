package com.example.shapesleuth.composables

import android.util.Log
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.center
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toOffset
import com.example.shapesleuth.data.Card
import com.example.shapesleuth.data.Colors
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin


/*Guess a card
* Imagining it as a a virtual "sphere"
* Horizontally, you can drag 3 shape guesses (fixed pattern)
* Vertically, you can drag 3 pattern guesses (drawn as fixed shapes)
* Color selection ring around the outside
* As you drag horizontally/vertically, it cycles through cards
*/
@Composable
fun GuesserWidget(modifier: Modifier = Modifier,
                  onSelect: (Card)-> Unit){
    var selectedColorIndex by remember{ mutableIntStateOf(0) }

    var draggingColor by remember { mutableStateOf(false)}
    var mouseX by remember { mutableStateOf(0f)}
    var mouseY by remember { mutableStateOf(0f)}

    var colorSelectionWidth = 5f

    val annulusCenterScale = 1/2.5f
    val annulusWidthScale = 1/10f

    Box(modifier
        //.background(Color.Black)
        .drawBehind({
            drawCircle(
                brush = Brush.radialGradient(0.0f to Color.LightGray, 1.0f to Color.DarkGray),
                style = Stroke(size.minDimension * annulusWidthScale),
                radius = size.minDimension * annulusCenterScale
            )
            Colors.entries.forEachIndexed { index, color ->
                val angle = (2 * Math.PI * index) / Colors.entries.size
                drawCircle(
                    brush = SolidColor(color.color),
                    center = Offset(
                        (size.center.x + size.minDimension * annulusCenterScale * cos(angle)).toFloat(),
                        (size.center.y + size.minDimension * annulusCenterScale * sin(angle)).toFloat()
                    ),
                    radius = size.minDimension * annulusWidthScale / 2
                )
            }

            if(draggingColor){
                drawCircle(
                    brush=SolidColor(Color.Black),
                    center = size.center, //Offset(mouseX, mouseY),
                    radius = size.minDimension * annulusWidthScale / 2 + colorSelectionWidth/2,
                    style = Stroke(colorSelectionWidth)
                )
            }

        })
        .pointerInput(Unit) {
            detectDragGestures(
                onDragStart = {offset -> //check if it's in the annulus
                    Log.d("drag start", "offset: $offset")
                    val center = size.center.toOffset()
                    val len = (offset - center).getDistance()
                    val minDim = min(size.width, size.height)
                    val dist = abs(len - minDim*annulusCenterScale)
                    if(dist  <= minDim*annulusWidthScale){
                        draggingColor = true
                    }
                },
                onDrag = { pointerInputChange, dragAmount -> },
                onDragEnd = { draggingColor = false},
                onDragCancel = {draggingColor = false}
            )

        }
    ){


    }
}



@Composable
@Preview
fun GuesserPreview(){
    GuesserWidget(Modifier.size(600.dp), onSelect = {})
}