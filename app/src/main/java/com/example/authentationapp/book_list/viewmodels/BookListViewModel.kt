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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BookListViewModel(application: Application, savedStateHandle: SavedStateHandle) :
    AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(BookListUiState())
    val uiState = _uiState.asStateFlow()

    private val currentUser = savedStateHandle.get<User>("currentUser")

    private val room: AppDatabase = Room.databaseBuilder(
        application.applicationContext,
        AppDatabase::class.java,
        "roomDb.db"
    ).build()


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
                val list = mutableListOf<BookListResponseModel>()
                _uiState.value.bookList.forEach {
                    if (it == events.bookItem) {
                        list.add(it.copy(selected = !it.selected))
                    } else {
                        list.add(it)
                    }
                }
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
                    it.copy(checked = events.swicth)
                }
                filterList()
            }

            is BookListEvents.UpdateList -> {
                _uiState.update {
                    it.copy(loading = true)
                }
                viewModelScope.launch(Dispatchers.IO) {
                    val list = room.userDao().getAll().first {
                        it.name == currentUser?.name && it.password == currentUser.password && it.country == currentUser.country
                    }.bookList.orEmpty()
                    _uiState.update { state ->
                        state.copy(
                            bookList = list,
                            loading = false
                        )
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
    data class MarkBookFavourite(val bookItem: BookListResponseModel) : BookListEvents
    data class SwitchButton(val swicth: Boolean) : BookListEvents
    object UpdateList : BookListEvents
}