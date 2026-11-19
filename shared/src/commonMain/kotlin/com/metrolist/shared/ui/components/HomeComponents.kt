@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.metrolist.shared.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.metrolist.shared.generated.resources.Res
import com.metrolist.shared.generated.resources.see_all
import com.metrolist.shared.ui.components.images.AsyncImage
import com.metrolist.shared.ui.icons.MetrolistIcons
import com.metrolist.shared.ui.utils.artistArtworkShape
import org.jetbrains.compose.resources.stringResource
import kotlin.math.absoluteValue

@Composable
fun GenreCard(
    title: String,
    onClick: () -> Unit,
    shape: Shape,
    modifier: Modifier = Modifier,
) {
    val base = MaterialTheme.colorScheme.secondaryContainer
    // Stable per-title hue so every genre keeps its own colour across recompositions and sessions.
    val accent =
        remember(title) {
            Color.hsv(hue = (title.hashCode().toLong().absoluteValue % 360).toFloat(), saturation = 0.55f, value = 0.9f)
        }
    Surface(
        onClick = onClick,
        modifier = modifier.height(56.dp),
        shape = shape,
        color = base,
        tonalElevation = 1.dp,
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            listOf(accent.copy(alpha = 0.34f).compositeOver(base), base),
                        ),
                    ),
            contentAlignment = Alignment.CenterStart,
        ) {
            Row(
                modifier = Modifier.padding(start = 12.dp, end = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier =
                        Modifier
                            .width(4.dp)
                            .height(22.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(accent),
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
fun SectionTitle(
    title: String,
    modifier: Modifier = Modifier,
    label: String? = null,
    thumbnailUrl: String? = null,
    isArtistEndpoint: Boolean = false,
    artworkSeed: String = thumbnailUrl.orEmpty(),
    seeAllLabel: String? = null,
    showSeeAllLabel: Boolean = true,
    shuffleLabel: String? = null,
    playAllLabel: String? = null,
    onClick: (() -> Unit)? = null,
    onPlayAllClick: (() -> Unit)? = null,
    onShuffleClick: (() -> Unit)? = null,
    isWide: Boolean = false,
) {
    val titleStyle = if (isWide) MaterialTheme.typography.headlineMediumEmphasized else MaterialTheme.typography.headlineSmallEmphasized
    val rowHeight = if (isWide || label != null) 60.dp else 50.dp

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .heightIn(min = rowHeight)
                .then(
                    if (onClick != null) {
                        Modifier.clip(RoundedCornerShape(12.dp)).clickable(
                            role = Role.Button,
                            onClickLabel = seeAllLabel ?: stringResource(Res.string.see_all),
                            onClick = onClick,
                        )
                    } else {
                        Modifier
                    },
                ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (thumbnailUrl != null) {
            AsyncImage(
                url = thumbnailUrl,
                contentDescription = null,
                modifier =
                    Modifier
                        .size(48.dp)
                        .clip(
                            if (isArtistEndpoint) artistArtworkShape(artworkSeed) else RoundedCornerShape(4.dp),
                        ),
                contentScale = ContentScale.Crop,
            )
            Spacer(modifier = Modifier.width(10.dp))
        }

        Column(modifier = Modifier.weight(1f)) {
            if (label != null) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }

            Text(
                text = title,
                style = titleStyle,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.primary,
            )
        }

        if (onClick != null) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (showSeeAllLabel && seeAllLabel != null) {
                    Text(
                        text = seeAllLabel,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                SectionActionButton(
                    icon = MetrolistIcons.Outlined.ChevronRight,
                    contentDescription = null,
                    isWide = isWide,
                )
            }
        } else if (onShuffleClick != null) {
            SectionActionButton(
                icon = MetrolistIcons.Rounded.Shuffle,
                contentDescription = shuffleLabel,
                onClick = onShuffleClick,
                isWide = isWide,
            )
        } else if (onPlayAllClick != null) {
            SectionActionButton(
                icon = MetrolistIcons.Default.PlayArrow,
                contentDescription = playAllLabel,
                onClick = onPlayAllClick,
                isWide = isWide,
            )
        }
    }
}

@Composable
private fun SectionActionButton(
    icon: ImageVector,
    contentDescription: String?,
    onClick: (() -> Unit)? = null,
    isWide: Boolean = false,
) {
    val boxSize = if (isWide) 56.dp else 48.dp
    val surfaceSize = Modifier.size(40.dp, 32.dp)
    val iconSize = 20.dp

    Box(
        modifier = Modifier.size(boxSize),
        contentAlignment = Alignment.Center,
    ) {
        val content: @Composable () -> Unit = {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = contentDescription,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(iconSize),
                )
            }
        }
        if (onClick != null) {
            Surface(
                modifier = surfaceSize,
                shape = RoundedCornerShape(100.dp),
                color = MaterialTheme.colorScheme.primary,
                onClick = onClick,
                content = content,
            )
        } else {
            Surface(
                modifier = surfaceSize,
                shape = RoundedCornerShape(100.dp),
                color = MaterialTheme.colorScheme.primary,
                content = content,
            )
        }
    }
}
