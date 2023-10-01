package com.example.authentationapp.room

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.authentationapp.book_list.models.BookListResponseModel
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class User(
    @PrimaryKey val name: String,
    @ColumnInfo(name = "password") val password: String?,
    @ColumnInfo(name = "country") val country: String?,
    @ColumnInfo(name = "bookList") val bookList: List<BookListResponseModel>?
) : Parcelable