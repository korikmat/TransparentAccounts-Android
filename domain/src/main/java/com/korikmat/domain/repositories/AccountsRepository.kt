package com.korikmat.domain.repositories

import com.korikmat.domain.models.Account
import com.korikmat.domain.models.AccountTransaction

interface AccountsRepository {
    suspend fun getAccountsByPage(page: Int): List<Account>
    suspend fun getAccountById(id: String): Account
    suspend fun getAccountTransactionsById(id: String, page: Int): List<AccountTransaction>
}