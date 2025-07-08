package com.korikmat.transparentaccounts.ui.screens

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.korikmat.transparentaccounts.ui.theme.TransparentAccountsTheme

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AccountDetailsScreen(
    id: Int = 0,
    onBackClick: () -> Unit = {},
    shared: SharedTransitionScope,
    visibility: AnimatedVisibilityScope
) {
    with(shared) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .sharedBounds(
                    rememberSharedContentState(key = "card-$id"),
                    animatedVisibilityScope = visibility
                ),
        ) {
            AccountDetailsContent(onBackClick = onBackClick)
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountDetailsContent(onBackClick: () -> Unit = {}) {
    val transactions = listOf(
        0,
        1,
        2,
        3,
        4,
        5,
        6,
        7,
        8,
        9,
        10,
        11,
        12,
        13,
        14,
        15,
        16,
        17,
        18,
        19,
        20,
        21,
        22,
        23,
        24,
        25,
        26,
        27,
        28,
        29,
        30,
        31,
        32,
        33,
        34,
        35,
        36,
        37,
    )
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
                scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
            )
        },
        contentColor = MaterialTheme.colorScheme.onPrimary
    ) { inner ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(5.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(MaterialTheme.colorScheme.primary)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 24.dp, bottom = 0.dp, start = 16.dp, end = 16.dp),

                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
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
                Spacer(Modifier.height(8.dp))

                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(transactions.size) { index ->
                        TransactionItem()
                    }

                }
            }
        }
    }
}

@Composable
fun TransactionItem(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .then(modifier)
            .shadow(5.dp, shape = RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.primary)

    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "30.06.2025"
            )
            Text(
                text = "EVA JIRKOVÁ"
            )
            Text(
                text = "CZK"
            )
            Text(
                text = "0598"
            )
            Text(
                text = "2,49 Kč"
            )
        }
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TransactionItemPreview() {
    TransparentAccountsTheme(dynamicColor = false) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            TransactionItem(modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AccountDetailsScreenPreview() {
    TransparentAccountsTheme(dynamicColor = false) {
        AccountDetailsContent()
    }
}