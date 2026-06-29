package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.FitnessDatabase
import com.example.data.db.UserProfileEntity
import com.example.data.db.WaterLogEntity
import com.example.data.db.WeightLogEntity
import com.example.data.db.WorkoutLogEntity
import com.example.data.db.WorkoutPlanEntity
import com.example.data.model.Exercise
import com.example.data.model.ExerciseList
import com.example.data.repository.FitnessRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import android.content.Context
import com.example.ui.translation.AppLanguage
import com.example.ui.translation.TranslationHelper
import java.text.SimpleDateFormat
import java.util.*

class FitnessViewModel(application: Application) : AndroidViewModel(application) {

    private val database = FitnessDatabase.getDatabase(application)
    private val repository = FitnessRepository(database.fitnessDao())

    // --- PROFILE STATE ---
    val userProfile: StateFlow<UserProfileEntity?> = repository.userProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // --- LANGUAGE STATE ---
    private val prefs = application.getSharedPreferences("fitness_prefs", Context.MODE_PRIVATE)
    private val _appLanguage = MutableStateFlow<AppLanguage>(
        AppLanguage.valueOf(prefs.getString("app_lang", AppLanguage.HINGLISH.name) ?: AppLanguage.HINGLISH.name)
    )
    val appLanguage: StateFlow<AppLanguage> = _appLanguage.asStateFlow()

    init {
        // Sync local static currentLanguage variable as well
        TranslationHelper.currentLanguage = _appLanguage.value
    }

    fun setAppLanguage(lang: AppLanguage) {
        _appLanguage.value = lang
        TranslationHelper.currentLanguage = lang
        prefs.edit().putString("app_lang", lang.name).apply()
    }

    // --- AUDIO & HAPTIC SETTINGS ---
    private val _soundEnabled = MutableStateFlow<Boolean>(prefs.getBoolean("sound_enabled", true))
    val soundEnabled: StateFlow<Boolean> = _soundEnabled.asStateFlow()

    private val _exerciseSoundsEnabled = MutableStateFlow<Boolean>(prefs.getBoolean("exercise_sounds_enabled", true))
    val exerciseSoundsEnabled: StateFlow<Boolean> = _exerciseSoundsEnabled.asStateFlow()

    private val _notificationSoundsEnabled = MutableStateFlow<Boolean>(prefs.getBoolean("notification_sound_enabled", true))
    val notificationSoundsEnabled: StateFlow<Boolean> = _notificationSoundsEnabled.asStateFlow()

    private val _thunderEffectsEnabled = MutableStateFlow<Boolean>(prefs.getBoolean("thunder_effects_enabled", true))
    val thunderEffectsEnabled: StateFlow<Boolean> = _thunderEffectsEnabled.asStateFlow()

    private val _vibrationEnabled = MutableStateFlow<Boolean>(prefs.getBoolean("vibration_enabled", true))
    val vibrationEnabled: StateFlow<Boolean> = _vibrationEnabled.asStateFlow()

    private val _voiceAnnouncementsEnabled = MutableStateFlow<Boolean>(prefs.getBoolean("voice_announcements_enabled", true))
    val voiceAnnouncementsEnabled: StateFlow<Boolean> = _voiceAnnouncementsEnabled.asStateFlow()

    // Volume Categories
    private val _soundVolume = MutableStateFlow<Float>(prefs.getFloat("sound_volume", 0.8f))
    val soundVolume: StateFlow<Float> = _soundVolume.asStateFlow()

    private val _exerciseVolume = MutableStateFlow<Float>(prefs.getFloat("exercise_volume", 0.8f))
    val exerciseVolume: StateFlow<Float> = _exerciseVolume.asStateFlow()

    private val _notificationVolume = MutableStateFlow<Float>(prefs.getFloat("notification_volume", 0.8f))
    val notificationVolume: StateFlow<Float> = _notificationVolume.asStateFlow()

    private val _timerVolume = MutableStateFlow<Float>(prefs.getFloat("timer_volume", 0.8f))
    val timerVolume: StateFlow<Float> = _timerVolume.asStateFlow()

    private val _uiVolume = MutableStateFlow<Float>(prefs.getFloat("ui_volume", 0.8f))
    val uiVolume: StateFlow<Float> = _uiVolume.asStateFlow()

    private val _thunderVolume = MutableStateFlow<Float>(prefs.getFloat("thunder_volume", 0.8f))
    val thunderVolume: StateFlow<Float> = _thunderVolume.asStateFlow()

    // Notification Sound Selection
    private val _notificationSoundSelection = MutableStateFlow<String>(prefs.getString("notification_sound_selection", "Standard Notification") ?: "Standard Notification")
    val notificationSoundSelection: StateFlow<String> = _notificationSoundSelection.asStateFlow()

    fun setSoundEnabled(enabled: Boolean) {
        _soundEnabled.value = enabled
        prefs.edit().putBoolean("sound_enabled", enabled).apply()
    }

    fun setExerciseSoundsEnabled(enabled: Boolean) {
        _exerciseSoundsEnabled.value = enabled
        prefs.edit().putBoolean("exercise_sounds_enabled", enabled).apply()
    }

