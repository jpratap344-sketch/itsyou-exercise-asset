package com.example.ui.translation

enum class AppLanguage(val displayName: String) {
    ENGLISH("English"),
    HINDI("हिन्दी (Hindi)"),
    HINGLISH("Hinglish")
}

object TranslationHelper {

    // Cache of custom selected language (default to Hinglish)
    var currentLanguage = AppLanguage.HINGLISH

    // UI Translation dictionary
    private val uiTranslations = mapOf(
        // --- Dashboard / Header Text ---
        "DASHBOARD" to mapOf(
            AppLanguage.ENGLISH to "DASHBOARD",
            AppLanguage.HINDI to "डैशबोर्ड",
            AppLanguage.HINGLISH to "DASHBOARD"
        ),
        "ELITE ATHLETICS SUITE" to mapOf(
            AppLanguage.ENGLISH to "ELITE ATHLETICS SUITE",
            AppLanguage.HINDI to "अभिजात वर्ग एथलेटिक्स सूट",
            AppLanguage.HINGLISH to "ELITE ATHLETICS SUITE"
        ),
        "ACTIVE STREAK" to mapOf(
            AppLanguage.ENGLISH to "ACTIVE STREAK",
            AppLanguage.HINDI to "सक्रिय स्ट्रीक",
            AppLanguage.HINGLISH to "ACTIVE STREAK"
        ),
        "DAYS" to mapOf(
            AppLanguage.ENGLISH to "DAYS",
            AppLanguage.HINDI to "दिन",
            AppLanguage.HINGLISH to "DAYS"
        ),
        "WORKOUT SHIELD ACTIVE" to mapOf(
            AppLanguage.ENGLISH to "WORKOUT SHIELD ACTIVE",
            AppLanguage.HINDI to "वर्कआउट शील्ड चालू है",
            AppLanguage.HINGLISH to "WORKOUT SHIELD ACTIVE"
        ),
        "TODAY'S DATE" to mapOf(
            AppLanguage.ENGLISH to "TODAY'S DATE",
            AppLanguage.HINDI to "आज की तारीख",
            AppLanguage.HINGLISH to "TODAY'S DATE"
        ),
        "RECOMMENDED TARGET ROUTINES" to mapOf(
            AppLanguage.ENGLISH to "RECOMMENDED TARGET ROUTINES",
            AppLanguage.HINDI to "अनुशंसित लक्ष्य दिनचर्या",
            AppLanguage.HINGLISH to "RECOMMENDED TARGET ROUTINES"
        ),
        "BUILD YOUR CUSTOM ROUTINE" to mapOf(
            AppLanguage.ENGLISH to "BUILD YOUR CUSTOM ROUTINE",
            AppLanguage.HINDI to "अपनी पसंद का वर्कआउट बनाएं",
            AppLanguage.HINGLISH to "Apna custom routine banayein"
        ),
        "CHOOSE LEVEL" to mapOf(
            AppLanguage.ENGLISH to "CHOOSE LEVEL",
            AppLanguage.HINDI to "लेवल चुनें",
            AppLanguage.HINGLISH to "Level select karein"
        ),
        "BEGINNER" to mapOf(
            AppLanguage.ENGLISH to "BEGINNER",
            AppLanguage.HINDI to "शुरुआती",
            AppLanguage.HINGLISH to "BEGINNER"
        ),
        "INTERMEDIATE" to mapOf(
            AppLanguage.ENGLISH to "INTERMEDIATE",
            AppLanguage.HINDI to "मध्यम",
            AppLanguage.HINGLISH to "INTERMEDIATE"
        ),
        "ADVANCED" to mapOf(
            AppLanguage.ENGLISH to "ADVANCED",
            AppLanguage.HINDI to "एडवांस",
            AppLanguage.HINGLISH to "ADVANCED"
        ),
        "LAUNCH ELITE SMART COACH" to mapOf(
            AppLanguage.ENGLISH to "LAUNCH ELITE SMART COACH",
            AppLanguage.HINDI to "इलीट स्मार्ट कोच शुरू करें",
            AppLanguage.HINGLISH to "Launch Elite Smart Coach"
        ),
        "MINUTES" to mapOf(
            AppLanguage.ENGLISH to "MINUTES",
            AppLanguage.HINDI to "मिनट",
            AppLanguage.HINGLISH to "MINUTES"
        ),
        "EXERCISES" to mapOf(
            AppLanguage.ENGLISH to "EXERCISES",
            AppLanguage.HINDI to "व्यायाम",
            AppLanguage.HINGLISH to "EXERCISES"
        ),

        // --- Water Intake Section ---
        "DAILY WATER INTENDED MATRIX" to mapOf(
            AppLanguage.ENGLISH to "DAILY WATER INTENDED MATRIX",
            AppLanguage.HINDI to "दैनिक जल सेवन मैट्रिक्स",
            AppLanguage.HINGLISH to "DAILY WATER MASS MATRIX"
        ),
        "TARGET TOTAL" to mapOf(
            AppLanguage.ENGLISH to "TARGET TOTAL",
            AppLanguage.HINDI to "कुल लक्ष्य",
            AppLanguage.HINGLISH to "TARGET TOTAL"
        ),
        "LOGGED TODAY" to mapOf(
            AppLanguage.ENGLISH to "LOGGED TODAY",
            AppLanguage.HINDI to "आज दर्ज किया गया",
            AppLanguage.HINGLISH to "Logged Today"
        ),
        "ADD 250ML GLASS" to mapOf(
            AppLanguage.ENGLISH to "ADD 250ML GLASS",
            AppLanguage.HINDI to "+२५० मिली गिलास",
            AppLanguage.HINGLISH to "+250ml Glass add karein"
        ),
        "ADD 500ML SHAKER" to mapOf(
            AppLanguage.ENGLISH to "ADD 500ML SHAKER",
            AppLanguage.HINDI to "+५०० मिली शेकर",
            AppLanguage.HINGLISH to "+500ml Shaker add karein"
        ),
        "RESET DRINKS" to mapOf(
            AppLanguage.ENGLISH to "RESET DRINKS",
            AppLanguage.HINDI to "जल रिकॉर्ड रीसेट करें",
            AppLanguage.HINGLISH to "Water clear-off karein"
        ),
        "REMAINING AMOUNT" to mapOf(
            AppLanguage.ENGLISH to "REMAINING AMOUNT",
            AppLanguage.HINDI to "बची हुई मात्रा",
            AppLanguage.HINGLISH to "Remaining Amount"
        ),
        "WATER CONSUMPTION CALCULATOR" to mapOf(
            AppLanguage.ENGLISH to "WATER CONSUMPTION CALCULATOR",
            AppLanguage.HINDI to "जल उपभोग कैलकुलेटर",
            AppLanguage.HINGLISH to "Water Consumption Calculator"
        ),
        "SUGGESTED DAILY WORKOUT" to mapOf(
            AppLanguage.ENGLISH to "SUGGESTED DAILY WORKOUT",
            AppLanguage.HINDI to "सुझाया गया दैनिक वर्कआउट",
            AppLanguage.HINGLISH to "Suggested Daily Workout"
        ),
        "TODAY'S SELECTION preset" to mapOf(
            AppLanguage.ENGLISH to "TODAY'S SELECTION PRESET",
            AppLanguage.HINDI to "आज का विशेष चयन",
            AppLanguage.HINGLISH to "Today's selection preset"
        ),
        "Intermediate Upper Split" to mapOf(
            AppLanguage.ENGLISH to "Intermediate Upper Split",
            AppLanguage.HINDI to "मध्यम श्रेणी अपर स्प्लिट",
            AppLanguage.HINGLISH to "Intermediate Upper Split"
        ),
        "HYPERTROPHY_DESC" to mapOf(
            AppLanguage.ENGLISH to "An advanced hypertrophy dynamic setup for high density chest and lat stimulation. Targets Pecs, Lats, Arms.",
            AppLanguage.HINDI to "उच्च घनत्व छाती और लैट उत्तेजना के लिए एक उन्नत हाइपरट्रॉफी गतिशील सेटअप। पेक्स, लैट्स, आर्म्स को लक्षित करता है।",
            AppLanguage.HINGLISH to "Hypertrophy dynamic target setup for deep chest and lats pressure stimulation. Target: Chest, Lats, Arms."
        ),
        "OPTIMAL HYDRATION" to mapOf(
            AppLanguage.ENGLISH to "OPTIMAL HYDRATION REACHED!",
            AppLanguage.HINDI to "इष्टतम जलयोजन पूरा हुआ!",
            AppLanguage.HINGLISH to "Optimal hydration reach ho gaya!"
        ),

        // --- Progress Tracking Section ---
        "METRIC TRACKING CENTER" to mapOf(
            AppLanguage.ENGLISH to "METRIC TRACKING CENTER",
            AppLanguage.HINDI to "मीट्रिक ट्रैकिंग सेंटर",
            AppLanguage.HINGLISH to "METRIC TRACKING CENTER"
        ),
        "BODY WEIGHT HISTORY LOGGER" to mapOf(
            AppLanguage.ENGLISH to "BODY WEIGHT HISTORY LOGGER",
            AppLanguage.HINDI to "शरीर के वजन का इतिहास",
            AppLanguage.HINGLISH to "Body weight history logger"
        ),
        "ENTER NEW WEIGHT" to mapOf(
            AppLanguage.ENGLISH to "ENTER NEW WEIGHT (KG)",
            AppLanguage.HINDI to "नया वजन दर्ज करें (किलोग्राम)",
            AppLanguage.HINGLISH to "Naya weight enter karein (kg)"
        ),
        "SAVE RECORD" to mapOf(
            AppLanguage.ENGLISH to "SAVE RECORD",
            AppLanguage.HINDI to "रिकॉर्ड सुरक्षित करें",
            AppLanguage.HINGLISH to "Record save karein"
        ),
        "WEIGHT HISTORY GRAPH & RECORDS" to mapOf(
            AppLanguage.ENGLISH to "WEIGHT HISTORY GRAPH & RECORDS",
            AppLanguage.HINDI to "वजन का इतिहास ग्राफ और रिकॉर्ड",
            AppLanguage.HINGLISH to "Weight history graph and records"
        ),
        "NO WEIGHT LOGS AVAILABLE" to mapOf(
            AppLanguage.ENGLISH to "No weight logs recorded yet.",
            AppLanguage.HINDI to "अभी तक कोई वजन रिकॉर्ड नहीं किया गया है।",
            AppLanguage.HINGLISH to "Abhi tak koi weight record nahi kiya gaya."
        ),
        "LOGGED AT" to mapOf(
            AppLanguage.ENGLISH to "Logged at",
            AppLanguage.HINDI to "दर्ज किया गया समय",
            AppLanguage.HINGLISH to "Logged at"
        ),
        "DELETE" to mapOf(
            AppLanguage.ENGLISH to "Delete",
            AppLanguage.HINDI to "मटाएं",
            AppLanguage.HINGLISH to "Delete"
        ),
        "WORKOUT HISTORY LOGS" to mapOf(
            AppLanguage.ENGLISH to "WORKOUT HISTORY LOGS",
            AppLanguage.HINDI to "वर्कआउट इतिहास लॉग",
            AppLanguage.HINGLISH to "Workout history logs"
        ),
        "NO LOGGED WORKOUTS YET" to mapOf(
            AppLanguage.ENGLISH to "No logged workouts completed yet. Start your journey!",
            AppLanguage.HINDI to "अभी तक कोई वर्कआउट पूरा नहीं हुआ है। अपनी यात्रा शुरू करें!",
            AppLanguage.HINGLISH to "Abhi tak koi workout complete nahi hua. Apna training shuru karein!"
        ),

        // --- Settings Screen ---
        "USER PROFILE SETTINGS" to mapOf(
            AppLanguage.ENGLISH to "USER PROFILE SETTINGS",
            AppLanguage.HINDI to "यूज़र प्रोफ़ाइल सेटिंग्स",
            AppLanguage.HINGLISH to "USER PROFILE SETTINGS"
        ),
        "BIOMETRIC SETUP" to mapOf(
            AppLanguage.ENGLISH to "BIOMETRIC SETUP",
            AppLanguage.HINDI to "बायोमेट्रिक सेटअप",
            AppLanguage.HINGLISH to "BIOMETRIC SETUP"
        ),
        "DISPLAY NAME" to mapOf(
            AppLanguage.ENGLISH to "Display Name",
            AppLanguage.HINDI to "नाम प्रदर्शित करें",
            AppLanguage.HINGLISH to "Display Name"
        ),
        "AGE (YEARS)" to mapOf(
            AppLanguage.ENGLISH to "Age (Years)",
            AppLanguage.HINDI to "आयु (वर्ष)",
            AppLanguage.HINGLISH to "Age (Years)"
        ),
        "GENDER" to mapOf(
            AppLanguage.ENGLISH to "Gender",
            AppLanguage.HINDI to "लिंग",
            AppLanguage.HINGLISH to "Gender"
        ),
        "HEIGHT (CM)" to mapOf(
            AppLanguage.ENGLISH to "Height (cm)",
            AppLanguage.HINDI to "ऊंचाई (सेमी)",
            AppLanguage.HINGLISH to "Height (cm)"
        ),
        "WEIGHT (KG)" to mapOf(
            AppLanguage.ENGLISH to "Weight (kg)",
            AppLanguage.HINDI to "वजन (किग्रा)",
            AppLanguage.HINGLISH to "Weight (kg)"
        ),
        "PREPARE YOUR MIND" to mapOf(
            AppLanguage.ENGLISH to "PREPARE YOUR MIND",
            AppLanguage.HINDI to "अपना मन तैयार करें",
            AppLanguage.HINGLISH to "Apna mind tayaar karein"
        ),
        "ITSYOU IS REBUILDING NOW..." to mapOf(
            AppLanguage.ENGLISH to "ITSYOU IS REBUILDING NOW...",
            AppLanguage.HINDI to "ITSYOU अब आपका पुनर्निर्माण कर रहा है...",
            AppLanguage.HINGLISH to "ITSYOU ab aapko rebuild kar raha hai..."
        ),
        "WORKOUT RECORDED!" to mapOf(
            AppLanguage.ENGLISH to "WORKOUT RECORDED!",
            AppLanguage.HINDI to "वर्कआउट दर्ज हो गया!",
            AppLanguage.HINGLISH to "Workout save ho gaya!"
        ),
        "METRIC RESULTS REWARD" to mapOf(
            AppLanguage.ENGLISH to "METRIC RESULTS REWARD",
            AppLanguage.HINDI to "मीट्रिक परिणाम पुरस्कार",
            AppLanguage.HINGLISH to "Workout metrics complete"
        ),
        "TOTAL TIME" to mapOf(
            AppLanguage.ENGLISH to "TOTAL TIME",
            AppLanguage.HINDI to "कुल समय",
            AppLanguage.HINGLISH to "Total Time"
        ),
        "BURN ESTIMATE" to mapOf(
            AppLanguage.ENGLISH to "BURN ESTIMATE",
            AppLanguage.HINDI to "अनुमानित कैलोरी बर्न",
            AppLanguage.HINGLISH to "Burn estimate"
        ),
        "SUCCESS" to mapOf(
            AppLanguage.ENGLISH to "SUCCESS",
            AppLanguage.HINDI to "सफलता",
            AppLanguage.HINGLISH to "Success"
        ),
        "UNLOCKED ACHIVEMENT REWARDS" to mapOf(
            AppLanguage.ENGLISH to "UNLOCKED ACHIEVEMENT REWARDS",
            AppLanguage.HINDI to "अनलॉक की गई उपलब्धियां",
            AppLanguage.HINGLISH to "Unlocked rewards"
        ),
        "RETURN TO GYM DASHBOARD" to mapOf(
            AppLanguage.ENGLISH to "RETURN TO GYM DASHBOARD",
            AppLanguage.HINDI to "जिम डैशबोर्ड पर वापस जाएं",
            AppLanguage.HINGLISH to "Dashboard par waapas chaliye"
        ),
        "EXERCISE" to mapOf(
            AppLanguage.ENGLISH to "EXERCISE",
            AppLanguage.HINDI to "व्यायाम",
            AppLanguage.HINGLISH to "Exercise"
        ),
        "OF" to mapOf(
            AppLanguage.ENGLISH to "OF",
            AppLanguage.HINDI to "का",
            AppLanguage.HINGLISH to "ka"
        ),
        "Routine Completion:" to mapOf(
            AppLanguage.ENGLISH to "Routine Completion:",
            AppLanguage.HINDI to "दिनचर्या पूर्णता:",
            AppLanguage.HINGLISH to "Routine completion:"
        ),
        "min elapsed" to mapOf(
            AppLanguage.ENGLISH to "min elapsed",
            AppLanguage.HINDI to "मिनट बीत चुके हैं",
            AppLanguage.HINGLISH to "min ho gaye"
        ),
        "Suggested Target Intensity: 4 Sets x 8-10 Reps" to mapOf(
            AppLanguage.ENGLISH to "Suggested Target Intensity: 4 Sets x 8-10 Reps",
            AppLanguage.HINDI to "सुझाया गया लक्ष्य: ४ सेट x ८-१० रेप्स",
            AppLanguage.HINGLISH to "Target recommendation: 4 Sets x 8-10 Reps"
        ),
        "COMPLETED SETS" to mapOf(
            AppLanguage.ENGLISH to "COMPLETED SETS",
            AppLanguage.HINDI to "पूरे किए गए सेट",
            AppLanguage.HINGLISH to "Sets complete"
        ),
        "+ SET" to mapOf(
            AppLanguage.ENGLISH to "+ SET",
            AppLanguage.HINDI to "+ सेट",
            AppLanguage.HINGLISH to "+ Set"
        ),
        "REST PERIOD - BREATHE" to mapOf(
            AppLanguage.ENGLISH to "REST PERIOD - BREATHE",
            AppLanguage.HINDI to "विश्राम का समय - सांस लें",
            AppLanguage.HINGLISH to "Rest time - Lambi saans lein"
        ),
        "SKIP REST" to mapOf(
            AppLanguage.ENGLISH to "SKIP REST",
            AppLanguage.HINDI to "विश्राम छोड़ें",
            AppLanguage.HINGLISH to "Rest skip karein"
        ),
        "FINISH WORKOUT ROUTINE" to mapOf(
            AppLanguage.ENGLISH to "FINISH WORKOUT ROUTINE",
            AppLanguage.HINDI to "वर्कआउट समाप्त करें",
            AppLanguage.HINGLISH to "Workout end karein"
        ),
        "LOG COMPLETED SET" to mapOf(
            AppLanguage.ENGLISH to "LOG COMPLETED SET",
            AppLanguage.HINDI to "सेट दर्ज करें",
            AppLanguage.HINGLISH to "Completed set log karein"
        ),
        "GYM TARGET GOAL FOCUS" to mapOf(
            AppLanguage.ENGLISH to "Gym Target Goal Focus",
            AppLanguage.HINDI to "जिम लक्ष्य फोकस",
            AppLanguage.HINGLISH to "Gym Target Goal Focus"
        ),
        "BASELINE DAILY ACTIVITY LEVEL" to mapOf(
            AppLanguage.ENGLISH to "Baseline Daily Activity Level",
            AppLanguage.HINDI to "दैनिक गतिविधि स्तर",
            AppLanguage.HINGLISH to "Daily Activity Level"
        ),
        "SAVE PROFILE BIOMETRICS" to mapOf(
            AppLanguage.ENGLISH to "SAVE PROFILE BIOMETRICS",
            AppLanguage.HINDI to "प्रोफ़ाइल बायोमेट्रिक्स सहेजें",
            AppLanguage.HINGLISH to "Biography save karein"
        ),
        "APP LANGUAGE SELECTION" to mapOf(
            AppLanguage.ENGLISH to "APP LANGUAGE SELECTION",
            AppLanguage.HINDI to "ऐप भाषा चयन",
            AppLanguage.HINGLISH to "APP LANGUAGE SELECTION"
        ),
        "CHOOSE LANGUAGE" to mapOf(
            AppLanguage.ENGLISH to "Choose Language",
            AppLanguage.HINDI to "भाषा चुनें",
            AppLanguage.HINGLISH to "Language select karein"
        ),

        // --- Exercise Details Sheet / Smart Coach UI ---
        "STEP-BY-STEP MOVEMENT GUIDE" to mapOf(
            AppLanguage.ENGLISH to "STEP-BY-STEP MOVEMENT GUIDE",
            AppLanguage.HINDI to "चरण-दर-चरण व्यायाम गाइड",
            AppLanguage.HINGLISH to "Step-by-Step Movement Guide"
        ),
        "BEGINNER START WEIGHT" to mapOf(
            AppLanguage.ENGLISH to "BEGINNER START WEIGHT",
            AppLanguage.HINDI to "शुरुआती शुरुआती वजन",
            AppLanguage.HINGLISH to "Beginner Start Weight"
        ),
        "EQUIPMENT BASE" to mapOf(
            AppLanguage.ENGLISH to "EQUIPMENT BASE",
            AppLanguage.HINDI to "आवश्यक उपकरण",
            AppLanguage.HINGLISH to "Equipment Base"
        ),
        "TARGETED PHYSIQUE PREP & CARE" to mapOf(
            AppLanguage.ENGLISH to "TARGETED PHYSIQUE PREP & CARE",
            AppLanguage.HINDI to "लक्ष्य शारीरिक तैयारी और देखभाल",
            AppLanguage.HINGLISH to "Targeted Physique Prep & Care"
        ),
        "Warm-up Before Exercise:" to mapOf(
            AppLanguage.ENGLISH to "Warm-up Before Exercise:",
            AppLanguage.HINDI to "व्यायाम से पहले वार्म-अप:",
            AppLanguage.HINGLISH to "Exercise se pehle Warm-up:"
        ),
        "Post-workout Muscle Stretch:" to mapOf(
            AppLanguage.ENGLISH to "Post-workout Muscle Stretch:",
            AppLanguage.HINDI to "वर्कआउट के बाद मांसपेशियों का खिंचाव:",
            AppLanguage.HINGLISH to "Workout ke baad Muscle Stretch:"
        ),
        "COMMON CRITICAL MISTAKES" to mapOf(
            AppLanguage.ENGLISH to "COMMON CRITICAL MISTAKES",
            AppLanguage.HINDI to "आम गंभीर गलतियाँ",
            AppLanguage.HINGLISH to "Common Critical Mistakes"
        ),

        // --- Timeline Steps ---
        "MACHINE SETUP" to mapOf(
            AppLanguage.ENGLISH to "MACHINE SETUP",
            AppLanguage.HINDI to "मशीन सेटअप",
            AppLanguage.HINGLISH to "Machine Setup"
        ),
        "START POSITION" to mapOf(
            AppLanguage.ENGLISH to "START POSITION",
            AppLanguage.HINDI to "शुरुआती स्थिति",
            AppLanguage.HINGLISH to "Start Position"
        ),
        "MOVEMENT PATH" to mapOf(
            AppLanguage.ENGLISH to "MOVEMENT PATH",
            AppLanguage.HINDI to "गतिविधि पथ",
            AppLanguage.HINGLISH to "Movement Path"
        ),
        "END POSITION" to mapOf(
            AppLanguage.ENGLISH to "END POSITION",
            AppLanguage.HINDI to "अंतिम स्थिति",
            AppLanguage.HINGLISH to "End Position"
        ),
        "RETURN POSTURE" to mapOf(
            AppLanguage.ENGLISH to "RETURN POSTURE",
            AppLanguage.HINDI to "वापसी मुद्रा",
            AppLanguage.HINGLISH to "Return Posture"
        )
    )

