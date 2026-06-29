package com.example.ui.screens

import androidx.compose.animation.*
import android.content.Context
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import com.example.data.db.UserProfileEntity
import com.example.data.db.WaterLogEntity
import com.example.data.db.WeightLogEntity
import com.example.data.db.WorkoutLogEntity
import com.example.data.db.WorkoutPlanEntity
import com.example.data.model.Exercise
import com.example.data.model.ExerciseList
import com.example.ui.components.*
import com.example.ui.viewmodel.ActiveWorkoutState
import com.example.ui.viewmodel.AuthState
import com.example.ui.viewmodel.FitnessViewModel
import com.example.ui.theme.*
import com.example.ui.translation.TranslationHelper
import com.example.ui.translation.AppLanguage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppContainer(
    viewModel: FitnessViewModel,
    modifier: Modifier = Modifier
) {
    val authState by viewModel.authState.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val activeWorkout by viewModel.activeWorkout.collectAsStateWithLifecycle()
    val appLanguage by viewModel.appLanguage.collectAsStateWithLifecycle()

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        com.example.ui.audio.PremiumSoundManager.playSoftThunderRumble(context)
    }

    var showSplash by remember { mutableStateOf(true) }

    val currentUserEmail = when (val auth = authState) {
        is AuthState.LoggedIn -> auth.email
        is AuthState.Guest -> "guest@itsyou.com"
        else -> ""
    }

    val prefs = remember(context) { context.getSharedPreferences("fitness_prefs", android.content.Context.MODE_PRIVATE) }
    var onboardingComplete by remember(currentUserEmail) {
        mutableStateOf(
            if (currentUserEmail.isBlank()) false
            else prefs.getBoolean("onboarding_complete_$currentUserEmail", false)
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(GymBlack)
    ) {
        if (showSplash) {
            FullscreenLightningLoader(
                onAnimationComplete = { showSplash = false }
            )
        } else {
            when (val auth = authState) {
                is AuthState.LoggedOut -> {
                    AuthScreen(viewModel = viewModel)
                }
                else -> {
                    if (!onboardingComplete) {
                        OnboardingScreen(
                            viewModel = viewModel,
                            currentUserEmail = currentUserEmail,
                            onOnboardingComplete = {
                                prefs.edit().putBoolean("onboarding_complete_$currentUserEmail", true).apply()
                                onboardingComplete = true
                            }
                        )
                    } else {
                        // Main Application Layout Container
                        AppNavigationLayout(
                            viewModel = viewModel,
                            userProfile = userProfile,
                            activeWorkout = activeWorkout,
                            onReconfigureProfile = {
                                prefs.edit().putBoolean("onboarding_complete_$currentUserEmail", false).apply()
                                onboardingComplete = false
                            }
                        )
                    }
                }
            }
        }
    }
}

