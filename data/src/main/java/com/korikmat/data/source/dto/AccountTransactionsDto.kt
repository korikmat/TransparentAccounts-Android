package com.korikmat.data.source.dto

data class AccountTransactionsResponseDto(
    val pageNumber: Number,
    val pageSize: Number,
    val pageCount: Number,
    val nextPage: Number,
    val recordCount: Number,
    val transactions: List<AccountTransactionsDto>,
)

data class AccountTransactionsDto(
    val amount: AmountDto,
    val type: String,
    val dueDate: String,
    val processingDate: String,
    val sender: PartyDto,
    val receiver: PartyDto,
    val typeDescription: String
)

data class AmountDto(
    val value: Number,
    val precision: Number,
    val currency: String
)

data class PartyDto(
    val accountNumber: String,
    val bankCode: String,
    val iban: String,
    val variableSymbol: String? = null,
    val specificSymbol: String? = null,
    val specificSymbolParty: String? = null,
    val constantSymbol: String? = null,
    val description: String? = null
)