    // Dynamic strings translated based on active language or returning translated value
    fun translateUI(key: String, lang: AppLanguage = currentLanguage): String {
        val normalized = key.trim()
        val entry = uiTranslations[normalized]
        if (entry != null) {
            return entry[lang] ?: key
        }
        // Partial matches for common titles
        for ((uKey, uLangMap) in uiTranslations) {
            if (normalized.equals(uKey, ignoreCase = true)) {
                return uLangMap[lang] ?: key
            }
        }
        return key
    }

    // Translate dynamic values of Exercises
    fun translateExerciseName(name: String, lang: AppLanguage = currentLanguage): String {
        if (lang == AppLanguage.ENGLISH) return name
        val normalized = name.lowercase().trim()
        return when {
            normalized.contains("bench press") && !normalized.contains("incline") && !normalized.contains("decline") && !normalized.contains("close") -> {
                if (lang == AppLanguage.HINDI) "बेंच प्रेस" else "Bench Press"
            }
            normalized.contains("incline dumbbell press") -> {
                if (lang == AppLanguage.HINDI) "इन्क्लाइन डम्बल प्रेस" else "Incline Dumbbell Press"
            }
            normalized.contains("decline barbell press") -> {
                if (lang == AppLanguage.HINDI) "डिक्लाइन बारबेल प्रेस" else "Decline Barbell Press"
            }
            normalized.contains("cable crossover") && normalized.contains("high-to-low") -> {
                if (lang == AppLanguage.HINDI) "केबल क्रॉसओवर हाई-टू-लो" else "Cable Crossover High-to-Low"
            }
            normalized.contains("cable crossover") && normalized.contains("low-to-high") -> {
                if (lang == AppLanguage.HINDI) "केबल क्रॉसओवर लो-टू-हाई" else "Cable Crossover Low-to-High"
            }
            normalized.contains("pec deck fly") -> {
                if (lang == AppLanguage.HINDI) "पेक डेक फ्लाई" else "Pec Deck Fly"
            }
            normalized.contains("hammer strength chest press") -> {
                if (lang == AppLanguage.HINDI) "हैमर चेस्ट प्रेस" else "Hammer Strength Chest Press"
            }
            normalized.contains("pullover") -> {
                if (lang == AppLanguage.HINDI) "डम्बल पुलओवर" else "Dumbbell Pullover"
            }
            normalized.contains("push-up") || normalized.contains("push up") || normalized.contains("pushups") -> {
                if (lang == AppLanguage.HINDI) "पुश-अप्स" else "Push-ups"
            }
            normalized.contains("weighted dips") || normalized.contains("bodyweight dips") -> {
                if (lang == AppLanguage.HINDI) "वेटेड डिप्स" else "Weighted Dips"
            }
            normalized.contains("chest press machine") -> {
                if (lang == AppLanguage.HINDI) "चेस्ट प्रेस मशीन" else "Chest Press Machine"
            }
            normalized.contains("lat pulldown") -> {
                if (lang == AppLanguage.HINDI) "लैट पुलडाउन" else "Lat Pulldown"
            }
            normalized.contains("cable row") || normalized.contains("seated row") -> {
                if (lang == AppLanguage.HINDI) "सीटेड केबल रो" else "Seated Cable Row"
            }
            normalized.contains("deadlift") && !normalized.contains("romanian") -> {
                if (lang == AppLanguage.HINDI) "डेडलिफ्ट" else "Deadlift"
            }
            normalized.contains("romanian deadlift") -> {
                if (lang == AppLanguage.HINDI) "रोमानियन डेडलिफ्ट" else "Romanian Deadlift"
            }
            normalized.contains("t-bar row") -> {
                if (lang == AppLanguage.HINDI) "टी-बार रो" else "T-Bar Row"
            }
            normalized.contains("dumbbell row") -> {
                if (lang == AppLanguage.HINDI) "डम्बल रो" else "Dumbbell Row"
            }
            normalized.contains("pull-up") || normalized.contains("pull up") || normalized.contains("chin-up") || normalized.contains("chin up") -> {
                if (lang == AppLanguage.HINDI) "पुल-अप्स" else "Pull-Ups"
            }
            normalized.contains("hyperextension") || normalized.contains("extensions") && normalized.contains("back") -> {
                if (lang == AppLanguage.HINDI) "बैक हाइपर-एक्सटेंशन" else "Back Hyperextensions"
            }
            normalized.contains("face pulls") || normalized.contains("face pull") -> {
                if (lang == AppLanguage.HINDI) "केबल फेस पुल्स" else "Cable Face Pulls"
            }
            normalized.contains("shrug") -> {
                if (lang == AppLanguage.HINDI) "डम्बल श्रग्स" else "Dumbbell Shrugs"
            }
            normalized.contains("shoulder press") || normalized.contains("seated dumbbell press") -> {
                if (lang == AppLanguage.HINDI) "शोल्डर प्रेस" else "Shoulder Press"
            }
            normalized.contains("lateral raise") -> {
                if (lang == AppLanguage.HINDI) "लेटरल रेज़" else "Lateral Raises"
            }
            normalized.contains("front raise") -> {
                if (lang == AppLanguage.HINDI) "फ्रंट रेज़" else "Front Raises"
            }
            normalized.contains("arnold press") -> {
                if (lang == AppLanguage.HINDI) "आर्नोल्ड प्रेस" else "Arnold Press"
            }
            normalized.contains("upright row") -> {
                if (lang == AppLanguage.HINDI) "अपराइट रो" else "Upright Rows"
            }
            normalized.contains("rear delt") -> {
                if (lang == AppLanguage.HINDI) "रियर डेल्ट" else "Rear Delt Fly"
            }
            normalized.contains("barbell curl") -> {
                if (lang == AppLanguage.HINDI) "बारबेल कर्ल" else "Barbell Curl"
            }
            normalized.contains("dumbbell curl") -> {
                if (lang == AppLanguage.HINDI) "डम्बल कर्ल" else "Dumbbell Curl"
            }
            normalized.contains("hammer curl") -> {
                if (lang == AppLanguage.HINDI) "हैमर कर्ल" else "Hammer Curl"
            }
            normalized.contains("preacher curl") -> {
                if (lang == AppLanguage.HINDI) "प्रीचर कर्ल" else "Preacher Curl"
            }
            normalized.contains("concentration curl") -> {
                if (lang == AppLanguage.HINDI) "कंसंट्रेशन कर्ल" else "Concentration Curl"
            }
            normalized.contains("spider curl") -> {
                if (lang == AppLanguage.HINDI) "स्पाइडर कर्ल" else "Spider Curl"
            }
            normalized.contains("zottman curl") -> {
                if (lang == AppLanguage.HINDI) "ज़ॉटमैन कर्ल" else "Zottman Curl"
            }
            normalized.contains("rope pushdown") || normalized.contains("pushdown") -> {
                if (lang == AppLanguage.HINDI) "ट्राइसैप पुशडाउन" else "Tricep Pushdown"
            }
            normalized.contains("skull crusher") || normalized.contains("skullcrusher") -> {
                if (lang == AppLanguage.HINDI) "स्कल क्रशर्स" else "Skull Crushers"
            }
            normalized.contains("overhead tricep") || normalized.contains("overhead dumbbell") -> {
                if (lang == AppLanguage.HINDI) "ओवरहेड ट्राइसैप एक्सटेंशन" else "Overhead Tricep Extension"
            }
            normalized.contains("kickbacks") || normalized.contains("kickback") -> {
                if (lang == AppLanguage.HINDI) "ट्राइसैप किकबैक" else "Tricep Kickback"
            }
            normalized.contains("squat") -> {
                if (lang == AppLanguage.HINDI) "स्क्वाट (उठक-बैठक)" else "Squat"
            }
            normalized.contains("leg press") -> {
                if (lang == AppLanguage.HINDI) "लेग प्रेस" else "Leg Press"
            }
            normalized.contains("hack squat") -> {
                if (lang == AppLanguage.HINDI) "हैके स्क्वाट" else "Hack Squat"
            }
            normalized.contains("leg extension") -> {
                if (lang == AppLanguage.HINDI) "लेग एक्सटेंशन" else "Leg Extension"
            }
            normalized.contains("leg curl") -> {
                if (lang == AppLanguage.HINDI) "लेग कर्ल" else "Leg Curl"
            }
            normalized.contains("calf raise") -> {
                if (lang == AppLanguage.HINDI) "काफ रेज़ (पिंडलियों की कसरत)" else "Calf Raises"
            }
            normalized.contains("split squat") -> {
                if (lang == AppLanguage.HINDI) "स्प्लिट स्क्वाट" else "Split Squats"
            }
            normalized.contains("lunge") -> {
                if (lang == AppLanguage.HINDI) "लंजेस (फेफड़े की कसरत)" else "Lunges"
            }
            normalized.contains("hip thrust") -> {
                if (lang == AppLanguage.HINDI) "हिप थ्रस्ट" else "Hip Thrust"
            }
            normalized.contains("knee raise") || normalized.contains("leg raise") -> {
                if (lang == AppLanguage.HINDI) "नी/लेग रेज़" else "Knee/Leg Raises"
            }
            normalized.contains("crunch") || normalized.contains("crunches") -> {
                if (lang == AppLanguage.HINDI) "क्रंचेस (पेट की कसरत)" else "Crunches"
            }
            normalized.contains("ab wheel") || normalized.contains("rollout") -> {
                if (lang == AppLanguage.HINDI) "एब व्हील रोलआउट" else "Ab Wheel Rollout"
            }
            normalized.contains("plank") -> {
                if (lang == AppLanguage.HINDI) "प्लैक होल्ड" else "Plank Hold"
            }
            normalized.contains("russian twist") -> {
                if (lang == AppLanguage.HINDI) "रशियन ट्विस्ट" else "Russian Twist"
            }
            else -> name
        }
    }

