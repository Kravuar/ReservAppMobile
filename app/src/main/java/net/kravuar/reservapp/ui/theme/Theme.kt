package net.kravuar.reservapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

val DarkColorScheme = darkColorScheme(
    primary = BlueGrey200,
    onPrimary = Color.Black,
    secondary = BlueGrey600,
    onSecondary = Color.Black,
    tertiary = BlueGrey700,
    onTertiary = Color.White,
    background = BlueGrey800,
    onBackground = Color.White,
    surface = BlueGrey900,
    onSurface = Color.White,
    error = Color(0xFFCF6679),
    onError = Color.Black
)

val LightColorScheme = lightColorScheme(
    primary = BlueGrey700,
    onPrimary = Color.White,
    secondary = BlueGrey800,
    onSecondary = Color.White,
    tertiary = BlueGrey900,
    onTertiary = Color.White,
    background = BlueGrey200,
    onBackground = Color.White,
    surface = BlueGrey600,
    onSurface = Color.Black,
    error = Color(0xFFB00020),
    onError = Color.White
)

@Composable
fun ReservAppMobileTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme // Inverted the condition to set light status bars in light theme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}