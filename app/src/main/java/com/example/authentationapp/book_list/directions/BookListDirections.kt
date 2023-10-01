package com.example.authentationapp.book_list.directions

import com.example.authentationapp.book_list.models.BookListResponseModel

sealed interface BookListDirections {
    object Back : BookListDirections
    object Logout : BookListDirections
    data class BookListDescription(val selectedItem: BookListResponseModel) : BookListDirections
}