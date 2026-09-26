package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.db.AppDatabase
import com.example.data.repository.SalonRepository
import com.example.ui.admin.AdminAppScreen
import com.example.ui.components.AdminDiscreetCornerButton
import com.example.ui.components.AdminPasswordDialog
import com.example.ui.components.NotificationSheet
import com.example.ui.customer.CustomerAppScreen
import com.example.ui.theme.DarkCanvasBackground
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.AppMode
import com.example.viewmodel.SalonViewModel
import com.example.viewmodel.SalonViewModelFactory
import com.example.data.backend.FirebaseBackendService
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getInstance(applicationContext)
        val firebaseBackend = FirebaseBackendService(applicationContext)
        val repository = SalonRepository(database.salonDao(), firebaseBackend, applicationContext)
        val viewModelFactory = SalonViewModelFactory(repository)

        setContent {
            MyApplicationTheme {
                val salonViewModel: SalonViewModel = viewModel(factory = viewModelFactory)
                SalonSyncApp(viewModel = salonViewModel)
            }
        }
    }
}

@Composable
fun SalonSyncApp(viewModel: SalonViewModel) {
    val activeAppMode by viewModel.appMode.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val unreadNotifs by viewModel.unreadNotificationCount.collectAsState()
    val notifications by viewModel.notifications.collectAsState()
    val uiMessage by viewModel.uiMessage.collectAsState()
    val isAdminAuthenticated by viewModel.isAdminAuthenticated.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    var isNotificationSheetOpen by remember { mutableStateOf(false) }
    var isPasswordDialogOpen by remember { mutableStateOf(false) }

    LaunchedEffect(uiMessage) {
        uiMessage?.let { msg ->
            snackbarHostState.showSnackbar(
                message = msg,
                duration = SnackbarDuration.Short
            )
            viewModel.dismissUiMessage()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag("salon_sync_root_scaffold"),
        containerColor = DarkCanvasBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            if (activeAppMode == AppMode.ADMIN) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 3.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF10B981))
                            )
                            Text(
                                text = "Admin Console • Live DB",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            IconButton(
                                onClick = { isNotificationSheetOpen = true },
                                modifier = Modifier.size(36.dp)
                            ) {
                                BadgedBox(badge = {
                                    if (unreadNotifs > 0) {
                                        Badge(containerColor = MaterialTheme.colorScheme.error) {
                                            Text(text = if (unreadNotifs > 9) "9+" else "$unreadNotifs")
                                        }
                                    }
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Notifications,
                                        contentDescription = "Notifications",
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }

                            OutlinedButton(
                                onClick = { viewModel.lockAdmin() },
                                modifier = Modifier.testTag("exit_admin_button"),
                                shape = RoundedCornerShape(12.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Exit to Customer", style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Crossfade(
                targetState = activeAppMode,
                label = "AppTransition"
            ) { mode ->
                when (mode) {
                    AppMode.CUSTOMER -> {
                        CustomerAppScreen(
                            viewModel = viewModel,
                            onOpenAdmin = {
                                if (isAdminAuthenticated || viewModel.isAdminDeviceRemembered()) {
                                    viewModel.setAppMode(AppMode.ADMIN)
                                } else {
                                    isPasswordDialogOpen = true
                                }
                            },
                            onOpenNotifications = { isNotificationSheetOpen = true },
                            unreadNotifications = unreadNotifs,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    AppMode.ADMIN -> {
                        AdminAppScreen(
                            viewModel = viewModel,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            // Admin Authentication Password Dialog
            if (isPasswordDialogOpen) {
                AdminPasswordDialog(
                    onDismiss = { isPasswordDialogOpen = false },
                    onSubmitPassword = { inputPassword ->
                        val success = viewModel.authenticateAdmin(inputPassword)
                        if (success) {
                            isPasswordDialogOpen = false
                        }
                        success
                    }
                )
            }

            // Real-Time Notification Sheet
            if (isNotificationSheetOpen) {
                NotificationSheet(
                    notifications = notifications,
                    onDismiss = { isNotificationSheetOpen = false },
                    onMarkAllRead = { viewModel.markAllNotificationsRead() },
                    onNotificationClick = { notif ->
                        viewModel.markNotificationRead(notif.id)
                    }
                )
            }
        }
    }
}
