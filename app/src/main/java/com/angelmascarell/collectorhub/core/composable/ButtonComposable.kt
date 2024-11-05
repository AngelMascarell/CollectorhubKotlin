package com.angelmascarell.collectorhub.core.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.angelmascarell.collectorhub.ui.theme.MyUltraBlue

@Composable
fun MyButton(text: String, horizontalPadding: Dp = 0.dp, onclick: () -> Unit) {
    Button(
        onClick = onclick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding)
            .heightIn(min = 40.dp)
            .background(color = MyUltraBlue, shape = MaterialTheme.shapes.medium),
        colors = ButtonColors(
            containerColor = MyUltraBlue,
            contentColor = Color.White,
            disabledContainerColor = MyUltraBlue,
            disabledContentColor = MyUltraBlue
        )
    ) {
        Text(text, color = Color.White)
    }
}