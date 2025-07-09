package com.korikmat.transparentaccounts.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.korikmat.transparentaccounts.ui.theme.TransparentAccountsTheme
import com.korikmat.transparentaccounts.ui.viewModels.AccountsScreenViewModel
import com.korikmat.transparentaccounts.ui.viewModels.AccountsState
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AccountsScreen(
    vm: AccountsScreenViewModel = koinViewModel(),
    shared: SharedTransitionScope,
    visibility: AnimatedVisibilityScope,
    onAccountClick: (String) -> Unit = {},
) {
    val uiState: AccountsState by vm.state.collectAsStateWithLifecycle()

    val context = LocalContext.current
    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        when (uiState.loading) {
            true -> Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            false ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        with(shared) {
                            items(
                                items = uiState.accounts,
                                key = { "card-${it.accountNumber}" }
                            ) { account ->
                                AccountCard(
                                    modifier = Modifier
                                        .clickable { onAccountClick(account.accountNumber ?: "") }
                                        .sharedBounds(
                                            rememberSharedContentState(key = "card-${account.accountNumber}"),
                                            animatedVisibilityScope = visibility,
                                        ),
                                    accountNumber = account.accountNumber,
                                    note = account.note,
                                    name = account.name,
                                    balance = account.balance,
                                    currency = account.currency,
                                    description = account.description
                                )


                            }
                        }
                        item(key = "footer") {
                            when (uiState.nextLoading) {
                                true -> CircularProgressIndicator(
                                    modifier = Modifier.padding(8.dp),
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                false -> IconButton(onClick = vm::loadMoreAccounts) {
                                    Icon(
                                        Icons.Default.Add,
                                        contentDescription = "Load more accounts",
                                    )

                                }
                            }

                        }
                    }
                    AccountsNumberBox(
                        modifier = Modifier.align(Alignment.TopEnd),
                        accountsCount = uiState.accounts.size
                    )

                }
        }
    }
}

@Composable
fun AccountCard(
    modifier: Modifier = Modifier,
    accountNumber: String?,
    note: String?,
    name: String?,
    balance: Number?,
    currency: String?,
    description: String?
) {
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
            if (note.isNullOrEmpty().not()) {
                Text(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(bottom = 8.dp),
                    text = note ?: "-",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Light
                )
                Spacer(Modifier.height(8.dp))
            }

            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = name ?: "-",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
            )

            Spacer(Modifier.height(12.dp))

            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = balance?.let { "%,.2f ".format(it.toDouble()) } + (currency ?: ""),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.inversePrimary
            )
            Spacer(Modifier.height(8.dp))
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = description ?: "",
                style = MaterialTheme.typography.bodySmall,
            )

            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = accountNumber ?: "-",
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
fun AccountsNumberBox(modifier: Modifier = Modifier, accountsCount: Int) {
    Box(
        modifier = Modifier
            .padding(end = 16.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(16.dp),
            )
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(16.dp)
            )
            .then(modifier),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            text = "#$accountsCount",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview(
    showBackground = true,
)
@Composable
fun AccountCardPreview() {
    TransparentAccountsTheme(dynamicColor = false) {
        AccountCard(
            accountNumber = "1234567890",
            note = "This is a note",
            name = "John Doe",
            balance = 12345.67,
            currency = "CZK",
            description = "This is a description of the account.",
        )
    }
}
