package com.korikmat.data.mappers

import com.korikmat.data.source.dto.AccountTransactionsDto
import com.korikmat.data.source.dto.AccountTransactionsResponseDto
import com.korikmat.data.source.dto.AccountsResponseDto
import com.korikmat.data.source.dto.TransparentAccountDto
import com.korikmat.domain.models.Account
import com.korikmat.domain.models.AccountTransaction
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun AccountsResponseDto.toDomain(): List<Account> {
    return this.accounts.map { it.toDomain() }
}

fun TransparentAccountDto.toDomain(): Account {
    return Account(
        accountNumber = this.accountNumber,
        bankCode = this.bankCode,
        transparencyFrom = this.transparencyFrom,
        transparencyTo = this.transparencyTo,
        publicationTo = this.publicationTo,
        actualizationDate = this.actualizationDate,
        balance = this.balance,
        currency = this.currency,
        name = this.name,
        description = this.description,
        note = this.note,
        iban = this.iban,
    )
}

fun AccountTransactionsResponseDto.toDomain(): List<AccountTransaction> {
    return this.transactions.map { it.toDomain() }
}

fun AccountTransactionsDto.toDomain(): AccountTransaction {
    return AccountTransaction(
        value = this.amount.value,
        precision = this.amount.precision,
        currency = this.amount.currency,
        type = this.type,
        dueDate = LocalDateTime.parse(this.dueDate)
            .format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
        processedDate = LocalDateTime.parse(this.processingDate)
            .format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
        description = this.sender.description,
        typeDescription = this.typeDescription,
        senderAccountNum = this.sender.accountNumber,
        receiverAccountNum = this.receiver.accountNumber,
        constantSymbol = this.sender.constantSymbol,
        variableSymbol = this.sender.variableSymbol,
        specificSymbol = this.sender.specificSymbol,
        bankCode = this.sender.bankCode,
    )
}