package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.GymBloodRed

@Composable
fun DumbbellIcon(
    modifier: Modifier = Modifier,
    color: Color = GymBloodRed,
    size: Dp = 24.dp
) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val cy = h / 2f
        val cx = w / 2f

        // Draw central bar
        drawLine(
            color = color,
            start = Offset(cx - w * 0.35f, cy),
            end = Offset(cx + w * 0.35f, cy),
            strokeWidth = h * 0.12f
        )

        // Draw left plate collars
        drawRect(
            color = color,
            topLeft = Offset(cx - w * 0.38f, cy - h * 0.22f),
            size = androidx.compose.ui.geometry.Size(w * 0.08f, h * 0.44f)
        )
        // Left main plates
        drawRoundRect(
            color = color,
            topLeft = Offset(cx - w * 0.48f, cy - h * 0.35f),
            size = androidx.compose.ui.geometry.Size(w * 0.09f, h * 0.70f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f, 4f)
        )

        // Draw right plate collars
        drawRect(
            color = color,
            topLeft = Offset(cx + w * 0.30f, cy - h * 0.22f),
            size = androidx.compose.ui.geometry.Size(w * 0.08f, h * 0.44f)
        )
        // Right main plates
        drawRoundRect(
            color = color,
            topLeft = Offset(cx + w * 0.39f, cy - h * 0.35f),
            size = androidx.compose.ui.geometry.Size(w * 0.09f, h * 0.70f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f, 4f)
        )
    }
}
