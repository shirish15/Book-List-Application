package com.example.authentationapp.utils

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ButtonComposable(
    modifier: Modifier = Modifier,
    btnEnabled: Boolean = true,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.size(80.dp),
        enabled = btnEnabled,
        shape = CircleShape,
        border = BorderStroke(1.dp, Color.White),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Blue)
    ) {
        Icon(
            Icons.Default.ArrowForward,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            tint = Color.White
        )
    }
}