package com.example.authentationapp.book_list.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.navArgs
import com.example.authentationapp.book_list.composables.BookListScreen
import com.example.authentationapp.book_list.directions.BookListDirections
import com.example.authentationapp.book_list.viewmodels.BookListViewModel
import com.example.authentationapp.utils.navigateBack
import com.example.authentationapp.utils.navigateForward
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookListFragment : Fragment() {
    private val bookListViewModel by viewModels<BookListViewModel>()
    private val args by navArgs<BookListFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireActivity()).apply {
        setContent {
            val uiState by bookListViewModel.uiState.collectAsStateWithLifecycle()
            BookListScreen(
                bookList = uiState.bookList,
                loading = uiState.loading,
                checked = uiState.checked,
                setEvents = bookListViewModel::setEvents
            ) { directions ->
                when (directions) {
                    BookListDirections.Back -> {
                        navigateBack()
                    }

                    is BookListDirections.BookListDescription -> {
                        val index = args.currentUser.bookList?.indexOfFirst {
                            it.id == directions.selectedItem.id
                        }
                        index?.let {
                            navigateForward(
                                destination = BookListFragmentDirections.actionBookListFragmentToBookListDescriptionFragment(
                                    currentUser = args.currentUser,
                                    index = it
                                )
                            )
                        }
                    }

                    BookListDirections.Logout -> {
                        navigateForward(destination = BookListFragmentDirections.actionBookListFragmentToLoginSignUpFragment())
                    }
                }
            }
        }
    }
}