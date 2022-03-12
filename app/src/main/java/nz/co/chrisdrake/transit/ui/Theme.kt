package nz.co.chrisdrake.transit.ui.common

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import nz.co.chrisdrake.transit.R

@Composable
fun MainTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    if (darkTheme) TODO()

    MaterialTheme(
        colors = lightColors(
            primary = colorResource(id = R.color.color_primary),
            primaryVariant = Color(0xFF000814),
            secondary = Color(0xFF3da5d9),
            secondaryVariant = Color(0xFFFFD60A),
        ),
        content = content
    )
}

val Colors.textPrimary: Color
    get() = Color.Black.copy(alpha = 0.87f)