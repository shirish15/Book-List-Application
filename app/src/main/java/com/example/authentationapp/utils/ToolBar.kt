package com.example.authentationapp.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.authentationapp.R

@Composable
fun TitleBar(title: String, showBack: Boolean = true, onLogout: () -> Unit, onClick: () -> Unit) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (showBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable(interactionSource = remember {
                            MutableInteractionSource()
                        }, indication = rememberRipple(bounded = false), onClick = onClick)
                )
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(color = Color.White),
                modifier = Modifier
                    .weight(1F)
                    .padding(horizontal = 20.dp)
            )
            Text(
                text = stringResource(R.string.logout),
                style = MaterialTheme.typography.titleLarge.copy(color = Color.White),
                modifier = Modifier
                    .padding(start = 20.dp)
                    .clickable(interactionSource = remember {
                        MutableInteractionSource()
                    }, indication = null, onClick = onLogout)
            )
        }
        Divider(modifier = Modifier.fillMaxWidth(), color = Color.White, thickness = 2.dp)
    }
}