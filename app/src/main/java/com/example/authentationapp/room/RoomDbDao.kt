package com.example.authentationapp.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.TypeConverter
import androidx.room.Update
import com.example.authentationapp.book_list.models.BookListResponseModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Insert
    fun insertUser(user: User)

    @Update
    fun updateUser(user: User)
}

class Converters {
    @TypeConverter
    fun fromString(value: String?): List<BookListResponseModel> {
        if (value == null) {
            return emptyList()
        }
        val listType = object : TypeToken<List<BookListResponseModel>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toString(value: List<BookListResponseModel>?): String {
        return Gson().toJson(value)
    }
}