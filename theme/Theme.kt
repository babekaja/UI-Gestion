

package org.babetech.borastock.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier


private val localColorScheme = compositionLocalOf { ColorScheme() }
private val localTypography = compositionLocalOf { Typography() }
private val localShape = compositionLocalOf { Shape.regular() }
private val localSpacing = compositionLocalOf { Spacing.regular() }
private val localSize = compositionLocalOf { Size() }

private val localThemeIsDark = compositionLocalOf { mutableStateOf(true) }
private val localFontScale = compositionLocalOf { mutableStateOf(1f) }
private val localUiScale = compositionLocalOf { mutableStateOf(1f) }

object AppTheme {

    val colors: ColorScheme
        @Composable @ReadOnlyComposable get() = localColorScheme.current

    val typography: Typography
        @Composable @ReadOnlyComposable get() = localTypography.current

    val shape: Shape
        @Composable @ReadOnlyComposable get() = localShape.current

    val spacing: Spacing
        @Composable @ReadOnlyComposable get() = localSpacing.current

    val size: Size
        @Composable @ReadOnlyComposable get() = localSize.current



    val isDark: MutableState<Boolean>
        @Composable @ReadOnlyComposable get() = localThemeIsDark.current

    val fontScale: MutableState<Float>
        @Composable @ReadOnlyComposable get() = localFontScale.current

    val uiScale: MutableState<Float>
        @Composable @ReadOnlyComposable get() = localUiScale.current
}


@Composable
fun BoraStockAppTheme(content: @Composable () -> Unit) {
    val isDark = remember { mutableStateOf(false) } // Default to light theme
    val colorScheme: ColorScheme = if (isDark.value) darkScheme else lightScheme
    val fontScale = remember { mutableStateOf(1f) }
    val typography = mutableStateOf(provideTypography(fontScale.value))
    val uiScale = remember { mutableStateOf(1f) }
    val fixedSize = mutableStateOf(provideSize(uiScale.value))

//    AdaptiveLayout(adaptiveLayoutType, contentType)
//    SystemAppearance(!isDark.value)

    CompositionLocalProvider(
        localColorScheme provides colorScheme,
        localTypography provides typography.value,
       localSpacing provides Spacing.regular(),
       localSize provides fixedSize.value,
        localShape provides Shape.regular(),
        //localAdaptiveLayoutType provides adaptiveLayoutType,
       // localContentType provides contentType,
       localThemeIsDark provides isDark,
       localFontScale provides fontScale,
        localUiScale provides uiScale,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            AppTheme.colors.surface,
                            AppTheme.colors.surfaceContainer.copy(alpha = 0.1f)
                        )
                    )
                )
        ) {
            content()
        }
    }