    // Machine setup guide translations
    fun translateMachineSetup(setup: String, lang: AppLanguage = currentLanguage): String {
        if (lang == AppLanguage.ENGLISH) return setup
        val normalized = setup.lowercase().trim()
        return when {
            normalized.contains("barbell bench") -> {
                if (lang == AppLanguage.HINDI) "बारबेल बेंच समतल सतह" else "Barbell Flat Bench"
            }
            normalized.contains("incline bench") || normalized.contains("incline adjustable") -> {
                if (lang == AppLanguage.HINDI) "इन्क्लाइन बेंच व्यवस्था" else "Incline Bench setup"
            }
            normalized.contains("decline bench") || normalized.contains("decline adjustable") -> {
                if (lang == AppLanguage.HINDI) "डिक्लाइन बेंच व्यवस्था" else "Decline Bench setup"
            }
            normalized.contains("high pulley") || normalized.contains("high column") -> {
                if (lang == AppLanguage.HINDI) "हाई पुली केबल मशीन" else "High Pulley Cable Station"
            }
            normalized.contains("low pulley") || normalized.contains("low row") -> {
                if (lang == AppLanguage.HINDI) "लो पुली केबल मशीन" else "Low Pulley Cable Station"
            }
            normalized.contains("pec deck") -> {
                if (lang == AppLanguage.HINDI) "पेक डेक मशीन सीट" else "Pec Deck Machine Setup"
            }
            normalized.contains("chest press machine") || normalized.contains("hammer strength") -> {
                if (lang == AppLanguage.HINDI) "चेस्ट प्रेस मशीन सीट" else "Chest Press Machine Setup"
            }
            normalized.contains("floor setup") || normalized.contains("floor mat") || normalized.contains("open space") || normalized.contains("standing") -> {
                if (lang == AppLanguage.HINDI) "फर्श या खुली जगह" else "Floor / Open Gym Space"
            }
            normalized.contains("dip bar") || normalized.contains("captain") -> {
                if (lang == AppLanguage.HINDI) "डिप बार्स या कैप्टन चेयर" else "Dip Bars or Captain's Chair"
            }
            normalized.contains("squat rack") || normalized.contains("power rack") || normalized.contains("deadlift platform") -> {
                if (lang == AppLanguage.HINDI) "स्क्वाट रैक और बारबेल सेटअप" else "Squat Rack & Barbell Setup"
            }
            normalized.contains("leg press") -> {
                if (lang == AppLanguage.HINDI) "लेग प्रेस फुटबोर्ड मशीन" else "Leg Press Platform Setup"
            }
            else -> {
                if (lang == AppLanguage.HINDI) "उपयुक्त जिम मशीन सेट करें" else "Setup Gym Equipment"
            }
        }
    }

