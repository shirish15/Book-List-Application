package com.example.authentationapp.utils

import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController

fun Fragment.navigateForward(destination: NavDirections) = view?.post {
    if (isAdded) {
        with(findNavController(this)) {
            currentDestination?.getAction(destination.actionId)?.let { navigate(destination) }
        }
    }
}

fun Fragment.navigateBack() {
    findNavController(this).popBackStack()
}