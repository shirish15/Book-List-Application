package com.example.authentationapp.utils

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.authentationapp.ui.theme.crimson

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenericTextField(
    modifier: Modifier = Modifier,
    error: Int? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    readOnly: Boolean = false,
    label: @Composable (() -> Unit)? = null,
    editable: Boolean = true,
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        containerColor = Color.Transparent,
        textColor = if (error == null) Color.White else crimson,
        cursorColor = Color.White,
        focusedLabelColor = Color.White,
        disabledLabelColor = Color.DarkGray,
        disabledTextColor = Color.DarkGray,
        disabledBorderColor = Color.DarkGray,
        unfocusedLabelColor = Color.White,
        unfocusedBorderColor = Color.White,
        focusedBorderColor = Color.White,
        errorBorderColor = crimson,
        errorCursorColor = crimson,
        errorLabelColor = crimson,
    ),
    value: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
    onValueChange: (String) -> Unit,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            trailingIcon = trailingIcon,
            label = label,
            maxLines = 1,
            singleLine = true,
            readOnly = readOnly,
            visualTransformation = visualTransformation,
            isError = error != null,
            colors = colors,
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth(),
            enabled = editable,
            value = value,
            onValueChange = onValueChange,
            keyboardOptions = keyboardOptions,
        )
        error?.let {
            Text(text = stringResource(id = error), color = crimson)
        }
    }
}