    // Warm-up details translation
    fun translateWarmUp(routine: String, lang: AppLanguage = currentLanguage): String {
        if (lang == AppLanguage.ENGLISH) return routine
        val normalized = routine.lowercase()
        return when {
            normalized.contains("arm circles") || normalized.contains("chest press") -> {
                if (lang == AppLanguage.HINDI) "५ मिनट बाइसेप/हाथ घुमाएं, हल्का चेस्ट प्रेस (१५ रेप्स)" 
                else "5 mins arm circles, light chest press (15 reps)"
            }
            normalized.contains("scapular") || normalized.contains("lat swing") -> {
                if (lang == AppLanguage.HINDI) "स्कैपुलर पुल (१५ रेप्स), गतिशील लैट स्विंग्स" 
                else "Scapular pulls (15 reps), dynamic lat swings"
            }
            normalized.contains("rotator") -> {
                if (lang == AppLanguage.HINDI) "केबल बैंड के साथ रोटेटर कफ वार्म-अप (हल्का वजन)" 
                else "Light rotator cuff exercises with cable band"
            }
            normalized.contains("shoulder") || normalized.contains("dynamic rotational") -> {
                if (lang == AppLanguage.HINDI) "५ मिनट कंधे रोटेशन कसरत और आर्म स्ट्रेच" 
                else "5 mins rotational shoulder movements & arm stretch"
            }
            normalized.contains("pushdown") -> {
                if (lang == AppLanguage.HINDI) "वार्म-अप ट्राइसैप पुशडाउन (१५ रेप्स, आधा वजन)" 
                else "Warm-up pushdowns (15 reps at 50% max)"
            }
            normalized.contains("squat") || normalized.contains("leg swing") -> {
                if (lang == AppLanguage.HINDI) "फ्री स्क्वाट्स (१५ रेप्स) और लेग स्विंग्स" 
                else "Bodyweight air squats (15 reps), leg swings"
            }
            normalized.contains("cat-cow") || normalized.contains("bird-dog") -> {
                if (lang == AppLanguage.HINDI) "कैट-काउ स्पाइनल खिंचाव और बर्ड-डॉग संतुलन बनाए रखें" 
                else "Cat-cow spinal stretches, bird-dog stabilization"
            }
            else -> {
                if (lang == AppLanguage.HINDI) "५ मिनट कार्डियो या हल्का वार्म-अप (१५ रेप्स)" 
                else "5 mins cardio or light warm-up set (15 reps)"
            }
        }
    }

