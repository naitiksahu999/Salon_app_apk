package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ui.theme.SalonGoldPrimary
import com.example.util.AppFeedbackHelper

object PhotoFitUtils {
    fun getAlignmentForFit(fitKey: String): Alignment {
        return when (fitKey.uppercase()) {
            "CROP_TOP", "TOP" -> Alignment.TopCenter
            "CROP_BOTTOM", "BOTTOM" -> Alignment.BottomCenter
            "CROP_LEFT", "LEFT" -> Alignment.CenterStart
            "CROP_RIGHT", "RIGHT" -> Alignment.CenterEnd
            else -> Alignment.Center
        }
    }

    fun getContentScaleForFit(fitKey: String): ContentScale {
        return when (fitKey.uppercase()) {
            "FIT" -> ContentScale.Fit
            "FILL_WIDTH" -> ContentScale.FillWidth
            "FILL_HEIGHT" -> ContentScale.FillHeight
            else -> ContentScale.Crop
        }
    }
}

data class CropOption(
    val key: String,
    val label: String,
    val description: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoPreviewAndCropDialog(
    photoUrl: String,
    photoTitle: String,
    currentFit: String,
    onDismiss: () -> Unit,
    onSaveFit: (newFit: String) -> Unit
) {
    val context = LocalContext.current
    var selectedFit by remember { mutableStateOf(if (currentFit.isNotBlank()) currentFit else "CROP_CENTER") }

    val cropOptions = listOf(
        CropOption("CROP_CENTER", "Center (Standard)", "Balanced center crop", Icons.Default.FilterCenterFocus),
        CropOption("CROP_TOP", "Top Portion", "Keep faces and signboards visible", Icons.Default.VerticalAlignTop),
        CropOption("CROP_BOTTOM", "Bottom Portion", "Focus on lower details and flooring", Icons.Default.VerticalAlignBottom),
        CropOption("FIT", "Fit Entire Photo", "Show whole image without any crop", Icons.Default.FitScreen),
        CropOption("FILL_WIDTH", "Fill Width", "Scale to full container width", Icons.Default.AspectRatio)
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.92f)
                .padding(8.dp),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
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
                                .background(SalonGoldPrimary.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Crop,
                                contentDescription = null,
                                tint = SalonGoldPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "Photo Preview & Visible Crop",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = photoTitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                // Live Customer View Preview Box
                Text(
                    text = "Customer View Live Preview:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = SalonGoldPrimary
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(190.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF0F172A))
                        .border(1.5.dp, SalonGoldPrimary.copy(alpha = 0.6f), RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(photoUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Preview",
                        contentScale = PhotoFitUtils.getContentScaleForFit(selectedFit),
                        alignment = PhotoFitUtils.getAlignmentForFit(selectedFit),
                        modifier = Modifier.fillMaxSize()
                    )

                    // Customer Mockup Overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.65f)
                                    ),
                                    startY = 100f
                                )
                            )
                    )

                    Surface(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(12.dp),
                        shape = RoundedCornerShape(8.dp),
                        color = Color.Black.copy(alpha = 0.65f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Visibility,
                                contentDescription = null,
                                tint = SalonGoldPrimary,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "Exact customer screen view",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                Text(
                    text = "Select visible area if photo aspect ratio differs:",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Alignment and Scale Selector Options
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    cropOptions.forEach { opt ->
                        val isSelected = selectedFit == opt.key
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected)
                                MaterialTheme.colorScheme.primaryContainer
                            else
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                            border = if (isSelected)
                                androidx.compose.foundation.BorderStroke(1.5.dp, SalonGoldPrimary)
                            else null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    AppFeedbackHelper.triggerSelection(context)
                                    selectedFit = opt.key
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Icon(
                                    imageVector = opt.icon,
                                    contentDescription = null,
                                    tint = if (isSelected) SalonGoldPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(22.dp)
                                )
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = opt.label,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                    )
                                    Text(
                                        text = opt.description,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                RadioButton(
                                    selected = isSelected,
                                    onClick = {
                                        AppFeedbackHelper.triggerSelection(context)
                                        selectedFit = opt.key
                                    },
                                    colors = RadioButtonDefaults.colors(selectedColor = SalonGoldPrimary)
                                )
                            }
                        }
                    }
                }

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            AppFeedbackHelper.triggerSuccess(context)
                            onSaveFit(selectedFit)
                        },
                        modifier = Modifier.weight(1.5f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SalonGoldPrimary,
                            contentColor = Color.Black
                        )
                    ) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Apply & Save Alignment", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
