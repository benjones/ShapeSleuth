package com.example.shapesleuth.composables

import android.content.Context
import android.graphics.BitmapShader
import android.graphics.RuntimeShader
import android.graphics.Shader
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.animation.defaultDecayAnimationSpec
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
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

                val brush = when (card.pattern) {

                    Patterns.Gradient -> {
                        Brush.verticalGradient(
                            Pair(0.0f, scaleColor(card.color.color, 1.5f)),
                            Pair(1.0f, scaleColor(card.color.color, 0.1f))
                        )

                    }

                    Patterns.Stars -> {
                        createPatternBrush(
                            context = context,
                            resId = R.drawable.stars,
                            sourceColors = listOf(Color.Black),
                            targetColors = listOf(card.color.color),
                            fallbackColor = lightenColor(card.color.color),
                            scale = 800.0f / size.width
                        )
                    }

                    Patterns.Plaid -> {
                        createPatternBrush(
                            context = context,
                            resId = R.drawable.plaid,
                            sourceColors = listOf(Color.Red, Color.Green),
                            targetColors = listOf(lightenColor(card.color.color, 0.95f), card.color.color),
                            fallbackColor = lightenColor(card.color.color, 0.75f),
                            scale = 1100.0f / size.width
                        )
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
                            style = Stroke(width = size.minDimension * .18f),

                            )
                }
            }
        }
    }
}

fun createPatternBrush(
    context: Context,
    @DrawableRes resId: Int,
    sourceColors: List<Color>,
    targetColors: List<Color>,
    fallbackColor: Color,
    scale: Float
): ShaderBrush {
    val image = AppCompatResources.getDrawable(context, resId)!!.toBitmap().asImageBitmap()
    val maxReplacementColors = 10

    @Language("AGSL")
    val colorReplacementShaderSrc = """
    uniform shader image;
    uniform half4[10] sourceColors; //must be maxReplacementColors.  
    uniform half4[10] targetColors; //TODO make this use that variable somehow
    uniform half4 fallbackColor; // color to use for transparent parts
    uniform int numReplacementColors;
    uniform float scale;

    half4 main(vec2 fragCoord) {
        half4 texColor = image.eval(scale * fragCoord);
        if(texColor.a < .5){
            return fallbackColor;
        }
        for(int i = 0; i < 10; i++){
            if(i >= numReplacementColors){
                break; 
            }
            if(length(texColor.rgb - sourceColors[i].rgb) < .01){
                return half4(targetColors[i].rgb, 1.0);
            }
        }
        //TODO fix: makes orange background look yellow
        return half4(targetColors[0].rgb, 0.5);
    }
""".trimIndent()

    val shader = RuntimeShader(colorReplacementShaderSrc)
    val bitmapShader = BitmapShader(
        image.asAndroidBitmap(),
        Shader.TileMode.REPEAT,
        Shader.TileMode.REPEAT
    )

    if (sourceColors.size != targetColors.size || sourceColors.size > maxReplacementColors) {
        throw RuntimeException("source and target color mismatch, or too many of them")
    }
    shader.setInputShader("image", bitmapShader)

    val sourceUniform = FloatArray(4 * maxReplacementColors)
    sourceColors.flatMap { color -> listOf(color.red, color.green, color.blue, 1.0f) }
        .toFloatArray().copyInto(sourceUniform)

    val targetUniform = FloatArray(4 * maxReplacementColors)
    targetColors.flatMap { color -> listOf(color.red, color.green, color.blue, 1.0f) }
        .toFloatArray().copyInto(targetUniform)

    val fallbackUniform = floatArrayOf(fallbackColor.red, fallbackColor.green, fallbackColor.blue, fallbackColor.alpha)

    shader.setFloatUniform("sourceColors", sourceUniform)
    shader.setFloatUniform("targetColors", targetUniform)
    shader.setFloatUniform("fallbackColor", fallbackUniform)
    shader.setIntUniform("numReplacementColors", sourceColors.size)
    shader.setFloatUniform("scale", scale)
    return ShaderBrush(shader)
}

fun scaleColor(color: Color, scale: Float): Color {
    return Color(
        color.red * scale,
        color.green * scale,
        color.blue * scale,
        alpha = 1.0f
    )
}

fun lightenColor(color: Color, amount: Float = 0.95f): Color {
    val hsv = FloatArray(3)
    android.graphics.Color.colorToHSV(color.toArgb(), hsv)
    hsv[1] = hsv[1] * 0.2f // desaturate
    hsv[2] = amount // lighten
    return Color(android.graphics.Color.HSVToColor(hsv))
}

@Preview
@Composable
fun CardPreview() {
    CardView(Card(Shapes.Spiral, Colors.Blue, Patterns.Stars))
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
    val segments = 50
    val dr = size.minDimension * .5f / segments
    val dTheta = PI.toFloat() * 3.5f / segments
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