    // Stretching details translation
    fun translateStretching(routine: String, lang: AppLanguage = currentLanguage): String {
        if (lang == AppLanguage.ENGLISH) return routine
        val normalized = routine.lowercase()
        return when {
            normalized.contains("doorway") -> {
                if (lang == AppLanguage.HINDI) "दरवाजे पर छाती को फैलाएं (दोनों तरफ ३० सेकंड)" 
                else "Doorway chest stretch (30 seconds each side)"
            }
            normalized.contains("behind-head") || normalized.contains("lat stretch") -> {
                if (lang == AppLanguage.HINDI) "सिर के पीछे लैट स्ट्रेच (दोनों ओर ३० सेकंड)" 
                else "Behind-head lat stretch (30s each side)"
            }
            normalized.contains("cross-body") -> {
                if (lang == AppLanguage.HINDI) "हाथ को गले के पार स्ट्रेच करें (दोनों ओर ३० सेकंड)" 
                else "Cross-body arm stretch (30s each side)"
            }
            normalized.contains("wall bicep") || normalized.contains("forearm") -> {
                if (lang == AppLanguage.HINDI) "दीवार के सहारे बाइसेप और कलाई स्ट्रेच (३० सेकंड)" 
                else "Wall bicep & forearm stretch (30s overall)"
            }
            normalized.contains("behind-neck") || normalized.contains("overhead") -> {
                if (lang == AppLanguage.HINDI) "सिर के पीछे से कोहनी पकड़कर ट्राइसैप स्ट्रेच" 
                else "Behind-neck overhead elbow stretch for triceps"
            }
            normalized.contains("quad") || normalized.contains("toe touch") -> {
                if (lang == AppLanguage.HINDI) "पैर पीछे खींचकर जांघ स्ट्रेच (३० से.) और पंजों को छुएं" 
                else "Deep quad stretch (30s) & floor toe touches"
            }
            normalized.contains("cobra") -> {
                if (lang == AppLanguage.HINDI) "फर्श पर लेटकर कोबरा स्ट्रेच (४५ सेकंड पेट के लिए)" 
                else "Cobra stretch on floor (45 seconds for abs)"
            }
            else -> {
                if (lang == AppLanguage.HINDI) "लक्षित मांसपेशियों का हल्का स्ट्रेच (३० सेकंड)" 
                else "Static target muscle stretching (30s overall)"
            }
        }
    }

