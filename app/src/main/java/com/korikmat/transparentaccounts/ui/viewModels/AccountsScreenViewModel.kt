package com.korikmat.transparentaccounts.ui.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.korikmat.domain.models.Account
import com.korikmat.domain.usecases.GetAccountsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AccountsState(
    val accounts: List<Account> = emptyList(),
    val loading: Boolean = false,
    val nextLoading: Boolean = false,
    val error: String? = null
)

class AccountsScreenViewModel(
    private val getAccountsUseCase: GetAccountsUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(AccountsState())

    val state: StateFlow<AccountsState> = _state

    private inline fun updateState(reducer: AccountsState.() -> AccountsState) {
        _state.update(reducer)
    }

    private var currentPage = 0

    init {
        viewModelScope.launch(Dispatchers.IO) {
            updateState { copy(loading = true, error = null) }

            try {
                val initialAccounts = getAccountsUseCase(currentPage)
                updateState { copy(accounts = initialAccounts, loading = false) }
            } catch (e: Exception) {
                Log.e("AccountVM", "Error", e)
                updateState { copy(loading = false, error = e.message ?: "Unknown error") }
            }
        }
    }

    fun loadMoreAccounts() {
        viewModelScope.launch(Dispatchers.IO) {
            updateState { copy(nextLoading = true) }

            currentPage++

            try {
                val newAccounts = getAccountsUseCase(currentPage)
                if (newAccounts.isEmpty()) {
                    return@launch
                }

                updateState {
                    copy(
                        accounts = _state.value.accounts + newAccounts,
                        loading = false,
                        nextLoading = false
                    )
                }
            } catch (e: Exception) {
                Log.e("AccountVM", "Error while loading more accounts", e)
                updateState { copy(nextLoading = false, error = e.message ?: "Unknown error") }
                return@launch
            }
        }
    }
}