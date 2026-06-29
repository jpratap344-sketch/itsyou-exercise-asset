package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GymBlack
import com.example.ui.theme.GymBloodRed
import com.example.ui.theme.GymCardGray
import com.example.ui.theme.GymDarkRed
import com.example.ui.translation.TranslationHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.*
import kotlin.random.Random

// Represents a mathematical segment of the realistic lightning bolt
data class LightningSegment(
    val start: Offset,
    val end: Offset,
    val strength: Float,
    val isBranch: Boolean = false
)

object LightningGenerator {
    // Generates a highly realistic natural sky lightning bolt using Midpoint Displacement (fractal math)
    fun generate(
        startX: Float,
        startY: Float,
        endX: Float,
        endY: Float,
        displacement: Float,
        minDistance: Float = 15f,
        displacementReduction: Float = 0.52f,
        random: Random = Random(System.nanoTime())
    ): List<LightningSegment> {
        val segments = mutableListOf<LightningSegment>()

        fun subdivide(p1: Offset, p2: Offset, disp: Float, strength: Float, isBranch: Boolean) {
            val dx = p2.x - p1.x
            val dy = p2.y - p1.y
            val distance = sqrt(dx * dx + dy * dy)

            if (distance < minDistance) {
                segments.add(LightningSegment(p1, p2, strength, isBranch))
                return
            }

            // Normal midpoint
            val midX = (p1.x + p2.x) / 2f
            val midY = (p1.y + p2.y) / 2f

            // Perpendicular unit vector for offset displacement
            val perpX = -dy / distance
            val perpY = dx / distance

            // Offset based on normal distribution noise
            val offsetVal = (random.nextFloat() * 2f - 1f) * disp
            val finalMidX = midX + perpX * offsetVal
            val finalMidY = midY + perpY * offsetVal
            val midpoint = Offset(finalMidX, finalMidY)

            // Random aggressive natural branch generation with 18% probability
            if (random.nextFloat() < 0.18f && distance > 60f) {
                val branchAngle = (random.nextFloat() * 2f - 1f) * (PI.toFloat() * 0.25f) // +/- 45 deg
                val length = distance * 0.45f
                
                val dirX = dx / distance
                val dirY = dy / distance
                
                val cosA = cos(branchAngle)
                val sinA = sin(branchAngle)
                
                val branchDirX = dirX * cosA - dirY * sinA
                val branchDirY = dirX * sinA + dirY * cosA
                
                val branchEndX = midpoint.x + branchDirX * length
                val branchEndXNormalized = branchEndX.coerceIn(0f, startX * 2) // avoid clipping limits
                val branchEndY = midpoint.y + branchDirY * length
                
                val branchEnd = Offset(branchEndXNormalized, branchEndY)
                
                // Subdivide the generated branch with lesser strength and higher reduction
                subdivide(midpoint, branchEnd, disp * 0.4f, strength * 0.5f, true)
            }

            // Subdivide left and right segments
            subdivide(p1, midpoint, disp * displacementReduction, strength, isBranch)
            subdivide(midpoint, p2, disp * displacementReduction, strength, isBranch)
        }

        subdivide(
            Offset(startX, startY),
            Offset(endX, endY),
            displacement,
            1.0f,
            false
        )
        return segments
    }
}

/**
 * Custom drawing logic that layers multiple strokes together to create
 * a realistic, natural lightning bolt with an intense blood-red electric glow effect.
 */