    // Standard instructions mapper
    fun translateInstructions(stepIndex: Int, instruction: String, category: String, lang: AppLanguage = currentLanguage): String {
        if (lang == AppLanguage.ENGLISH) return instruction
        val cat = category.lowercase().trim()
        val index = stepIndex % 5
        
        if (lang == AppLanguage.HINDI) {
            return when (cat) {
                "chest" -> when (index) {
                    0 -> "अपने उपकरणों को सेट करें और आराम से बैठें या लेटें।"
                    1 -> "अपने कंधों को पीछे लॉक करने के लिए स्कैपुला को पीछे और नीचे खींचें।"
                    2 -> "वजन को मजबूती से पकड़ें और अपनी छाती को संलग्न करें।"
                    3 -> "वजन को नियंत्रित तरीके से धीरे-धीरे नीचे लाएं जब तक कि आपको गहरा खिंचाव महसूस न हो।"
                    else -> "साँस छोड़ते हुए वजन को गतिशील रूप से ऊपर की ओर धकेलें, शीर्ष पर अपनी छाती को सिकोड़ें।"
                }
                "back" -> when (index) {
                    0 -> "मशीन की ओर मुंह करके खुद को सुरक्षित रूप से स्थापित करें।"
                    1 -> "निर्दिष्ट चौड़ाई या अटैचमेंट का उपयोग करके हैंडल को पकड़ें।"
                    2 -> "अपने स्कैपुला को नीचे करें और अपने नितंबों को थोड़ा पीछे झुकाएं।"
                    3 -> "अपनी कोहनी को नीचे और अपनी रीढ़ की ओर पीछे खींचें, निचली छाती को लक्षित करें।"
                    else -> "प्रतिनिधित्व को पूरा करने के लिए अपनी बाहों को धीरे-धीरे वापस शुरुआती बिंदु पर लाएं।"
                }
                "shoulders" -> when (index) {
                    0 -> "अपने कंधों को गुरुत्वाकर्षण/प्रणाली के बिल्कुल समानांतर संरेखित करें।"
                    1 -> "डम्बल को अपने कानों के पास रखें या मशीन के हैंडल को पकड़ें।"
                    2 -> "अपने कोर को कस लें, पैरों को जमीन पर मजबूती से लॉक रखें।"
                    3 -> "वजन को सीधे अपने कंधे के जोड़ों के ऊपर लंबवत रूप से ऊपर ले जाएं।"
                    else -> "आंदोलन को पूरा करने के लिए धीरे-धीरे वजन वापस नीचे लाएं।"
                }
                "biceps" -> when (index) {
                    0 -> "अपने लक्षित वजन को सेट करें और फर्श पर सीधे खड़े हों।"
                    1 -> "अपनी कोहनी को अपनी पसलियों में मजबूती से सिकोड़ें और कलाईयों को ऊपर की ओर घुमाएं।"
                    2 -> "नियंत्रित वजन को अपनी छाती तक खींचने के लिए अपने बाइसेप्स को गतिशील रूप से सिकोड़ें।"
                    3 -> "अपनी कोहनी स्थिर रखें, उन्हें आगे की ओर न झूलने दें।"
                    else -> "सटीक नकारात्मक तनाव के तहत धीरे-धीरे वजन वापस नीचे लाएं।"
                }
                "triceps" -> when (index) {
                    0 -> "अपने सीने को थोड़ा झुकाकर हाई पुली के सामने खुद को स्थापित करें।"
                    1 -> "हाई चेस्ट लेवल पर रस्सी या बार अटैचमेंट को पकड़ें।"
                    2 -> "अपनी कोहनी को अपनी रिब केज में मजबूती से सिकोड़ें।"
                    3 -> "अपने ट्राइसेप्स को फ्लेक्स करके अपने हाथों को नीचे धकेलें जब तक कि बाहें पूरी तरह से लॉक न हो जाएं।"
                    else -> "अटैचमेंट को पूर्ण नियंत्रण में धीरे-धीरे वापस ऊपर जाने दें।"
                }
                "legs" -> when (index) {
                    0 -> "मशीन या सेटअप के अंदर खुद को सही तरीके से स्थापित करें।"
                    1 -> "हैंडल को पकड़ें या बारबेल को अपनी पीठ पर सुरक्षित रूप से लोड करें।"
                    2 -> "पैरों को कंधे की चौड़ाई के बराबर खोलकर खड़े हों और पंजों को थोड़ा बाहर की ओर रखें।"
                    3 -> "कूल्हों को मोड़कर नीचे की ओर बैठें या वजन धकेलें जब तक कि ग्लूट्स गहरे न हो जाएं।"
                    else -> "घुटनों और कूल्हों को एक साथ फैलाकर वजन को विस्फोटक ढंग से वापस ऊपर धकेलें।"
                }
                "abs" -> when (index) {
                    0 -> "फर्श या उपकरण पर अपनी ध्यान केंद्रित स्थिति तैयार करें।"
                    1 -> "अपने कोर को सिकोड़ें, अपने स्टर्नम को अपने हिप्स की तरफ लाएं।"
                    2 -> "एब्स के अधिकतम संकुचन के दौरान पूरी सांस बाहर छोड़ें।"
                    3 -> "2 पूरे सेकंड के लिए अपने कोर को मजबूती से सिकोड़ें।"
                    else -> "रेक्टस रेंज को पूरी तरह से खींचने के लिए तनाव के तहत धीरे-धीरे छोड़ें।"
                }
                else -> instruction
            }
        } else { // Hinglish
            return when (cat) {
                "chest" -> when (index) {
                    0 -> "Apne equipment ko set karein aur comfort ke anusar baithein ya letein."
                    1 -> "Apne shoulders ko peeche lock karne ke liye scapula ko retract aur depress karein."
                    2 -> "Weights ko majbooti se pakdein aur chest muscles ko engage karein."
                    3 -> "Weight ko slow controls ke saath neeche layen jab tak ek deep stretch feel na ho."
                    else -> "Saans chhodte hue weight ko dynamically upar press karein, aur top par chest ko squeeze karein."
                }
                "back" -> when (index) {
                    0 -> "Machine ki taraf face karke khud ko securely position karein."
                    1 -> "Specified width ya attachment use karke handle ko grip karein."
                    2 -> "Apne scapula ko depress karein aur hips se halka peeche lean karein."
                    3 -> "Apne elbows ko down aur back spine ki taraf pull karein, lower chest ko target karte hue."
                    else -> "Rep complete karne ke liye arms ko slowly waapas starting position par extend karein."
                }
                "shoulders" -> when (index) {
                    0 -> "Apne shoulders ko gravity/system ke perfectly parallel align karein."
                    1 -> "Dumbbells ko apne kaano ke paas rakhein ya machine handles ko capture karein."
                    2 -> "Core ko tight brace karein, feet ko ground par firmly lock rakhein."
                    3 -> "Weight ko shoulder joints ke theek upar vertically upar drive karein."
                    else -> "Movement ko complete karne ke liye weights ko slowly waapas neeche layen."
                }
                "biceps" -> when (index) {
                    0 -> "Apne target weights set karein aur floor par flat khade ho jayein."
                    1 -> "Apne elbows ko ribs ke paas tight rakhein aur wrists ko upar rotate karein."
                    2 -> "Control weight ko chest tak lane ke liye biceps ko dynamically contract karein."
                    3 -> "Elbows ko stationary rakhein, unhe aage swing na hone dein."
                    else -> "Weight ko precise negative tension ke andar slowly waapas neeche layen."
                }
                "triceps" -> when (index) {
                    0 -> "Chest ko halka tilt karke high pulley ki taraf face karke position karein."
                    1 -> "High chest level par rope ya bar attachment ko hold karein."
                    2 -> "Apne elbows ko rib cage ke paas tight squeeze karein."
                    3 -> "Triceps flex karke hand ko down push karein jab tak arms completely lock na ho jayein."
                    else -> "Attachment ko slowly aur complete control ke saath upar slide hone dein."
                }
                "legs" -> when (index) {
                    0 -> "Machine ya setup ke andar sahi tareeqe se position ho jayein."
                    1 -> "Handles ko tight grip karein ya barbell ko back par securely load karein."
                    2 -> "Feet ko shoulder-width apart rakhein aur toes ko halka bahar point karein."
                    3 -> "Hips ko bend karte hue niche squat karein jab tak glutes deep position tak na jayein."
                    else -> "Knees aur hips ko extend karte hue upar explosive push karein."
                }
                "abs" -> when (index) {
                    0 -> "Floor ya machine par apne focus position ko set karein."
                    1 -> "Core ko contract karein, sternum ko hips ki taraf late hue."
                    2 -> "Abs contraction ke waqt saari saans bahar chhodein."
                    3 -> "2 full seconds ke liye core ko tight squeeze karein."
                    else -> "Rectus range ko full stretch karne ke liye tension ke under slowly release karein."
                }
                else -> instruction
            }
        }
    }

