package com.example.ui.screens

import android.content.Context
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.db.UserProfileEntity
import com.example.ui.audio.PremiumSoundManager
import com.example.ui.theme.*
import com.example.ui.translation.AppLanguage
import com.example.ui.translation.TranslationHelper
import com.example.ui.viewmodel.FitnessViewModel
import java.util.Locale
import kotlinx.coroutines.delay

@Composable
fun OnboardingScreen(
    viewModel: FitnessViewModel,
    currentUserEmail: String,
    onOnboardingComplete: () -> Unit
) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("fitness_prefs", Context.MODE_PRIVATE) }
    
    // Onboarding Steps State
    var currentStep by remember { mutableStateOf(1) } // 1 to 10
    val totalSteps = 10
    
    // Profile State Variables
    var name by remember { mutableStateOf("") }
    var ageString by remember { mutableStateOf("25") }
    var gender by remember { mutableStateOf("Male") }
    var heightString by remember { mutableStateOf("175") }
    var weightString by remember { mutableStateOf("75") }
    
    // Onboarding Selections
    var selectedGoal by remember { mutableStateOf("Muscle Gain") }
    var selectedActivityLevel by remember { mutableStateOf("Active") }
    var selectedExperience by remember { mutableStateOf("1-6 Months") }
    var selectedReason by remember { mutableStateOf("Look Better") }
    
    // Targets
    var targetWeightString by remember { mutableStateOf("70") }
    var targetTimeframe by remember { mutableStateOf("12 Weeks") }
    
    // Languages Selection
    val appLanguage by viewModel.appLanguage.collectAsState()
    
    // Validation Error Message
    var errorMessage by remember { mutableStateOf("") }
    
    // Sound Effects & Haptics Driver
    LaunchedEffect(currentStep) {
        errorMessage = ""
        PremiumSoundManager.playStandardClick(context)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // 1. Full-screen Edge-to-Edge premium dark gym interior hero background with orange-black theme
        Image(
            painter = painterResource(id = R.drawable.onboarding_bg),
            contentDescription = "Premium Gym Interior Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = androidx.compose.ui.layout.ContentScale.Crop
        )

        // 2. Subtle tangy orange premium ambient lighting overlay over the background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.45f),
                            Color(0xFFFF5722).copy(alpha = 0.12f),
                            Color.Black.copy(alpha = 0.88f)
                        )
                    )
                )
        )

        // 3. Large semi-transparent "YOU VS YOU" watermark text across the background
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "YOU  VS  YOU",
                style = TextStyle(
                    color = Color(0xFFFF5722).copy(alpha = 0.08f),
                    fontSize = 72.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 8.sp,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer(rotationZ = -15f)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // HERO BRAND ELEMENT: Redesigned centered typography brand label with premium "YOU VS YOU" subtext
            if (currentStep == 1) {
                Spacer(modifier = Modifier.height(36.dp))
                
                Text(
                    text = "IT'S YOU",
                    style = TextStyle(
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Black,
                        fontSize = 44.sp,
                        letterSpacing = 6.sp,
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color.White,
                                Color(0xFFFF5722)
                            )
                        ),
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .testTag("onboarding_large_logo_text")
                )
                
                Spacer(modifier = Modifier.height(10.dp))
                
                Text(
                    text = "YOU  VS  YOU",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 5.sp,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .testTag("onboarding_you_vs_you")
                )
            }
            // STEP PROGRESS HUD
            if (currentStep > 1 && currentStep < 10) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = { if (currentStep > 1) currentStep-- },
                        modifier = Modifier
                            .size(36.dp)
                            .background(GymCardGray, CircleShape)
                            .border(1.dp, GymSurfaceGray, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "ATHLETE MILESTONE ASSESSMENT",
                            color = GymTextGray,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "STEP $currentStep OF $totalSteps",
                            color = GymBloodRed,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    Box(modifier = Modifier.size(36.dp)) // Symmetry spacer
                }

                // Smooth linear progress indicator
                LinearProgressIndicator(
                    progress = { currentStep.toFloat() / totalSteps.toFloat() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
                        .height(3.dp)
                        .clip(RoundedCornerShape(1.5.dp)),
                    color = GymBloodRed,
                    trackColor = GymSurfaceGray
                )
            }

            // PRIMARY PAGE CONTENT: Added glassmorphic dark container background for readability
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.58f), RoundedCornerShape(20.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                AnimatedContent(
                    targetState = currentStep,
                    transitionSpec = {
                        if (targetState > initialState) {
                            (slideInHorizontally { width -> width / 2 } + fadeIn())
                                .togetherWith(slideOutHorizontally { width -> -width / 2 } + fadeOut())
                        } else {
                            (slideInHorizontally { width -> -width / 2 } + fadeIn())
                                .togetherWith(slideOutHorizontally { width -> width / 2 } + fadeOut())
                        }
                    },
                    label = "step_transition"
                ) { step ->
                    when (step) {
                        1 -> WelcomeStep(onStart = { currentStep = 2 })
                        2 -> LanguageStep(
                            currentLanguage = appLanguage,
                            onLanguageSelected = { lang ->
                                viewModel.setAppLanguage(lang)
                                currentStep = 3
                            }
                        )
                        3 -> BasicInfoStep(
                            name = name,
                            age = ageString,
                            gender = gender,
                            height = heightString,
                            weight = weightString,
                            onNameChange = { name = it },
                            onAgeChange = { ageString = it },
                            onGenderChange = { gender = it },
                            onHeightChange = { heightString = it },
                            onWeightChange = { weightString = it },
                            errorMessage = errorMessage,
                            onNext = {
                                if (name.isBlank()) {
                                    errorMessage = "Please enter your name."
                                } else if (ageString.toIntOrNull() == null || (ageString.toIntOrNull() ?: 0) <= 0) {
                                    errorMessage = "Please enter a valid age."
                                } else if (heightString.toDoubleOrNull() == null || (heightString.toDoubleOrNull() ?: 0.0) <= 0.0) {
                                    errorMessage = "Please enter a valid height."
                                } else if (weightString.toDoubleOrNull() == null || (weightString.toDoubleOrNull() ?: 0.0) <= 0.0) {
                                    errorMessage = "Please enter a valid weight."
                                } else {
                                    errorMessage = ""
                                    currentStep = 4
                                }
                            }
                        )
                        4 -> GoalStep(
                            selectedGoal = selectedGoal,
                            onGoalSelect = { selectedGoal = it; currentStep = 5 }
                        )
                        5 -> ActivityStep(
                            selectedActivity = selectedActivityLevel,
                            onActivitySelect = { selectedActivityLevel = it; currentStep = 6 }
                        )
                        6 -> ExperienceStep(
                            selectedExp = selectedExperience,
                            onExpSelect = { selectedExperience = it; currentStep = 7 }
                        )
                        7 -> ReasonStep(
                            selectedReason = selectedReason,
                            onReasonSelect = { selectedReason = it; currentStep = 8 }
                        )
                        8 -> TargetStep(
                            targetWeight = targetWeightString,
                            targetTimeframe = targetTimeframe,
                            onTargetWeightChange = { targetWeightString = it },
                            onTimeframeChange = { targetTimeframe = it },
                            errorMessage = errorMessage,
                            onNext = {
                                val tW = targetWeightString.toDoubleOrNull()
                                if (tW == null || tW <= 0.0) {
                                    errorMessage = "Please enter a valid target weight."
                                } else {
                                    errorMessage = ""
                                    currentStep = 9
                                }
                            }
                        )
                        9 -> PlanGenerationStep(
                            name = name,
                            age = ageString.toIntOrNull() ?: 25,
                            gender = gender,
                            height = heightString.toDoubleOrNull() ?: 175.0,
                            currentWeight = weightString.toDoubleOrNull() ?: 75.0,
                            targetWeight = targetWeightString.toDoubleOrNull() ?: 70.0,
                            activityLevel = selectedActivityLevel,
                            goal = selectedGoal,
                            gymExperience = selectedExperience,
                            targetTimeframe = targetTimeframe,
                            onNext = { currentStep = 10 }
                        )
                        10 -> SaveProfileStep(
                            viewModel = viewModel,
                            currentUserEmail = currentUserEmail,
                            name = name,
                            age = ageString.toIntOrNull() ?: 25,
                            gender = gender,
                            height = heightString.toDoubleOrNull() ?: 175.0,
                            weight = weightString.toDoubleOrNull() ?: 75.0,
                            goalWeight = targetWeightString.toDoubleOrNull() ?: 70.0,
                            activity = when (selectedActivityLevel) {
                                "Never Exercise" -> "Sedentary"
                                "Beginner" -> "Sedentary"
                                "Occasionally Active" -> "Active"
                                "Active" -> "Active"
                                "Very Active" -> "Very Active"
                                else -> "Active"
                            },
                            goal = when (selectedGoal) {
                                "General Fitness" -> "Fitness"
                                "Fat Loss" -> "Fat Loss"
                                "Muscle Gain" -> "Muscle Gain"
                                "Strength" -> "Strength"
                                "Athletic Performance" -> "Fitness"
                                else -> selectedGoal
                            },
                            onComplete = {
                                // Write raw onboarding settings to SharedPreferences for custom checks
                                prefs.edit()
                                    .putString("onboarding_experience_$currentUserEmail", selectedExperience)
                                    .putString("onboarding_reason_$currentUserEmail", selectedReason)
                                    .putString("onboarding_timeframe_$currentUserEmail", targetTimeframe)
                                    .apply()
                                
                                onOnboardingComplete()
                            }
                        )
                    }
                }
            }
        }
    }
}

