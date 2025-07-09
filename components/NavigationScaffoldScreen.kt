package org.babetech.borastock.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.babetech.borastock.ui.navigation.AppDestinations
import org.babetech.borastock.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationScaffoldScreen(
    title: String,
    currentDestination: AppDestinations,
    onDestinationChanged: (AppDestinations) -> Unit,
    content: @Composable () -> Unit
) {
    var isMenuExpanded by rememberSaveable { mutableStateOf(false) }
    var showNavigationDrawer by rememberSaveable { mutableStateOf(true) }

    BoxWithConstraints {
        val isCompact = maxWidth < 600.dp
        val isMedium = maxWidth in 600.dp..839.dp
        val isExpanded = maxWidth >= 840.dp

        val layoutType = when {
            isExpanded && showNavigationDrawer -> NavigationSuiteType.NavigationDrawer
            isExpanded && !showNavigationDrawer -> NavigationSuiteType.NavigationRail
            isMedium -> NavigationSuiteType.NavigationRail
            else -> NavigationSuiteType.NavigationBar
        }

        val destinationsToShow = if (layoutType == NavigationSuiteType.NavigationBar)
            AppDestinations.entries.take(5)
        else AppDestinations.entries

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        brush = Brush.linearGradient(
                                            colors = listOf(
                                                MaterialTheme.colorScheme.primary,
                                                MaterialTheme.colorScheme.secondary
                                            )
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "B",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                )
                            }
                            Text(
                                text = title,
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                        }
                    },
                    navigationIcon = {
                        if (isExpanded) {
                            IconButton(
                                onClick = { showNavigationDrawer = !showNavigationDrawer },
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surfaceContainer)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Menu",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    },
                    actions = {
                        // Notification button
                        IconButton(
                            onClick = { /* TODO: Handle notifications */ },
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceContainer)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        // Profile button
                        IconButton(
                            onClick = { isMenuExpanded = true },
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.primary,
                                            MaterialTheme.colorScheme.secondary
                                        )
                                    )
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Profil",
                                tint = Color.White
                            )
                        }
                        
                        DropdownMenu(
                            expanded = isMenuExpanded,
                            onDismissRequest = { isMenuExpanded = false },
                            modifier = Modifier
                                .shadow(8.dp, RoundedCornerShape(12.dp))
                                .background(
                                    MaterialTheme.colorScheme.surface,
                                    RoundedCornerShape(12.dp)
                                )
                        ) {
                            DropdownMenuItem(
                                text = { Text("Mon profil") },
                                onClick = {
                                    isMenuExpanded = false
                                    // TODO: action profil
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Déconnexion") },
                                onClick = {
                                    isMenuExpanded = false
                                    // TODO: action déconnexion
                                }
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer
                    ),
                    modifier = Modifier.shadow(
                        elevation = 4.dp,
                        spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    )
                )
            }
        ) { innerPadding ->
            NavigationSuiteScaffold(
                modifier = Modifier.padding(innerPadding),
                layoutType = layoutType,
                navigationSuiteItems = {
                    destinationsToShow.forEach { dest ->
                        item(
                            selected = currentDestination == dest,
                            onClick = { onDestinationChanged(dest) },
                            icon = {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            if (currentDestination == dest)
                                                MaterialTheme.colorScheme.primaryContainer
                                            else Color.Transparent
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        painter = dest.icon(),
                                        contentDescription = dest.contentDescription,
                                        tint = if (currentDestination == dest)
                                            MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            },
                            label = {
                                if (layoutType != NavigationSuiteType.NavigationBar) {
                                    Text(
                                        text = dest.label,
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = if (currentDestination == dest) 
                                                FontWeight.Bold else FontWeight.Medium
                                        ),
                                        color = if (currentDestination == dest)
                                            MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        )
                    }
                }
            ) {
                content()
            }
        }
    }
