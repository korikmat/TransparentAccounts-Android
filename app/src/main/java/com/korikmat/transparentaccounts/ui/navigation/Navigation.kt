package com.korikmat.transparentaccounts.ui.navigation

sealed class NavigationScreens(var screenRoute: String) {
    data object Accounts : NavigationScreens("accounts")
    data object SelectedAccount : NavigationScreens("selected_account")
}