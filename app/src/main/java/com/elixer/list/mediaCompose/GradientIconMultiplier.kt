package com.elixer.list.mediaCompose

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradient
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter

@Composable
fun GradientIconMultiplier(painter: Painter, modifier: Modifier, brush: Brush) {
    Box() {
        Icon(
            modifier = modifier.then(
                Modifier
                    .graphicsLayer(alpha = 0.99f)
                    .drawWithCache {
                        onDrawWithContent {
                            drawContent()
                            drawRect(brush, blendMode = BlendMode.SrcAtop)
                        }
                    },
            ),
            painter = painter,
            contentDescription = null,
        )
        Icon(
            modifier = modifier.then(
                Modifier
                    .graphicsLayer(alpha = 0.99f)
                    .drawWithCache {
                        onDrawWithContent {
                            drawContent()
                            drawRect(brush, blendMode = BlendMode.SrcAtop)
                        }
                    },
            ),
            painter = painter,
            contentDescription = null,
        )
        Icon(
            modifier = modifier.then(
                Modifier
                    .graphicsLayer(alpha = 0.99f)
                    .drawWithCache {
                        onDrawWithContent {
                            drawContent()
                            drawRect(brush, blendMode = BlendMode.SrcAtop)
                        }
                    },
            ),
            painter = painter,
            contentDescription = null,
        )
    }
}


val purple = Brush.linearGradient(
    colors = listOf(
        Color(red = 233f / 255f, green = 103f / 255f, blue = 255f / 255f, alpha = 1f, colorSpace = ColorSpaces.DisplayP3),
        Color(red = 124f / 255f, green = 0f / 255f, blue = 255f / 255f, alpha = 1f, colorSpace = ColorSpaces.DisplayP3)
    ),
    start = Offset.Zero,
    end = Offset.Infinite
) as LinearGradient