package com.korikmat.transparentaccounts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.korikmat.transparentaccounts.ui.TransparentAccountsApp
import com.korikmat.transparentaccounts.ui.theme.TransparentAccountsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TransparentAccountsTheme(dynamicColor = false) {
                TransparentAccountsApp()
            }
        }
    }
}

