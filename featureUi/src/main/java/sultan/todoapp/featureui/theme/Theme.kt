package sultan.todoapp.featureui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
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
import sultan.todoapp.domain.Importance

private val DarkColorScheme = darkColorScheme(
    primary = BackDarkPrimary,
    secondary = BackDarkSecondary,
    tertiary = Pink80,
    background = BackDarkPrimary,
    onPrimary = LabelDarkPrimary,
    onBackground = LabelDarkPrimary,
    primaryContainer = CheckboxDarkChecked,
    error = ErrorDarkColor,
    onPrimaryContainer = BlueDark,
    onTertiary = TertiaryDark.withTransparency(0.4f),
    onSecondary = ElevatedBackDark,
    surfaceContainer = SupportDark.withTransparency(0.32f),
    surface = SupportSeperatorDark.withTransparency(0.2f)
)

private val LightColorScheme = lightColorScheme(
    primary = BackLightPrimary,
    secondary = BackLightSecondary,
    tertiary = Pink40,
    background = BackLightPrimary,
    onPrimary = LabelLightPrimary,
    onBackground = LabelLightPrimary,
    primaryContainer = CheckboxLightChecked,
    error = ErrorLightColor,
    onPrimaryContainer = BlueLight,
    onTertiary = TertiaryLight.withTransparency(0.3f),
    onSecondary = ElevatedBackLight,
    surfaceContainer = SupportLight.withTransparency(0.06f),
    surface = SupportSeperatorLight.withTransparency(0.2f)
    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun TodoAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
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
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

fun Color.withTransparency(alpha: Float): Color {
    return this.copy(alpha = alpha)
}

@Composable
fun taskCheckBoxColors(importance: Importance): CheckboxColors {
    return when (importance) {
        Importance.LOW -> CheckboxDefaults.colors(
            checkedColor = MaterialTheme.colorScheme.primaryContainer,
            uncheckedColor = MaterialTheme.colorScheme.secondaryContainer,
            checkmarkColor = MaterialTheme.colorScheme.background
        )

        Importance.HIGH -> CheckboxDefaults.colors(
            checkedColor = MaterialTheme.colorScheme.primaryContainer,
            uncheckedColor = MaterialTheme.colorScheme.error,
            checkmarkColor = MaterialTheme.colorScheme.background
        )

        Importance.MEDIUM -> CheckboxDefaults.colors(
            checkedColor = MaterialTheme.colorScheme.primaryContainer,
            uncheckedColor = MaterialTheme.colorScheme.secondaryContainer,
            checkmarkColor = MaterialTheme.colorScheme.background
        )
    }
}