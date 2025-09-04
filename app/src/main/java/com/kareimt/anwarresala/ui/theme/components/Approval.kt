package com.kareimt.anwarresala.ui.theme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kareimt.anwarresala.R
import com.kareimt.anwarresala.data.local.volunteer.VolunteerEntity

@Composable
fun ActionConfirmationDialog(
    title: String,
    message: String,
    confirmText: String,
    dismissText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title, style = MaterialTheme.typography.titleMedium) },
        text = { Text(text = message, style = MaterialTheme.typography.bodyMedium) },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors()
            ) {
                Text(confirmText)
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text(dismissText)
            }
        }
    )
}

@Composable
fun ErrorMessageCard(message: String) {
    Surface(
        color = MaterialTheme.colorScheme.errorContainer,
        contentColor = MaterialTheme.colorScheme.onErrorContainer,
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = message,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun EmptyState(
    icon: ImageVector,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
    }
}

@Composable
fun VolunteerDetailItem(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(100.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun RequestTypeSelector(
    showPending: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier
    ) {
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier.padding(4.dp)
        ) {
            SegmentedButton(
                selected = showPending,
                onClick = { onToggle(true) },
                shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                colors = SegmentedButtonDefaults.colors()
            ) {
                Row (
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ){
                    Icon(
                        imageVector = Icons.Default.Pending,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(stringResource(R.string.pending))
                }
            }
            SegmentedButton(
                selected = !showPending,
                onClick = { onToggle(false) },
                shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                colors = SegmentedButtonDefaults.colors()
            ) {
                Row (
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ){
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(stringResource(R.string.approved))
                }
            }
        }
    }
}

@Composable
fun VolunteerRequestsList(
    volunteers: List<VolunteerEntity>,
    isPending: Boolean,
    onApprove: ((VolunteerEntity) -> Unit)? = null,
    onReject: ((VolunteerEntity) -> Unit)? = null,
    onDelete: ((VolunteerEntity) -> Unit)? = null,
    modifier: Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(volunteers) { volunteer ->
            VolunteerRequestCard(
                volunteer = volunteer,
                isPending = isPending,
                onApprove = onApprove,
                onReject = onReject,
                onDelete = onDelete
            )
        }
    }
}

@Composable
fun VolunteerRequestCard(
    volunteer: VolunteerEntity,
    isPending: Boolean,
    onApprove: ((VolunteerEntity) -> Unit)? = null,
    onReject: ((VolunteerEntity) -> Unit)? = null,
    onDelete: ((VolunteerEntity) -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header with avatar and name
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Name and email
                Column {
                    Text(
                        text = volunteer.name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = volunteer.email,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Details
            VolunteerDetailItem(
                label = stringResource(R.string.responsibility),
                value = volunteer.responsibility
            )
            VolunteerDetailItem(
                label = stringResource(R.string.branch),
                value = volunteer.branch
            )
            VolunteerDetailItem(
                label = stringResource(R.string.committee),
                value = volunteer.committee
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Action buttons
            if (isPending) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { onReject?.invoke(volunteer) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        ),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text(stringResource(R.string.reject))
                    }
                    Button(
                        onClick = { onApprove?.invoke(volunteer) },
                        colors = ButtonDefaults.buttonColors()
                    ) {
                        Text(stringResource(R.string.approve))
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.ThumbUp,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp).scale(scaleX = -1f, scaleY = 1f)
                        )

                    }
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = { onDelete?.invoke(volunteer) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        )
                    ) {
                        Text(stringResource(R.string.delete))
                    }
                }
            }
        }
    }
}