    // Standard tips mapper
    fun translateTip(tip: String, category: String, lang: AppLanguage = currentLanguage): String {
        if (lang == AppLanguage.ENGLISH) return tip
        val cat = category.lowercase().trim()
        val normalized = tip.lowercase()
        return if (lang == AppLanguage.HINDI) {
            when {
                normalized.contains("elbows at a 45-degree") -> "अपनी कोहनी को ४५ डिग्री के कोण पर रखें।"
                normalized.contains("drive through your feet") -> "स्थिरता के लिए अपने पैरों से फर्श पर बल लगाएं।"
                normalized.contains("elbows, not with your hands") -> "अपनी कोहनियों से खींचें, हाथों से नहीं।"
                normalized.contains("shoulder blades together") -> "कंधे की हड्डियों (शोल्डर ब्लेड्स) को आपस में सिकोड़ें।"
                normalized.contains("shrug during raise") -> "लेटरल रेज़ के दौरान कंधों को न उचकाएं।"
                normalized.contains("lateral raises slightly in front") -> "लेटरल रेज़ को हमेशा अपनी छाती के थोड़ा आगे की तरफ चलाएं।"
                normalized.contains("biceps hard at the peak") -> "बाइसेप्स को ऊपर लाकर जोर से सिकोड़ें।"
                normalized.contains("lowering phase completely") -> "नीचे लाने की प्रक्रिया को पूरी तरह नियंत्रित रखें।"
                normalized.contains("spread the rope") -> "रेप के अंत में रोप/रस्सी को बाहर की तरफ फैलाएं।"
                normalized.contains("shoulders roll forward") -> "कंधों को आगे की ओर न झुकने दें।"
                normalized.contains("weight driven through your heels") -> "अपने पंजों के बजाय एड़ियों से दबाव बनाएं।"
                normalized.contains("knees in line with your toes") -> "घुटने बिल्कुल आपके पंजों की कतार में होने चाहिए।"
                normalized.contains("spinal curling") -> "रीढ़ की हड्डी के घुमाव पर ध्यान दें।"
                normalized.contains("exhale fully") -> "प्रत्येक संकुचन गति पर पूरी तरह सांस छोड़ें।"
                else -> "व्यायाम को पूर्ण नियंत्रण और संयम के साथ पूरा करें।"
            }
        } else { // Hinglish
            when {
                normalized.contains("elbows at a 45-degree") -> "Elbows ko body se 45-degree angle par rakhein."
                normalized.contains("drive through your feet") -> "Feet ko floor par majbooti se daba kar power generate karein."
                normalized.contains("elbows, not with your hands") -> "Elbows ke support se pull karein, haathon ke force se nahi."
                normalized.contains("shoulder blades together") -> "Movement ke peak par shoulder blades ko aapas mein squeeze karein."
                normalized.contains("shrug during raise") -> "Raises compression ke dauran neck shoulders ko shrug na karein."
                normalized.contains("lateral raises slightly in front") -> "Lateral raises ko chest line se halka sa aage rakh kar perform karein."
                normalized.contains("biceps hard at the peak") -> "Biceps ko movement ke top peak point par push squeeze karein."
                normalized.contains("lowering phase completely") -> "Weight ko slowly control ke sath neeche lakar negative phase lock karein."
                normalized.contains("spread the rope") -> "Rope pull ke end point par dono ends ko bahar swing failayein."
                normalized.contains("shoulders roll forward") -> "Heavy extensions mein shoulders ko unnecessary roll aage na hone dein."
                normalized.contains("weight driven through your heels") -> "Full range push load hamesha body ke heels se generate karein."
                normalized.contains("knees in line with your toes") -> "Heavy squats mein knees ko hamesha toes ki align position mein rakhein."
                normalized.contains("spinal curling") -> "Crunches contraction ke waqt abdominal compression ko focus karein."
                normalized.contains("exhale fully") -> "Maximally flex point par clear exhale air out karein."
                else -> "Workout ko proper control aur complete range ke sath perform karein."
            }
        }
    }