@Composable
fun LightningCanvas(
    segments: List<LightningSegment>,
    flickerAlpha: Float,
    glowColor: Color = GymBloodRed,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        if (segments.isEmpty() || flickerAlpha <= 0f) return@Canvas

        // Draw background blood red energy blast glow (very wide, highly transparent blur approximation)
        segments.forEach { seg ->
            val strokeW = if (seg.isBranch) 8f else 18f
            drawLine(
                color = glowColor.copy(alpha = 0.25f * flickerAlpha),
                start = seg.start,
                end = seg.end,
                strokeWidth = strokeW * seg.strength,
                cap = StrokeCap.Round
            )
        }

        // Draw core hot crimson corona
        segments.forEach { seg ->
            val strokeW = if (seg.isBranch) 4f else 8f
            drawLine(
                color = glowColor.copy(alpha = 0.65f * flickerAlpha),
                start = seg.start,
                end = seg.end,
                strokeWidth = strokeW * seg.strength,
                cap = StrokeCap.Round
            )
        }

        // Draw central intense white hot ion channel (super thin, absolute center)
        segments.forEach { seg ->
            val strokeW = if (seg.isBranch) 1.5f else 2.5f
            drawLine(
                color = Color.White.copy(alpha = flickerAlpha),
                start = seg.start,
                end = seg.end,
                strokeWidth = strokeW * seg.strength,
                cap = StrokeCap.Round
            )
        }
    }
}

/**
 * Premium fullscreen Loading screen.
 * - Black background
 * - Powerful natural lightning strike generating dynamically
 * - Pulsing radial deep blood red glow in background
 * - Bold metallic style title:
 *   IT'S YOU
 *   VS YOU
 *   Mindset is Everything
 * - Automatic complete callback once animation finishes (0.85 seconds)
 */
@Composable
fun FullscreenLightningLoader(
    textTitle: String = "IT'S YOU",
    textTagline: String = "VS YOU",
    motivationText: String = "MINDSET IS EVERYTHING",
    onAnimationComplete: () -> Unit = {}
) {
    var strikeSegments1 by remember { mutableStateOf<List<LightningSegment>>(emptyList()) }
    var strikeSegments2 by remember { mutableStateOf<List<LightningSegment>>(emptyList()) }
    
    var timeElapsed by remember { mutableStateOf(0) }
    var scaleTimer by remember { mutableStateOf(0.9f) }
    var textAlpha by remember { mutableStateOf(0f) }
    var showRedCoreRadial by remember { mutableStateOf(0.1f) }

    // Multi-phase lightning strike timeline:
    // 0ms - 150ms: Dark screen fade, text emerges
    // 150ms - 280ms: Strike 1 flashes down, intense red central glow burst!
    // 280ms - 420ms: Residual electric field flickers
    // 420ms - 600ms: Strike 2 (mega) crashes down. Ultra white glow.
    // 600ms - 850ms: Slow fade out of electric current, trigger completion!
    LaunchedEffect(Unit) {
        val scope = this
        textAlpha = 1f
        scaleTimer = 1.0f

        // Time ticker
        scope.launch {
            while (timeElapsed < 850) {
                delay(30)
                timeElapsed += 30
            }
            onAnimationComplete()
        }

        // Generate strike 1
        delay(120)
        strikeSegments1 = LightningGenerator.generate(
            startX = 400f, startY = 10f,
            endX = 550f, endY = 1600f,
            displacement = 180f
        )
        showRedCoreRadial = 0.95f
        delay(80)
        strikeSegments1 = emptyList() // fast flash
        showRedCoreRadial = 0.2f

        delay(160)
        // Generate strike 2 (different coordinates for natural variety)
        strikeSegments2 = LightningGenerator.generate(
            startX = 600f, startY = 10f,
            endX = 450f, endY = 1700f,
            displacement = 220f
        )
        showRedCoreRadial = 1.0f
        delay(110)
        strikeSegments2 = emptyList()
        showRedCoreRadial = 0.3f
        
        delay(50)
        // brief last rumble/flicker
        strikeSegments2 = LightningGenerator.generate(
            startX = 480f, startY = 10f,
            endX = 510f, endY = 1500f,
            displacement = 140f
        )
        delay(60)
        strikeSegments2 = emptyList()
        showRedCoreRadial = 0.1f
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GymBlack),
        contentAlignment = Alignment.Center
    ) {
        // Red glowing energy burst in the background (Pulsing dynamically based on lightning phases)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            GymBloodRed.copy(alpha = 0.65f * showRedCoreRadial),
                            GymDarkRed.copy(alpha = 0.18f * showRedCoreRadial),
                            Color.Transparent
                        ),
                        radius = 1200f
                    )
                )
        )

        // Render Lightning 1 and 2
        if (strikeSegments1.isNotEmpty()) {
            LightningCanvas(segments = strikeSegments1, flickerAlpha = 1.0f)
        }
        if (strikeSegments2.isNotEmpty()) {
            LightningCanvas(segments = strikeSegments2, flickerAlpha = 1.0f)
        }

        // Sparks / Electric Energy floating fragments in background
        Canvas(modifier = Modifier.fillMaxSize()) {
            val sparkCount = if (showRedCoreRadial > 0.5f) 25 else 4
            val rand = Random(System.currentTimeMillis())
            repeat(sparkCount) {
                drawCircle(
                    color = if (rand.nextBoolean()) Color.White else GymBloodRed,
                    radius = rand.nextFloat() * 4f + 2f,
                    center = Offset(
                        rand.nextFloat() * size.width,
                        rand.nextFloat() * size.height
                    ),
                    alpha = rand.nextFloat() * 0.8f + 0.2f
                )
            }
        }

        // Stylish typography display centered
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .scale(scaleTimer)
                .alpha(textAlpha)
                .padding(24.dp)
        ) {
            // "IT'S YOU"
            Text(
                text = textTitle,
                color = Color.White,
                fontSize = 42.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 6.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.White, Color.LightGray)
                        ),
                        shape = CircleShape
                    )
                    .padding(horizontal = 4.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Tagline: "VS YOU"
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .background(GymBloodRed.copy(alpha = 0.85f))
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text(
                    text = textTagline,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 4.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Sub tagline: "Mindset"
            Text(
                text = motivationText,
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 5.sp
            )
        }
    }
}

