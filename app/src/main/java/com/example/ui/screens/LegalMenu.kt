package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

enum class LegalItemType {
    EULA,
    PRIVACY_POLICY,
    TERMS_OF_SERVICE,
    DISCLAIMER,
    COPYRIGHT,
    ABOUT,
    CONTACT
}

data class LegalMenuItem(
    val type: LegalItemType,
    val title: String,
    val subtitle: String,
    val icon: ImageVector
)

@Composable
fun LegalMenu(
    onItemSelect: (LegalItemType) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        LegalMenuItem(
            LegalItemType.EULA,
            "End User License Agreement (EULA)",
            "Terms of license, personal and commercial use boundaries",
            Icons.Default.List
        ),
        LegalMenuItem(
            LegalItemType.PRIVACY_POLICY,
            "Privacy Policy",
            "Data safety, device logging, local encryption status",
            Icons.Default.Lock
        ),
        LegalMenuItem(
            LegalItemType.TERMS_OF_SERVICE,
            "Terms of Service",
            "General terms governing your app usage experience",
            Icons.Default.Check
        ),
        LegalMenuItem(
            LegalItemType.DISCLAIMER,
            "Exercise & Health Disclaimer",
            "Kinesiology safety guidelines and liability limitations",
            Icons.Default.Warning
        ),
        LegalMenuItem(
            LegalItemType.COPYRIGHT,
            "Copyright & Patent Notice",
            "Proprietary visual designs and software copyrights",
            Icons.Default.CheckCircle
        ),
        LegalMenuItem(
            LegalItemType.ABOUT,
            "About ITSYOU",
            "The story, concept, and architecture of ITSYOU Fitness",
            Icons.Default.Info
        ),
        LegalMenuItem(
            LegalItemType.CONTACT,
            "Contact & Support Desk",
            "Official support email support@itsyoufitness.in and details",
            Icons.Default.Email
        )
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(GymBlack)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(items) { item ->
            Card(
                colors = CardDefaults.cardColors(containerColor = GymCardGray),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onItemSelect(item.type) }
                    .testTag("legal_menu_item_${item.type.name}")
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(GymSurfaceGray, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            tint = GymBloodRed,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1.0f),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = item.title,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Text(
                            text = item.subtitle,
                            color = GymTextGray,
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        )
                    }
                }
            }
        }
    }
}
