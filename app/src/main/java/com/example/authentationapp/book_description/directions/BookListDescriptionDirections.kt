package com.example.authentationapp.book_description.directions

sealed interface BookListDescriptionDirections {
    object Back : BookListDescriptionDirections
    object Logout : BookListDescriptionDirections
}