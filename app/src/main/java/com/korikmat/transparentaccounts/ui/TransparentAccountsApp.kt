package com.korikmat.transparentaccounts.ui

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.korikmat.transparentaccounts.ui.navigation.NavigationScreens
import com.korikmat.transparentaccounts.ui.screens.AccountDetailsScreen
import com.korikmat.transparentaccounts.ui.screens.AccountsScreen
import com.korikmat.transparentaccounts.ui.theme.TransparentAccountsTheme

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun TransparentAccountsApp() {
    SharedTransitionLayout {
        val nav = rememberNavController()
        NavHost(
            nav,
            startDestination = NavigationScreens.Accounts.screenRoute,
        ) {
            composable(NavigationScreens.Accounts.screenRoute) {
                AccountsScreen(
                    shared = this@SharedTransitionLayout,
                    visibility = this@composable
                ) { id ->
                    nav.navigate(NavigationScreens.SelectedAccount.screenRoute + "/" + id) {
                        launchSingleTop = true
                    }
                }
            }

            composable(
                NavigationScreens.SelectedAccount.screenRoute + "/{id}",
                arguments = listOf(navArgument("id") { type = NavType.StringType })
            )
            { backStackEntry ->
                AccountDetailsScreen(
                    id = backStackEntry.arguments!!.getString("id", ""),
                    onBackClick = { nav.navigateUp() },
                    shared = this@SharedTransitionLayout,
                    visibility = this@composable
                )
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun TransparentAccountsAppPreview() {
    TransparentAccountsTheme(dynamicColor = false) {
        TransparentAccountsApp()
    }
}