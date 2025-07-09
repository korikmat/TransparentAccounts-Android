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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.korikmat.domain.models.Account
import com.korikmat.domain.models.AccountTransaction
import com.korikmat.transparentaccounts.R
import com.korikmat.transparentaccounts.ui.theme.TransparentAccountsTheme
import com.korikmat.transparentaccounts.ui.viewModels.AccountDetailsScreenViewModel
import com.korikmat.transparentaccounts.ui.viewModels.AccountDetailsState
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AccountDetailsScreen(
    id: String = "",
    vm: AccountDetailsScreenViewModel = koinViewModel(),
    onBackClick: () -> Unit = {},
    shared: SharedTransitionScope,
    visibility: AnimatedVisibilityScope
) {
    val uiState: AccountDetailsState by vm.state.collectAsStateWithLifecycle()

    val context = LocalContext.current
    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }


    with(shared) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .sharedBounds(
                    rememberSharedContentState(key = "card-$id"),
                    animatedVisibilityScope = visibility
                ),
        ) {
            AccountDetailsContent(
                uiState.account,
                uiState.transactions,
                uiState.loading,
                uiState.nextLoading,
                onLoadMoreTransactionsClick = vm::loadMoreTransactions,
                onBackClick = onBackClick
            )
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountDetailsContent(
    account: Account,
    transactions: List<AccountTransaction>,
    loading: Boolean,
    nextLoading: Boolean = false,
    onBackClick: () -> Unit = {},
    onLoadMoreTransactionsClick: () -> Unit = {}
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = MaterialTheme.colorScheme.primary
                ),
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
        containerColor = Color.Transparent,
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
            when (loading) {
                true -> Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                }

                false ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 24.dp, bottom = 0.dp, start = 16.dp, end = 16.dp),

                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top,
                    ) {
                        if (account.note.isNullOrEmpty().not()) {
                            Text(
                                modifier = Modifier
                                    .align(Alignment.End)
                                    .padding(bottom = 8.dp),
                                text = account.note ?: "-",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Light
                            )
                            Spacer(Modifier.height(8.dp))
                        }

                        Text(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            text = account.name ?: "-",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                        )

                        Spacer(Modifier.height(12.dp))

                        Text(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            text = account.balance?.let { "%,.2f ".format(it.toDouble()) } + (account.currency
                                ?: ""),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.inversePrimary
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            text = account.description ?: "",
                            style = MaterialTheme.typography.bodySmall,
                        )
                        Spacer(Modifier.height(8.dp))

                        val clipboard = LocalClipboardManager.current
                        val context = LocalContext.current
                        Text(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable {
                                    clipboard.setText(AnnotatedString(account.accountNumber ?: "-"))
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.account_number_copied_to_clipboard),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                            text = account.accountNumber ?: "-",
                            style = MaterialTheme.typography.headlineSmall,
                        )
                        Spacer(Modifier.height(8.dp))

                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            items(
                                items = transactions,
                            ) { transaction ->
                                TransactionItem(
                                    dueDate = transaction.dueDate,
                                    value = transaction.value,
                                    currency = transaction.currency,
                                    typeDescription = transaction.typeDescription,
                                    description = transaction.description,
                                    senderAccountNum = transaction.senderAccountNum,
                                    receiverAccountNum = transaction.receiverAccountNum,
                                    constantSymbol = transaction.constantSymbol,
                                    variableSymbol = transaction.variableSymbol,
                                    specificSymbol = transaction.specificSymbol,
                                )
                            }
                            item(key = "footer") {
                                when (nextLoading) {
                                    true ->
                                        CircularProgressIndicator(
                                            modifier = Modifier.padding(12.dp),
                                            color = MaterialTheme.colorScheme.onPrimary
                                        )

                                    false -> IconButton(
                                        modifier = Modifier.padding(12.dp),
                                        onClick = onLoadMoreTransactionsClick
                                    ) {
                                        Icon(
                                            Icons.Default.Add,
                                            contentDescription = "Load more transactions",
                                        )
                                    }
                                }

                            }
                        }
                    }
            }
        }
    }
}

@Composable
fun TransactionItem(
    modifier: Modifier = Modifier,
    dueDate: String?,
    value: Number?,
    currency: String?,
    typeDescription: String,
    description: String? = null,
    senderAccountNum: String?,
    receiverAccountNum: String?,
    constantSymbol: String?,
    variableSymbol: String?,
    specificSymbol: String?,
) {
    Box(
        modifier = Modifier
            .then(modifier)
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 4.dp, start = 8.dp, end = 8.dp)
            .shadow(15.dp, shape = RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)

    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = dueDate ?: "-",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.align(Alignment.End),

                    )


                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = typeDescription,
                        style = MaterialTheme.typography.titleLarge,
                    )
                    Text(
                        text = description ?: "-",
                        style = MaterialTheme.typography.titleLarge,
                    )

                    Text(
                        text = "$value $currency",
                        color = MaterialTheme.colorScheme.inversePrimary,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.titleLarge,

                        )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .shadow(10.dp, shape = RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer),
                ) {

                    Column(
                        modifier = Modifier
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,

                        ) {

                        Text(
                            text = senderAccountNum ?: "-",
                            textAlign = TextAlign.End,
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Arrow",
                            modifier = Modifier.size(25.dp),
                            tint = MaterialTheme.colorScheme.inversePrimary,
                        )
                        Text(
                            text = receiverAccountNum ?: "-",
                            textAlign = TextAlign.Start,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "Variable symbol: " + (variableSymbol ?: "-"),
                    )
                    Text(
                        text = "Constant symbol: " + (constantSymbol ?: "-"),
                    )
                    Text(
                        text = "Specific symbol: " + (specificSymbol ?: "-"),
                    )
                }

            }
        }
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TransactionItemPreview() {
    TransparentAccountsTheme(dynamicColor = false) {
        Surface(contentColor = MaterialTheme.colorScheme.onPrimary) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primary),

                ) {
                TransactionItem(
                    modifier = Modifier.align(Alignment.Center),
                    "2023-10-01",
                    1234.56,
                    "CZK",
                    "Payment for services",
                    "Payment for electricity bill",
                    "000000-1234567890",
                    "000000-0987654321",
                    "1234",
                    "5678",
                    "91011",
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AccountDetailsScreenPreview() {
    TransparentAccountsTheme(dynamicColor = false) {
        val account = Account(
            accountNumber = "000000-2906478309",
            bankCode = "0800",
            transparencyFrom = "2016-08-18T00:00:00",
            transparencyTo = "3000-01-01T00:00:00",
            publicationTo = "3000-01-01T00:00:00",
            actualizationDate = "2016-09-07T10:00:06",
            balance = 1063961.87,
            currency = "CZK",
            name = "SVAZEK OBCÍ - REGION DOLNÍ BEROUNKA",
            description = "sbírka pro Nepál",
            note = "Příjmový účet",
            iban = "CZ13 0800 0000 0029 0647 8309"
        )
        val transactions = emptyList<AccountTransaction>()
        AccountDetailsContent(account, transactions, false)
    }
}