/**
 * SUKIZY liquid glass layer for Metrolist (GPL-3.0). Original app (C) MetrolistGroup.
 */

package com.metrolist.shared.ui.glass

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp

/**
 * Glass pill used behind the floating navigation items.
 * It never runs the live blur: the bar sits over scrolling content, so a dense tint keeps every frame cheap.
 */
@Composable
fun Modifier.glassNavPill(): Modifier = glassSurface(shape = CircleShape, shadow = 8.dp, liveBlur = false)

/**
 * Lit glass capsule that marks the selected tab.
 * The animated value is only read in the draw and layer phases, so a tab change redraws the capsule
 * without recomposing the bar on every frame.
 */
@Composable
fun Modifier.glassSelectionIndicator(selected: Boolean): Modifier {
    val fraction =
        animateFloatAsState(
            targetValue = if (selected) 1f else 0f,
            animationSpec = spring(dampingRatio = 1f, stiffness = 600f),
            label = "glassSelection",
        )
    val scheme = MaterialTheme.colorScheme
    val dark = scheme.surface.luminance() < 0.5f
    val primary = scheme.primary
    val fill =
        remember(primary, dark) {
            Brush.verticalGradient(
                listOf(
                    primary.copy(alpha = if (dark) 0.36f else 0.26f),
                    primary.copy(alpha = if (dark) 0.18f else 0.12f),
                ),
            )
        }
    val edge = Color.White.copy(alpha = if (dark) 0.35f else 0.70f)
    return this
        .graphicsLayer {
            val scale = 0.97f + 0.03f * fraction.value
            scaleX = scale
            scaleY = scale
        }.drawBehind {
            val value = fraction.value
            if (value > 0.001f) {
                val radius = CornerRadius(size.minDimension / 2f)
                drawRoundRect(brush = fill, cornerRadius = radius, alpha = value)
                drawRoundRect(color = edge, cornerRadius = radius, style = Stroke(1.dp.toPx()), alpha = value)
            }
        }
}
