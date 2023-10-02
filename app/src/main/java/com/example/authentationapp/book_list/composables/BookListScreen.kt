package com.example.authentationapp.book_list.composables

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.authentationapp.R
import com.example.authentationapp.book_list.directions.BookListDirections
import com.example.authentationapp.book_list.enums.Filters
import com.example.authentationapp.book_list.models.BookListResponseModel
import com.example.authentationapp.book_list.viewmodels.BookListEvents
import com.example.authentationapp.utils.CustomLoader
import com.example.authentationapp.utils.TitleBar

@Composable
fun BookListScreen(
    loading: Boolean,
    checked: Boolean,
    bookList: List<BookListResponseModel>?,
    setEvents: (BookListEvents) -> Unit,
    navigate: (BookListDirections) -> Unit
) {
    LaunchedEffect(key1 = Unit, block = {
        setEvents(BookListEvents.UpdateList)
    })
    val context = LocalContext.current
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    listOf(Color.Black, Color.DarkGray)
                )
            )
    ) {
        val filterList = remember {
            listOf(Filters.TITLE, Filters.HITS, Filters.ID)
        }
        TitleBar(title = stringResource(R.string.your_bookshelf), showBack = false, onLogout = {
            Toast.makeText(
                context,
                context.getText(R.string.logout_successful), Toast.LENGTH_LONG
            ).show()
            navigate(BookListDirections.Logout)
        }) {
            navigate(BookListDirections.Back)
        }
        Row(
            modifier = Modifier.padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.sort_by),
                style = MaterialTheme.typography.titleLarge.copy(color = Color.White),
                modifier = Modifier.weight(1F),
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.ascending),
                    style = MaterialTheme.typography.titleLarge.copy(color = Color.White),
                )
                Switch(
                    checked = checked,
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = Color.White,
                        uncheckedTrackColor = Color.Transparent,
                        checkedThumbColor = Color.DarkGray
                    ),
                    onCheckedChange = {
                        setEvents(BookListEvents.SwitchButton(it))
                    }
                )
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            var selected: Filters? by remember {
                mutableStateOf(null)
            }
            filterList.forEach {
                ChipComposable(text = it.toString(), isSelected = selected == it) {
                    selected = it
                    setEvents(BookListEvents.SelectFilter(it))
                }
            }
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (!loading) {
                items(bookList?.size ?: 0) { index ->
                    bookList?.get(index)?.let {
                        BookCard(bookItem = it, onFavClick = {
                            setEvents(BookListEvents.MarkBookFavourite(it))
                        }, onItemClick = {
                            navigate(BookListDirections.BookListDescription(it))
                        })
                    }
                }
            } else {
                item {
                    CustomLoader()
                }
            }
        }
    }
}

@Composable
fun ChipComposable(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .wrapContentSize()
            .background(
                if (isSelected) Color.White else Color.Transparent,
                shape = RoundedCornerShape(20.dp)
            )
            .border(color = Color.White, shape = RoundedCornerShape(24.dp), width = 1.dp)
            .clickable(interactionSource = remember {
                MutableInteractionSource()
            }, indication = null, onClick = onClick)
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.Black else Color.White,
            modifier = Modifier.padding(horizontal = 40.dp, vertical = 12.dp)
        )
    }
}

@Composable
fun BookCard(
    modifier: Modifier = Modifier,
    bookItem: BookListResponseModel,
    color: Color = Color.White,
    onFavClick: () -> Unit,
    onItemClick: () -> Unit = {}
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .border(color = Color.White, shape = RoundedCornerShape(20.dp), width = 2.dp)
            .clickable(interactionSource = remember {
                MutableInteractionSource()
            }, indication = null, onClick = onItemClick)
            .padding(16.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(bookItem.image),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(20.dp))
        )
        Column(
            modifier = Modifier.weight(1F),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = bookItem.title.orEmpty(),
                style = MaterialTheme.typography.titleMedium.copy(color = color)
            )
            if (bookItem.hits != null) {
                Text(
                    text = stringResource(id = R.string.hits, bookItem.hits),
                    style = MaterialTheme.typography.titleMedium.copy(color = color)
                )
            }
        }
        Icon(
            imageVector = if (bookItem.selected) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            tint = color,
            contentDescription = null,
            modifier = Modifier.clickable(interactionSource = remember {
                MutableInteractionSource()
            }, indication = rememberRipple(bounded = false), onClick = onFavClick)
        )
    }
}