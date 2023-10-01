package com.example.authentationapp.book_description.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.room.Room
import com.example.authentationapp.book_description.composables.BookListDescriptionScreen
import com.example.authentationapp.book_description.directions.BookListDescriptionDirections
import com.example.authentationapp.book_list.models.BookListResponseModel
import com.example.authentationapp.room.AppDatabase
import com.example.authentationapp.utils.navigateBack
import com.example.authentationapp.utils.navigateForward
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookListDescriptionFragment : Fragment() {

    private val args by navArgs<BookListDescriptionFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireActivity()).apply {
        setContent {
            val room: AppDatabase = Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "roomDb.db"
            ).build()
            var bookListItem: BookListResponseModel? by remember {
                mutableStateOf(null)
            }
            LaunchedEffect(key1 = Unit, block = {
                lifecycleScope.launch(Dispatchers.IO) {
                    bookListItem = room.userDao().getAll()
                        .first { it.name == args.currentUser.name && it.password == args.currentUser.password && it.country == args.currentUser.country }.bookList?.get(
                            args.index
                        )
                }
            })
            BookListDescriptionScreen(bookCardItem = bookListItem, updateRoom = {
                bookListItem = it.copy(selected = !it.selected)
                lifecycleScope.launch(Dispatchers.IO) {
                    val list = args.currentUser.bookList?.toMutableList()
                    list?.set(args.index, it)
                    room.userDao().updateUser(args.currentUser.copy(bookList = list))
                }
            }) { directions ->
                when (directions) {
                    BookListDescriptionDirections.Back -> {
                        navigateBack()
                    }

                    BookListDescriptionDirections.Logout -> {
                        navigateForward(destination = BookListDescriptionFragmentDirections.actionBookListDescriptionFragmentToLoginSignUpFragment())
                    }
                }
            }
        }
    }
}