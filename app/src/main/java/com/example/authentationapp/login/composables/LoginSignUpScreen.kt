package com.example.authentationapp.login.composables

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.authentationapp.R
import com.example.authentationapp.login.directions.LoginSignUpDirections
import com.example.authentationapp.login.models.CountryModel
import com.example.authentationapp.login.viewmodels.LoginSignUpEvents
import com.example.authentationapp.room.AppDatabase
import com.example.authentationapp.ui.theme.darkBlue
import com.example.authentationapp.ui.theme.emeraldGreen
import com.example.authentationapp.utils.ButtonComposable
import com.example.authentationapp.utils.CustomLoader
import com.example.authentationapp.utils.GenericTextField
import com.example.authentationapp.utils.LabelComposable

@Composable
fun LoginSignUpScreen(
    loginUser: Boolean,
    password: String,
    room: AppDatabase,
    loginToast: Int?,
    name: String,
    btnEnabled: Boolean,
    nameError: Int?,
    passwordError: Int?,
    countriesApiError: Int?,
    currentSelectedCountry: CountryModel?,
    countryList: List<CountryModel?>?,
    setEvents: (LoginSignUpEvents) -> Unit,
    navigate: (LoginSignUpDirections) -> Unit,
) {
    val context = LocalContext.current
    if (loginToast != null) {
        LaunchedEffect(key1 = Unit, block = {
            Toast.makeText(context, context.getText(loginToast), Toast.LENGTH_LONG).show()
            setEvents(LoginSignUpEvents.ClearToast)
        })
    }
    if (loginUser) {
        LaunchedEffect(key1 = Unit, block = {
            navigate(LoginSignUpDirections.BookList)
        })
    }
    if (countriesApiError != null) {
        LaunchedEffect(key1 = Unit, block = {
            Toast.makeText(context, context.getText(countriesApiError), Toast.LENGTH_LONG)
                .show()
        })
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(
                brush = Brush.verticalGradient(
                    listOf(darkBlue, emeraldGreen)
                )
            )
            .padding(horizontal = 28.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.welcome_to_bookstore),
            style = MaterialTheme.typography.displayLarge.copy(
                color = Color.Yellow,
                textAlign = TextAlign.Center
            )
        )
        DropDownComposable(
            modifier = Modifier.padding(vertical = 20.dp),
            currentSelected = currentSelectedCountry,
            countryList = countryList,
            setEvents = setEvents
        )
        GenericTextField(
            value = name,
            error = nameError,
            label = { LabelComposable(text = stringResource(id = R.string.name)) },
            modifier = Modifier.padding(vertical = 12.dp),
            onValueChange = {
                setEvents(LoginSignUpEvents.EditName(it))
            })
        GenericTextField(
            value = password,
            error = passwordError,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
            label = { LabelComposable(text = stringResource(id = R.string.password)) },
            onValueChange = {
                setEvents(LoginSignUpEvents.EditPassword(it))
            })
        AnimatedVisibility(visible = btnEnabled, modifier = Modifier.padding(vertical = 20.dp)) {
            ButtonComposable(modifier = Modifier.padding(horizontal = 40.dp)) {
                setEvents(LoginSignUpEvents.LoginSignUpUser(room = room))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropDownComposable(
    modifier: Modifier,
    currentSelected: CountryModel?,
    countryList: List<CountryModel?>?,
    setEvents: (LoginSignUpEvents) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        TextField(
            value = currentSelected?.country ?: stringResource(id = R.string.select_a_country),
            onValueChange = {},
            readOnly = true,
            modifier = Modifier.menuAnchor(),
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.White,
                containerColor = Color.Transparent,
                unfocusedIndicatorColor = Color.White,
                focusedIndicatorColor = Color.White
            ),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.rotate(if (expanded) 180f else 0f)
                )
            }
        )
        ExposedDropdownMenu(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            if (!countryList.isNullOrEmpty()) {
                countryList.forEach { item ->
                    item?.country?.let {
                        DropdownMenuItem(
                            colors = MenuDefaults.itemColors(
                                disabledTextColor = Color.Gray,
                                textColor = Color.White,
                            ),
                            text = {
                                Text(
                                    text = it,
                                    modifier = Modifier.fillMaxWidth(),
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        color = Color.Black,
                                        textAlign = TextAlign.Center
                                    )
                                )
                            },
                            onClick = {
                                setEvents(LoginSignUpEvents.SelectCountry(item))
                                expanded = false
                            }
                        )
                    }
                }
            } else {
                DropdownMenuItem(
                    modifier = Modifier.fillMaxWidth(),
                    colors = MenuDefaults.itemColors(
                        disabledTextColor = Color.Gray,
                        textColor = Color.Black,
                    ),
                    text = {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (countryList == null) {
                                CustomLoader()
                            } else {
                                Text(
                                    text = stringResource(id = R.string.no_items_found),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    },
                    onClick = {}
                )
            }
        }
    }
}