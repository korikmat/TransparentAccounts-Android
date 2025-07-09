package com.korikmat.data.repositories

import android.util.Log
import com.korikmat.data.mappers.toDomain
import com.korikmat.data.source.RemoteAccountsSource
import com.korikmat.domain.models.Account
import com.korikmat.domain.models.AccountTransaction
import com.korikmat.domain.repositories.AccountsRepository
import retrofit2.HttpException

class AccountsRepositoryImpl(private val remoteAccountsSource: RemoteAccountsSource) :
    AccountsRepository {
    override suspend fun getAccountsByPage(page: Int): List<Account> {
        return try {
            val response = remoteAccountsSource.getAccounts(page)

            if (response.isSuccessful) {
                response.body()?.toDomain() ?: emptyList()
            } else {
                throw HttpException(response)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("AccountRepository", "Error: ${e.message}", e)
            throw e
        }
    }

    override suspend fun getAccountById(id: String): Account {
        return try {
            val response = remoteAccountsSource.getAccountById(id)

            if (response.isSuccessful) {
                response.body()?.toDomain()
                    ?: throw IllegalStateException("Response body is null")
            } else {
                throw HttpException(response)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("AccountRepository", "Error: ${e.message}", e)
            throw e
        }
    }

    override suspend fun getAccountTransactionsById(
        id: String,
        page: Int
    ): List<AccountTransaction> {
        return try {
            val response = remoteAccountsSource.getAccountTransactions(
                id = id,
                page = page
            )

            if (response.isSuccessful) {
                response.body()?.toDomain() ?: emptyList()
            } else {
                throw HttpException(response)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("AccountRepository", "Error: ${e.message}", e)
            throw e
        }
    }

}