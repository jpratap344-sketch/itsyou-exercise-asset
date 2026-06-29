package com.example.ui.audio

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import com.example.ui.translation.AppLanguage
import com.example.ui.translation.TranslationHelper
import java.util.Locale

object PremiumTTSManager : TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    private var isInitialized = false
    private var pendingSpeech: Pair<String, AppLanguage>? = null

    fun initialize(context: Context) {
        if (tts == null) {
            tts = TextToSpeech(context.applicationContext, this)
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isInitialized = true
            Log.d("PremiumTTSManager", "TTS Initialized successfully")
            // Apply pending speech if any
            pendingSpeech?.let { (text, lang) ->
                speakNow(text, lang)
                pendingSpeech = null
            }
        } else {
            Log.e("PremiumTTSManager", "Failed to initialize TTS")
        }
    }

    private fun setLanguageForTTS(lang: AppLanguage) {
        val speechEngine = tts ?: return
        try {
            when (lang) {
                AppLanguage.HINDI -> {
                    val result = speechEngine.setLanguage(Locale("hi", "IN"))
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("PremiumTTSManager", "Hindi language is not supported or missing data, falling back to English")
                        speechEngine.setLanguage(Locale.US)
                    }
                }
                AppLanguage.HINGLISH -> {
                    // For Hinglish, use Hindi engine or mixed, we can use Hindi locale so it pronounces Hinglish phrases with Indian dialect correctly
                    val result = speechEngine.setLanguage(Locale("hi", "IN"))
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        speechEngine.setLanguage(Locale.US)
                    }
                }
                AppLanguage.ENGLISH -> {
                    speechEngine.setLanguage(Locale.US)
                }
            }
        } catch (e: Exception) {
            Log.e("PremiumTTSManager", "Error setting language on TTS", e)
        }
    }

    private fun speakNow(text: String, lang: AppLanguage) {
        val speechEngine = tts ?: return
        if (!isInitialized) return
        
        try {
            setLanguageForTTS(lang)
            speechEngine.speak(text, TextToSpeech.QUEUE_FLUSH, null, "PremiumTTSAnnouncement")
        } catch (e: Exception) {
            Log.e("PremiumTTSManager", "Error speaking text: $text", e)
        }
    }

    fun speak(text: String, lang: AppLanguage, context: Context? = null) {
        // Double check settings first
        val prefs = context?.getSharedPreferences("fitness_prefs", Context.MODE_PRIVATE)
        val announcementsEnabled = prefs?.getBoolean("voice_announcements_enabled", true) ?: true
        if (!announcementsEnabled) {
            Log.d("PremiumTTSManager", "Voice announcements are disabled in settings")
            return
        }

        if (tts == null && context != null) {
            initialize(context)
        }

        if (!isInitialized) {
            pendingSpeech = Pair(text, lang)
            return
        }

        speakNow(text, lang)
    }

    // High level helper functions matching requirements

    fun announceExerciseName(exerciseName: String, lang: AppLanguage, context: Context) {
        val translatedName = TranslationHelper.translateExerciseName(exerciseName, lang)
        val speakText = when (lang) {
            AppLanguage.ENGLISH -> "Starting $translatedName"
            AppLanguage.HINDI -> "$translatedName शुरू करें"
            AppLanguage.HINGLISH -> "$translatedName शुरू करते हैं"
        }
        speak(speakText, lang, context)
    }

    fun announceNextExercise(exerciseName: String, lang: AppLanguage, context: Context) {
        val translatedName = TranslationHelper.translateExerciseName(exerciseName, lang)
        val speakText = when (lang) {
            AppLanguage.ENGLISH -> "Next exercise is $translatedName"
            AppLanguage.HINDI -> "अगला व्यायाम है $translatedName"
            AppLanguage.HINGLISH -> "Agla exercise hai $translatedName"
        }
        speak(speakText, lang, context)
    }

    fun announceRestComplete(lang: AppLanguage, context: Context) {
        val speakText = when (lang) {
            AppLanguage.ENGLISH -> "Rest complete. Get ready!"
            AppLanguage.HINDI -> "विश्राम पूरा हुआ। तैयार हो जाइए!"
            AppLanguage.HINGLISH -> "Rest poora ho gaya hai. Taiyaar ho jao!"
        }
        speak(speakText, lang, context)
    }

    fun announceWorkoutComplete(lang: AppLanguage, context: Context) {
        val speakText = when (lang) {
            AppLanguage.ENGLISH -> "Workout complete. Great job!"
            AppLanguage.HINDI -> "वर्कआउट पूरा हुआ। बहुत बढ़िया!"
            AppLanguage.HINGLISH -> "Workout khatam ho gaya. Great job!"
        }
        speak(speakText, lang, context)
    }

    fun shutdown() {
        try {
            tts?.stop()
            tts?.shutdown()
            tts = null
            isInitialized = false
        } catch (e: Exception) {
            Log.e("PremiumTTSManager", "Error shutting down TTS", e)
        }
    }
}
