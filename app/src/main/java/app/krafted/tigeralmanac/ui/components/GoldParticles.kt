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
import androidx.compose.ui.geometry.Offset
import app.krafted.tigeralmanac.ui.theme.TigerGoldLight
import kotlin.random.Random

private class Particle(
    val xPercent: Float,
    val delayMs: Int,
    val durationMs: Int,
    val size: Float,
    val drift: Float
)

@Composable
fun GoldParticles(modifier: Modifier = Modifier, count: Int = 18) {
    val particles = remember(count) {
        List(count) {
            Particle(
                xPercent = Random.nextFloat(),
                delayMs = Random.nextInt(0, 8000),
                durationMs = Random.nextInt(6000, 14000),
                size = Random.nextFloat() * 3f + 2f,
                drift = Random.nextFloat() * 60f - 30f
            )
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "gold_particles")
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

        particles.forEach { particle ->
            val totalTime = timeMsState.value + particle.delayMs.toFloat()
            val progress = (totalTime % particle.durationMs.toFloat()) / particle.durationMs.toFloat()
            val currentY = h * (1f - progress * 1.1f)
            val startX = w * particle.xPercent
            val currentX = (startX + (particle.drift * progress)).coerceIn(0f, w)

            val alpha = when {
                progress < 0.15f -> progress / 0.15f
                progress > 0.85f -> (1f - progress) / 0.15f
                else -> 1f
            }

            drawCircle(
                color = TigerGoldLight.copy(alpha = alpha * 0.8f),
                radius = particle.size,
                center = Offset(currentX, currentY)
            )
        }
    }
}