    // Standard mistakes mapper
    fun translateMistake(mistake: String, category: String, lang: AppLanguage = currentLanguage): String {
        if (lang == AppLanguage.ENGLISH) return mistake
        val cat = category.lowercase().trim()
        val normalized = mistake.lowercase()
        return if (lang == AppLanguage.HINDI) {
            when {
                normalized.contains("flaring") -> "कोहनियों को ७५ डिग्री से ज्यादा बाहर खोलना।"
                normalized.contains("bouncing") -> "वजन या बारबेल को छाती से उछालना।"
                normalized.contains("excessive momentum") || normalized.contains("swinging") -> "बहुत ज्यादा शरीर को झुलाना या झटके से वजन खींचना।"
                normalized.contains("rounding") -> "तनाव के दौरान या भारी वजन उठाते समय रीढ़ को मोड़ना।"
                normalized.contains("arch") -> "ओवरहेड प्रेस करते समय कमर को बहुत अधिक पीछे मोड़ना।"
                normalized.contains("drift") -> "बाइसेप्स कर्ल करते समय कोहनियों को शरीर से बहुत आगे ले जाना।"
                normalized.contains("sides") -> "प्रेसडाउन करते समय कोहनियों को बाहर फैलाना।"
                normalized.contains("valgus") || normalized.contains("knees inward") -> "स्क्वाट्स के समय घुटनों को अंदर धकेलना (असुरक्षित स्थिति)।"
                normalized.contains("heels off") -> "बैठक (स्क्वाट) लगाते समय एड़ियों को जमीन से उठाना।"
                normalized.contains("hip flexors") -> "पेट की कसरत के समय एब्स के बजाय कमर की मांसपेशियों का उपयोग करना।"
                normalized.contains("pulling neck") -> "क्रंचेस करते समय गर्दन को हाथों से आगे की ओर खींचना।"
                else -> "गलत पोस्चर में कसरत करना (असुरक्षित)।"
            }
        } else { // Hinglish
            when {
                normalized.contains("flaring") -> "Elbows ko collar alignment se zyada flare-out karna (Sholder injury danger)."
                normalized.contains("bouncing") -> "Barbell ya dumbbell plates ko chest se bounce lock karna."
                normalized.contains("excessive momentum") || normalized.contains("swinging") -> "Heavy weights lift karne ke liye torso swing ya jhatka use karna."
                normalized.contains("rounding") -> "Tension time par spinal structure ya lower back ko round shape karna."
                normalized.contains("arch") -> "Heavy shoulder presses mein core weak hone par back ko boht arch karna."
                normalized.contains("drift") -> "Bicep curl ranges mein elbows ko rib frame se aage shift hone dena."
                normalized.contains("sides") -> "Triceps down cuts mein elbows ko bahar ki side flaring out dena."
                normalized.contains("valgus") || normalized.contains("knees inward") -> "Deep squats mein pressure drop hoke ghutno ka andar collapse hona."
                normalized.contains("heels off") -> "Body squat weight shift hone par ankles/heels ko ground se pull karna."
                normalized.contains("hip flexors") -> "Abdominal crunches ko control abs ki jagha hip flexors se pull karna."
                normalized.contains("pulling neck") -> "Crunches load points par head/neck ko force se aage bends karna."
                else -> "Incomplete range of motion control aur wrong frame lock use karna."
            }
        }
    }

    // Dynamic pre-defined warmups and stretching guides translation
    fun translateGuide(title: String, desc: String, lang: AppLanguage = currentLanguage): Pair<String, String> {
        if (lang == AppLanguage.ENGLISH) return Pair(title, desc)
        val normalizedTitle = title.lowercase().trim()
        
        return if (lang == AppLanguage.HINDI) {
            when {
                normalizedTitle.contains("chest day") -> Pair(
                    "चेस्ट डे वार्म-अप तैयारी",
                    "५ मिनट हाथ घुमाना, हल्का चेस्ट प्रेस (१५ रेप्स), इलास्टिक बैंड चेस्ट ओपनर्स (१५ रेप्स)।"
                )
                normalizedTitle.contains("back") -> Pair(
                    "पीठ और टी-बार वार्म-अप",
                    "स्कैपुला रिट्रैक्शन पुल्स (१५ रेप्स), बर्ड-डॉग स्टेबिलिटी होल्ड्स (३० सेकंड), इलास्टिक रोइंग।"
                )
                normalizedTitle.contains("lower body") -> Pair(
                    "निचले शरीर की तैयारी (लेग डे)",
                    "फ्री-बॉडीवेट स्क्वाट्स (१५ रेप्स), लेग स्विंग्स (१२ प्रति पैर), जांघ खिंचाव स्ट्रेच।"
                )
                normalizedTitle.contains("shoulder") -> Pair(
                    "कंधे की सक्रिय सक्रियता",
                    "बाई-बाई रोटेशन्स (१५ रेप्स), डम्बल रोटेटर कफ घुमाव (१५ रेप्स), सक्रिय आर्म स्लाइड्स।"
                )
                normalizedTitle.contains("arm pump") -> Pair(
                    "हाथ की पंप कसरत तैयारी",
                    "हल्के ट्राइसैप पुशडाउन (१५ रेप्स), बाइसेप आर्म कर्ल गति, कलाई रोटेशन (३० सेकंड)।"
                )
                normalizedTitle.contains("pectorals") -> Pair(
                    "चेस्ट और पेक्टोरल स्ट्रेच",
                    "दरवाजे के फ्रेम का उपयोग करके छाती को आगे बढ़ाएं। ३० सेकंड तक खिंचाव बनाए रखें।"
                )
                normalizedTitle.contains("lats") -> Pair(
                    "लैट्स और लोअर बैक खिंचाव",
                    "दोनों हाथों को ऊपर उठाएं, शरीर को पूरी तरह सीधा करें और फिर एक तरफ झुकें। दोहराएं।"
                )
                normalizedTitle.contains("hamstrings") -> Pair(
                    "हैमस्ट्रिंग और हिप फ्लेक्सर्स",
                    "लंज मुद्रा में घुटने टिकाएं, कूल्हों को आगे बढ़ाएं। प्रत्येक तरफ ३० सेकंड तक खिंचाव महसूस करें।"
                )
                normalizedTitle.contains("deltoids") -> Pair(
                    "अग्र कंधा और डेल्टोइड्स रिलीज",
                    "एक हाथ को छाती के ऊपर से ले जाते हुए दूसरे कोहनी से कसकर लॉक करें। ३० सेकंड रुकें।"
                )
                normalizedTitle.contains("biceps / triceps") -> Pair(
                    "बाइसेप्स और ओवरहेड ट्राइसैप्स स्ट्रेच",
                    "हाथ को सिर के पीछे ले जाएं, कोहनी को नीचे खींचें। ३० सेकंड तक खिंचाव बनाए रखें।"
                )
                else -> Pair(title, desc)
            }
        } else { // Hinglish
            when {
                normalizedTitle.contains("chest day") -> Pair(
                    "Chest Day Preparation",
                    "Arm circles (15 reps), light pushups (10 reps), chest bands openers (15 reps)."
                )
                normalizedTitle.contains("back") -> Pair(
                    "Back & T-Bar Ignition",
                    "Scapular pulling control (15 reps), bird-dog stability holds (30 seconds), elastic bands."
                )
                normalizedTitle.contains("lower body") -> Pair(
                    "Lower Body Squat Prep",
                    "Bodyweight deep squats (15 reps), leg swings (12 per leg), hip dynamic lunge stretches."
                )
                normalizedTitle.contains("shoulder") -> Pair(
                    "Shoulder Dynamic Activation",
                    "Y rotations (15 reps), rotator cuff cuff rotations (15 reps), active wall slides."
                )
                normalizedTitle.contains("arm pump") -> Pair(
                    "Arm Pump Prep Session",
                    "Light triceps pushdowns (15 reps), bicep arm dynamic rotations, wrist mobility."
                )
                normalizedTitle.contains("pectorals") -> Pair(
                    "Pectorals & Chest Stretch",
                    "Doorway pose lelo, dono arms frames par tika kar forward lean karo. Hold for 30s."
                )
                normalizedTitle.contains("lats") -> Pair(
                    "Lats & Lower Back Stretch",
                    "Hands clasp over head locks karo, body completely up karke side curves pe lean karo."
                )
                normalizedTitle.contains("hamstrings") -> Pair(
                    "Hamstrings & Hip Flexors",
                    "Lunge position frame down, hips active forward press karo back thighs stretch ke liye."
                )
                normalizedTitle.contains("deltoids") -> Pair(
                    "Active Deltoids Release",
                    "Single arm chest ke across cross karein aur opposite elbow se press stretch lock karein."
                )
                normalizedTitle.contains("biceps / triceps") -> Pair(
                    "Biceps / Triceps Overheads",
                    "Single hand head back lock shoulder bones touch and elbow downward press. Hold for 30s."
                )
                else -> Pair(title, desc)
            }
        }
    }
}
