package com.korikmat.transparentaccounts.ui.viewModels

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.korikmat.domain.models.Account
import com.korikmat.domain.models.AccountTransaction
import com.korikmat.domain.usecases.GetAccountDetailsUseCase
import com.korikmat.domain.usecases.GetTransactionsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class AccountDetailsState(
    val account: Account = Account(),
    val transactions: List<AccountTransaction> = emptyList(),
    val loading: Boolean = false,
    val nextLoading: Boolean = false,
    val error: String? = null
)

class AccountDetailsScreenViewModel(
    private val getAccountDetailsUseCase: GetAccountDetailsUseCase,
    private val getTransactionsUseCase: GetTransactionsUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _state = MutableStateFlow(AccountDetailsState())
    val state: StateFlow<AccountDetailsState> = _state

    private inline fun updateState(reducer: AccountDetailsState.() -> AccountDetailsState) {
        _state.update(reducer)
    }

    private val accountId: String = checkNotNull(savedStateHandle["id"])
    private var currentPage: Int = 0

    init {
        viewModelScope.launch {
            updateState { copy(loading = true, error = null) }

            try {
                val (account, transactions) = withContext(Dispatchers.IO) {
                    coroutineScope {
                        val accDef = async { getAccountDetailsUseCase(accountId) }
                        val tranDef =
                            async { getTransactionsUseCase(accountId, page = currentPage) }
                        accDef.await() to tranDef.await()
                    }
                }

                updateState { copy(account = account) }
                updateState { copy(transactions = transactions, loading = false) }

            } catch (e: Exception) {
                Log.e("AccountDetailsVM", "Error", e)
                updateState { copy(error = e.message ?: "Unknown error", loading = false) }
            }
        }
    }


    fun loadMoreTransactions() {
        viewModelScope.launch(Dispatchers.IO) {
            updateState { copy(nextLoading = true) }

            currentPage++
            try {
                val newTransaction = getTransactionsUseCase(accountId, currentPage)
                if (newTransaction.isEmpty()) {
                    updateState { copy(nextLoading = false) }
                    return@launch
                }

                updateState {
                    copy(
                        transactions = _state.value.transactions + newTransaction,
                        loading = false,
                        nextLoading = false
                    )
                }
            } catch (e: Exception) {
                Log.e("AccountDetailsVM", "Error loading more transactions", e)
                updateState { copy(error = e.message ?: "Unknown error", nextLoading = false) }
                return@launch
            }
        }
    }
}