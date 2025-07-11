package org.babetech.borastock.ui

import androidx.compose.runtime.Composable
import org.babetech.borastock.ui.navigation.MainNavHost
import org.babetech.borastock.ui.theme.AppTheme
import org.babetech.borastock.ui.theme.BoraStockAppTheme


@Composable
fun BoraStockApp(
    themeSelection: String,
    onThemeChange: (String) -> Unit
) {
    // Tu peux encapsuler ton thème ici si tu veux :

        MainNavHost(
            themeSelection = themeSelection,
            onThemeChange = onThemeChange,

        )

}
