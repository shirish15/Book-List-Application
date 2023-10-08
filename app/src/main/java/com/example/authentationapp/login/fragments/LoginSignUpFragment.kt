package com.example.authentationapp.login.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.authentationapp.book_list.models.BookListResponseModel
import com.example.authentationapp.login.composables.LoginSignUpScreen
import com.example.authentationapp.login.directions.LoginSignUpDirections
import com.example.authentationapp.login.viewmodels.LoginSignUpEvents
import com.example.authentationapp.login.viewmodels.LoginSignUpViewModel
import com.example.authentationapp.room.AppDatabase
import com.example.authentationapp.utils.navigateForward
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginSignUpFragment : Fragment() {

    private val loginSignUpViewModel by viewModels<LoginSignUpViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.context?.getBookList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireActivity()).apply {
        setContent {
            val uiState by loginSignUpViewModel.uiState.collectAsStateWithLifecycle()
            val room: AppDatabase = AppDatabase.getInstance(this.context)
            LoginSignUpScreen(
                password = uiState.password,
                name = uiState.name,
                room = room,
                loginToast = uiState.loginToast,
                loginUser = uiState.loginUser,
                nameError = uiState.nameError,
                btnEnabled = uiState.btnEnabled,
                passwordError = uiState.passwordError,
                countriesApiError = uiState.apiError,
                currentSelectedCountry = uiState.currentSelectedCountry,
                countryList = uiState.countryList,
                setEvents = loginSignUpViewModel::setEvents
            ) { directions ->
                when (directions) {
                    LoginSignUpDirections.BookList -> {
                        lifecycleScope.launch(Dispatchers.IO) {
                            val currentUser = room.userDao().getAll().first {
                                it.name == uiState.name && it.password == uiState.password && it.country == uiState.currentSelectedCountry?.country
                            }
                            if (!currentUser.bookList.isNullOrEmpty()) {
                                navigateForward(
                                    destination = LoginSignUpFragmentDirections.actionLoginSignUpFragmentToBookListFragment(
                                        currentUser = currentUser
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun Context.getBookList() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val inputStream = assets.open("bookListResponse.json")
                val size = inputStream.available()
                val buffer = ByteArray(size)
                inputStream.read(buffer)
                inputStream.close()
                loginSignUpViewModel.setEvents(
                    LoginSignUpEvents.UpdateBookData(
                        bookList = Gson().fromJson(
                            String(buffer, Charsets.UTF_8),
                            Array<BookListResponseModel>::class.java
                        ).asList()
                    )
                )
            } catch (e: Exception) {
                loginSignUpViewModel.setEvents(
                    LoginSignUpEvents.UpdateBookData(
                        bookList = emptyList()
                    )
                )
            }
        }
    }
}