// --- SPLASH SCREEN WITH OFFICIAL BRANDING ---
@Composable
fun SplashScreen() {
    var startAnims by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (startAnims) 1f else 0.85f,
        animationSpec = tween(1200, easing = EaseOutBack),
        label = "scale"
    )
    val alpha by animateFloatAsState(
        targetValue = if (startAnims) 1f else 0f,
        animationSpec = tween(1000, easing = EaseInOutCubic),
        label = "alpha"
    )

    // Breathing luxury red glow backend pulse
    val infiniteTransition = rememberInfiniteTransition(label = "glowPulse")
    val glowScale by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowScale"
    )
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.25f,
        targetValue = 0.60f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )

    LaunchedEffect(Unit) {
        startAnims = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GymBlack),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .scale(scale)
                .alpha(alpha)
                .padding(24.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(310.dp)
            ) {
                // Subtle blood-red pulsing visual glow
                Box(
                    modifier = Modifier
                        .size(240.dp)
                        .scale(glowScale)
                        .alpha(glowAlpha)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    GymBloodRed.copy(alpha = 0.65f),
                                    GymBloodRed.copy(alpha = 0.15f),
                                    Color.Transparent
                                )
                            ),
                            shape = CircleShape
                        )
                )

                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "IT'S YOU Brand Logo",
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "IT'S YOU",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 4.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "FITNESS • HEALTH • LIFE",
                color = GymBloodRed,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 5.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
            Spacer(modifier = Modifier.height(40.dp))
            CircularProgressIndicator(
                color = GymBloodRed,
                strokeWidth = 3.dp,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

// --- AUTHENTICATION SCREEN ---
@Composable
fun AuthScreen(viewModel: FitnessViewModel) {
    var isLogin by remember { mutableStateOf(true) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var isForgotPassword by remember { mutableStateOf(false) }

    var feedbackMessage by remember { mutableStateOf("") }
    var isSuccessFeedback by remember { mutableStateOf(true) }
    var isLoading by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    // Blood-red background atmospheric lightning strike driver
    var backgroundLightningSegments by remember { mutableStateOf<List<LightningSegment>>(emptyList()) }
    var lightningAlpha by remember { mutableStateOf(0f) }

    // Pulse state for background brand logo glow
    val infiniteTransition = rememberInfiniteTransition(label = "logoGlowPulse")
    val logoGlowIntensity by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.75f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "logoGlowIntensity"
    )

    LaunchedEffect(Unit) {
        while (true) {
            delay(kotlin.random.Random.nextLong(3200, 6400))
            // Crack of thunder strike 1
            lightningAlpha = 0.85f
            backgroundLightningSegments = LightningGenerator.generate(
                startX = 100f + kotlin.random.Random.nextInt(700), startY = 10f,
                endX = 100f + kotlin.random.Random.nextInt(700), endY = 1700f,
                displacement = 190f
            )
            delay(110)
            lightningAlpha = 0.25f
            delay(50)
            // rumble flicker
            lightningAlpha = 0.6f
            backgroundLightningSegments = LightningGenerator.generate(
                startX = 100f + kotlin.random.Random.nextInt(700), startY = 10f,
                endX = 100f + kotlin.random.Random.nextInt(700), endY = 1600f,
                displacement = 120f
            )
            delay(90)
            backgroundLightningSegments = emptyList()
            lightningAlpha = 0f
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GymBlack),
        contentAlignment = Alignment.Center
    ) {
        // Deep background dark-red atmospheric radial storm clouds effect
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            GymDarkRed.copy(alpha = 0.35f),
                            GymBlack
                        ),
                        radius = 1100f
                    )
                )
        )

        // Periodic lightnings rendered on top of background but behind contents
        if (backgroundLightningSegments.isNotEmpty()) {
            LightningCanvas(segments = backgroundLightningSegments, flickerAlpha = lightningAlpha)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(GymBloodRed.copy(alpha = 0.15f * lightningAlpha))
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .verticalScroll(rememberScrollState())
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Large center aligned premium brand logo with blood-red glow effect
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(170.dp)
            ) {
                // Pulsing outer energetic corona backdrop
                Box(
                    modifier = Modifier
                        .size(145.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    GymBloodRed.copy(alpha = 0.65f * logoGlowIntensity),
                                    GymDarkRed.copy(alpha = 0.2f * logoGlowIntensity),
                                    Color.Transparent
                                )
                            ),
                            shape = CircleShape
                        )
                )

                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "IT'S YOU Brand Logo",
                    modifier = Modifier
                        .size(130.dp)
                        .clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Premium brand typography headers
            Text(
                text = "IT'S YOU",
                color = Color.White,
                fontSize = 34.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 6.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .background(GymBloodRed)
                    .padding(horizontal = 14.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "VS YOU",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 3.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Mindset  •  Discipline  •  Progress",
                color = GymTextGray,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Sub-box Card holding credentials — Modern Translucent Glassmorphism Design
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("auth_card"),
                colors = CardDefaults.cardColors(containerColor = GymBlack.copy(alpha = 0.75f)),
                border = BorderStroke(
                    width = 1.2.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            GymBloodRed.copy(alpha = 0.8f),
                            GymDarkRed.copy(alpha = 0.25f),
                            GymBloodRed.copy(alpha = 0.8f)
                        )
                    )
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (isForgotPassword) "RESET PASSWORD" else if (isLogin) "WELCOME ATHLETE" else "SIGN UP FOR THE GAINS",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    if (!isLogin && !isForgotPassword) {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Your Name", color = GymTextGray) },
                            textStyle = TextStyle(color = Color.White),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("username_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GymBloodRed,
                                unfocusedBorderColor = GymSurfaceGray.copy(alpha = 0.6f),
                                focusedLabelColor = GymBloodRed,
                                unfocusedLabelColor = GymTextGray,
                                focusedContainerColor = GymBlack.copy(alpha = 0.5f),
                                unfocusedContainerColor = GymBlack.copy(alpha = 0.3f),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email Address", color = GymTextGray) },
                        textStyle = TextStyle(color = Color.White),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("email_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GymBloodRed,
                            unfocusedBorderColor = GymSurfaceGray.copy(alpha = 0.6f),
                            focusedLabelColor = GymBloodRed,
                            unfocusedLabelColor = GymTextGray,
                            focusedContainerColor = GymBlack.copy(alpha = 0.5f),
                            unfocusedContainerColor = GymBlack.copy(alpha = 0.3f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )

                    if (!isForgotPassword) {
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Password", color = GymTextGray) },
                            textStyle = TextStyle(color = Color.White),
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("password_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GymBloodRed,
                                unfocusedBorderColor = GymSurfaceGray.copy(alpha = 0.6f),
                                focusedLabelColor = GymBloodRed,
                                unfocusedLabelColor = GymTextGray,
                                focusedContainerColor = GymBlack.copy(alpha = 0.5f),
                                unfocusedContainerColor = GymBlack.copy(alpha = 0.3f),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    if (feedbackMessage.isNotEmpty()) {
                        Text(
                            feedbackMessage,
                            color = if (isSuccessFeedback) GymSuccessGreen else GymBloodRed,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                    }

                    if (isLoading) {
                        CircularProgressIndicator(color = GymBloodRed, modifier = Modifier.size(24.dp))
                    } else {
                        Button(
                            onClick = {
                                isLoading = true
                                feedbackMessage = ""
                                if (isForgotPassword) {
                                    viewModel.handleForgotPassword(email) { success, msg ->
                                        isLoading = false
                                        isSuccessFeedback = success
                                        feedbackMessage = msg
                                        if (success) {
                                            isForgotPassword = false
                                            isLogin = true
                                        }
                                    }
                                } else if (isLogin) {
                                    viewModel.loginWithEmail(email, password) { success, msg ->
                                        isLoading = false
                                        isSuccessFeedback = success
                                        feedbackMessage = msg
                                    }
                                } else {
                                    viewModel.registerWithEmail(email, password, name) { success, msg ->
                                        isLoading = false
                                        isSuccessFeedback = success
                                        feedbackMessage = msg
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("submit_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = GymBloodRed),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = if (isForgotPassword) "SEND RESET LINK" else if (isLogin) "SIGN IN" else "CREATE ACCOUNT & GROW",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                letterSpacing = 1.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Mode toggles
                    if (!isForgotPassword) {
                        TextButton(onClick = { isForgotPassword = true }) {
                            Text("FORGOT PASSWORD?", color = GymTextGray, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        }
                    } else {
                        TextButton(onClick = { isForgotPassword = false; isLogin = true }) {
                            Text("BACK TO LOGIN", color = GymBloodRed, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }

                    TextButton(
                        onClick = {
                            isForgotPassword = false
                            isLogin = !isLogin
                            feedbackMessage = ""
                        }
                    ) {
                        Text(
                            if (isLogin) "NEW ATHLETE? REGISTER HERE" else "ALREADY A MEMBER? SIGN IN",
                            color = GymBloodRed,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            Text("OR CONNECT AS ELITE", color = GymTextGray, fontSize = 11.sp, letterSpacing = 2.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(14.dp))

            // Premium Google Sign-In and Guest button Row
            Row(
                modifier = Modifier.fillMaxWidth(0.95f),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Google
                Button(
                    onClick = {
                        isLoading = true
                        viewModel.loginWithGoogle { success, msg ->
                            isLoading = false
                            feedbackMessage = msg
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                        .border(1.2.dp, GymSurfaceGray, RoundedCornerShape(10.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = GymBlack.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = "Google Icon",
                        tint = GymBloodRed,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("CONTINUE WITH GOOGLE", color = Color.White, fontSize = 9.5.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 0.5.sp)
                }

                // Guest Mode
                Button(
                    onClick = { viewModel.loginAsGuest() },
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                        .border(1.2.dp, GymBloodRed, RoundedCornerShape(10.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = GymBlack.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = "Guest Play",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("CONTINUE AS GUEST", color = Color.White, fontSize = 9.5.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 0.5.sp)
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

// --- APP HEADER PORTED FROM DESIGN ---
@Composable
fun AppHeader(
    userProfile: UserProfileEntity?,
    onNotificationClick: () -> Unit,
    onProfileClick: () -> Unit,
    notificationCount: Int = 0
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .background(GymBlack)
            .border(width = 1.dp, color = Color.White.copy(alpha = 0.05f))
            .padding(horizontal = 24.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(24.dp)
                    .background(GymBloodRed, RoundedCornerShape(2.dp))
            )
            Text(
                "ITSYOU",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                letterSpacing = (-1).sp
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(contentAlignment = Alignment.TopEnd) {
                IconButton(
                    onClick = onNotificationClick,
                    modifier = Modifier.size(36.dp).testTag("header_notifications_button")
                ) {
                    Icon(
                        imageVector = Icons.Filled.Notifications,
                        contentDescription = "Notifications",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
                if (notificationCount > 0) {
                    Box(
                        modifier = Modifier
                            .offset(x = 2.dp, y = (-2).dp)
                            .background(GymBloodRed, CircleShape)
                            .size(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = notificationCount.toString(),
                            color = Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, GymBloodRed, CircleShape)
                    .background(GymDarkGray)
                    .clickable { onProfileClick() }
                    .testTag("header_profile_avatar_button"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Profile Avatar",
                    tint = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

// --- SECURE AUTHENTICATED SYSTEM CONTAINER ---
@Composable
fun AppNavigationLayout(
    viewModel: FitnessViewModel,
    userProfile: UserProfileEntity?,
    activeWorkout: ActiveWorkoutState?,
    onReconfigureProfile: () -> Unit
) {
    val appLanguage by viewModel.appLanguage.collectAsStateWithLifecycle()
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()
    val unreadNotificationCount = remember(notifications) { notifications.count { !it.isRead } }
    var selectedTab by remember { mutableStateOf("home") } // "home", "planner", "library", "stretch", "analytics", "profile"
    var selectedExerciseForDetail by remember { mutableStateOf<Exercise?>(null) }

    var lightningTriggerKey by remember { mutableStateOf<String?>(null) }
    var lightningImpactAction by remember { mutableStateOf<(() -> Unit)?>(null) }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    var showNotificationCenter by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }
    var showHelpDialog by remember { mutableStateOf(false) }
    var showLicenseDialog by remember { mutableStateOf(false) }

    LaunchedEffect(showNotificationCenter) {
        if (showNotificationCenter) {
            com.example.ui.audio.PremiumSoundManager.playNotificationSound(context)
        }
    }

    // Multi-Language Dialog
    if (showLanguageDialog) {
        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            title = { Text("CHANGE LANGUAGE", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    listOf<Pair<AppLanguage, String>>(
                        AppLanguage.ENGLISH to "English (US)",
                        AppLanguage.HINDI to "हिन्दी (Hindi)",
                        AppLanguage.HINGLISH to "Hinglish (Hindi + English)"
                    ).forEach { (lang, dispName) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.setAppLanguage(lang)
                                    showLanguageDialog = false
                                }
                                .background(if (appLanguage == lang) GymBloodRed else GymBlack, RoundedCornerShape(8.dp))
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(dispName, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            if (appLanguage == lang) {
                                Icon(imageVector = Icons.Filled.Check, contentDescription = "Active", tint = Color.White, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            },
            containerColor = GymCardGray,
            confirmButton = {
                TextButton(onClick = { showLanguageDialog = false }) {
                    Text("CLOSE", color = GymBloodRed, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    // Privacy Policy Hub Overlay
    if (showPrivacyDialog) {
        androidx.compose.ui.window.Dialog(
            onDismissRequest = { showPrivacyDialog = false },
            properties = androidx.compose.ui.window.DialogProperties(
                usePlatformDefaultWidth = false
            )
        ) {
            LegalScreen(
                onDismiss = { showPrivacyDialog = false },
                initialSection = LegalItemType.PRIVACY_POLICY,
                modifier = Modifier.fillMaxSize()
            )
        }
    }

    // Help Dialog
    if (showHelpDialog) {
        AlertDialog(
            onDismissRequest = { showHelpDialog = false },
            title = { Text("HELP & PREMIUM SUPPORT", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("SYSTEM DIAGNOSTICS & HELP", color = GymBloodRed, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Text(
                        "• Kinesiology Live Engine: Active & Validated\n" +
                        "• Sound Manager Decibels: Premium 80%\n" +
                        "• Persistent Handshake Token: Verified\n" +
                        "• Dynamic Database Host: Online (Room SQL)",
                        color = Color.White,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Need deeper customization? Report feedback directly or file tickets securely.", color = GymTextGray, fontSize = 10.sp)
                }
            },
            containerColor = GymCardGray,
            confirmButton = {
                Button(
                    onClick = { showHelpDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = GymBloodRed)
                ) {
                    Text("SUBMIT TICKET", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showHelpDialog = false }) {
                    Text("CLOSE", color = GymTextGray)
                }
            }
        )
    }

    // License & Copyright Hub Overlay
    if (showLicenseDialog) {
        androidx.compose.ui.window.Dialog(
            onDismissRequest = { showLicenseDialog = false },
            properties = androidx.compose.ui.window.DialogProperties(
                usePlatformDefaultWidth = false
            )
        ) {
            LegalScreen(
                onDismiss = { showLicenseDialog = false },
                modifier = Modifier.fillMaxSize()
            )
        }
    }

    // Functional Notification Center Dialog
    if (showNotificationCenter) {
        AlertDialog(
            onDismissRequest = { showNotificationCenter = false },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(imageVector = Icons.Filled.Notifications, contentDescription = "Bell", tint = GymBloodRed, modifier = Modifier.size(20.dp))
                        Text("NOTIFICATION CENTER", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                    if (notifications.any { !it.isRead }) {
                        TextButton(
                            onClick = { 
                                notifications.forEach { viewModel.markNotificationAsRead(it.id) }
                            },
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier.height(24.dp)
                        ) {
                            Text("READ ALL", color = GymBloodRed, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            },
            text = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                    ) {
                        if (notifications.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "No active notifications",
                                    color = GymTextGray,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        } else {
                            notifications.forEach { item ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (item.isRead) GymBlack else GymBlack.copy(alpha = 0.5f)
                                    ),
                                    border = BorderStroke(
                                        1.dp,
                                        if (item.isRead) GymSurfaceGray else GymBloodRed.copy(alpha = 0.4f)
                                    )
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                if (!item.isRead) {
                                                    Box(
                                                        modifier = Modifier
                                                            .size(6.dp)
                                                            .background(GymBloodRed, CircleShape)
                                                    )
                                                }
                                                Text(
                                                    text = item.title,
                                                    color = if (item.isRead) Color.White.copy(alpha = 0.7f) else GymBloodRed,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 11.sp
                                                )
                                            }
                                            
                                            IconButton(
                                                onClick = { viewModel.dismissNotification(item.id) },
                                                modifier = Modifier.size(22.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Filled.Close,
                                                    contentDescription = "Dismiss",
                                                    tint = GymTextGray,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                            }
                                        }
                                        
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = item.text,
                                            color = if (item.isRead) Color.White.copy(alpha = 0.6f) else Color.White,
                                            fontSize = 10.sp,
                                            lineHeight = 13.sp
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Button(
                                                onClick = {
                                                    viewModel.markNotificationAsRead(item.id)
                                                    if (item.buttonLabel == "LETS LIFT!" || item.buttonLabel == "OPEN PLANNER") {
                                                        selectedTab = "planner"
                                                        showNotificationCenter = false
                                                    } else if (item.buttonLabel == "DRINK 500ML") {
                                                        viewModel.logWaterIntake(500.0)
                                                        showNotificationCenter = false
                                                    } else {
                                                        viewModel.dismissNotification(item.id)
                                                    }
                                                },
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = GymBloodRed.copy(alpha = if (item.isRead) 0.1f else 0.2f)
                                                ),
                                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                                                modifier = Modifier.height(26.dp)
                                            ) {
                                                Text(
                                                    text = item.buttonLabel,
                                                    color = if (item.isRead) GymBloodRed.copy(alpha = 0.6f) else GymBloodRed,
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.ExtraBold
                                                )
                                            }
                                            
                                            if (!item.isRead) {
                                                TextButton(
                                                    onClick = { viewModel.markNotificationAsRead(item.id) },
                                                    modifier = Modifier.height(26.dp),
                                                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                                                ) {
                                                    Text(
                                                        "MARK READ",
                                                        color = GymTextGray,
                                                        fontSize = 8.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            },
            containerColor = GymCardGray,
            confirmButton = {
                TextButton(onClick = { 
                    viewModel.clearAllNotifications()
                    showNotificationCenter = false 
                }) {
                    Text("CLEAR ALL", color = GymBloodRed, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            },
            dismissButton = {
                TextButton(onClick = { showNotificationCenter = false }) {
                    Text("CLOSE", color = GymTextGray, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = GymCardGray,
                drawerContentColor = Color.White,
                modifier = Modifier.width(310.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(GymCardGray)
                        .padding(24.dp)
                ) {
                    // Header
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 20.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(54.dp)
                                .clip(CircleShape)
                                .border(2.dp, GymBloodRed, CircleShape)
                                .background(GymBlack),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = "User Profile Picture",
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                userProfile?.name ?: "Gym Athlete",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                userProfile?.email ?: "premium@athlete.com",
                                color = GymTextGray,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Divider(color = GymSurfaceGray, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(16.dp))

                    // Drawer Options list
                    val menuItems = listOf(
                        Triple("Profile", Icons.Filled.Person, "profile"),
                        Triple("Settings", Icons.Filled.Settings, "settings"),
                        Triple("Language", Icons.Filled.Refresh, "language"),
                        Triple("Notifications", Icons.Filled.Notifications, "notifications"),
                        Triple("Contrast Theme", Icons.Filled.ThumbUp, "theme"),
                        Triple("Privacy Policy", Icons.Filled.Lock, "privacy"),
                        Triple("License & Ownership", Icons.Filled.CheckCircle, "license"),
                        Triple("Help & Support", Icons.Filled.Info, "help"),
                        Triple("Logout & Exit", Icons.Filled.ExitToApp, "logout")
                    )

                    menuItems.forEach { (label, icon, action) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    coroutineScope.launch { drawerState.close() }
                                    when (action) {
                                        "profile" -> {
                                            selectedTab = "profile"
                                        }
                                        "settings" -> {
                                            selectedTab = "profile"
                                        }
                                        "language" -> {
                                            showLanguageDialog = true
                                        }
                                        "notifications" -> {
                                            showNotificationCenter = true
                                        }
                                        "theme" -> {
                                            com.example.ui.audio.PremiumSoundManager.playSoftThunderRumble(context)
                                        }
                                        "privacy" -> {
                                            showPrivacyDialog = true
                                        }
                                        "license" -> {
                                            showLicenseDialog = true
                                        }
                                        "help" -> {
                                            showHelpDialog = true
                                        }
                                        "logout" -> {
                                            com.example.ui.audio.PremiumSoundManager.playSoftThunderRumble(context)
                                            viewModel.logout()
                                        }
                                    }
                                }
                                .padding(vertical = 12.dp, horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = label,
                                tint = if (action == "logout") GymBloodRed else Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                label,
                                color = if (action == "logout") GymBloodRed else Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Streak Footer
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(GymBlack, RoundedCornerShape(8.dp))
                            .border(1.dp, GymSurfaceGray, RoundedCornerShape(8.dp))
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("🔥", fontSize = 18.sp)
                            Column {
                                Text("ACTIVE STREAK", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                Text("${userProfile?.streakCount ?: 0} Days Consistent", color = GymTextGray, fontSize = 11.sp)
                            }
                        }
                        Box(
                            modifier = Modifier
                                .background(GymBloodRed.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text("PRO", color = GymBloodRed, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold)
                        }
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                AppHeader(
                    userProfile = userProfile,
                    onNotificationClick = { showNotificationCenter = true },
                    onProfileClick = {
                        coroutineScope.launch { drawerState.open() }
                    },
                    notificationCount = unreadNotificationCount
                )
            },
            bottomBar = {
                NavigationBar(
                    containerColor = GymBlack,
                    modifier = Modifier
                        .border(BorderStroke(1.dp, GymSurfaceGray))
                        .navigationBarsPadding(),
                    tonalElevation = 10.dp
                ) {
                    val tabs = listOf(
                        Triple("home", TranslationHelper.translateUI("HOME"), Icons.Filled.Home),
                        Triple("planner", TranslationHelper.translateUI("PLANNER"), Icons.Filled.Build),
                        Triple("library", TranslationHelper.translateUI("EXERCISES"), Icons.Filled.List),
                        Triple("stretch", TranslationHelper.translateUI("GUIDES"), Icons.Filled.Info),
                        Triple("analytics", TranslationHelper.translateUI("CHARTS"), Icons.Filled.Star),
                        Triple("profile", TranslationHelper.translateUI("PROFILE"), Icons.Filled.Person)
                    )
                    tabs.forEach { (route, label, icon) ->
                        val isSelected = selectedTab == route
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                if (route == "analytics" && selectedTab != "analytics") {
                                    lightningImpactAction = { selectedTab = route }
                                    lightningTriggerKey = java.util.UUID.randomUUID().toString()
                                } else {
                                    selectedTab = route
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = label,
                                    tint = if (isSelected) Color.White else GymTextGray
                                )
                            },
                            label = {
                                Text(
                                    label,
                                    fontSize = 9.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) GymBloodRed else GymTextGray
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = GymDarkRed
                            )
                        )
                    }
                }
            },
            containerColor = GymBlack
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Screen router
                when (selectedTab) {
                    "home" -> HomeScreen(
                        viewModel = viewModel,
                        userProfile = userProfile,
                        onTriggerLightning = { action ->
                            lightningImpactAction = action
                            lightningTriggerKey = java.util.UUID.randomUUID().toString()
                        }
                    )
                    "planner" -> PlannerScreen(
                        viewModel = viewModel,
                        onTriggerLightning = { action ->
                            lightningImpactAction = action
                            lightningTriggerKey = java.util.UUID.randomUUID().toString()
                        }
                    )
                    "library" -> ExercisesScreen(onExerciseSelect = { exerciseItem ->
                        lightningImpactAction = { selectedExerciseForDetail = exerciseItem }
                        lightningTriggerKey = java.util.UUID.randomUUID().toString()
                    })
                    "stretch" -> GuidesScreen()
                    "analytics" -> AnalyticsScreen(
                        viewModel = viewModel,
                        onTriggerLightning = { action ->
                            lightningImpactAction = action
                            lightningTriggerKey = java.util.UUID.randomUUID().toString()
                        }
                    )
                    "profile" -> ProfileScreen(
                        viewModel = viewModel,
                        userProfile = userProfile,
                        onReconfigureProfile = onReconfigureProfile,
                        onViewLicense = { showLicenseDialog = true }
                    )
                }

                // Exercise details full sheet modal overlay
                selectedExerciseForDetail?.let { exercise ->
                    ExerciseDetailModal(
                        exercise = exercise,
                        onDismiss = { selectedExerciseForDetail = null }
                    )
                }

                // Coach workout executing controller overlay!
                activeWorkout?.let { state ->
                    ActiveWorkoutCoachOverlay(
                        activeState = state,
                        viewModel = viewModel,
                        onTriggerLightning = { action ->
                            lightningImpactAction = action
                            lightningTriggerKey = java.util.UUID.randomUUID().toString()
                        }
                    )
                }

                // Lightning Flash transition driver
                LightningTransitionOverlay(
                    triggerKey = lightningTriggerKey,
                    onImpact = {
                        lightningImpactAction?.invoke()
                    },
                    onCompleted = {
                        lightningTriggerKey = null
                        lightningImpactAction = null
                    }
                )
            }
        }
    }
}

// --- SCREEN 1: THE MAIN DASHBOARD (HOME) ---
@Composable
fun HomeScreen(
    viewModel: FitnessViewModel,
    userProfile: UserProfileEntity?,
    onTriggerLightning: (() -> Unit) -> Unit = {}
) {
    val todayString = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()) }
    val todayWater by viewModel.todayWaterLog.collectAsStateWithLifecycle()
    val weightLogs by viewModel.allWeightLogs.collectAsStateWithLifecycle()
    val completedWorkouts by viewModel.allWorkoutLogs.collectAsStateWithLifecycle()

    val currentWeight = userProfile?.weightKg ?: 75.0
    val weightUnit = if (weightLogs.isEmpty()) "$currentWeight kg" else "${weightLogs.last().weightKg} kg"

    // Fun Gym Quotes List
    val motivationQuotes = listOf(
        "ITSYOU against ITSYOU. No short cuts. No surrender.",
        "Your body is a metric machine. Forge it with intensity.",
        "Weakness is a dynamic state of choice. Choose strength.",
        "Squeezing the muscle is where the soul meets physical gold.",
        "The heavy barbell doesn't care about your mood. Lift it."
    )
    val randomQuote = remember(todayString) { motivationQuotes[todayString.hashCode().coerceAtLeast(0) % motivationQuotes.size] }

    val coroutineScope = rememberCoroutineScope()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcomer header element (clean)
        item {
            Column {
                Text(
                    "WELCOME BACK,",
                    color = GymTextGray,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
                Text(
                    (userProfile?.name?.uppercase(Locale.ROOT) ?: "ATHLETE"),
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = (-0.5).sp
                )
            }
        }

        // Daily Overview Hero
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFF1A1A1A), Color(0xFF0A0A0A))
                        )
                    )
                    .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)), RoundedCornerShape(24.dp))
                    .padding(20.dp)
            ) {
                // Gym icon watermark/overlay in upper right (opacity 15%)
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 10.dp, y = (-12).dp)
                ) {
                    DumbbellIcon(size = 80.dp, color = GymBloodRed.copy(alpha = 0.15f))
                }

                Column {
                    Text(
                        "CURRENT STREAK",
                        color = GymBloodRed,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            "${userProfile?.streakCount ?: 0}",
                            color = Color.White,
                            fontSize = 44.sp,
                            fontWeight = FontWeight.Black
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            "DAYS",
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Progress indicator row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Progress track
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(Color.White.copy(alpha = 0.1f))
                        ) {
                            // Filled part
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.75f)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(GymBloodRed)
                            )
                        }
                        Text(
                            text = (userProfile?.goal?.uppercase(Locale.ROOT) ?: "LEG DAY FOCUS"),
                            fontSize = 10.sp,
                            color = Color.White.copy(alpha = 0.6f),
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }
        }

        // Motivational Quote Row
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f)),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.08f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = "Quote Icon",
                        tint = GymBloodRed,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "\"$randomQuote\"",
                        color = GymTextLight,
                        fontSize = 13.sp,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // Action Metrics Hub / Stats Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Calorie Target
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f)),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.08f)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("ESTIMATED BURN", color = GymTextGray, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        val totalBurn = completedWorkouts.sumOf { it.calories }
                        Text("$totalBurn kcal", color = GymBloodRed, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("${completedWorkouts.size} sessions completed", color = GymTextGray, fontSize = 10.sp)
                    }
                }

                // Current logged weight
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f)),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.08f)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("CURRENT METRIC", color = GymTextGray, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(weightUnit, color = GymBloodRed, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(userProfile?.goal?.uppercase(Locale.ROOT) ?: "BULKING", color = GymTextGray, fontSize = 10.sp)
                    }
                }
            }
        }

        // Liquid Water Logger Block
        item {
            val consumed = todayWater?.liters ?: 0.0
            val target = userProfile?.waterGoal ?: 2.5
            val progress = (consumed / target).coerceAtMost(1.0).toFloat()

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f)),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.08f)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(TranslationHelper.translateUI("WATER CONSUMPTION CALCULATOR"), color = GymTextGray, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                String.format(Locale.US, "%.2fL / %.2fL", consumed, target),
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                        Icon(
                            imageVector = Icons.Filled.AddCircle,
                            contentDescription = "Drop",
                            tint = GymBlueAccent,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Progress bar
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = GymBlueAccent,
                        trackColor = GymSurfaceGray
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Quick buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(
                            "+250ml" to 250.0,
                            "+500ml" to 500.0,
                            "+750ml" to 750.0,
                            "+1.0L" to 1000.0
                        ).forEach { (label, ml) ->
                            Button(
                                onClick = { viewModel.logWaterIntake(ml) },
                                colors = ButtonDefaults.buttonColors(containerColor = GymBlack),
                                modifier = Modifier
                                    .weight(1f)
                                    .border(1.dp, GymSurfaceGray, RoundedCornerShape(8.dp))
                                    .height(38.dp),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text(label, color = GymBlueAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // Recommendation card
        item {
            Text(TranslationHelper.translateUI("SUGGESTED DAILY WORKOUT"), color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
        }

        item {
            val suggestedTitle = when (userProfile?.goal) {
                "Fat Loss" -> "High Intensity Conditioning Split"
                "Muscle Gain" -> "Chest + Triceps Hypertrophy"
                "Strength" -> "Heavy Strength Clean-Pull"
                else -> "Intermediate Upper Split"
            }
            val suggestedExercises = when (userProfile?.goal) {
                "Fat Loss" -> "5 exercises | 45 minutes"
                "Muscle Gain" -> "6 exercises | 75 minutes"
                "Strength" -> "5 exercises | 90 minutes"
                else -> "6 exercises | 60 minutes"
            }
            val suggestedDesc = when (userProfile?.goal) {
                "Fat Loss" -> "A fast-paced circuit designed to keep your target heart rate elevated while maintaining high metabolic tone."
                "Muscle Gain" -> "An evidence-based hypertrophy split focused on loading horizontal press angles and target triceps lockout."
                "Strength" -> "High rest periods with explosive barbell/dumbell compounds designed to maximize motor unit recruitment."
                else -> "A balanced upper/lower dynamic resistance design perfect for comprehensive muscular endurance and development."
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f)),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.08f)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(TranslationHelper.translateUI("TODAY'S SELECTION preset"), color = GymBloodRed, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            Text(suggestedTitle, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Text(suggestedExercises, color = GymTextGray, fontSize = 11.sp)
                        }
                        Icon(
                            imageVector = Icons.Filled.PlayArrow,
                            contentDescription = "Go",
                            tint = GymBloodRed,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        suggestedDesc,
                        color = GymTextGray,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}

// --- SCREEN 2: THE PLANS & WORKOUT PLANNER (PLANNER) ---
@Composable
fun PlannerScreen(
    viewModel: FitnessViewModel,
    onTriggerLightning: (() -> Unit) -> Unit = {}
) {
    val plans by viewModel.allWorkoutPlans.collectAsStateWithLifecycle()
    var isCreatingCustom by remember { mutableStateOf(false) }

    // Forms
    var customName by remember { mutableStateOf("") }
    var selectedDifficulty by remember { mutableStateOf("Beginner") }
    var customDuration by remember { mutableStateOf(60) }
    var selectedExercises = remember { mutableStateListOf<String>() }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (!isCreatingCustom) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("SMART WORKOUT PLANNER", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
                        Text("Create custom routines or generate target levels below", color = GymTextGray, fontSize = 11.sp)
                    }
                }
            }

            // Presets generator options
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = GymCardGray),
                    border = BorderStroke(1.dp, GymSurfaceGray)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("AUTOMATIC PLAN GENERATOR", color = GymBloodRed, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(
                                "BEGINNER" to "45m / Light",
                                "INTERMEDIATE" to "75m / Med",
                                "ADVANCED" to "110m / Shred"
                            ).forEach { (mode, detail) ->
                                Button(
                                    onClick = { viewModel.generateQuickPresetPlan(mode) },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(containerColor = GymBlack),
                                    border = BorderStroke(1.dp, GymSurfaceGray)
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(mode, color = GymBloodRed, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                        Text(detail, color = Color.White, fontSize = 8.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Create plan trigger buttons
            item {
                Button(
                    onClick = {
                        isCreatingCustom = true
                        customName = ""
                        selectedExercises.clear()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = GymBloodRed)
                ) {
                    Icon(Icons.Filled.Add, "add", tint = Color.White)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("CREATE NEW CUSTOM WORKOUT PLAN", fontWeight = FontWeight.Bold)
                }
            }

            // List of Plans
            item {
                Text("YOUR SAVED WORKOUT PLANS", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }

            if (plans.isEmpty()) {
                item {
                    Text("No workout plans recorded. Click generate or create one above!", color = GymTextGray, fontSize = 12.sp, style = TextStyle(textAlign = TextAlign.Center))
                }
            } else {
                items(plans) { plan ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = GymCardGray),
                        border = BorderStroke(1.dp, GymSurfaceGray)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(plan.name, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Text("${plan.difficulty.uppercase(Locale.ROOT)} MODE", color = GymBloodRed, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                        Text("|", color = GymTextGray, fontSize = 10.sp)
                                        Text("${plan.durationMinutes} MINUTES ESTIMATED", color = GymTextGray, fontSize = 10.sp)
                                    }
                                }

                                // Start active coaching mode workout plan with high-intensity lightning!
                                Button(
                                    onClick = {
                                        onTriggerLightning {
                                            viewModel.startCoachWorkout(plan)
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = GymBloodRed),
                                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                                ) {
                                    Text("START", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Exercises: ${plan.exercises.replace(",", " ✦ ")}",
                                color = GymTextGray,
                                fontSize = 11.sp,
                                maxLines = 2
                            )

                            Spacer(modifier = Modifier.height(10.dp))
                            // Delete action block
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                TextButton(onClick = { viewModel.deleteWorkoutPlan(plan.id) }) {
                                    Text("DELETE ROUTINE", color = GymTextGray, fontSize = 10.sp)
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // --- CUSTOM CREATOR FORM PANEL ---
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("CREATE NEW PLAN", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    IconButton(onClick = { isCreatingCustom = false }) {
                        // REPLACE ALL CROSSES WITH DUMBBELL ICON RULE MANDATORY
                        DumbbellIcon(size = 20.dp, color = GymBloodRed)
                    }
                }
            }

            item {
                OutlinedTextField(
                    value = customName,
                    onValueChange = { customName = it },
                    label = { Text("Plan Routine Name (e.g. Iron Chest)", color = GymTextGray) },
                    textStyle = TextStyle(color = Color.White),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GymBloodRed,
                        unfocusedBorderColor = GymSurfaceGray
                    )
                )
            }

            item {
                Text("Select Routine Mode Difficulty", color = Color.White, fontSize = 13.sp)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Beginner", "Intermediate", "Advanced").forEach { diff ->
                        Button(
                            onClick = { selectedDifficulty = diff },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedDifficulty == diff) GymBloodRed else GymCardGray
                            ),
                            border = BorderStroke(1.dp, GymSurfaceGray)
                        ) {
                            Text(diff, color = Color.White, fontSize = 11.sp)
                        }
                    }
                }
            }

            // WORKOUT TIME ESTIMATOR WIDGET (DYNAMIC MATH CALCULATOR)
            item {
                // Dynamic time estimation based on selected list:
                // Warmup duration = 10 min, each exercise = 8 min (3sets x 2mins + 2min prep), rest = 90s * 3.
                val totalExercises = selectedExercises.size
                val estWarmup = 8
                val estExercise = totalExercises * 6
                val estRest = totalExercises * 3 * 1.5 // 3 sets * 90s rest
                val estTotal = if (totalExercises == 0) 0 else (estWarmup + estExercise + estRest).toInt()

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = GymBlack),
                    border = BorderStroke(1.dp, GymBloodRed)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("DYNAMIC ESTIMATED ESTIMATES", color = GymBloodRed, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Warm-up Time", color = GymTextGray, fontSize = 10.sp)
                                Text("$estWarmup mins", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                            Column {
                                Text("Set Lift Sessions", color = GymTextGray, fontSize = 10.sp)
                                Text("$estExercise mins", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                            Column {
                                Text("Rest Countdown", color = GymTextGray, fontSize = 10.sp)
                                Text("${estRest.toInt()} mins", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        Divider(modifier = Modifier.padding(vertical = 10.dp), color = GymSurfaceGray)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("TOTAL ROUTINE TIME", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text("$estTotal MINUTES", color = GymBloodRed, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
                        }
                    }
                }
            }

            item {
                Text("EXERCISES SELECTOR (Click to select list, reorder below)", color = Color.White, fontSize = 13.sp)
            }

            // Quick list selections sorted by muscle group
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(GymCardGray, RoundedCornerShape(8.dp))
                        .border(1.dp, GymSurfaceGray, RoundedCornerShape(8.dp))
                        .padding(8.dp)
                ) {
                    val quickLib = remember { ExerciseList.library }
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(quickLib) { ex ->
                            val isSelected = selectedExercises.contains(ex.name)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        if (isSelected) {
                                            selectedExercises.remove(ex.name)
                                        } else {
                                            selectedExercises.add(ex.name)
                                        }
                                    }
                                    .padding(vertical = 6.dp, horizontal = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(ex.name, color = if (isSelected) GymBloodRed else Color.White, fontSize = 12.sp)
                                if (isSelected) {
                                    Icon(Icons.Filled.Check, "ch", tint = GymBloodRed, modifier = Modifier.size(16.dp))
                                }
                            }
                            Divider(color = GymSurfaceGray)
                        }
                    }
                }
            }

            // REORDER SELECTED EXERCISES DRAG AND DROP SIMULATOR
            if (selectedExercises.isNotEmpty()) {
                item {
                    Text("DRAG & DROP / REORDER ACTION LIST", color = GymBloodRed, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }

                itemsIndexed(selectedExercises) { idx, exName ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = GymBlack),
                        border = BorderStroke(1.dp, GymSurfaceGray)
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("${idx + 1}. $exName", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                            
                            // Reorder arrow buttons
                            Row {
                                if (idx > 0) {
                                    IconButton(
                                        onClick = {
                                            val t = selectedExercises[idx]
                                            selectedExercises[idx] = selectedExercises[idx - 1]
                                            selectedExercises[idx - 1] = t
                                        },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(Icons.Filled.KeyboardArrowUp, "up", tint = Color.LightGray)
                                    }
                                }
                                if (idx < selectedExercises.size - 1) {
                                    IconButton(
                                        onClick = {
                                            val t = selectedExercises[idx]
                                            selectedExercises[idx] = selectedExercises[idx + 1]
                                            selectedExercises[idx + 1] = t
                                        },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(Icons.Filled.KeyboardArrowDown, "down", tint = Color.LightGray)
                                    }
                                }
                                // Remove option
                                IconButton(
                                    onClick = { selectedExercises.remove(exName) },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    // REPLACE ALL CROSSES WITH DUMBBELL SECURE RULE
                                    DumbbellIcon(size = 14.dp, color = GymBloodRed)
                                }
                            }
                        }
                    }
                }
            }

            // Save plan button
            item {
                Button(
                    onClick = {
                        if (customName.isNotBlank() && selectedExercises.isNotEmpty()) {
                            val totalEx = selectedExercises.size
                            val estWarmup = 8
                            val estExercise = totalEx * 6
                            val estRest = totalEx * 3 * 1.5
                            val estTotal = (estWarmup + estExercise + estRest).toInt()

                            viewModel.createOrUpdateCustomPlan(
                                customName,
                                selectedExercises.toList(),
                                selectedDifficulty,
                                estTotal
                            )
                            isCreatingCustom = false
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = GymBloodRed)
                ) {
                    Text("SAVE CUSTOM PLAN FOR USE", fontWeight = FontWeight.Bold, color = Color.White)
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

// --- SCREEN 3: EXERCISES LIBRARY SHEET (100 EXERCISES) ---
@Composable
fun ExercisesScreen(onExerciseSelect: (Exercise) -> Unit) {
    val categories = listOf("Chest", "Back", "Shoulders", "Biceps", "Triceps", "Legs", "Abs")
    var selectedCat by remember { mutableStateOf("Chest") }
    var searchQuery by remember { mutableStateOf("") }

    val fullLibrary = remember { ExerciseList.library }

    // Filtered computation
    val filteredList = remember(selectedCat, searchQuery) {
        fullLibrary.filter {
            it.category.equals(selectedCat, ignoreCase = true) &&
                    it.name.contains(searchQuery, ignoreCase = true)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "100 GYM EXERCISES LIBRARY",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.sp
        )
        Text(
            "Double-click or tap any to explore realistic anatomy postures",
            color = GymTextGray,
            fontSize = 11.sp
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Search Bar Outlined
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search 100 exercises...", color = GymTextGray, fontSize = 12.sp) },
            textStyle = TextStyle(color = Color.White, fontSize = 13.sp),
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("exercise_search_input"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GymBloodRed,
                unfocusedBorderColor = GymSurfaceGray
            ),
            trailingIcon = {
                Icon(Icons.Filled.Search, "search", tint = GymBloodRed)
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Scrollable category horizontal tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { cat ->
                val isSel = selectedCat == cat
                Box(
                    modifier = Modifier
                        .background(if (isSel) GymBloodRed else GymCardGray, RoundedCornerShape(8.dp))
                        .clickable { selectedCat = cat }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        cat.uppercase(Locale.ROOT),
                        color = if (isSel) Color.White else GymTextGray,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Exercises Lazy Column List
        Box(modifier = Modifier.weight(1f)) {
            if (filteredList.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No exercises match selection.", color = GymTextGray, fontSize = 12.sp)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredList) { ex ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onExerciseSelect(ex) },
                            colors = CardDefaults.cardColors(containerColor = GymCardGray),
                            border = BorderStroke(1.dp, GymSurfaceGray)
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(TranslationHelper.translateExerciseName(ex.name), color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        Text(TranslationHelper.translateUI(ex.equipment), color = GymBloodRed, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                                        Text("|", color = GymSurfaceGray, fontSize = 10.sp)
                                        Text(TranslationHelper.translateUI(ex.difficulty).uppercase(Locale.ROOT), color = GymTextGray, fontSize = 10.sp)
                                    }
                                }
                                Icon(
                                    imageVector = Icons.Filled.KeyboardArrowRight,
                                    contentDescription = "Go",
                                    tint = GymBloodRed,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- SCREEN 4: POST-WORKOUT STRETCHING & WARM-UP GUIDES ---
@Composable
fun GuidesScreen() {
    var selectedGuideMode by remember { mutableStateOf("Warmup") } // "Warmup", "Stretching"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            TranslationHelper.translateUI("PHYSICAL LIFT PREPARATION"),
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.sp
        )
        Text(
            TranslationHelper.translateUI("Prevent injuries and activate joint lubrication before heavy sessions"),
            color = GymTextGray,
            fontSize = 11.sp
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Selector tabs
        Row(
            modifier = Modifier
                .border(1.dp, GymSurfaceGray, RoundedCornerShape(8.dp))
                .background(GymCardGray, RoundedCornerShape(8.dp))
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(if (selectedGuideMode == "Warmup") GymBloodRed else Color.Transparent, RoundedCornerShape(8.dp))
                    .clickable { selectedGuideMode = "Warmup" }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(TranslationHelper.translateUI("WARM-UP ROUTINES"), color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(if (selectedGuideMode == "Stretching") GymBloodRed else Color.Transparent, RoundedCornerShape(8.dp))
                    .clickable { selectedGuideMode = "Stretching" }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(TranslationHelper.translateUI("STRETCHING SEQUENCES"), color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (selectedGuideMode == "Warmup") {
                val warmups = listOf(
                    "Chest Day Preparation" to "Arm circles (15 left/right), light pushups (10 Slow), band chest openers (15 reps), shoulder capsule dynamic rotations.",
                    "Back & T-Bar Ignition" to "Scapular retraction pulls (15 reps), light bird-dog stability holds (30 seconds), resistance elastic band pulls (15).",
                    "Lower Body Squat Prep" to "Bodyweight deep squats (15 reps), leg swings (12 per leg), hip flexor dynamic lunge stretches, calf wall ankle curls.",
                    "Shoulder Dynamic Activation" to "Y-Y rotations (15), dumbbell external cuff rotations (2-3kg, 15 reps), active shoulder wall slides.",
                    "Arm Pump Prep Session" to "Light rope pushdowns (15 reps), dynamic bicep arm flexion, wrist joint circular mobility taps (30 seconds)."
                )
                items(warmups) { (rawTitle, rawRoutine) ->
                    val (title, routine) = remember(rawTitle, rawRoutine) {
                        TranslationHelper.translateGuide(rawTitle, rawRoutine)
                    }
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = GymCardGray),
                        border = BorderStroke(1.dp, GymSurfaceGray)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(title, color = GymBloodRed, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(routine, color = GymTextLight, fontSize = 12.sp, lineHeight = 18.sp)
                        }
                    }
                }
            } else {
                val stretches = listOf(
                    "Pectorals & Chest Stretch" to "Stand inside a doorway side, rest both forearms on frame angles and lean forward. Hold standard hold for 30 seconds.",
                    "Lats & Lower Back Stretch" to "Clasp your hands completely over your head, extend your body straight up then lean deeply to single lateral hips. Repeat.",
                    "Hamstrings & Hip Flexors" to "Lunge posture knee down flat, push hips actively forward. Lean forward to feel back thigh length. Repeat 30 seconds.",
                    "Active Deltoids Release" to "Draw a single arm across your chest borders, locking tightly with opposite elbow. Push shoulder joints down. Hold.",
                    "Biceps / Triceps Overheads" to "Swing arm behind your head, touch scapular bones and pull elbow vertically down. Tension held for 30 seconds."
                )
                items(stretches) { (rawTitle, rawRoutine) ->
                    val (title, routine) = remember(rawTitle, rawRoutine) {
                        TranslationHelper.translateGuide(rawTitle, rawRoutine)
                    }
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = GymCardGray),
                        border = BorderStroke(1.dp, GymSurfaceGray)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(title, color = GymBloodRed, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(routine, color = GymTextLight, fontSize = 12.sp, lineHeight = 18.sp)
                        }
                    }
                }
            }
        }
    }
}

// --- SCREEN 5: PROGRESS ANALYTICS CHARTS ---
@Composable
fun AnalyticsScreen(
    viewModel: FitnessViewModel,
    onTriggerLightning: (() -> Unit) -> Unit = {}
) {
    val weightLogs by viewModel.allWeightLogs.collectAsStateWithLifecycle()
    val waterRecent by viewModel.recentWaterLogs.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    var inputWeight by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                "PROGRESS ANALYTICS AND METRICS",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
            )
        }

        // DYNAMIC BIOMETRICS ENGINE BASED ON EVIDENCE-BASED KINESIOLOGY
        item {
            val profile = userProfile
            if (profile != null) {
                val bmi = profile.bmi
                val bmiCategory = profile.bmiCategory
                val minHealthyW = profile.minHealthyWeight
                val maxHealthyW = profile.maxHealthyWeight
                val estGoalW = profile.goalWeightKg

                val bmr = profile.bmr
                val tdee = profile.tdee
                val fatLossKcal = profile.fatLossCalories
                val muscleGainKcal = profile.muscleGainCalories

                val calculatedWater = profile.waterGoal
                val stepGoal = profile.stepGoal
                val trainingSessions = profile.workoutFrequency

                Card(
                    modifier = Modifier.fillMaxWidth().testTag("evidence_biometrics_card"),
                    colors = CardDefaults.cardColors(containerColor = GymCardGray),
                    border = BorderStroke(1.dp, GymSurfaceGray)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(modifier = Modifier.size(6.dp).background(GymBloodRed, CircleShape))
                            Text("🔬 DYNAMIC METRIC ENGINE", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
                        }

                        Divider(color = GymSurfaceGray, thickness = 1.dp, modifier = Modifier.padding(vertical = 12.dp))

                        // BMI & Weight Ranges
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Column(modifier = Modifier.weight(1f).background(GymBlack.copy(alpha = 0.4f), RoundedCornerShape(12.dp)).padding(10.dp)) {
                                Text("BODY MASS INDEX", color = GymTextGray, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(String.format(Locale.US, "%.1f", bmi), color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Black)
                                Text(bmiCategory.uppercase(), color = if (bmiCategory == "Healthy Range") Color(0xFF3ECF8E) else GymBloodRed, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                            Column(modifier = Modifier.weight(1f).background(GymBlack.copy(alpha = 0.4f), RoundedCornerShape(12.dp)).padding(10.dp)) {
                                Text("HEALTHY HEIGHT-WEIGHT", color = GymTextGray, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    String.format(Locale.US, "%.1f-%.1fkg", minHealthyW, maxHealthyW),
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                                Text("GOAL: " + String.format(Locale.US, "%.1fkg", estGoalW), color = GymTextLight, fontSize = 8.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // TDEE Calories
                        Column(modifier = Modifier.fillMaxWidth().background(GymBlack.copy(alpha = 0.4f), RoundedCornerShape(12.dp)).padding(12.dp)) {
                            Text("ENERGY MULTIPLIERS (TDEE CALORIES)", color = GymTextGray, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Column {
                                    Text("Maintenance", color = GymTextLight, fontSize = 9.sp)
                                    Text(String.format(Locale.US, "%.0f kcal", tdee), color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                }
                                Column {
                                    Text("Fat Loss Deficit", color = GymBloodRed, fontSize = 9.sp)
                                    Text(String.format(Locale.US, "%.0f kcal", fatLossKcal), color = GymBloodRed, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                }
                                Column {
                                    Text("Muscle Gain Surplus", color = Color(0xFF3ECF8E), fontSize = 9.sp)
                                    Text(String.format(Locale.US, "%.0f kcal", muscleGainKcal), color = Color(0xFF3ECF8E), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Water & Steps Targets
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Column(modifier = Modifier.weight(1f).background(GymBlack.copy(alpha = 0.4f), RoundedCornerShape(12.dp)).padding(10.dp)) {
                                Text("WATER GOAL", color = GymTextGray, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                Text(String.format(Locale.US, "%.2f L", calculatedWater), color = GymBlueAccent, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold)
                                Text("35ml per kg baseline", color = GymTextGray, fontSize = 8.sp)
                            }
                            Column(modifier = Modifier.weight(1f).background(GymBlack.copy(alpha = 0.4f), RoundedCornerShape(12.dp)).padding(10.dp)) {
                                Text("STEPS TARGET", color = GymTextGray, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                Text(String.format(Locale.US, "%,d", stepGoal), color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold)
                                Text("Active pacing target", color = GymTextGray, fontSize = 8.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Suggested Splitting
                        Column(modifier = Modifier.fillMaxWidth().background(GymBlack.copy(alpha = 0.4f), RoundedCornerShape(12.dp)).padding(10.dp)) {
                            Text("SUGGESTED WEEKLY ACTIVE SPLIT", color = GymTextGray, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(trainingSessions, color = GymBloodRed, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxWidth().height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Hydrating metrics database connection...", color = GymTextGray, fontSize = 11.sp)
                }
            }
        }

        // Weight log input
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = GymCardGray),
                border = BorderStroke(1.dp, GymSurfaceGray)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("ADD TODAY'S WEIGHT ENTRY", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = inputWeight,
                            onValueChange = { inputWeight = it },
                            placeholder = { Text("Weight (e.g. 78.5)", color = GymTextGray, fontSize = 12.sp) },
                            textStyle = TextStyle(color = Color.White, fontSize = 13.sp),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GymBloodRed,
                                unfocusedBorderColor = GymSurfaceGray
                            )
                        )
                        Button(
                            onClick = {
                                val wNum = inputWeight.toDoubleOrNull()
                                if (wNum != null) {
                                    onTriggerLightning {
                                        viewModel.addWeightRecord(wNum)
                                    }
                                    inputWeight = ""
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = GymBloodRed)
                        ) {
                            Text("SAVE", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // WEIGHT HISTORY CUSTOM CHART (CUSTOM CONPOSE DRAWING BRUSH)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = GymCardGray),
                border = BorderStroke(1.dp, GymSurfaceGray)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("WEIGHT LOG GROWTH TREND", color = GymTextGray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(14.dp))

                    if (weightLogs.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Log some weights to map the trend curves", color = GymTextGray, fontSize = 11.sp)
                        }
                    } else {
                        // Drawing custom vector premium weight chart
                        Canvas(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(130.dp)
                        ) {
                            val w = size.width
                            val h = size.height

                            val maxW = (weightLogs.maxOfOrNull { it.weightKg } ?: 100.0) + 5
                            val minW = (weightLogs.minOfOrNull { it.weightKg } ?: 50.0) - 5
                            val deltaVal = (maxW - minW).coerceAtLeast(1.0)

                            val stepX = w / (weightLogs.size.coerceAtLeast(2) - 1).coerceAtLeast(1)

                            val points = weightLogs.mapIndexed { index, record ->
                                val ptX = index * stepX
                                val ratio = (record.weightKg - minW) / deltaVal
                                val ptY = h - (ratio.toFloat() * h).coerceIn(10f, h - 10f)
                                Offset(ptX, ptY)
                            }

                            // Draw line
                            val path = Path().apply {
                                if (points.isNotEmpty()) {
                                    moveTo(points[0].x, points[0].y)
                                    for (i in 1 until points.size) {
                                        lineTo(points[i].x, points[i].y)
                                    }
                                }
                            }

                            drawPath(
                                path = path,
                                color = GymBloodRed,
                                style = Stroke(width = 4f, cap = StrokeCap.Round)
                            )

                            // Underline fill
                            val fillPath = Path().apply {
                                if (points.isNotEmpty()) {
                                    moveTo(points[0].x, h)
                                    for (pt in points) {
                                        lineTo(pt.x, pt.y)
                                    }
                                    lineTo(points.last().x, h)
                                    close()
                                }
                            }
                            drawPath(
                                path = fillPath,
                                brush = Brush.verticalGradient(
                                    colors = listOf(GymBloodRed.copy(alpha = 0.35f), Color.Transparent)
                                )
                            )

                            // Draw point bullets
                            points.forEachIndexed { idx, pt ->
                                drawCircle(Color.White, radius = 5f, center = pt)
                                drawCircle(GymBloodRed, radius = 3f, center = pt)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Display list notes
                        Row(
                            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            weightLogs.takeLast(5).forEach { wl ->
                                Box(
                                    modifier = Modifier
                                        .background(GymBlack, RoundedCornerShape(6.dp))
                                        .border(1.dp, GymSurfaceGray, RoundedCornerShape(6.dp))
                                        .padding(6.dp)
                                ) {
                                    Text("${wl.date}: ${wl.weightKg}kg", color = Color.White, fontSize = 9.sp)
                                }
                            }
                        }
                    }
                }
            }
        }

        // WATER RECENT LOGS BAR CHART CUSTOM
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = GymCardGray),
                border = BorderStroke(1.dp, GymSurfaceGray)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("WATER TIMELINE HISTORY (LAST 7 ENTRIES)", color = GymTextGray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(14.dp))

                    if (waterRecent.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(110.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No recent hydration data registered", color = GymTextGray, fontSize = 11.sp)
                        }
                    } else {
                        Canvas(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                        ) {
                            val w = size.width
                            val h = size.height
                            val maxWaterVal = (waterRecent.maxOfOrNull { it.liters } ?: 3.0).coerceAtLeast(1.0)

                            val totalBars = waterRecent.size
                            val barSpacing = w / totalBars
                            val barWidth = barSpacing * 0.5f

                            waterRecent.forEachIndexed { index, water ->
                                val bRatio = (water.liters / maxWaterVal).toFloat()
                                val activeBarH = bRatio * h
                                val bX = (index * barSpacing) + (barSpacing * 0.25f)
                                val bY = h - activeBarH

                                drawRect(
                                    color = GymBlueAccent,
                                    topLeft = Offset(bX, bY),
                                    size = androidx.compose.ui.geometry.Size(barWidth, activeBarH)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            waterRecent.forEach { w ->
                                Text(w.date.substringAfter("-"), color = GymTextGray, fontSize = 8.sp)
                            }
                        }
                    }
                }
            }
        }

        // ACHIEVEMENT BADGES CELEBRATION
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = GymCardGray),
                border = BorderStroke(1.dp, GymSurfaceGray)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("ATHLETIC ACHIEVEMENT BADGES", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        listOf(
                            Triple("IRON BREAKER", "Completed first workout", Icons.Filled.Add),
                            Triple("AQUA GLADIATOR", "Hit daily water target", Icons.Filled.CheckCircle),
                            Triple("STREAK MASTER", "Achieved 3 days on row", Icons.Filled.Star),
                            Triple("METRIC MASTER", "Updated weight metric", Icons.Filled.ThumbUp),
                            Triple("ELITE FORGER", "Custom plan built", Icons.Filled.Send)
                        ).forEach { (bName, bDesc, icon) ->
                            Column(
                                modifier = Modifier
                                    .width(100.dp)
                                    .background(GymBlack, RoundedCornerShape(8.dp))
                                    .border(1.dp, GymSurfaceGray, RoundedCornerShape(8.dp))
                                    .padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(GymDarkRed, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(icon, null, tint = Color.White, modifier = Modifier.size(16.dp))
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(bName, color = GymBloodRed, fontSize = 8.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(bDesc, color = GymTextGray, fontSize = 7.sp, textAlign = TextAlign.Center)
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- SCREEN 6: THE USER PROFILE SETTINGS (PROFILE) ---
@Composable
fun ProfileScreen(
    viewModel: FitnessViewModel,
    userProfile: UserProfileEntity?,
    onReconfigureProfile: () -> Unit,
    onViewLicense: () -> Unit
) {
    val appLanguage by viewModel.appLanguage.collectAsStateWithLifecycle()
    val soundEnabled by viewModel.soundEnabled.collectAsStateWithLifecycle()
    val exerciseSoundsEnabled by viewModel.exerciseSoundsEnabled.collectAsStateWithLifecycle()
    val notificationSoundsEnabled by viewModel.notificationSoundsEnabled.collectAsStateWithLifecycle()
    val thunderEffectsEnabled by viewModel.thunderEffectsEnabled.collectAsStateWithLifecycle()
    val vibrationEnabled by viewModel.vibrationEnabled.collectAsStateWithLifecycle()
    val voiceAnnouncementsEnabled by viewModel.voiceAnnouncementsEnabled.collectAsStateWithLifecycle()

    val soundVolume by viewModel.soundVolume.collectAsStateWithLifecycle()
    val exerciseVolume by viewModel.exerciseVolume.collectAsStateWithLifecycle()
    val notificationVolume by viewModel.notificationVolume.collectAsStateWithLifecycle()
    val timerVolume by viewModel.timerVolume.collectAsStateWithLifecycle()
    val uiVolume by viewModel.uiVolume.collectAsStateWithLifecycle()
    val thunderVolume by viewModel.thunderVolume.collectAsStateWithLifecycle()
    val notificationSoundSelection by viewModel.notificationSoundSelection.collectAsStateWithLifecycle()

    var editName by remember { mutableStateOf(userProfile?.name ?: "") }
    var editAge by remember { mutableStateOf(userProfile?.age?.toString() ?: "25") }
    var editGender by remember { mutableStateOf(userProfile?.gender ?: "Male") }
    var editHeight by remember { mutableStateOf(userProfile?.heightCm?.toString() ?: "175") }
    var editWeight by remember { mutableStateOf(userProfile?.weightKg?.toString() ?: "75") }
    var editGoalWeight by remember { mutableStateOf(userProfile?.goalWeightKg?.toString() ?: "70") }
    var editActivity by remember { mutableStateOf(userProfile?.activityLevel ?: "Active") }
    var editGoal by remember { mutableStateOf(userProfile?.goal ?: "Muscle Gain") }

    // Apply values once loaded
    LaunchedEffect(userProfile) {
        userProfile?.let {
            editName = it.name
            editAge = it.age.toString()
            editGender = it.gender
            editHeight = it.heightCm.toString()
            editWeight = it.weightKg.toString()
            editGoalWeight = it.goalWeightKg.toString()
            editActivity = it.activityLevel
            editGoal = it.goal
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                TranslationHelper.translateUI("USER PROFILE SETTINGS"),
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
            )
            // Secure Supabase badge
            Box(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .background(Color(0xFF3ECF8E).copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    TranslationHelper.translateUI("SECURED WITH SUPABASE LOCAL DB ENCRYPTION"),
                    color = Color(0xFF3ECF8E),
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        }

        // App Language Selection Section Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth().testTag("language_selector_card"),
                colors = CardDefaults.cardColors(containerColor = GymCardGray),
                border = BorderStroke(1.dp, GymSurfaceGray)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        TranslationHelper.translateUI("APP LANGUAGE SELECTION"),
                        color = GymBloodRed,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AppLanguage.values().forEach { lang ->
                            val isSelected = appLanguage == lang
                            Button(
                                onClick = { viewModel.setAppLanguage(lang) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("lang_btn_${lang.name.lowercase()}"),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isSelected) GymBloodRed else GymBlack
                                ),
                                border = BorderStroke(1.dp, GymSurfaceGray),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text(
                                    lang.displayName,
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // SOUND & HAPTIC PREFERENCES SETTINGS CARD
        item {
            Card(
                modifier = Modifier.fillMaxWidth().testTag("sound_settings_card"),
                colors = CardDefaults.cardColors(containerColor = GymCardGray),
                border = BorderStroke(1.dp, GymSurfaceGray)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        TranslationHelper.translateUI("SOUND & HAPTIC PREFERENCES"),
                        color = GymBloodRed,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    // Unified Sound & Voice Announcements Switch (ONLY 1 SOUND OPTION)
                    Row(
                        modifier = Modifier.fillMaxWidth().testTag("unified_sound_toggle_row"),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Workout sound & voicecoach", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text("Enable sound effects, voice coach and alerts", color = GymTextGray, fontSize = 10.sp)
                        }
                        Switch(
                            modifier = Modifier.testTag("voice_announcements_switch"),
                            checked = soundEnabled,
                            onCheckedChange = { isEnabled ->
                                viewModel.setSoundEnabled(isEnabled)
                                viewModel.setVoiceAnnouncementsEnabled(isEnabled)
                                viewModel.setNotificationSoundsEnabled(isEnabled)
                                viewModel.setExerciseSoundsEnabled(isEnabled)
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = GymBloodRed)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Vibration Toggle (Haptic Preference)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Vibration", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text("Feel subtle tactile vibration haptics on actions", color = GymTextGray, fontSize = 10.sp)
                        }
                        Switch(
                            checked = vibrationEnabled,
                            onCheckedChange = { viewModel.setVibrationEnabled(it) },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = GymBloodRed)
                        )
                    }
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = GymCardGray),
                border = BorderStroke(1.dp, GymSurfaceGray)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(TranslationHelper.translateUI("BIOMETRIC SETUP"), color = GymBloodRed, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Display Name", color = GymTextGray) },
                        textStyle = TextStyle(color = Color.White),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = GymBloodRed, unfocusedBorderColor = GymSurfaceGray)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = editAge,
                        onValueChange = { editAge = it },
                        label = { Text("Age (Years)", color = GymTextGray) },
                        textStyle = TextStyle(color = Color.White),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = GymBloodRed, unfocusedBorderColor = GymSurfaceGray)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Gender Selector
                    Text("Gender Model View Selection", color = GymTextGray, fontSize = 11.sp)
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Male", "Female").forEach { g ->
                            Button(
                                onClick = { editGender = g },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (editGender == g) GymBloodRed else GymBlack
                                ),
                                border = BorderStroke(1.dp, GymSurfaceGray)
                            ) {
                                Text(g, color = Color.White, fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = GymCardGray),
                border = BorderStroke(1.dp, GymSurfaceGray)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("DIMENSION AND PHYSICAL GOALS", color = GymBloodRed, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = editHeight,
                        onValueChange = { editHeight = it },
                        label = { Text("Height (cm)", color = GymTextGray) },
                        textStyle = TextStyle(color = Color.White),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = GymBloodRed, unfocusedBorderColor = GymSurfaceGray)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = editWeight,
                        onValueChange = { editWeight = it },
                        label = { Text("Weight (kg)", color = GymTextGray) },
                        textStyle = TextStyle(color = Color.White),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = GymBloodRed, unfocusedBorderColor = GymSurfaceGray)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = editGoalWeight,
                        onValueChange = { editGoalWeight = it },
                        label = { Text("Goal Weight (kg)", color = GymTextGray) },
                        textStyle = TextStyle(color = Color.White),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = GymBloodRed, unfocusedBorderColor = GymSurfaceGray)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Targets selection options
                    Text("Gym Target Goal Focus", color = GymTextGray, fontSize = 11.sp)
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 4.dp).horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Fat Loss", "Muscle Gain", "Strength", "Fitness").forEach { gl ->
                            Box(
                                modifier = Modifier
                                    .background(if (editGoal == gl) GymBloodRed else GymBlack, RoundedCornerShape(8.dp))
                                    .border(1.dp, GymSurfaceGray, RoundedCornerShape(8.dp))
                                    .clickable { editGoal = gl }
                                    .padding(horizontal = 14.dp, vertical = 10.dp)
                            ) {
                                Text(gl.uppercase(), color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Activity Level
                    Text("Baseline Daily Activity Level", color = GymTextGray, fontSize = 11.sp)
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Sedentary", "Active", "Very Active").forEach { ac ->
                            Button(
                                onClick = { editActivity = ac },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (editActivity == ac) GymBloodRed else GymBlack
                                ),
                                border = BorderStroke(1.dp, GymSurfaceGray),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text(ac, color = Color.White, fontSize = 10.sp)
                            }
                        }
                    }
                }
            }
        }

        // Update profile execution
        item {
            Button(
                onClick = {
                    viewModel.updateProfile(
                        name = editName,
                        age = editAge.toIntOrNull() ?: 25,
                        gender = editGender,
                        height = editHeight.toDoubleOrNull() ?: 175.0,
                        weight = editWeight.toDoubleOrNull() ?: 75.0,
                        goalWeight = editGoalWeight.toDoubleOrNull() ?: 70.0,
                        activity = editActivity,
                        goal = editGoal
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = GymBloodRed)
            ) {
                Text("SAVE PROFILE BIOMETRICS", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }

        // Reconfigure Profile settings action
        item {
            Button(
                onClick = onReconfigureProfile,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("reconfigure_profile_button"),
                colors = ButtonDefaults.buttonColors(containerColor = GymBlack),
                border = BorderStroke(1.2.dp, GymBloodRed),
                shape = RoundedCornerShape(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Reset Profile icon",
                        tint = GymBloodRed,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "RECONFIGURE PROTOCOL PROFILE",
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        fontSize = 11.sp,
                        letterSpacing = 1.sp
                    )
                }
            }
        }

        // --- ABOUT "IT'S YOU" BRANDING SECTION ---
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = GymCardGray),
                border = BorderStroke(1.dp, GymSurfaceGray),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "ABOUT IT'S YOU",
                        color = GymBloodRed,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "IT'S YOU Brand Logo",
                        modifier = Modifier
                            .fillMaxWidth(0.55f)
                            .aspectRatio(1f)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "IT'S YOU",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp
                    )
                    Text(
                        "FITNESS • HEALTH • LIFE",
                        color = GymBloodRed,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 3.sp,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        "An elite personal workout tracking, scheduling and kinesiology coaching suite. Forge your metric soul, optimize your anatomy activation, and master the weights. All data encrypted and persisted locally.",
                        color = GymTextLight,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(GymSurfaceGray))
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("App Version", color = GymTextGray, fontSize = 11.sp)
                        Text("1.0.0 (Gold Master)", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Encryption Shield", color = GymTextGray, fontSize = 11.sp)
                        Text("Cipher AES-256", color = GymSuccessGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Copyright License", color = GymTextGray, fontSize = 11.sp)
                        Text("All Rights Reserved", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Account Status", color = GymTextGray, fontSize = 11.sp)
                        Text("Verified & Licensed", color = GymSuccessGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onViewLicense,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = GymBloodRed.copy(alpha = 0.15f)),
                        border = BorderStroke(1.dp, GymBloodRed)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = "Verify License Icon",
                            tint = GymBloodRed,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("VIEW OFFICIAL LICENSE CERTIFICATE", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        item {
            Button(
                onClick = { viewModel.logout() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = GymBlack),
                border = BorderStroke(1.dp, GymBloodRed)
            ) {
                Text("LOGOUT ROUTINE", color = GymBloodRed, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

// --- EXERCISE DETAIL MODAL BOARD OVERLAY SHEET ---
@Composable
fun ExerciseDetailModal(exercise: Exercise, onDismiss: () -> Unit) {
    com.example.ui.screens.PremiumExerciseDetailScreen(
        exercise = exercise,
        onDismiss = onDismiss
    )
}

@Composable
fun OldExerciseDetailModal(exercise: Exercise, onDismiss: () -> Unit) {
    val context = LocalContext.current
    var prWeight by remember { mutableStateOf("") }
    val prKey = "pr_weight_${exercise.name.replace(" ", "_")}"
    val savedPr = remember(exercise.name) { context.getSharedPreferences("fitness_prefs", Context.MODE_PRIVATE).getString(prKey, "N/A") }
    var displayPr by remember(exercise.name) { mutableStateOf(savedPr) }

    LaunchedEffect(exercise.name) {
        val equipment = exercise.equipment.lowercase()
        val name = exercise.name.lowercase()
        
        when {
            equipment.contains("barbell") || name.contains("barbell") -> {
                com.example.ui.audio.PremiumSoundManager.playBarbellMetalSound(context)
            }
            equipment.contains("dumbbell") || name.contains("dumbbell") -> {
                com.example.ui.audio.PremiumSoundManager.playDumbbellPickupSound(context)
            }
            equipment.contains("cable") || name.contains("cable") -> {
                com.example.ui.audio.PremiumSoundManager.playCableMachineSound(context)
            }
            equipment.contains("machine") || equipment.contains("press") || equipment.contains("deck") || equipment.contains("squat") || equipment.contains("curl") || equipment.contains("extension") -> {
                com.example.ui.audio.PremiumSoundManager.playMachineClickSound(context)
            }
            else -> {
                com.example.ui.audio.PremiumSoundManager.playCardioEquipmentSound(context)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.85f))
            .clickable { onDismiss() }, // taps background dismisses
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .fillMaxHeight(0.85f)
                .clickable(enabled = false) {}, // prevent child taps passing out
            colors = CardDefaults.cardColors(containerColor = GymCardGray),
            border = BorderStroke(1.dp, GymSurfaceGray),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Header with dumbbell dismisser icon
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(TranslationHelper.translateExerciseName(exercise.name).uppercase(Locale.ROOT), color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                        Text("${TranslationHelper.translateUI(exercise.category).uppercase()} DAY PRESET | DIFFICULTY: ${TranslationHelper.translateUI(exercise.difficulty).uppercase()}", color = GymBloodRed, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                    IconButton(onClick = onDismiss) {
                        // MANDATORY RULE REPLACE ALL CROSS BUTTONS WITH DUMBBELLS
                        DumbbellIcon(size = 22.dp, color = GymBloodRed)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Box(modifier = Modifier.weight(1f)) {
                    val scrollState = rememberScrollState()
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Premium Dynamic Exercise Player!
                        PremiumExercisePlayer(
                            exercise = exercise,
                            currentSet = 1,
                            totalSets = 4,
                            repsRange = "8-12 Reps",
                            restTimeSeconds = 90
                        )

                        // 5-Step Instruction timeline sheet
                        Text(TranslationHelper.translateUI("STEP-BY-STEP MOVEMENT GUIDE"), color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = GymBlack),
                            border = BorderStroke(1.dp, GymSurfaceGray)
                        ) {
                            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                val steps = listOf(
                                    TranslationHelper.translateUI("MACHINE SETUP") to TranslationHelper.translateMachineSetup(exercise.machineSetup),
                                    TranslationHelper.translateUI("START POSITION") to TranslationHelper.translateUI("Position ${exercise.footPlacement} and set ${exercise.handPlacement}. Extend body in line."),
                                    TranslationHelper.translateUI("MOVEMENT PATH") to TranslationHelper.translateInstructions(2, exercise.instructions.firstOrNull().orEmpty(), exercise.category),
                                    TranslationHelper.translateUI("END POSITION") to TranslationHelper.translateUI("Squeeze muscles at contraction limits completely for 2 seconds."),
                                    TranslationHelper.translateUI("RETURN POSTURE") to TranslationHelper.translateUI("Guide variables back slowly under negative control parameters.")
                                )
                                steps.forEachIndexed { sIdx, (sTitle, sDesc) ->
                                    Row(verticalAlignment = Alignment.Top) {
                                        Box(
                                            modifier = Modifier
                                                .size(20.dp)
                                                .background(GymBloodRed, CircleShape),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text("${sIdx + 1}", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                        }
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column {
                                            Text(sTitle, color = GymBloodRed, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                            Text(sDesc, color = GymTextLight, fontSize = 11.sp, lineHeight = 15.sp)
                                        }
                                    }
                                }
                            }
                        }

                        // Beginner suggestions
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f).background(GymBlack, RoundedCornerShape(8.dp)).padding(10.dp)) {
                                Text(TranslationHelper.translateUI("BEGINNER START WEIGHT"), color = GymBloodRed, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                Text(exercise.beginnerWeight, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                            Column(modifier = Modifier.weight(1f).background(GymBlack, RoundedCornerShape(8.dp)).padding(10.dp)) {
                                Text(TranslationHelper.translateUI("EQUIPMENT BASE"), color = GymBloodRed, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                Text(TranslationHelper.translateUI(exercise.equipment), color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        // Warm up and stretching integrations for this specific exercise!
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(GymBlack, RoundedCornerShape(8.dp))
                                .border(1.dp, GymSurfaceGray, RoundedCornerShape(8.dp))
                                .padding(12.dp)
                        ) {
                            Text(TranslationHelper.translateUI("TARGETED PHYSIQUE PREP & CARE"), color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(TranslationHelper.translateUI("Warm-up Before Exercise:"), color = GymBloodRed, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            Text(TranslationHelper.translateWarmUp(exercise.warmUpRoutine), color = GymTextLight, fontSize = 11.sp)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(TranslationHelper.translateUI("Post-workout Muscle Stretch:"), color = GymBloodRed, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            Text(TranslationHelper.translateStretching(exercise.stretchingRoutine), color = GymTextLight, fontSize = 11.sp)
                        }

                        // Common mistakes and tips
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(GymDarkRed.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                                .border(1.dp, GymDarkRed, RoundedCornerShape(8.dp))
                                .padding(12.dp)
                        ) {
                            Text(TranslationHelper.translateUI("COMMON CRITICAL MISTAKES"), color = GymBloodRed, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            exercise.mistakes.forEach { mist ->
                                Row(modifier = Modifier.padding(top = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Filled.Warning, null, tint = GymBloodRed, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(TranslationHelper.translateMistake(mist, exercise.category), color = GymTextLight, fontSize = 11.sp)
                                }
                            }
                        }

                        // Personal Record (PR) Track Card with Lightning/Thunder feedback
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = GymBlack),
                            border = BorderStroke(1.dp, GymSurfaceGray)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    TranslationHelper.translateUI("PERSONAL BEST / PR"),
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "CURRENT PR: ${displayPr ?: "N/A"}",
                                    color = GymBloodRed,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    OutlinedTextField(
                                        value = prWeight,
                                        onValueChange = { prWeight = it },
                                        placeholder = { Text("e.g. 100 kg x 8 reps", color = GymTextGray, fontSize = 11.sp) },
                                        textStyle = TextStyle(color = Color.White, fontSize = 12.sp),
                                        singleLine = true,
                                        modifier = Modifier.weight(1f),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = GymBloodRed,
                                            unfocusedBorderColor = GymSurfaceGray
                                        )
                                    )
                                    Button(
                                        onClick = {
                                            if (prWeight.isNotBlank()) {
                                                context.getSharedPreferences("fitness_prefs", Context.MODE_PRIVATE).edit()
                                                    .putString(prKey, prWeight).apply()
                                                displayPr = prWeight
                                                prWeight = ""
                                                // Trigger New Personal Record: Powerful Thunder Sound Effect!
                                                com.example.ui.audio.PremiumSoundManager.playNewPersonalRecord(context)
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = GymBloodRed),
                                        modifier = Modifier.height(36.dp),
                                        contentPadding = PaddingValues(horizontal = 12.dp)
                                    ) {
                                        Text("LOG PR", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- ACTIVE WORKOUT COACH OVERLAY (COACHING MODE ENGINE) ---
@Composable
fun ActiveWorkoutCoachOverlay(
    activeState: ActiveWorkoutState,
    viewModel: FitnessViewModel,
    onTriggerLightning: (() -> Unit) -> Unit = {}
) {
    val currentExIndex = activeState.currentExerciseIndex
    val currentExerciseName = activeState.exercises.getOrNull(currentExIndex) ?: "Gym Exercise"

    val overlayContext = LocalContext.current
    LaunchedEffect(activeState.isStarted) {
        if (activeState.isStarted) {
            com.example.ui.audio.PremiumSoundManager.playLightningStrike(overlayContext)
        }
    }

    // Look up detail guides dynamically if needed
    val activeLibraryRef = remember { ExerciseList.library }
    val currentExerciseFullObj = remember(currentExerciseName) {
        activeLibraryRef.find { it.name.equals(currentExerciseName, ignoreCase = true) }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GymBlack)
    ) {
        if (!activeState.isStarted) {
            // Atmospheric dynamic lightning aura flickers synchronized with countdown seconds
            var countdownLightningSegments by remember { mutableStateOf<List<LightningSegment>>(emptyList()) }
            var countdownStrikeAlpha by remember { mutableStateOf(0f) }
            
            LaunchedEffect(activeState.countdownSeconds) {
                countdownStrikeAlpha = 0.95f
                // generate random bolt direction
                countdownLightningSegments = LightningGenerator.generate(
                    startX = 100f + kotlin.random.Random.nextInt(600), startY = 10f,
                    endX = 100f + kotlin.random.Random.nextInt(600), endY = 1600f,
                    displacement = 160f
                )
                delay(125)
                countdownStrikeAlpha = 0.3f
                delay(60)
                // quick secondary spark
                countdownLightningSegments = LightningGenerator.generate(
                    startX = 200f + kotlin.random.Random.nextInt(400), startY = 15f,
                    endX = 200f + kotlin.random.Random.nextInt(400), endY = 1500f,
                    displacement = 90f
                )
                countdownStrikeAlpha = 0.7f
                delay(95)
                countdownLightningSegments = emptyList()
                countdownStrikeAlpha = 0f
            }

            if (countdownLightningSegments.isNotEmpty()) {
                LightningCanvas(segments = countdownLightningSegments, flickerAlpha = countdownStrikeAlpha)
                
                // Pulsing red radial glow on strike
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    GymBloodRed.copy(alpha = 0.5f * countdownStrikeAlpha),
                                    Color.Transparent
                                ),
                                radius = 900f
                            )
                        )
                )
            }

            // RED 3-2-1 START COUNTDOWN SCANNERS
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    TranslationHelper.translateUI("PREPARE YOUR MIND"),
                    color = GymTextGray,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    "${activeState.countdownSeconds}",
                    color = GymBloodRed,
                    fontSize = 120.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    TranslationHelper.translateUI("ITSYOU IS REBUILDING NOW..."),
                    color = Color.White,
                    fontSize = 12.sp,
                    letterSpacing = 1.sp
                )
            }
        } else if (activeState.isFinished) {
            // --- CELEBRATION FINISH SUMMARY ---
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Star",
                    tint = GymBloodRed,
                    modifier = Modifier.size(72.dp)
                )

                Text(
                    TranslationHelper.translateUI("WORKOUT RECORDED!"),
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 2.sp
                )
                Text(
                    activeState.planName.uppercase(),
                    color = GymBloodRed,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Stats summaries
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = GymCardGray),
                    border = BorderStroke(1.dp, GymSurfaceGray)
                ) {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(TranslationHelper.translateUI("METRIC RESULTS REWARD"), color = GymTextGray, fontSize = 9.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(TranslationHelper.translateUI("TOTAL TIME"), color = GymBloodRed, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                Text("${activeState.elapsedSeconds / 60}m ${activeState.elapsedSeconds % 60}s", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(TranslationHelper.translateUI("BURN ESTIMATE"), color = GymBloodRed, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                Text("${activeState.caloriesReward} kcal", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(TranslationHelper.translateUI("SUCCESS"), color = GymBloodRed, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                Text("${activeState.totalExercises}/${activeState.totalExercises}", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Badges rewards
                Text(TranslationHelper.translateUI("UNLOCKED ACHIVEMENT REWARDS"), color = Color.White, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    listOf("IRON HEART", "STREAK RISE", "SQUEEZE SOUL").forEach { badge ->
                        Box(
                            modifier = Modifier
                                .background(GymDarkRed, RoundedCornerShape(8.dp))
                                .border(1.dp, GymBloodRed, RoundedCornerShape(8.dp))
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Text(badge, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(35.dp))

                Button(
                    onClick = { viewModel.dismissActiveWorkout() },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GymBloodRed)
                ) {
                    Text(TranslationHelper.translateUI("RETURN TO GYM DASHBOARD"), fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        } else {
            // --- COAHING ACTIVE PROGRESS WORKOUT DISPLAY WITH PREMIUM PLAYER ---
            var showStopConfirmation by remember { mutableStateOf(false) }

            if (showStopConfirmation) {
                AlertDialog(
                    onDismissRequest = { showStopConfirmation = false },
                    containerColor = GymCardGray,
                    title = { Text("STOP WORKOUT?", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp) },
                    text = { Text("Are you sure you want to stop this workout? Your active progress will be lost.", color = Color.LightGray, fontSize = 14.sp) },
                    confirmButton = {
                        Button(
                            onClick = {
                                showStopConfirmation = false
                                viewModel.dismissActiveWorkout()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = GymBloodRed)
                        ) {
                            Text("Yes, Stop", color = Color.White)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showStopConfirmation = false }) {
                            Text("No, Continue", color = Color.Gray)
                        }
                    }
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Header progress tracking counters
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(activeState.planName, color = GymBloodRed, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text(
                            "${TranslationHelper.translateUI("EXERCISE")} ${currentExIndex + 1} ${TranslationHelper.translateUI("OF")} ${activeState.totalExercises}",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    // Leave Workout Dismiss button (Stop Workout asks for confirmation)
                    IconButton(onClick = { showStopConfirmation = true }) {
                        DumbbellIcon(size = 22.dp, color = GymBloodRed)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Progress Bar Completed math
                val compPercent = (activeState.completedExercisesCount.toFloat() / activeState.totalExercises.toFloat()).coerceIn(0f, 1f)
                Column {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("${TranslationHelper.translateUI("Routine Completion:")} ${(compPercent * 100f).toInt()}%", color = GymTextGray, fontSize = 10.sp)
                        Text("${activeState.elapsedSeconds / 60} ${TranslationHelper.translateUI("min elapsed")}", color = GymTextGray, fontSize = 10.sp)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { compPercent },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = GymBloodRed,
                        trackColor = GymSurfaceGray
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Our breathtaking, highly interactive Professional Exercise Player
                if (currentExerciseFullObj != null) {
                    PremiumExercisePlayer(
                        exercise = currentExerciseFullObj,
                        currentStepSeconds = activeState.elapsedSeconds,
                        currentSet = activeState.currentSetCount,
                        totalSets = 4,
                        repsRange = "8-10 Reps",
                        restTimeSeconds = 90,
                        onPrevExercise = if (currentExIndex > 0) { { viewModel.previousExercise() } } else null,
                        onNextExercise = if (currentExIndex < activeState.totalExercises - 1) { { viewModel.nextExercise() } } else null,
                        isWorkoutResting = activeState.isResting,
                        restSecondsLeft = activeState.restSecondsLeft,
                        isWorkoutPaused = activeState.isPaused,
                        onTogglePause = { viewModel.togglePlayPause() },
                        
                        // Bound viewmodel states and handles for premium set timer and choice
                        setTimerSeconds = activeState.setTimerSeconds,
                        isSetTimerRunning = activeState.isSetTimerRunning,
                        targetSetDuration = activeState.targetSetDuration,
                        onStartSetTimer = { viewModel.startSetTimerAction() },
                        onPauseSetTimer = { viewModel.pauseWorkoutAction() },
                        onResumeSetTimer = { viewModel.resumeWorkoutAction() },
                        onResetSetTimer = { viewModel.resetSetTimer() },
                        onUpdateTargetSetDuration = { viewModel.updateTargetSetDuration(it) },
                        onCompleteSet = {
                            val isFinalSetOfFinalExercise = (activeState.currentExerciseIndex == activeState.totalExercises - 1 && activeState.currentSetCount == 4)
                            if (isFinalSetOfFinalExercise) {
                                onTriggerLightning {
                                    viewModel.completeSet()
                                }
                            } else {
                                viewModel.completeSet()
                            }
                        },
                        onSkipRest = { viewModel.skipRestTimer() }
                    )
                }
            }
        }
    }
}
