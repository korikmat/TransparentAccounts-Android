package com.korikmat.data.source.dto


data class AccountsResponseDto(
    val accounts: List<TransparentAccountDto>,
    val pageNumber: Number,
    val pageSize: Number,
    val pageCount: Number,
    val nextPage: Number,
    val recordCount: Number
)

data class TransparentAccountDto(
    val accountNumber: String?,
    val bankCode: String?,
    val transparencyFrom: String?,
    val transparencyTo: String?,
    val publicationTo: String?,
    val actualizationDate: String?,
    val balance: Number,
    val currency: String?,
    val name: String?,
    val description: String?,
    val note: String?,
    val iban: String?,
)
