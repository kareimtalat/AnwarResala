package com.kareimt.anwarresala.presentation.screens.volunteer

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kareimt.anwarresala.R
import com.kareimt.anwarresala.data.local.volunteer.VolunteerEntity
import com.kareimt.anwarresala.presentation.components.ActionConfirmationDialog
import com.kareimt.anwarresala.presentation.components.EmptyState
import com.kareimt.anwarresala.presentation.components.ErrorMessageCard
import com.kareimt.anwarresala.presentation.components.RequestTypeSelector
import com.kareimt.anwarresala.presentation.components.VolunteerRequestsList
import com.kareimt.anwarresala.presentation.viewmodels.ApprovalViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApprovalScreen(
    viewModel: ApprovalViewModel,
    navController: NavController
) {
    var pendingVolunteers by viewModel.pendingVolunteers
    var approvedVolunteers by viewModel.approvedVolunteers
    var isLoading by viewModel.isLoading
    var errorMessage by viewModel.errorMessage

    var showPending by remember { mutableStateOf(true) }
    var selectedVolunteerForAction by remember { mutableStateOf<VolunteerEntity?>(null) }
    var showActionDialog by remember { mutableStateOf(false) }
    var actionType by remember { mutableStateOf("") } // "approve", "reject", or "delete"

    val rotation = remember { Animatable(0f) }
    var shouldAnimate by remember { mutableStateOf(false) }
    LaunchedEffect(shouldAnimate) {
        if (shouldAnimate) {
            rotation.snapTo(0f)
            rotation.animateTo(
                targetValue = 360f * 2,
                animationSpec = tween(durationMillis = 1200, easing = LinearEasing)
            )
            rotation.snapTo(0f)
            shouldAnimate = false // Reset trigger
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigateUp()
                              },
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                }
                                 },
                title = {
                    Text(
                        text = stringResource(R.string.approval_requests),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    IconButton(onClick = {
                        viewModel.refreshVolunteers()
                        shouldAnimate = true
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_refresh),
                            contentDescription = "Refresh",
                            modifier = Modifier.rotate(rotation.value),
                            tint = Color.Unspecified
                        )
                    }
                },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Show error message if any
            if (!errorMessage.isNullOrEmpty()) {
                ErrorMessageCard(errorMessage!!)
            }

            // Toggle between pending and approved
            RequestTypeSelector(
                showPending = showPending,
                onToggle = { showPending = it },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                // Display the appropriate list
                if (showPending) {
                    if (pendingVolunteers.isEmpty()) {
                        EmptyState(
                            icon = Icons.Default.Pending,
                            title = stringResource(R.string.no_pending_requests),
                            subtitle = stringResource(R.string.no_pending_requests_desc),
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        VolunteerRequestsList(
                            volunteers = pendingVolunteers,
                            isPending = true,
                            onApprove = { volunteer ->
                                selectedVolunteerForAction = volunteer
                                actionType = "approve"
                                showActionDialog = true
                            },
                            onReject = { volunteer ->
                                selectedVolunteerForAction = volunteer
                                actionType = "reject"
                                showActionDialog = true
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                } else {
                    if (approvedVolunteers.isEmpty()) {
                        EmptyState(
                            icon = Icons.Default.CheckCircle,
                            title = stringResource(R.string.no_approved_requests),
                            subtitle = stringResource(R.string.no_approved_requests_desc),
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        VolunteerRequestsList(
                            volunteers = approvedVolunteers,
                            isPending = false,
                            onDelete = { volunteer ->
                                selectedVolunteerForAction = volunteer
                                actionType = "delete"
                                showActionDialog = true
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }

    // Action confirmation dialog
    if (showActionDialog && selectedVolunteerForAction != null) {
        val volunteer = selectedVolunteerForAction!!
        val title = when (actionType) {
            "approve" -> stringResource(R.string.approve_request)
            "reject" -> stringResource(R.string.reject_request)
            else -> stringResource(R.string.delete_volunteer)
        }

        val message = when (actionType) {
            "approve" -> stringResource(R.string.confirm_approve, volunteer.name)
            "reject" -> stringResource(R.string.confirm_reject, volunteer.name)
            else -> stringResource(R.string.confirm_delete, volunteer.name)
        }

        ActionConfirmationDialog(
            title = title,
            message = message,
            confirmText = when (actionType) {
                "approve" -> stringResource(R.string.approve)
                "reject" -> stringResource(R.string.reject)
                else -> stringResource(R.string.delete)
            },
            dismissText = stringResource(R.string.cancel),
            onConfirm = {
                when (actionType) {
                    "approve" -> viewModel.approveVolunteer(volunteer.firebaseId!!)
                    "reject" -> viewModel.rejectVolunteer(volunteer.firebaseId!!)
                    "delete" -> viewModel.deleteVolunteer(volunteer.firebaseId!!)
                }
                showActionDialog = false
                selectedVolunteerForAction = null
            },
            onDismiss = {
                showActionDialog = false
                selectedVolunteerForAction = null
            }
        )
    }
}