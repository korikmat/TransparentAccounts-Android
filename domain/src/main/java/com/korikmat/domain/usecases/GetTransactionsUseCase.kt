package com.korikmat.domain.usecases

import com.korikmat.domain.repositories.AccountsRepository

class GetTransactionsUseCase(private val accountsRepository: AccountsRepository) {
    suspend operator fun invoke(id: String, page: Int) =
        accountsRepository.getAccountTransactionsById(id, page)
}