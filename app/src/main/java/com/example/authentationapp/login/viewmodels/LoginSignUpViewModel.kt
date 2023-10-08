package com.example.authentationapp.login.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authentationapp.R
import com.example.authentationapp.book_list.models.BookListResponseModel
import com.example.authentationapp.book_list.viewmodels.BookListEvents
import com.example.authentationapp.login.models.CountryModel
import com.example.authentationapp.login.repo.ApiRepo
import com.example.authentationapp.login.repo.impl.ApiRepoImpl
import com.example.authentationapp.room.AppDatabase
import com.example.authentationapp.room.User
import com.example.authentationapp.utils.ApiResultWrapper
import com.example.authentationapp.utils.Validator.validateName
import com.example.authentationapp.utils.Validator.validatePassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginSignUpViewModel @Inject constructor(private val repo: ApiRepo) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginSignUpUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getCountriesList()
    }

    fun setEvents(events: LoginSignUpEvents) {
        when (events) {
            is LoginSignUpEvents.EditName -> {
                val (isNameValid, nameError) = events.name.validateName()
                _uiState.update {
                    it.copy(name = events.name, nameError = nameError, nameValid = isNameValid)
                }
                checkBtnEnabled()
            }

            is LoginSignUpEvents.EditPassword -> {
                val (isPasswordValid, passwordError) = events.name.validatePassword()
                _uiState.update {
                    it.copy(
                        password = events.name,
                        passwordError = passwordError,
                        passwordValid = isPasswordValid
                    )
                }
                checkBtnEnabled()
            }

            is LoginSignUpEvents.SelectCountry -> {
                _uiState.update {
                    it.copy(currentSelectedCountry = events.country)
                }
                checkBtnEnabled()
            }

            is LoginSignUpEvents.UpdateBookData -> {
                _uiState.update {
                    it.copy(
                        bookList = events.bookList,
                    )
                }
            }

            is LoginSignUpEvents.LoginSignUpUser -> {
                viewModelScope.launch(Dispatchers.Default) {
                    val userDao = events.room.userDao()
                    val users: List<User> = userDao.getAll()
                    var foundUser: Boolean = false
                    var foundExactUser: Boolean = false
                    users.forEach {
                        if ((it.name == _uiState.value.name) && (it.password == _uiState.value.password) && (it.country == _uiState.value.currentSelectedCountry?.country)) {
                            foundExactUser = true
                            return@forEach
                        } else if (it.name == _uiState.value.name) {
                            foundUser = true
                            return@forEach
                        }
                    }
                    if (foundExactUser) {
                        _uiState.update { loginState ->
                            loginState.copy(
                                loginToast = R.string.welcome_back_again,
                                loginUser = true
                            )
                        }
                    } else if (foundUser) {
                        _uiState.update { loginState ->
                            loginState.copy(
                                loginToast = R.string.entering_wrong_details_for,
                                loginUser = false
                            )
                        }
                    } else {
                        _uiState.update { loginState ->
                            events.room.userDao().insertUser(
                                User(
                                    name = _uiState.value.name,
                                    country = _uiState.value.currentSelectedCountry?.country,
                                    password = _uiState.value.password,
                                    bookList = _uiState.value.bookList
                                )
                            )
                            loginState.copy(
                                loginToast = R.string.welcome_to_bookstore_app,
                                loginUser = true
                            )
                        }
                    }
                }
            }

            LoginSignUpEvents.ClearToast -> {
                _uiState.update {
                    it.copy(loginToast = null)
                }
            }
        }
    }

    private fun checkBtnEnabled() {
        _uiState.update {
            it.copy(btnEnabled = _uiState.value.nameValid && _uiState.value.passwordValid && _uiState.value.currentSelectedCountry != null)
        }
    }

    private fun getCountriesList() {
        _uiState.update {
            it.copy(apiError = null)
        }
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = repo.getCountriesList()) {
                is ApiResultWrapper.Error -> {
                    _uiState.update {
                        it.copy(apiError = R.string.something_went_wrong)
                    }
                }

                is ApiResultWrapper.Success -> {
                    val data = result.data.data
                    val list = listOf(data?.AO, data?.BJ, data?.BW, data?.DZ, data?.CF)
                    _uiState.update {
                        it.copy(
                            countryList = list,
                        )
                    }
                }
            }
        }
    }

    data class LoginSignUpUiState(
        val loading: Boolean = false,
        val loginUser: Boolean = false,
        val loginToast: Int? = null,
        val countryList: List<CountryModel?>? = null,
        val name: String = "",
        val btnEnabled: Boolean = false,
        val nameValid: Boolean = false,
        val nameError: Int? = null,
        val passwordValid: Boolean = false,
        val passwordError: Int? = null,
        val password: String = "",
        val currentSelectedCountry: CountryModel? = null,
        val apiError: Int? = null,
        val bookList: List<BookListResponseModel> = emptyList(),
    )
}

sealed interface LoginSignUpEvents {
    data class EditName(val name: String) : LoginSignUpEvents
    data class EditPassword(val name: String) : LoginSignUpEvents
    data class SelectCountry(val country: CountryModel) : LoginSignUpEvents
    data class LoginSignUpUser(val room: AppDatabase) : LoginSignUpEvents
    object ClearToast : LoginSignUpEvents
    data class UpdateBookData(
        val bookList: List<BookListResponseModel>,
    ) : LoginSignUpEvents
}