// ==========================================
// STEP 1: WELCOME SCREEN
// ==========================================
@Composable
fun WelcomeStep(onStart: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Welcome to IT'S YOU".uppercase(),
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 2.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "The elitist physical assessment engine. Forge your body metric, sculpt your biomechanical blueprints, and transcend sample boundaries.",
            color = GymTextGray,
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp,
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        Spacer(modifier = Modifier.height(44.dp))

        Button(
            onClick = onStart,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .testTag("onboarding_start_button"),
            colors = ButtonDefaults.buttonColors(containerColor = GymBloodRed),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "BEGIN ASSESSMENT PROTOCOL",
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    fontSize = 13.sp,
                    letterSpacing = 1.sp
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

// ==========================================
// STEP 2: LANGUAGE SELECTION
// ==========================================
@Composable
fun LanguageStep(
    currentLanguage: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "CHOOSE INTERFACE DIALECT",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Sync core system menus, voice directives, and kinesiology coaches.",
            color = GymTextGray,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(30.dp))

        AppLanguage.values().forEach { lang ->
            val isSelected = currentLanguage == lang
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable { onLanguageSelected(lang) }
                    .border(
                        width = if (isSelected) 1.5.dp else 1.dp,
                        color = if (isSelected) GymBloodRed else GymSurfaceGray,
                        shape = RoundedCornerShape(14.dp)
                    )
                    .testTag("lang_select_${lang.name.lowercase()}"),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) GymBloodRed.copy(alpha = 0.12f) else GymCardGray
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = lang.displayName,
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .border(2.dp, if (isSelected) GymBloodRed else GymTextGray, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .background(GymBloodRed, CircleShape)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// STEP 3: BASIC INFORMATION
// ==========================================
@Composable
fun BasicInfoStep(
    name: String,
    age: String,
    gender: String,
    height: String,
    weight: String,
    onNameChange: (String) -> Unit,
    onAgeChange: (String) -> Unit,
    onGenderChange: (String) -> Unit,
    onHeightChange: (String) -> Unit,
    onWeightChange: (String) -> Unit,
    errorMessage: String,
    onNext: () -> Unit
) {
    val scrollState = rememberScrollState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "BIOMETRIC BLUEPRINT ELEMENTS",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.sp,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Critical values used to calibrate BMR, target calorie burn curves, and hydration metrics.",
            color = GymTextGray,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Name
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Athlete Full Name", color = GymTextGray) },
            textStyle = TextStyle(color = Color.White),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("onboarding_name_input"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GymBloodRed,
                unfocusedBorderColor = GymSurfaceGray,
                focusedContainerColor = GymCardGray,
                unfocusedContainerColor = GymCardGray
            )
        )

        // Age
        OutlinedTextField(
            value = age,
            onValueChange = onAgeChange,
            label = { Text("Age (Years)", color = GymTextGray) },
            textStyle = TextStyle(color = Color.White),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("onboarding_age_input"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GymBloodRed,
                unfocusedBorderColor = GymSurfaceGray,
                focusedContainerColor = GymCardGray,
                unfocusedContainerColor = GymCardGray
            )
        )

        // Gender Model View Selection
        Column(modifier = Modifier.fillMaxWidth()) {
            Text("Physiological Model Configuration", color = GymTextGray, fontSize = 11.sp, modifier = Modifier.padding(bottom = 6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                listOf("Male", "Female").forEach { g ->
                    val isSel = gender == g
                    Button(
                        onClick = { onGenderChange(g) },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("onboarding_gender_$g"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSel) GymBloodRed else GymCardGray
                        ),
                        border = BorderStroke(1.dp, if (isSel) GymBloodRed else GymSurfaceGray),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(g, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Height & Weight Rows
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedTextField(
                value = height,
                onValueChange = onHeightChange,
                label = { Text("Height (cm)", color = GymTextGray) },
                textStyle = TextStyle(color = Color.White),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .weight(1f)
                    .testTag("onboarding_height_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GymBloodRed,
                    unfocusedBorderColor = GymSurfaceGray,
                    focusedContainerColor = GymCardGray,
                    unfocusedContainerColor = GymCardGray
                )
            )

            OutlinedTextField(
                value = weight,
                onValueChange = onWeightChange,
                label = { Text("Weight (kg)", color = GymTextGray) },
                textStyle = TextStyle(color = Color.White),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .weight(1f)
                    .testTag("onboarding_weight_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GymBloodRed,
                    unfocusedBorderColor = GymSurfaceGray,
                    focusedContainerColor = GymCardGray,
                    unfocusedContainerColor = GymCardGray
                )
            )
        }

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = GymLightRed,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = onNext,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("onboarding_step3_next"),
            colors = ButtonDefaults.buttonColors(containerColor = GymBloodRed),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("PROCEED TO PHYSICAL GOALS", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
    }
}

// ==========================================
// STEP 4: FITNESS GOAL
// ==========================================
@Composable
fun GoalStep(
    selectedGoal: String,
    onGoalSelect: (String) -> Unit
) {
    val goals = listOf(
        "Fat Loss" to "Reduce lipid index and improve muscular visibility through metabolic conditioning.",
        "Muscle Gain" to "Increase skeletal muscle hypertrophy, loading compounds for density.",
        "Strength" to "Maximize neuromuscular efficiency, heavy barbell compounds and motor coordination.",
        "General Fitness" to "Boost daily capacity, cardiac output, mobility and baseline energy indexes.",
        "Athletic Performance" to "Forge power, lateral velocity, rapid reactive reflexes and elastic bounce."
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "CHOOSE PRIMARY DIRECTIVE",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Calibrates the target macro blueprints and dynamic exercise coaching filters.",
            color = GymTextGray,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(20.dp))

        goals.forEach { (gTitle, gDesc) ->
            val isSel = selectedGoal == gTitle
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clickable { onGoalSelect(gTitle) }
                    .border(
                        width = if (isSel) 1.5.dp else 1.dp,
                        color = if (isSel) GymBloodRed else GymSurfaceGray,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .testTag("goal_card_$gTitle"),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSel) GymBloodRed.copy(alpha = 0.08f) else GymCardGray
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(if (isSel) GymBloodRed else GymSurfaceGray, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when (gTitle) {
                                "Fat Loss" -> Icons.Default.FavoriteBorder
                                "Muscle Gain" -> Icons.Default.Star
                                "Strength" -> Icons.Default.Info
                                "General Fitness" -> Icons.Default.Done
                                else -> Icons.Default.AccountBox
                            },
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(gTitle.uppercase(), color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text(gDesc, color = GymTextGray, fontSize = 10.5.sp, lineHeight = 15.sp)
                    }
                }
            }
        }
    }
}

// ==========================================
// STEP 5: CURRENT ACTIVITY LEVEL
// ==========================================
@Composable
fun ActivityStep(
    selectedActivity: String,
    onActivitySelect: (String) -> Unit
) {
    val options = listOf(
        "Never Exercise" to "Completely sedentary. Desk bound pattern with zero intentional exercises.",
        "Beginner" to "Occasional light movement. Initiating muscular or aerobic habits.",
        "Occasionally Active" to "1-2 training sessions per week. Solid, steady aerobic base.",
        "Active" to "3-4 regular intensive compound sessions per week. Consistent consistency.",
        "Very Active" to "5+ elite high-intensity compound sessions per week. Advanced physical lifestyle."
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "DAILY METABOLIC ACTIVITY LEVEL",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Fine-tunes the exact multiplier used to calculate your Total Daily Energy Expenditure.",
            color = GymTextGray,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(20.dp))

        options.forEach { (aTitle, aDesc) ->
            val isSel = selectedActivity == aTitle
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clickable { onActivitySelect(aTitle) }
                    .border(
                        width = if (isSel) 1.5.dp else 1.dp,
                        color = if (isSel) GymBloodRed else GymSurfaceGray,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .testTag("activity_card_$aTitle"),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSel) GymBloodRed.copy(alpha = 0.08f) else GymCardGray
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(if (isSel) GymBloodRed else GymSurfaceGray, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(aTitle.uppercase(), color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text(aDesc, color = GymTextGray, fontSize = 10.5.sp, lineHeight = 15.sp)
                    }
                }
            }
        }
    }
}

// ==========================================
// STEP 6: GYM EXPERIENCE
// ==========================================
@Composable
fun ExperienceStep(
    selectedExp: String,
    onExpSelect: (String) -> Unit
) {
    val experiences = listOf(
        "Starting Today" to "Complete newcomer. Need highly structured machine and angle onboarding.",
        "Less than 1 Month" to "Familiarized with fundamental gym equipment, launching continuous rhythm.",
        "1-6 Months" to "Consistent compound execution. Mastered solid posture under basic resistance load.",
        "6-12 Months" to "Capable of progressive overload, looking for advanced kinesiology activation cues.",
        "1-3 Years" to "Intermediate compound lifting. Deeply integrated personal splits and targets.",
        "3+ Years" to "Advanced lifter. Fully optimized neuromuscular connections, looking for extreme precision."
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "GYM RESISTANCE EXPERIENCE",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Used to assign recommended workout splits and anatomical movement complexity.",
            color = GymTextGray,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(18.dp))

        experiences.forEach { (eTitle, eDesc) ->
            val isSel = selectedExp == eTitle
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { onExpSelect(eTitle) }
                    .border(
                        width = if (isSel) 1.5.dp else 1.dp,
                        color = if (isSel) GymBloodRed else GymSurfaceGray,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .testTag("exp_card_$eTitle"),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSel) GymBloodRed.copy(alpha = 0.08f) else GymCardGray
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .background(if (isSel) GymBloodRed else GymSurfaceGray, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(eTitle.uppercase(), color = Color.White, fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
                        Text(eDesc, color = GymTextGray, fontSize = 10.sp, lineHeight = 14.sp)
                    }
                }
            }
        }
    }
}

