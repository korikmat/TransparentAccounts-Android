package com.korikmat.transparentaccounts.ui.screens

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.korikmat.transparentaccounts.ui.theme.TransparentAccountsTheme


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AccountsScreen(
    shared: SharedTransitionScope,
    visibility: AnimatedVisibilityScope,
    onAccountClick: (Int) -> Unit = {},
) {
    val accounts = listOf(
        1, 2, 3, 4, 5, 6, 7, 8, 9, 10
    )
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),

            ) {
            with(shared) {
                items(accounts.size) { index ->
                    AccountCard(
                        modifier = Modifier
                            .clickable { onAccountClick(index) }
                            .sharedBounds(
                                rememberSharedContentState(key = "card-$index"),
                                animatedVisibilityScope = visibility
                            ))
                }
            }
        }
    }

}

@Composable
fun AccountCard(modifier: Modifier = Modifier) {
    Card(
        modifier = Modifier
            .then(modifier),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                modifier = Modifier.align(Alignment.End),
                text = "Příjmový účet",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Light
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "SVAZEK OBCÍ - REGION DOLNÍ BEROUNKA",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
            )

            Spacer(Modifier.height(12.dp))

            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = "%,.2f CZK".format(1063961.87),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFEC8D00)
            )

            Spacer(Modifier.height(8.dp))

            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = "sbírka pro Nepál",
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun AccountsScreenPreview() {
    TransparentAccountsTheme(dynamicColor = false) {
//        AccountsScreen()
    }
}