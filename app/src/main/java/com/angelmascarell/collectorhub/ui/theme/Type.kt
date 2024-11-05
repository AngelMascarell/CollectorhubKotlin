package com.angelmascarell.collectorhub.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.angelmascarell.collectorhub.R

// Set of Material typography styles to start with
val AudioWideFontFamily = FontFamily(
    Font(R.font.audiowide_regular, FontWeight.Normal)
)

val TitleTextStyle: TextStyle = TextStyle (
    color = MyUltraBlue,
    background = Color.Transparent,
    textAlign = TextAlign.Center,
    fontStyle = FontStyle.Normal,
    fontFamily = AudioWideFontFamily,
    fontSize = 16.sp,
    shadow = Shadow(color = Color.White, offset = Offset(.4f,.4f), blurRadius = .3f)
)

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)