/**
 * Reusable full screen lightning flash transition coordinator.
 * It sits invisibly on top of the screen content.
 * When `triggerKey` changes to a non-null value:
 * 1. An intense lightning bolt strikes, bringing a quick white/red flash.
 * 2. At the peak of the flash (~220ms), the `onImpact()` block is called (updating underlying states or modals).
 * 3. The lightning bolt and sparks fade out gracefully, completing at 600ms.
 */
@Composable
fun LightningTransitionOverlay(
    triggerKey: Any?,
    onImpact: () -> Unit,
    onCompleted: () -> Unit
) {
    if (triggerKey == null) return

    var activeTrigger by remember { mutableStateOf(triggerKey) }
    var strikeSegments by remember { mutableStateOf<List<LightningSegment>>(emptyList()) }
    var flashStrength by remember { mutableStateOf(0f) }
    var isImpactingDone by remember { mutableStateOf(false) }

    LaunchedEffect(triggerKey) {
        activeTrigger = triggerKey
        isImpactingDone = false
        val scope = this

        // Instant powerful initial flash
        flashStrength = 0.95f
        strikeSegments = LightningGenerator.generate(
            startX = 500f, startY = 15f,
            endX = 520f, endY = 1500f,
            displacement = 200f
        )

        delay(120)
        // Flash reduction
        flashStrength = 0.3f
        delay(60)

        // Heavy impact strike precisely at 200ms
        flashStrength = 1.0f
        strikeSegments = LightningGenerator.generate(
            startX = 450f, startY = 20f,
            endX = 560f, endY = 1600f,
            displacement = 240f
        )
        onImpact() // Update the states below (e.g. opens modal or starts workout)
        isImpactingDone = true

        delay(140)
        flashStrength = 0.4f
        strikeSegments = emptyList() // clear main bolt

        delay(80)
        // quick residual rumble/glow strike
        strikeSegments = LightningGenerator.generate(
            startX = 530f, startY = 15f,
            endX = 490f, endY = 1400f,
            displacement = 150f
        )
        flashStrength = 0.7f
        delay(80)
        strikeSegments = emptyList()
        flashStrength = 0f

        onCompleted()
    }

    // Capture entire screen with flash overlay
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.35f * flashStrength))
    ) {
        // Red radial flash glow
        if (flashStrength > 0f) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                GymBloodRed.copy(alpha = 0.75f * flashStrength),
                                Color.Transparent
                            ),
                            radius = 1000f
                        )
                    )
            )
        }

        // Render standard lightning paths
        LightningCanvas(segments = strikeSegments, flickerAlpha = flashStrength)
    }
}
