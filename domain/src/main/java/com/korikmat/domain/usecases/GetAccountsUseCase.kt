package com.korikmat.domain.usecases

import com.korikmat.domain.repositories.AccountsRepository

class GetAccountsUseCase(private val accountsRepository: AccountsRepository) {
    suspend operator fun invoke(page: Int) = accountsRepository.getAccountsByPage(page)
}