// ==========================================
// STEP 7: MAIN REASON FOR TRAINING
// ==========================================
@Composable
fun ReasonStep(
    selectedReason: String,
    onReasonSelect: (String) -> Unit
) {
    val reasons = listOf(
        "Lose Weight" to "Shed excess fat mass to sculpt a lightweight aesthetic.",
        "Build Muscle" to "Trigger muscular hypertrophy for dense fiber block composition.",
        "Improve Health" to "Strengthen cardiovascular efficiency, glucose profile, and orthopedic health.",
        "Look Better" to "Improve asymmetry, posture structure, and muscle definition.",
        "Increase Strength" to "Upgrade raw neuromuscular recruit, heavy compound PRs.",
        "Sports Performance" to "Optimize explosive power, elasticity, speed, and reaction rate.",
        "Break Bad Habits" to "Substantiate toxic habits with physical, oxygenating gym disciplines.",
        "Improve Discipline" to "Force compliance and grit through progressive physical resistance.",
        "Improve Confidence" to "Forge a powerful mental mindset and pride through undeniable physical work.",
        "Other" to "Custom athlete motivation or specialized wellness directives."
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "WHY TRAIN AT IT'S YOU?",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Provides contextual motivational logs and mental focus cues inside key exercise detail cards.",
            color = GymTextGray,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))

        reasons.forEach { (rTitle, rDesc) ->
            val isSel = selectedReason == rTitle
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { onReasonSelect(rTitle) }
                    .border(
                        width = if (isSel) 1.5.dp else 1.dp,
                        color = if (isSel) GymBloodRed else GymSurfaceGray,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .testTag("reason_card_$rTitle"),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSel) GymBloodRed.copy(alpha = 0.08f) else GymCardGray
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(11.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(if (isSel) GymBloodRed else GymSurfaceGray, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ThumbUp,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(rTitle.uppercase(), color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text(rDesc, color = GymTextGray, fontSize = 9.5.sp, lineHeight = 13.sp)
                    }
                }
            }
        }
    }
}