    fun setNotificationSoundsEnabled(enabled: Boolean) {
        _notificationSoundsEnabled.value = enabled
        prefs.edit().putBoolean("notification_sound_enabled", enabled).apply()
    }

    fun setThunderEffectsEnabled(enabled: Boolean) {
        _thunderEffectsEnabled.value = enabled
        prefs.edit().putBoolean("thunder_effects_enabled", enabled).apply()
    }

    fun setVibrationEnabled(enabled: Boolean) {
        _vibrationEnabled.value = enabled
        prefs.edit().putBoolean("vibration_enabled", enabled).apply()
    }

    fun setVoiceAnnouncementsEnabled(enabled: Boolean) {
        _voiceAnnouncementsEnabled.value = enabled
        prefs.edit().putBoolean("voice_announcements_enabled", enabled).apply()
    }

    fun setSoundVolume(volume: Float) {
        _soundVolume.value = volume
        prefs.edit().putFloat("sound_volume", volume).apply()
    }

    fun setExerciseVolume(volume: Float) {
        _exerciseVolume.value = volume
        prefs.edit().putFloat("exercise_volume", volume).apply()
    }

    fun setNotificationVolume(volume: Float) {
        _notificationVolume.value = volume
        prefs.edit().putFloat("notification_volume", volume).apply()
    }

    fun setTimerVolume(volume: Float) {
        _timerVolume.value = volume
        prefs.edit().putFloat("timer_volume", volume).apply()
    }

    fun setUiVolume(volume: Float) {
        _uiVolume.value = volume
        prefs.edit().putFloat("ui_volume", volume).apply()
    }

    fun setThunderVolume(volume: Float) {
        _thunderVolume.value = volume
        prefs.edit().putFloat("thunder_volume", volume).apply()
    }

    fun setNotificationSoundSelection(selection: String) {
        _notificationSoundSelection.value = selection
        prefs.edit().putString("notification_sound_selection", selection).apply()
    }

    // --- DYNAMIC NOTIFICATIONS CENTER ---
    private val _dismissedNotifs = MutableStateFlow<Set<String>>(
        prefs.getStringSet("dismissed_notifs", emptySet()) ?: emptySet()
    )
    private val _readNotifs = MutableStateFlow<Set<String>>(
        prefs.getStringSet("read_notifs", emptySet()) ?: emptySet()
    )

