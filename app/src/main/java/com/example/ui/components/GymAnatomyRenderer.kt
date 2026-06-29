package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlin.math.sin


@Composable
fun GymAnatomyRenderer(
    exerciseName: String,
    primaryMuscles: List<String>,
    secondaryMuscles: List<String>,
    equipment: String,
    attachment: String,
    pulleyPosition: String,
    seatAdjustment: String,
    handPlacement: String,
    footPlacement: String,
    modifier: Modifier = Modifier
) {
    var isMale by remember { mutableStateOf(true) }
    var viewMode by remember { mutableStateOf("Front") } // "Front", "Back", "Side"
    
    // Auto-looping motion path to show setup instruction step-by-step
    val infiniteTransition = rememberInfiniteTransition(label = "motion_anim")
    val animationProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "lift_progress"
    )

    // Set view according to standard exercise target muscle
    LaunchedEffect(exerciseName) {
        val prim = primaryMuscles.firstOrNull() ?: ""
        viewMode = when {
            prim.contains("Latissimus") || prim.contains("Trap") || prim.contains("Back") || prim.contains("Triceps") -> "Back"
            prim.contains("Deltoid") -> "Front"
            prim.contains("Glute") || prim.contains("Hamstring") -> "Back"
            else -> "Front"
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, GymSurfaceGray, RoundedCornerShape(12.dp))
            .background(GymCardGray, RoundedCornerShape(12.dp))
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Selector controls for Anatomy Model
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Model Toggle
            Row(
                modifier = Modifier
                    .border(1.dp, GymSurfaceGray, RoundedCornerShape(8.dp))
                    .background(GymBlack, RoundedCornerShape(8.dp))
            ) {
                Box(
                    modifier = Modifier
                        .background(if (isMale) GymBloodRed else Color.Transparent, RoundedCornerShape(8.dp))
                        .clickable { isMale = true }
                        .padding(horizontal = 12.pxToDp(), vertical = 6.pxToDp()),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "MALE MODEL",
                        color = if (isMale) Color.White else GymTextGray,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Box(
                    modifier = Modifier
                        .background(if (!isMale) GymBloodRed else Color.Transparent, RoundedCornerShape(8.dp))
                        .clickable { isMale = false }
                        .padding(horizontal = 12.pxToDp(), vertical = 6.pxToDp()),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "FEMALE MODEL",
                        color = if (!isMale) Color.White else GymTextGray,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // View modes
            Row(
                modifier = Modifier
                    .border(1.dp, GymSurfaceGray, RoundedCornerShape(8.dp))
                    .background(GymBlack, RoundedCornerShape(8.dp))
            ) {
                listOf("Front", "Back", "Side").forEach { mode ->
                    Box(
                        modifier = Modifier
                            .background(if (viewMode == mode) GymDarkRed else Color.Transparent, RoundedCornerShape(8.dp))
                            .clickable { viewMode = mode }
                            .padding(horizontal = 10.pxToDp(), vertical = 6.pxToDp()),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            mode.uppercase(),
                            color = if (viewMode == mode) Color.White else GymTextGray,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))

        // PRIMARY MEDIA BOARD / CANVAS
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .background(GymBlack, RoundedCornerShape(8.dp))
                .border(1.dp, GymSurfaceGray, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            // High-End, Solid Realistic Anatomy Silhouette (absolutely NO stick-figures)
            Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height
                    val centerX = w / 2
                    val centerY = h / 2

                    // Color palette
                    val anatomyLineColor = Color(0xFF444444)
                    val primaryHighlight = GymBloodRed
                    val secondaryHighlight = GymOrangeAccent
                    val machineColor = Color(0xFF00E5FF)
                    val guideColor = GymSuccessGreen

                    // Premium aesthetic grid
                    for (i in 1..8) {
                        val x = w * (i / 9f)
                        drawLine(Color(0xFF111111), Offset(x, 0f), Offset(x, h), strokeWidth = 1f)
                        val y = h * (i / 9f)
                        drawLine(Color(0xFF111111), Offset(0f, y), Offset(w, y), strokeWidth = 1f)
                    }

                    // Proportions and dynamic scales for organic human outline
                    val headRadius = if (isMale) 22f else 20f
                    val shoulderW = if (isMale) 62f else 46f
                    val hipW = if (isMale) 38f else 44f
                    val waistW = if (isMale) 34f else 28f
                    val neckY = centerY - 95f

                    val shoulderL = Offset(centerX - shoulderW, neckY + 14f)
                    val shoulderR = Offset(centerX + shoulderW, neckY + 14f)

                    // 1. Neck Block
                    drawRect(
                        color = Color(0xFF1A1A1A),
                        topLeft = Offset(centerX - 10f, neckY - 14f),
                        size = Size(20f, 16f),
                        style = Fill
                    )
                    drawRect(
                        color = anatomyLineColor,
                        topLeft = Offset(centerX - 10f, neckY - 14f),
                        size = Size(20f, 16f),
                        style = Stroke(1.5f)
                    )

                    // 2. Anatomically Curved Head
                    drawCircle(
                        color = Color(0xFF222222),
                        radius = headRadius,
                        center = Offset(centerX, neckY - headRadius - 12f),
                        style = Fill
                    )
                    drawCircle(
                        color = anatomyLineColor,
                        radius = headRadius,
                        center = Offset(centerX, neckY - headRadius - 12f),
                        style = Stroke(2f)
                    )

                    // 3. Realistic Torso Taper Silhouette Path (Zero stick spinal wires or cross lines)
                    val torsoPath = Path().apply {
                        moveTo(centerX, neckY)
                        // Curve to Left Shoulder Cap
                        quadraticTo(centerX - shoulderW * 0.4f, neckY + 2f, centerX - shoulderW, neckY + 14f)
                        // Under-arm Lat flare
                        quadraticTo(centerX - waistW * 1.15f, centerY - 15f, centerX - waistW, centerY)
                        // Taper back to hip bone point
                        quadraticTo(centerX - waistW * 0.95f, centerY + 15f, centerX - hipW, centerY + 30f)
                        // Leg root boundary
                        lineTo(centerX - 8f, centerY + 30f)
                        lineTo(centerX, centerY + 18f)
                        lineTo(centerX + 8f, centerY + 30f)
                        // Right hip symmetry
                        lineTo(centerX + hipW, centerY + 30f)
                        quadraticTo(centerX + waistW * 0.95f, centerY + 15f, centerX + waistW, centerY)
                        quadraticTo(centerX + waistW * 1.15f, centerY - 15f, centerX + shoulderW, neckY + 14f)
                        quadraticTo(centerX + shoulderW * 0.4f, neckY + 2f, centerX, neckY)
                        close()
                    }
                    // Fill and Outline the Torso (Luxurious, solid athletic body)
                    drawPath(torsoPath, color = Color(0xFF1F1F1F), style = Fill)
                    drawPath(torsoPath, color = Color(0xFF333333), style = Stroke(2.5f))

                    // 4. Muscle highlights and view-mode plates (e.g. Abs, Chest, Lats)
                    when (viewMode) {
                        "Front" -> {
                            // Chest plates
                            val chestHigh = primaryMuscles.any { it.contains("Pect") || it.contains("Chest") }
                            val chestSec = secondaryMuscles.any { it.contains("Pect") || it.contains("Chest") }
                            val chestPaint = if (chestHigh) primaryHighlight else if (chestSec) secondaryHighlight else Color.Transparent

                            if (chestPaint != Color.Transparent) {
                                drawRect(
                                    color = chestPaint.copy(alpha = 0.55f),
                                    topLeft = Offset(centerX - shoulderW + 14f, neckY + 20f),
                                    size = Size(shoulderW - 18f, 32f),
                                    style = Fill
                                )
                                drawRect(
                                    color = chestPaint.copy(alpha = 0.55f),
                                    topLeft = Offset(centerX + 4f, neckY + 20f),
                                    size = Size(shoulderW - 18f, 32f),
                                    style = Fill
                                )
                            }
                            drawRect(color = anatomyLineColor, topLeft = Offset(centerX - shoulderW + 14f, neckY + 20f), size = Size(shoulderW - 18f, 32f), style = Stroke(1.5f))
                            drawRect(color = anatomyLineColor, topLeft = Offset(centerX + 4f, neckY + 20f), size = Size(shoulderW - 18f, 32f), style = Stroke(1.5f))

                            // Abs (6-pack anatomy)
                            val absHigh = primaryMuscles.any { it.contains("Abdom") || it.contains("Abs") }
                            val absColor = if (absHigh) primaryHighlight.copy(alpha = 0.5f) else Color(0xFF151515)
                            for (row in 0..2) {
                                drawRect(color = absColor, topLeft = Offset(centerX - 14f, neckY + 58f + (row * 14f)), size = Size(12f, 10f), style = Fill)
                                drawRect(color = absColor, topLeft = Offset(centerX + 2f, neckY + 58f + (row * 14f)), size = Size(12f, 10f), style = Fill)
                            }

                            // Quads (Fleshy, tapered thigh paths)
                            val quadsHigh = primaryMuscles.any { it.contains("Quad") || it.contains("Leg") }
                            val quadsSec = secondaryMuscles.any { it.contains("Quad") || it.contains("Leg") }
                            val quadColor = if (quadsHigh) primaryHighlight else if (quadsSec) secondaryHighlight else Color(0xFF1F1F1F)

                            // Left Thigh
                            drawPath(
                                path = Path().apply {
                                    moveTo(centerX - hipW + 4f, centerY + 32f)
                                    lineTo(centerX - hipW + 8f, centerY + 110f)
                                    lineTo(centerX - 10f, centerY + 110f)
                                    lineTo(centerX - 8f, centerY + 32f)
                                    close()
                                },
                                color = quadColor.copy(alpha = 0.5f),
                                style = Fill
                            )
                            drawPath(
                                path = Path().apply {
                                    moveTo(centerX - hipW + 4f, centerY + 32f)
                                    lineTo(centerX - hipW + 8f, centerY + 110f)
                                    lineTo(centerX - 10f, centerY + 110f)
                                    lineTo(centerX - 8f, centerY + 32f)
                                    close()
                                },
                                color = anatomyLineColor,
                                style = Stroke(1.5f)
                            )
                            // Right Thigh
                            drawPath(
                                path = Path().apply {
                                    moveTo(centerX + 8f, centerY + 32f)
                                    lineTo(centerX + 10f, centerY + 110f)
                                    lineTo(centerX + hipW - 8f, centerY + 110f)
                                    lineTo(centerX + hipW - 4f, centerY + 32f)
                                    close()
                                },
                                color = quadColor.copy(alpha = 0.5f),
                                style = Fill
                            )
                            drawPath(
                                path = Path().apply {
                                    moveTo(centerX + 8f, centerY + 32f)
                                    lineTo(centerX + 10f, centerY + 110f)
                                    lineTo(centerX + hipW - 8f, centerY + 110f)
                                    lineTo(centerX + hipW - 4f, centerY + 32f)
                                    close()
                                },
                                color = anatomyLineColor,
                                style = Stroke(1.5f)
                            )
                        }
                        "Back" -> {
                            // Latissimus (V-taper aesthetic muscle blocks)
                            val latsHigh = primaryMuscles.any { it.contains("Lat") || it.contains("Back") }
                            val latsSec = secondaryMuscles.any { it.contains("Lat") || it.contains("Back") }
                            val latColor = if (latsHigh) primaryHighlight else if (latsSec) secondaryHighlight else Color(0xFF262626)

                            val latLeft = Path().apply {
                                moveTo(centerX, neckY + 16f)
                                lineTo(centerX - shoulderW + 12f, neckY + 32f)
                                lineTo(centerX - 12f, centerY + 24f)
                                lineTo(centerX, centerY + 24f)
                                close()
                            }
                            val latRight = Path().apply {
                                moveTo(centerX, neckY + 16f)
                                lineTo(centerX + shoulderW - 12f, neckY + 32f)
                                lineTo(centerX + 12f, centerY + 24f)
                                lineTo(centerX, centerY + 24f)
                                close()
                            }
                            drawPath(latLeft, color = latColor.copy(alpha = 0.62f))
                            drawPath(latRight, color = latColor.copy(alpha = 0.62f))
                            drawPath(latLeft, color = anatomyLineColor, style = Stroke(1.5f))
                            drawPath(latRight, color = anatomyLineColor, style = Stroke(1.5f))

                            // Muscular, tapered Calf Blocks (No simple generic circles)
                            val calvesHigh = primaryMuscles.any { it.contains("Calf") || it.contains("Calves") }
                            val calvesPaint = if (calvesHigh) primaryHighlight.copy(alpha = 0.6f) else Color(0xFF202020)

                            val calfLeft = Path().apply {
                                moveTo(centerX - hipW + 8f, centerY + 114f)
                                quadraticTo(centerX - hipW - 4f, centerY + 140f, centerX - hipW + 8f, centerY + 165f)
                                lineTo(centerX - 12f, centerY + 165f)
                                quadraticTo(centerX - 10f, centerY + 140f, centerX - 10f, centerY + 114f)
                                close()
                            }
                            val calfRight = Path().apply {
                                moveTo(centerX + 10f, centerY + 114f)
                                quadraticTo(centerX + 10f, centerY + 140f, centerX + hipW - 8f, centerY + 165f)
                                lineTo(centerX + hipW - 8f, centerY + 165f)
                                quadraticTo(centerX + hipW + 4f, centerY + 140f, centerX + hipW - 8f, centerY + 114f)
                                close()
                            }
                            drawPath(calfLeft, color = calvesPaint, style = Fill)
                            drawPath(calfRight, color = calvesPaint, style = Fill)
                            drawPath(calfLeft, color = anatomyLineColor, style = Stroke(1.5f))
                            drawPath(calfRight, color = anatomyLineColor, style = Stroke(1.5f))
                        }
                        "Side" -> {
                            val sideH = Path().apply {
                                addOval(Rect(Offset(centerX - 24f, centerY - 40f), Size(48f, 80f)))
                            }
                            drawPath(sideH, color = Color(0xFF262626), style = Fill)
                            drawPath(sideH, color = anatomyLineColor, style = Stroke(1.5f))
                        }
                    }

                    // 5. Muscular Upper & Lower Arms
                    val armLOffset = sin(animationProgress * Math.PI.toFloat()) * if (equipment == "Lat Pulldown" || equipment == "Cable Machine" || equipment.contains("Pulley")) 40f else 15f
                    val leftHand = Offset(centerX - shoulderW - 12f, centerY + 22f + armLOffset)
                    val rightHand = Offset(centerX + shoulderW + 12f, centerY + 22f + armLOffset)
                    val leftElbow = Offset(centerX - shoulderW - 16f, neckY + 60f)
                    val rightElbow = Offset(centerX + shoulderW + 16f, neckY + 60f)

                    // Draw organic solid fleshy forearm/bicep limbs (Never thin wire sticks)
                    // Upper Arms
                    drawLine(color = Color(0xFF2A2A2A), start = shoulderL, end = leftElbow, strokeWidth = 22f, cap = StrokeCap.Round)
                    drawLine(color = Color(0xFF2A2A2A), start = shoulderR, end = rightElbow, strokeWidth = 22f, cap = StrokeCap.Round)
                    drawLine(color = anatomyLineColor, start = shoulderL, end = leftElbow, strokeWidth = 4f, cap = StrokeCap.Round)
                    drawLine(color = anatomyLineColor, start = shoulderR, end = rightElbow, strokeWidth = 4f, cap = StrokeCap.Round)

                    // Forearms
                    drawLine(color = Color(0xFF1F1F1F), start = leftElbow, end = leftHand, strokeWidth = 16f, cap = StrokeCap.Round)
                    drawLine(color = Color(0xFF1F1F1F), start = rightElbow, end = rightHand, strokeWidth = 16f, cap = StrokeCap.Round)
                    drawLine(color = anatomyLineColor, start = leftElbow, end = leftHand, strokeWidth = 3f, cap = StrokeCap.Round)
                    drawLine(color = anatomyLineColor, start = rightElbow, end = rightHand, strokeWidth = 3f, cap = StrokeCap.Round)

                    // Highlight targeted arm strain strains (Bicep / Tricep muscle bellies)
                    val bicepHigh = primaryMuscles.any { it.contains("Bicep") || it.contains("Arm") }
                    val tricepHigh = primaryMuscles.any { it.contains("Tricep") }
                    if (bicepHigh) {
                        drawCircle(primaryHighlight, 12f, Offset(centerX - shoulderW - 14f, neckY + 45f))
                        drawCircle(primaryHighlight, 12f, Offset(centerX + shoulderW + 14f, neckY + 45f))
                    } else if (tricepHigh) {
                        drawCircle(primaryHighlight, 12f, Offset(centerX - shoulderW - 18f, neckY + 52f))
                        drawCircle(primaryHighlight, 12f, Offset(centerX + shoulderW + 18f, neckY + 52f))
                    }

                    // 6. Interactive Equipment Render Overlays
                    when (equipment) {
                        "Cable Machine" -> {
                            // Column structures
                            drawLine(Color.Gray, Offset(centerX - 100f, 20f), Offset(centerX - 100f, h - 20f), strokeWidth = 4f)
                            drawLine(Color.Gray, Offset(centerX + 100f, 20f), Offset(centerX + 100f, h - 20f), strokeWidth = 4f)

                            val pulY = if (pulleyPosition.contains("High")) 35f else if (pulleyPosition.contains("Mid")) h/2 else h - 45f
                            drawCircle(machineColor, 8f, Offset(centerX - 100f, pulY))
                            drawCircle(machineColor, 8f, Offset(centerX + 100f, pulY))

                            // Cables
                            drawLine(machineColor, Offset(centerX - 100f, pulY), leftHand, strokeWidth = 2f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 5f)))
                            drawLine(machineColor, Offset(centerX + 100f, pulY), rightHand, strokeWidth = 2f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 5f)))

                            if (attachment == "Rope") {
                                drawLine(Color.White, leftHand, leftHand + Offset(10f, 15f), strokeWidth = 4f)
                                drawLine(Color.White, rightHand, rightHand + Offset(-10f, 15f), strokeWidth = 4f)
                            } else if (attachment == "Straight bar" || attachment == "EZ bar" || attachment.contains("bar")) {
                                drawLine(Color.LightGray, leftHand - Offset(15f, 0f), leftHand + Offset(15f, 0f), strokeWidth = 3f)
                                drawLine(Color.LightGray, rightHand - Offset(15f, 0f), rightHand + Offset(15f, 0f), strokeWidth = 3f)
                            }
                        }
                        "Barbell", "Smith Machine" -> {
                            val barY = leftHand.y
                            drawLine(Color.LightGray, Offset(centerX - 110f, barY), Offset(centerX + 110f, barY), strokeWidth = 6f)
                            drawRect(Color.DarkGray, topLeft = Offset(centerX - 118f, barY - 15f), size = Size(10f, 30f))
                            drawRect(Color.DarkGray, topLeft = Offset(centerX + 108f, barY - 15f), size = Size(10f, 30f))

                            if (equipment == "Smith Machine") {
                                drawLine(Color.Gray, Offset(centerX - 110f, 10f), Offset(centerX - 110f, h - 10f), strokeWidth = 3f)
                                drawLine(Color.Gray, Offset(centerX + 110f, 10f), Offset(centerX + 110f, h - 10f), strokeWidth = 3f)
                            }
                        }
                        "Lat Pulldown" -> {
                            drawLine(Color.DarkGray, Offset(centerX - 120f, 30f), Offset(centerX + 120f, 30f), strokeWidth = 5f)
                            val activeBarY = 30f + (leftHand.y - 30f) * 0.9f
                            drawLine(machineColor, Offset(centerX, 30f), Offset(centerX, activeBarY), strokeWidth = 2f)
                            drawLine(Color.White, Offset(centerX - 95f, activeBarY), Offset(centerX + 95f, activeBarY), strokeWidth = 4f)
                            drawLine(Color.Gray, Offset(centerX - 95f, activeBarY), leftHand, strokeWidth = 1.5f)
                            drawLine(Color.Gray, Offset(centerX + 95f, activeBarY), rightHand, strokeWidth = 1.5f)
                        }
                        "Leg Press", "Hack Squat" -> {
                            drawLine(Color.DarkGray, Offset(centerX - 80f, centerY + 120f), Offset(centerX + 80f, centerY + 120f), strokeWidth = 8f)
                            val sledY = centerY + 30f + sin(animationProgress * Math.PI.toFloat()) * 30f
                            drawLine(machineColor, Offset(centerX - 60f, sledY), Offset(centerX + 60f, sledY), strokeWidth = 5f)
                        }
                    }

                    // 7. Dynamic Action Movement Motion Arrows
                    val arrowSweep = (animationProgress * 30f)
                    val arrowY = centerY - 30f - arrowSweep
                    drawPath(
                        path = Path().apply {
                            moveTo(centerX, arrowY)
                            lineTo(centerX - 10f, arrowY + 12f)
                            lineTo(centerX + 10f, arrowY + 12f)
                            close()
                        },
                        color = primaryHighlight
                    )
                    drawLine(
                        color = primaryHighlight,
                        start = Offset(centerX, arrowY + 10f),
                        end = Offset(centerX, arrowY + 35f),
                        strokeWidth = 3f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f))
                    )

                    // Adjustment indicators
                    if (seatAdjustment != "N/A" && equipment != "N/A") {
                        drawCircle(guideColor, 8f, Offset(centerX, centerY + 65f), style = Stroke(3f))
                        drawCircle(guideColor, 3f, Offset(centerX, centerY + 65f))
                    }
                    drawCircle(guideColor, 6f, leftHand, style = Stroke(2f))
                    drawCircle(guideColor, 6f, rightHand, style = Stroke(2f))
                }

                // Legend indicators
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp)
                        .background(GymBlack.copy(alpha = 0.82f), RoundedCornerShape(4.dp))
                        .padding(6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(8.dp).background(GymBloodRed, RoundedCornerShape(2.dp)))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Primary Strain", color = GymTextLight, fontSize = 9.sp)
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(8.dp).background(GymOrangeAccent, RoundedCornerShape(2.dp)))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Secondary Load", color = GymTextLight, fontSize = 9.sp)
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(8.dp).background(GymSuccessGreen, RoundedCornerShape(2.dp)))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Adjustment Lock", color = GymTextLight, fontSize = 9.sp)
                    }
                }

                // Posture state bubble
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .background(GymDarkRed, RoundedCornerShape(4.dp))
                        .padding(vertical = 4.dp, horizontal = 8.dp)
                ) {
                    Text(
                        if (animationProgress < 0.5f) "START POSTURE" else "CONTRACTION PEAK",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

        Spacer(modifier = Modifier.height(10.dp))

        // Guide parameter breakdown sheet
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            InfoCard(title = "HANDS", value = handPlacement, modifier = Modifier.weight(1f))
            InfoCard(title = "FEET", value = footPlacement, modifier = Modifier.weight(1f))
            if (seatAdjustment != "N/A") {
                InfoCard(title = "SEAT ADJUST", value = seatAdjustment, modifier = Modifier.weight(1f))
            }
        }
    }
}

// Convert Int pixels to Dp safely
@Composable
fun Int.pxToDp(): androidx.compose.ui.unit.Dp {
    val density = androidx.compose.ui.platform.LocalDensity.current
    return with(density) { this@pxToDp.toDp() }
}

@Composable
fun InfoCard(title: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(GymBlack, RoundedCornerShape(6.dp))
            .border(1.dp, GymSurfaceGray, RoundedCornerShape(6.dp))
            .padding(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(title, color = GymBloodRed, fontSize = 9.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(2.dp))
        Text(value, color = GymTextLight, fontSize = 11.sp, maxLines = 1)
    }
}
