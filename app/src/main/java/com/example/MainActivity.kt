package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.audio.PremiumTTSManager
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.screens.MainAppContainer
import com.example.ui.viewmodel.FitnessViewModel

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    // Pre-initialize TTS engine for seamless, zero-lag premium speech announcements
    PremiumTTSManager.initialize(applicationContext)
    setContent {
      MyApplicationTheme {
        val vm: FitnessViewModel = viewModel()
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
          MainAppContainer(
            viewModel = vm,
            modifier = Modifier.padding(innerPadding)
          )
        }
      }
    }
  }

  override fun onDestroy() {
    PremiumTTSManager.shutdown()
    super.onDestroy()
  }
}
