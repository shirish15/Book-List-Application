package com.example.authentationapp.book_list.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.authentationapp.book_list.enums.Filters
import com.example.authentationapp.book_list.models.BookListResponseModel
import com.example.authentationapp.room.AppDatabase
import com.example.authentationapp.room.User
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookListViewModel @Inject constructor(
    private val room: AppDatabase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(BookListUiState())
    val uiState = _uiState.asStateFlow()

    private val currentUser = savedStateHandle.get<User>("currentUser")

    init {
        setEvents(BookListEvents.UpdateList)
    }


    //function to handle events
    fun setEvents(events: BookListEvents) {
        when (events) {

            is BookListEvents.SelectFilter -> {
                _uiState.update {
                    it.copy(selectedFilter = events.selectedFilter)
                }
                filterList()
            }

            is BookListEvents.MarkBookFavourite -> {
                val list = _uiState.value.bookList.toMutableList()
                val item = list[events.index]
                list[events.index] = item.copy(selected = !item.selected)
                viewModelScope.launch(Dispatchers.IO) {
                    if (currentUser != null) {
                        room.userDao()
                            .updateUser(user = currentUser.copy(bookList = list))
                    }
                    _uiState.update {
                        it.copy(bookList = list)
                    }
                }
            }

            is BookListEvents.SwitchButton -> {
                _uiState.update {
                    it.copy(checked = events.switch)
                }
                filterList()
            }

            is BookListEvents.UpdateList -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val list = room.userDao().getAll().first {
                        it.name == currentUser?.name && it.password == currentUser.password && it.country == currentUser.country
                    }.bookList.orEmpty()
                    var l = _uiState.value.bookList.toMutableList()
                    if (_uiState.value.bookList.isNotEmpty()) {
                        list.forEachIndexed { idx, item ->
                            if (l[idx].id == item.id && l[idx].selected != item.selected) {
                                l[idx] = l[idx].copy(selected = item.selected)
                            }
                        }
                    } else {
                        l = list.toMutableList()
                    }
                    _uiState.update { state ->
                        state.copy(bookList = l)
                    }
                }
            }
        }
    }

    private fun filterList() {
        _uiState.update {
            it.copy(loading = true)
        }
        viewModelScope.launch(Dispatchers.Default) {
            delay(2000)
            val list = when (_uiState.value.selectedFilter) {
                Filters.TITLE -> {
                    _uiState.value.bookList.sortedBy {
                        it.title
                    }
                }

                Filters.HITS -> {
                    _uiState.value.bookList.sortedBy {
                        it.hits
                    }
                }

                Filters.ID -> {
                    _uiState.value.bookList.sortedBy {
                        it.id
                    }
                }

                Filters.FAV -> {
                    _uiState.value.bookList.sortedBy {
                        it.selected
                    }
                }

                else -> {
                    _uiState.value.bookList
                }
            }
            _uiState.update {
                it.copy(
                    bookList = if (_uiState.value.checked) list else list.reversed(),
                    loading = false
                )
            }
        }
    }

    data class BookListUiState(
        val loginUser: Boolean = false,
        val checked: Boolean = false,
        val loading: Boolean = false,
        val selectedFilter: Filters? = null,
        val bookList: List<BookListResponseModel> = emptyList()
    )
}

sealed interface BookListEvents {
    data class SelectFilter(val selectedFilter: Filters) : BookListEvents
    data class MarkBookFavourite(val index: Int) : BookListEvents
    data class SwitchButton(val switch: Boolean) : BookListEvents
    object UpdateList : BookListEvents
}