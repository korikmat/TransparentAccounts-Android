package com.korikmat.transparentaccounts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.korikmat.transparentaccounts.ui.TransparentAccountsApp
import com.korikmat.transparentaccounts.ui.theme.TransparentAccountsTheme
import org.koin.androidx.compose.KoinAndroidContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KoinAndroidContext {
                TransparentAccountsTheme(dynamicColor = false) {
                    TransparentAccountsApp()
                }
            }
        }
    }
}

