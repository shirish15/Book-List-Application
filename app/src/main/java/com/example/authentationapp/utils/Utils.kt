package com.example.authentationapp.utils

import android.annotation.SuppressLint
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date


//wraps api result in a sealed class
sealed class ApiResultWrapper<T : Any> {
    class Success<T : Any>(val data: T) : ApiResultWrapper<T>()
    class Error<T : Any>(val message: String?) : ApiResultWrapper<T>()
}

@Composable
fun LabelComposable(text: String) {
    Text(text = text)
}

@Composable
fun CustomLoader() {
    CircularProgressIndicator(
        color = Color.LightGray,
        strokeWidth = 8.dp
    )
}

@SuppressLint("SimpleDateFormat")
fun Long.convertLongToTime(): String {
    val date = Date(this)
    val format = SimpleDateFormat("dd-MM-yyyy")
    return format.format(date)
}