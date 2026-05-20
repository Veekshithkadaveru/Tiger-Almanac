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
    val color: Color
)

@Composable
fun CherryPetals(modifier: Modifier = Modifier, count: Int = 8) {
    val petals = remember(count) {
        List(count) {
            Petal(
                xPercent = Random.nextFloat() * 1.2f - 0.1f,
                delayMs = Random.nextInt(0, 14000),
                durationMs = Random.nextInt(12000, 22000),
                size = Random.nextFloat() * 10f + 8f,
                xDrift = Random.nextFloat() * 80f - 40f,
                rotationTarget = Random.nextFloat() * 720f + 360f,
                color = if (Random.nextBoolean()) Color(0xFFF5B8C8) else Color(0xFFE88AA6)
            )
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "petals")
    val progresses = petals.map { petal ->
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    petal.durationMs,
                    delayMillis = petal.delayMs,
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Restart
            ),
            label = "petal_pos"
        )
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        petals.forEachIndexed { index, petal ->
            val progress = progresses[index].value
            val currentY = h * (progress * 1.2f) - 20f
            val startX = w * petal.xPercent
            val currentX = startX + (petal.xDrift * progress)

            val rotation = petal.rotationTarget * progress

            withTransform({
                translate(currentX, currentY)
                rotate(rotation)
            }) {
                val path = Path().apply {
                    moveTo(0f, -petal.size / 2)
                    cubicTo(
                        petal.size / 2,
                        -petal.size / 4,
                        petal.size / 2,
                        petal.size / 2,
                        0f,
                        petal.size
                    )
                    cubicTo(
                        -petal.size / 2,
                        petal.size / 2,
                        -petal.size / 2,
                        -petal.size / 4,
                        0f,
                        -petal.size / 2
                    )
                    close()
                }
                drawPath(
                    path = path,
                    color = petal.color.copy(alpha = 0.7f)
                )
            }
        }
    }
}
