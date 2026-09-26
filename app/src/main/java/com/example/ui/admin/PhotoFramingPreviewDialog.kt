package com.example.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Crop
import androidx.compose.material.icons.filled.FitScreen
import androidx.compose.material.icons.filled.Preview
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VerticalAlignBottom
import androidx.compose.material.icons.filled.VerticalAlignTop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ui.theme.DarkBorderColor
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.DarkSurfaceCardElevated
import com.example.ui.theme.PlayfairDisplayFontFamily
import com.example.ui.theme.PlusJakartaSansFontFamily
import com.example.ui.theme.SalonGoldPill
import com.example.ui.theme.SalonGoldPrimary
import com.example.util.AppFeedbackHelper

/**
 * Interactive Dialog to inspect exactly how uploaded photos look to customers,
 * and select which focal area is visible if the photo aspect ratio doesn't match the card size.
 */
@Composable
fun PhotoFramingPreviewDialog(
    photoTitle: String,
    imageUrl: String,
    initialFit: String = "CROP_CENTER",
    onDismissRequest: () -> Unit,
    onSaveFraming: (String) -> Unit
) {
    val context = LocalContext.current
    var selectedFit by remember { mutableStateOf(initialFit.ifBlank { "CROP_CENTER" }) }

    // Map fit key to ContentScale and Alignment
    val (contentScale, alignment) = when (selectedFit) {
        "CROP_TOP" -> Pair(ContentScale.Crop, Alignment.TopCenter)
        "CROP_BOTTOM" -> Pair(ContentScale.Crop, Alignment.BottomCenter)
        "FIT_CENTER" -> Pair(ContentScale.Fit, Alignment.Center)
        else -> Pair(ContentScale.Crop, Alignment.Center)
    }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xF0000000))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 660.dp)
                    .testTag("photo_framing_preview_dialog"),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Preview,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "Customer View Preview",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "$photoTitle • Real Customer Rendering",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        IconButton(onClick = onDismissRequest) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                        }
                    }

                    // Live Customer Preview Container (Simulating customer display)
                    Text(
                        text = "LIVE CUSTOMER DISPLAY PREVIEW:",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = SalonGoldPrimary,
                        letterSpacing = 1.sp
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(210.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF121212))
                            .border(1.dp, SalonGoldPrimary.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                    ) {
                        if (imageUrl.isNotBlank()) {
                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data(imageUrl)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "Preview",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = contentScale,
                                alignment = alignment
                            )
                        } else {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("No photo URL provided", color = Color.Gray, fontSize = 12.sp)
                            }
                        }

                        // Gradient & Real UI Simulation Overlay
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            Color(0x88000000),
                                            Color(0xEE121212)
                                        )
                                    )
                                )
                        )

                        // Sample Customer Elements to show realistic context
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(14.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0x99000000)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(Icons.Default.Star, contentDescription = null, tint = SalonGoldPrimary, modifier = Modifier.size(12.dp))
                                    Text("4.9 Verified Salon", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Premium Care for Everyone",
                                fontFamily = PlayfairDisplayFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color.White
                            )
                            Text(
                                text = "Hair | Skin | Grooming | Luxury Sanctuary",
                                fontFamily = PlusJakartaSansFontFamily,
                                fontSize = 10.sp,
                                color = Color(0xFFD1D5DB)
                            )
                        }

                        // Framing indicator badge
                        Surface(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp),
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xCC000000)
                        ) {
                            Text(
                                text = when (selectedFit) {
                                    "CROP_TOP" -> "Top / Face Focus"
                                    "CROP_BOTTOM" -> "Bottom Focus"
                                    "FIT_CENTER" -> "Full Photo (Letterboxed)"
                                    else -> "Center Focus (Default)"
                                },
                                fontSize = 10.sp,
                                color = SalonGoldPrimary,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    // Framing / Visible Portion Selector
                    Text(
                        text = "SELECT VISIBLE PORTION IF SIZE DOESN'T MATCH:",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        letterSpacing = 0.5.sp
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        val framingOptions = listOf(
                            Triple("CROP_CENTER", "Center (Balanced)", "Focuses on the exact center of the photo. Best for interior & landscape views."),
                            Triple("CROP_TOP", "Top / Face Focus", "Focuses on the upper portion. Best for portrait models, haircuts, and face styling."),
                            Triple("CROP_BOTTOM", "Bottom Focus", "Focuses on the lower portion. Best for products, footwear, and salon stations."),
                            Triple("FIT_CENTER", "Fit Entire Photo", "Shows 100% of the photo without any cropping. Adds subtle luxury letterbox bars.")
                        )

                        framingOptions.forEach { (key, label, desc) ->
                            val isSelected = selectedFit == key
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                                border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, SalonGoldPrimary) else null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        AppFeedbackHelper.triggerSelection(context)
                                        selectedFit = key
                                    }
                                    .testTag("framing_option_$key")
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Icon(
                                        imageVector = when (key) {
                                            "CROP_TOP" -> Icons.Default.VerticalAlignTop
                                            "CROP_BOTTOM" -> Icons.Default.VerticalAlignBottom
                                            "FIT_CENTER" -> Icons.Default.FitScreen
                                            else -> Icons.Default.CenterFocusStrong
                                        },
                                        contentDescription = null,
                                        tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(20.dp)
                                    )

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = label,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = desc,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            fontSize = 11.sp
                                        )
                                    }

                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Selected",
                                            tint = SalonGoldPrimary,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Action buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = onDismissRequest,
                            colors = ButtonDefaults.outlinedButtonColors(),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancel")
                        }

                        Button(
                            onClick = {
                                AppFeedbackHelper.triggerSuccess(context)
                                onSaveFraming(selectedFit)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = SalonGoldPrimary, contentColor = Color(0xFF141414)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1.5f)
                                .testTag("save_photo_framing_button")
                        ) {
                            Icon(Icons.Default.Crop, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Apply & Save Framing", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

/**
 * Convenient floating corner button placed over photo thumbnails
 * to trigger the preview and framing dialog.
 */
@Composable
fun PhotoCornerEditButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Edit & Preview"
) {
    Surface(
        onClick = onClick,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .testTag("photo_corner_edit_button"),
        shape = RoundedCornerShape(8.dp),
        color = Color(0xCC000000),
        border = androidx.compose.foundation.BorderStroke(1.dp, SalonGoldPrimary.copy(alpha = 0.8f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Crop,
                contentDescription = "Edit photo framing",
                tint = SalonGoldPrimary,
                modifier = Modifier.size(13.dp)
            )
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}
