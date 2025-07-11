package org.babetech.borastock.ui.screens.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import org.babetech.borastock.ui.components.NavigationScaffoldScreen
import org.babetech.borastock.ui.navigation.AppDestinations
import org.babetech.borastock.ui.screens.stock.StockScreen
import org.babetech.borastock.ui.screens.entries.EntriesScreen
import org.babetech.borastock.ui.screens.exits.ExitsScreen

@Composable
fun DashboardScreen() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.Home) }

    NavigationScaffoldScreen(
        title = "BoraStock",
        currentDestination = currentDestination,
        onDestinationChanged = { currentDestination = it }
    ) {
        when (currentDestination) {
            AppDestinations.Home -> AccueilScreen()
            AppDestinations.Stocks -> StockScreen()
            AppDestinations.Entries -> EntriesScreen()
            AppDestinations.Exits -> ExitsScreen()
            AppDestinations.Suppliers -> Box(Modifier.fillMaxSize()) { Text("Fournisseurs") }
            AppDestinations.Analytics -> Box(Modifier.fillMaxSize()) { Text("Analytique") }
            AppDestinations.Settings -> Box(Modifier.fillMaxSize()) { Text("Paramètres") }
        }
    }
}
