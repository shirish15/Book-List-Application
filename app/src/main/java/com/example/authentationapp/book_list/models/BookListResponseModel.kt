package com.example.authentationapp.book_list.models

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class BookListResponseModel(
    val id: String?,
    val image: String?,
    val hits: Long?,
    val alias: String?,
    val title: String?,
    var selected: Boolean = false,
    val lastChapterDate: Long?
) : Parcelable