    val notifications: StateFlow<List<GymNotification>> = combine(
        userProfile,
        _dismissedNotifs,
        _readNotifs
    ) { profile, dismissed, read ->
        val list = mutableListOf<GymNotification>()
        
        val streak = profile?.streakCount ?: 0
        val targetWater = profile?.waterGoal ?: 2.5
        
        if (!dismissed.contains("notif_streak")) {
            list.add(
                GymNotification(
                    id = "notif_streak",
                    title = "🔥 Streak Protection",
                    text = "Protect your $streak-day consistency streak! Get an active gym lift in today to maintain momentum.",
                    buttonLabel = "LETS LIFT!"
                )
            )
        }
        if (!dismissed.contains("notif_hydrate")) {
            list.add(
                GymNotification(
                    id = "notif_hydrate",
                    title = "💧 Hydration Milestones",
                    text = "You must stay hydrated on intensive lifting days. Complete your dynamic ${String.format(Locale.US, "%.1f", targetWater)} L goal.",
                    buttonLabel = "DRINK 500ML"
                )
            )
        }
        if (!dismissed.contains("notif_schedule")) {
            list.add(
                GymNotification(
                    id = "notif_schedule",
                    title = "🏋️ Scheduled Training",
                    text = "Weekly active recommendation of 4-5 strength or hypertrophy sessions. Check out your training split preset in planner.",
                    buttonLabel = "OPEN PLANNER"
                )
            )
        }
        if (!dismissed.contains("notif_update")) {
            list.add(
                GymNotification(
                    id = "notif_update",
                    title = "✨ App Engine Update",
                    text = "ITSYOU premium coaching engine upgraded to modern dynamic theme alignment & edge-to-edge system integrations.",
                    buttonLabel = "DISMISS"
                )
            )
        }
        
        // Custom dynamic notifications stored in shared prefs
        val dynamicJson = prefs.getString("dynamic_notifications", "[]") ?: "[]"
        try {
            val jsonArray = org.json.JSONArray(dynamicJson)
            for (i in 0 until jsonArray.length()) {
                val jsonString = jsonArray.getString(i)
                val obj = org.json.JSONObject(jsonString)
                val id = obj.getString("id")
                if (!dismissed.contains(id)) {
                    list.add(
                        GymNotification(
                            id = id,
                            title = obj.getString("title"),
                            text = obj.getString("text"),
                            buttonLabel = obj.getString("buttonLabel"),
                            timestamp = obj.optLong("timestamp", System.currentTimeMillis())
                        )
                    )
                }
            }
        } catch (e: Exception) {
            android.util.Log.e("FitnessViewModel", "Error parsing dynamic notifications", e)
        }
        
        // Sort: unread first, then by timestamp
        list.map { item ->
            item.copy(isRead = read.contains(item.id))
        }.sortedWith(compareBy<GymNotification> { it.isRead }.thenByDescending { it.timestamp })
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun dismissNotification(id: String) {
        val currentSet = _dismissedNotifs.value.toMutableSet()
        currentSet.add(id)
        _dismissedNotifs.value = currentSet
        prefs.edit().putStringSet("dismissed_notifs", currentSet).apply()
    }

    fun markNotificationAsRead(id: String) {
        val currentSet = _readNotifs.value.toMutableSet()
        currentSet.add(id)
        _readNotifs.value = currentSet
        prefs.edit().putStringSet("read_notifs", currentSet).apply()
    }

    fun clearAllNotifications() {
        val allIds = notifications.value.map { it.id }.toSet()
        val currentSet = _dismissedNotifs.value.toMutableSet()
        currentSet.addAll(allIds)
        _dismissedNotifs.value = currentSet
        prefs.edit().putStringSet("dismissed_notifs", currentSet).apply()
    }

    fun addDynamicNotification(title: String, text: String, buttonLabel: String) {
        try {
            val dynamicJson = prefs.getString("dynamic_notifications", "[]") ?: "[]"
            val jsonArray = org.json.JSONArray(dynamicJson)
            
            val newObj = org.json.JSONObject()
            val newId = "dyn_notif_" + System.currentTimeMillis()
            newObj.put("id", newId)
            newObj.put("title", title)
            newObj.put("text", text)
            newObj.put("buttonLabel", buttonLabel)
            newObj.put("isRead", false)
            newObj.put("timestamp", System.currentTimeMillis())
            
            jsonArray.put(newObj)
            prefs.edit().putString("dynamic_notifications", jsonArray.toString()).apply()
            
            val currentSet = _dismissedNotifs.value.toMutableSet()
            _dismissedNotifs.value = currentSet
        } catch (e: Exception) {
            android.util.Log.e("FitnessViewModel", "Error adding dynamic notification", e)
        }
    }

    // --- AUTHENTICATION STATE ---
    private val _authState = MutableStateFlow<AuthState>(AuthState.LoggedOut)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    // Mock Database for Registered Users (Supabase-like persistence simulation)
    private val registeredUsersMock = mutableMapOf<String, String>(
        "demo@itsyou.com" to "password123",
        "jay@itsyoufitness.in" to "premiumgym"
    )

    // --- WATER MANAGEMENT ---
    private val todayString: String
        get() = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    val todayWaterLog: StateFlow<WaterLogEntity?> = repository.getWaterLog(todayString)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val recentWaterLogs: StateFlow<List<WaterLogEntity>> = repository.recentWaterLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- WEIGHTS ---
    val allWeightLogs: StateFlow<List<WeightLogEntity>> = repository.allWeightLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- WORKOUT PLANS ---
    val allWorkoutPlans: StateFlow<List<WorkoutPlanEntity>> = repository.allWorkoutPlans
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- WORKOUT LOGS ---
    val allWorkoutLogs: StateFlow<List<WorkoutLogEntity>> = repository.allWorkoutLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- SMART WORKOUT COACH ENGINE STATE ---
    private val _activeWorkout = MutableStateFlow<ActiveWorkoutState?>(null)
    val activeWorkout: StateFlow<ActiveWorkoutState?> = _activeWorkout.asStateFlow()

    private var coachTimerJob: Job? = null
    private var restTimerJob: Job? = null
    private var setTimerJob: Job? = null

    init {
        viewModelScope.launch {
            repository.prepopulateDefaultPlans()
            checkAndUpdateStreakOnLaunch()
            loadPersistedActiveWorkout()
        }
        val persistedType = prefs.getString("auth_state_type", "LoggedOut")
        val persistedEmail = prefs.getString("auth_email", "")
        _authState.value = when (persistedType) {
            "LoggedIn" -> if (persistedEmail.orEmpty().isNotBlank()) AuthState.LoggedIn(persistedEmail!!) else AuthState.LoggedOut
            "Guest" -> AuthState.Guest
            else -> AuthState.LoggedOut
        }
    }

    private fun saveActiveStateToDb(state: ActiveWorkoutState?) {
        viewModelScope.launch {
            if (state == null) {
                repository.clearActiveWorkoutState()
            } else {
                repository.saveActiveWorkoutState(
                    com.example.data.db.ActiveWorkoutStateEntity(
                        planName = state.planName,
                        totalExercises = state.totalExercises,
                        exercises = state.exercises.joinToString(","),
                        estimatedLength = state.estimatedLength,
                        difficulty = state.difficulty,
                        currentExerciseIndex = state.currentExerciseIndex,
                        currentSetCount = state.currentSetCount,
                        countdownSeconds = state.countdownSeconds,
                        isStarted = state.isStarted,
                        isResting = state.isResting,
                        restSecondsLeft = state.restSecondsLeft,
                        elapsedSeconds = state.elapsedSeconds,
                        completedExercisesCount = state.completedExercisesCount,
                        isFinished = state.isFinished,
                        caloriesReward = state.caloriesReward,
                        voiceNotification = state.voiceNotification,
                        flashMessage = state.flashMessage,
                        isPaused = state.isPaused,
                        setTimerSeconds = state.setTimerSeconds,
                        isSetTimerRunning = state.isSetTimerRunning,
                        targetSetDuration = state.targetSetDuration
                    )
                )
            }
        }
    }

    private fun loadPersistedActiveWorkout() {
        viewModelScope.launch {
            val entity = repository.getActiveWorkoutStateDirect()
            if (entity != null) {
                val exercisesList = entity.exercises.split(",").filter { it.isNotBlank() }
                _activeWorkout.value = ActiveWorkoutState(
                    planName = entity.planName,
                    totalExercises = entity.totalExercises,
                    exercises = exercisesList,
                    estimatedLength = entity.estimatedLength,
                    difficulty = entity.difficulty,
                    currentExerciseIndex = entity.currentExerciseIndex,
                    currentSetCount = entity.currentSetCount,
                    countdownSeconds = entity.countdownSeconds,
                    isStarted = entity.isStarted,
                    isResting = entity.isResting,
                    restSecondsLeft = entity.restSecondsLeft,
                    elapsedSeconds = entity.elapsedSeconds,
                    completedExercisesCount = entity.completedExercisesCount,
                    isFinished = entity.isFinished,
                    caloriesReward = entity.caloriesReward,
                    voiceNotification = entity.voiceNotification,
                    flashMessage = entity.flashMessage,
                    isPaused = entity.isPaused,
                    setTimerSeconds = entity.setTimerSeconds,
                    isSetTimerRunning = entity.isSetTimerRunning,
                    targetSetDuration = entity.targetSetDuration
                )
                // Resume countdown or timers if started and not finished
                if (entity.isStarted && !entity.isFinished) {
                    startCoachTimer()
                    if (entity.isResting) {
                        startRestTimer()
                    } else if (entity.isSetTimerRunning && !entity.isPaused) {
                        startSetTimer()
                    }
                }
            }
        }
    }

    fun checkAndUpdateStreakOnLaunch() {
        viewModelScope.launch {
            val profile = repository.getProfileDirect() ?: return@launch
            val lastDate = profile.lastWorkoutDate ?: return@launch
            try {
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val last = sdf.parse(lastDate)
                val today = sdf.parse(todayString)
                if (last != null && today != null) {
                    val diff = today.time - last.time
                    val diffDays = diff / (24 * 60 * 60 * 1000)
                    if (diffDays > 1L) {
                        // User missed yesterday, streak breaks! Reset to 0
                        if (profile.streakCount > 0) {
                            repository.saveProfile(profile.copy(streakCount = 0))
                        }
                    }
                }
            } catch (e: Exception) {
                // Ignore Exception
            }
        }
    }

    private fun saveAuthStateToPrefs(state: AuthState) {
        val editor = prefs.edit()
        when (state) {
            is AuthState.LoggedIn -> {
                editor.putString("auth_state_type", "LoggedIn")
                editor.putString("auth_email", state.email)
            }
            AuthState.Guest -> {
                editor.putString("auth_state_type", "Guest")
                editor.putString("auth_email", "guest@itsyou.com")
            }
            AuthState.LoggedOut -> {
                editor.putString("auth_state_type", "LoggedOut")
                editor.putString("auth_email", "")
            }
        }
        editor.apply()
    }

    // --- AUTH ACTIONS (SUPABASE SIMULATOR) ---
    fun loginWithEmail(email: String, word: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            delay(800) // Realistic secure network delay
            val registeredPw = registeredUsersMock[email.lowercase()]
            if (registeredPw != null && registeredPw == word) {
                // Fetch profile or populate defaults
                var existingProfile = repository.getProfileDirect()
                if (existingProfile == null) {
                    existingProfile = UserProfileEntity(
                        email = email.lowercase(),
                        name = email.substringBefore("@").capitalize(Locale.ROOT),
                        age = 25,
                        gender = "Male",
                        heightCm = 175.0,
                        weightKg = 75.0,
                        goalWeightKg = 70.0,
                        activityLevel = "Active",
                        goal = "Muscle Gain",
                        streakCount = 0, // No fake starting streak!
                        lastWorkoutDate = null
                    )
                    repository.saveProfile(existingProfile)
                }
                val newState = AuthState.LoggedIn(existingProfile.email)
                _authState.value = newState
                saveAuthStateToPrefs(newState)
                checkAndUpdateStreakOnLaunch()
                onResult(true, "Login Successful!")
            } else {
                onResult(false, "Invalid email or password.")
            }
        }
    }

