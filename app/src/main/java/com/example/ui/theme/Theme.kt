package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkGymColorScheme = darkColorScheme(
  primary = GymBloodRed,
  secondary = GymDarkRed,
  tertiary = GymOrangeAccent,
  background = GymBlack,
  surface = GymDarkGray,
  onPrimary = Color.White,
  onSecondary = Color.White,
  onTertiary = Color.White,
  onBackground = GymTextLight,
  onSurface = GymTextLight,
  surfaceVariant = GymCardGray,
  onSurfaceVariant = GymTextGray,
  outline = GymSurfaceGray
)

// In Premium Gym design, we enforce a dark aesthetic (or customizable dark theme) for ultimate contrast
@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true, // default to gym-dark
  dynamicColor: Boolean = false, // false to prevent color distortions
  content: @Composable () -> Unit,
) {
  // Always use dark gym theme to respect user's black/blood-red layout intent
  MaterialTheme(
    colorScheme = DarkGymColorScheme,
    typography = Typography,
    content = content
  )
}
