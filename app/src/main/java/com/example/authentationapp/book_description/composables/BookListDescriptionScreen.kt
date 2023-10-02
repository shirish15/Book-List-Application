package com.example.authentationapp.book_description.composables

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.authentationapp.R
import com.example.authentationapp.book_description.directions.BookListDescriptionDirections
import com.example.authentationapp.book_list.composables.BookCard
import com.example.authentationapp.book_list.models.BookListResponseModel
import com.example.authentationapp.utils.TitleBar
import com.example.authentationapp.utils.convertLongToTime

@Composable
fun BookListDescriptionScreen(
    bookCardItem: BookListResponseModel?,
    updateRoom: () -> Unit,
    navigate: (BookListDescriptionDirections) -> Unit
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.linearGradient(
                    listOf(Color.Black, Color.DarkGray)
                )
            ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TitleBar(title = stringResource(R.string.book_description), onLogout = {
            Toast.makeText(
                context,
                context.getText(R.string.logout_successful), Toast.LENGTH_LONG
            ).show()
            navigate(BookListDescriptionDirections.Logout)
        }) {
            navigate(BookListDescriptionDirections.Back)
        }
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            bookCardItem?.let {
                BookCard(bookItem = it, color = Color.White, onFavClick = {
                    updateRoom()
                })
            }
            bookCardItem?.alias?.let {
                Text(
                    text = stringResource(id = R.string.alias, it),
                    style = MaterialTheme.typography.titleLarge.copy(color = Color.White)
                )
            }
            bookCardItem?.lastChapterDate?.let {
                Text(
                    text = stringResource(id = R.string.updated_date, it.convertLongToTime()),
                    style = MaterialTheme.typography.titleLarge.copy(color = Color.White)
                )
            }
            val textStyle = SpanStyle(color = Color.White, fontSize = 16.sp)
            Text(
                text = buildAnnotatedString {
                    withStyle(textStyle) {
                        append(stringResource(id = R.string.summary))
                    }
                    withStyle(textStyle.copy(fontSize = 12.sp)) {
                        append(
                            LoremIpsum(words = 200).values.toList().joinToString(separator = " ")
                        )
                    }
                }
            )
        }
    }
}