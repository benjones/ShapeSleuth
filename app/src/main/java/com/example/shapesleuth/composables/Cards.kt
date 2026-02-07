package com.example.shapesleuth.composables

import android.graphics.BitmapShader
import android.graphics.RuntimeShader
import android.graphics.Shader
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.LinearGradient
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.toPath
import com.example.shapesleuth.R
import com.example.shapesleuth.data.Card
import com.example.shapesleuth.data.Colors
import com.example.shapesleuth.data.Patterns
import com.example.shapesleuth.data.Shapes
import com.example.shapesleuth.data.makeDeck
import org.intellij.lang.annotations.Language
import kotlin.math.PI
import kotlin.random.Random
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CardView(card: Card, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Card(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
                .align(Alignment.CenterHorizontally),
            horizontalArrangement = Arrangement.Center
        ) {
            Canvas(
                modifier = Modifier
                    .aspectRatio(1.0f)
                    .fillMaxSize()
                    .padding(4.dp)
                    .align(Alignment.CenterVertically),

                contentDescription = "Card:${card}"
            ) {

                val brush = when(card.pattern){

                    Patterns.Gradient -> {
                        Brush.verticalGradient(
                            Pair(0.0f, scaleColor(card.color.color, 1.5f)),
                            Pair(1.0f, scaleColor(card.color.color, 0.1f))
                        )

                    }
                    Patterns.Stars->{
                        val id = R.drawable.stars
                        val bitmap = AppCompatResources.getDrawable(context, id)!!.toBitmap().asImageBitmap()
                        createBlackReplacementBrush(bitmap,card.color.color  )
                    }

                    else -> SolidColor(card.color.color)
                }
                when (card.shape) {
                    Shapes.Square -> drawRect(brush)
                    Shapes.Circle -> drawCircle(brush)
                    Shapes.Triangle -> drawPath(
                        brush = brush,
                        path = RoundedPolygon(
                            vertices = triangleVertices(size)
                        ).toPath().asComposePath()
                    )

                    Shapes.Diamond -> drawPath(
                        brush = brush,
                        path = RoundedPolygon(
                            vertices = diamondVertices(size)
                        ).toPath().asComposePath()
                    )

                    Shapes.Oval -> drawOval(
                        brush = brush,
                        topLeft = Offset(size.width / 8, 0f),
                        size = Size(3 * size.width / 4, size.height)
                    )

                    Shapes.Rainbow -> drawArc(
                        brush = brush,
                        startAngle = 0f,
                        sweepAngle = -180f,
                        useCenter = false,
                        style = Stroke(width = size.minDimension * .4f),
                        size = Size(size.width * .6f, size.height * .8f),
                        topLeft = Offset(size.width * .2f, size.height * .4f)
                    )

                    Shapes.Spiral ->
                        drawPath(
                            brush = brush,
                            path = spiralPath(size),
                            style = Stroke(width = size.minDimension * .1f),

                            )
                }
            }
        }
    }
}

fun scaleColor(color: Color, scale: Float): Color {
    return Color(color.red*scale,
        color.green*scale,
        color.blue*scale,
        alpha = 1.0f)
}

@Preview
@Composable
fun CardPreview() {
    CardView(Card(Shapes.Triangle, Colors.Blue, Patterns.PolkaDot))
}

@Preview
@Composable
fun DeckPreview() {
    val random = Random(7)
    val deck = makeDeck(random).shuffled(random) // Elway

    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Fixed(4)
    ) {
        items(deck) { card ->
            CardView(card, modifier = Modifier.height(96.dp))
        }
    }
}

fun triangleVertices(size: Size) = floatArrayOf(
    0F, size.height,
    size.width / 2, 0F,
    size.width, size.height
)

fun diamondVertices(size: Size) = floatArrayOf(
    0f, size.height / 2,
    size.width / 2, 0F,
    size.width, size.height / 2,
    size.width / 2, size.height
)

fun spiralPath(size: Size): Path {
    val segments = 60
    val dr = size.minDimension * .5f / segments
    val dTheta = PI.toFloat() * 5f / segments
    return Path().apply {
        moveTo(size.width / 2, size.height / 2)
        (0..segments).forEach { i ->
            lineTo(
                size.width / 2 + i * dr * cos(i * dTheta),
                size.height / 2 + i * dr * sin(i * dTheta)
            )
        }
        //close()
    }
}

fun spiralVertices(size: Size): FloatArray {
    val segments = 25
    val dr = size.minDimension * .6f / segments
    val dTheta = 45f
    return (0..segments).map { i ->
        listOf(
            size.width / 2 + i * dr * cos(i * dTheta),
            size.height / 2 + i * dr * sin(i * dTheta)
        )
    }.flatten().toFloatArray()
}


//unused
fun rainbowPolygon(size: Size): Path {
    val roundAmount = size.minDimension * .2f
    val sharpCorner = CornerRounding(0f)
    val rounded = CornerRounding(roundAmount)
    return RoundedPolygon(
        vertices = floatArrayOf(
            0f, size.height,
            size.width / 2, -roundAmount,
            size.width, size.height,
            .8f * size.width, size.height,
            size.width / 2, .2f * size.height - roundAmount,
            .2f * size.width, size.height
        ),
        perVertexRounding = listOf(
            sharpCorner,
            rounded,
            sharpCorner,
            sharpCorner,
            rounded,
            sharpCorner

        )
    ).toPath().asComposePath()
}

@Language("AGSL")
val BLACK_REPLACEMENT_SHADER_SRC = """
    uniform shader image;
    uniform half4 color;

    half4 main(vec2 fragCoord) {
        float scale = 5.0;
        half4 texColor = image.eval(scale*fragCoord);
        // Use a small threshold to check for black, to avoid precision issues
        if (texColor.r < 0.01 && texColor.g < 0.01 && texColor.b < 0.01) {
            return half4(color.rgb, texColor.a);
        }
        return texColor;
    }
""".trimIndent()

fun createBlackReplacementBrush(image: ImageBitmap, color: Color): ShaderBrush {
    val shader = RuntimeShader(BLACK_REPLACEMENT_SHADER_SRC)
    val bitmapShader = BitmapShader(
        image.asAndroidBitmap(),
        Shader.TileMode.REPEAT,
        Shader.TileMode.REPEAT
    )

    shader.setInputShader("image", bitmapShader)
    shader.setFloatUniform("color", color.red, color.green, color.blue, color.alpha)
    return ShaderBrush(shader)
}
