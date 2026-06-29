package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LegalScreen(
    onDismiss: () -> Unit,
    initialSection: LegalItemType? = null,
    modifier: Modifier = Modifier
) {
    var activeSection by remember { mutableStateOf<LegalItemType?>(initialSection) }

    AnimatedContent(
        targetState = activeSection,
        transitionSpec = {
            if (targetState != null) {
                // Navigating deeper: slide in from right, fade in
                (slideInHorizontally { width -> width } + fadeIn()).togetherWith(
                    slideOutHorizontally { width -> -width } + fadeOut()
                )
            } else {
                // Navigating back: slide out to right, fade out
                (slideInHorizontally { width -> -width } + fadeIn()).togetherWith(
                    slideOutHorizontally { width -> width } + fadeOut()
                )
            }
        },
        label = "LegalScreenNavigation"
    ) { section ->
        when (section) {
            null -> {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    "Legal & Licensing",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            },
                            navigationIcon = {
                                IconButton(
                                    onClick = onDismiss,
                                    modifier = Modifier.testTag("legal_screen_close")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBack,
                                        contentDescription = "Close Legal Hub",
                                        tint = Color.White
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = GymDarkGray
                            )
                        )
                    },
                    containerColor = GymBlack,
                    modifier = modifier.testTag("legal_screen_menu_container")
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        LegalMenu(
                            onItemSelect = { selectedType ->
                                activeSection = selectedType
                            }
                        )
                    }
                }
            }
            LegalItemType.EULA -> {
                EulaScreen(onBack = { activeSection = null })
            }
            LegalItemType.PRIVACY_POLICY -> {
                PrivacyPolicyScreen(onBack = { activeSection = null })
            }
            LegalItemType.TERMS_OF_SERVICE -> {
                TermsScreen(onBack = { activeSection = null })
            }
            LegalItemType.DISCLAIMER -> {
                DisclaimerScreen(onBack = { activeSection = null })
            }
            LegalItemType.COPYRIGHT -> {
                CopyrightScreen(onBack = { activeSection = null })
            }
            LegalItemType.ABOUT -> {
                AboutScreen(onBack = { activeSection = null })
            }
            LegalItemType.CONTACT -> {
                ContactScreen(onBack = { activeSection = null })
            }
        }
    }
}
