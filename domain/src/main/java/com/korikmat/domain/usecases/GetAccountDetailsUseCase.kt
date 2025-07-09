package com.korikmat.domain.usecases

import com.korikmat.domain.repositories.AccountsRepository

class GetAccountDetailsUseCase(private val accountsRepository: AccountsRepository) {
    suspend operator fun invoke(id: String) = accountsRepository.getAccountById(id)
}