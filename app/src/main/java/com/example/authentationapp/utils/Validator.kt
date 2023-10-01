package com.example.authentationapp.utils

import com.example.authentationapp.R

object Validator {

    fun String.validateName(): Pair<Boolean, Int?> {
        return if (this.length >= 2) Pair(true, null)
        else Pair(false, R.string.name_invalid)
    }

    fun String.validatePassword(): Pair<Boolean, Int?> {
        val regex = Regex("^(?=.*[0-9])(?=.*[!@#\$%&()])(?=.*[a-z])(?=.*[A-Z]).{8,}\$")
        return if (this.matches(regex)) Pair(true, null)
        else Pair(false, R.string.password_invalid)
    }
}