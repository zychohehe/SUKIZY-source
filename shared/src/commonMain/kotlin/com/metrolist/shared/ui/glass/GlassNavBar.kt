/**
 * SUKIZY liquid glass layer for Metrolist (GPL-3.0). Original app (C) MetrolistGroup.
 */

package com.metrolist.shared.ui.glass

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp

/** Glass pill used behind the floating navigation items. */
@Composable
fun Modifier.glassNavPill(): Modifier = glassSurface(shape = CircleShape, shadow = 14.dp)

/** Lit glass capsule that marks the selected tab. the highlight animates smoothly with a spring. */
@Composable
fun Modifier.glassSelectionIndicator(selected: Boolean): Modifier {
    val fraction by animateFloatAsState(
        targetValue = if (selected) 1f else 0f,
        animationSpec = spring(dampingRatio = 0.8f, stiffness = 500f),
        label = "glassSelection",
    )
    val scheme = MaterialTheme.colorScheme
    val dark = scheme.surface.luminance() < 0.5f
    val fill =
        Brush.verticalGradient(
            listOf(
                scheme.primary.copy(alpha = (if (dark) 0.36f else 0.26f) * fraction),
                scheme.primary.copy(alpha = (if (dark) 0.18f else 0.12f) * fraction),
            ),
        )
    val edge = Color.White.copy(alpha = (if (dark) 0.35f else 0.70f) * fraction)
    return this
        .graphicsLayer {
            val scale = 0.97f + 0.03f * fraction
            scaleX = scale
            scaleY = scale
        }.background(fill, CircleShape)
        .border(1.dp, edge, CircleShape)
}
