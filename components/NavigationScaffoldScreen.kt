package org.babetech.borastock.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.babetech.borastock.ui.navigation.AppDestinations

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
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    },
                    navigationIcon = {
                        if (isExpanded) {
                            IconButton(onClick = { showNavigationDrawer = !showNavigationDrawer }) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Menu",
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    },
                    actions = {
                        IconButton(onClick = { isMenuExpanded = true }) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Profil",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                        DropdownMenu(
                            expanded = isMenuExpanded,
                            onDismissRequest = { isMenuExpanded = false }
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
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
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
                                Icon(
                                    painter = dest.icon(),
                                    contentDescription = dest.contentDescription,
                                    tint = if (currentDestination == dest)
                                        MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onSurface
                                )
                            },
                            label = {
                                if (layoutType != NavigationSuiteType.NavigationBar) {
                                    Text(
                                        text = dest.label,
                                        color = if (currentDestination == dest)
                                            MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.onSurface
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
}
