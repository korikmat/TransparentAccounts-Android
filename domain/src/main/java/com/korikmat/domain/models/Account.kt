package com.korikmat.domain.models

data class Account(
    val accountNumber: String? = null,
    val bankCode: String? = null,
    val transparencyFrom: String? = null,
    val transparencyTo: String? = null,
    val publicationTo: String? = null,
    val actualizationDate: String? = null,
    val balance: Number? = null,
    val currency: String? = null,
    val name: String? = null,
    val description: String? = null,
    val note: String? = null,
    val iban: String? = null,
)