    fun registerWithEmail(email: String, word: String, name: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            delay(1000)
            if (email.isBlank() || word.length < 6) {
                onResult(false, "Password must be at least 6 characters.")
                return@launch
            }
            if (registeredUsersMock.containsKey(email.lowercase())) {
                onResult(false, "Email is already registered.")
                return@launch
            }
            registeredUsersMock[email.lowercase()] = word
            val seedProfile = UserProfileEntity(
                email = email.lowercase(),
                name = name.ifBlank { email.substringBefore("@") },
                age = 26,
                gender = "Male",
                heightCm = 178.0,
                weightKg = 80.0,
                goalWeightKg = 75.0,
                activityLevel = "Sedentary",
                goal = "Muscle Gain"
            )
            repository.saveProfile(seedProfile)
            val newState = AuthState.LoggedIn(seedProfile.email)
            _authState.value = newState
            saveAuthStateToPrefs(newState)
            onResult(true, "Registration complete! Welcome.")
        }
    }

    fun handleForgotPassword(email: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            delay(800)
            if (registeredUsersMock.containsKey(email.lowercase())) {
                onResult(true, "Reset link has been securely transmitted. Check your inbox.")
            } else {
                onResult(false, "No registered account found with this email.")
            }
        }
    }

    fun loginAsGuest() {
        val newState = AuthState.Guest
        _authState.value = newState
        saveAuthStateToPrefs(newState)
        viewModelScope.launch {
            val existing = repository.getProfileDirect()
            if (existing == null) {
                repository.saveProfile(
                    UserProfileEntity(
                        email = "guest@itsyou.com",
                        name = "Guest Gym Athlete",
                        age = 24,
                        gender = "Male",
                        heightCm = 180.0,
                        weightKg = 78.0,
                        goalWeightKg = 75.0,
                        activityLevel = "Sedentary",
                        goal = "Fitness"
                    )
                )
            }
            checkAndUpdateStreakOnLaunch()
        }
    }

    fun loginWithGoogle(onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            delay(1200) // OAuth delay
            val email = "jay@itsyoufitness.in"
            val existing = UserProfileEntity(
                email = email,
                name = "Jay Pratap",
                age = 28,
                gender = "Male",
                heightCm = 182.0,
                weightKg = 85.0,
                goalWeightKg = 80.0,
                activityLevel = "Very Active",
                goal = "Strength",
                streakCount = 0, // No fake starting streak!
                lastWorkoutDate = null
            )
            repository.saveProfile(existing)
            val newState = AuthState.LoggedIn(email)
            _authState.value = newState
            saveAuthStateToPrefs(newState)
            checkAndUpdateStreakOnLaunch()
            onResult(true, "Google Sign-In successful!")
        }
    }

    fun logout() {
        val newState = AuthState.LoggedOut
        _authState.value = newState
        saveAuthStateToPrefs(newState)
    }

    // --- WATER CONTROL ACTIONS ---
    fun logWaterIntake(deltaMl: Double) {
        viewModelScope.launch {
            val profile = repository.getProfileDirect()
            val baseTarget = profile?.waterGoal ?: 2.5
            val currentLog = todayWaterLog.value
            val oldConsumed = currentLog?.liters ?: 0.0
            val targetLitersVal = currentLog?.targetLiters ?: baseTarget
            
            repository.logWater(todayString, deltaMl, baseTarget)
            
            val newConsumed = oldConsumed + (deltaMl / 1000.0)
            if (oldConsumed < targetLitersVal && newConsumed >= targetLitersVal) {
                com.example.ui.audio.PremiumSoundManager.playWaterGoalCompleted(getApplication())
            }
        }
    }

    // --- WEIGHT ACTIONS ---
    fun addWeightRecord(weightKg: Double) {
        viewModelScope.launch {
            repository.logWeight(todayString, weightKg)
            // Synchronize with user profile weight
            val profile = repository.getProfileDirect()
            if (profile != null) {
                repository.saveProfile(profile.copy(weightKg = weightKg))
            }
        }
    }

    fun deleteWeightRecord(id: Int) {
        viewModelScope.launch {
            repository.deleteWeightLog(id)
        }
    }

    // --- UPDATE PROFILE ---
    fun updateProfile(
        name: String,
        age: Int,
        gender: String,
        height: Double,
        weight: Double,
        goalWeight: Double,
        activity: String,
        goal: String
    ) {
        viewModelScope.launch {
            val email = when (val auth = _authState.value) {
                is AuthState.LoggedIn -> auth.email
                else -> "guest@itsyou.com"
            }
            val profile = UserProfileEntity(
                email = email,
                name = name,
                age = age,
                gender = gender,
                heightCm = height,
                weightKg = weight,
                goalWeightKg = goalWeight,
                activityLevel = activity,
                goal = goal,
                streakCount = userProfile.value?.streakCount ?: 0,
                lastWorkoutDate = userProfile.value?.lastWorkoutDate
            )
            repository.saveProfile(profile)
            
            // Log weight record for this date too
            repository.logWeight(todayString, weight)
        }
    }

    // --- SMART WORKOUT PLANNER ACTIONS ---
    fun createOrUpdateCustomPlan(name: String, exercisesList: List<String>, difficulty: String, duration: Int) {
        viewModelScope.launch {
            val listStr = exercisesList.joinToString(",")
            val plan = WorkoutPlanEntity(
                name = name,
                exercises = listStr,
                difficulty = difficulty,
                durationMinutes = duration,
                isCustom = true
            )
            repository.saveWorkoutPlan(plan)
        }
    }

    fun deleteWorkoutPlan(id: Int) {
        viewModelScope.launch {
            repository.deleteWorkoutPlan(id)
        }
    }

    fun generateQuickPresetPlan(mode: String) {
        viewModelScope.launch {
            // Generates predefined templates fast
            when (mode) {
                "BEGINNER" -> {
                    val p = WorkoutPlanEntity(
                        name = "Beginner Quick Tone (Generated)",
                        exercises = "Bench Press,Classic Push-Ups,Lat Pulldown,Seated Leg Curl Machine",
                        difficulty = "Beginner",
                        durationMinutes = 45,
                        isCustom = true
                    )
                    repository.saveWorkoutPlan(p)
                }
                "INTERMEDIATE" -> {
                    val p = WorkoutPlanEntity(
                        name = "Intermediate Hypertrophy (Generated)",
                        exercises = "Incline Dumbbell Press,Seated Cable Row,Seated Dumbbell Press,Heavy Leg Press,EZ-Bar Skull Crushers",
                        difficulty = "Intermediate",
                        durationMinutes = 75,
                        isCustom = true
                    )
                    repository.saveWorkoutPlan(p)
                }
                "ADVANCED" -> {
                    val p = WorkoutPlanEntity(
                        name = "Advanced Iron Shredder (Generated)",
                        exercises = "Barbell Back Squat,Deadlift,Overhead Barbell Press,Weighted Dips,Rope Pushdown,Hanging Straight Leg Raise",
                        difficulty = "Advanced",
                        durationMinutes = 110,
                        isCustom = true
                    )
                    repository.saveWorkoutPlan(p)
                }
            }
        }
    }

    // --- WORKOUT COACH CONTROLS ---
    fun startCoachWorkout(plan: WorkoutPlanEntity) {
        coachTimerJob?.cancel()
        restTimerJob?.cancel()
        setTimerJob?.cancel()

        val exerciseNames = plan.exercises.split(",").filter { it.isNotBlank() }
        if (exerciseNames.isEmpty()) return

        // Standard setup coach state
        _activeWorkout.value = ActiveWorkoutState(
            planName = plan.name,
            totalExercises = exerciseNames.size,
            exercises = exerciseNames,
            estimatedLength = plan.durationMinutes,
            difficulty = plan.difficulty,
            setTimerSeconds = 0,
            isSetTimerRunning = false,
            targetSetDuration = 30
        )
        saveActiveStateToDb(_activeWorkout.value)

        // Starting 3-2-1 countdown screen
        viewModelScope.launch {
            for (countdown in 3 downTo 1) {
                _activeWorkout.update { it?.copy(countdownSeconds = countdown) }
                delay(1000)
            }
            // Workout begins!
            _activeWorkout.update {
                it?.copy(
                    countdownSeconds = 0,
                    isStarted = true,
                    currentExerciseIndex = 0,
                    completedExercisesCount = 0
                )
            }
            saveActiveStateToDb(_activeWorkout.value)
            com.example.ui.audio.PremiumSoundManager.playLightningStrike(getApplication())
            startCoachTimer()
        }
    }

    private fun startCoachTimer() {
        coachTimerJob?.cancel()
        coachTimerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                _activeWorkout.update {
                    if (it != null && !it.isPaused) {
                        it.copy(elapsedSeconds = (it.elapsedSeconds + 1))
                    } else {
                        it
                    }
                }
                saveActiveStateToDb(_activeWorkout.value)
            }
        }
    }

    fun startSetTimer() {
        setTimerJob?.cancel()
        setTimerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                val state = _activeWorkout.value ?: break
                if (state.isSetTimerRunning && !state.isPaused && !state.isResting) {
                    val nextSeconds = state.setTimerSeconds + 1
                    if (nextSeconds >= state.targetSetDuration) {
                        // Reached target! Play sound, stop set timer
                        _activeWorkout.update {
                            it?.copy(
                                setTimerSeconds = state.targetSetDuration,
                                isSetTimerRunning = false
                            )
                        }
                        com.example.ui.audio.PremiumSoundManager.playRestTimerComplete(getApplication()) // play notification sound
                        saveActiveStateToDb(_activeWorkout.value)
                        setTimerJob?.cancel()
                        break
                    } else {
                        _activeWorkout.update {
                            it?.copy(setTimerSeconds = nextSeconds)
                        }
                        saveActiveStateToDb(_activeWorkout.value)
                    }
                } else {
                    break
                }
            }
        }
    }

    fun startSetTimerAction() {
        _activeWorkout.update {
            it?.copy(isSetTimerRunning = true, isPaused = false)
        }
        startSetTimer()
        saveActiveStateToDb(_activeWorkout.value)
    }

    fun pauseWorkoutAction() {
        _activeWorkout.update {
            it?.copy(isPaused = true)
        }
        saveActiveStateToDb(_activeWorkout.value)
    }

    fun resumeWorkoutAction() {
        _activeWorkout.update {
            it?.copy(isPaused = false)
        }
        val state = _activeWorkout.value
        if (state != null) {
            if (state.isResting) {
                startRestTimer()
            } else if (state.isSetTimerRunning) {
                startSetTimer()
            }
        }
        saveActiveStateToDb(_activeWorkout.value)
    }

    fun resetSetTimer() {
        setTimerJob?.cancel()
        _activeWorkout.update {
            it?.copy(
                setTimerSeconds = 0,
                isSetTimerRunning = false
            )
        }
        saveActiveStateToDb(_activeWorkout.value)
    }

    fun updateTargetSetDuration(durationSeconds: Int) {
        _activeWorkout.update {
            it?.copy(
                targetSetDuration = durationSeconds,
                setTimerSeconds = 0,
                isSetTimerRunning = false
            )
        }
        saveActiveStateToDb(_activeWorkout.value)
    }

    fun completeSet() {
        val state = _activeWorkout.value ?: return
        
        // Stop current set timer
        setTimerJob?.cancel()
        
        val currentSet = state.currentSetCount
        val targetSets = 4 // standard sets

        // Calculate calories burned for this set based on exercise type, duration, MET, body weight
        val profile = userProfile.value
        val weight = profile?.weightKg ?: 75.0
        val currentExIndex = state.currentExerciseIndex
        val exerciseName = state.exercises.getOrNull(currentExIndex) ?: "Exercise"
        val currentExerciseFullObj = ExerciseList.library.find { it.name.equals(exerciseName, ignoreCase = true) }
        val category = currentExerciseFullObj?.category ?: "Chest"
        
        // Calories = MET * 3.5 * weightKg / 200 * durationInMinutes
        // Set duration in minutes
        val setDurationMin = state.targetSetDuration / 60.0
        val met = when (category.lowercase()) {
            "chest" -> 6.0
            "back" -> 6.0
            "shoulders" -> 5.0
            "biceps", "triceps" -> 4.0
            "legs" -> 7.0
            "abs" -> 3.5
            else -> 5.0
        }
        val caloriesForSet = (met * 3.5 * weight / 200.0 * setDurationMin).coerceAtLeast(1.0)
        val updatedCalories = state.caloriesReward + caloriesForSet.toInt()

        if (currentSet < targetSets) {
            // Immediately start rest timer, and increment set count on finish/skip
            _activeWorkout.update {
                it?.copy(
                    isResting = true,
                    restSecondsLeft = 90,
                    setTimerSeconds = 0,
                    isSetTimerRunning = false,
                    caloriesReward = updatedCalories,
                    flashMessage = "SET COMPLETED! TAKE A BREATH."
                )
            }
            startRestTimer()
        } else {
            // This exercise is finished!
            // If it's the last exercise of the plan, complete workout!
            if (state.currentExerciseIndex == state.totalExercises - 1) {
                _activeWorkout.update {
                    it?.copy(
                        caloriesReward = updatedCalories,
                        setTimerSeconds = 0,
                        isSetTimerRunning = false
                    )
                }
                finishActiveWorkout()
            } else {
                // Not the last exercise. Start the rest timer, and on skip/end we transition to next exercise
                _activeWorkout.update {
                    it?.copy(
                        isResting = true,
                        restSecondsLeft = 90,
                        setTimerSeconds = 0,
                        isSetTimerRunning = false,
                        caloriesReward = updatedCalories,
                        flashMessage = "EXERCISE COMPLETED! GET READY FOR THE NEXT ONE."
                    )
                }
                startRestTimer()
            }
        }
        saveActiveStateToDb(_activeWorkout.value)
    }

    private fun startRestTimer() {
        restTimerJob?.cancel()
        restTimerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                val state = _activeWorkout.value ?: break
                if (state.isResting) {
                    if (state.isPaused) continue // pause rest timer countdown as well
                    val remaining = state.restSecondsLeft - 1
                    if (remaining > 0) {
                        _activeWorkout.update {
                            it?.copy(
                                restSecondsLeft = remaining,
                                voiceNotification = if (remaining == 5) "Prepare for Next Set!" else it.voiceNotification
                            )
                        }
                        saveActiveStateToDb(_activeWorkout.value)
                    } else {
                        // Rest ended
                        handleRestTimerCompletion()
                        break
                    }
                } else {
                    break
                }
            }
        }
    }

    fun handleRestTimerCompletion() {
        restTimerJob?.cancel()
        val state = _activeWorkout.value ?: return
        
        if (state.currentSetCount < 4) {
            val nextSet = state.currentSetCount + 1
            _activeWorkout.update {
                it?.copy(
                    isResting = false,
                    currentSetCount = nextSet,
                    setTimerSeconds = 0,
                    isSetTimerRunning = false
                )
            }
            com.example.ui.audio.PremiumSoundManager.playRestTimerComplete(getApplication())
        } else {
            val nextIndex = state.currentExerciseIndex + 1
            if (nextIndex < state.totalExercises) {
                val nextExerciseName = state.exercises.getOrNull(nextIndex) ?: "Exercise"
                _activeWorkout.update {
                    it?.copy(
                        isResting = false,
                        currentExerciseIndex = nextIndex,
                        currentSetCount = 1,
                        setTimerSeconds = 0,
                        isSetTimerRunning = false,
                        completedExercisesCount = nextIndex
                    )
                }
                com.example.ui.audio.PremiumSoundManager.playRestTimerComplete(getApplication())
                com.example.ui.audio.PremiumTTSManager.announceNextExercise(
                    exerciseName = nextExerciseName,
                    lang = TranslationHelper.currentLanguage,
                    context = getApplication()
                )
            }
        }
        saveActiveStateToDb(_activeWorkout.value)
    }

    fun togglePlayPause() {
        _activeWorkout.update {
            if (it != null) {
                val nextPaused = !it.isPaused
                it.copy(isPaused = nextPaused)
            } else {
                null
            }
        }
        saveActiveStateToDb(_activeWorkout.value)
    }

    fun previousExercise() {
        _activeWorkout.update {
            if (it == null) return@update null
            val prevIndex = (it.currentExerciseIndex - 1).coerceAtLeast(0)
            it.copy(
                currentExerciseIndex = prevIndex,
                currentSetCount = 1,
                completedExercisesCount = prevIndex,
                isResting = false,
                setTimerSeconds = 0,
                isSetTimerRunning = false
            )
        }
        saveActiveStateToDb(_activeWorkout.value)
    }

    fun nextExercise() {
        val state = _activeWorkout.value ?: return
        val nextIndex = state.currentExerciseIndex + 1
        if (nextIndex < state.totalExercises) {
            val nextExerciseName = state.exercises.getOrNull(nextIndex) ?: "Exercise"
            _activeWorkout.update {
                it?.copy(
                    currentExerciseIndex = nextIndex,
                    currentSetCount = 1,
                    completedExercisesCount = nextIndex,
                    isResting = false,
                    setTimerSeconds = 0,
                    isSetTimerRunning = false,
                    flashMessage = "NEXT EXERCISE!"
                )
            }
            saveActiveStateToDb(_activeWorkout.value)
            com.example.ui.audio.PremiumTTSManager.announceNextExercise(
                exerciseName = nextExerciseName,
                lang = TranslationHelper.currentLanguage,
                context = getApplication()
            )
        } else {
            finishActiveWorkout()
        }
    }

    // Skip Rest
    fun skipRestTimer() {
        handleRestTimerCompletion()
    }

    private fun finishActiveWorkout() {
        coachTimerJob?.cancel()
        restTimerJob?.cancel()
        setTimerJob?.cancel()
        val state = _activeWorkout.value ?: return

        val durationMin = (state.elapsedSeconds / 60).coerceAtLeast(1)
        val calories = state.caloriesReward.coerceAtLeast(5)

        viewModelScope.launch {
            repository.logCompletedWorkout(
                planName = state.planName,
                date = todayString,
                durationMinutes = durationMin,
                calories = calories,
                completedCount = state.totalExercises,
                totalCount = state.totalExercises,
                personalRecords = "Personal Record: ${state.exercises.firstOrNull() ?: "Workout"} Completed with ${state.totalExercises * 4} sets!"
            )
            
            // Increment streak count on today's workout completion
            val profile = repository.getProfileDirect()
            if (profile != null) {
                val lastDate = profile.lastWorkoutDate
                val today = todayString
                val newStreak = if (lastDate == today) {
                    profile.streakCount
                } else if (lastDate == null) {
                    1
                } else {
                    try {
                        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        val last = sdf.parse(lastDate)
                        val curr = sdf.parse(today)
                        if (last != null && curr != null) {
                            val diff = curr.time - last.time
                            val diffDays = diff / (24 * 60 * 60 * 1000)
                            if (diffDays <= 1L) {
                                profile.streakCount + 1
                            } else {
                                1
                            }
                        } else {
                            1
                        }
                    } catch (e: Exception) {
                        1
                    }
                }
                repository.saveProfile(profile.copy(streakCount = newStreak, lastWorkoutDate = today))
            }

            // Add dynamic victory notification
            addDynamicNotification(
                title = "🏆 Workout Completed!",
                text = "Incredible effort! You finished ${state.planName}, completed ${state.totalExercises} exercises, burned $calories kcal in $durationMin mins.",
                buttonLabel = "VICTORY!"
            )
            
            // Celebrate active state complete
            _activeWorkout.update {
                it?.copy(
                    isFinished = true,
                    caloriesReward = calories,
                    elapsedSeconds = state.elapsedSeconds
                )
            }
            // Clear active workout state from DB since it's completed
            repository.clearActiveWorkoutState()
            
            com.example.ui.audio.PremiumSoundManager.playWorkoutComplete(getApplication())
            com.example.ui.audio.PremiumTTSManager.announceWorkoutComplete(
                lang = TranslationHelper.currentLanguage,
                context = getApplication()
            )
        }
    }

    fun dismissActiveWorkout() {
        coachTimerJob?.cancel()
        restTimerJob?.cancel()
        setTimerJob?.cancel()
        _activeWorkout.value = null
        viewModelScope.launch {
            repository.clearActiveWorkoutState()
        }
    }
}

// --- STATE DEFINITIONS ---
sealed interface AuthState {
    object LoggedOut : AuthState
    object Guest : AuthState
    data class LoggedIn(val email: String) : AuthState
}

data class ActiveWorkoutState(
    val planName: String,
    val totalExercises: Int,
    val exercises: List<String>,
    val estimatedLength: Int,
    val difficulty: String,
    val currentExerciseIndex: Int = 0,
    val currentSetCount: Int = 1,
    val countdownSeconds: Int = 3, // For 3-2-1 start countdown
    val isStarted: Boolean = false,
    val isResting: Boolean = false,
    val restSecondsLeft: Int = 90,
    val elapsedSeconds: Int = 0,
    val completedExercisesCount: Int = 0,
    val isFinished: Boolean = false,
    val caloriesReward: Int = 0,
    val voiceNotification: String = "",
    val flashMessage: String = "",
    val isPaused: Boolean = false,
    val setTimerSeconds: Int = 0,
    val isSetTimerRunning: Boolean = false,
    val targetSetDuration: Int = 30
)

data class GymNotification(
    val id: String,
    val title: String,
    val text: String,
    val buttonLabel: String,
    val isRead: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)
