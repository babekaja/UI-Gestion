package org.babetech.borastock.ui.screens.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.*
import androidx.compose.material3.adaptive.navigation.rememberSupportingPaneScaffoldNavigator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun AccueilScreen() {
    val navigator = rememberSupportingPaneScaffoldNavigator()
    val scope = rememberCoroutineScope()

    SupportingPaneScaffold(
        value = navigator.scaffoldValue,
        directive = navigator.scaffoldDirective,
        mainPane = {
            with(this) {
                MainDashboardPane(
                    showChartButton = navigator.scaffoldValue[SupportingPaneScaffoldRole.Supporting] == PaneAdaptedValue.Hidden,
                    onToggleChart = {
                        scope.launch {
                            navigator.navigateTo(SupportingPaneScaffoldRole.Supporting)
                        }
                    }
                )
            }
        },
        supportingPane = {
            with(this) {
                SupportingChartPane(
                    onBack = {
                        scope.launch { navigator.navigateBack() }
                    }
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ThreePaneScaffoldScope.MainDashboardPane(
    showChartButton: Boolean,
    onToggleChart: () -> Unit
) {
    AnimatedPane(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Column {
            Spacer(Modifier.height(24.dp))
            DashboardMetricsGrid()
            Spacer(Modifier.height(24.dp))
            RecentMovementsList()
            if (showChartButton) {
                Spacer(Modifier.height(24.dp))
                Button(onClick = onToggleChart) {
                    Text("Afficher / Masquer le graphique")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ThreePaneScaffoldScope.SupportingChartPane(
    onBack: () -> Unit
) {

    val navigator = rememberSupportingPaneScaffoldNavigator()
    AnimatedPane(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (navigator.scaffoldValue[SupportingPaneScaffoldRole.Supporting] == PaneAdaptedValue.Hidden) {
                IconButton(onClick = onBack, modifier = Modifier.align(Alignment.TopStart)) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                }
            }
            SimpleBarChart()
        }
    }
}

@Composable
fun DashboardMetricsGrid() {
    LazyVerticalGrid(columns = GridCells.Adaptive(150.dp)) {
        item { StatCard("Produits", "128", Icons.Default.Info) }
        item { StatCard("Fournisseurs", "24", Icons.Default.Info) }
        item { StatCard("Stock Total", "2350", Icons.Default.Info) }
    }
}

@Composable
fun StatCard(title: String, value: String, icon: ImageVector) {
    Card(
        modifier = Modifier.padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Column {
                Text(text = title, style = MaterialTheme.typography.labelLarge)
                Text(text = value, style = MaterialTheme.typography.headlineMedium)
            }
        }
    }
}

@Composable
fun RecentMovementsList() {
    Column {
        Text("Mouvements récents", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        val movements = listOf(
            "Ajout 20 unités - Produit A",
            "Sortie 5 unités - Produit B",
            "Ajout 50 unités - Produit C"
        )

        movements.forEach {
            Text("- $it", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun SimpleBarChart() {
    Text("Graphique à venir...", style = MaterialTheme.typography.titleMedium)
}
