package app.krafted.tigeralmanac.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.withTransform
import kotlin.random.Random

private class Petal(
    val xPercent: Float,
    val delayMs: Int,
    val durationMs: Int,
    val size: Float,
    val xDrift: Float,
    val rotationTarget: Float,
    val color: Color,
    val path: Path
)

@Composable
fun CherryPetals(modifier: Modifier = Modifier, count: Int = 8) {
    val petals = remember(count) {
        List(count) {
            val size = Random.nextFloat() * 10f + 8f
            val path = Path().apply {
                moveTo(0f, -size / 2)
                cubicTo(
                    size / 2,
                    -size / 4,
                    size / 2,
                    size / 2,
                    0f,
                    size
                )
                cubicTo(
                    -size / 2,
                    size / 2,
                    -size / 2,
                    -size / 4,
                    0f,
                    -size / 2
                )
                close()
            }
            Petal(
                xPercent = Random.nextFloat() * 1.2f - 0.1f,
                delayMs = Random.nextInt(0, 14000),
                durationMs = Random.nextInt(12000, 22000),
                size = size,
                xDrift = Random.nextFloat() * 80f - 40f,
                rotationTarget = Random.nextFloat() * 720f + 360f,
                color = if (Random.nextBoolean()) Color(0xFFF5B8C8) else Color(0xFFE88AA6),
                path = path
            )
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "cherry_petals")
    val timeMsState = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 60000f,
        animationSpec = infiniteRepeatable(
            animation = tween(60000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "global_time"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        petals.forEach { petal ->
            val totalTime = timeMsState.value + petal.delayMs.toFloat()
            val progress = (totalTime % petal.durationMs.toFloat()) / petal.durationMs.toFloat()
            val currentY = h * (progress * 1.2f) - 20f
            val startX = w * petal.xPercent
            val currentX = startX + (petal.xDrift * progress)

            val rotation = petal.rotationTarget * progress

            withTransform({
                translate(currentX, currentY)
                rotate(rotation)
            }) {
                drawPath(
                    path = petal.path,
                    color = petal.color.copy(alpha = 0.7f)
                )
            }
        }
    }
}
