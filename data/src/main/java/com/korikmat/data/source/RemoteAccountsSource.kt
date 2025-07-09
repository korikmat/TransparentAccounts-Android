package com.korikmat.data.source

class RemoteAccountsSource(private val apiService: EDPApiService, private val apiKey: String) {
    suspend fun getAccounts(page: Int) = apiService.getAccounts(apiKey, page)

    suspend fun getAccountById(id: String) = apiService.getAccountById(apiKey, id)

    suspend fun getAccountTransactions(id: String, page: Int) =
        apiService.getAccountTransactions(apiKey, id, page)
}