// ==========================================
// STEP 8: TARGET TIMEFRAME & WEIGHT SETUP
// ==========================================
@Composable
fun TargetStep(
    targetWeight: String,
    targetTimeframe: String,
    onTargetWeightChange: (String) -> Unit,
    onTimeframeChange: (String) -> Unit,
    errorMessage: String,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "TARGET BIOMETRICS DEFINE",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.sp,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Configure your precise weight target and ideal progression window.",
            color = GymTextGray,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
        )

        // Target weight input
        OutlinedTextField(
            value = targetWeight,
            onValueChange = onTargetWeightChange,
            label = { Text("Target Bodyweight (kg)", color = GymTextGray) },
            textStyle = TextStyle(color = Color.White),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("onboarding_target_weight_input"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GymBloodRed,
                unfocusedBorderColor = GymSurfaceGray,
                focusedContainerColor = GymCardGray,
                unfocusedContainerColor = GymCardGray
            )
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Timeframe choice
        Text(
            text = "Strategic Assessment Timeframe",
            color = GymTextGray,
            fontSize = 11.sp,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 6.dp)
        )
        
        Row(
            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("8 Weeks", "12 Weeks", "16 Weeks", "24 Weeks", "52 Weeks").forEach { tf ->
                val isSel = targetTimeframe == tf
                Box(
                    modifier = Modifier
                        .background(if (isSel) GymBloodRed else GymCardGray, RoundedCornerShape(10.dp))
                        .border(1.dp, if (isSel) GymBloodRed else GymSurfaceGray, RoundedCornerShape(10.dp))
                        .clickable { onTimeframeChange(tf) }
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .testTag("tf_btn_$tf")
                ) {
                    Text(
                        tf.uppercase(),
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = errorMessage,
                color = GymLightRed,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(34.dp))

        Button(
            onClick = onNext,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("onboarding_step8_next"),
            colors = ButtonDefaults.buttonColors(containerColor = GymBloodRed),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("GENERATE BIOLOGICAL REPORT", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
    }
}

// ==========================================
// STEP 9: PLAN GENERATION & CALCULATIONS
// ==========================================
@Composable
fun PlanGenerationStep(
    name: String,
    age: Int,
    gender: String,
    height: Double,
    currentWeight: Double,
    targetWeight: Double,
    activityLevel: String,
    goal: String,
    gymExperience: String,
    targetTimeframe: String,
    onNext: () -> Unit
) {
    var isGenerating by remember { mutableStateOf(true) }
    var currentAnalysisText by remember { mutableStateOf("GENERATING DATA SHIELDS...") }

    // Run professional metrics generator logic animation
    LaunchedEffect(Unit) {
        val strings = listOf(
            "🧬 CRUNCHING BODY MASS ANALYSIS...",
            "⚙️ MAPPING LBM HYPERTROPHY CURVE...",
            "⚖️ INTEGRATING COMPOUND LOAD THRESHOLDS...",
            "💧 CALIBRATING TARGET OSMOLAR FLOW...",
            "🦾 OPTIMIZING ANATOMICAL SPLIT ALIGNMENT..."
        )
        for (i in strings.indices) {
            currentAnalysisText = strings[i]
            delay(600)
        }
        isGenerating = false
    }

    // Mathematical calculations
    val heightM = height / 100.0
    val bmi = if (heightM > 0.0) currentWeight / (heightM * heightM) else 22.0
    val minHealthy = 18.5 * (heightM * heightM)
    val maxHealthy = 24.9 * (heightM * heightM)
    
    // Nutrition & Water
    val bmr = if (gender.lowercase() == "female") {
        (10.0 * currentWeight) + (6.25 * height) - (5.0 * age) - 161.0
    } else {
        (10.0 * currentWeight) + (6.25 * height) - (5.0 * age) + 5.0
    }
    
    val actMulti = when (activityLevel) {
        "Never Exercise" -> 1.2
        "Beginner" -> 1.3
        "Occasionally Active" -> 1.4
        "Active" -> 1.55
        "Very Active" -> 1.725
        else -> 1.3
    }
    val tdee = bmr * actMulti
    
    val caloriesTarget = when (goal) {
        "Fat Loss" -> tdee * 0.82
        "Muscle Gain" -> tdee * 1.10
        "Strength" -> tdee * 1.05
        "General Fitness" -> tdee
        "Athletic Performance" -> tdee * 1.05
        else -> tdee
    }
    
    val waterGoal = currentWeight * 0.035
    
    val frequency = when (activityLevel) {
        "Never Exercise" -> "2-3 sessions / week"
        "Beginner" -> "3 sessions / week"
        "Occasionally Active" -> "3-4 sessions / week"
        "Active" -> "4 sessions / week"
        "Very Active" -> "5 sessions / week"
        else -> "3-4 sessions"
    }
    
    val suggestedSplit = when {
        gymExperience == "Starting Today" || gymExperience == "Less than 1 Month" -> "Linear Full Body Compound Base"
        gymExperience == "1-6 Months" -> "Upper / Lower Linear Hypertrophy"
        gymExperience == "6-12 Months" -> "Push-Pull-Legs Target Activation"
        else -> "Arnold Chest-Back Hypertrophy Block"
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (isGenerating) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    color = GymBloodRed,
                    strokeWidth = 3.dp,
                    modifier = Modifier.size(44.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = currentAnalysisText,
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Header success
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Success",
                            tint = GymSuccessGreen,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "METRICA REPORT GENERATED",
                            color = GymSuccessGreen,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Based on $targetTimeframe assessment protocol for $name.",
                            color = GymTextGray,
                            fontSize = 11.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Metric Card 1: BMI & Weight Profile
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = GymCardGray),
                        border = BorderStroke(1.dp, GymSurfaceGray)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("🧬 BIOLOGICAL WEIGHT COMPOSITION", color = GymBloodRed, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text("Current Body Mass Index (BMI)", color = GymTextGray, fontSize = 10.sp)
                                    Text(
                                        text = String.format(Locale.US, "%.1f", bmi),
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text("BMI Status", color = GymTextGray, fontSize = 10.sp)
                                    Text(
                                        text = when {
                                            bmi < 18.5 -> "Underweight"
                                            bmi < 25.0 -> "Healthy Range"
                                            bmi < 30.0 -> "Overweight"
                                            else -> "Obese"
                                        }.uppercase(),
                                        color = when {
                                            bmi < 18.5 -> GymOrangeAccent
                                            bmi < 25.0 -> GymSuccessGreen
                                            else -> GymLightRed
                                        },
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(10.dp))
                            Divider(color = GymSurfaceGray)
                            Spacer(modifier = Modifier.height(10.dp))
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text("Healthy weight range", color = GymTextGray, fontSize = 10.sp)
                                    Text(
                                        text = String.format(Locale.US, "%.1f kg - %.1f kg", minHealthy, maxHealthy),
                                        color = Color.White,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text("Requested target", color = GymTextGray, fontSize = 10.sp)
                                    Text(
                                        text = "$targetWeight kg",
                                        color = Color.White,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }

                // Metric Card 2: Nutrients & Hydration
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = GymCardGray),
                        border = BorderStroke(1.dp, GymSurfaceGray)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("🔥 DAILY CALORIC FUEL & HYDRATION", color = GymBloodRed, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text("Total Daily Calories Required", color = GymTextGray, fontSize = 10.sp)
                                    Text(
                                        text = String.format(Locale.US, "%,d kcal / Day", caloriesTarget.toInt()),
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                    Text(
                                        text = "Base metabolism (BMR) is ${BmrCaloriesLabel(bmr)} kcal.",
                                        color = GymTextGray,
                                        fontSize = 9.sp
                                    )
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text("Liquid Water Goal", color = GymTextGray, fontSize = 10.sp)
                                    Text(
                                        text = String.format(Locale.US, "%.2f Liters / Day", waterGoal),
                                        color = GymBlueAccent,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }

                // Metric Card 3: Training frequency & Suggested Split
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = GymCardGray),
                        border = BorderStroke(1.dp, GymSurfaceGray)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("🦾 WORKOUT VOLUME CONFIGURATION", color = GymBloodRed, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text("Suggested Workout Volume Ratio", color = GymTextGray, fontSize = 10.sp)
                                Text(
                                    text = frequency.uppercase(),
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text("Suggested Kinesiology Training Split Focus", color = GymTextGray, fontSize = 10.sp)
                                Text(
                                    text = suggestedSplit,
                                    color = GymBloodRed,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // Next Step trigger
                item {
                    Spacer(modifier = Modifier.height(6.dp))
                    Button(
                        onClick = onNext,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("onboarding_step9_next"),
                        colors = ButtonDefaults.buttonColors(containerColor = GymBloodRed),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("PROCEED TO ACCOUNT LAUNCH", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

private fun BmrCaloriesLabel(bmr: Double): String {
    return String.format(Locale.US, "%,d", bmr.toInt())
}

// ==========================================
// STEP 10: SAVE PROFILE SCREEN
// ==========================================
@Composable
fun SaveProfileStep(
    viewModel: FitnessViewModel,
    currentUserEmail: String,
    name: String,
    age: Int,
    gender: String,
    height: Double,
    weight: Double,
    goalWeight: Double,
    activity: String,
    goal: String,
    onComplete: () -> Unit
) {
    var isSaving by remember { mutableStateOf(false) }
    var completeMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = "Secure lock",
            tint = GymBloodRed,
            modifier = Modifier.size(48.dp)
        )
        
        Spacer(modifier = Modifier.height(20.dp))
        
        Text(
            text = "STORE PROTOCOL PROFILE",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "All biometrics and target milestones will be permanently synchronized into local Room persistence database layers. Zero personal data leaves the sandbox.",
            color = GymTextGray,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(30.dp))

        if (isSaving) {
            CircularProgressIndicator(
                color = GymBloodRed,
                strokeWidth = 3.dp,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text("SYNCHRONIZING RECONSTRUCTED DATA SHIELD...", color = GymTextGray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        } else {
            Button(
                onClick = {
                    isSaving = true
                    viewModel.updateProfile(
                        name = name,
                        age = age,
                        gender = gender,
                        height = height,
                        weight = weight,
                        goalWeight = goalWeight,
                        activity = activity,
                        goal = goal
                    )
                    completeMessage = "SYSTEM REGISTER SUCCESSFUL."
                    isSaving = false
                    onComplete()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("onboarding_save_profile_button"),
                colors = ButtonDefaults.buttonColors(containerColor = GymBloodRed),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "SAVE PROFILE & UNLEASH SHIELD",
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    fontSize = 13.sp,
                    letterSpacing = 1.sp
                )
            }
        }

        if (completeMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(completeMessage, color = GymSuccessGreen, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)
        }
    }
}
