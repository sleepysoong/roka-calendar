package com.sleepysoong.rokacalendar.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

@Composable
fun LiquidGlassBackground(
    modifier: Modifier = Modifier,
    accentColor: Color,
    secondaryAccent: Color,
) {
    Box(
        modifier = modifier.background(
            brush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFFF4F6F1),
                    Color(0xFFE6EDF0),
                    Color(0xFFF8F2EA),
                ),
            ),
        ),
    ) {
        GlowOrb(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 8.dp, start = 8.dp),
            color = accentColor.copy(alpha = 0.34f),
            size = 220.dp,
        )
        GlowOrb(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 96.dp, end = 12.dp),
            color = secondaryAccent.copy(alpha = 0.28f),
            size = 250.dp,
        )
        GlowOrb(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 64.dp),
            color = Color.White.copy(alpha = 0.65f),
            size = 320.dp,
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 28.dp, bottom = 180.dp)
                .size(180.dp)
                .clip(CircleShape)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            accentColor.copy(alpha = 0.24f),
                            Color.Transparent,
                        ),
                    ),
                )
                .blur(72.dp),
        )
    }
}

@Composable
fun GlassPanel(
    backdrop: Backdrop,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(30.dp),
    tintColor: Color = Color.White.copy(alpha = 0.16f),
    paddingValues: PaddingValues = PaddingValues(20.dp),
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Column(
        modifier = modifier
            .drawBackdrop(
                backdrop = backdrop,
                shape = { shape },
                effects = {
                    vibrancy()
                    blur(18.dp.toPx())
                    lens(14.dp.toPx(), 26.dp.toPx())
                },
                onDrawSurface = {
                    drawRect(tintColor)
                    drawRect(Color.White.copy(alpha = 0.08f))
                },
            )
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.42f),
                shape = shape,
            )
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = onClick,
                    )
                } else {
                    Modifier
                },
            )
            .padding(paddingValues),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        content = content,
    )
}

@Composable
fun GlassActionButton(
    text: String,
    backdrop: Backdrop,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    tintColor: Color = Color.White.copy(alpha = 0.18f),
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(26.dp)
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = modifier
            .clip(shape)
            .drawBackdrop(
                backdrop = backdrop,
                shape = { shape },
                effects = {
                    vibrancy()
                    blur(16.dp.toPx())
                    lens(12.dp.toPx(), 22.dp.toPx())
                },
                onDrawSurface = {
                    drawRect(
                        if (enabled) tintColor else tintColor.copy(alpha = 0.35f),
                    )
                    drawRect(Color.White.copy(alpha = if (enabled) 0.08f else 0.04f))
                },
            )
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = if (enabled) 0.4f else 0.18f),
                shape = shape,
            )
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            )
            .padding(horizontal = 18.dp, vertical = 15.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = if (enabled) contentColor else contentColor.copy(alpha = 0.45f),
        )
    }
}

@Composable
fun GlassProgressBar(
    progress: Float,
    accentColor: Color,
    secondaryAccent: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(14.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.24f)),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progress.coerceIn(0f, 1f))
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(accentColor, secondaryAccent),
                    ),
                ),
        )
    }
}

@Composable
fun GlassTag(
    text: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.28f))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Spacer(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.82f)),
        )
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.78f),
        )
    }
}

@Composable
private fun BoxScope.GlowOrb(
    modifier: Modifier = Modifier,
    color: Color,
    size: androidx.compose.ui.unit.Dp,
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        color,
                        Color.Transparent,
                    ),
                ),
            )
            .blur(84.dp),
    )
}
